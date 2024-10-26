package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q401_PathToAWarrior extends Quest
{
	private static final String QUEST_NAME = "Q401_PathToAWarrior";
	
	// Items
	private static final int AURON_LETTER = 1138;
	private static final int WARRIOR_GUILD_MARK = 1139;
	private static final int RUSTED_BRONZE_SWORD_1 = 1140;
	private static final int RUSTED_BRONZE_SWORD_2 = 1141;
	private static final int RUSTED_BRONZE_SWORD_3 = 1142;
	private static final int SIMPLON_LETTER = 1143;
	private static final int POISON_SPIDER_LEG = 1144;
	private static final int MEDALLION_OF_WARRIOR = 1145;
	
	// NPCs
	private static final int AURON = 30010;
	private static final int SIMPLON = 30253;
	
	public Q401_PathToAWarrior()
	{
		super(401, "Path to a Warrior");
		
		setItemsIds(AURON_LETTER, WARRIOR_GUILD_MARK, RUSTED_BRONZE_SWORD_1, RUSTED_BRONZE_SWORD_2, RUSTED_BRONZE_SWORD_3, SIMPLON_LETTER, POISON_SPIDER_LEG);
		
		addQuestStart(AURON);
		addTalkId(AURON, SIMPLON);
		
		addMyDying(20035, 20038, 20042, 20043);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30010-05.htm"))
		{
			if (player.getClassId() != ClassId.HUMAN_FIGHTER)
				htmltext = (player.getClassId() == ClassId.WARRIOR) ? "30010-03.htm" : "30010-02b.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30010-02.htm";
			else if (player.getInventory().hasItem(MEDALLION_OF_WARRIOR))
				htmltext = "30010-04.htm";
		}
		else if (event.equalsIgnoreCase("30010-06.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, AURON_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30253-02.htm"))
		{
			st.setCond(2);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, AURON_LETTER, 1);
			giveItems(player, WARRIOR_GUILD_MARK, 1);
		}
		else if (event.equalsIgnoreCase("30010-11.htm"))
		{
			st.setCond(5);
			playSound(player, SOUND_MIDDLE);
			takeItems(player, RUSTED_BRONZE_SWORD_2, 1);
			takeItems(player, SIMPLON_LETTER, 1);
			giveItems(player, RUSTED_BRONZE_SWORD_3, 1);
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
				htmltext = "30010-01.htm";
				break;
			
			case STARTED:
				final int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case AURON:
						if (cond == 1)
							htmltext = "30010-07.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "30010-08.htm";
						else if (cond == 4)
							htmltext = "30010-09.htm";
						else if (cond == 5)
							htmltext = "30010-12.htm";
						else if (cond == 6)
						{
							htmltext = "30010-13.htm";
							takeItems(player, POISON_SPIDER_LEG, -1);
							takeItems(player, RUSTED_BRONZE_SWORD_3, 1);
							giveItems(player, MEDALLION_OF_WARRIOR, 1);
							rewardExpAndSp(player, 3200, 1500);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						break;
					
					case SIMPLON:
						if (cond == 1)
							htmltext = "30253-01.htm";
						else if (cond == 2)
						{
							if (!player.getInventory().hasItem(RUSTED_BRONZE_SWORD_1))
								htmltext = "30253-03.htm";
							else if (player.getInventory().getItemCount(RUSTED_BRONZE_SWORD_1) <= 9)
								htmltext = "30253-03b.htm";
						}
						else if (cond == 3)
						{
							htmltext = "30253-04.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, RUSTED_BRONZE_SWORD_1, 10);
							takeItems(player, WARRIOR_GUILD_MARK, 1);
							giveItems(player, RUSTED_BRONZE_SWORD_2, 1);
							giveItems(player, SIMPLON_LETTER, 1);
						}
						else if (cond == 4)
							htmltext = "30253-05.htm";
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
			case 20035, 20042:
				if (st.getCond() == 2 && dropItems(player, RUSTED_BRONZE_SWORD_1, 1, 10, 400000))
					st.setCond(3);
				break;
			
			case 20038, 20043:
				if (st.getCond() == 5 && player.getInventory().getItemIdFrom(Paperdoll.RHAND) == RUSTED_BRONZE_SWORD_3)
					if (dropItemsAlways(player, POISON_SPIDER_LEG, 1, 20))
						st.setCond(6);
				break;
		}
	}
}