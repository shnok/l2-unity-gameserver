package com.shnok.javaserver.gameserver.model.actor.attack;

import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.GaugeColor;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.container.creature.ChanceSkillList;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.Attack;
import com.shnok.javaserver.gameserver.network.serverpackets.SetupGauge;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.Formulas;

/**
 * This class groups all attack data related to a {@link Creature}.
 * @param <T> : The {@link Creature} used as actor.
 */
public class CreatureAttack<T extends Creature>
{
	public static final CLogger LOGGER = new CLogger(CreatureAttack.class.getName());
	
	protected final T _actor;
	
	private boolean _isAttackingNow;
	private boolean _isBowCoolingDown;
	private HitHolder[] _hitHolders;
	private WeaponType _weaponType;
	private int _afterAttackDelay;
	private boolean _isHit;
	private boolean _isInHitAnimation;
	
	private ScheduledFuture<?> _attackTask;
	
	public CreatureAttack(T actor)
	{
		_actor = actor;
	}
	
	public boolean isAttackingNow()
	{
		return _isAttackingNow;
	}
	
	public boolean isBowCoolingDown()
	{
		return _isBowCoolingDown;
	}
	
	public boolean isInHitAnimation()
	{
		return _isInHitAnimation;
	}
	
	/**
	 * @param target : The {@link Creature} target to test.
	 * @return True if this {@link Creature} owner can attack the {@link Creature} target set as parameter, or false otherwise.
	 */
	public boolean canAttack(Creature target)
	{
		if (_actor.isAttackingDisabled())
			return false;
		
		if (_actor.isMovementDisabled() && !_actor.isIn2DRadius(target, (int) (_actor.getStatus().getPhysicalAttackRange() + _actor.getCollisionRadius() + target.getCollisionRadius() + (target.isMoving() ? 60 : 10))))
			return false;
		
		if (!_actor.knows(target) || !target.isAttackableBy(_actor))
			return false;
		
		if (!GeoEngine.getInstance().canSeeTarget(_actor, target))
		{
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANT_SEE_TARGET));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Manage hit process (called by Hit Task).<BR>
	 * <BR>
	 * <B><U> Actions</U> :</B>
	 * <ul>
	 * <li>If the attacker/target is dead or use fake death, notify the AI with EVT_CANCEL and send ActionFailed (if attacker is a Player)</li>
	 * <li>If attack isn't aborted, send a message system (critical hit, missed...) to attacker/target if they are Player</li>
	 * <li>If attack isn't aborted and hit isn't missed, reduce HP of the target and calculate reflection damage to reduce HP of attacker if necessary</li>
	 * <li>if attack isn't aborted and hit isn't missed, manage attack or cast break of the target (calculating rate, sending message...)</li>
	 * </ul>
	 */
	private final void onHitTimer()
	{
		// Content was cleaned meantime, simply return doing nothing.
		if (!isAttackingNow())
			return;
			
		// Something happens to the target between the attacker attacking and the actual damage being dealt.
		// There is no PEACE zone check here. If the attack starts outside and in the meantime the mainTarget walks into a PEACE zone, it gets hit.
		final Creature mainTarget = _hitHolders[0]._target;
		if (!_actor.knows(mainTarget) || mainTarget.isDead())
		{
			stop();
			return;
		}
		
		// Test curses. Prevents messing up drop calculation.
		if (_actor instanceof Playable && mainTarget.isRaidRelated() && _actor.testCursesOnAttack((Npc) mainTarget))
		{
			stop();
			return;
		}
		
		final Player player = _actor.getActingPlayer();
		
		// Player can't flag if attacking his Summon, and vice-versa.
		if (player != null && player.getSummon() != mainTarget && !(player.getSummon() == _actor && mainTarget == player))
			player.updatePvPStatus(mainTarget);
		
		if (_isHit)
		{
			// Discharge soulshot.
			_actor.setChargedShot(ShotType.SOULSHOT, false);
			
			// If hit by a CW or by an hero while holding a CW, CP are reduced to 0.
			if (_actor instanceof Player actorPlayer && mainTarget instanceof Player targetPlayer && !targetPlayer.isInvul() && (actorPlayer.isCursedWeaponEquipped() || (actorPlayer.isHero() && targetPlayer.isCursedWeaponEquipped())))
				targetPlayer.getStatus().setCp(0);
		}
		
		if (_actor instanceof Playable)
			_actor.rechargeShots(true, false);
		
		switch (_weaponType)
		{
			case BOW:
				doHit(_hitHolders[0]);
				
				_isBowCoolingDown = true;
				
				_attackTask = ThreadPool.schedule(() ->
				{
					_isBowCoolingDown = false;
					_actor.getAI().notifyEvent(AiEventType.BOW_ATTACK_REUSED, null, null);
					
				}, _afterAttackDelay);
				
				onFinishedAttackBow(mainTarget);
				break;
			
			case DUAL, DUALFIST:
				doHit(_hitHolders[0]);
				
				_attackTask = ThreadPool.schedule(() ->
				{
					// Content was cleaned meantime, simply return doing nothing.
					if (!isAttackingNow())
						return;
					
					doHit(_hitHolders[1]);
					
					_attackTask = ThreadPool.schedule(() -> onFinishedAttack(mainTarget), _afterAttackDelay);
				}, _afterAttackDelay);
				break;
			
			case POLE:
				for (HitHolder hitHolder : _hitHolders)
					doHit(hitHolder);
				
				_attackTask = ThreadPool.schedule(() -> onFinishedAttack(mainTarget), _afterAttackDelay);
				break;
			
			default:
				doHit(_hitHolders[0]);
				
				_attackTask = ThreadPool.schedule(() -> onFinishedAttack(mainTarget), _afterAttackDelay);
				break;
		}
		
		ThreadPool.schedule(() ->
		{
			_isInHitAnimation = false;
			
			if (_actor instanceof Npc npc)
				npc.getAI().runAI(false);
			
		}, 300);
	}
	
	protected void onFinishedAttackBow(Creature mainTarget)
	{
		clearAttackTask(false);
		
		if (_actor instanceof Npc npc)
			npc.getAI().runAI(false);
		
		_actor.getAI().notifyEvent(AiEventType.FINISHED_ATTACK_BOW, null, null);
	}
	
	protected void onFinishedAttack(Creature mainTarget)
	{
		clearAttackTask(false);
		
		if (_actor instanceof Npc npc)
			npc.getAI().runAI(false);
		
		_actor.getAI().notifyEvent(AiEventType.FINISHED_ATTACK, null, null);
	}
	
	private void doHit(HitHolder hitHolder)
	{
		final Creature target = hitHolder._target;
		if (hitHolder._miss)
		{
			target.getAI().notifyEvent(AiEventType.EVADED, _actor, null);
			
			if (target instanceof Player)
				target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(_actor));
		}
		
		_actor.sendDamageMessage(target, hitHolder._damage, false, hitHolder._crit, hitHolder._miss);
		
		if (!hitHolder._miss && hitHolder._damage > 0)
		{
			_actor.getAI().startAttackStance();
			
			target.getAI().notifyEvent(AiEventType.ATTACKED, _actor, null);
			
			int reflectedDamage = 0;
			
			// Reflect damage system - do not reflect if weapon is a bow or target is invulnerable
			if (_weaponType != WeaponType.BOW && !target.isInvul())
			{
				// quick fix for no drop from raid if boss attack high-level char with damage reflection
				if (!target.isRaidRelated() || _actor.getActingPlayer() == null || _actor.getActingPlayer().getStatus().getLevel() <= target.getStatus().getLevel() + 8)
				{
					// Calculate reflection damage to reduce HP of attacker if necessary
					final double reflectPercent = target.getStatus().calcStat(Stats.REFLECT_DAMAGE_PERCENT, 0, null, null);
					if (reflectPercent > 0)
					{
						reflectedDamage = (int) (reflectPercent / 100. * hitHolder._damage);
						
						if (reflectedDamage > target.getStatus().getMaxHp())
							reflectedDamage = target.getStatus().getMaxHp();
					}
				}
			}
			
			// Reduce target HPs
			target.reduceCurrentHp(hitHolder._damage, _actor, null);
			
			// Reduce attacker HPs in case of a reflect.
			if (reflectedDamage > 0)
				_actor.reduceCurrentHp(reflectedDamage, target, true, false, null);
			
			// Calculate the absorbed HP percentage. Do not absorb if weapon is a bow.
			if (_weaponType != WeaponType.BOW)
			{
				final double absorbPercent = _actor.getStatus().calcStat(Stats.ABSORB_DAMAGE_PERCENT, 0, null, null);
				if (absorbPercent > 0)
					_actor.getStatus().addHp(absorbPercent / 100. * hitHolder._damage);
			}
			
			// Manage cast break of the target (calculating rate, sending message...)
			Formulas.calcCastBreak(target, hitHolder._damage);
			
			// Maybe launch chance skills on us
			final ChanceSkillList chanceSkills = _actor.getChanceSkills();
			if (chanceSkills != null)
			{
				chanceSkills.onTargetHit(target, hitHolder._crit);
				
				// Reflect triggers onHit
				if (reflectedDamage > 0)
					chanceSkills.onSelfHit(target);
			}
			
			// Maybe launch chance skills on target
			if (target.getChanceSkills() != null)
				target.getChanceSkills().onSelfHit(_actor);
			
			// Launch weapon Special ability effect if available
			if (hitHolder._crit)
			{
				final Weapon activeWeapon = _actor.getActiveWeaponItem();
				if (activeWeapon != null)
					activeWeapon.castSkillOnCrit(_actor, target);
			}
		}
	}
	
	/**
	 * Launch a physical attack against a {@link Creature}.
	 * @param target : The {@link Creature} used as target.
	 */
	public void doAttack(Creature target)
	{
		final int timeAtk = Formulas.calculateTimeBetweenAttacks(_actor);
		final Weapon weaponItem = _actor.getActiveWeaponItem();
		final boolean isSoulshot = _actor.isChargedShot(ShotType.SOULSHOT);
		
		_actor.getPosition().setHeadingTo(target);
		
		HitHolder[] hits;
		
		switch (_actor.getAttackType())
		{
			case BOW:
				hits = doAttackHitByBow(target, weaponItem, timeAtk, isSoulshot);
				break;
			
			case DUAL, DUALFIST:
				hits = doAttackHitByDual(target, weaponItem, timeAtk / 2, isSoulshot);
				break;
			
			case POLE:
				hits = doAttackHitByPole(target, weaponItem, timeAtk / 2, isSoulshot);
				break;
			
			default:
				hits = doAttackHitSimple(target, weaponItem, timeAtk / 2, isSoulshot);
				break;
		}
		
		if (hits != null)
			_actor.broadcastPacket(new Attack(_actor, hits));
	}
	
	/**
	 * Launch a Bow attack.
	 * @param weapon : The used {@link Weapon}.
	 * @param target : The targeted {@link Creature}.
	 * @param sAtk : The Attack Speed of the attacker.
	 * @param isSoulshot : True if a soulshot was charged, false otherwise.
	 * @return An array of generated {@link HitHolder}s.
	 */
	private HitHolder[] doAttackHitByBow(Creature target, Weapon weapon, int sAtk, boolean isSoulshot)
	{
		_actor.reduceArrowCount();
		_actor.getStatus().reduceMp(_actor.getActiveWeaponItem().getMpConsume());
		
		final HitHolder[] hits = new HitHolder[]
		{
			getHitHolder(target, isSoulshot, false)
		};
		
		int reuse = weapon.getReuseDelay();
		if (reuse != 0)
			reuse = (reuse * 345) / _actor.getStatus().getPAtkSpd();
		
		setAttackTask(hits, weapon, reuse, isSoulshot);
		
		_attackTask = ThreadPool.schedule(this::onHitTimer, sAtk);
		
		if (_actor instanceof Player)
		{
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.GETTING_READY_TO_SHOOT_AN_ARROW));
			_actor.sendPacket(new SetupGauge(GaugeColor.RED, sAtk + reuse));
		}
		return hits;
	}
	
	/**
	 * Launch a Dual wield attack.
	 * @param target : The targeted {@link Creature}.
	 * @param weapon : The used {@link Weapon}.
	 * @param sAtk : The Attack Speed of the attacker.
	 * @param isSoulshot : True if a soulshot was charged, false otherwise.
	 * @return An array of generated {@link HitHolder}s.
	 */
	private HitHolder[] doAttackHitByDual(Creature target, Weapon weapon, int sAtk, boolean isSoulshot)
	{
		final HitHolder[] hits = new HitHolder[]
		{
			getHitHolder(target, isSoulshot, true),
			getHitHolder(target, isSoulshot, true)
		};
		
		setAttackTask(hits, weapon, sAtk / 2, isSoulshot);
		
		_attackTask = ThreadPool.schedule(this::onHitTimer, sAtk / 2);
		
		return hits;
	}
	
	/**
	 * Launch a Pole attack.
	 * @param target : The targeted {@link Creature}.
	 * @param weapon : The used {@link Weapon}.
	 * @param sAtk : The Attack Speed of the attacker.
	 * @param isSoulshot : True if a soulshot was charged, false otherwise.
	 * @return An array of generated {@link HitHolder}s.
	 */
	private HitHolder[] doAttackHitByPole(Creature target, Weapon weapon, int sAtk, boolean isSoulshot)
	{
		final ArrayList<HitHolder> hitHolders = new ArrayList<>();
		hitHolders.add(getHitHolder(target, isSoulshot, false));
		
		final int maxAttackedCount;
		if (_actor.getFirstEffect(EffectType.POLEARM_TARGET_SINGLE) != null)
			maxAttackedCount = 1;
		else
			maxAttackedCount = (int) _actor.getStatus().calcStat(Stats.ATTACK_COUNT_MAX, 0, null, null);
		
		if (maxAttackedCount > 1)
		{
			final int maxAngleDiff = (int) _actor.getStatus().calcStat(Stats.POWER_ATTACK_ANGLE, 120, null, null);
			final boolean isMainTargetPlayable = target instanceof Playable;
			
			for (Creature knownCreature : _actor.getKnownTypeInRadius(Creature.class, _actor.getStatus().getPhysicalAttackRange()))
			{
				if (knownCreature == target)
					continue;
				
				if (!_actor.isFacing(knownCreature, maxAngleDiff))
					continue;
				
				if (!knownCreature.isAttackableBy(_actor))
					continue;
				
				if (_actor instanceof Playable playable && knownCreature instanceof Playable && (knownCreature.isInsideZone(ZoneId.PEACE) || !isMainTargetPlayable || !knownCreature.isAttackableWithoutForceBy(playable)))
					continue;
				
				// Hold the hit.
				hitHolders.add(getHitHolder(knownCreature, isSoulshot, false));
				
				// Break the loop if holders reached max count.
				if (hitHolders.size() >= maxAttackedCount)
					break;
			}
		}
		
		final HitHolder[] hits = hitHolders.toArray(new HitHolder[] {});
		
		setAttackTask(hits, weapon, sAtk, isSoulshot);
		
		_attackTask = ThreadPool.schedule(this::onHitTimer, sAtk);
		
		return hits;
	}
	
	/**
	 * Launch a simple attack.
	 * @param target : The targeted {@link Creature}.
	 * @param weapon : The used {@link Weapon}.
	 * @param sAtk : The Attack Speed of the attacker.
	 * @param isSoulshot : True if a soulshot was charged, false otherwise.
	 * @return An array of generated {@link HitHolder}s.
	 */
	private HitHolder[] doAttackHitSimple(Creature target, Weapon weapon, int sAtk, boolean isSoulshot)
	{
		final HitHolder[] hits = new HitHolder[]
		{
			getHitHolder(target, isSoulshot, false)
		};
		
		setAttackTask(hits, weapon, sAtk, isSoulshot);
		
		_attackTask = ThreadPool.schedule(this::onHitTimer, sAtk);
		
		return hits;
	}
	
	/**
	 * @param target : The targeted {@link Creature}.
	 * @param isSoulshot : If true, a soulshot was charged and will be used.
	 * @param isSplit : If true, damages will be split in 2. Used for dual wield attacks.
	 * @return a new {@link HitHolder} with generated damage, shield resistance, critical and miss informations.
	 */
	private HitHolder getHitHolder(Creature target, boolean isSoulshot, boolean isSplit)
	{
		boolean crit = false;
		ShieldDefense shld = ShieldDefense.FAILED;
		int damage = 0;
		
		final boolean miss = Formulas.calcHitMiss(_actor, target);
		if (!miss)
		{
			crit = Formulas.calcCrit(_actor, target, null);
			shld = Formulas.calcShldUse(_actor, target, null, crit);
			damage = (int) Formulas.calcPhysicalAttackDamage(_actor, target, shld, crit, isSoulshot);
			
			if (isSplit)
				damage /= 2;
		}
		
		return new HitHolder(target, damage, crit, miss, shld);
	}
	
	/**
	 * Abort the current attack of the {@link Creature} and send {@link ActionFailed} packet.
	 */
	public void stop()
	{
		if (_attackTask != null)
		{
			_attackTask.cancel(false);
			_attackTask = null;
		}
		
		clearAttackTask(true);
	}
	
	/**
	 * Abort the current attack and send {@link SystemMessageId#ATTACK_FAILED} to the {@link Creature}.
	 */
	public void interrupt()
	{
		if (isAttackingNow())
		{
			stop();
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
		}
	}
	
	private void setAttackTask(HitHolder[] hitHolders, Weapon weapon, int afterAttackDelay, boolean isSoulshot)
	{
		final WeaponType weaponType = (weapon == null) ? WeaponType.ETC : weapon.getItemType();
		final int weaponGrade = (weapon == null) ? 0 : weapon.getCrystalType().getId();
		
		// Set variables.
		_isAttackingNow = true;
		_isBowCoolingDown = (weaponType == WeaponType.BOW);
		_hitHolders = hitHolders;
		_weaponType = weaponType;
		_afterAttackDelay = afterAttackDelay;
		_isHit = false;
		_isInHitAnimation = true;
		
		// Generate flags for every HitHolder. Feed _isHit.
		for (HitHolder hit : _hitHolders)
		{
			if (hit._miss)
			{
				hit._flags = Attack.HITFLAG_MISS;
				continue;
			}
			
			_isHit = true;
			
			if (isSoulshot)
				hit._flags = Attack.HITFLAG_SS | weaponGrade;
			
			if (hit._crit)
				hit._flags |= Attack.HITFLAG_CRIT;
			
			if (hit._sDef != ShieldDefense.FAILED)
				hit._flags |= Attack.HITFLAG_SHLD;
		}
	}
	
	private void clearAttackTask(boolean clearBowCooldown)
	{
		_isAttackingNow = false;
		_isInHitAnimation = false;
		
		if (clearBowCooldown)
			_isBowCoolingDown = false;
	}
	
	public static class HitHolder
	{
		public Creature _target;
		public int _targetId;
		public int _damage;
		public boolean _crit;
		public boolean _miss;
		public ShieldDefense _sDef;
		public int _flags;
		
		public HitHolder(Creature target, int damage, boolean crit, boolean miss, ShieldDefense sDef)
		{
			_target = target;
			_targetId = target.getObjectId();
			_damage = damage;
			_crit = crit;
			_sDef = sDef;
			_miss = miss;
		}
	}
}