package com.shnok.javaserver.gameserver.skills.effects;

import java.util.List;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.model.actor.instance.EffectPoint;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSignet;

public class EffectSignetAntiSummon extends AbstractEffect
{
	private EffectPoint _actor;
	
	public EffectSignetAntiSummon(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SIGNET_GROUND;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(_skill instanceof L2SkillSignet))
			return false;
		
		_actor = (EffectPoint) getEffected();
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getCount() == getTemplate().getCounter() - 1)
			return true; // do nothing first time
			
		final List<Summon> list = _actor.getKnownTypeInRadius(Summon.class, _skill.getSkillRadius(), summon -> !summon.isDead() && !summon.isInsideZone(ZoneId.PEACE));
		if (list.isEmpty())
			return true;
		
		final Summon[] targets = list.toArray(new Summon[list.size()]);
		for (Summon summon : targets)
		{
			summon.broadcastPacket(new MagicSkillUse(summon, _skill.getId(), _skill.getLevel(), 0, 0));
			summon.unSummon(summon.getOwner());
		}
		_actor.broadcastPacket(new MagicSkillLaunched(_actor, _skill, targets));
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (_actor != null)
			_actor.deleteMe();
	}
}