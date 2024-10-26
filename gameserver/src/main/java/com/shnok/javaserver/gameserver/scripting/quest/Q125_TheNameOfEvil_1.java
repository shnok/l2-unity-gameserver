package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q125_TheNameOfEvil_1 extends Quest
{
	public static final String QUEST_NAME = "Q125_TheNameOfEvil_1";
	
	private static final int MUSHIKA = 32114;
	private static final int KARAKAWEI = 32117;
	private static final int ULU_KAIMU = 32119;
	private static final int BALU_KAIMU = 32120;
	private static final int CHUTA_KAIMU = 32121;
	
	private static final int ORNITHOMIMUS_CLAW = 8779;
	private static final int DEINONYCHUS_BONE = 8780;
	private static final int EPITAPH_OF_WISDOM = 8781;
	private static final int GAZKH_FRAGMENT = 8782;
	
	private static final int[] ORNITHOMIMUS =
	{
		22200,
		22201,
		22202,
		22219,
		22224,
		22742,
		22744
	};
	
	private static final int[] DEINONYCHUS =
	{
		16067,
		22203,
		22204,
		22205,
		22220,
		22225,
		22743,
		22745
	};
	
	public Q125_TheNameOfEvil_1()
	{
		super(125, "The Name of Evil - 1");
		
		setItemsIds(ORNITHOMIMUS_CLAW, DEINONYCHUS_BONE, EPITAPH_OF_WISDOM, GAZKH_FRAGMENT);
		
		addQuestStart(MUSHIKA);
		addTalkId(MUSHIKA, KARAKAWEI, ULU_KAIMU, BALU_KAIMU, CHUTA_KAIMU);
		
		for (int i : ORNITHOMIMUS)
			addMyDying(i);
		
		for (int i : DEINONYCHUS)
			addMyDying(i);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("32114-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("32114-09.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, GAZKH_FRAGMENT, 1);
		}
		else if (event.equalsIgnoreCase("32117-08.htm"))
		{
			st.setCond(3);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32117-14.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32119-14.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32120-15.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("32121-16.htm"))
		{
			st.setCond(8);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, GAZKH_FRAGMENT, -1);
			giveItems(player, EPITAPH_OF_WISDOM, 1);
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
				QuestState first = player.getQuestList().getQuestState(Q124_MeetingTheElroki.QUEST_NAME);
				if (first != null && first.isCompleted() && player.getStatus().getLevel() >= 76)
					htmltext = "32114-01.htm";
				else
					htmltext = "32114-00.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case MUSHIKA:
						if (cond == 1)
							htmltext = "32114-07.htm";
						else if (cond == 2)
							htmltext = "32114-10.htm";
						else if (cond > 2 && cond < 8)
							htmltext = "32114-11.htm";
						else if (cond == 8)
						{
							htmltext = "32114-12.htm";
							takeItems(player, EPITAPH_OF_WISDOM, -1);
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case KARAKAWEI:
						if (cond == 2)
							htmltext = "32117-01.htm";
						else if (cond == 3)
							htmltext = "32117-09.htm";
						else if (cond == 4)
						{
							if (player.getInventory().getItemCount(ORNITHOMIMUS_CLAW) >= 2 && player.getInventory().getItemCount(DEINONYCHUS_BONE) >= 2)
							{
								htmltext = "32117-10.htm";
								takeItems(player, ORNITHOMIMUS_CLAW, -1);
								takeItems(player, DEINONYCHUS_BONE, -1);
								playSound(player, SOUND_MIDDLE);
							}
							else
							{
								htmltext = "32117-09.htm";
								st.setCond(3);
							}
						}
						else if (cond == 5)
							htmltext = "32117-15.htm";
						else if (cond == 6 || cond == 7)
							htmltext = "32117-16.htm";
						else if (cond == 8)
							htmltext = "32117-17.htm";
						break;
					
					case ULU_KAIMU:
						if (cond == 5)
						{
							npc.getAI().addCastDesire(npc, 5089, 1, 1000000);
							htmltext = "32119-01.htm";
						}
						else if (cond == 6)
							htmltext = "32119-14.htm";
						break;
					
					case BALU_KAIMU:
						if (cond == 6)
						{
							npc.getAI().addCastDesire(npc, 5089, 1, 1000000);
							htmltext = "32120-01.htm";
						}
						else if (cond == 7)
							htmltext = "32120-16.htm";
						break;
					
					case CHUTA_KAIMU:
						if (cond == 7)
						{
							npc.getAI().addCastDesire(npc, 5089, 1, 1000000);
							htmltext = "32121-01.htm";
						}
						else if (cond == 8)
							htmltext = "32121-17.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 3);
		if (st == null)
			return;
		
		final int npcId = npc.getNpcId();
		if (ArraysUtil.contains(ORNITHOMIMUS, npcId))
		{
			if (dropItems(player, ORNITHOMIMUS_CLAW, 1, 2, 50000) && player.getInventory().getItemCount(DEINONYCHUS_BONE) == 2)
				st.setCond(4);
		}
		else if (ArraysUtil.contains(DEINONYCHUS, npcId))
		{
			if (dropItems(player, DEINONYCHUS_BONE, 1, 2, 50000) && player.getInventory().getItemCount(ORNITHOMIMUS_CLAW) == 2)
				st.setCond(4);
		}
	}
}