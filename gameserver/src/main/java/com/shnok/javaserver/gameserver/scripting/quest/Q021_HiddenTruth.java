package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q021_HiddenTruth extends Quest
{
	private static final String QUEST_NAME = "Q021_HiddenTruth";
	
	// NPCs
	private static final int MYSTERIOUS_WIZARD = 31522;
	private static final int TOMBSTONE = 31523;
	private static final int VON_HELLMAN_DUKE = 31524;
	private static final int VON_HELLMAN_PAGE = 31525;
	private static final int BROKEN_BOOKSHELF = 31526;
	private static final int AGRIPEL = 31348;
	private static final int DOMINIC = 31350;
	private static final int BENEDICT = 31349;
	private static final int INNOCENTIN = 31328;
	
	// Items
	private static final int CROSS_OF_EINHASAD = 7140;
	private static final int CROSS_OF_EINHASAD_NEXT_QUEST = 7141;
	
	// Sounds
	private static final String SOUND_HORROR = "SkillSound5.horror_02";
	private static final String SOUND_ITEM_DROP = "ItemSound.item_drop_equip_armor_cloth";
	
	private Npc _duke;
	private Npc _page;
	
	public Q021_HiddenTruth()
	{
		super(21, "Hidden Truth");
		
		setItemsIds(CROSS_OF_EINHASAD);
		
		addQuestStart(MYSTERIOUS_WIZARD);
		addTalkId(MYSTERIOUS_WIZARD, TOMBSTONE, VON_HELLMAN_DUKE, VON_HELLMAN_PAGE, BROKEN_BOOKSHELF, AGRIPEL, DOMINIC, BENEDICT, INNOCENTIN);
		
		addDecayed(VON_HELLMAN_DUKE, VON_HELLMAN_PAGE);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31522-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("31523-03.htm"))
		{
			if (_duke == null)
			{
				if (st.getCond() == 1)
					st.setCond(2);
				
				_duke = addSpawn(VON_HELLMAN_DUKE, 51432, -54570, -3136, 0, false, 300000, true);
				_duke.broadcastNpcSay(NpcStringId.ID_2150);
			}
			else
				htmltext = "31523-04.htm";
			
			playSound(player, SOUND_HORROR);
		}
		else if (event.equalsIgnoreCase("31524-06.htm"))
		{
			if (spawnThePage(player))
			{
				st.setCond(3);
				playSound(player, SOUND_MIDDLE);
			}
			else
				htmltext = "31524-06a.htm";
		}
		else if (event.equalsIgnoreCase("31526-03.htm"))
		{
			playSound(player, SOUND_ITEM_DROP);
		}
		else if (event.equalsIgnoreCase("31526-07.htm"))
		{
			st.set("truth", 1);
		}
		else if (event.equalsIgnoreCase("31526-08.htm"))
		{
			st.setCond(5);
			st.unset("end_walk");
			st.unset("truth");
			
			if (_page != null)
				_page.deleteMe();
			
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("31526-14.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CROSS_OF_EINHASAD, 1);
		}
		else if (event.equalsIgnoreCase("31328-05.htm"))
		{
			if (player.getInventory().hasItem(CROSS_OF_EINHASAD))
			{
				takeItems(player, CROSS_OF_EINHASAD, 1);
				giveItems(player, CROSS_OF_EINHASAD_NEXT_QUEST, 1);
				playSound(player, SOUND_FINISH);
				st.exitQuest(false);
			}
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2102"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_2151, player.getName());
			
			startQuestTimer("2103", npc, player, 9000);
		}
		else if (name.equalsIgnoreCase("2103"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_2152, player.getName());
			
			QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
			if (st != null)
				st.set("end_walk", 1);
		}
		
		return null;
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
				htmltext = (player.getStatus().getLevel() < 63) ? "31522-03.htm" : "31522-01.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MYSTERIOUS_WIZARD:
						htmltext = "31522-05.htm";
						break;
					
					case TOMBSTONE:
						if (cond == 1 || cond == 2 || cond == 3)
							htmltext = "31523-01.htm";
						break;
					
					case VON_HELLMAN_DUKE:
						if (cond == 2)
							htmltext = "31524-01.htm";
						else if (cond == 3)
						{
							if (st.getInteger("end_walk") == 0)
							{
								if (spawnThePage(player))
								{
									st.set("end_walk", 0);
									htmltext = "31524-07.htm";
								}
								else
									htmltext = "31524-07a.htm";
							}
							else
							{
								htmltext = "31524-07b.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
						}
						else if (cond == 4)
							htmltext = "31524-07b.htm";
						else if (cond > 4)
							htmltext = "31524-07c.htm";
						break;
					
					case VON_HELLMAN_PAGE:
						if (cond == 3)
						{
							if (st.getInteger("end_walk") == 1)
							{
								htmltext = "31525-02.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
							}
							else
								htmltext = "31525-01.htm";
						}
						else if (cond == 4)
							htmltext = "31525-02.htm";
						break;
					
					case BROKEN_BOOKSHELF:
						if ((cond == 3 && st.getInteger("end_walk") == 1))
							htmltext = "31526-01.htm";
						else if (cond == 4)
						{
							if (st.getInteger("truth") == 0)
								htmltext = "31526-01.htm";
							else
							{
								htmltext = "31526-10.htm";
								
								st.setCond(5);
								st.unset("end_walk");
								st.unset("truth");
								
								if (_page != null)
									_page.deleteMe();
								
								playSound(player, SOUND_MIDDLE);
							}
						}
						else if (cond == 5)
							htmltext = "31526-11.htm";
						else if (cond == 6)
							htmltext = "31526-15.htm";
						break;
					
					case AGRIPEL, BENEDICT, DOMINIC:
						if ((cond == 6 || cond == 7) && player.getInventory().hasItem(CROSS_OF_EINHASAD))
						{
							int npcId = npc.getNpcId();
							
							// For cond 6, make checks until cond 7 is activated.
							if (cond == 6)
							{
								int npcId1 = 0;
								int npcId2 = 0;
								
								if (npcId == AGRIPEL)
								{
									npcId1 = BENEDICT;
									npcId2 = DOMINIC;
								}
								else if (npcId == BENEDICT)
								{
									npcId1 = AGRIPEL;
									npcId2 = DOMINIC;
								}
								else if (npcId == DOMINIC)
								{
									npcId1 = AGRIPEL;
									npcId2 = BENEDICT;
								}
								
								if (st.getInteger(String.valueOf(npcId1)) == 1 && st.getInteger(String.valueOf(npcId2)) == 1)
								{
									st.setCond(7);
									playSound(player, SOUND_MIDDLE);
								}
								else
									st.set(String.valueOf(npcId), 1);
							}
							
							htmltext = npcId + "-01.htm";
						}
						break;
					
					case INNOCENTIN:
						if (cond == 7 && player.getInventory().hasItem(CROSS_OF_EINHASAD))
							htmltext = "31328-01.htm";
						break;
				}
				break;
			
			case COMPLETED:
				if (npc.getNpcId() == INNOCENTIN)
					htmltext = "31328-06.htm";
				else
					htmltext = getAlreadyCompletedMsg();
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onDecayed(Npc npc)
	{
		if (npc == _duke)
		{
			_duke = null;
		}
		else if (npc == _page)
		{
			cancelQuestTimers(_page);
			_page = null;
		}
	}
	
	private boolean spawnThePage(Player player)
	{
		if (_page == null)
		{
			_page = addSpawn(VON_HELLMAN_PAGE, 51608, -54520, -3168, 0, false, 90000, true);
			
			startQuestTimer("2102", _page, player, 500);
			return true;
		}
		
		return false;
	}
}