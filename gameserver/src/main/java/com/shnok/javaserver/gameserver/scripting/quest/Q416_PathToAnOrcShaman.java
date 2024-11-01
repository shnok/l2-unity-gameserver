package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q416_PathToAnOrcShaman extends Quest
{
	private static final String QUEST_NAME = "Q416_PathToAnOrcShaman";
	
	// Items
	private static final int FIRE_CHARM = 1616;
	private static final int KASHA_BEAR_PELT = 1617;
	private static final int KASHA_BLADE_SPIDER_HUSK = 1618;
	private static final int FIERY_EGG_1 = 1619;
	private static final int HESTUI_MASK = 1620;
	private static final int FIERY_EGG_2 = 1621;
	private static final int TOTEM_SPIRIT_CLAW = 1622;
	private static final int TATARU_LETTER = 1623;
	private static final int FLAME_CHARM = 1624;
	private static final int GRIZZLY_BLOOD = 1625;
	private static final int BLOOD_CAULDRON = 1626;
	private static final int SPIRIT_NET = 1627;
	private static final int BOUND_DURKA_SPIRIT = 1628;
	private static final int DURKA_PARASITE = 1629;
	private static final int TOTEM_SPIRIT_BLOOD = 1630;
	private static final int MASK_OF_MEDIUM = 1631;
	
	// NPCs
	private static final int TATARU_ZU_HESTUI = 30585;
	private static final int UMOS = 30502;
	private static final int HESTUI_TOTEM_SPIRIT = 30592;
	private static final int DUDA_MARA_TOTEM_SPIRIT = 30593;
	private static final int MOIRA = 31979;
	private static final int TOTEM_SPIRIT_OF_GANDI = 32057;
	private static final int DEAD_LEOPARD_CARCASS = 32090;
	
	// Monsters
	private static final int VENOMOUS_SPIDER = 20038;
	private static final int ARACHNID_TRACKER = 20043;
	private static final int GRIZZLY_BEAR = 20335;
	private static final int SCARLET_SALAMANDER = 20415;
	private static final int KASHA_BLADE_SPIDER = 20478;
	private static final int KASHA_BEAR = 20479;
	private static final int DURKA_SPIRIT = 27056;
	private static final int BLACK_LEOPARD = 27319;
	
	public Q416_PathToAnOrcShaman()
	{
		super(416, "Path To An Orc Shaman");
		
		setItemsIds(FIRE_CHARM, KASHA_BEAR_PELT, KASHA_BLADE_SPIDER_HUSK, FIERY_EGG_1, HESTUI_MASK, FIERY_EGG_2, TOTEM_SPIRIT_CLAW, TATARU_LETTER, FLAME_CHARM, GRIZZLY_BLOOD, BLOOD_CAULDRON, SPIRIT_NET, BOUND_DURKA_SPIRIT, DURKA_PARASITE, TOTEM_SPIRIT_BLOOD);
		
		addQuestStart(TATARU_ZU_HESTUI);
		addTalkId(TATARU_ZU_HESTUI, UMOS, HESTUI_TOTEM_SPIRIT, DUDA_MARA_TOTEM_SPIRIT, MOIRA, TOTEM_SPIRIT_OF_GANDI, DEAD_LEOPARD_CARCASS);
		
		addMyDying(VENOMOUS_SPIDER, ARACHNID_TRACKER, GRIZZLY_BEAR, SCARLET_SALAMANDER, KASHA_BLADE_SPIDER, KASHA_BEAR, DURKA_SPIRIT, BLACK_LEOPARD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// TATARU ZU HESTUI
		if (event.equalsIgnoreCase("30585-05.htm"))
		{
			if (player.getClassId() != ClassId.ORC_MYSTIC)
				htmltext = (player.getClassId() == ClassId.ORC_SHAMAN) ? "30585-02a.htm" : "30585-02.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30585-03.htm";
			else if (player.getInventory().hasItem(MASK_OF_MEDIUM))
				htmltext = "30585-04.htm";
		}
		else if (event.equalsIgnoreCase("30585-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, FIRE_CHARM, 1);
		}
		else if (event.equalsIgnoreCase("30585-11b.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, TOTEM_SPIRIT_CLAW, 1);
			giveItems(player, TATARU_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30585-11c.htm"))
		{
			st.setCond(12);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, TOTEM_SPIRIT_CLAW, 1);
		}
		// HESTUI TOTEM SPIRIT
		else if (event.equalsIgnoreCase("30592-03.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, HESTUI_MASK, 1);
			takeItems(player, FIERY_EGG_2, 1);
			giveItems(player, TOTEM_SPIRIT_CLAW, 1);
		}
		// DUDA MARA TOTEM SPIRIT
		else if (event.equalsIgnoreCase("30593-03.htm"))
		{
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, BLOOD_CAULDRON, 1);
			giveItems(player, SPIRIT_NET, 1);
		}
		// TOTEM SPIRIT OF GANDI
		else if (event.equalsIgnoreCase("32057-02.htm"))
		{
			st.setCond(14);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32057-05.htm"))
		{
			st.setCond(21);
			playSound(player, SOUND_MIDDLE);
		}
		// DEAD LEOPARD CARCASS
		else if (event.equalsIgnoreCase("32090-04.htm"))
		{
			st.setCond(18);
			playSound(player, SOUND_MIDDLE);
		}
		// UMOS
		else if (event.equalsIgnoreCase("30502-07.htm"))
		{
			takeItems(player, TOTEM_SPIRIT_BLOOD, -1);
			giveItems(player, MASK_OF_MEDIUM, 1);
			rewardExpAndSp(player, 3200, 2600);
			player.broadcastPacket(new SocialAction(player, 3));
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				htmltext = "30585-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case TATARU_ZU_HESTUI:
						if (cond == 1)
							htmltext = "30585-07.htm";
						else if (cond == 2)
						{
							htmltext = "30585-08.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, FIERY_EGG_1, 1);
							takeItems(player, FIRE_CHARM, 1);
							takeItems(player, KASHA_BEAR_PELT, 1);
							takeItems(player, KASHA_BLADE_SPIDER_HUSK, 1);
							giveItems(player, FIERY_EGG_2, 1);
							giveItems(player, HESTUI_MASK, 1);
						}
						else if (cond == 3)
							htmltext = "30585-09.htm";
						else if (cond == 4)
							htmltext = "30585-10.htm";
						else if (cond == 5)
							htmltext = "30585-12.htm";
						else if (cond > 5 && cond < 12)
							htmltext = "30585-13.htm";
						else if (cond == 12)
							htmltext = "30585-11c.htm";
						break;
					
					case HESTUI_TOTEM_SPIRIT:
						if (cond == 3)
							htmltext = "30592-01.htm";
						else if (cond == 4)
							htmltext = "30592-04.htm";
						else if (cond > 4 && cond < 12)
							htmltext = "30592-05.htm";
						break;
					
					case UMOS:
						if (cond == 5)
						{
							htmltext = "30502-01.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, TATARU_LETTER, 1);
							giveItems(player, FLAME_CHARM, 1);
						}
						else if (cond == 6)
							htmltext = "30502-02.htm";
						else if (cond == 7)
						{
							htmltext = "30502-03.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, FLAME_CHARM, 1);
							takeItems(player, GRIZZLY_BLOOD, 3);
							giveItems(player, BLOOD_CAULDRON, 1);
						}
						else if (cond == 8)
							htmltext = "30502-04.htm";
						else if (cond == 9 || cond == 10)
							htmltext = "30502-05.htm";
						else if (cond == 11)
							htmltext = "30502-06.htm";
						break;
					
					case MOIRA:
						if (cond == 12)
						{
							htmltext = "31979-01.htm";
							st.setCond(13);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond > 12 && cond < 21)
							htmltext = "31979-02.htm";
						else if (cond == 21)
						{
							htmltext = "31979-03.htm";
							giveItems(player, MASK_OF_MEDIUM, 1);
							rewardExpAndSp(player, 3200, 3250);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case TOTEM_SPIRIT_OF_GANDI:
						if (cond == 13)
							htmltext = "32057-01.htm";
						else if (cond > 13 && cond < 20)
							htmltext = "32057-03.htm";
						else if (cond == 20)
							htmltext = "32057-04.htm";
						break;
					
					case DUDA_MARA_TOTEM_SPIRIT:
						if (cond == 8)
							htmltext = "30593-01.htm";
						else if (cond == 9)
							htmltext = "30593-04.htm";
						else if (cond == 10)
						{
							htmltext = "30593-05.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, BOUND_DURKA_SPIRIT, 1);
							giveItems(player, TOTEM_SPIRIT_BLOOD, 1);
						}
						else if (cond == 11)
							htmltext = "30593-06.htm";
						break;
					
					case DEAD_LEOPARD_CARCASS:
						if (cond == 14)
							htmltext = "32090-01a.htm";
						else if (cond == 15)
						{
							htmltext = "32090-01.htm";
							st.setCond(16);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 16)
							htmltext = "32090-01b.htm";
						else if (cond == 17)
							htmltext = "32090-02.htm";
						else if (cond == 18)
							htmltext = "32090-05.htm";
						else if (cond == 19)
						{
							htmltext = "32090-06.htm";
							st.setCond(20);
							playSound(player, SOUND_MIDDLE);
						}
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
		
		final int cond = st.getCond();
		
		switch (npc.getNpcId())
		{
			case KASHA_BEAR:
				if (cond == 1 && !player.getInventory().hasItem(KASHA_BEAR_PELT))
				{
					giveItems(player, KASHA_BEAR_PELT, 1);
					if (player.getInventory().hasItems(FIERY_EGG_1, KASHA_BLADE_SPIDER_HUSK))
					{
						st.setCond(2);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case KASHA_BLADE_SPIDER:
				if (cond == 1 && !player.getInventory().hasItem(KASHA_BLADE_SPIDER_HUSK))
				{
					giveItems(player, KASHA_BLADE_SPIDER_HUSK, 1);
					if (player.getInventory().hasItems(KASHA_BEAR_PELT, FIERY_EGG_1))
					{
						st.setCond(2);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case SCARLET_SALAMANDER:
				if (cond == 1 && !player.getInventory().hasItem(FIERY_EGG_1))
				{
					giveItems(player, FIERY_EGG_1, 1);
					if (player.getInventory().hasItems(KASHA_BEAR_PELT, KASHA_BLADE_SPIDER_HUSK))
					{
						st.setCond(2);
						playSound(player, SOUND_MIDDLE);
					}
					else
						playSound(player, SOUND_ITEMGET);
				}
				break;
			
			case GRIZZLY_BEAR:
				if (cond == 6 && dropItemsAlways(player, GRIZZLY_BLOOD, 1, 3))
					st.setCond(7);
				break;
			
			case VENOMOUS_SPIDER, ARACHNID_TRACKER:
				if (cond == 9)
				{
					final int count = player.getInventory().getItemCount(DURKA_PARASITE);
					final int rnd = Rnd.get(10);
					if ((count == 5 && rnd < 1) || ((count == 6 || count == 7) && rnd < 2) || count >= 8)
					{
						playSound(player, SOUND_BEFORE_BATTLE);
						takeItems(player, DURKA_PARASITE, -1);
						addSpawn(DURKA_SPIRIT, npc, false, 120000, true);
					}
					else
						dropItemsAlways(player, DURKA_PARASITE, 1, 0);
				}
				break;
			
			case DURKA_SPIRIT:
				if (cond == 9)
				{
					st.setCond(10);
					playSound(player, SOUND_MIDDLE);
					takeItems(player, DURKA_PARASITE, -1);
					takeItems(player, SPIRIT_NET, 1);
					giveItems(player, BOUND_DURKA_SPIRIT, 1);
				}
				break;
			
			case BLACK_LEOPARD:
				if (cond == 14)
				{
					if (st.getInteger("leopard") > 0)
					{
						st.setCond(15);
						playSound(player, SOUND_MIDDLE);
						
						if (Rnd.get(3) < 2)
							npc.broadcastNpcSay("My dear friend of " + player.getName() + ", who has gone on ahead of me!");
					}
					else
						st.set("leopard", 1);
				}
				else if (cond == 16)
				{
					st.setCond(17);
					playSound(player, SOUND_MIDDLE);
					
					if (Rnd.get(3) < 2)
						npc.broadcastNpcSay("Listen to Tejakar Gandi, young Oroka! The spirit of the slain leopard is calling you, " + player.getName() + "!");
				}
				else if (cond == 18)
				{
					st.setCond(19);
					playSound(player, SOUND_MIDDLE);
				}
				break;
		}
	}
}