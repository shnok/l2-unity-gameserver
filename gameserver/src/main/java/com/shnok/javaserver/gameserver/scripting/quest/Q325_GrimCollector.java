package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q325_GrimCollector extends Quest
{
	private static final String QUEST_NAME = "Q325_GrimCollector";
	
	// Items
	private static final int ANATOMY_DIAGRAM = 1349;
	private static final int ZOMBIE_HEAD = 1350;
	private static final int ZOMBIE_HEART = 1351;
	private static final int ZOMBIE_LIVER = 1352;
	private static final int SKULL = 1353;
	private static final int RIB_BONE = 1354;
	private static final int SPINE = 1355;
	private static final int ARM_BONE = 1356;
	private static final int THIGH_BONE = 1357;
	private static final int COMPLETE_SKELETON = 1358;
	
	// NPCs
	private static final int CURTIS = 30336;
	private static final int VARSAK = 30342;
	private static final int SAMED = 30434;
	
	private static final Map<Integer, List<IntIntHolder>> DROPLIST = new HashMap<>();
	{
		DROPLIST.put(20026, Arrays.asList(new IntIntHolder(ZOMBIE_HEAD, 30), new IntIntHolder(ZOMBIE_HEART, 50), new IntIntHolder(ZOMBIE_LIVER, 75)));
		DROPLIST.put(20029, Arrays.asList(new IntIntHolder(ZOMBIE_HEAD, 30), new IntIntHolder(ZOMBIE_HEART, 52), new IntIntHolder(ZOMBIE_LIVER, 75)));
		DROPLIST.put(20035, Arrays.asList(new IntIntHolder(SKULL, 5), new IntIntHolder(RIB_BONE, 15), new IntIntHolder(SPINE, 29), new IntIntHolder(THIGH_BONE, 79)));
		DROPLIST.put(20042, Arrays.asList(new IntIntHolder(SKULL, 6), new IntIntHolder(RIB_BONE, 19), new IntIntHolder(ARM_BONE, 69), new IntIntHolder(THIGH_BONE, 86)));
		DROPLIST.put(20045, Arrays.asList(new IntIntHolder(SKULL, 9), new IntIntHolder(SPINE, 59), new IntIntHolder(ARM_BONE, 77), new IntIntHolder(THIGH_BONE, 97)));
		DROPLIST.put(20051, Arrays.asList(new IntIntHolder(SKULL, 9), new IntIntHolder(RIB_BONE, 59), new IntIntHolder(SPINE, 79), new IntIntHolder(ARM_BONE, 100)));
		DROPLIST.put(20457, Arrays.asList(new IntIntHolder(ZOMBIE_HEAD, 40), new IntIntHolder(ZOMBIE_HEART, 60), new IntIntHolder(ZOMBIE_LIVER, 80)));
		DROPLIST.put(20458, Arrays.asList(new IntIntHolder(ZOMBIE_HEAD, 40), new IntIntHolder(ZOMBIE_HEART, 70), new IntIntHolder(ZOMBIE_LIVER, 100)));
		DROPLIST.put(20514, Arrays.asList(new IntIntHolder(SKULL, 6), new IntIntHolder(RIB_BONE, 21), new IntIntHolder(SPINE, 30), new IntIntHolder(ARM_BONE, 31), new IntIntHolder(THIGH_BONE, 64)));
		DROPLIST.put(20515, Arrays.asList(new IntIntHolder(SKULL, 5), new IntIntHolder(RIB_BONE, 20), new IntIntHolder(SPINE, 31), new IntIntHolder(ARM_BONE, 33), new IntIntHolder(THIGH_BONE, 69)));
	}
	
	public Q325_GrimCollector()
	{
		super(325, "Grim Collector");
		
		setItemsIds(ZOMBIE_HEAD, ZOMBIE_HEART, ZOMBIE_LIVER, SKULL, RIB_BONE, SPINE, ARM_BONE, THIGH_BONE, COMPLETE_SKELETON, ANATOMY_DIAGRAM);
		
		addQuestStart(CURTIS);
		addTalkId(CURTIS, VARSAK, SAMED);
		
		for (int npcId : DROPLIST.keySet())
			addMyDying(npcId);
	}
	
	private static int getNumberOfPieces(Player player)
	{
		return player.getInventory().getItemCount(ZOMBIE_HEAD) + player.getInventory().getItemCount(SPINE) + player.getInventory().getItemCount(ARM_BONE) + player.getInventory().getItemCount(ZOMBIE_HEART) + player.getInventory().getItemCount(ZOMBIE_LIVER) + player.getInventory().getItemCount(SKULL) + player.getInventory().getItemCount(RIB_BONE) + player.getInventory().getItemCount(THIGH_BONE) + player.getInventory().getItemCount(COMPLETE_SKELETON);
	}
	
	private static void payback(Player player)
	{
		final int count = getNumberOfPieces(player);
		if (count > 0)
		{
			int reward = 30 * player.getInventory().getItemCount(ZOMBIE_HEAD) + 20 * player.getInventory().getItemCount(ZOMBIE_HEART) + 20 * player.getInventory().getItemCount(ZOMBIE_LIVER) + 100 * player.getInventory().getItemCount(SKULL) + 40 * player.getInventory().getItemCount(RIB_BONE) + 14 * player.getInventory().getItemCount(SPINE) + 14 * player.getInventory().getItemCount(ARM_BONE) + 14 * player.getInventory().getItemCount(THIGH_BONE) + 341 * player.getInventory().getItemCount(COMPLETE_SKELETON);
			if (count > 10)
				reward += 1629;
			
			if (player.getInventory().hasItem(COMPLETE_SKELETON))
				reward += 543;
			
			takeItems(player, ZOMBIE_HEAD, -1);
			takeItems(player, ZOMBIE_HEART, -1);
			takeItems(player, ZOMBIE_LIVER, -1);
			takeItems(player, SKULL, -1);
			takeItems(player, RIB_BONE, -1);
			takeItems(player, SPINE, -1);
			takeItems(player, ARM_BONE, -1);
			takeItems(player, THIGH_BONE, -1);
			takeItems(player, COMPLETE_SKELETON, -1);
			
			rewardItems(player, 57, reward);
		}
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30336-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30434-03.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, ANATOMY_DIAGRAM, 1);
		}
		else if (event.equalsIgnoreCase("30434-06.htm"))
		{
			takeItems(player, ANATOMY_DIAGRAM, -1);
			payback(player);
			playSound(player, SOUND_FINISH);
			st.exitQuest(true);
		}
		else if (event.equalsIgnoreCase("30434-07.htm"))
		{
			payback(player);
		}
		else if (event.equalsIgnoreCase("30434-09.htm"))
		{
			final int skeletons = player.getInventory().getItemCount(COMPLETE_SKELETON);
			if (skeletons > 0)
			{
				playSound(player, SOUND_MIDDLE);
				takeItems(player, COMPLETE_SKELETON, -1);
				rewardItems(player, 57, 543 + 341 * skeletons);
			}
		}
		else if (event.equalsIgnoreCase("30342-03.htm"))
		{
			if (!player.getInventory().hasItems(SPINE, ARM_BONE, SKULL, RIB_BONE, THIGH_BONE))
				htmltext = "30342-02.htm";
			else
			{
				takeItems(player, SPINE, 1);
				takeItems(player, SKULL, 1);
				takeItems(player, ARM_BONE, 1);
				takeItems(player, RIB_BONE, 1);
				takeItems(player, THIGH_BONE, 1);
				
				if (Rnd.get(10) < 9)
					giveItems(player, COMPLETE_SKELETON, 1);
				else
					htmltext = "30342-04.htm";
			}
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
				htmltext = (player.getStatus().getLevel() < 15) ? "30336-01.htm" : "30336-02.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case CURTIS:
						htmltext = (!player.getInventory().hasItem(ANATOMY_DIAGRAM)) ? "30336-04.htm" : "30336-05.htm";
						break;
					
					case SAMED:
						if (!player.getInventory().hasItem(ANATOMY_DIAGRAM))
							htmltext = "30434-01.htm";
						else
						{
							if (getNumberOfPieces(player) == 0)
								htmltext = "30434-04.htm";
							else
								htmltext = (!player.getInventory().hasItem(COMPLETE_SKELETON)) ? "30434-05.htm" : "30434-08.htm";
						}
						break;
					
					case VARSAK:
						htmltext = "30342-01.htm";
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
		
		if (player.getInventory().hasItem(ANATOMY_DIAGRAM))
		{
			final int chance = Rnd.get(100);
			for (IntIntHolder drop : DROPLIST.get(npc.getNpcId()))
			{
				if (chance < drop.getValue())
				{
					dropItemsAlways(player, drop.getId(), 1, 0);
					break;
				}
			}
		}
	}
}