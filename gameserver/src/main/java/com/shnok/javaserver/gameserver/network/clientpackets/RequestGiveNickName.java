package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.enums.PrivilegeType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class RequestGiveNickName extends L2GameClientPacket
{
	private String _name;
	private String _title;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_title = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (!StringUtil.isValidString(_title, "^[a-zA-Z0-9 !@#$&()\\-`.+,/\"]*{0,16}$"))
		{
			player.sendPacket(SystemMessageId.NOT_WORKING_PLEASE_TRY_AGAIN_LATER);
			return;
		}
		
		// Noblesse can bestow a title to themselves
		if (player.isNoble() && _name.matches(player.getName()))
		{
			player.setTitle(_title);
			player.sendPacket(SystemMessageId.TITLE_CHANGED);
			player.broadcastTitleInfo();
		}
		else
		{
			// Can the player change/give a title?
			if (!player.hasClanPrivileges(PrivilegeType.SP_MANAGE_TITLES))
			{
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
				return;
			}
			
			if (player.getClan().getLevel() < 3)
			{
				player.sendPacket(SystemMessageId.CLAN_LVL_3_NEEDED_TO_ENDOWE_TITLE);
				return;
			}
			
			final ClanMember member = player.getClan().getClanMember(_name);
			if (member != null)
			{
				final Player playerMember = member.getPlayerInstance();
				if (playerMember != null)
				{
					playerMember.setTitle(_title);
					
					playerMember.sendPacket(SystemMessageId.TITLE_CHANGED);
					if (player != playerMember)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CLAN_MEMBER_S1_TITLE_CHANGED_TO_S2).addCharName(playerMember).addString(_title));
					
					playerMember.broadcastTitleInfo();
				}
				else
					player.sendPacket(SystemMessageId.TARGET_IS_NOT_FOUND_IN_THE_GAME);
			}
			else
				player.sendPacket(SystemMessageId.TARGET_MUST_BE_IN_CLAN);
		}
	}
}