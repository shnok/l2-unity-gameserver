package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.Calendar;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.enums.SealType;
import com.shnok.javaserver.gameserver.enums.actors.NpcTalkCond;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.ExShowCropInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.ExShowCropSetting;
import com.shnok.javaserver.gameserver.network.serverpackets.ExShowManorDefaultInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.ExShowSeedInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.ExShowSeedSetting;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.SiegeInfo;

/**
 * An instance type extending {@link Merchant}, used for castle chamberlains.<br>
 * <br>
 * It handles following actions :
 * <ul>
 * <li>Tax rate control</li>
 * <li>Regional manor system control</li>
 * <li>Castle treasure control</li>
 * <li>Siege time modifier</li>
 * <li>Items production</li>
 * <li>Doors management && Doors/walls upgrades</li>
 * <li>Traps management && upgrades</li>
 * </ul>
 */
public class CastleChamberlain extends Merchant
{
	private static final int CERTIFICATES_BUNDLE = 10;
	private static final int CERTIFICATES_PRICE = 1000;
	
	private int _preHour = 6;
	
	public CastleChamberlain(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		final NpcTalkCond condition = getNpcTalkCond(player);
		if (condition == NpcTalkCond.NONE)
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/chamberlain/noprivs.htm");
			player.sendPacket(html);
			return;
		}
		
		if (condition == NpcTalkCond.UNDER_SIEGE)
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/chamberlain/busy.htm");
			player.sendPacket(html);
			return;
		}
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		String val = "";
		if (st.hasMoreTokens())
			val = st.nextToken();
		
		if (actualCommand.equalsIgnoreCase("banish_foreigner"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_RIGHT_TO_DISMISS))
				return;
			
			// Move non-clan members off castle area, and send html
			getCastle().banishForeigners();
			sendFileMessage(player, "data/html/chamberlain/banishafter.htm");
		}
		else if (actualCommand.equalsIgnoreCase("banish_foreigner_show"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_RIGHT_TO_DISMISS))
				return;
			
			sendFileMessage(player, "data/html/chamberlain/banishfore.htm");
		}
		else if (actualCommand.equalsIgnoreCase("manage_functions"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			sendFileMessage(player, "data/html/chamberlain/manage.htm");
		}
		else if (actualCommand.equalsIgnoreCase("products"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_USE_FUNCTIONS))
				return;
			
			sendFileMessage(player, "data/html/chamberlain/products.htm");
		}
		else if (actualCommand.equalsIgnoreCase("list_siege_clans"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_MANAGE_SIEGE_WAR))
				return;
			
			player.sendPacket(new SiegeInfo(getCastle()));
		}
		else if (actualCommand.equalsIgnoreCase("receive_report"))
		{
			if (condition == NpcTalkCond.CLAN_MEMBER)
				sendFileMessage(player, "data/html/chamberlain/noprivs.htm");
			else
			{
				final Clan clan = ClanTable.getInstance().getClan(getCastle().getOwnerId());
				
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/chamberlain/report.htm");
				html.replace("%objectId%", getObjectId());
				html.replace("%clanname%", clan.getName());
				html.replace("%clanleadername%", clan.getLeaderName());
				html.replace("%castlename%", getCastle().getName());
				html.replace("%ss_event%", SevenSignsManager.getInstance().getCurrentPeriod().getName());
				
				switch (SevenSignsManager.getInstance().getSealOwner(SealType.AVARICE))
				{
					case NORMAL:
						html.replace("%ss_avarice%", "Not in Possession");
						break;
					
					case DAWN:
						html.replace("%ss_avarice%", "Lords of Dawn");
						break;
					
					case DUSK:
						html.replace("%ss_avarice%", "Revolutionaries of Dusk");
						break;
				}
				
				switch (SevenSignsManager.getInstance().getSealOwner(SealType.GNOSIS))
				{
					case NORMAL:
						html.replace("%ss_gnosis%", "Not in Possession");
						break;
					
					case DAWN:
						html.replace("%ss_gnosis%", "Lords of Dawn");
						break;
					
					case DUSK:
						html.replace("%ss_gnosis%", "Revolutionaries of Dusk");
						break;
				}
				
				switch (SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE))
				{
					case NORMAL:
						html.replace("%ss_strife%", "Not in Possession");
						break;
					
					case DAWN:
						html.replace("%ss_strife%", "Lords of Dawn");
						break;
					
					case DUSK:
						html.replace("%ss_strife%", "Revolutionaries of Dusk");
						break;
				}
				player.sendPacket(html);
			}
		}
		else if (actualCommand.equalsIgnoreCase("items"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_USE_FUNCTIONS))
				return;
			
			if (val.isEmpty())
				return;
			
			showBuyWindow(player, Integer.parseInt(val + "1"));
		}
		else if (actualCommand.equalsIgnoreCase("manage_vault"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_MANAGE_TAXES))
				return;
			
			String filename = "data/html/chamberlain/vault.htm";
			int amount = 0;
			if (st.hasMoreTokens())
				amount = Integer.parseInt(st.nextToken());
			
			if (amount > 0)
			{
				if (val.equalsIgnoreCase("deposit"))
				{
					if (getCastle().getTreasury() + amount < Integer.MAX_VALUE && player.reduceAdena(amount, true))
						getCastle().editTreasury(amount, true);
				}
				else if (val.equalsIgnoreCase("withdraw"))
				{
					if (getCastle().getTreasury() < amount)
						filename = "data/html/chamberlain/vault-no.htm";
					else if (getCastle().editTreasury(-amount, true))
						player.addAdena(amount, true);
				}
			}
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile(filename);
			html.replace("%objectId%", getObjectId());
			html.replace("%tax_income%", StringUtil.formatNumber(getCastle().getTreasury()));
			html.replace("%tax_income_reserved%", StringUtil.formatNumber(getCastle().getTaxRevenue()));
			html.replace("%seed_income%", StringUtil.formatNumber(getCastle().getSeedIncome()));
			html.replace("%withdraw_amount%", StringUtil.formatNumber(amount));
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("operate_door")) // door control
		{
			if (!validatePrivileges(player, PrivilegeType.CP_ENTRY_EXIT_RIGHTS))
				return;
			
			if (val.isEmpty())
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/chamberlain/" + getNpcId() + "-d.htm");
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
				return;
			}
			
			boolean open = (Integer.parseInt(val) == 1);
			while (st.hasMoreTokens())
			{
				if (open)
					getCastle().openDoor(player, Integer.parseInt(st.nextToken()));
				else
					getCastle().closeDoor(player, Integer.parseInt(st.nextToken()));
			}
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile((open) ? "data/html/chamberlain/doors-open.htm" : "data/html/chamberlain/doors-close.htm");
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
			
		}
		else if (actualCommand.equalsIgnoreCase("tax_set")) // tax rates control
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			if (!validatePrivileges(player, PrivilegeType.CP_MANAGE_TAXES))
			{
				html.setFile("data/html/chamberlain/tax-member.htm");
				html.replace("%currentTaxRate%", getCastle().getCurrentTaxPercent());
				html.replace("%nextTaxRate%", getCastle().getNextTaxPercent());
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
				return;
			}
			
			html.setFile("data/html/chamberlain/tax-leader.htm");
			html.replace("%currentTaxRate%", getCastle().getCurrentTaxPercent());
			html.replace("%nextTaxRate%", getCastle().getNextTaxPercent());
			html.replace("%objectId%", getObjectId());
			html.replace("%taxLimit%", getTaxLimit());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("tax_adjust")) // tax rates control
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			final int taxLimit = getTaxLimit();
			final int taxRate = (val.isEmpty()) ? 0 : Integer.parseInt(val);
			
			if (taxRate < 0 || taxRate > taxLimit)
			{
				html.setFile("data/html/chamberlain/tax-toohigh.htm");
				html.replace("%objectId%", getObjectId());
				html.replace("%taxLimit%", taxLimit);
				player.sendPacket(html);
				return;
			}
			
			// Set the next tax rate.
			getCastle().setNextTaxPercent(taxRate, true);
			
			html.setFile("data/html/chamberlain/tax-after.htm");
			html.replace("%nextTaxRate%", getCastle().getNextTaxPercent());
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("manor"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_MANOR_ADMINISTRATION))
				return;
			
			String filename = "";
			if (!Config.ALLOW_MANOR)
				filename = "data/html/npcdefault.htm";
			else
			{
				int cmd = Integer.parseInt(val);
				switch (cmd)
				{
					case 0:
						filename = "data/html/chamberlain/manor/manor.htm";
						break;
					
					// TODO: correct in html's to 1
					case 4:
						filename = "data/html/chamberlain/manor/manor_help00" + st.nextToken() + ".htm";
						break;
					
					default:
						filename = "data/html/chamberlain/no.htm";
						break;
				}
			}
			
			if (filename.length() != 0)
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(filename);
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
			}
		}
		else if (command.startsWith("manor_menu_select"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_MANOR_ADMINISTRATION))
				return;
			
			final CastleManorManager manor = CastleManorManager.getInstance();
			if (manor.isUnderMaintenance())
			{
				player.sendPacket(ActionFailed.STATIC_PACKET);
				player.sendPacket(SystemMessageId.THE_MANOR_SYSTEM_IS_CURRENTLY_UNDER_MAINTENANCE);
				return;
			}
			
			final String params = command.substring(command.indexOf("?") + 1);
			final StringTokenizer str = new StringTokenizer(params, "&");
			
			final int ask = Integer.parseInt(str.nextToken().split("=")[1]);
			final int state = Integer.parseInt(str.nextToken().split("=")[1]);
			final boolean time = str.nextToken().split("=")[1].equals("1");
			
			final int castleId = (state == -1) ? getCastle().getId() : state;
			
			switch (ask)
			{
				case 3: // Current seeds (Manor info)
					player.sendPacket(new ExShowSeedInfo(castleId, time, true));
					break;
				
				case 4: // Current crops (Manor info)
					player.sendPacket(new ExShowCropInfo(castleId, time, true));
					break;
				
				case 5: // Basic info (Manor info)
					player.sendPacket(new ExShowManorDefaultInfo(true));
					break;
				
				case 7: // Edit seed setup
					if (manor.isManorApproved())
						player.sendPacket(SystemMessageId.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_6_AM_AND_8_PM);
					else
						player.sendPacket(new ExShowSeedSetting(castleId));
					break;
				
				case 8: // Edit crop setup
					if (manor.isManorApproved())
						player.sendPacket(SystemMessageId.A_MANOR_CANNOT_BE_SET_UP_BETWEEN_6_AM_AND_8_PM);
					else
						player.sendPacket(new ExShowCropSetting(castleId));
					break;
			}
		}
		else if (actualCommand.equalsIgnoreCase("siege_change")) // set siege time
		{
			if (!validatePrivileges(player, PrivilegeType.CP_MANAGE_SIEGE_WAR))
				return;
			
			if (getCastle().getSiege().getSiegeRegistrationEndDate() < Calendar.getInstance().getTimeInMillis())
				sendFileMessage(player, "data/html/chamberlain/siegetime1.htm");
			else if (getCastle().getSiege().isTimeRegistrationOver())
				sendFileMessage(player, "data/html/chamberlain/siegetime2.htm");
			else
				sendFileMessage(player, "data/html/chamberlain/siegetime3.htm");
		}
		else if (actualCommand.equalsIgnoreCase("siege_time_set")) // set preDay
		{
			if (!validatePrivileges(player, PrivilegeType.CP_MANAGE_SIEGE_WAR))
				return;
			
			switch (Integer.parseInt(val))
			{
				case 1:
					_preHour = Integer.parseInt(st.nextToken());
					break;
				
				default:
					break;
			}
			
			if (_preHour != 6)
			{
				getCastle().getSiegeDate().set(Calendar.HOUR_OF_DAY, _preHour + 12);
				
				// now store the changed time and finished next Siege Time registration
				getCastle().getSiege().endTimeRegistration(false);
				sendFileMessage(player, "data/html/chamberlain/siegetime8.htm");
				return;
			}
			
			sendFileMessage(player, "data/html/chamberlain/siegetime6.htm");
		}
		else if (actualCommand.equals("give_crown"))
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			if (condition == NpcTalkCond.OWNER)
			{
				if (player.getInventory().getItemByItemId(6841) == null)
				{
					player.addItem(6841, 1, true);
					
					html.setFile("data/html/chamberlain/gavecrown.htm");
					html.replace("%CharName%", player.getName());
					html.replace("%FeudName%", getCastle().getName());
				}
				else
					html.setFile("data/html/chamberlain/hascrown.htm");
			}
			else
				html.setFile("data/html/chamberlain/noprivs.htm");
			
			player.sendPacket(html);
		}
		else if (actualCommand.equals("manor_certificate"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_USE_FUNCTIONS))
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			// Player is registered as dusk, or we aren't in the good side of competition.
			if (SevenSignsManager.getInstance().isSealValidationPeriod())
			{
				if (SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId()) == CabalType.DUSK)
					html.setFile("data/html/chamberlain/not-dawn-or-event.htm");
				// We already reached the tickets limit.
				else if (getCastle().getLeftCertificates() == 0)
					html.setFile("data/html/chamberlain/not-enough-ticket.htm");
				else
				{
					html.setFile("data/html/chamberlain/sell-dawn-ticket.htm");
					html.replace("%left%", getCastle().getLeftCertificates());
					html.replace("%bundle%", CERTIFICATES_BUNDLE);
					html.replace("%price%", CERTIFICATES_PRICE);
				}
			}
			else
				html.setFile("data/html/chamberlain/not-dawn-or-event.htm");
			
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equals("validate_certificate"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_USE_FUNCTIONS))
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			// Player is registered as dusk, or we aren't in the good side of competition.
			if (SevenSignsManager.getInstance().isSealValidationPeriod())
			{
				if (SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId()) == CabalType.DUSK)
					html.setFile("data/html/chamberlain/not-dawn-or-event.htm");
				// We already reached the tickets limit.
				else if (getCastle().getLeftCertificates() == 0)
					html.setFile("data/html/chamberlain/not-enough-ticket.htm");
				else if (player.reduceAdena(CERTIFICATES_BUNDLE * CERTIFICATES_PRICE, true))
				{
					// We add certificates.
					player.addItem(6388, CERTIFICATES_BUNDLE, true);
					
					// We update that castle certificates count.
					getCastle().setLeftCertificates(getCastle().getLeftCertificates() - 10, true);
					
					html.setFile("data/html/chamberlain/sell-dawn-ticket.htm");
					html.replace("%left%", getCastle().getLeftCertificates());
					html.replace("%bundle%", CERTIFICATES_BUNDLE);
					html.replace("%price%", CERTIFICATES_PRICE);
				}
				else
					html.setFile("data/html/chamberlain/not-enough-adena.htm");
			}
			else
				html.setFile("data/html/chamberlain/not-dawn-or-event.htm");
			
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("castle_devices"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			sendFileMessage(player, "data/html/chamberlain/devices.htm");
		}
		else if (actualCommand.equalsIgnoreCase("doors_update"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			if (val.isEmpty())
				html.setFile("data/html/chamberlain/" + getNpcId() + "-gu.htm");
			else
			{
				html.setFile("data/html/chamberlain/doors-update.htm");
				html.replace("%id%", val);
				html.replace("%type%", st.nextToken());
			}
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("doors_choose_upgrade"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			final String id = val;
			final String type = st.nextToken();
			final String level = st.nextToken();
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/chamberlain/doors-confirm.htm");
			html.replace("%objectId%", getObjectId());
			html.replace("%id%", id);
			html.replace("%level%", level);
			html.replace("%type%", type);
			html.replace("%price%", getDoorCost(Integer.parseInt(type), Integer.parseInt(level)));
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("doors_confirm_upgrade"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			final int type = Integer.parseInt(st.nextToken());
			final int level = Integer.parseInt(st.nextToken());
			final int price = getDoorCost(type, level);
			
			if (price == 0)
				return;
			
			final int id = Integer.parseInt(val);
			final Door door = getCastle().getDoor(id);
			if (door == null)
				return;
			
			final int currentHpRatio = door.getStatus().getUpgradeHpRatio();
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			if (currentHpRatio >= level)
			{
				html.setFile("data/html/chamberlain/doors-already-updated.htm");
				html.replace("%level%", currentHpRatio * 100);
			}
			else if (!player.reduceAdena(price, true))
				html.setFile("data/html/chamberlain/not-enough-adena.htm");
			else
			{
				getCastle().upgradeDoor(id, level, true);
				
				html.setFile("data/html/chamberlain/doors-success.htm");
			}
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("traps_update"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			if (val.isEmpty())
				html.setFile("data/html/chamberlain/" + getNpcId() + "-tu.htm");
			else
			{
				html.setFile("data/html/chamberlain/traps-update" + ((getCastle().getName().equalsIgnoreCase("aden")) ? "1" : "") + ".htm");
				html.replace("%trapIndex%", val);
			}
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("traps_choose_upgrade"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			final String trapIndex = val;
			final String level = st.nextToken();
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/chamberlain/traps-confirm.htm");
			html.replace("%objectId%", getObjectId());
			html.replace("%trapIndex%", trapIndex);
			html.replace("%level%", level);
			html.replace("%price%", getTrapCost(Integer.parseInt(level)));
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("traps_confirm_upgrade"))
		{
			if (!validatePrivileges(player, PrivilegeType.CP_SET_FUNCTIONS))
				return;
			
			final int level = Integer.parseInt(st.nextToken());
			final int price = getTrapCost(level);
			
			if (price == 0)
				return;
			
			final int trapIndex = Integer.parseInt(val);
			final int currentLevel = getCastle().getTrapUpgradeLevel(trapIndex);
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			
			if (currentLevel >= level)
			{
				html.setFile("data/html/chamberlain/traps-already-updated.htm");
				html.replace("%level%", currentLevel);
			}
			else if (!player.reduceAdena(price, true))
				html.setFile("data/html/chamberlain/not-enough-adena.htm");
			else
			{
				getCastle().setTrapUpgrade(trapIndex, level, true);
				
				html.setFile("data/html/chamberlain/traps-success.htm");
			}
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		
		switch (getNpcTalkCond(player))
		{
			case NONE:
				html.setFile("data/html/chamberlain/no.htm");
				break;
			
			case UNDER_SIEGE:
				html.setFile("data/html/chamberlain/busy.htm");
				break;
			
			default:
				html.setFile("data/html/chamberlain/chamberlain.htm");
				break;
		}
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	@Override
	protected NpcTalkCond getNpcTalkCond(Player player)
	{
		if (getCastle() != null && player.getClan() != null)
		{
			if (getCastle().getSiege().isInProgress())
				return NpcTalkCond.UNDER_SIEGE;
			
			if (getCastle().getOwnerId() == player.getClanId())
			{
				if (player.isClanLeader())
					return NpcTalkCond.OWNER;
				
				return NpcTalkCond.CLAN_MEMBER;
			}
		}
		return NpcTalkCond.NONE;
	}
	
	private boolean validatePrivileges(Player player, PrivilegeType privilege)
	{
		if (!player.hasClanPrivileges(privilege))
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/chamberlain/noprivs.htm");
			player.sendPacket(html);
			return false;
		}
		return true;
	}
	
	private void sendFileMessage(Player player, String htmlMessage)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(htmlMessage);
		html.replace("%objectId%", getObjectId());
		html.replace("%npcId%", getNpcId());
		html.replace("%time%", getCastle().getSiegeDate().getTime().toString());
		player.sendPacket(html);
	}
	
	/**
	 * Retrieve the price of the door, following its type, required level of upgrade and current Seven Signs state.
	 * @param type : The type of doors (1: normal gates, 2: metallic gates, 3: walls).
	 * @param level : The required level of upgrade (x2, x3 or x5 HPs).
	 * @return The price modified by Seal of Strife state (-20% if Dawn is winning, x3 if Dusk is winning).
	 */
	private static int getDoorCost(int type, int level)
	{
		int price = 0;
		
		switch (type)
		{
			case 1:
				switch (level)
				{
					case 2:
						price = 300000;
						break;
					case 3:
						price = 400000;
						break;
					case 5:
						price = 500000;
						break;
				}
				break;
			
			case 2:
				switch (level)
				{
					case 2:
						price = 750000;
						break;
					case 3:
						price = 900000;
						break;
					case 5:
						price = 1000000;
						break;
				}
				break;
			
			case 3:
				switch (level)
				{
					case 2:
						price = 1600000;
						break;
					case 3:
						price = 1800000;
						break;
					case 5:
						price = 2000000;
						break;
				}
				break;
		}
		
		switch (SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE))
		{
			case DUSK:
				price *= 3;
				break;
			
			case DAWN:
				price *= 0.8;
				break;
		}
		
		return price;
	}
	
	/**
	 * Retrieve the price of traps, following its level.
	 * @param level : The required level of upgrade.
	 * @return The price modified by Seal of Strife state (-20% if Dawn is winning, x3 if Dusk is winning).
	 */
	private static int getTrapCost(int level)
	{
		int price = 0;
		
		switch (level)
		{
			case 1:
				price = 3000000;
				break;
			
			case 2:
				price = 4000000;
				break;
			
			case 3:
				price = 5000000;
				break;
			
			case 4:
				price = 6000000;
				break;
		}
		
		switch (SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE))
		{
			case DUSK:
				price *= 3;
				break;
			
			case DAWN:
				price *= 0.8;
				break;
		}
		
		return price;
	}
	
	private static int getTaxLimit()
	{
		switch (SevenSignsManager.getInstance().getSealOwner(SealType.STRIFE))
		{
			case DAWN:
				return 25;
			
			case DUSK:
				return 5;
			
			default:
				return 15;
		}
	}
}