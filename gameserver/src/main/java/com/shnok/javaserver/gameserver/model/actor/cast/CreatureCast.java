package com.shnok.javaserver.gameserver.model.actor.cast;

import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.math.MathUtil;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.enums.GaugeColor;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.handler.SkillHandler;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillCanceled;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillLaunched;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillUse;
import com.shnok.javaserver.gameserver.network.serverpackets.SetupGauge;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * This class groups all cast data related to a {@link Creature}.
 * @param <T> : The {@link Creature} used as actor.
 */
public class CreatureCast<T extends Creature>
{
	protected final T _actor;
	
	protected long _castInterruptTime;
	
	protected Creature[] _targets;
	protected Creature _target;
	protected L2Skill _skill;
	protected ItemInstance _item;
	protected int _hitTime;
	protected int _coolTime;
	
	protected ScheduledFuture<?> _castTask;
	
	protected boolean _isCastingNow;
	
	public CreatureCast(T actor)
	{
		_actor = actor;
	}
	
	public final boolean canAbortCast()
	{
		return _castInterruptTime > System.currentTimeMillis();
	}
	
	public final boolean isCastingNow()
	{
		return _isCastingNow;
	}
	
	public final L2Skill getCurrentSkill()
	{
		return _skill;
	}
	
	public void doFusionCast(L2Skill skill, Creature target)
	{
		// Non-Player Creatures cannot use FUSION or SIGNETS
	}
	
	public void doInstantCast(L2Skill itemSkill, ItemInstance item)
	{
		// Non-Playable Creatures cannot use potions or energy stones
	}
	
	public void doToggleCast(L2Skill skill, Creature target)
	{
		// Non-Player Creatures cannot use TOGGLES
	}
	
	/**
	 * Manage the casting task and display the casting bar and animation on client.
	 * @param skill : The {@link L2Skill} to cast.
	 * @param target : The {@link Creature} effected target.
	 * @param itemInstance : The potential {@link ItemInstance} used to cast.
	 */
	public void doCast(L2Skill skill, Creature target, ItemInstance itemInstance)
	{
		int hitTime = skill.getHitTime();
		int coolTime = skill.getCoolTime();
		if (!skill.isStaticHitTime())
		{
			hitTime = Formulas.calcAtkSpd(_actor, skill, hitTime);
			if (coolTime > 0)
				coolTime = Formulas.calcAtkSpd(_actor, skill, coolTime);
			
			if (skill.isMagic() && (_actor.isChargedShot(ShotType.SPIRITSHOT) || _actor.isChargedShot(ShotType.BLESSED_SPIRITSHOT)))
			{
				hitTime = (int) (0.70 * hitTime);
				coolTime = (int) (0.70 * coolTime);
			}
			
			if (skill.getHitTime() >= 500 && hitTime < 500)
				hitTime = 500;
		}
		
		int reuseDelay = skill.getReuseDelay();
		if (!skill.isStaticReuse())
		{
			reuseDelay *= _actor.getStatus().calcStat(skill.isMagic() ? Stats.MAGIC_REUSE_RATE : Stats.P_REUSE, 1, null, null);
			reuseDelay *= 333.0 / (skill.isMagic() ? _actor.getStatus().getMAtkSpd() : _actor.getStatus().getPAtkSpd());
		}
		
		final boolean skillMastery = Formulas.calcSkillMastery(_actor, skill);
		if (skillMastery)
		{
			if (_actor.getActingPlayer() != null)
				_actor.getActingPlayer().sendPacket(SystemMessageId.SKILL_READY_TO_USE_AGAIN);
		}
		else
		{
			if (reuseDelay > 30000)
				_actor.addTimeStamp(skill, reuseDelay);
			
			if (reuseDelay > 10)
				_actor.disableSkill(skill, reuseDelay);
		}
		
		final int initMpConsume = _actor.getStatus().getMpInitialConsume(skill);
		if (initMpConsume > 0)
			_actor.getStatus().reduceMp(initMpConsume);
		
		_actor.broadcastPacket(new MagicSkillUse(_actor, target, skill.getId(), skill.getLevel(), hitTime, reuseDelay, false));
		
		if (itemInstance == null)
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USE_S1).addSkillName(skill));
		
		final long castInterruptTime = System.currentTimeMillis() + hitTime - 200;
		
		setCastTask(skill, target, itemInstance, hitTime, coolTime, castInterruptTime);
		
		if (_hitTime > 410)
		{
			if (_actor instanceof Player player)
				player.sendPacket(new SetupGauge(GaugeColor.BLUE, _hitTime));
		}
		else
			_hitTime = 0;
		
		_castTask = ThreadPool.schedule(this::onMagicLaunch, hitTime > 410 ? hitTime - 400 : 0);
	}
	
	/**
	 * Manage the launching task and display the animation on client.
	 */
	private final void onMagicLaunch()
	{
		// Content was cleaned meantime, simply return doing nothing.
		if (!isCastingNow())
			return;
		
		// No checks for range, LoS, PEACE if the target is the caster.
		if (_target != _actor)
		{
			int escapeRange = 0;
			if (_skill.getEffectRange() > 0)
				escapeRange = _skill.getEffectRange();
			else if (_skill.getCastRange() <= 0 && _skill.getSkillRadius() > 80)
				escapeRange = _skill.getSkillRadius();
			
			// If the target disappears, stop the cast.
			if (_actor.getAI().isTargetLost(_target, _skill))
			{
				stop();
				return;
			}
			
			// If the target is out of range, stop the cast.
			if (escapeRange > 0 && !MathUtil.checkIfInRange(escapeRange, _actor, _target, true))
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED));
				
				stop();
				return;
			}
			
			// If the target is out of view, stop the cast.
			if (_skill.getSkillRadius() > 0 && !GeoEngine.getInstance().canSeeTarget(_actor, _target))
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANT_SEE_TARGET));
				
				stop();
				return;
			}
			
			// If the target reached a PEACE zone, stop the cast.
			if (_skill.isOffensive() && _actor instanceof Playable playable && _target instanceof Playable targetPlayable)
			{
				if (playable.isInsideZone(ZoneId.PEACE))
				{
					playable.sendPacket(SystemMessageId.CANT_ATK_PEACEZONE);
					
					stop();
					return;
				}
				
				if (targetPlayable.isInsideZone(ZoneId.PEACE))
				{
					playable.sendPacket(SystemMessageId.TARGET_IN_PEACEZONE);
					
					stop();
					return;
				}
			}
		}
		
		_targets = _skill.getTargetList(_actor, _target);
		
		_actor.broadcastPacket(new MagicSkillLaunched(_actor, _skill, _targets));
		
		_castTask = ThreadPool.schedule(this::onMagicHitTimer, _hitTime == 0 ? 0 : 400);
	}
	
	/**
	 * Manage effects application, after cast animation occured. Verify if conditions are still met.
	 */
	protected void onMagicHitTimer()
	{
		// Content was cleaned meantime, simply return doing nothing.
		if (!isCastingNow())
			return;
		
		final double mpConsume = _actor.getStatus().getMpConsume(_skill);
		if (mpConsume > 0)
		{
			if (mpConsume > _actor.getStatus().getMp())
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP));
				stop();
				return;
			}
			
			_actor.getStatus().reduceMp(mpConsume);
		}
		
		final double hpConsume = _skill.getHpConsume();
		if (hpConsume > 0)
		{
			if (hpConsume > _actor.getStatus().getHp())
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_HP));
				stop();
				return;
			}
			
			_actor.getStatus().reduceHp(hpConsume, _actor, true);
		}
		
		if (_actor instanceof Player player)
		{
			if (_skill.getNumCharges() > 0)
			{
				if (_skill.getMaxCharges() > 0)
					player.increaseCharges(_skill.getNumCharges(), _skill.getMaxCharges());
				else
					player.decreaseCharges(_skill.getNumCharges());
			}
			
			for (final Creature target : _targets)
			{
				if (target instanceof Summon targetSummon)
					targetSummon.updateAndBroadcastStatus(1);
			}
		}
		
		callSkill(_skill, _targets, _item);
		
		_castTask = ThreadPool.schedule(this::onMagicFinalizer, (_hitTime == 0 || _coolTime == 0) ? 0 : _coolTime);
	}
	
	/**
	 * Manage the end of a cast launch.
	 */
	protected final void onMagicFinalizer()
	{
		// Content was cleaned meantime, simply return doing nothing.
		if (!isCastingNow())
			return;
		
		if (_actor instanceof Playable)
			_actor.rechargeShots(_skill.useSoulShot(), _skill.useSpiritShot());
		
		if (_skill.isOffensive() && _targets.length != 0)
			_actor.getAI().startAttackStance();
		
		_isCastingNow = false;
		
		notifyCastFinishToAI(false);
	}
	
	/**
	 * Check cast conditions BEFORE MOVEMENT.
	 * @param target : The {@link Creature} used as parameter.
	 * @param skill : The {@link L2Skill} used as parameter.
	 * @return True if casting is possible, false otherwise.
	 */
	public boolean canAttemptCast(Creature target, L2Skill skill)
	{
		if (_actor.isSkillDisabled(skill))
		{
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PREPARED_FOR_REUSE).addSkillName(skill));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check cast conditions for Hp, Mp.
	 * @param target : The {@link Creature} used as parameter.
	 * @param skill : The {@link L2Skill} used as parameter.
	 * @return True if casting is possible, false otherwise.
	 */
	public boolean meetsHpMpConditions(Creature target, L2Skill skill)
	{
		if (target == null || skill == null)
			return false;
		
		final int initialMpConsume = _actor.getStatus().getMpInitialConsume(skill);
		final int mpConsume = _actor.getStatus().getMpConsume(skill);
		
		if ((initialMpConsume > 0 || mpConsume > 0) && (int) _actor.getStatus().getMp() < initialMpConsume + mpConsume)
		{
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_MP));
			return false;
		}
		
		if (skill.getHpConsume() > 0 && (int) _actor.getStatus().getHp() <= skill.getHpConsume())
		{
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.NOT_ENOUGH_HP));
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check cast conditions for Hp, Mp and disables (ex. Silence).
	 * @param target : The {@link Creature} used as parameter.
	 * @param skill : The {@link L2Skill} used as parameter.
	 * @return True if casting is possible, false otherwise.
	 */
	public boolean meetsHpMpDisabledConditions(Creature target, L2Skill skill)
	{
		if (!meetsHpMpConditions(target, skill))
			return false;
		
		if ((skill.isMagic() && _actor.isMuted()) || (!skill.isMagic() && _actor.isPhysicalMuted()))
			return false;
		
		return true;
	}
	
	/**
	 * Check cast conditions AFTER MOVEMENT.
	 * @param target : The {@link Creature} used as parameter.
	 * @param skill : The {@link L2Skill} used as parameter.
	 * @param isCtrlPressed : If True, we use specific CTRL rules.
	 * @param itemObjectId : If different than 0, an object has been used.
	 * @return True if casting is possible, false otherwise.
	 */
	public boolean canCast(Creature target, L2Skill skill, boolean isCtrlPressed, int itemObjectId)
	{
		if (!meetsHpMpDisabledConditions(target, skill))
			return false;
		
		if (skill.getCastRange() > 0 && !GeoEngine.getInstance().canSeeTarget(_actor, target))
		{
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANT_SEE_TARGET));
			return false;
		}
		
		return skill.getWeaponDependancy(_actor);
	}
	
	/**
	 * Abort the current cast, no matter actual cast step.
	 */
	public void stop()
	{
		if (_actor.getFusionSkill() != null)
			_actor.getFusionSkill().onCastAbort();
		
		final AbstractEffect effect = _actor.getFirstEffect(EffectType.SIGNET_GROUND);
		if (effect != null)
			effect.exit();
		
		if (_actor.isAllSkillsDisabled())
			_actor.enableAllSkills();
		
		if (isCastingNow())
		{
			// Send the client animation cancel.
			_actor.broadcastPacket(new MagicSkillCanceled(_actor.getObjectId()));
			
			// Cancel the task, if running.
			if (_castTask != null)
			{
				_castTask.cancel(false);
				_castTask = null;
			}
			
			// Notify the AI about interruption.
			notifyCastFinishToAI(true);
			
			// Reset the variable.
			_isCastingNow = false;
		}
	}
	
	/**
	 * Interrupt the current cast, if it is still breakable.
	 */
	public void interrupt()
	{
		if (canAbortCast())
		{
			stop();
			_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CASTING_INTERRUPTED));
		}
	}
	
	/**
	 * Launch the magic skill and calculate its effects on each target contained in the targets array.
	 * @param skill : The {@link L2Skill} to use.
	 * @param targets : The array of {@link Creature} targets.
	 * @param itemInstance : The {@link ItemInstance} used for skill cast.
	 */
	public void callSkill(L2Skill skill, Creature[] targets, ItemInstance itemInstance)
	{
		for (final Creature target : targets)
		{
			if (_actor instanceof Playable && target instanceof Monster targetMonster && skill.isOverhit())
				targetMonster.getOverhitState().set(true);
			
			switch (skill.getSkillType())
			{
				case COMMON_CRAFT, DWARVEN_CRAFT:
					break;
				
				default:
					final Weapon activeWeaponItem = _actor.getActiveWeaponItem();
					if (activeWeaponItem != null && !target.isDead())
						activeWeaponItem.castSkillOnMagic(_actor, target, skill);
					
					if (_actor.getChanceSkills() != null)
						_actor.getChanceSkills().onSkillTargetHit(target, skill);
					
					if (target.getChanceSkills() != null)
						target.getChanceSkills().onSkillSelfHit(_actor, skill);
			}
		}
		
		if (skill.isOffensive())
		{
			switch (skill.getSkillType())
			{
				case AGGREDUCE, AGGREMOVE, AGGREDUCE_CHAR:
					break;
				
				default:
					for (final Creature target : targets)
						target.getAI().notifyEvent(AiEventType.ATTACKED, _actor, null);
					break;
			}
		}
		
		final ISkillHandler handler = SkillHandler.getInstance().getHandler(skill.getSkillType());
		if (handler != null)
			handler.useSkill(_actor, skill, targets, itemInstance);
		else
			skill.useSkill(_actor, targets);
		
		if (skill.isOffensive() && (skill.isDebuff() || skill.getAggroPoints() > 0))
		{
			for (final Creature target : targets)
			{
				if (!(target instanceof Npc targetNpc))
					continue;
				
				for (Quest quest : (targetNpc).getTemplate().getEventQuests(EventHandler.ATTACKED))
					quest.onAttacked(targetNpc, _actor, Math.max(120, skill.getAggroPoints()), skill);
				
				// Party aggro (minion/master).
				if (targetNpc.isMaster() || targetNpc.hasMaster())
				{
					// Retrieve scripts associated to called Attackable and notify the party call.
					for (Quest quest : targetNpc.getTemplate().getEventQuests(EventHandler.PARTY_ATTACKED))
						quest.onPartyAttacked(targetNpc, targetNpc, _actor, Math.max(120, skill.getAggroPoints()));
					
					// If we have a master, we call the event.
					final Npc master = targetNpc.getMaster();
					
					if (master != null && !master.isDead() && targetNpc != master)
					{
						// Retrieve scripts associated to called Attackable and notify the party call.
						for (Quest quest : master.getTemplate().getEventQuests(EventHandler.PARTY_ATTACKED))
							quest.onPartyAttacked(targetNpc, master, _actor, Math.max(120, skill.getAggroPoints()));
					}
					
					// For all minions except me, we call the event.
					for (Npc minion : targetNpc.getMinions())
					{
						if (minion == targetNpc || minion.isDead())
							continue;
						
						// Retrieve scripts associated to called Attackable and notify the party call.
						for (Quest quest : minion.getTemplate().getEventQuests(EventHandler.PARTY_ATTACKED))
							quest.onPartyAttacked(targetNpc, minion, _actor, Math.max(120, skill.getAggroPoints()));
					}
				}
				
				// Social aggro.
				final String[] actorClans = targetNpc.getTemplate().getClans();
				if (actorClans != null && targetNpc.getTemplate().getClanRange() > 0)
				{
					targetNpc.forEachKnownTypeInRadius(Attackable.class, targetNpc.getTemplate().getClanRange(), called ->
					{
						// Called is dead.
						if (called.isDead())
							return;
						
						// Caller clan doesn't correspond to the called clan.
						if (!ArraysUtil.contains(actorClans, called.getTemplate().getClans()))
							return;
						
						// Called ignores that type of caller id.
						if (ArraysUtil.contains(called.getTemplate().getIgnoredIds(), targetNpc.getNpcId()))
							return;
						
						// Check if the Attackable is in the LoS of the caller.
						if (!GeoEngine.getInstance().canSeeTarget(target, called))
							return;
						
						// Retrieve scripts associated to called Attackable and notify the clan call.
						for (Quest quest : called.getTemplate().getEventQuests(EventHandler.CLAN_ATTACKED))
							quest.onClanAttacked((Attackable) target, called, _actor, Math.max(120, skill.getAggroPoints()), skill);
					});
				}
			}
		}
		
		final Player player = _actor.getActingPlayer();
		if (player != null)
		{
			for (final Creature target : targets)
			{
				if (skill.isOffensive())
				{
					if (player.getSummon() != target)
						player.updatePvPStatus(target);
				}
				else
				{
					if (target instanceof Playable)
					{
						final Player targetPlayer = target.getActingPlayer();
						if (!(targetPlayer.equals(_actor) || targetPlayer.equals(player)) && (targetPlayer.getPvpFlag() > 0 || targetPlayer.getKarma() > 0))
							player.updatePvPStatus();
					}
					else if (target instanceof Attackable targetAttackable && !targetAttackable.isGuard())
					{
						switch (skill.getSkillType())
						{
							case SUMMON, BEAST_FEED, UNLOCK, UNLOCK_SPECIAL, DELUXE_KEY_UNLOCK:
								break;
							
							default:
								player.updatePvPStatus();
						}
					}
					
					if (target instanceof Npc targetNpc)
						for (Quest quest : targetNpc.getTemplate().getEventQuests(EventHandler.SPELLED))
							quest.onSpelled(targetNpc, player, skill);
				}
				
				switch (skill.getTargetType())
				{
					case CORPSE_MOB, AREA_CORPSE_MOB:
						if (skill.getSkillType() != SkillType.HARVEST)
							target.forceDecay();
						break;
					default:
						break;
				}
			}
			
			// Notify NPCs in a 1000 range of a skill use.
			_actor.forEachKnownTypeInRadius(Npc.class, 1000, npc ->
			{
				// Do not trigger if the skill is a solo target skill, and if the target is player summon OR if the target is the npc and the skill was a positive effect.
				if (targets.length == 1 && ((player.getSummon() != null && ArraysUtil.contains(targets, player.getSummon())) || (!skill.isOffensive() && !skill.isDebuff() && ArraysUtil.contains(targets, npc))))
					return;
				
				for (Quest quest : npc.getTemplate().getEventQuests(EventHandler.SEE_SPELL))
					quest.onSeeSpell(npc, player, skill, targets, _actor instanceof Summon);
			});
		}
	}
	
	/**
	 * Notify AI the cast ended.
	 * @param isInterrupted : If true, we ended the cast prematurely.
	 */
	protected void notifyCastFinishToAI(boolean isInterrupted)
	{
		_actor.getAI().notifyEvent(AiEventType.FINISHED_CASTING, null, null);
	}
	
	protected void setCastTask(L2Skill skill, Creature target, ItemInstance item, int hitTime, int coolTime, long castInterruptTime)
	{
		_skill = skill;
		_target = target;
		_item = item;
		_hitTime = hitTime;
		_coolTime = coolTime;
		_castInterruptTime = castInterruptTime;
		_isCastingNow = true;
	}
	
	public void describeCastTo(Player player)
	{
		player.sendPacket(new MagicSkillUse(_actor, _target, _skill.getId(), _skill.getLevel(), _skill.getHitTime(), _skill.getReuseDelay(), false));
	}
}