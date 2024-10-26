package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectManaHeal extends AbstractEffect
{
	public EffectManaHeal(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.MANA_HEAL;
	}
	
	@Override
	public boolean onStart()
	{
		if (!getEffected().canBeHealed())
			return false;
		
		final double power = getEffected().getStatus().calcStat(Stats.RECHARGE_MP_RATE, getTemplate().getValue(), null, null);
		final double amount = getEffected().getStatus().addMp(power);
		
		getEffected().getStatus().addMp(amount);
		
		if (getEffected() instanceof Player targetPlayer)
		{
			if (getEffector() != targetPlayer)
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_MP_RESTORED_BY_S1).addCharName(getEffector()).addNumber((int) amount));
			else
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_MP_RESTORED).addNumber((int) amount));
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}