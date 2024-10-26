package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q413_PathToAShillienOracle extends Quest
{
	private static final String QUEST_NAME = "Q413_PathToAShillienOracle";
	
	// Items
	private static final int SIDRA_LETTER = 1262;
	private static final int BLANK_SHEET = 1263;
	private static final int BLOODY_RUNE = 1264;
	private static final int GARMIEL_BOOK = 1265;
	private static final int PRAYER_OF_ADONIUS = 1266;
	private static final int PENITENT_MARK = 1267;
	private static final int ASHEN_BONES = 1268;
	private static final int ANDARIEL_BOOK = 1269;
	private static final int ORB_OF_ABYSS = 1270;
	
	// NPCs
	private static final int SIDRA = 30330;
	private static final int ADONIUS = 30375;
	private static final int TALBOT = 30377;
	
	public Q413_PathToAShillienOracle()
	{
		super(413, "Path to a Shillien Oracle");
		
		setItemsIds(SIDRA_LETTER, BLANK_SHEET, BLOODY_RUNE, GARMIEL_BOOK, PRAYER_OF_ADONIUS, PENITENT_MARK, ASHEN_BONES, ANDARIEL_BOOK);
		
		addQuestStart(SIDRA);
		addTalkId(SIDRA, ADONIUS, TALBOT);
		
		addMyDying(20776, 20457, 20458, 20514, 20515);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30330-05.htm"))
		{
			if (player.getClassId() != ClassId.DARK_MYSTIC)
				htmltext = (player.getClassId() == ClassId.SHILLIEN_ORACLE) ? "30330-02a.htm" : "30330-03.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30330-02.htm";
			else if (player.getInventory().hasItem(ORB_OF_ABYSS))
				htmltext = "30330-04.htm";
		}
		else if (event.equalsIgnoreCase("30330-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, SIDRA_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30377-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SIDRA_LETTER, 1);
			giveItems(player, BLANK_SHEET, 5);
		}
		else if (event.equalsIgnoreCase("30375-04.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, PRAYER_OF_ADONIUS, 1);
			giveItems(player, PENITENT_MARK, 1);
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
				htmltext = "30330-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case SIDRA:
						if (cond == 1)
							htmltext = "30330-07.htm";
						else if (cond > 1 && cond < 4)
							htmltext = "30330-08.htm";
						else if (cond > 3 && cond < 7)
							htmltext = "30330-09.htm";
						else if (cond == 7)
						{
							htmltext = "30330-10.htm";
							takeItems(player, ANDARIEL_BOOK, 1);
							takeItems(player, GARMIEL_BOOK, 1);
							giveItems(player, ORB_OF_ABYSS, 1);
							rewardExpAndSp(player, 3200, 3120);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case TALBOT:
						if (cond == 1)
							htmltext = "30377-01.htm";
						else if (cond == 2)
							htmltext = (player.getInventory().hasItem(BLOODY_RUNE)) ? "30377-04.htm" : "30377-03.htm";
						else if (cond == 3)
						{
							htmltext = "30377-05.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BLOODY_RUNE, -1);
							giveItems(player, GARMIEL_BOOK, 1);
							giveItems(player, PRAYER_OF_ADONIUS, 1);
						}
						else if (cond > 3 && cond < 7)
							htmltext = "30377-06.htm";
						else if (cond == 7)
							htmltext = "30377-07.htm";
						break;
					
					case ADONIUS:
						if (cond == 4)
							htmltext = "30375-01.htm";
						else if (cond == 5)
							htmltext = (player.getInventory().hasItem(ASHEN_BONES)) ? "30375-05.htm" : "30375-06.htm";
						else if (cond == 6)
						{
							htmltext = "30375-07.htm";
							st.setCond(7);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ASHEN_BONES, -1);
							takeItems(player, PENITENT_MARK, -1);
							giveItems(player, ANDARIEL_BOOK, 1);
						}
						else if (cond == 7)
							htmltext = "30375-08.htm";
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
		
		if (npc.getNpcId() == 20776)
		{
			if (st.getCond() == 2)
			{
				takeItems(player, BLANK_SHEET, 1);
				if (dropItemsAlways(player, BLOODY_RUNE, 1, 5))
					st.setCond(3);
			}
		}
		else if (st.getCond() == 5 && dropItemsAlways(player, ASHEN_BONES, 1, 10))
			st.setCond(6);
	}
}