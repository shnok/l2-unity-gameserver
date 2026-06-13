package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.enums.actors.NpcTalkCond;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class CastleBlacksmith extends Folk
{
	public CastleBlacksmith(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (!Config.ALLOW_MANOR)
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/npcdefault.htm");
			html.replace("%objectId%", getObjectId());
			html.replace("%npcname%", getName());
			player.sendPacket(html);
			return;
		}
		
		if (getNpcTalkCond(player) != NpcTalkCond.OWNER)
			return;
		
		if (command.startsWith("Chat"))
		{
			int val = 0;
			try
			{
				val = Integer.parseInt(command.substring(5));
			}
			catch (IndexOutOfBoundsException | NumberFormatException e)
			{
				// Do nothing.
			}
			showChatWindow(player, val);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		if (!Config.ALLOW_MANOR)
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/npcdefault.htm");
			html.replace("%objectId%", getObjectId());
			html.replace("%npcname%", getName());
			player.sendPacket(html);
			return;
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		
		switch (getNpcTalkCond(player))
		{
			case NONE:
				html.setFile("data/html/castleblacksmith/castleblacksmith-no.htm");
				break;
			
			case UNDER_SIEGE:
				html.setFile("data/html/castleblacksmith/castleblacksmith-busy.htm");
				break;
			
			default:
				if (val == 0)
					html.setFile("data/html/castleblacksmith/castleblacksmith.htm");
				else
					html.setFile("data/html/castleblacksmith/castleblacksmith-" + val + ".htm");
				break;
		}
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		html.replace("%castleid%", getCastle().getId());
		player.sendPacket(html);
	}
	
	@Override
	protected NpcTalkCond getNpcTalkCond(Player player)
	{
		if (getCastle() != null && player.getClan() != null)
		{
			if (getCastle().getSiege().isInProgress())
				return NpcTalkCond.UNDER_SIEGE;
			
			if (getCastle().getOwnerId() == player.getClanId() && player.hasClanPrivileges(PrivilegeType.CP_MANOR_ADMINISTRATION))
				return NpcTalkCond.OWNER;
		}
		return NpcTalkCond.NONE;
	}
}