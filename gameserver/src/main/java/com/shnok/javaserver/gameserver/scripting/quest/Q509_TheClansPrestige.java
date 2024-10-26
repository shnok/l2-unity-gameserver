package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.lang.StringUtil;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q509_TheClansPrestige extends Quest
{
	private static final String QUEST_NAME = "Q509_TheClansPrestige";
	
	// NPCs
	private static final int VALDIS = 31331;
	
	// Items
	private static final int DAIMON_EYES = 8489;
	private static final int HESTIA_FAIRY_STONE = 8490;
	private static final int NUCLEUS_OF_LESSER_GOLEM = 8491;
	private static final int FALSTON_FANG = 8492;
	private static final int SHAID_TALON = 8493;
	
	// Raid Bosses
	private static final int DAIMON_THE_WHITE_EYED = 25290;
	private static final int HESTIA_GUARDIAN_DEITY = 25293;
	private static final int PLAGUE_GOLEM = 25523;
	private static final int DEMON_AGENT_FALSTON = 25322;
	private static final int QUEEN_SHYEED = 25514;
	
	// Reward list (itemId, minClanPoints, maxClanPoints)
	private static final int[][] REWARDS =
	{
		{
			DAIMON_THE_WHITE_EYED,
			DAIMON_EYES,
			180,
			215
		},
		{
			HESTIA_GUARDIAN_DEITY,
			HESTIA_FAIRY_STONE,
			430,
			465
		},
		{
			PLAGUE_GOLEM,
			NUCLEUS_OF_LESSER_GOLEM,
			380,
			415
		},
		{
			DEMON_AGENT_FALSTON,
			FALSTON_FANG,
			220,
			255
		},
		{
			QUEEN_SHYEED,
			SHAID_TALON,
			130,
			165
		}
	};
	
	// Radar
	private static final Location[] LOCS =
	{
		new Location(186320, -43904, -3175),
		new Location(134672, -115600, -1216),
		new Location(170000, -59900, -3848),
		new Location(93296, -75104, -1824),
		new Location(79635, -55612, -5980)
	};
	
	public Q509_TheClansPrestige()
	{
		super(509, "The Clan's Prestige");
		
		setItemsIds(DAIMON_EYES, HESTIA_FAIRY_STONE, NUCLEUS_OF_LESSER_GOLEM, FALSTON_FANG, SHAID_TALON);
		
		addQuestStart(VALDIS);
		addTalkId(VALDIS);
		
		addMyDying(DAIMON_THE_WHITE_EYED, HESTIA_GUARDIAN_DEITY, PLAGUE_GOLEM, DEMON_AGENT_FALSTON, QUEEN_SHYEED);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (StringUtil.isDigit(event))
		{
			htmltext = "31331-" + event + ".htm";
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("raid", event);
			playSound(player, SOUND_ACCEPT);
			player.getRadarList().addMarker(LOCS[Integer.parseInt(event) - 1]);
		}
		else if (event.equalsIgnoreCase("31331-6.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = getNoQuestMsg();
		if (st == null)
			return htmltext;
		
		Clan clan = player.getClan();
		
		switch (st.getState())
		{
			case CREATED:
				if (!player.isClanLeader())
					htmltext = "31331-0a.htm";
				else if (clan.getLevel() < 6)
					htmltext = "31331-0b.htm";
				else
					htmltext = "31331-0c.htm";
				break;
			
			case STARTED:
				final int raid = st.getInteger("raid");
				final int item = REWARDS[raid - 1][1];
				
				if (!player.getInventory().hasItem(item))
					htmltext = "31331-" + raid + "a.htm";
				else
				{
					final int reward = Rnd.get(REWARDS[raid - 1][2], REWARDS[raid - 1][3]);
					
					htmltext = "31331-" + raid + "b.htm";
					takeItems(player, item, 1);
					clan.addReputationScore(reward);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CLAN_QUEST_COMPLETED_AND_S1_POINTS_GAINED).addNumber(reward));
					clan.broadcastToMembers(new PledgeShowInfoUpdate(clan));
				}
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		// Retrieve the qS of the clan leader.
		final QuestState st = getClanLeaderQuestState(player, npc);
		if (st == null || !st.isStarted())
			return;
		
		// Reward only if quest is setup on good index.
		final int raid = st.getInteger("raid");
		if (REWARDS[raid - 1][0] == npc.getNpcId())
			dropItemsAlways(st.getPlayer(), REWARDS[raid - 1][1], 1, 1);
	}
}