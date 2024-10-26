package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.SealType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHall;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * An instance type extending {@link Doorman}, used by clan hall doorman.<br>
 * <br>
 * isOwnerClan() checks if the user is part of clan owning the clan hall.
 */
public class ClanHallDoorman extends Doorman
{
	public ClanHallDoorman(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("wyvern_info"))
		{
			if (!canProvideWyvernService())
				return;
			
			if (!isOwnerClan(player))
				return;
			
			sendHtm(player, "1");
		}
		else if (command.startsWith("wyvern_help"))
		{
			if (!canProvideWyvernService())
				return;
			
			if (!isOwnerClan(player))
				return;
			
			sendHtm(player, "7");
		}
		else if (command.startsWith("wyvern_ride"))
		{
			if (!canProvideWyvernService())
				return;
			
			if (!isOwnerClan(player))
				return;
			
			if (!player.isClanLeader())
				return;
			
			// Verify if Dusk owns the Seal of Strife (if true, CLs can't mount Wyvern).
			if (SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE) == CabalType.DUSK)
			{
				sendHtm(player, "3");
				return;
			}
			
			// Check if the Player is mounted on a Strider.
			if (!player.isMounted() || (player.getMountNpcId() != 12526 && player.getMountNpcId() != 12527 && player.getMountNpcId() != 12528))
			{
				if (player.getMountLevel() < Config.WYVERN_REQUIRED_LEVEL)
				{
					sendHtm(player, "8");
					return;
				}
				
				player.sendPacket(SystemMessageId.YOU_MAY_ONLY_RIDE_WYVERN_WHILE_RIDING_STRIDER);
				sendHtm(player, "1");
				return;
			}
			
			// Check for strider level.
			if (player.getMountLevel() < Config.WYVERN_REQUIRED_LEVEL)
			{
				sendHtm(player, "6");
				return;
			}
			
			// Check for items consumption.
			if (!player.destroyItemByItemId(1460, Config.WYVERN_REQUIRED_CRYSTALS, true))
			{
				sendHtm(player, "5");
				return;
			}
			
			// Dismount the Strider.
			player.dismount();
			
			// Mount a Wyvern. If successful, call an HTM.
			if (player.mount(12621, 0))
				sendHtm(player, "4");
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		if (getClanHall() == null)
			return;
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		
		final Clan owner = ClanTable.getInstance().getClan(getClanHall().getOwnerId());
		if (isOwnerClan(player))
		{
			if (canProvideWyvernService())
				html.setFile("data/html/clanHallDoormen/doormen_wyvern.htm");
			else
				html.setFile("data/html/clanHallDoormen/doormen.htm");
			html.replace("%clanname%", owner.getName());
		}
		else
		{
			if (owner != null && owner.getLeader() != null)
			{
				html.setFile("data/html/clanHallDoormen/doormen-no.htm");
				html.replace("%leadername%", owner.getLeaderName());
				html.replace("%clanname%", owner.getName());
			}
			else
			{
				html.setFile("data/html/clanHallDoormen/emptyowner.htm");
				html.replace("%hallname%", getClanHall().getName());
			}
		}
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		showChatWindow(player);
	}
	
	@Override
	protected final void openDoors(Player player, String command)
	{
		getClanHall().openDoors();
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/clanHallDoormen/doormen-opened.htm");
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	@Override
	protected final void closeDoors(Player player, String command)
	{
		getClanHall().closeDoors();
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/clanHallDoormen/doormen-closed.htm");
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	@Override
	protected final boolean isOwnerClan(Player player)
	{
		return getClanHall() != null && player.getClan() != null && player.getClanId() == getClanHall().getOwnerId();
	}
	
	private void sendHtm(Player player, String val)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/clanHallDoormen/wyvernmanager-" + val + ".htm");
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		html.replace("%wyvern_level%", Config.WYVERN_REQUIRED_LEVEL);
		html.replace("%needed_crystals%", Config.WYVERN_REQUIRED_CRYSTALS);
		player.sendPacket(html);
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	private boolean canProvideWyvernService()
	{
		// Aden clan halls (ID 36-41) can provide wyvern services
		final ClanHall clanHall = getClanHall();
		return clanHall != null && clanHall.getId() >= 36 && clanHall.getId() <= 41;
	}
}