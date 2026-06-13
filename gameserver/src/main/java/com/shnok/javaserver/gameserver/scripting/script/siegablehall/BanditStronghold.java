package com.shnok.javaserver.gameserver.scripting.script.siegablehall;

import java.text.SimpleDateFormat;
import java.util.Map.Entry;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.scripting.QuestState;

/**
 * In order to participate in a battle to occupy the Brigand's Hideaway clan hall, the head of a clan of level 4 or above must complete the Brigand's Hideaway quest within a certain time before the clan hall battle is started. Only the first five clans that complete the hideaway quest first can
 * participate. The number of clan members that can participate per clan is limited to 18 people. The quest to participate in the Brigand's Hideaway clan hall battle is only valid for participating in the clan hall battle on that date. Having completed the quest previously does not make it possible
 * to participate in the clan battle.<br>
 * <br>
 * Once the decision is made to participate in the clan battle, the clan leader must register the 18 clan members and select the NPC that he will protect by conversing with the herald NPC. Clans that currently occupy the clan hall must register the 18 clan members to participate in the defense.
 */
public final class BanditStronghold extends FlagWar
{
	public static final String QUEST_NAME = "Q504_CompetitionForTheBanditStronghold";
	
	private static final int TARLK_AMULET = 4332;
	private static final int CONTEST_CERTIFICATE = 4333;
	private static final int TROPHY_OF_ALLIANCE = 5009;
	
	public BanditStronghold()
	{
		super("siegablehall", BANDIT_STRONGHOLD);
		
		MAKER_NAME = "oren15_azit_teleporter01";
		
		ROYAL_FLAG = 35422;
		FLAG_RED = 35423;
		FLAG_YELLOW = 35424;
		FLAG_GREEN = 35425;
		FLAG_BLUE = 35426;
		FLAG_PURPLE = 35427;
		
		ALLY_1 = 35428;
		ALLY_2 = 35429;
		ALLY_3 = 35430;
		ALLY_4 = 35431;
		ALLY_5 = 35432;
		
		TELEPORT_1 = 35560;
		
		MESSENGER = 35437;
		
		OUTTER_DOORS = new int[2];
		OUTTER_DOORS[0] = 22170001;
		OUTTER_DOORS[1] = 22170002;
		
		INNER_DOORS = new int[2];
		INNER_DOORS[0] = 22170003;
		INNER_DOORS[1] = 22170004;
		
		FLAG_COORDS = new SpawnLocation[7];
		FLAG_COORDS[0] = new SpawnLocation(83699, -17468, -1774, 19048);
		FLAG_COORDS[1] = new SpawnLocation(82053, -17060, -1784, 5432);
		FLAG_COORDS[2] = new SpawnLocation(82142, -15528, -1799, 58792);
		FLAG_COORDS[3] = new SpawnLocation(83544, -15266, -1770, 44976);
		FLAG_COORDS[4] = new SpawnLocation(84609, -16041, -1769, 35816);
		FLAG_COORDS[5] = new SpawnLocation(81981, -15708, -1858, 60392);
		FLAG_COORDS[6] = new SpawnLocation(84375, -17060, -1860, 27712);
		
		CENTER = new SpawnLocation(82882, -16280, -1894, 0);
		
		attachListeners();
	}
	
	@Override
	public String getFlagHtml(int flag)
	{
		switch (flag)
		{
			case 35423:
				return "agit_oel_mahum_messeger_4a.htm";
			
			case 35424:
				return "agit_oel_mahum_messeger_4b.htm";
			
			case 35425:
				return "agit_oel_mahum_messeger_4c.htm";
			
			case 35426:
				return "agit_oel_mahum_messeger_4d.htm";
			
			case 35427:
				return "agit_oel_mahum_messeger_4e.htm";
		}
		return null;
	}
	
	@Override
	public String getAllyHtml(int ally)
	{
		switch (ally)
		{
			case 35428:
				return "agit_oel_mahum_messeger_17.htm";
			
			case 35429:
				return "agit_oel_mahum_messeger_18.htm";
			
			case 35430:
				return "agit_oel_mahum_messeger_19.htm";
			
			case 35431:
				return "agit_oel_mahum_messeger_20.htm";
			
			case 35432:
				return "agit_oel_mahum_messeger_23.htm";
		}
		return null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String html = null;
		
		if (npc.getNpcId() == MESSENGER)
		{
			if (!_hall.isFree())
			{
				final Clan clan = ClanTable.getInstance().getClan(_hall.getOwnerId());
				
				html = getHtmlText("azit_messenger001.htm");
				html = html.replace("%my_pledge_name%", (clan == null) ? "no owner" : clan.getName());
				html = html.replace("%my_owner_name%", (clan == null) ? "no owner" : clan.getLeaderName());
			}
			else
				html = "azit_messenger002.htm";
		}
		
		if (html == null)
			return super.onFirstTalk(npc, player);
		
		return html;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String html = event;
		
		final Clan clan = player.getClan();
		
		if (event.equalsIgnoreCase("register"))
		{
			if (player.isOverweight())
			{
				player.sendPacket(SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT);
				return "";
			}
			
			if (_hall.isWaitingBattle())
				html = "agit_oel_mahum_messeger_1.htm";
			else
			{
				html = getHtmlText("azit_messenger_q0504_09.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
		}
		else if (event.equalsIgnoreCase("view_participants"))
		{
			html = getRegisteredPledgeList(player);
		}
		else if (event.equalsIgnoreCase("register_clan"))
		{
			if (!_hall.isWaitingBattle())
			{
				html = getHtmlText("azit_messenger_q0504_03.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else if (clan == null || _hall.getOwnerId() != player.getClanId())
				html = getClanRegisterStatus(player, false);
			else
				html = "agit_oel_mahum_messeger_22.htm";
		}
		else if (event.equalsIgnoreCase("register_clan_member"))
		{
			if (!_hall.isWaitingBattle())
			{
				html = getHtmlText("azit_messenger_q0504_03.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else if (!player.isClanLeader())
				html = registerClanMember(player);
			else
				html = "agit_oel_mahum_messeger_5.htm";
		}
		else if (event.equalsIgnoreCase("register_with_adena"))
		{
			if (!_hall.isWaitingBattle())
			{
				html = getHtmlText("azit_messenger_q0504_03.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else
				html = getClanRegisterStatus(player, true);
		}
		else if (event.equalsIgnoreCase("select_clan_npc") || event.equalsIgnoreCase("view_clan_npc"))
		{
			if (!_hall.isWaitingBattle())
			{
				html = getHtmlText("azit_messenger_q0504_03.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else
				html = getNpcType(player);
		}
		else if (event.startsWith("select_npc_"))
		{
			try
			{
				final int npcId = 35427 + Integer.parseInt(event.substring("select_npc_".length()));
				
				html = setNpcType(player, npcId);
			}
			catch (NumberFormatException e)
			{
				// Handle the case where the npcIndex is not a valid integer
				LOGGER.error("Invalid NPC index in event: " + event);
			}
		}
		else if (event.equalsIgnoreCase("reselect_npc"))
		{
			html = getHtmlText("agit_oel_mahum_messeger_6.htm");
		}
		else if (event.startsWith("info_npc_"))
		{
			try
			{
				final int npcIndex = Integer.parseInt(event.substring("info_npc_".length()));
				final String htmlFilename = String.format("agit_oel_mahum_messeger_%d.htm", 10 + npcIndex);
				
				return getHtmlText(htmlFilename);
			}
			catch (NumberFormatException e)
			{
				// Handle the case where the npcIndex is not a valid integer
				LOGGER.error("Invalid NPC index in event: " + event);
			}
		}
		
		return html;
	}
	
	@Override
	public void spawnNpcs()
	{
		// Do nothing.
	}
	
	@Override
	public void unspawnNpcs()
	{
		// Do nothing.
	}
	
	/**
	 * Check if the {@link Player}'s clan can be registered for the battle and registers it if possible.
	 * @param player : The {@link Player} whose clan is to be checked and registered.
	 * @return A {@link String} indicating the result of the registration process.
	 */
	private String checkAndRegisterClan(Player player)
	{
		final Clan clan = player.getClan();
		
		// Check if the hall is waiting for battle, the Player has a Clan, and the number of attacker clans is within the limit.
		if (!_hall.isWaitingBattle() || clan == null || getAttackerClans().size() >= (5 + (_hall.getOwnerId() == 0 ? 1 : 0)))
			return "agit_oel_mahum_messeger_3.htm";
		
		ClanData cd = _data.get(clan.getClanId());
		if (cd == null)
		{
			// Take required items from the Player.
			takeItems(player, TARLK_AMULET, 30);
			takeItems(player, CONTEST_CERTIFICATE, -1);
			takeItems(player, TROPHY_OF_ALLIANCE, -1);
			
			// Check if the Player has the quest, and exit it if he does.
			final QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st != null)
			{
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			
			// Register the Player's Clan.
			cd = registerClan(clan);
		}
		
		// Return the HTML for the Clan's flag.
		return getFlagHtml(cd.flag);
	}
	
	/**
	 * Determine the registration status of the {@link Player}'s clan and generates the corresponding HTML response.
	 * @param player : The {@link Player} whose clan registration status is being determined. Must not be null.
	 * @param withAdena : A boolean indicating whether the registration is being checked with Adena.
	 * @return A {@link String} representing the HTML page that indicates the clan's registration status.
	 */
	private String getClanRegisterStatus(Player player, boolean withAdena)
	{
		final Clan clan = player.getClan();
		final ClanData cd = _data.get(player.getClanId());
		
		// Check if the clan and ClanData exist.
		if (clan != null && cd != null)
			return getFlagHtml(cd.flag);
		
		// Check if the maximum number of attacker clans has been reached.
		if (getAttackerClans().size() >= (5 + (_hall.getOwnerId() == 0 ? 1 : 0)))
			return "agit_oel_mahum_messeger_21.htm";
		
		// Check if the Player's clan owns the hall.
		if (clan != null && _hall.getOwnerId() == player.getClanId())
			return "agit_oel_mahum_messeger_22.htm";
		
		// Check registration without Adena.
		if (!withAdena)
		{
			if (!player.destroyItemByItemId(TROPHY_OF_ALLIANCE, 1, true))
				return "agit_oel_mahum_messeger_24.htm";
		}
		else
		{
			// Check if the Clan is null or already has a ClanHall.
			if (clan == null || clan.hasClanHall())
				return "azit_messenger_q0504_10.htm";
			
			// Check if the Player is the Clan leader.
			if (!player.isClanLeader())
				return "azit_messenger_q0504_05.htm";
			
			// Check if the Clan level is at least 4.
			if (clan.getLevel() < 4)
				return "azit_messenger_q0504_04.htm";
			
			// Check if the player has enough Adena.
			if (!player.reduceAdena(200000, true))
				return "agit_oel_mahum_messeger_26.htm";
		}
		
		// Register the Clan if all conditions are met.
		return checkAndRegisterClan(player);
	}
	
	/**
	 * Determine the NPC type to display based on the {@link Player}'s clan status and role.
	 * @param player : The {@link Player} for whom the Npc type is being determined.
	 * @return A {@link String} indicating the Npc type to be displayed.
	 **/
	private String getNpcType(Player player)
	{
		final Clan clan = player.getClan();
		
		// Check if the Player is in a Clan and owns the hall.
		if (clan != null && _hall.getOwnerId() == clan.getClanId())
			return "agit_oel_mahum_messeger_25.htm";
		
		final ClanData cd = _data.get(player.getClanId());
		
		// Check if the Player is not in a Clan or the Clan data is null.
		if (clan == null || cd == null)
			return "agit_oel_mahum_messeger_7.htm";
		
		// Check if the Npc data is not zero.
		if (cd.npc != 0)
			return getAllyHtml(cd.npc);
		
		// Check if the Player is a Clan leader.
		if (player.isClanLeader())
			return "agit_oel_mahum_messeger_6.htm";
		
		// Default case.
		return "agit_oel_mahum_messeger_10.htm";
	}
	
	/**
	 * Set the NPC type for the {@link Player}'s clan based on the given ally Npc ID.
	 * @param player : The {@link Player} setting the Npc type. Must be a clan leader.
	 * @param allyNpcId : The ID of the ally Npc to set. Must be within the range 35428 to 35432.
	 * @return A {@link String} indicating the result of the operation.
	 **/
	private String setNpcType(Player player, int allyNpcId)
	{
		final ClanData cd = _data.get(player.getClanId());
		
		// Check if the player is a clan leader and if the player's clan exists in the data.
		if (!player.isClanLeader() || cd == null)
			return "agit_oel_mahum_messeger_7.htm";
		
		// Check if the allyNpcId is within the valid range.
		if (allyNpcId >= 35428 && allyNpcId <= 35432)
		{
			// Update the NPC type in the ClanData and save it.
			cd.npc = allyNpcId;
			saveNpc(allyNpcId, player.getClanId());
			
			return "agit_oel_mahum_messeger_9.htm";
		}
		
		// Return an empty string for invalid allyNpcId.
		return "";
	}
	
	/**
	 * Generate the HTML list of registered clans and their member counts for a given {@link Player}.
	 * @param player : The {@link Player} for whom the registered pledge list is being generated. Must not be null.
	 * @return A {@link String} representing the HTML page with the list of registered clans and their member counts.
	 */
	private String getRegisteredPledgeList(Player player)
	{
		// Retrieve the base HTML template.
		String html = getHtmlText("azit_messenger003.htm");
		
		int i = 0;
		for (Entry<Integer, ClanData> entry : _data.entrySet())
		{
			// Get the clan associated with the current entry.
			final Clan attacker = ClanTable.getInstance().getClan(entry.getKey());
			if (attacker == null || attacker.getClanId() == _hall.getOwnerId())
				continue;
			
			// Replace placeholders in the HTML with the clan's name and member count.
			html = html.replaceAll("%clan" + i + "%", attacker.getName());
			html = html.replaceAll("%clanMem" + i + "%", String.valueOf(entry.getValue().players.size()));
			i++;
		}
		
		// If there are fewer than 5 registered clans, fill the remaining slots with "unregistered" placeholders.
		if (_data.size() < 5)
		{
			for (int c = 0; c < 5; c++)
			{
				html = html.replaceAll("%clan" + c + "%", "**unregistered**");
				html = html.replaceAll("%clanMem" + c + "%", "");
			}
		}
		
		// Return the populated HTML.
		return html;
	}
	
	/**
	 * Register the given {@link Player} if registration is allowed and the ClanData has not reached its member limit.
	 * @param player : The {@link Player} to register as a Clan member. Must not be null.
	 * @return A {@link String} representing the HTML page that indicates the result of the registration attempt.
	 */
	private String registerClanMember(Player player)
	{
		final int clanId = player.getClanId();
		final ClanData cd = _data.get(clanId);
		
		// Check if the ClanData exists and registration is allowed.
		if (cd == null || !_hall.isRegistering())
			return "agit_oel_mahum_messeger_7.htm";
		
		// Check if the Clan has reached its member limit.
		if (cd.players.size() >= 18)
			return "agit_oel_mahum_messeger_8.htm";
		
		// Add the Player's objectId to the Clan's list of members.
		cd.players.add(player.getObjectId());
		
		// Persist the registration.
		saveMember(clanId, player.getObjectId());
		
		// Return the resulting HTML page.
		return "agit_oel_mahum_messeger_9.htm";
	}
}