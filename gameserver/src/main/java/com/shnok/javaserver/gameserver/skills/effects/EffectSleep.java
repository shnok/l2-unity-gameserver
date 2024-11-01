package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectSleep extends AbstractEffect
{
	public EffectSleep(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SLEEP;
	}
	
	@Override
	public boolean onStart()
	{
		// Abort attack, cast and move.
		getEffected().abortAll(false);
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (!(getEffected() instanceof Player))
			getEffected().getAI().notifyEvent(AiEventType.THINK, null, null);
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public boolean onSameEffect(AbstractEffect effect)
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.SLEEP.getMask();
	}
}