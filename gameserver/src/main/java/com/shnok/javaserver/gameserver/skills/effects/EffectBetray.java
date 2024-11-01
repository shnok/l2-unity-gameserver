package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectBetray extends AbstractEffect
{
	public EffectBetray(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.BETRAY;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(getEffected() instanceof Summon summon))
			return false;
		
		final Player player = summon.getOwner();
		if (player == null)
			return false;
		
		summon.getAI().tryToAttack(player, false, false);
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (!(getEffected() instanceof Summon summon))
			return;
		
		final Player player = summon.getOwner();
		if (player == null)
			return;
		
		summon.getAI().tryToFollow(player, false);
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.BETRAYED.getMask();
	}
}