package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.enums.SiegeSide;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.residence.castle.Castle;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class TakeCastle implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.TAKE_CASTLE
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be called by a Player.
		if (!(creature instanceof Player player))
			return;
		
		if (targets.length == 0)
			return;
		
		if (!player.isClanLeader())
			return;
		
		final Castle castle = check(player, targets[0], skill, false);
		if (castle == null)
			return;
		
		castle.engrave(player.getClan(), targets[0]);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param target : The {@link WorldObject} to test.
	 * @param skill : The {@link L2Skill} to test.
	 * @param announce : If True, broadcast to SiegeSide.DEFENDER than opponents started to engrave.
	 * @return The {@link Castle} affiliated to the {@link WorldObject} target, or null if operation aborted for a condition or another.
	 */
	public static Castle check(Player player, WorldObject target, L2Skill skill, boolean announce)
	{
		final Castle castle = CastleManager.getInstance().getCastle(player);
		if (castle == null || castle.getId() <= 0)
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
		else if (!castle.isGoodArtifact(target))
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.INVALID_TARGET));
		else if (!castle.getSiege().isInProgress())
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
		else if (!player.isIn3DRadius(target, 200))
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED));
		else if (!player.isInsideZone(ZoneId.CAST_ON_ARTIFACT))
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
		else if (!castle.getSiege().checkSide(player.getClan(), SiegeSide.ATTACKER))
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(skill));
		else
		{
			if (announce)
				castle.getSiege().announce(SystemMessageId.OPPONENT_STARTED_ENGRAVING, SiegeSide.DEFENDER);
			
			return castle;
		}
		return null;
	}
}