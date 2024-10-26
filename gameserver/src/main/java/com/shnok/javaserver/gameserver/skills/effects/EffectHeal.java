package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectHeal extends AbstractEffect
{
	public EffectHeal(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.HEAL;
	}
	
	@Override
	public boolean onStart()
	{
		if (!getEffected().canBeHealed())
			return false;
		
		final double power = getTemplate().getValue() + getEffected().getStatus().calcStat(Stats.HEAL_PROFICIENCY, 0, null, null);
		final double amount = getEffected().getStatus().addHp(power * getEffected().getStatus().calcStat(Stats.HEAL_EFFECTIVNESS, 100, null, null) / 100.);
		
		getEffected().getStatus().addHp(amount);
		
		if (getEffected() instanceof Player targetPlayer)
		{
			if (getEffector() != targetPlayer)
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_HP_RESTORED_BY_S1).addCharName(getEffector()).addNumber((int) amount));
			else
				targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HP_RESTORED).addNumber((int) amount));
		}
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}