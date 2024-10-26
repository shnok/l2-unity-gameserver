package com.shnok.javaserver.gameserver.model.actor.container.creature;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.enums.skills.TriggerType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.handler.SkillHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillLaunched;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillUse;
import com.shnok.javaserver.gameserver.skills.ChanceCondition;
import com.shnok.javaserver.gameserver.skills.IChanceSkillTrigger;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.effects.EffectChanceSkillTrigger;

public class ChanceSkillList extends ConcurrentHashMap<IChanceSkillTrigger, ChanceCondition>
{
	private static final long serialVersionUID = 1L;
	
	private final Creature _owner;
	
	public ChanceSkillList(Creature owner)
	{
		super();
		
		_owner = owner;
	}
	
	public Creature getOwner()
	{
		return _owner;
	}
	
	public void onTargetHit(Creature target, boolean isCrit)
	{
		final EnumSet<TriggerType> triggers = EnumSet.noneOf(TriggerType.class);
		
		triggers.add(TriggerType.ON_HIT);
		
		if (isCrit)
			triggers.add(TriggerType.ON_CRIT);
		
		onChanceSkillEvent(triggers, target);
	}
	
	public void onSelfHit(Creature target)
	{
		final EnumSet<TriggerType> triggers = EnumSet.noneOf(TriggerType.class);
		
		triggers.add(TriggerType.ON_ATTACKED);
		triggers.add(TriggerType.ON_ATTACKED_HIT);
		
		onChanceSkillEvent(triggers, target);
	}
	
	public void onSkillTargetHit(Creature target, L2Skill skill)
	{
		final EnumSet<TriggerType> triggers = EnumSet.noneOf(TriggerType.class);
		
		if (skill.isDamage())
			triggers.add(TriggerType.ON_MAGIC_OFFENSIVE);
		else if (!skill.isOffensive())
			triggers.add(TriggerType.ON_MAGIC_GOOD);
		
		onChanceSkillEvent(triggers, target);
	}
	
	public void onSkillSelfHit(Creature target, L2Skill skill)
	{
		final EnumSet<TriggerType> triggers = EnumSet.noneOf(TriggerType.class);
		if (skill.isDamage())
			triggers.add(TriggerType.ON_ATTACKED);
		
		onChanceSkillEvent(triggers, target);
	}
	
	public void onChanceSkillEvent(Set<TriggerType> triggers, Creature target)
	{
		if (_owner.isDead())
			return;
		
		for (Entry<IChanceSkillTrigger, ChanceCondition> entry : entrySet())
		{
			final ChanceCondition cond = entry.getValue();
			if (cond != null && cond.trigger(triggers))
			{
				final IChanceSkillTrigger trigger = entry.getKey();
				if (trigger instanceof L2Skill skill)
					makeCast(skill, target);
				else if (trigger instanceof EffectChanceSkillTrigger ecst)
					makeCast(ecst, target);
			}
		}
	}
	
	private void makeCast(L2Skill skill, Creature target)
	{
		if (skill.getWeaponDependancy(_owner) && skill.checkCondition(_owner, target, false))
		{
			if (skill.triggersChanceSkill()) // skill will trigger another skill, but only if its not chance skill
			{
				skill = SkillTable.getInstance().getInfo(skill.getTriggeredChanceId(), skill.getTriggeredChanceLevel());
				if (skill == null || skill.getSkillType() == SkillType.NOTDONE)
					return;
			}
			
			if (_owner.isSkillDisabled(skill))
				return;
			
			if (skill.getReuseDelay() > 0)
				_owner.disableSkill(skill, skill.getReuseDelay());
			
			final Creature[] targets = skill.getTargetList(_owner, target);
			if (targets.length == 0)
				return;
			
			final Creature firstTarget = targets[0];
			
			_owner.broadcastPacket(new MagicSkillLaunched(_owner, skill, targets));
			_owner.broadcastPacket(new MagicSkillUse(_owner, firstTarget, skill.getId(), skill.getLevel(), 0, 0));
			
			// Launch the magic skill and calculate its effects
			// TODO: once core will support all possible effects, use effects (not handler)
			final ISkillHandler handler = SkillHandler.getInstance().getHandler(skill.getSkillType());
			if (handler != null)
				handler.useSkill(_owner, skill, targets, null);
			else
				skill.useSkill(_owner, targets);
		}
	}
	
	private void makeCast(EffectChanceSkillTrigger effect, Creature target)
	{
		if (effect == null || !effect.triggersChanceSkill())
			return;
		
		final L2Skill triggered = SkillTable.getInstance().getInfo(effect.getTriggeredChanceId(), effect.getTriggeredChanceLevel());
		if (triggered == null)
			return;
		
		final Creature caster = triggered.getTargetType() == SkillTargetType.SELF ? _owner : effect.getEffector();
		
		if (caster == null || triggered.getSkillType() == SkillType.NOTDONE || caster.isSkillDisabled(triggered))
			return;
		
		if (triggered.getReuseDelay() > 0)
			caster.disableSkill(triggered, triggered.getReuseDelay());
		
		final Creature[] targets = triggered.getTargetList(_owner, target);
		if (targets.length == 0)
			return;
		
		final Creature firstTarget = targets[0];
		final ISkillHandler handler = SkillHandler.getInstance().getHandler(triggered.getSkillType());
		
		_owner.broadcastPacket(new MagicSkillLaunched(_owner, triggered, targets));
		_owner.broadcastPacket(new MagicSkillUse(_owner, firstTarget, triggered.getId(), triggered.getLevel(), 0, 0));
		
		// Launch the magic skill and calculate its effects
		// TODO: once core will support all possible effects, use effects (not handler)
		if (handler != null)
			handler.useSkill(caster, triggered, targets, null);
		else
			triggered.useSkill(caster, targets);
	}
}