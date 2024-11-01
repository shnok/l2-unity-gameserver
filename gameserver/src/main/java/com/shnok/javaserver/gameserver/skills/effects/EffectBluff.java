package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.instance.Folk;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeSummon;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.StartRotation;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.StopRotation;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectBluff extends AbstractEffect
{
	public EffectBluff(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.BLUFF;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected() instanceof SiegeSummon || getEffected() instanceof Folk || getEffected().isRaidRelated() || (getEffected() instanceof Npc targetNpc && targetNpc.getNpcId() == 35062))
			return false;
		
		getEffected().broadcastPacket(new StartRotation(getEffected().getObjectId(), getEffected().getHeading(), 1, 65535));
		getEffected().broadcastPacket(new StopRotation(getEffected().getObjectId(), getEffector().getHeading(), 65535));
		getEffected().getPosition().setHeading(getEffector().getHeading());
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
}