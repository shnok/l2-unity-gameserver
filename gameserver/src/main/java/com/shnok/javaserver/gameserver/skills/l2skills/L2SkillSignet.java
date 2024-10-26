package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.xml.NpcData;
import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.idfactory.IdFactory;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.EffectPoint;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class L2SkillSignet extends L2Skill
{
	public final int effectNpcId;
	public final int effectId;
	
	public L2SkillSignet(StatSet set)
	{
		super(set);
		effectNpcId = set.getInteger("effectNpcId", -1);
		effectId = set.getInteger("effectId", -1);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		final NpcTemplate template = NpcData.getInstance().getTemplate(effectNpcId);
		if (template == null)
			return;
		
		final EffectPoint effectPoint = new EffectPoint(IdFactory.getInstance().getNextId(), template, creature);
		effectPoint.getStatus().setMaxHpMp();
		
		Location worldPosition = null;
		if (creature instanceof Player player && getTargetType() == SkillTargetType.GROUND)
			worldPosition = player.getCast().getSignetLocation();
		
		getEffects(creature, effectPoint);
		
		effectPoint.setInvul(true);
		effectPoint.spawnMe((worldPosition != null) ? worldPosition : creature.getPosition());
	}
}