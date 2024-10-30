package com.shnok.javaserver.gameserver.model.actor.status;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.StatusType;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.ElementType;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.StatusUpdate;
import com.shnok.javaserver.gameserver.skills.Calculator;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * This class groups all data related to HP/MP tracking of a {@link Creature}, aswell as {@link Stats} calculation.<br>
 * <br>
 * In case of a {@link Player}, it also tracks CP.
 * @param <T> : The {@link Creature} used as actor.
 */
public class CreatureStatus<T extends Creature>
{
	protected static final double BAR_SIZE = 352.0;
	
	protected final T _actor;
	
	private final Set<Player> _statusListener = ConcurrentHashMap.newKeySet();
	
	protected static final byte REGEN_FLAG_CP = 4;
	private static final byte REGEN_FLAG_HP = 1;
	private static final byte REGEN_FLAG_MP = 2;
	
	protected double _hp = .0;
	protected double _mp = .0;
	
	private Future<?> _regTask;
	protected byte _flagsRegenActive = 0;
	
	private double _hpUpdateIncCheck = .0;
	private double _hpUpdateDecCheck = .0;
	private double _hpUpdateInterval = .0;
	
	public CreatureStatus(T actor)
	{
		_actor = actor;
	}
	
	/**
	 * @return The {@link Set} of {@link Player}s to inform.
	 */
	public final Set<Player> getStatusListener()
	{
		return _statusListener;
	}
	
	/**
	 * Add the {@link Player} to this {@link CreatureStatus} status listener that must inform HP/MP updates of this {@link Creature}.
	 * @param player : The {@link Player} to add to the listener.
	 */
	public final void addStatusListener(Player player)
	{
		if (player == _actor)
			return;
		
		_statusListener.add(player);
	}
	
	/**
	 * Remove the {@link Player} to this {@link CreatureStatus} status listener that must inform HP/MP updates of this {@link Creature}.
	 * @param player : The {@link Player} to remove from the listener.
	 */
	public final void removeStatusListener(Player player)
	{
		_statusListener.remove(player);
	}
	
	/**
	 * Start the HP/MP/CP Regeneration task.
	 */
	public final synchronized void startHpMpRegeneration()
	{
		if (_regTask == null && !_actor.isDead())
		{
			// Get the regeneration period.
			final int period = Formulas.getRegeneratePeriod(_actor);
			
			// Create the HP/MP/CP regeneration task.
			_regTask = ThreadPool.scheduleAtFixedRate(this::doRegeneration, period, period);
		}
	}
	
	/**
	 * Stop the HP/MP/CP Regeneration task.
	 */
	public final synchronized void stopHpMpRegeneration()
	{
		if (_regTask != null)
		{
			// Stop the HP/MP/CP regeneration task.
			_regTask.cancel(false);
			_regTask = null;
			
			// Set the RegenActive flag to false.
			_flagsRegenActive = 0;
		}
	}
	
	public final double getHp()
	{
		return _hp;
	}
	
	public final void setHp(double newHp)
	{
		setHp(newHp, true);
	}
	
	/**
	 * Set current HPs to the amount set as parameter. We also start or stop the regeneration task if needed.
	 * @param newHp : The new amount to set.
	 * @param broadcastPacket : If true, call {@link #broadcastStatusUpdate()}.
	 */
	public void setHp(double newHp, boolean broadcastPacket)
	{
		final int maxHp = getMaxHp();
		
		synchronized (this)
		{
			if (_actor.isDead())
				return;
			
			if (newHp >= maxHp)
			{
				// Set the RegenActive flag to false
				_hp = maxHp;
				_flagsRegenActive &= ~REGEN_FLAG_HP;
				
				// Stop the HP/MP/CP Regeneration task
				if (_flagsRegenActive == 0)
					stopHpMpRegeneration();
			}
			else
			{
				// Set the RegenActive flag to true
				_hp = newHp;
				_flagsRegenActive |= REGEN_FLAG_HP;
				
				// Start the HP/MP/CP Regeneration task with Medium priority
				startHpMpRegeneration();
			}
		}
		
		if (broadcastPacket)
			broadcastStatusUpdate();
	}
	
	/**
	 * Update this {@link CreatureStatus} health value with the amount set as parameter.
	 * @param value : The amount of HPs to add.
	 * @return The health value which was finally ADDED.
	 */
	public double addHp(double value)
	{
		// Bypass set to avoid to send pointless packet.
		if (value == 0)
			return value;
		
		final double maxHp = getMaxHp();
		if (_hp + value > maxHp)
		{
			value = maxHp - _hp;
			
			// Bypass set to avoid to send pointless packet.
			if (value == 0)
				return value;
		}
		
		setHp(_hp + value);
		return value;
	}
	
	/**
	 * Reduce the current HP of the Creature and launch the doDie Task if necessary.
	 * @param value : The amount of removed HPs.
	 * @param attacker : The Creature who attacks.
	 */
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	public void reduceHp(double value, Creature attacker, boolean isHpConsumption)
	{
		reduceHp(value, attacker, true, false, isHpConsumption);
	}
	
	public void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption)
	{
		if (_actor.isDead())
			return;
		
		// invul handling
		if (_actor.isInvul())
		{
			// other chars can't damage
			if (attacker != _actor)
				return;
			
			// only DOT and HP consumption allowed for damage self
			if (!isDOT && !isHPConsumption)
				return;
		}
		
		if (attacker != null)
		{
			final Player attackerPlayer = attacker.getActingPlayer();
			if (attackerPlayer != null && !attackerPlayer.getAccessLevel().canGiveDamage())
				return;
		}
		
		if (!isDOT && !isHPConsumption)
		{
			_actor.stopEffects(EffectType.SLEEP);
			_actor.stopEffects(EffectType.IMMOBILE_UNTIL_ATTACKED);
			
			if (_actor.isStunned() && Rnd.get(10) == 0)
			{
				_actor.stopEffects(EffectType.STUN);
				
				// Refresh abnormal effects.
				_actor.updateAbnormalEffect();
			}
			
			if (_actor.isImmobileUntilAttacked())
			{
				_actor.stopEffects(EffectType.IMMOBILE_UNTIL_ATTACKED);
				
				// Refresh abnormal effects.
				_actor.updateAbnormalEffect();
			}
		}
		
		// Reduce HPs. The value is blocked to 1 if the Creature isn't mortal.
		if (value > 0)
			setHp(Math.max(_hp - value, (_actor.isMortal()) ? 0 : 1));
		
		// Handle die process if value is too low.
		if (_hp < 0.5)
			_actor.doDie(attacker);
	}
	
	public final double getMp()
	{
		return _mp;
	}
	
	public final void setMp(double newMp)
	{
		setMp(newMp, true);
	}
	
	/**
	 * Set current MPs to the amount set as parameter. We also start or stop the regeneration task if needed.
	 * @param newMp : The new amount to set.
	 * @param broadcastPacket : If true, call {@link #broadcastStatusUpdate()}.
	 */
	public final void setMp(double newMp, boolean broadcastPacket)
	{
		final int maxMp = getMaxMp();
		
		synchronized (this)
		{
			if (_actor.isDead())
				return;
			
			if (newMp >= maxMp)
			{
				// Set the RegenActive flag to false
				_mp = maxMp;
				_flagsRegenActive &= ~REGEN_FLAG_MP;
				
				// Stop the HP/MP/CP Regeneration task
				if (_flagsRegenActive == 0)
					stopHpMpRegeneration();
			}
			else
			{
				// Set the RegenActive flag to true
				_mp = newMp;
				_flagsRegenActive |= REGEN_FLAG_MP;
				
				// Start the HP/MP/CP Regeneration task with Medium priority
				startHpMpRegeneration();
			}
		}
		
		if (broadcastPacket)
			broadcastStatusUpdate();
	}
	
	/**
	 * Update this {@link CreatureStatus} mana value with the amount set as parameter.
	 * @param value : The amount of mana points to add.
	 * @return The mana value which was finally ADDED.
	 */
	public double addMp(double value)
	{
		// Bypass set to avoid to send pointless packet.
		if (value == 0)
			return value;
		
		final double maxMp = getMaxMp();
		if (_mp + value > maxMp)
		{
			value = maxMp - _mp;
			
			// Bypass set to avoid to send pointless packet.
			if (value == 0)
				return value;
		}
		
		setMp(_mp + value);
		return value;
	}
	
	/**
	 * Reduce this {@link CreatureStatus} mana value from the amount set as parameter.
	 * @param value : The amount of mana points to remove.
	 * @return The mana value which was finally REMOVED.
	 */
	public double reduceMp(double value)
	{
		// Bypass set to avoid to send pointless packet.
		if (value == 0)
			return value;
		
		if (_mp - value < 0)
		{
			value = _mp;
			
			// Bypass set to avoid to send pointless packet.
			if (value == 0)
				return value;
		}
		
		setMp(_mp - value);
		return value;
	}
	
	/**
	 * Set both HPs and MPs to given values set as parameters. The udpate is called only one time, during MPs allocation.
	 * @param newHp : The new HP value.
	 * @param newMp : The new MP value.
	 */
	public final void setHpMp(double newHp, double newMp)
	{
		setHp(newHp, false);
		setMp(newMp, true);
	}
	
	/**
	 * Set HPs to the maximum value.
	 */
	public final void setMaxHp()
	{
		setHp(getMaxHp());
	}
	
	/**
	 * Set both HPs and MPs to the maximum values. The udpate is called only one time.
	 */
	public final void setMaxHpMp()
	{
		setMp(getMaxMp(), false);
		
		setMaxHp();
	}
	
	/**
	 * @return The ratio, between 0 and 1.0, of current HPs divided by maximum HPs.
	 */
	public final double getHpRatio()
	{
		return _hp / getMaxHp();
	}
	
	/**
	 * @return The ratio, between 0 and 1.0, of current MPs divided by maximum MPs.
	 */
	public final double getMpRatio()
	{
		return _mp / getMaxMp();
	}
	
	protected void doRegeneration()
	{
		// Modify the current HP of the Creature.
		if (_hp < getMaxHp())
			setHp(_hp + Math.max(1, getRegenHp()), false);
		
		// Modify the current MP of the Creature.
		if (_mp < getMaxMp())
			setMp(_mp + Math.max(1, getRegenMp()), false);
		
		// Send the StatusUpdate packet.
		broadcastStatusUpdate();
	}
	
	public void initializeValues()
	{
		final double maxHp = getMaxHp();
		
		_hpUpdateInterval = maxHp / BAR_SIZE;
		_hpUpdateIncCheck = maxHp;
		_hpUpdateDecCheck = maxHp - _hpUpdateInterval;
	}
	
	/**
	 * @return True if an HP update should be done, otherwise false.
	 */
	public boolean needHpUpdate()
	{
		final double hp = _hp;
		final double maxHp = getMaxHp();
		
		if (hp <= 1.0 || maxHp < BAR_SIZE)
			return true;
		
		if (hp <= _hpUpdateDecCheck || hp >= _hpUpdateIncCheck)
		{
			if (hp == maxHp)
			{
				_hpUpdateIncCheck = hp + 1;
				_hpUpdateDecCheck = hp - _hpUpdateInterval;
			}
			else
			{
				final double doubleMulti = hp / _hpUpdateInterval;
				int intMulti = (int) doubleMulti;
				
				_hpUpdateDecCheck = _hpUpdateInterval * (doubleMulti < intMulti ? intMulti-- : intMulti);
				_hpUpdateIncCheck = _hpUpdateDecCheck + _hpUpdateInterval;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Broadcast {@link StatusUpdate} packet to all registered {@link Player}s.
	 */
	public void broadcastStatusUpdate()
	{
		if (_statusListener.isEmpty() || !needHpUpdate())
			return;
		
		final StatusUpdate su = new StatusUpdate(_actor);
		su.addAttribute(StatusType.CUR_HP, (int) _hp);
		
		for (final Player player : _statusListener)
			player.sendPacket(su);
	}
	
	/**
	 * @param stat : The {@link Stats} to calculate.
	 * @param init : The initial value of the {@link Stats} before applying modifiers.
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The value with modifiers of a given {@link Stats} that will be applied on the targeted {@link Creature}.
	 */
	public final double calcStat(Stats stat, double init, Creature target, L2Skill skill)
	{
		if (stat == null)
			return init;
		
		// Retrieve the Calculator, based on parameterized Stats ordinal.
		final Calculator calculator = _actor.getCalculators()[stat.ordinal()];
		if (calculator == null || calculator.size() == 0)
			return init;
		
		// Launch the calculation.
		double value = calculator.calc(_actor, target, skill, init);
		
		// Enforce positive value, based on parameterized Stats.
		if (value <= 0 && stat.cantBeNegative())
			value = 1.0;
		
		return value;
	}
	
	/**
	 * @return the STR of this {@link Creature}.
	 */
	public int getSTR()
	{
		return _actor.getTemplate().getBaseSTR();
	}
	
	/**
	 * @return the DEX of this {@link Creature}.
	 */
	public int getDEX()
	{
		return _actor.getTemplate().getBaseDEX();
	}
	
	/**
	 * @return the CON of this {@link Creature}.
	 */
	public int getCON()
	{
		return _actor.getTemplate().getBaseCON();
	}
	
	/**
	 * @return the INT of this {@link Creature}.
	 */
	public int getINT()
	{
		return _actor.getTemplate().getBaseINT();
	}
	
	/**
	 * @return the MEN of this {@link Creature}.
	 */
	public int getMEN()
	{
		return _actor.getTemplate().getBaseMEN();
	}
	
	/**
	 * @return the WIT of this {@link Creature}.
	 */
	public int getWIT()
	{
		return _actor.getTemplate().getBaseWIT();
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The physical critical hit rate (base+modifier) of this {@link Creature}. It can't exceed 500.
	 */
	public int getCriticalHit(Creature target, L2Skill skill)
	{
		return Math.min((int) calcStat(Stats.CRITICAL_RATE, _actor.getTemplate().getBaseCritRate(), target, skill), 500);
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The magical critical hit rate (base+modifier) of this {@link Creature}.
	 */
	public final int getMCriticalHit(Creature target, L2Skill skill)
	{
		return (int) calcStat(Stats.MCRITICAL_RATE, 8, target, skill);
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @return The evasion rate (base+modifier) of this {@link Creature}.
	 */
	public int getEvasionRate(Creature target)
	{
		return (int) calcStat(Stats.EVASION_RATE, 0, target, null);
	}
	
	/**
	 * @return The accuracy (base+modifier) of this {@link Creature}.
	 */
	public int getAccuracy()
	{
		return (int) calcStat(Stats.ACCURACY_COMBAT, 0, null, null);
	}
	
	/**
	 * @return The maximum HP of this {@link Creature}, based on its current level.
	 */
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, _actor.getTemplate().getBaseHpMax(getLevel()), null, null);
	}
	
	/**
	 * @return The maximum CP of this {@link Creature}. Overriden in {@link PlayerStatus}.
	 */
	public int getMaxCp()
	{
		return 0;
	}
	
	/**
	 * @return The maximum MP of this {@link Creature}, based on its current level.
	 */
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, _actor.getTemplate().getBaseMpMax(getLevel()), null, null);
	}
	
	/**
	 * @return The HP regeneration of this {@link Creature}.
	 */
	public double getRegenHp()
	{
		return calcStat(Stats.REGENERATE_HP_RATE, _actor.getTemplate().getBaseHpRegen(getLevel()) * (_actor.isRaidRelated() ? Config.RAID_HP_REGEN_MULTIPLIER : Config.HP_REGEN_MULTIPLIER), null, null);
	}
	
	/**
	 * @return The MP regeneration of this {@link Creature}.
	 */
	public double getRegenMp()
	{
		return calcStat(Stats.REGENERATE_MP_RATE, _actor.getTemplate().getBaseMpRegen(getLevel()) * (_actor.isRaidRelated() ? Config.RAID_MP_REGEN_MULTIPLIER : Config.MP_REGEN_MULTIPLIER), null, null);
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The MAtk (base+modifier) of this {@link Creature} for a given {@link L2Skill} and {@link Creature} target.
	 */
	public int getMAtk(Creature target, L2Skill skill)
	{
		return (int) calcStat(Stats.MAGIC_ATTACK, _actor.getTemplate().getBaseMAtk(), target, skill);
	}
	
	/**
	 * @return The MAtk Speed (base+modifier) of this {@link Creature}.
	 */
	public int getMAtkSpd()
	{
		return (int) calcStat(Stats.MAGIC_ATTACK_SPEED, 333.0, null, null);
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The MDef (base+modifier) of this {@link Creature} for a given {@link L2Skill} and {@link Creature} target.
	 */
	public int getMDef(Creature target, L2Skill skill)
	{
		// Calculate modifiers Magic Attack
		return (int) calcStat(Stats.MAGIC_DEFENCE, _actor.getTemplate().getBaseMDef() * ((_actor.isRaidRelated()) ? Config.RAID_DEFENCE_MULTIPLIER : 1), target, skill);
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @return The PAtk (base+modifier) of this {@link Creature} for a given {@link Creature} target.
	 */
	public int getPAtk(Creature target)
	{
		return (int) calcStat(Stats.POWER_ATTACK, _actor.getTemplate().getBasePAtk(), target, null);
	}
	
	/**
	 * @return The PAtk Speed (base+modifier) of this {@link Creature}.
	 */
	public int getPAtkSpd()
	{
		return (int) calcStat(Stats.POWER_ATTACK_SPEED, _actor.getTemplate().getBasePAtkSpd(), null, null);
	}
	
	/**
	 * @param target : The {@link Creature} target whose properties will be used in the calculation.
	 * @return The PDef (base+modifier) of this {@link Creature} for a given {@link Creature} target.
	 */
	public int getPDef(Creature target)
	{
		return (int) calcStat(Stats.POWER_DEFENCE, _actor.getTemplate().getBasePDef() * ((_actor.isRaidRelated()) ? Config.RAID_DEFENCE_MULTIPLIER : 1), target, null);
	}
	
	/**
	 * @return The Physical Attack range (base+modifier) of this {@link Creature}.
	 */
	public int getPhysicalAttackRange()
	{
		return _actor.getAttackType().getRange();
	}
	
	/**
	 * @return The shield defense rate (base+modifier) of this {@link Creature}.
	 */
	public final int getShldDef()
	{
		return (int) calcStat(Stats.SHIELD_DEFENCE, 0, null, null);
	}
	
	/**
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The mana consumption of the {@link L2Skill} set as parameter.
	 */
	public final int getMpConsume(L2Skill skill)
	{
		if (skill == null)
			return 1;
		
		double mpConsume = skill.getMpConsume();
		
		if (skill.isDance())
		{
			if (_actor != null && _actor.getDanceCount() > 0)
				mpConsume += _actor.getDanceCount() * skill.getNextDanceMpCost();
			
			return (int) calcStat(Stats.DANCE_MP_CONSUME_RATE, mpConsume, null, null);
		}
		
		if (skill.isMagic())
			return (int) calcStat(Stats.MAGICAL_MP_CONSUME_RATE, mpConsume, null, null);
		
		return (int) calcStat(Stats.PHYSICAL_MP_CONSUME_RATE, mpConsume, null, null);
	}
	
	/**
	 * @param skill : The {@link L2Skill} whose properties will be used in the calculation.
	 * @return The initial mana consumption of the {@link L2Skill} set as parameter.
	 */
	public final int getMpInitialConsume(L2Skill skill)
	{
		if (skill == null)
			return 1;
		
		double mpConsume = skill.getMpInitialConsume();
		
		if (skill.isDance())
			return (int) calcStat(Stats.DANCE_MP_CONSUME_RATE, mpConsume, null, null);
		
		if (skill.isMagic())
			return (int) calcStat(Stats.MAGICAL_MP_CONSUME_RATE, mpConsume, null, null);
		
		return (int) calcStat(Stats.PHYSICAL_MP_CONSUME_RATE, mpConsume, null, null);
	}
	
	/**
	 * @param element : The {@link ElementType} to test.
	 * @return The calculated attack power of a given {@link ElementType} for this {@link Creature}.
	 */
	public int getAttackElementValue(ElementType element)
	{
		return (element == ElementType.NONE) ? 0 : (int) calcStat(element.getAtkStat(), 0, null, null);
	}
	
	/**
	 * @param element : The {@link ElementType} to test.
	 * @return The calculated defense power of a given {@link ElementType} for this {@link Creature}.
	 */
	public double getDefenseElementValue(ElementType element)
	{
		return (element == ElementType.NONE) ? 1. : calcStat(element.getResStat(), 1., null, null);
	}
	
	/**
	 * @return The base running speed, given by owner template. Player is affected by mount type.
	 */
	public int getBaseRunSpeed()
	{
		return (int) _actor.getTemplate().getBaseRunSpeed();
	}
	
	/**
	 * @return The base walking speed, given by owner template. Player is affected by mount type.
	 */
	public int getBaseWalkSpeed()
	{
		return (int) _actor.getTemplate().getBaseWalkSpeed();
	}
	
	/**
	 * @return The base movement speed, given by owner template and movement status. Player is affected by mount type and by being in L2WaterZone.
	 */
	protected final int getBaseMoveSpeed()
	{
		return (_actor.isRunning()) ? getBaseRunSpeed() : getBaseWalkSpeed();
	}
	
	/**
	 * @return The movement speed multiplier, which is used by client to set correct character/object movement speed.
	 */
	public final float getMovementSpeedMultiplier()
	{
		if (getBaseMoveSpeed() == 0)
			return 0;
		
		return getMoveSpeed() / getBaseMoveSpeed();
	}
	
	/**
	 * @return The attack speed multiplier, which is used by client to set correct character/object attack speed.
	 */
	public final float getAttackSpeedMultiplier()
	{
		if (_actor.getTemplate().getBasePAtkSpd() == 0)
			return 0;
		
		return (float) ((1.1) * getPAtkSpd() / _actor.getTemplate().getBasePAtkSpd());
	}
	
	/**
	 * @return The movement speed, given by owner template, status and effects.
	 */
	public float getMoveSpeed()
	{
		return (float) calcStat(Stats.RUN_SPEED, getBaseMoveSpeed(), null, null);
	}
	
	/**
	 * @param isStillWalking : If set to True, we use walking speed rather than running speed.
	 * @return An emulated movement speed, based on client animation.
	 */
	public float getRealMoveSpeed(boolean isStillWalking)
	{
		return getMoveSpeed();
	}
	
	/**
	 * @return The level of this {@link Creature}.
	 */
	public int getLevel()
	{
		return 1;
	}
	
	/**
	 * @return the level modifier.
	 */
	public double getLevelMod()
	{
		return (100.0 - 11 + getLevel()) / 100.0;
	}
}