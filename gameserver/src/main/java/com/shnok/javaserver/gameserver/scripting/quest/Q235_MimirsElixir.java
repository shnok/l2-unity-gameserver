package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillUse;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q235_MimirsElixir extends Quest
{
	private static final String QUEST_NAME = "Q235_MimirsElixir";
	
	// Items
	private static final int STAR_OF_DESTINY = 5011;
	private static final int PURE_SILVER = 6320;
	private static final int TRUE_GOLD = 6321;
	private static final int SAGE_STONE = 6322;
	private static final int BLOOD_FIRE = 6318;
	private static final int MIMIR_ELIXIR = 6319;
	private static final int MAGISTER_MIXING_STONE = 5905;
	
	// Reward
	private static final int SCROLL_ENCHANT_WEAPON_A = 729;
	
	// NPCs
	private static final int JOAN = 30718;
	private static final int LADD = 30721;
	private static final int MIXING_URN = 31149;
	
	public Q235_MimirsElixir()
	{
		super(235, "Mimir's Elixir");
		
		setItemsIds(PURE_SILVER, TRUE_GOLD, SAGE_STONE, BLOOD_FIRE, MAGISTER_MIXING_STONE, MIMIR_ELIXIR);
		
		addQuestStart(LADD);
		addTalkId(LADD, JOAN, MIXING_URN);
		
		addMyDying(20965, 21090);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30721-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30721-12.htm") && player.getInventory().hasItem(TRUE_GOLD))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, MAGISTER_MIXING_STONE, 1);
		}
		else if (event.equalsIgnoreCase("30721-16.htm") && player.getInventory().hasItem(MIMIR_ELIXIR))
		{
			player.broadcastPacket(new MagicSkillUse(player, player, 4339, 1, 1, 1));
			
			takeItems(player, MAGISTER_MIXING_STONE, -1);
			takeItems(player, MIMIR_ELIXIR, -1);
			takeItems(player, STAR_OF_DESTINY, -1);
			giveItems(player, SCROLL_ENCHANT_WEAPON_A, 1);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(false);
		}
		else if (event.equalsIgnoreCase("30718-03.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31149-02.htm"))
		{
			if (!player.getInventory().hasItem(MAGISTER_MIXING_STONE))
				htmltext = "31149-havent.htm";
		}
		else if (event.equalsIgnoreCase("31149-03.htm"))
		{
			if (!player.getInventory().hasItems(MAGISTER_MIXING_STONE, PURE_SILVER))
				htmltext = "31149-havent.htm";
		}
		else if (event.equalsIgnoreCase("31149-05.htm"))
		{
			if (!player.getInventory().hasItems(MAGISTER_MIXING_STONE, PURE_SILVER, TRUE_GOLD))
				htmltext = "31149-havent.htm";
		}
		else if (event.equalsIgnoreCase("31149-07.htm"))
		{
			if (!player.getInventory().hasItems(MAGISTER_MIXING_STONE, PURE_SILVER, TRUE_GOLD, BLOOD_FIRE))
				htmltext = "31149-havent.htm";
		}
		else if (event.equalsIgnoreCase("31149-success.htm"))
		{
			if (player.getInventory().hasItems(MAGISTER_MIXING_STONE, PURE_SILVER, TRUE_GOLD, BLOOD_FIRE))
			{
				st.setCond(8);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, PURE_SILVER, -1);
				takeItems(player, TRUE_GOLD, -1);
				takeItems(player, BLOOD_FIRE, -1);
				giveItems(player, MIMIR_ELIXIR, 1);
			}
			else
				htmltext = "31149-havent.htm";
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
				if (player.getStatus().getLevel() < 75)
					htmltext = "30721-01b.htm";
				else if (!player.getInventory().hasItem(STAR_OF_DESTINY))
					htmltext = "30721-01a.htm";
				else
					htmltext = "30721-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case LADD:
						if (cond == 1)
						{
							if (player.getInventory().hasItem(PURE_SILVER))
							{
								htmltext = "30721-08.htm";
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								htmltext = "30721-07.htm";
						}
						else if (cond < 5)
							htmltext = "30721-10.htm";
						else if (cond == 5 && player.getInventory().hasItem(TRUE_GOLD))
							htmltext = "30721-11.htm";
						else if (cond == 6 || cond == 7)
							htmltext = "30721-13.htm";
						else if (cond == 8 && player.getInventory().hasItem(MIMIR_ELIXIR))
							htmltext = "30721-14.htm";
						break;
					
					case JOAN:
						if (cond == 2)
							htmltext = "30718-01.htm";
						else if (cond == 3)
							htmltext = "30718-04.htm";
						else if (cond == 4 && player.getInventory().hasItem(SAGE_STONE))
						{
							htmltext = "30718-05.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SAGE_STONE, -1);
							giveItems(player, TRUE_GOLD, 1);
						}
						else if (cond > 4)
							htmltext = "30718-06.htm";
						break;
					
					// The urn gives the same first htm. Bypasses' events will do all the job.
					case MIXING_URN:
						htmltext = "31149-01.htm";
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
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case 20965:
				if (st.getCond() == 3 && dropItems(player, SAGE_STONE, 1, 1, 200000))
					st.setCond(4);
				break;
			
			case 21090:
				if (st.getCond() == 6 && dropItems(player, BLOOD_FIRE, 1, 1, 200000))
					st.setCond(7);
				break;
		}
	}
}