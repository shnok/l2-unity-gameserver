package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectFlag;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.EtcStatusUpdate;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectCharmOfCourage extends AbstractEffect
{
	public EffectCharmOfCourage(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.CHARM_OF_COURAGE;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof Player targetPlayer)
		{
			getEffected().broadcastPacket(new EtcStatusUpdate(targetPlayer));
			return true;
		}
		return false;
	}
	
	@Override
	public void onExit()
	{
		if (getEffected() instanceof Player targetPlayer)
			getEffected().broadcastPacket(new EtcStatusUpdate(targetPlayer));
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.CHARM_OF_COURAGE.getMask();
	}
}