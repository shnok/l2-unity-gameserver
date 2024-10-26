package com.shnok.javaserver.gameserver.skills.effects;

import java.util.List;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.EffectPoint;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillLaunched;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.l2skills.L2SkillSignet;

public class EffectSignet extends AbstractEffect
{
	private EffectPoint _actor;
	
	public EffectSignet(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SIGNET_EFFECT;
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
		final List<Creature> list = _actor.getKnownTypeInRadius(Creature.class, _skill.getSkillRadius(), creature -> !creature.isDead() && !(creature instanceof Door) && !creature.isInsideZone(ZoneId.PEACE));
		if (list.isEmpty())
			return true;
		
		final L2Skill signetSkill = SkillTable.getInstance().getInfo(((L2SkillSignet) _skill).effectId, _skill.getLevel());
		final Creature[] targets = list.toArray(new Creature[list.size()]);
		for (Creature creature : targets)
		{
			signetSkill.getEffects(_actor, creature);
			_actor.broadcastPacket(new MagicSkillUse(_actor, creature, signetSkill.getId(), signetSkill.getLevel(), 0, 0));
		}
		_actor.broadcastPacket(new MagicSkillLaunched(_actor, signetSkill, targets));
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (_actor != null)
			_actor.deleteMe();
	}
}