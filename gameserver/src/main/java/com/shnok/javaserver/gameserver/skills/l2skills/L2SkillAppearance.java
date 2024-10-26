package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.skills.L2Skill;

public class L2SkillAppearance extends L2Skill
{
	private final int _faceId;
	private final int _hairColorId;
	private final int _hairStyleId;
	
	public L2SkillAppearance(StatSet set)
	{
		super(set);
		
		_faceId = set.getInteger("faceId", -1);
		_hairColorId = set.getInteger("hairColorId", -1);
		_hairStyleId = set.getInteger("hairStyleId", -1);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		for (WorldObject target : targets)
		{
			if (target instanceof Player targetPlayer)
			{
				if (_faceId >= 0)
					targetPlayer.getAppearance().setFace(_faceId);
				if (_hairColorId >= 0)
					targetPlayer.getAppearance().setHairColor(_hairColorId);
				if (_hairStyleId >= 0)
					targetPlayer.getAppearance().setHairStyle(_hairStyleId);
				
				targetPlayer.broadcastUserInfo();
			}
		}
	}
}