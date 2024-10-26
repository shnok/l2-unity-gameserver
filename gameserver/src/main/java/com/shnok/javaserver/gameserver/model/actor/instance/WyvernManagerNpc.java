package com.shnok.javaserver.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.SevenSignsManager;
import net.sf.l2j.gameserver.enums.CabalType;
import net.sf.l2j.gameserver.enums.SealType;
import net.sf.l2j.gameserver.enums.actors.NpcTalkCond;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

/**
 * This instance leads the behavior of Wyvern Managers.<br>
 * Those NPCs allow Castle Lords to mount a wyvern in return for B Crystals.<br>
 * Three configs exist so far :<br>
 * <ul>
 * <li>WYVERN_ALLOW_UPGRADER : spawn instances of Wyvern Manager through the world, or no;</li>
 * <li>WYVERN_REQUIRED_LEVEL : the strider's required level;</li>
 * <li>WYVERN_REQUIRED_CRYSTALS : the B-crystals' required amount;</li>
 * </ul>
 */
public class WyvernManagerNpc extends CastleChamberlain
{
	public WyvernManagerNpc(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (player.getCurrentFolk() == null || player.getCurrentFolk().getObjectId() != getObjectId())
			return;
		
		if (command.startsWith("RideWyvern"))
		{
			if (!isLordOwner(player))
			{
				sendHtm(player, "2");
				return;
			}
			
			// Verify if Dusk owns the Seal of Strife (if true, CLs can't mount Wyvern).
			if (SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE) == CabalType.DUSK)
			{
				sendHtm(player, "3");
				return;
			}
			
			// Check if the Player is mounted on a Strider.
			if (!player.isMounted() || (player.getMountNpcId() != 12526 && player.getMountNpcId() != 12527 && player.getMountNpcId() != 12528))
			{
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
		else if (command.startsWith("Chat"))
		{
			// Default send you to error HTM.
			String val = "1";
			try
			{
				val = command.substring(5);
			}
			catch (IndexOutOfBoundsException ioobe)
			{
				// Do nothing.
			}
			
			sendHtm(player, val);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		switch (getNpcTalkCond(player))
		{
			case OWNER:
				sendHtm(player, (player.isFlying()) ? "4" : "0");
				break;
			
			case CLAN_MEMBER:
				sendHtm(player, "2");
				break;
			
			default:
				sendHtm(player, "0a");
				break;
		}
	}
	
	@Override
	protected NpcTalkCond getNpcTalkCond(Player player)
	{
		if (player.getClan() != null && getResidence() != null && getResidence().getOwnerId() == player.getClanId())
			return (player.isClanLeader()) ? NpcTalkCond.OWNER : NpcTalkCond.CLAN_MEMBER;
		
		return NpcTalkCond.NONE;
	}
	
	private void sendHtm(Player player, String val)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/wyvernmanager/wyvernmanager-" + val + ".htm");
		html.replace("%objectId%", getObjectId());
		html.replace("%npcname%", getName());
		html.replace("%wyvern_level%", Config.WYVERN_REQUIRED_LEVEL);
		html.replace("%needed_crystals%", Config.WYVERN_REQUIRED_CRYSTALS);
		player.sendPacket(html);
		
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
}