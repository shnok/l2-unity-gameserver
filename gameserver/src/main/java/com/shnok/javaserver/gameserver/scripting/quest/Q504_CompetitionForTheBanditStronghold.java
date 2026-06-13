package com.shnok.javaserver.gameserver.scripting.quest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.SiegableHall;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q504_CompetitionForTheBanditStronghold extends Quest
{
	private static final String QUEST_NAME = "Q504_CompetitionForTheBanditStronghold";
	
	// Misc
	private static final SiegableHall BANDIT_STRONGHOLD = ClanHallManager.getInstance().getSiegableHall(35);
	
	// NPC
	private static final int MESSENGER = 35437;
	
	// Monsters
	private static final Map<Integer, Integer> MONSTERS = HashMap.newHashMap(5);
	
	// Items
	private static final int TARLK_AMULET = 4332;
	private static final int CONTEST_CERTIFICATE = 4333;
	private static final int TROPHY_OF_ALLIANCE = 5009;
	
	public Q504_CompetitionForTheBanditStronghold()
	{
		super(504, "Competition for the Bandit Stronghold");
		
		MONSTERS.put(20570, 600000); // Tarlk Bugbear
		MONSTERS.put(20571, 700000); // Tarlk Bugbear Warrior
		MONSTERS.put(20572, 800000); // Tarlk Bugbear High Warrior
		MONSTERS.put(20573, 900000); // Tarlk Basilisk
		MONSTERS.put(20574, 700000); // Elder Tarlk Basilisk
		
		addQuestStart(MESSENGER);
		addTalkId(MESSENGER);
		
		for (int mob : MONSTERS.keySet())
			addMyDying(mob);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("35437-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, CONTEST_CERTIFICATE, 1);
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
		
		final Clan clan = player.getClan();
		
		if (!BANDIT_STRONGHOLD.isWaitingBattle())
			htmltext = getHtmlText("35437-09.htm").replace("%nextSiege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(BANDIT_STRONGHOLD.getSiegeDate().getTime()));
		else if (clan == null || clan.getLevel() < 4)
			htmltext = "35437-04.htm";
		else if (!player.isClanLeader())
			htmltext = "35437-05.htm";
		else if (clan.getClanHallId() > 0)
			htmltext = "35437-10.htm";
		else if (player.getInventory().hasAtLeastOneItem(TROPHY_OF_ALLIANCE))
			htmltext = "35437-07a.htm";
		else
		{
			switch (st.getState())
			{
				case CREATED:
					if (!BANDIT_STRONGHOLD.isWaitingBattle())
						htmltext = getHtmlText("35437-03.htm").replace("%nextSiege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(BANDIT_STRONGHOLD.getSiegeDate().getTime()));
					else if (player.getInventory().hasAtLeastOneItem(CONTEST_CERTIFICATE))
					{
						takeItems(player, CONTEST_CERTIFICATE, -1);
						takeItems(player, TARLK_AMULET, -1);
						takeItems(player, TROPHY_OF_ALLIANCE, -1);
						
						htmltext = "35437-06.htm";
						playSound(player, SOUND_FINISH);
						st.exitQuest(true);
					}
					else
						htmltext = "35437-01.htm";
					break;
				
				case STARTED:
					if (player.getInventory().getItemCount(TARLK_AMULET) < 30)
						htmltext = "35437-07.htm";
					else
					{
						htmltext = "35437-08.htm";
						takeItems(player, TARLK_AMULET, 30);
						takeItems(player, CONTEST_CERTIFICATE, -1);
						takeItems(player, TROPHY_OF_ALLIANCE, -1);
						rewardItems(player, TROPHY_OF_ALLIANCE, 1);
						playSound(player, SOUND_FINISH);
						st.exitQuest(true);
					}
					break;
				
				case COMPLETED:
					htmltext = "35437-07a.htm";
					break;
			}
		}
		return htmltext;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		dropItems(player, TARLK_AMULET, 1, 30, MONSTERS.get(npc.getNpcId()));
	}
}