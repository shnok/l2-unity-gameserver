package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.skills.AbnormalEffect;
import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectParalyze extends AbstractEffect
{
	public EffectParalyze(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.PARALYZE;
	}
	
	@Override
	public boolean onStart()
	{
		getEffected().startAbnormalEffect(AbnormalEffect.HOLD_1);
		
		// Abort attack, cast and move.
		getEffected().abortAll(false);
		
		return true;
	}
	
	@Override
	public void onExit()
	{
		getEffected().stopAbnormalEffect(AbnormalEffect.HOLD_1);
		
		if (!(getEffected() instanceof Player))
			getEffected().getAI().notifyEvent(AiEventType.THINK, null, null);
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.PARALYZED.getMask();
	}
}