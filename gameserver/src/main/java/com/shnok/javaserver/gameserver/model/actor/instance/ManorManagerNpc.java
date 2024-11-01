package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.StringTokenizer;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.BuyListSeed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExShowCropInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExShowManorDefaultInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExShowProcureCropDetail;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExShowSeedInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExShowSellCropList;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class ManorManagerNpc extends Merchant
{
	public ManorManagerNpc(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("manor_menu_select"))
		{
			if (CastleManorManager.getInstance().isUnderMaintenance())
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				player.sendPacket(SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
				return;
			}
			
			final StringTokenizer st = new StringTokenizer(command, "&");
			
			final int ask = Integer.parseInt(st.nextToken().split("=")[1]);
			final int state = Integer.parseInt(st.nextToken().split("=")[1]);
			final boolean time = st.nextToken().split("=")[1].equals("1");
			
			final int castleId = (state < 0) ? getCastle().getId() : state;
			
			switch (ask)
			{
				case 1: // Seed purchase
					if (castleId != getCastle().getId())
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.HERE_YOU_CAN_BUY_ONLY_SEEDS_OF_S1_MANOR).addString(getCastle().getName()));
					else
						player.sendPacket(new BuyListSeed(player.getAdena(), castleId));
					break;
				
				case 2: // Crop sales
					player.sendPacket(new ExShowSellCropList(player.getInventory(), castleId));
					break;
				
				case 3: // Current seeds (Manor info)
					player.sendPacket(new ExShowSeedInfo(castleId, time, false));
					break;
				
				case 4: // Current crops (Manor info)
					player.sendPacket(new ExShowCropInfo(castleId, time, false));
					break;
				
				case 5: // Basic info (Manor info)
					player.sendPacket(new ExShowManorDefaultInfo(false));
					break;
				
				case 6: // Buy harvester
					showBuyWindow(player, 300000 + getNpcId());
					break;
				
				case 9: // Edit sales (Crop sales)
					player.sendPacket(new ExShowProcureCropDetail(state));
					break;
			}
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		return "data/html/manormanager/manager.htm";
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		if (!Config.ALLOW_MANOR)
		{
			showChatWindow(player, "data/html/npcdefault.htm");
			return;
		}
		
		if (getCastle() != null && player.getClan() != null && getCastle().getOwnerId() == player.getClanId() && player.isClanLeader())
			showChatWindow(player, "data/html/manormanager/manager-lord.htm");
		else
			showChatWindow(player, "data/html/manormanager/manager.htm");
	}
}
