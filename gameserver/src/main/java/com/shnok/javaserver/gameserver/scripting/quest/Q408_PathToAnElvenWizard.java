package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q408_PathToAnElvenWizard extends Quest
{
	private static final String QUEST_NAME = "Q408_PathToAnElvenWizard";
	
	// Items
	private static final int ROSELLA_LETTER = 1218;
	private static final int RED_DOWN = 1219;
	private static final int MAGICAL_POWERS_RUBY = 1220;
	private static final int PURE_AQUAMARINE = 1221;
	private static final int APPETIZING_APPLE = 1222;
	private static final int GOLD_LEAVES = 1223;
	private static final int IMMORTAL_LOVE = 1224;
	private static final int AMETHYST = 1225;
	private static final int NOBILITY_AMETHYST = 1226;
	private static final int FERTILITY_PERIDOT = 1229;
	private static final int ETERNITY_DIAMOND = 1230;
	private static final int CHARM_OF_GRAIN = 1272;
	private static final int SAP_OF_THE_MOTHER_TREE = 1273;
	private static final int LUCKY_POTPOURRI = 1274;
	
	// NPCs
	private static final int ROSELLA = 30414;
	private static final int GREENIS = 30157;
	private static final int THALIA = 30371;
	private static final int NORTHWIND = 30423;
	
	public Q408_PathToAnElvenWizard()
	{
		super(408, "Path to an Elven Wizard");
		
		setItemsIds(ROSELLA_LETTER, RED_DOWN, MAGICAL_POWERS_RUBY, PURE_AQUAMARINE, APPETIZING_APPLE, GOLD_LEAVES, IMMORTAL_LOVE, AMETHYST, NOBILITY_AMETHYST, FERTILITY_PERIDOT, CHARM_OF_GRAIN, SAP_OF_THE_MOTHER_TREE, LUCKY_POTPOURRI);
		
		addQuestStart(ROSELLA);
		addTalkId(ROSELLA, GREENIS, THALIA, NORTHWIND);
		
		addMyDying(20047, 20019, 20466);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30414-06.htm"))
		{
			if (player.getClassId() != ClassId.ELVEN_MYSTIC)
				htmltext = (player.getClassId() == ClassId.ELVEN_WIZARD) ? "30414-02a.htm" : "30414-03.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30414-04.htm";
			else if (player.getInventory().hasItem(ETERNITY_DIAMOND))
				htmltext = "30414-05.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				giveItems(player, FERTILITY_PERIDOT, 1);
			}
		}
		else if (event.equalsIgnoreCase("30414-07.htm"))
		{
			if (!player.getInventory().hasItem(MAGICAL_POWERS_RUBY))
			{
				playSound(player, SOUND_MIDDLE);
				giveItems(player, ROSELLA_LETTER, 1);
			}
			else
				htmltext = "30414-10.htm";
		}
		else if (event.equalsIgnoreCase("30414-14.htm"))
		{
			if (!player.getInventory().hasItem(PURE_AQUAMARINE))
			{
				playSound(player, SOUND_MIDDLE);
				giveItems(player, APPETIZING_APPLE, 1);
			}
			else
				htmltext = "30414-13.htm";
		}
		else if (event.equalsIgnoreCase("30414-18.htm"))
		{
			if (!player.getInventory().hasItem(NOBILITY_AMETHYST))
			{
				playSound(player, SOUND_MIDDLE);
				giveItems(player, IMMORTAL_LOVE, 1);
			}
			else
				htmltext = "30414-17.htm";
		}
		else if (event.equalsIgnoreCase("30157-02.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			takeItems(player, ROSELLA_LETTER, 1);
			giveItems(player, CHARM_OF_GRAIN, 1);
		}
		else if (event.equalsIgnoreCase("30371-02.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			takeItems(player, APPETIZING_APPLE, 1);
			giveItems(player, SAP_OF_THE_MOTHER_TREE, 1);
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
				htmltext = "30414-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case ROSELLA:
						if (player.getInventory().hasItems(MAGICAL_POWERS_RUBY, NOBILITY_AMETHYST, PURE_AQUAMARINE))
						{
							htmltext = "30414-24.htm";
							takeItems(player, FERTILITY_PERIDOT, 1);
							takeItems(player, MAGICAL_POWERS_RUBY, 1);
							takeItems(player, NOBILITY_AMETHYST, 1);
							takeItems(player, PURE_AQUAMARINE, 1);
							giveItems(player, ETERNITY_DIAMOND, 1);
							rewardExpAndSp(player, 3200, 1890);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						else if (player.getInventory().hasItem(ROSELLA_LETTER))
							htmltext = "30414-08.htm";
						else if (player.getInventory().hasItem(CHARM_OF_GRAIN))
						{
							if (player.getInventory().getItemCount(RED_DOWN) == 5)
								htmltext = "30414-25.htm";
							else
								htmltext = "30414-09.htm";
						}
						else if (player.getInventory().hasItem(APPETIZING_APPLE))
							htmltext = "30414-15.htm";
						else if (player.getInventory().hasItem(SAP_OF_THE_MOTHER_TREE))
						{
							if (player.getInventory().getItemCount(GOLD_LEAVES) == 5)
								htmltext = "30414-26.htm";
							else
								htmltext = "30414-16.htm";
						}
						else if (player.getInventory().hasItem(IMMORTAL_LOVE))
							htmltext = "30414-19.htm";
						else if (player.getInventory().hasItem(LUCKY_POTPOURRI))
						{
							if (player.getInventory().getItemCount(AMETHYST) == 2)
								htmltext = "30414-27.htm";
							else
								htmltext = "30414-20.htm";
						}
						else
							htmltext = "30414-11.htm";
						break;
					
					case GREENIS:
						if (player.getInventory().hasItem(ROSELLA_LETTER))
							htmltext = "30157-01.htm";
						else if (player.getInventory().getItemCount(RED_DOWN) == 5)
						{
							htmltext = "30157-04.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CHARM_OF_GRAIN, 1);
							takeItems(player, RED_DOWN, -1);
							giveItems(player, MAGICAL_POWERS_RUBY, 1);
						}
						else if (player.getInventory().hasItem(CHARM_OF_GRAIN))
							htmltext = "30157-03.htm";
						break;
					
					case THALIA:
						if (player.getInventory().hasItem(APPETIZING_APPLE))
							htmltext = "30371-01.htm";
						else if (player.getInventory().getItemCount(GOLD_LEAVES) == 5)
						{
							htmltext = "30371-04.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, GOLD_LEAVES, -1);
							takeItems(player, SAP_OF_THE_MOTHER_TREE, 1);
							giveItems(player, PURE_AQUAMARINE, 1);
						}
						else if (player.getInventory().hasItem(SAP_OF_THE_MOTHER_TREE))
							htmltext = "30371-03.htm";
						break;
					
					case NORTHWIND:
						if (player.getInventory().hasItem(IMMORTAL_LOVE))
						{
							htmltext = "30423-01.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, IMMORTAL_LOVE, 1);
							giveItems(player, LUCKY_POTPOURRI, 1);
						}
						else if (player.getInventory().getItemCount(AMETHYST) == 2)
						{
							htmltext = "30423-03.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, AMETHYST, -1);
							takeItems(player, LUCKY_POTPOURRI, 1);
							giveItems(player, NOBILITY_AMETHYST, 1);
						}
						else if (player.getInventory().hasItem(LUCKY_POTPOURRI))
							htmltext = "30423-02.htm";
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
			case 20019:
				if (player.getInventory().hasItem(SAP_OF_THE_MOTHER_TREE))
					dropItems(player, GOLD_LEAVES, 1, 5, 400000);
				break;
			
			case 20047:
				if (player.getInventory().hasItem(LUCKY_POTPOURRI))
					dropItems(player, AMETHYST, 1, 2, 400000);
				break;
			
			case 20466:
				if (player.getInventory().hasItem(CHARM_OF_GRAIN))
					dropItems(player, RED_DOWN, 1, 5, 700000);
				break;
		}
	}
}