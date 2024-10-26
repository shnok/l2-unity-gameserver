package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public final class RequestExMagicSkillUseGround extends L2GameClientPacket
{
	private int _x;
	private int _y;
	private int _z;
	
	private int _skillId;
	
	private boolean _ctrlPressed;
	private boolean _shiftPressed;
	
	@Override
	protected void readImpl()
	{
		_x = readD();
		_y = readD();
		_z = readD();
		
		_skillId = readD();
		
		_ctrlPressed = readD() != 0;
		_shiftPressed = readC() != 0;
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final L2Skill skill = player.getSkill(_skillId);
		if (skill == null || skill.getTargetType() != SkillTargetType.GROUND)
			return;
		
		player.getCast().getSignetLocation().set(_x, _y, GeoEngine.getInstance().getHeight(_x, _y, _z));
		
		player.getAI().tryToCast(player, skill, _ctrlPressed, _shiftPressed, 0);
	}
}