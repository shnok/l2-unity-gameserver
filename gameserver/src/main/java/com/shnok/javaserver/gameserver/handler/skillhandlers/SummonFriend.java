package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ConfirmDlg;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class SummonFriend implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.SUMMON_FRIEND,
		SkillType.SUMMON_PARTY,
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be called by a Player.
		if (!(creature instanceof Player player))
			return;
		
		// Check player status.
		if (!checkSummoner(player))
			return;
		
		// Bypass target and stuff, simply retrieve Party exclude caster.
		if (skill.getSkillType() == SkillType.SUMMON_PARTY)
		{
			final Party party = player.getParty();
			if (party == null)
				return;
			
			for (Player member : party.getMembers())
			{
				if (member == player)
					continue;
				
				// Check target status.
				if (!checkSummoned(player, member))
					continue;
				
				teleportTo(member, player, skill);
			}
		}
		else
		{
			for (WorldObject obj : targets)
			{
				// The target must be a player.
				if (!(obj instanceof Player))
					continue;
				
				final Player target = ((Player) obj);
				
				// Check target status.
				if (!checkSummoned(player, target))
					continue;
				
				// Check target teleport request status.
				if (!target.teleportRequest(player, skill))
				{
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_ALREADY_SUMMONED).addCharName(target));
					continue;
				}
				
				// Send a request for Summon Friend skill.
				if (skill.getId() == 1403)
				{
					final ConfirmDlg confirm = new ConfirmDlg(SystemMessageId.S1_WISHES_TO_SUMMON_YOU_FROM_S2_DO_YOU_ACCEPT.getId());
					confirm.addCharName(player);
					confirm.addZoneName(player.getPosition());
					confirm.addTime(30000);
					confirm.addRequesterId(player.getObjectId());
					target.sendPacket(confirm);
				}
				else
				{
					teleportTo(target, player, skill);
					target.teleportRequest(null, null);
				}
			}
		}
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
	
	/**
	 * Test if the current {@link Player} can summon. Send back messages if he can't.
	 * @param player : The {@link Player} to test.
	 * @return True if the {@link Player} can summon, false otherwise.
	 */
	public static boolean checkSummoner(Player player)
	{
		if (player.isMounted())
			return false;
		
		if (player.isInOlympiadMode() || player.isInObserverMode() || player.isInsideZone(ZoneId.NO_SUMMON_FRIEND))
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_SUMMON_FROM_YOUR_CURRENT_LOCATION);
			return false;
		}
		return true;
	}
	
	/**
	 * Test if the {@link WorldObject} can be summoned. Send back messages if he can't.
	 * @param player : The {@link Player} to test.
	 * @param target : The {@link WorldObject} to test.
	 * @return True if the given {@link WorldObject} can be summoned, false otherwise.
	 */
	public static boolean checkSummoned(Player player, WorldObject target)
	{
		if (!(target instanceof Player))
			return false;
		
		final Player targetPlayer = (Player) target;
		
		if (targetPlayer == player)
		{
			player.sendPacket(SystemMessageId.CANNOT_USE_ON_YOURSELF);
			return false;
		}
		
		if (targetPlayer.isAlikeDead())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_DEAD_AT_THE_MOMENT_AND_CANNOT_BE_SUMMONED).addCharName(targetPlayer));
			return false;
		}
		
		if (targetPlayer.isOperating())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CURRENTLY_TRADING_OR_OPERATING_PRIVATE_STORE_AND_CANNOT_BE_SUMMONED).addCharName(targetPlayer));
			return false;
		}
		
		if (targetPlayer.isRooted() || targetPlayer.isInCombat())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_ENGAGED_IN_COMBAT_AND_CANNOT_BE_SUMMONED).addCharName(targetPlayer));
			return false;
		}
		
		if (targetPlayer.isInOlympiadMode())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_SUMMON_PLAYERS_WHO_ARE_IN_OLYMPIAD);
			return false;
		}
		
		if (targetPlayer.isFestivalParticipant() || targetPlayer.isMounted())
		{
			player.sendPacket(SystemMessageId.YOUR_TARGET_IS_IN_AN_AREA_WHICH_BLOCKS_SUMMONING);
			return false;
		}
		
		if (targetPlayer.isInObserverMode() || targetPlayer.isInsideZone(ZoneId.NO_SUMMON_FRIEND))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IN_SUMMON_BLOCKING_AREA).addCharName(targetPlayer));
			return false;
		}
		return true;
	}
	
	/**
	 * Teleport the current {@link Player} to the destination of another player.<br>
	 * <br>
	 * Check if summoning is allowed, and consume items if {@link L2Skill} got such constraints.
	 * @param player : The {@link Player} which requests the teleport.
	 * @param target : The {@link Player} to teleport on.
	 * @param skill : The {@link L2Skill} used to find item consumption informations.
	 */
	public static void teleportTo(Player player, Player target, L2Skill skill)
	{
		if (!checkSummoner(player) || !checkSummoned(player, target))
			return;
		
		if (skill.getTargetConsumeId() > 0 && skill.getTargetConsume() > 0)
		{
			if (player.getInventory().getItemCount(skill.getTargetConsumeId()) < skill.getTargetConsume())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_REQUIRED_FOR_SUMMONING).addItemName(skill.getTargetConsumeId()));
				return;
			}
			
			player.destroyItemByItemId(skill.getTargetConsumeId(), skill.getTargetConsume(), true);
		}
		player.teleportTo(target.getPosition(), 20);
	}
}