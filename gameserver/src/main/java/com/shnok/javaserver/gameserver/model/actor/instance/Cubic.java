package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.manager.DuelManager;
import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.handler.SkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.MagicSkillUse;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillDrain;
import com.shnok.javaserver.gameserver.taskmanager.AttackStanceTaskManager;

public class Cubic
{
	public static final int STORM_CUBIC = 1;
	public static final int VAMPIRIC_CUBIC = 2;
	public static final int LIFE_CUBIC = 3;
	public static final int VIPER_CUBIC = 4;
	public static final int POLTERGEIST_CUBIC = 5;
	public static final int BINDING_CUBIC = 6;
	public static final int AQUA_CUBIC = 7;
	public static final int SPARK_CUBIC = 8;
	public static final int ATTRACT_CUBIC = 9;
	
	public static final int SKILL_CUBIC_HEAL = 4051;
	public static final int SKILL_CUBIC_CURE = 5579;
	
	private static final int MAX_MAGIC_RANGE = 900;
	private static final int CAST_DELAY = 2000;
	
	private Player _owner;
	
	private int _id;
	private int _matk;
	private int _activationTime;
	private int _activationChance;
	private final boolean _givenByOther;
	
	private final List<L2Skill> _skills = new ArrayList<>(3);
	
	private Future<?> _actionTask;
	private Future<?> _disappearTask;
	private Future<?> _castTask;
	
	public Cubic(Player owner, int id, int level, int mAtk, int activationTime, int activationChance, int totalLifeTime, boolean givenByOther)
	{
		_owner = owner;
		_id = id;
		_matk = mAtk;
		_activationTime = activationTime * 1000;
		_activationChance = activationChance;
		_givenByOther = givenByOther;
		
		switch (_id)
		{
			case STORM_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4049, level));
				break;
			
			case VAMPIRIC_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4050, level));
				break;
			
			case LIFE_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4051, level));
				doAction();
				break;
			
			case VIPER_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4052, level));
				break;
			
			case POLTERGEIST_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4053, level));
				_skills.add(SkillTable.getInstance().getInfo(4054, level));
				_skills.add(SkillTable.getInstance().getInfo(4055, level));
				break;
			
			case BINDING_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4164, level));
				break;
			
			case AQUA_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4165, level));
				break;
			
			case SPARK_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(4166, level));
				break;
			
			case ATTRACT_CUBIC:
				_skills.add(SkillTable.getInstance().getInfo(5115, level));
				_skills.add(SkillTable.getInstance().getInfo(5116, level));
				break;
		}
		
		_disappearTask = ThreadPool.schedule(this::stop, totalLifeTime);
	}
	
	public synchronized void doAction()
	{
		if (_actionTask != null)
			return;
		
		_actionTask = ThreadPool.scheduleAtFixedRate(this::fireAction, _activationTime, _activationTime);
	}
	
	public int getId()
	{
		return _id;
	}
	
	public Player getOwner()
	{
		return _owner;
	}
	
	public int getMAtk()
	{
		return _matk;
	}
	
	public boolean givenByOther()
	{
		return _givenByOther;
	}
	
	public void stopAction()
	{
		if (_actionTask != null)
		{
			_actionTask.cancel(false);
			_actionTask = null;
		}
	}
	
	public void cancelDisappear()
	{
		if (_disappearTask != null)
		{
			_disappearTask.cancel(false);
			_disappearTask = null;
		}
	}
	
	public void stopCastTask()
	{
		if (_castTask != null)
		{
			_castTask.cancel(false);
			_castTask = null;
		}
	}
	
	/**
	 * Refresh disappear task timer, with total lifetime set as parameter.
	 * @param totalLifeTime : The total lifetime used as new disappear task timer.
	 */
	public void refreshDisappearTask(int totalLifeTime)
	{
		// Cancel current disappearTask.
		cancelDisappear();
		
		// Restart a task.
		_disappearTask = ThreadPool.schedule(this::stop, totalLifeTime);
	}
	
	/**
	 * @return a valid enemy {@link Creature} target for an offensive cubic, or null otherwise.
	 */
	private Creature pickEnemyTarget()
	{
		final WorldObject target = _owner.getTarget();
		if (!(target instanceof Creature targetCreature))
			return null;
		
		if (!_owner.isIn3DRadius(targetCreature, MAX_MAGIC_RANGE))
			return null;
		
		if (!targetCreature.isAttackableWithoutForceBy(_owner))
			return null;
		
		return targetCreature;
	}
	
	/**
	 * @return a valid friendly {@link Creature} target for the healing cubic, or null otherwise.
	 */
	private Creature pickFriendlyTarget()
	{
		Creature target = null;
		double ratio = 1.;
		
		Party party = _owner.getParty();
		if (_owner.isInOlympiadMode() || (_owner.isInDuel() && !DuelManager.getInstance().getDuel(_owner.getDuelId()).isPartyDuel()))
			party = null;
		
		if (party != null)
		{
			for (Player member : party.getMembers())
			{
				final double hpRatio = member.getStatus().getHpRatio();
				if (!member.isDead() && hpRatio < 1.0 && ratio > hpRatio && _owner.isIn3DRadius(member, MAX_MAGIC_RANGE))
				{
					target = member;
					ratio = hpRatio;
				}
			}
		}
		else
		{
			final double hpRatio = _owner.getStatus().getHpRatio();
			if (hpRatio < 1.0)
			{
				target = _owner;
				ratio = hpRatio;
			}
		}
		
		// Once a valid target is chosen, we test the probability to heal it based on left HP ratio.
		if (target != null)
		{
			final int i0 = Rnd.get(100);
			if (ratio > 0.6)
			{
				if (i0 > 13)
					return null;
			}
			else if (ratio < 0.3)
			{
				if (i0 > 53)
					return null;
			}
			else if (i0 > 33)
				return null;
		}
		return target;
	}
	
	/**
	 * Stop entirely this {@link Cubic} action (both action/disappear tasks are dropped, id is removed from {@link Player} owner. Enforce broadcast.
	 * @see #stop(boolean)
	 */
	public void stop()
	{
		stop(true);
	}
	
	/**
	 * Stop entirely this {@link Cubic} action - all tasks are dropped, id is removed from {@link Player} owner.
	 * @param doBroadcast : If true, we broadcast UserInfo/CharInfo.
	 */
	public void stop(boolean doBroadcast)
	{
		stopAction();
		cancelDisappear();
		stopCastTask();
		
		_owner.getCubicList().removeCubic(_id);
		
		if (doBroadcast)
			_owner.broadcastUserInfo();
	}
	
	/**
	 * Fire the action associated to this {@link Cubic} id. If the owner is dead or offline, stop the {@link Cubic} entirely.
	 */
	private void fireAction()
	{
		if (_owner.isDead() || !_owner.isOnline())
		{
			stop();
			return;
		}
		
		if (_id == LIFE_CUBIC)
		{
			final L2Skill skill = _skills.stream().filter(s -> s.getId() == SKILL_CUBIC_HEAL).findFirst().orElse(null);
			if (skill == null)
				return;
			
			final Creature target = pickFriendlyTarget();
			if (target == null)
				return;
			
			_castTask = ThreadPool.schedule(() -> useHealSkill(skill, target), CAST_DELAY);
			
			// Life Cubic for Beginners got a level of 8 on L2J, but it's 20 on L2OFF.
			_owner.broadcastPacket(new MagicSkillUse(_owner, target, skill.getId(), (skill.getLevel() == 8) ? 20 : skill.getLevel(), CAST_DELAY, CAST_DELAY));
		}
		else
		{
			if (!AttackStanceTaskManager.getInstance().isInAttackStance(_owner))
			{
				stopAction();
				return;
			}
			
			if (Rnd.get(100) >= _activationChance)
				return;
			
			final L2Skill skill = Rnd.get(_skills);
			if (skill == null)
				return;
			
			final Creature target = pickEnemyTarget();
			if (target == null)
				return;
			
			final Creature[] targets =
			{
				target
			};
			
			_castTask = ThreadPool.schedule(() ->
			{
				switch (skill.getSkillType())
				{
					case PARALYZE, STUN, ROOT, AGGDAMAGE:
						useDisablerSkill(skill, target);
						break;
					
					case MDAM:
						useMdamSkill(skill, target);
						break;
					
					case POISON, DEBUFF, DOT:
						useContinuousSkill(skill, target);
						break;
					
					case DRAIN:
						useDrainSkill((L2SkillDrain) skill, target);
						break;
					
					default:
						SkillHandler.getInstance().getHandler(skill.getSkillType()).useSkill(_owner, skill, targets, null);
						break;
				}
			}, CAST_DELAY);
			
			_owner.broadcastPacket(new MagicSkillUse(_owner, target, skill.getId(), skill.getLevel(), CAST_DELAY, CAST_DELAY));
		}
	}
	
	private static void useHealSkill(L2Skill skill, Creature target)
	{
		if (!target.canBeHealed())
			return;
		
		target.getStatus().addHp(skill.getPower() * target.getStatus().calcStat(Stats.HEAL_EFFECTIVNESS, 100, null, null) / 100.);
		
		if (target instanceof Player)
			target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.REJUVENATING_HP));
	}
	
	private void useDisablerSkill(L2Skill skill, Creature target)
	{
		if (target.isDead())
			return;
		
		final boolean bss = getOwner().isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		final ShieldDefense sDef = Formulas.calcShldUse(getOwner(), target, skill, false);
		
		if (!Formulas.calcCubicSkillSuccess(this, target, skill, sDef, bss))
			return;
		
		if (skill.getSkillType() == SkillType.AGGDAMAGE && target instanceof Attackable)
			target.getAI().notifyEvent(AiEventType.AGGRESSION, getOwner(), (int) ((150 * skill.getPower()) / (target.getStatus().getLevel() + 7)));
		
		skill.getEffects(this, target);
	}
	
	private void useMdamSkill(L2Skill skill, Creature target)
	{
		if (target.isDead())
			return;
		
		final boolean isCrit = Formulas.calcMCrit(getOwner(), target, skill);
		final ShieldDefense sDef = Formulas.calcShldUse(getOwner(), target, skill, false);
		
		int damage = (int) Formulas.calcMagicDam(this, target, skill, isCrit, sDef);
		
		// If target is reflecting the skill then no damage is done Ignoring vengance-like reflections
		if ((Formulas.calcSkillReflect(target, skill) & Formulas.SKILL_REFLECT_SUCCEED) > 0)
			damage = 0;
		
		if (damage > 0)
		{
			// Manage cast break of the target (calculating rate, sending message...)
			Formulas.calcCastBreak(target, damage);
			
			getOwner().sendDamageMessage(target, damage, isCrit, false, false);
			
			if (skill.hasEffects())
			{
				// activate attacked effects, if any
				target.stopSkillEffects(skill.getId());
				
				if (target.getFirstEffect(skill) != null)
					target.removeEffect(target.getFirstEffect(skill));
				
				final boolean bss = getOwner().isChargedShot(ShotType.BLESSED_SPIRITSHOT);
				if (Formulas.calcCubicSkillSuccess(this, target, skill, sDef, bss))
					skill.getEffects(this, target);
			}
			
			target.reduceCurrentHp(damage, getOwner(), skill);
		}
	}
	
	private void useContinuousSkill(L2Skill skill, Creature target)
	{
		if (target.isDead())
			return;
		
		if (skill.isOffensive())
		{
			final ShieldDefense sDef = Formulas.calcShldUse(getOwner(), target, skill, false);
			final boolean bss = getOwner().isChargedShot(ShotType.BLESSED_SPIRITSHOT);
			final boolean acted = Formulas.calcCubicSkillSuccess(this, target, skill, sDef, bss);
			
			if (!acted)
			{
				getOwner().sendPacket(SystemMessageId.ATTACK_FAILED);
				return;
			}
		}
		
		skill.getEffects(this, target);
	}
	
	private void useDrainSkill(L2SkillDrain skill, Creature target)
	{
		if (target.isAlikeDead() && skill.getTargetType() != SkillTargetType.CORPSE_MOB)
			return;
		
		final boolean isCrit = Formulas.calcMCrit(getOwner(), target, skill);
		final ShieldDefense sDef = Formulas.calcShldUse(getOwner(), target, skill, false);
		final int damage = (int) Formulas.calcMagicDam(this, target, skill, isCrit, sDef);
		
		// Check to see if we should damage the target
		if (damage > 0)
		{
			// Add HPs to the Cubic owner.
			getOwner().getStatus().addHp(skill.getAbsorbAbs() + skill.getAbsorbPart() * damage);
			
			// That section is launched for drain skills made on ALIVE targets.
			if (!target.isDead() || skill.getTargetType() != SkillTargetType.CORPSE_MOB)
			{
				// Reduce HPs of the Creature target.
				target.reduceCurrentHp(damage, getOwner(), skill);
				
				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(target, damage);
				
				// Send message.
				getOwner().sendDamageMessage(target, damage, isCrit, false, false);
			}
		}
	}
}