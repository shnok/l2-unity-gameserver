package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q106_ForgottenTruth extends Quest
{
	private static final String QUEST_NAME = "Q106_ForgottenTruth";
	
	// NPCs
	private static final int THIFIELL = 30358;
	private static final int KARTIA = 30133;
	
	// Items
	private static final int ONYX_TALISMAN_1 = 984;
	private static final int ONYX_TALISMAN_2 = 985;
	private static final int ANCIENT_SCROLL = 986;
	private static final int ANCIENT_CLAY_TABLET = 987;
	private static final int KARTIA_TRANSLATION = 988;
	
	// Rewards
	private static final int ELDRITCH_DAGGER = 989;
	private static final int LESSER_HEALING_POTION = 1060;
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int SPIRITSHOT_NO_GRADE = 2509;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	public Q106_ForgottenTruth()
	{
		super(106, "Forgotten Truth");
		
		setItemsIds(ONYX_TALISMAN_1, ONYX_TALISMAN_2, ANCIENT_SCROLL, ANCIENT_CLAY_TABLET, KARTIA_TRANSLATION);
		
		addQuestStart(THIFIELL);
		addTalkId(THIFIELL, KARTIA);
		
		addMyDying(27070); // Tumran Orc Brigand
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		String htmltext = event;
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30358-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ONYX_TALISMAN_1, 1);
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30358-00.htm";
				else if (player.getStatus().getLevel() < 10)
					htmltext = "30358-02.htm";
				else
					htmltext = "30358-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case THIFIELL:
						if (cond == 1)
							htmltext = "30358-06.htm";
						else if (cond == 2)
							htmltext = "30358-06.htm";
						else if (cond == 3)
							htmltext = "30358-06.htm";
						else if (cond == 4)
						{
							htmltext = "30358-07.htm";
							takeItems(player, KARTIA_TRANSLATION, 1);
							giveItems(player, ELDRITCH_DAGGER, 1);
							
							if (player.isMageClass())
								rewardItems(player, SPIRITSHOT_NO_GRADE, 500);
							else
								rewardItems(player, SOULSHOT_NO_GRADE, 1000);
							
							rewardNewbieShots(player, 0, 3000);
							rewardItems(player, LESSER_HEALING_POTION, 100);
							rewardItems(player, ECHO_BATTLE, 10);
							rewardItems(player, ECHO_LOVE, 10);
							rewardItems(player, ECHO_SOLITUDE, 10);
							rewardItems(player, ECHO_FEAST, 10);
							rewardItems(player, ECHO_CELEBRATION, 10);
							
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case KARTIA:
						if (cond == 1)
						{
							htmltext = "30133-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ONYX_TALISMAN_1, 1);
							giveItems(player, ONYX_TALISMAN_2, 1);
						}
						else if (cond == 2)
							htmltext = "30133-02.htm";
						else if (cond == 3)
						{
							htmltext = "30133-03.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ONYX_TALISMAN_2, 1);
							takeItems(player, ANCIENT_SCROLL, 1);
							takeItems(player, ANCIENT_CLAY_TABLET, 1);
							giveItems(player, KARTIA_TRANSLATION, 1);
						}
						else if (cond == 4)
							htmltext = "30133-04.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 2);
		if (st == null)
			return;
		
		if (!player.getInventory().hasItem(ANCIENT_SCROLL))
			dropItems(player, ANCIENT_SCROLL, 1, 1, 200000);
		else if (dropItems(player, ANCIENT_CLAY_TABLET, 1, 1, 200000))
			st.setCond(3);
	}
}