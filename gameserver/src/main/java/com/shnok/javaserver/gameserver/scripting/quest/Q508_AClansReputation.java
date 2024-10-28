package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowInfoUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q508_AClansReputation extends Quest
{
	private static final String QUEST_NAME = "Q508_AClansReputation";
	
	// NPC
	private static final int SIR_ERIC_RODEMAI = 30868;
	
	// Items
	private static final int NUCLEUS_OF_FLAMESTONE_GIANT = 8494;
	private static final int THEMIS_SCALE = 8277;
	private static final int NUCLEUS_OF_HEKATON_PRIME = 8279;
	private static final int TIPHON_SHARD = 8280;
	private static final int GLAKI_NUCLEUS = 8281;
	private static final int RAHHA_FANG = 8282;
	
	// Raidbosses
	private static final int FLAMESTONE_GIANT = 25524;
	private static final int PALIBATI_QUEEN_THEMIS = 25252;
	private static final int HEKATON_PRIME = 25140;
	private static final int GARGOYLE_LORD_TIPHON = 25255;
	private static final int LAST_LESSER_GIANT_GLAKI = 25245;
	private static final int RAHHA = 25051;
	
	// Reward list (itemId, minClanPoints, maxClanPoints)
	private static final int[][] REWARDS =
	{
		{
			PALIBATI_QUEEN_THEMIS,
			THEMIS_SCALE,
			65,
			100
		},
		{
			HEKATON_PRIME,
			NUCLEUS_OF_HEKATON_PRIME,
			40,
			75
		},
		{
			GARGOYLE_LORD_TIPHON,
			TIPHON_SHARD,
			30,
			65
		},
		{
			LAST_LESSER_GIANT_GLAKI,
			GLAKI_NUCLEUS,
			105,
			140
		},
		{
			RAHHA,
			RAHHA_FANG,
			40,
			75
		},
		{
			FLAMESTONE_GIANT,
			NUCLEUS_OF_FLAMESTONE_GIANT,
			60,
			95
		}
	};
	
	// Radar
	private static final Location[] LOCS =
	{
		new Location(192346, 21528, -3648),
		new Location(191979, 54902, -7658),
		new Location(170038, -26236, -3824),
		new Location(171762, 55028, -5992),
		new Location(117232, -9476, -3320),
		new Location(144218, -5816, -4722)
	};
	
	public Q508_AClansReputation()
	{
		super(508, "A Clan's Reputation");
		
		setItemsIds(THEMIS_SCALE, NUCLEUS_OF_HEKATON_PRIME, TIPHON_SHARD, GLAKI_NUCLEUS, RAHHA_FANG, NUCLEUS_OF_FLAMESTONE_GIANT);
		
		addQuestStart(SIR_ERIC_RODEMAI);
		addTalkId(SIR_ERIC_RODEMAI);
		
		addMyDying(FLAMESTONE_GIANT, PALIBATI_QUEEN_THEMIS, HEKATON_PRIME, GARGOYLE_LORD_TIPHON, LAST_LESSER_GIANT_GLAKI, RAHHA);
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
			htmltext = "30868-" + event + ".htm";
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			st.set("raid", event);
			playSound(player, SOUND_ACCEPT);
			player.getRadarList().addMarker(LOCS[Integer.parseInt(event) - 1]);
		}
		else if (event.equalsIgnoreCase("30868-7.htm"))
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
					htmltext = "30868-0a.htm";
				else if (clan.getLevel() < 5)
					htmltext = "30868-0b.htm";
				else
					htmltext = "30868-0c.htm";
				break;
			
			case STARTED:
				final int raid = st.getInteger("raid");
				final int item = REWARDS[raid - 1][1];
				
				if (!player.getInventory().hasItem(item))
					htmltext = "30868-" + raid + "a.htm";
				else
				{
					final int reward = Rnd.get(REWARDS[raid - 1][2], REWARDS[raid - 1][3]);
					
					htmltext = "30868-" + raid + "b.htm";
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