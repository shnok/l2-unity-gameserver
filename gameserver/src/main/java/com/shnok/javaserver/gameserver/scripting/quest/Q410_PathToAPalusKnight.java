package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q410_PathToAPalusKnight extends Quest
{
	private static final String QUEST_NAME = "Q410_PathToAPalusKnight";
	
	// Items
	private static final int PALUS_TALISMAN = 1237;
	private static final int LYCANTHROPE_SKULL = 1238;
	private static final int VIRGIL_LETTER = 1239;
	private static final int MORTE_TALISMAN = 1240;
	private static final int PREDATOR_CARAPACE = 1241;
	private static final int ARACHNID_TRACKER_SILK = 1242;
	private static final int COFFIN_OF_ETERNAL_REST = 1243;
	private static final int GAZE_OF_ABYSS = 1244;
	
	// NPCs
	private static final int KALINTA = 30422;
	private static final int VIRGIL = 30329;
	
	// Monsters
	private static final int POISON_SPIDER = 20038;
	private static final int ARACHNID_TRACKER = 20043;
	private static final int LYCANTHROPE = 20049;
	
	public Q410_PathToAPalusKnight()
	{
		super(410, "Path to a Palus Knight");
		
		setItemsIds(PALUS_TALISMAN, LYCANTHROPE_SKULL, VIRGIL_LETTER, MORTE_TALISMAN, PREDATOR_CARAPACE, ARACHNID_TRACKER_SILK, COFFIN_OF_ETERNAL_REST);
		
		addQuestStart(VIRGIL);
		addTalkId(VIRGIL, KALINTA);
		
		addMyDying(POISON_SPIDER, ARACHNID_TRACKER, LYCANTHROPE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30329-05.htm"))
		{
			if (player.getClassId() != ClassId.DARK_FIGHTER)
				htmltext = (player.getClassId() == ClassId.PALUS_KNIGHT) ? "30329-02a.htm" : "30329-03.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30329-02.htm";
			else if (player.getInventory().hasItem(GAZE_OF_ABYSS))
				htmltext = "30329-04.htm";
		}
		else if (event.equalsIgnoreCase("30329-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, PALUS_TALISMAN, 1);
		}
		else if (event.equalsIgnoreCase("30329-10.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LYCANTHROPE_SKULL, -1);
			takeItems(player, PALUS_TALISMAN, 1);
			giveItems(player, VIRGIL_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30422-02.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, VIRGIL_LETTER, 1);
			giveItems(player, MORTE_TALISMAN, 1);
		}
		else if (event.equalsIgnoreCase("30422-06.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ARACHNID_TRACKER_SILK, -1);
			takeItems(player, MORTE_TALISMAN, 1);
			takeItems(player, PREDATOR_CARAPACE, -1);
			giveItems(player, COFFIN_OF_ETERNAL_REST, 1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		switch (st.getState())
		{
			case CREATED:
				htmltext = "30329-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case VIRGIL:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(LYCANTHROPE_SKULL)) ? "30329-07.htm" : "30329-08.htm";
						else if (cond == 2)
							htmltext = "30329-09.htm";
						else if (cond > 2 && cond < 6)
							htmltext = "30329-12.htm";
						else if (cond == 6)
						{
							htmltext = "30329-11.htm";
							takeItems(player, COFFIN_OF_ETERNAL_REST, 1);
							giveItems(player, GAZE_OF_ABYSS, 1);
							rewardExpAndSp(player, 3200, 1500);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case KALINTA:
						if (cond == 3)
							htmltext = "30422-01.htm";
						else if (cond == 4)
						{
							if (!player.getInventory().hasItem(ARACHNID_TRACKER_SILK) || !player.getInventory().hasItem(PREDATOR_CARAPACE))
								htmltext = "30422-03.htm";
							else
								htmltext = "30422-04.htm";
						}
						else if (cond == 5)
							htmltext = "30422-05.htm";
						else if (cond == 6)
							htmltext = "30422-06.htm";
						break;
				}
				break;
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
		
		switch (npc.getNpcId())
		{
			case LYCANTHROPE:
				if (st.getCond() == 1 && dropItemsAlways(player, LYCANTHROPE_SKULL, 1, 13))
					st.setCond(2);
				break;
			
			case ARACHNID_TRACKER:
				if (st.getCond() == 4 && dropItemsAlways(player, ARACHNID_TRACKER_SILK, 1, 5) && player.getInventory().hasItem(PREDATOR_CARAPACE))
					st.setCond(5);
				break;
			
			case POISON_SPIDER:
				if (st.getCond() == 4 && dropItemsAlways(player, PREDATOR_CARAPACE, 1, 1) && player.getInventory().getItemCount(ARACHNID_TRACKER_SILK) == 5)
					st.setCond(5);
				break;
		}
	}
}