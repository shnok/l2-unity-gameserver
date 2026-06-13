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

public final class WildBeastReserve extends FlagWar
{
	public static final String QUEST_NAME = "Q655_AGrandPlanForTamingWildBeasts";
	
	private static final int CRYSTAL_OF_PURITY = 8084;
	private static final int TRAINER_LICENSE = 8293;
	
	public WildBeastReserve()
	{
		super("siegablehall", BEAST_FARM);
		
		MAKER_NAME = "rune07_azit2115_11m1";
		
		ROYAL_FLAG = 35606;
		FLAG_RED = 35607; // Red flag
		FLAG_YELLOW = 35608; // Yellow flag
		FLAG_GREEN = 35609; // Green flag
		FLAG_BLUE = 35610; // Blue flag
		FLAG_PURPLE = 35611; // Purple flag
		
		ALLY_1 = 35618;
		ALLY_2 = 35619;
		ALLY_3 = 35620;
		ALLY_4 = 35621;
		ALLY_5 = 35622;
		
		TELEPORT_1 = 35612;
		
		MESSENGER = 35627;
		
		FLAG_COORDS = new SpawnLocation[7];
		FLAG_COORDS[0] = new SpawnLocation(56963, -92211, -1303, 60611);
		FLAG_COORDS[1] = new SpawnLocation(58090, -91641, -1303, 47274);
		FLAG_COORDS[2] = new SpawnLocation(58908, -92556, -1303, 34450);
		FLAG_COORDS[3] = new SpawnLocation(58336, -93600, -1303, 21100);
		FLAG_COORDS[4] = new SpawnLocation(57152, -93360, -1303, 8400);
		FLAG_COORDS[5] = new SpawnLocation(59116, -93251, -1302, 31000);
		FLAG_COORDS[6] = new SpawnLocation(56432, -92864, -1303, 64000);
		
		OUTTER_DOORS = new int[2];
		OUTTER_DOORS[0] = 21150003;
		OUTTER_DOORS[1] = 21150004;
		
		INNER_DOORS = new int[2];
		INNER_DOORS[0] = 21150001;
		INNER_DOORS[1] = 21150002;
		
		CENTER = new SpawnLocation(57762, -92696, -1359, 0);
		
		attachListeners();
	}
	
	@Override
	public String getFlagHtml(int flag)
	{
		switch (flag)
		{
			case 35607:
				return "farm_kel_mahum_messenger_4a.htm";
			
			case 35608:
				return "farm_kel_mahum_messenger_4b.htm";
			
			case 35609:
				return "farm_kel_mahum_messenger_4c.htm";
			
			case 35610:
				return "farm_kel_mahum_messenger_4d.htm";
			
			case 35611:
				return "farm_kel_mahum_messenger_4e.htm";
		}
		return null;
	}
	
	@Override
	public String getAllyHtml(int ally)
	{
		switch (ally)
		{
			case 35618:
				return "farm_kel_mahum_messenger_17.htm";
			
			case 35619:
				return "farm_kel_mahum_messenger_18.htm";
			
			case 35620:
				return "farm_kel_mahum_messenger_19.htm";
			
			case 35621:
				return "farm_kel_mahum_messenger_20.htm";
			
			case 35622:
				return "farm_kel_mahum_messenger_23.htm";
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
				
				html = getHtmlText("farm_messenger001.htm");
				html = html.replace("%my_pledge_name%", (clan == null) ? "no owner" : clan.getName());
				html = html.replace("%my_owner_name%", (clan == null) ? "no owner" : clan.getLeaderName());
			}
			else
				html = "farm_messenger002.htm";
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
				html = "farm_kel_mahum_messenger_1.htm";
			else
			{
				html = getHtmlText("farm_messenger_q0655_11.htm");
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
				html = getHtmlText("farm_messenger_q0655_11.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else if (clan != null && _hall.getOwnerId() == player.getClanId())
				html = "farm_kel_mahum_messenger_22.htm";
			else
				html = getClanRegisterStatus(player);
		}
		else if (event.equalsIgnoreCase("register_clan_member"))
		{
			if (!_hall.isWaitingBattle())
			{
				html = getHtmlText("farm_messenger_q0655_11.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else if (!player.isClanLeader())
				html = registerClanMember(player);
			else
				html = "farm_kel_mahum_messenger_5.htm";
		}
		else if (event.equalsIgnoreCase("select_clan_npc") || event.equalsIgnoreCase("view_clan_npc"))
		{
			if (!_hall.isWaitingBattle())
			{
				html = getHtmlText("farm_messenger_q0655_11.htm");
				html = html.replace("%next_siege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
			}
			else
				html = getNpcType(player);
		}
		else if (event.startsWith("select_npc_"))
		{
			try
			{
				final int npcId = 35617 + Integer.parseInt(event.substring("select_npc_".length()));
				
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
			html = getHtmlText("farm_kel_mahum_messenger_6.htm");
		}
		else if (event.startsWith("info_npc_"))
		{
			try
			{
				final int npcIndex = Integer.parseInt(event.substring("info_npc_".length()));
				final String htmlFilename = String.format("farm_kel_mahum_messenger_%d.htm", 10 + npcIndex);
				
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
	public boolean canPayRegistration()
	{
		return false;
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
			return "farm_kel_mahum_messenger_3.htm";
		
		ClanData cd = _data.get(clan.getClanId());
		if (cd == null)
		{
			// Take required items from the Player.
			takeItems(player, CRYSTAL_OF_PURITY, -1);
			takeItems(player, TRAINER_LICENSE, 1);
			
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
	 * @return A {@link String} representing the HTML page that indicates the clan's registration status.
	 */
	private String getClanRegisterStatus(Player player)
	{
		final Clan clan = player.getClan();
		final ClanData cd = _data.get(player.getClanId());
		
		// Check if the clan and ClanData exist.
		if (clan != null && cd != null)
			return getFlagHtml(cd.flag);
		
		// Check if the maximum number of attacker clans has been reached.
		if (getAttackerClans().size() >= (5 + (_hall.getOwnerId() == 0 ? 1 : 0)))
			return "farm_kel_mahum_messenger_21.htm";
		
		// Check if the Player's clan owns the hall.
		if (clan != null && _hall.getOwnerId() == player.getClanId())
			return "farm_kel_mahum_messenger_22.htm";
		
		if (!player.destroyItemByItemId(TRAINER_LICENSE, 1, true))
			return "farm_kel_mahum_messenger_27.htm";
		
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
			return "farm_kel_mahum_messenger_25.htm";
		
		final ClanData cd = _data.get(player.getClanId());
		
		// Check if the Player is not in a Clan or the Clan data is null.
		if (clan == null || cd == null)
			return "farm_kel_mahum_messenger_7.htm";
		
		// Check if the Npc data is not zero.
		if (cd.npc != 0)
			return getAllyHtml(cd.npc);
		
		// Check if the Player is a Clan leader.
		if (player.isClanLeader())
			return "farm_kel_mahum_messenger_6.htm";
		
		// Default case.
		return "farm_kel_mahum_messenger_10.htm";
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
			return "farm_kel_mahum_messenger_7.htm";
		
		// Check if the allyNpcId is within the valid range.
		if (allyNpcId >= 35618 && allyNpcId <= 35622)
		{
			// Update the NPC type in the ClanData and save it.
			cd.npc = allyNpcId;
			saveNpc(allyNpcId, player.getClanId());
			
			return "farm_kel_mahum_messenger_9.htm";
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
		String html = getHtmlText("farm_messenger003.htm");
		
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
			return "farm_kel_mahum_messenger_7.htm";
		
		// Check if the Clan has reached its member limit.
		if (cd.players.size() >= 18)
			return "farm_kel_mahum_messenger_8.htm";
		
		// Add the Player's objectId to the Clan's list of members.
		cd.players.add(player.getObjectId());
		
		// Persist the registration.
		saveMember(clanId, player.getObjectId());
		
		// Return the resulting HTML page.
		return "farm_kel_mahum_messenger_9.htm";
	}
}