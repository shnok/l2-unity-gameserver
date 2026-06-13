package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q242_PossessorOfAPreciousSoul extends Quest
{
	private static final String QUEST_NAME = "Q242_PossessorOfAPreciousSoul";
	
	// NPCs
	private static final int VIRGIL = 31742;
	private static final int KASSANDRA = 31743;
	private static final int OGMAR = 31744;
	private static final int MYSTERIOUS_KNIGHT = 31751;
	private static final int ANGEL_CORPSE = 31752;
	private static final int KALIS = 30759;
	private static final int MATILD = 30738;
	private static final int CORNERSTONE = 31748;
	private static final int FALLEN_UNICORN = 31746;
	private static final int PURE_UNICORN = 31747;
	
	// Monsters
	private static final int RESTRAINER_OF_GLORY = 27317;
	
	// Items
	private static final int VIRGIL_LETTER = 7677;
	private static final int GOLDEN_HAIR = 7590;
	private static final int SORCERY_INGREDIENT = 7596;
	private static final int ORB_OF_BINDING = 7595;
	private static final int CARADINE_LETTER = 7678;
	
	private Npc _pureUnicorn;
	
	public Q242_PossessorOfAPreciousSoul()
	{
		super(242, "Possessor of a Precious Soul - 2");
		
		setItemsIds(GOLDEN_HAIR, SORCERY_INGREDIENT, ORB_OF_BINDING);
		
		addQuestStart(VIRGIL);
		addTalkId(VIRGIL, KASSANDRA, OGMAR, MYSTERIOUS_KNIGHT, ANGEL_CORPSE, KALIS, MATILD, CORNERSTONE, FALLEN_UNICORN, PURE_UNICORN);
		
		addMyDying(RESTRAINER_OF_GLORY);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Kasandra
		if (event.equalsIgnoreCase("31743-05.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
		}
		// Ogmar
		else if (event.equalsIgnoreCase("31744-02.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		// Mysterious Knight
		else if (event.equalsIgnoreCase("31751-02.htm"))
		{
			st.setCond(4);
			playSound(player, SOUND_MIDDLE);
		}
		// Kalis
		else if (event.equalsIgnoreCase("30759-02.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30759-05.htm"))
		{
			if (player.getInventory().hasItem(SORCERY_INGREDIENT))
			{
				st.setCond(9);
				playSound(player, SOUND_MIDDLE);
				takeItems(player, GOLDEN_HAIR, 1);
				takeItems(player, SORCERY_INGREDIENT, 1);
			}
			else
			{
				st.setCond(7);
				htmltext = "30759-02.htm";
			}
		}
		// Matild
		else if (event.equalsIgnoreCase("30738-02.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, SORCERY_INGREDIENT, 1);
		}
		// Cornerstone
		else if (event.equalsIgnoreCase("31748-03.htm"))
		{
			if (player.getInventory().hasItem(ORB_OF_BINDING))
			{
				npc.deleteMe();
				takeItems(player, ORB_OF_BINDING, 1);
				
				final int cornerstones = st.getInteger("cornerstone") + 1;
				if (cornerstones == 4)
				{
					st.unset("orb");
					st.unset("cornerstone");
					st.setCond(10);
					playSound(player, SOUND_MIDDLE);
				}
				else
					st.set("cornerstone", cornerstones);
			}
			else
				htmltext = null;
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("fallen"))
		{
			if (_pureUnicorn == null)
			{
				npc.deleteMe();
				
				// Spawn Pure Unicorn (spawn for 30 seconds)
				_pureUnicorn = addSpawn(PURE_UNICORN, npc, false, 30000, true);
			}
		}
		else if (name.equalsIgnoreCase("pure"))
		{
			if (_pureUnicorn != null)
			{
				// Despawn Pure Unicorn
				_pureUnicorn.deleteMe();
				_pureUnicorn = null;
			}
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
				if (player.getInventory().hasItem(VIRGIL_LETTER))
				{
					if (!player.isSubClassActive() || player.getStatus().getLevel() < 60)
						htmltext = "31742-02.htm";
					else
					{
						htmltext = "31742-03.htm";
						st.setState(QuestStatus.STARTED);
						st.setCond(1);
						playSound(player, SOUND_ACCEPT);
						takeItems(player, VIRGIL_LETTER, 1);
					}
				}
				break;
			
			case STARTED:
				if (!player.isSubClassActive())
					break;
				
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case VIRGIL:
						if (cond == 1)
							htmltext = "31742-04.htm";
						else if (cond == 2)
							htmltext = "31742-05.htm";
						break;
					
					case KASSANDRA:
						if (cond == 1)
							htmltext = "31743-01.htm";
						else if (cond == 2)
							htmltext = "31743-06.htm";
						else if (cond == 11)
						{
							htmltext = "31743-07.htm";
							giveItems(player, CARADINE_LETTER, 1);
							rewardExpAndSp(player, 455764, 0);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case OGMAR:
						if (cond == 2)
							htmltext = "31744-01.htm";
						else if (cond == 3)
							htmltext = "31744-03.htm";
						break;
					
					case MYSTERIOUS_KNIGHT:
						if (cond == 3)
							htmltext = "31751-01.htm";
						else if (cond == 4)
							htmltext = "31751-03.htm";
						else if (cond == 5)
						{
							if (player.getInventory().hasItem(GOLDEN_HAIR))
							{
								htmltext = "31751-04.htm";
								st.setCond(6);
								playSound(player, SOUND_MIDDLE);
							}
							else
							{
								htmltext = "31751-03.htm";
								st.setCond(4);
							}
						}
						else if (cond == 6)
							htmltext = "31751-05.htm";
						break;
					
					case ANGEL_CORPSE:
						if (cond == 4)
						{
							npc.deleteMe();
							
							final int angel = st.getInteger("angel") + 1;
							if (angel == 4)
							{
								htmltext = "31752-02.htm";
								st.unset("angel");
								st.setCond(5);
								playSound(player, SOUND_MIDDLE);
								giveItems(player, GOLDEN_HAIR, 1);
							}
							else
							{
								htmltext = "31752-01.htm";
								st.set("angel", angel);
							}
						}
						else if (cond == 5)
							htmltext = "31752-01.htm";
						break;
					
					case KALIS:
						if (cond == 6)
							htmltext = "30759-01.htm";
						else if (cond == 7)
							htmltext = "30759-03.htm";
						else if (cond == 8)
						{
							if (player.getInventory().hasItem(SORCERY_INGREDIENT))
								htmltext = "30759-04.htm";
							else
							{
								htmltext = "30759-03.htm";
								st.setCond(7);
							}
						}
						else if (cond == 9)
							htmltext = "30759-06.htm";
						break;
					
					case MATILD:
						if (cond == 7)
							htmltext = "30738-01.htm";
						else if (cond == 8)
							htmltext = "30738-03.htm";
						break;
					
					case CORNERSTONE:
						if (cond == 9)
						{
							if (player.getInventory().hasItem(ORB_OF_BINDING))
								htmltext = "31748-02.htm";
							else
								htmltext = "31748-01.htm";
						}
						break;
					
					case FALLEN_UNICORN:
						if (cond == 9)
							htmltext = "31746-01.htm";
						else if (cond == 10)
						{
							htmltext = "31746-02.htm";
							startQuestTimer("fallen", npc, null, 3000);
						}
						break;
					
					case PURE_UNICORN:
						if (cond == 10)
						{
							htmltext = "31747-01.htm";
							st.setCond(11);
							playSound(player, SOUND_MIDDLE);
							startQuestTimer("pure", npc, null, 3000);
						}
						else if (cond == 11)
							htmltext = "31747-02.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 9);
		if (st == null || !player.isSubClassActive())
			return;
		
		// Check orbs internally, because player can use them before he gets them all.
		final int orbs = st.getInteger("orb");
		if (orbs < 4)
		{
			st.set("orb", orbs + 1);
			playSound(player, SOUND_ITEMGET);
			giveItems(player, ORB_OF_BINDING, 1);
		}
	}
}