package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q105_SkirmishWithTheOrcs extends Quest
{
	private static final String QUEST_NAME = "Q105_SkirmishWithTheOrcs";
	
	// Item
	private static final int KENDELL_ORDER_1 = 1836;
	private static final int KENDELL_ORDER_2 = 1837;
	private static final int KENDELL_ORDER_3 = 1838;
	private static final int KENDELL_ORDER_4 = 1839;
	private static final int KENDELL_ORDER_5 = 1840;
	private static final int KENDELL_ORDER_6 = 1841;
	private static final int KENDELL_ORDER_7 = 1842;
	private static final int KENDELL_ORDER_8 = 1843;
	private static final int KABOO_CHIEF_TORC_1 = 1844;
	private static final int KABOO_CHIEF_TORC_2 = 1845;
	
	// Monster
	private static final int KABOO_CHIEF_UOPH = 27059;
	private static final int KABOO_CHIEF_KRACHA = 27060;
	private static final int KABOO_CHIEF_BATOH = 27061;
	private static final int KABOO_CHIEF_TANUKIA = 27062;
	private static final int KABOO_CHIEF_TUREL = 27064;
	private static final int KABOO_CHIEF_ROKO = 27065;
	private static final int KABOO_CHIEF_KAMUT = 27067;
	private static final int KABOO_CHIEF_MURTIKA = 27068;
	
	// Rewards
	private static final int RED_SUNSET_STAFF = 754;
	private static final int RED_SUNSET_SWORD = 981;
	private static final int LESSER_HEALING_POT = 1060;
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int SPIRITSHOT_NO_GRADE = 2509;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	public Q105_SkirmishWithTheOrcs()
	{
		super(105, "Skirmish with the Orcs");
		
		setItemsIds(KENDELL_ORDER_1, KENDELL_ORDER_2, KENDELL_ORDER_3, KENDELL_ORDER_4, KENDELL_ORDER_5, KENDELL_ORDER_6, KENDELL_ORDER_7, KENDELL_ORDER_8, KABOO_CHIEF_TORC_1, KABOO_CHIEF_TORC_2);
		
		addQuestStart(30218); // Kendell
		addTalkId(30218);
		
		addMyDying(KABOO_CHIEF_UOPH, KABOO_CHIEF_KRACHA, KABOO_CHIEF_BATOH, KABOO_CHIEF_TANUKIA, KABOO_CHIEF_TUREL, KABOO_CHIEF_ROKO, KABOO_CHIEF_KAMUT, KABOO_CHIEF_MURTIKA);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30218-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, Rnd.get(1836, 1839), 1); // Kendell's orders 1 to 4.
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
		
		switch (st.getState())
		{
			case CREATED:
				if (player.getRace() != ClassRace.ELF)
					htmltext = "30218-00.htm";
				else if (player.getStatus().getLevel() < 10)
					htmltext = "30218-01.htm";
				else
					htmltext = "30218-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				if (cond == 1)
					htmltext = "30218-05.htm";
				else if (cond == 2)
				{
					htmltext = "30218-06.htm";
					st.setCond(3);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, KABOO_CHIEF_TORC_1, 1);
					takeItems(player, KENDELL_ORDER_1, 1);
					takeItems(player, KENDELL_ORDER_2, 1);
					takeItems(player, KENDELL_ORDER_3, 1);
					takeItems(player, KENDELL_ORDER_4, 1);
					giveItems(player, Rnd.get(1840, 1843), 1); // Kendell's orders 5 to 8.
				}
				else if (cond == 3)
					htmltext = "30218-07.htm";
				else if (cond == 4)
				{
					htmltext = "30218-08.htm";
					takeItems(player, KABOO_CHIEF_TORC_2, 1);
					takeItems(player, KENDELL_ORDER_5, 1);
					takeItems(player, KENDELL_ORDER_6, 1);
					takeItems(player, KENDELL_ORDER_7, 1);
					takeItems(player, KENDELL_ORDER_8, 1);
					
					if (player.isMageClass())
					{
						giveItems(player, RED_SUNSET_STAFF, 1);
						rewardItems(player, SPIRITSHOT_NO_GRADE, 500);
					}
					else
					{
						giveItems(player, RED_SUNSET_SWORD, 1);
						rewardItems(player, SOULSHOT_NO_GRADE, 1000);
					}
					
					rewardNewbieShots(player, 7000, 3000);
					rewardItems(player, LESSER_HEALING_POT, 100);
					rewardItems(player, ECHO_BATTLE, 10);
					rewardItems(player, ECHO_LOVE, 10);
					rewardItems(player, ECHO_SOLITUDE, 10);
					rewardItems(player, ECHO_FEAST, 10);
					rewardItems(player, ECHO_CELEBRATION, 10);
					
					player.broadcastPacket(new SocialAction(player, 3));
					playSound(player, SOUND_FINISH);
					st.exitQuest(false);
				}
				break;
			
			case COMPLETED:
				htmltext = getAlreadyCompletedMsg();
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
			case KABOO_CHIEF_UOPH, KABOO_CHIEF_KRACHA, KABOO_CHIEF_BATOH, KABOO_CHIEF_TANUKIA:
				if (st.getCond() == 1 && player.getInventory().hasItem(npc.getNpcId() - 25223)) // npcId - 25223 = itemId to verify.
				{
					st.setCond(2);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, KABOO_CHIEF_TORC_1, 1);
				}
				break;
			
			case KABOO_CHIEF_TUREL, KABOO_CHIEF_ROKO:
				if (st.getCond() == 3 && player.getInventory().hasItem(npc.getNpcId() - 25224)) // npcId - 25224 = itemId to verify.
				{
					st.setCond(4);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, KABOO_CHIEF_TORC_2, 1);
				}
				break;
			
			case KABOO_CHIEF_KAMUT, KABOO_CHIEF_MURTIKA:
				if (st.getCond() == 3 && player.getInventory().hasItem(npc.getNpcId() - 25225)) // npcId - 25225 = itemId to verify.
				{
					st.setCond(4);
					playSound(player, SOUND_MIDDLE);
					giveItems(player, KABOO_CHIEF_TORC_2, 1);
				}
				break;
		}
	}
}