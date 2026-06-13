package com.shnok.javaserver.gameserver.handler.targethandlers;

import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.handler.ITargetHandler;
import com.shnok.javaserver.gameserver.model.WorldRegion;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.ValidateLocation;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TargetGround implements ITargetHandler
{
	@Override
	public SkillTargetType getTargetType()
	{
		return SkillTargetType.GROUND;
	}
	
	@Override
	public Creature[] getTargetList(Creature caster, Creature target, L2Skill skill)
	{
		return new Creature[]
		{
			caster
		};
	}
	
	@Override
	public Creature getFinalTarget(Creature caster, Creature target, L2Skill skill)
	{
		return caster;
	}
	
	@Override
	public boolean meetCastConditions(Playable caster, Creature target, L2Skill skill, boolean isCtrlPressed)
	{
		final WorldRegion region = caster.getRegion();
		if (region == null || !(caster instanceof Player player))
			return false;
		
		final Location signetLocation = player.getCast().getSignetLocation();
		if (!GeoEngine.getInstance().canSeeLocation(player, signetLocation))
		{
			player.sendPacket(SystemMessageId.CANT_SEE_TARGET);
			return false;
		}
		
		if (!region.checkEffectRangeInsidePeaceZone(skill, signetLocation))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
			return false;
		}
		
		player.getPosition().setHeadingTo(signetLocation);
		player.broadcastPacket(new ValidateLocation(player));
		return true;
	}
}