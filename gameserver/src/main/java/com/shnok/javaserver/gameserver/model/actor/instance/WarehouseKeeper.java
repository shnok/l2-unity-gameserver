package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.Map;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.itemcontainer.PcFreight;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.PackageToList;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.WarehouseDepositList;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.WarehouseWithdrawList;

/**
 * An instance type extending {@link Folk}, used by warehouse keepers.<br>
 * <br>
 * A warehouse keeper stores {@link Player} items in a personal container.
 */
public class WarehouseKeeper extends Folk
{
	public WarehouseKeeper(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isWarehouse()
	{
		return true;
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/warehouse/" + filename + ".htm";
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		// Generic PK check. Send back the HTM if found and cancel current action.
		if (!Config.KARMA_PLAYER_CAN_USE_WH && player.getKarma() > 0 && showPkDenyChatWindow(player, "warehouse"))
			return;
		
		if (player.isProcessingTransaction())
		{
			player.sendPacket(SystemMessageId.ALREADY_TRADING);
			return;
		}
		
		player.cancelActiveEnchant();
		
		if (command.startsWith("WithdrawP"))
		{
			player.setActiveWarehouse(player.getWarehouse());
			
			if (player.getActiveWarehouse().getSize() == 0)
			{
				player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
				return;
			}
			
			player.sendPacket(new WarehouseWithdrawList(player, WarehouseWithdrawList.PRIVATE));
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else if (command.equals("DepositP"))
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.setActiveWarehouse(player.getWarehouse());
			player.tempInventoryDisable();
			player.sendPacket(new WarehouseDepositList(player, WarehouseDepositList.PRIVATE));
		}
		else if (command.equals("WithdrawC"))
		{
			if (!player.hasClanPrivileges(PrivilegeType.SP_WAREHOUSE_SEARCH))
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE);
				return;
			}
			
			final Clan clan = player.getClan();
			if (clan == null || clan.getLevel() == 0)
			{
				player.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
				return;
			}
			
			player.setActiveWarehouse(clan.getWarehouse());
			
			if (player.getActiveWarehouse().getSize() == 0)
			{
				player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
				return;
			}
			
			player.sendPacket(new WarehouseWithdrawList(player, WarehouseWithdrawList.CLAN));
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else if (command.equals("DepositC"))
		{
			final Clan clan = player.getClan();
			if (clan == null || clan.getLevel() == 0)
			{
				player.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
				return;
			}
			
			player.setActiveWarehouse(clan.getWarehouse());
			player.tempInventoryDisable();
			player.sendPacket(new WarehouseDepositList(player, WarehouseDepositList.CLAN));
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else if (command.startsWith("WithdrawF"))
		{
			if (!Config.ALLOW_FREIGHT)
				return;
			
			player.sendPacket(ActionFailed.STATIC_PACKET);
			
			final PcFreight freight = player.getFreight();
			if (freight == null || freight.getSize() <= 0)
			{
				player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
				return;
			}
			
			freight.setActiveLocation((Config.REGION_BASED_FREIGHT) ? getRegion().hashCode() : 0);
			
			player.setActiveWarehouse(freight);
			player.sendPacket(new WarehouseWithdrawList(player, WarehouseWithdrawList.FREIGHT));
		}
		else if (command.startsWith("DepositF"))
		{
			if (!Config.ALLOW_FREIGHT)
				return;
			
			player.sendPacket(ActionFailed.STATIC_PACKET);
			
			// No other chars in the account of this Player.
			final Map<Integer, String> chars = player.getAccountChars();
			if (chars.isEmpty())
			{
				player.sendPacket(SystemMessageId.CHARACTER_DOES_NOT_EXIST);
				return;
			}
			
			// One or more chars other than this Player for this account.
			if (chars.size() < 1)
				return;
			
			player.sendPacket(new PackageToList(chars));
		}
		else if (command.startsWith("FreightChar"))
		{
			if (!Config.ALLOW_FREIGHT)
				return;
			
			final String id = command.substring(command.lastIndexOf("_") + 1);
			final PcFreight freight = player.getDepositedFreight(Integer.parseInt(id));
			freight.setActiveLocation((Config.REGION_BASED_FREIGHT) ? getRegion().hashCode() : 0);
			
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.setActiveWarehouse(freight);
			player.tempInventoryDisable();
			player.sendPacket(new WarehouseDepositList(player, WarehouseDepositList.FREIGHT));
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		// Generic PK check. Send back the HTM if found and cancel current action.
		if (!Config.KARMA_PLAYER_CAN_USE_WH && player.getKarma() > 0 && showPkDenyChatWindow(player, "warehouse"))
			return;
		
		showChatWindow(player, getHtmlPath(getNpcId(), val));
	}
}