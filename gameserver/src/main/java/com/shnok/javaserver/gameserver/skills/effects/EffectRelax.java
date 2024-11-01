package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectRelax extends AbstractEffect
{
	public EffectRelax(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.RELAXING;
	}
	
	@Override
	public boolean onStart()
	{
		((Player) getEffected()).sitDown();
		
		return super.onStart();
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getEffected().isDead())
			return false;
		
		if (!((Player) getEffected()).isSitting() && !((Player) getEffected()).isSittingNow())
			return false;
		
		if (getEffected().getStatus().getHp() + 1 > getEffected().getStatus().getMaxHp())
		{
			getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_DEACTIVATED_HP_FULL));
			return false;
		}
		
		if (getTemplate().getValue() > getEffected().getStatus().getMp())
		{
			getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
			return false;
		}
		
		getEffected().getStatus().reduceMp(getTemplate().getValue());
		return true;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.RELAXING.getMask();
	}
}