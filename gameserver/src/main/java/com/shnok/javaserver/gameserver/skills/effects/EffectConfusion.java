package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.skills.EffectFlag;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Chest;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectConfusion extends AbstractEffect
{
	public EffectConfusion(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.CONFUSION;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof Player)
			return true;
		
		// Abort move.
		getEffected().getMove().stop();
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		// Find a random target from known Attackables (without doors nor chests) and Playables.
		final Creature target = Rnd.get(getEffected().getKnownType(Creature.class, wo -> (wo instanceof Attackable || wo instanceof Playable) && wo != getEffected() && !(wo instanceof Door || wo instanceof Chest) && wo.distance2D(getEffected()) <= 1000));
		if (target == null)
			return true;
		
		if (getEffected() instanceof Playable targetPlayable)
			targetPlayable.getAI().tryToAttack(target, false, false);
		else if (getEffected() instanceof Npc targetNpc)
			targetNpc.getAI().addAttackDesire(target, Integer.MAX_VALUE);
		
		return true;
	}
	
	@Override
	public void onExit()
	{
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		if (getEffected() instanceof Playable targetPlayable)
			targetPlayable.getAI().tryToFollow(getEffected().getActingPlayer(), false);
		else if (getEffected() instanceof Npc targetNpc)
			targetNpc.getAI().getAggroList().stopHate(targetNpc.getAI().getAggroList().getMostHatedCreature());
		
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.CONFUSED.getMask();
	}
}