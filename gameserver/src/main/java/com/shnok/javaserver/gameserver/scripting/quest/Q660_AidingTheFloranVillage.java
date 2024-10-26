package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q660_AidingTheFloranVillage extends Quest
{
	private static final String QUEST_NAME = "Q660_AidingTheFloranVillage";
	
	// NPCs
	private static final int MARIA = 30608;
	private static final int ALEX = 30291;
	
	// Items
	private static final int WATCHING_EYES = 8074;
	private static final int GOLEM_SHARD = 8075;
	private static final int LIZARDMEN_SCALE = 8076;
	
	// Mobs
	private static final int PLAIN_WATCHMAN = 21102;
	private static final int ROCK_GOLEM = 21103;
	private static final int LIZARDMEN_SUPPLIER = 21104;
	private static final int LIZARDMEN_AGENT = 21105;
	private static final int CURSED_SEER = 21106;
	private static final int LIZARDMEN_COMMANDER = 21107;
	private static final int LIZARDMEN_SHAMAN = 20781;
	
	// Rewards
	private static final int ADENA = 57;
	private static final int ENCHANT_WEAPON_D = 955;
	private static final int ENCHANT_ARMOR_D = 956;
	
	public Q660_AidingTheFloranVillage()
	{
		super(660, "Aiding the Floran Village");
		
		setItemsIds(WATCHING_EYES, LIZARDMEN_SCALE, GOLEM_SHARD);
		
		addQuestStart(MARIA, ALEX);
		addTalkId(MARIA, ALEX);
		
		addMyDying(CURSED_SEER, PLAIN_WATCHMAN, ROCK_GOLEM, LIZARDMEN_SHAMAN, LIZARDMEN_SUPPLIER, LIZARDMEN_COMMANDER, LIZARDMEN_AGENT);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30608-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30291-02.htm"))
		{
			if (player.getStatus().getLevel() < 30)
				htmltext = "30291-02a.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(2);
				playSound(player, SOUND_ACCEPT);
			}
		}
		else if (event.equalsIgnoreCase("30291-05.htm"))
		{
			final int count = player.getInventory().getItemCount(WATCHING_EYES) + player.getInventory().getItemCount(LIZARDMEN_SCALE) + player.getInventory().getItemCount(GOLEM_SHARD);
			if (count == 0)
				htmltext = "30291-05a.htm";
			else
			{
				takeItems(player, GOLEM_SHARD, -1);
				takeItems(player, LIZARDMEN_SCALE, -1);
				takeItems(player, WATCHING_EYES, -1);
				rewardItems(player, ADENA, count * 100 + ((count >= 45) ? 9000 : 0));
			}
		}
		else if (event.equalsIgnoreCase("30291-06.htm"))
		{
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30291-11.htm"))
		{
			if (!verifyAndRemoveItems(player, 100))
				htmltext = "30291-11a.htm";
			else
			{
				if (Rnd.get(10) < 8)
					rewardItems(player, ADENA, 1000);
				else
				{
					rewardItems(player, ADENA, 13000);
					rewardItems(player, ENCHANT_ARMOR_D, 1);
				}
			}
		}
		else if (event.equalsIgnoreCase("30291-12.htm"))
		{
			if (!verifyAndRemoveItems(player, 200))
				htmltext = "30291-12a.htm";
			else
			{
				final int luck = Rnd.get(15);
				if (luck < 8)
					rewardItems(player, ADENA, 2000);
				else if (luck < 12)
				{
					rewardItems(player, ADENA, 20000);
					rewardItems(player, ENCHANT_ARMOR_D, 1);
				}
				else
					rewardItems(player, ENCHANT_WEAPON_D, 1);
			}
		}
		else if (event.equalsIgnoreCase("30291-13.htm"))
		{
			if (!verifyAndRemoveItems(player, 500))
				htmltext = "30291-13a.htm";
			else
			{
				if (Rnd.get(10) < 8)
					rewardItems(player, ADENA, 5000);
				else
				{
					rewardItems(player, ADENA, 45000);
					rewardItems(player, ENCHANT_WEAPON_D, 1);
				}
			}
		}
		else if (event.equalsIgnoreCase("30291-17.htm"))
		{
			final int count = player.getInventory().getItemCount(WATCHING_EYES) + player.getInventory().getItemCount(LIZARDMEN_SCALE) + player.getInventory().getItemCount(GOLEM_SHARD);
			if (count != 0)
			{
				htmltext = "30291-17a.htm";
				takeItems(player, WATCHING_EYES, -1);
				takeItems(player, LIZARDMEN_SCALE, -1);
				takeItems(player, GOLEM_SHARD, -1);
				rewardItems(player, ADENA, count * 100 + ((count >= 45) ? 9000 : 0));
			}
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
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
				switch (npc.getNpcId())
				{
					case MARIA:
						htmltext = (player.getStatus().getLevel() < 30) ? "30608-01.htm" : "30608-02.htm";
						break;
					
					case ALEX:
						htmltext = "30291-01.htm";
						break;
				}
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case MARIA:
						htmltext = "30608-06.htm";
						break;
					
					case ALEX:
						final int cond = st.getCond();
						if (cond == 1)
						{
							htmltext = "30291-03.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
						}
						else if (cond == 2)
							htmltext = (player.getInventory().hasAtLeastOneItem(WATCHING_EYES, LIZARDMEN_SCALE, GOLEM_SHARD)) ? "30291-04.htm" : "30291-05a.htm";
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
		
		final QuestState st = getRandomPartyMember(player, npc, 2);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case PLAIN_WATCHMAN, CURSED_SEER:
				dropItems(st.getPlayer(), WATCHING_EYES, 1, 0, 790000);
				break;
			
			case ROCK_GOLEM:
				dropItems(st.getPlayer(), GOLEM_SHARD, 1, 0, 750000);
				break;
			
			case LIZARDMEN_SHAMAN, LIZARDMEN_SUPPLIER, LIZARDMEN_AGENT, LIZARDMEN_COMMANDER:
				dropItems(st.getPlayer(), LIZARDMEN_SCALE, 1, 0, 670000);
				break;
		}
	}
	
	/**
	 * This method drops items following current counts.
	 * @param player : The player to check.
	 * @param numberToVerify : The count of qItems to drop from the different categories.
	 * @return false when counter isn't reached, true otherwise.
	 */
	private static boolean verifyAndRemoveItems(Player player, int numberToVerify)
	{
		final int eyes = player.getInventory().getItemCount(WATCHING_EYES);
		final int scale = player.getInventory().getItemCount(LIZARDMEN_SCALE);
		final int shard = player.getInventory().getItemCount(GOLEM_SHARD);
		
		if (eyes + scale + shard < numberToVerify)
			return false;
		
		if (eyes >= numberToVerify)
			takeItems(player, WATCHING_EYES, numberToVerify);
		else
		{
			int currentNumber = numberToVerify - eyes;
			
			takeItems(player, WATCHING_EYES, -1);
			if (scale >= currentNumber)
				takeItems(player, LIZARDMEN_SCALE, currentNumber);
			else
			{
				currentNumber -= scale;
				takeItems(player, LIZARDMEN_SCALE, -1);
				takeItems(player, GOLEM_SHARD, currentNumber);
			}
		}
		return true;
	}
}