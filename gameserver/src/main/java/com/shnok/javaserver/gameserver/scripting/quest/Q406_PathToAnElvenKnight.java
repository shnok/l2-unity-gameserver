package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q406_PathToAnElvenKnight extends Quest
{
	private static final String QUEST_NAME = "Q406_PathToAnElvenKnight";
	
	// Items
	private static final int SORIUS_LETTER = 1202;
	private static final int KLUTO_BOX = 1203;
	private static final int ELVEN_KNIGHT_BROOCH = 1204;
	private static final int TOPAZ_PIECE = 1205;
	private static final int EMERALD_PIECE = 1206;
	private static final int KLUTO_MEMO = 1276;
	
	// NPCs
	private static final int SORIUS = 30327;
	private static final int KLUTO = 30317;
	
	public Q406_PathToAnElvenKnight()
	{
		super(406, "Path to an Elven Knight");
		
		setItemsIds(SORIUS_LETTER, KLUTO_BOX, TOPAZ_PIECE, EMERALD_PIECE, KLUTO_MEMO);
		
		addQuestStart(SORIUS);
		addTalkId(SORIUS, KLUTO);
		
		addMyDying(20035, 20042, 20045, 20051, 20054, 20060, 20782);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30327-05.htm"))
		{
			if (player.getClassId() != ClassId.ELVEN_FIGHTER)
				htmltext = (player.getClassId() == ClassId.ELVEN_KNIGHT) ? "30327-02a.htm" : "30327-02.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30327-03.htm";
			else if (player.getInventory().hasItem(ELVEN_KNIGHT_BROOCH))
				htmltext = "30327-04.htm";
		}
		else if (event.equalsIgnoreCase("30327-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30317-02.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, SORIUS_LETTER, 1);
			giveItems(player, KLUTO_MEMO, 1);
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
				htmltext = "30327-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case SORIUS:
						if (cond == 1)
							htmltext = (!player.getInventory().hasItem(TOPAZ_PIECE)) ? "30327-07.htm" : "30327-08.htm";
						else if (cond == 2)
						{
							htmltext = "30327-09.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							giveItems(player, SORIUS_LETTER, 1);
						}
						else if (cond > 2 && cond < 6)
							htmltext = "30327-11.htm";
						else if (cond == 6)
						{
							htmltext = "30327-10.htm";
							takeItems(player, KLUTO_BOX, 1);
							takeItems(player, KLUTO_MEMO, 1);
							giveItems(player, ELVEN_KNIGHT_BROOCH, 1);
							rewardExpAndSp(player, 3200, 2280);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case KLUTO:
						if (cond == 3)
							htmltext = "30317-01.htm";
						else if (cond == 4)
							htmltext = (!player.getInventory().hasItem(EMERALD_PIECE)) ? "30317-03.htm" : "30317-04.htm";
						else if (cond == 5)
						{
							htmltext = "30317-05.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, EMERALD_PIECE, -1);
							takeItems(player, TOPAZ_PIECE, -1);
							giveItems(player, KLUTO_BOX, 1);
						}
						else if (cond == 6)
							htmltext = "30317-06.htm";
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
			case 20035, 20042, 20045, 20051, 20054, 20060:
				if (st.getCond() == 1 && dropItems(player, TOPAZ_PIECE, 1, 20, 700000))
					st.setCond(2);
				break;
			
			case 20782:
				if (st.getCond() == 4 && dropItems(player, EMERALD_PIECE, 1, 20, 500000))
					st.setCond(5);
				break;
		}
	}
}