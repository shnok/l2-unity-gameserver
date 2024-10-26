package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q241_PossessorOfAPreciousSoul extends Quest
{
	private static final String QUEST_NAME = "Q241_PossessorOfAPreciousSoul";
	
	// NPCs
	private static final int TALIEN = 31739;
	private static final int GABRIELLE = 30753;
	private static final int GILMORE = 30754;
	private static final int KANTABILON = 31042;
	private static final int STEDMIEL = 30692;
	private static final int VIRGIL = 31742;
	private static final int OGMAR = 31744;
	private static final int RAHORAKTI = 31336;
	private static final int KASSANDRA = 31743;
	private static final int CARADINE = 31740;
	private static final int NOEL = 31272;
	
	// Monsters
	private static final int BARAHAM = 27113;
	private static final int MALRUK_SUCCUBUS_1 = 20244;
	private static final int MALRUK_SUCCUBUS_TUREN_1 = 20245;
	private static final int MALRUK_SUCCUBUS_2 = 20283;
	private static final int MALRUK_SUCCUBUS_TUREN_2 = 20284;
	private static final int SPLINTER_STAKATO = 21508;
	private static final int SPLINTER_STAKATO_WALKER = 21509;
	private static final int SPLINTER_STAKATO_SOLDIER = 21510;
	private static final int SPLINTER_STAKATO_DRONE_1 = 21511;
	private static final int SPLINTER_STAKATO_DRONE_2 = 21512;
	
	// Items
	private static final int LEGEND_OF_SEVENTEEN = 7587;
	private static final int MALRUK_SUCCUBUS_CLAW = 7597;
	private static final int ECHO_CRYSTAL = 7589;
	private static final int POETRY_BOOK = 7588;
	private static final int CRIMSON_MOSS = 7598;
	private static final int RAHORAKTI_MEDICINE = 7599;
	private static final int LUNARGENT = 6029;
	private static final int HELLFIRE_OIL = 6033;
	private static final int VIRGIL_LETTER = 7677;
	
	public Q241_PossessorOfAPreciousSoul()
	{
		super(241, "Possessor of a Precious Soul - 1");
		
		setItemsIds(LEGEND_OF_SEVENTEEN, MALRUK_SUCCUBUS_CLAW, ECHO_CRYSTAL, POETRY_BOOK, CRIMSON_MOSS, RAHORAKTI_MEDICINE);
		
		addQuestStart(TALIEN);
		addTalkId(TALIEN, GABRIELLE, GILMORE, KANTABILON, STEDMIEL, VIRGIL, OGMAR, RAHORAKTI, KASSANDRA, CARADINE, NOEL);
		
		addMyDying(BARAHAM, MALRUK_SUCCUBUS_1, MALRUK_SUCCUBUS_2, MALRUK_SUCCUBUS_TUREN_1, MALRUK_SUCCUBUS_TUREN_2, SPLINTER_STAKATO, SPLINTER_STAKATO_WALKER, SPLINTER_STAKATO_SOLDIER, SPLINTER_STAKATO_DRONE_1, SPLINTER_STAKATO_DRONE_2);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Talien
		if (event.equalsIgnoreCase("31739-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31739-07.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, LEGEND_OF_SEVENTEEN, 1);
		}
		else if (event.equalsIgnoreCase("31739-10.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ECHO_CRYSTAL, 1);
		}
		else if (event.equalsIgnoreCase("31739-13.htm"))
		{
			st.setCond(11);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, POETRY_BOOK, 1);
		}
		// Gabrielle
		else if (event.equalsIgnoreCase("30753-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		// Gilmore
		else if (event.equalsIgnoreCase("30754-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		// Kantabilon
		else if (event.equalsIgnoreCase("31042-02.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31042-05.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, MALRUK_SUCCUBUS_CLAW, -1);
			giveItems(player, ECHO_CRYSTAL, 1);
		}
		// Stedmiel
		else if (event.equalsIgnoreCase("30692-02.htm"))
		{
			st.setCond(10);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, POETRY_BOOK, 1);
		}
		// Virgil
		else if (event.equalsIgnoreCase("31742-02.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31742-05.htm"))
		{
			st.setCond(18);
			playSound(player, SOUND_MIDDLE);
		}
		// Ogmar
		else if (event.equalsIgnoreCase("31744-02.htm"))
		{
			st.setCond(13);
			playSound(player, SOUND_MIDDLE);
		}
		// Rahorakti
		else if (event.equalsIgnoreCase("31336-02.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31336-05.htm"))
		{
			st.setCond(16);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, CRIMSON_MOSS, -1);
			giveItems(player, RAHORAKTI_MEDICINE, 1);
		}
		// Kassandra
		else if (event.equalsIgnoreCase("31743-02.htm"))
		{
			st.setCond(17);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, RAHORAKTI_MEDICINE, 1);
		}
		// Caradine
		else if (event.equalsIgnoreCase("31740-02.htm"))
		{
			st.setCond(19);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31740-05.htm"))
		{
			giveItems(player, VIRGIL_LETTER, 1);
			rewardExpAndSp(player, 263043, 0);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		// Noel
		else if (event.equalsIgnoreCase("31272-02.htm"))
		{
			st.setCond(20);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31272-05.htm"))
		{
			if (player.getInventory().hasItem(HELLFIRE_OIL) && player.getInventory().getItemCount(LUNARGENT) >= 5)
			{
				st.setCond(21);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, LUNARGENT, 5);
				takeItems(player, HELLFIRE_OIL, 1);
			}
			else
				htmltext = "31272-07.htm";
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
				htmltext = (!player.isSubClassActive() || player.getStatus().getLevel() < 50) ? "31739-02.htm" : "31739-01.htm";
				break;
			
			case STARTED:
				if (!player.isSubClassActive())
					break;
				
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case TALIEN:
						if (cond == 1)
							htmltext = "31739-04.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "31739-05.htm";
						else if (cond == 4)
							htmltext = "31739-06.htm";
						else if (cond == 5)
							htmltext = "31739-08.htm";
						else if (cond == 8)
							htmltext = "31739-09.htm";
						else if (cond == 9)
							htmltext = "31739-11.htm";
						else if (cond == 10)
							htmltext = "31739-12.htm";
						else if (cond == 11)
							htmltext = "31739-14.htm";
						break;
					
					case GABRIELLE:
						if (cond == 1)
							htmltext = "30753-01.htm";
						else if (cond == 2)
							htmltext = "30753-03.htm";
						break;
					
					case GILMORE:
						if (cond == 2)
							htmltext = "30754-01.htm";
						else if (cond == 3)
							htmltext = "30754-03.htm";
						break;
					
					case KANTABILON:
						if (cond == 5)
							htmltext = "31042-01.htm";
						else if (cond == 6)
							htmltext = "31042-03.htm";
						else if (cond == 7)
							htmltext = "31042-04.htm";
						else if (cond == 8)
							htmltext = "31042-06.htm";
						break;
					
					case STEDMIEL:
						if (cond == 9)
							htmltext = "30692-01.htm";
						else if (cond == 10)
							htmltext = "30692-03.htm";
						break;
					
					case VIRGIL:
						if (cond == 11)
							htmltext = "31742-01.htm";
						else if (cond == 12)
							htmltext = "31742-03.htm";
						else if (cond == 17)
							htmltext = "31742-04.htm";
						else if (cond == 18)
							htmltext = "31742-06.htm";
						break;
					
					case OGMAR:
						if (cond == 12)
							htmltext = "31744-01.htm";
						else if (cond == 13)
							htmltext = "31744-03.htm";
						break;
					
					case RAHORAKTI:
						if (cond == 13)
							htmltext = "31336-01.htm";
						else if (cond == 14)
							htmltext = "31336-03.htm";
						else if (cond == 15)
							htmltext = "31336-04.htm";
						else if (cond == 16)
							htmltext = "31336-06.htm";
						break;
					
					case KASSANDRA:
						if (cond == 16)
							htmltext = "31743-01.htm";
						else if (cond == 17)
							htmltext = "31743-03.htm";
						break;
					
					case CARADINE:
						if (cond == 18)
							htmltext = "31740-01.htm";
						else if (cond == 19)
							htmltext = "31740-03.htm";
						else if (cond == 21)
							htmltext = "31740-04.htm";
						break;
					
					case NOEL:
						if (cond == 19)
							htmltext = "31272-01.htm";
						else if (cond == 20)
						{
							if (player.getInventory().hasItem(HELLFIRE_OIL) && player.getInventory().getItemCount(LUNARGENT) >= 5)
								htmltext = "31272-04.htm";
							else
								htmltext = "31272-03.htm";
						}
						else if (cond == 21)
							htmltext = "31272-06.htm";
						break;
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
		if (st == null || !player.isSubClassActive())
			return;
		
		switch (npc.getNpcId())
		{
			case BARAHAM:
				if (st.getCond() == 3)
				{
					st.setCond(4);
					giveItems(player, LEGEND_OF_SEVENTEEN, 1);
					playSound(player, SOUND_MIDDLE);
				}
				break;
			
			case MALRUK_SUCCUBUS_1, MALRUK_SUCCUBUS_2:
				if (st.getCond() == 6 && dropItems(player, MALRUK_SUCCUBUS_CLAW, 1, 10, 100000))
					st.setCond(7);
				break;
			
			case MALRUK_SUCCUBUS_TUREN_1, MALRUK_SUCCUBUS_TUREN_2:
				if (st.getCond() == 6 && dropItems(player, MALRUK_SUCCUBUS_CLAW, 1, 10, 120000))
					st.setCond(7);
				break;
			
			case SPLINTER_STAKATO, SPLINTER_STAKATO_WALKER, SPLINTER_STAKATO_SOLDIER, SPLINTER_STAKATO_DRONE_1, SPLINTER_STAKATO_DRONE_2:
				if (st.getCond() == 14 && dropItems(player, CRIMSON_MOSS, 1, 5, 100000))
					st.setCond(15);
				break;
		}
	}
}