package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectNoblesseBless extends AbstractEffect
{
	public EffectNoblesseBless(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.NOBLESSE_BLESSING;
	}
	
	@Override
	public boolean onStart()
	{
		return true;
	}
	
	@Override
	public void onExit()
	{
		// Do nothing.
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.NOBLESS_BLESSING.getMask();
	}
}