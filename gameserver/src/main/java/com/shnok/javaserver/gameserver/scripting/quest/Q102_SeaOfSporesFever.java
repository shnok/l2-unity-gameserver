package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q102_SeaOfSporesFever extends Quest
{
	private static final String QUEST_NAME = "Q102_SeaOfSporesFever";
	
	// Items
	private static final int ALBERIUS_LETTER = 964;
	private static final int EVERGREEN_AMULET = 965;
	private static final int DRYAD_TEARS = 966;
	private static final int ALBERIUS_LIST = 746;
	private static final int COBENDELL_MEDICINE_1 = 1130;
	private static final int COBENDELL_MEDICINE_2 = 1131;
	private static final int COBENDELL_MEDICINE_3 = 1132;
	private static final int COBENDELL_MEDICINE_4 = 1133;
	private static final int COBENDELL_MEDICINE_5 = 1134;
	
	// Rewards
	private static final int SWORD_OF_SENTINEL = 743;
	private static final int STAFF_OF_SENTINEL = 744;
	private static final int LESSER_HEALING_POT = 1060;
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int SPIRITSHOT_NO_GRADE = 2509;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	// NPCs
	private static final int ALBERIUS = 30284;
	private static final int COBENDELL = 30156;
	private static final int BERROS = 30217;
	private static final int VELTRESS = 30219;
	private static final int RAYEN = 30221;
	private static final int GARTRANDELL = 30285;
	
	public Q102_SeaOfSporesFever()
	{
		super(102, "Sea of Spores Fever");
		
		setItemsIds(ALBERIUS_LETTER, EVERGREEN_AMULET, DRYAD_TEARS, COBENDELL_MEDICINE_1, COBENDELL_MEDICINE_2, COBENDELL_MEDICINE_3, COBENDELL_MEDICINE_4, COBENDELL_MEDICINE_5, ALBERIUS_LIST);
		
		addQuestStart(ALBERIUS);
		addTalkId(ALBERIUS, COBENDELL, BERROS, RAYEN, GARTRANDELL, VELTRESS);
		
		addMyDying(20013, 20019);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30284-02.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, ALBERIUS_LETTER, 1);
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
				if (player.getRace() != ClassRace.ELF)
					htmltext = "30284-00.htm";
				else if (player.getStatus().getLevel() < 12)
					htmltext = "30284-08.htm";
				else
					htmltext = "30284-07.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case ALBERIUS:
						if (cond == 1)
							htmltext = "30284-03.htm";
						else if (cond == 2 || cond == 3)
							htmltext = "30284-09.htm";
						else if (cond == 4)
						{
							htmltext = "30284-04.htm";
							st.setCond(5);
							st.set("medicines", 4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, COBENDELL_MEDICINE_1, 1);
							giveItems(player, ALBERIUS_LIST, 1);
						}
						else if (cond == 5)
							htmltext = "30284-05.htm";
						else if (cond == 6)
						{
							htmltext = "30284-06.htm";
							takeItems(player, ALBERIUS_LIST, 1);
							
							if (player.isMageClass())
							{
								giveItems(player, STAFF_OF_SENTINEL, 1);
								rewardItems(player, SPIRITSHOT_NO_GRADE, 500);
							}
							else
							{
								giveItems(player, SWORD_OF_SENTINEL, 1);
								rewardItems(player, SOULSHOT_NO_GRADE, 1000);
							}
							
							rewardItems(player, LESSER_HEALING_POT, 100);
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
					
					case COBENDELL:
						if (cond == 1)
						{
							htmltext = "30156-03.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ALBERIUS_LETTER, 1);
							giveItems(player, EVERGREEN_AMULET, 1);
						}
						else if (cond == 2)
							htmltext = "30156-04.htm";
						else if (cond == 5)
							htmltext = "30156-07.htm";
						else if (cond == 3)
						{
							htmltext = "30156-05.htm";
							st.setCond(4);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, DRYAD_TEARS, -1);
							takeItems(player, EVERGREEN_AMULET, 1);
							giveItems(player, COBENDELL_MEDICINE_1, 1);
							giveItems(player, COBENDELL_MEDICINE_2, 1);
							giveItems(player, COBENDELL_MEDICINE_3, 1);
							giveItems(player, COBENDELL_MEDICINE_4, 1);
							giveItems(player, COBENDELL_MEDICINE_5, 1);
						}
						else if (cond == 4)
							htmltext = "30156-06.htm";
						break;
					
					case BERROS:
						if (cond == 5)
						{
							htmltext = "30217-01.htm";
							checkItem(player, st, COBENDELL_MEDICINE_2);
						}
						break;
					
					case VELTRESS:
						if (cond == 5)
						{
							htmltext = "30219-01.htm";
							checkItem(player, st, COBENDELL_MEDICINE_3);
						}
						break;
					
					case RAYEN:
						if (cond == 5)
						{
							htmltext = "30221-01.htm";
							checkItem(player, st, COBENDELL_MEDICINE_4);
						}
						break;
					
					case GARTRANDELL:
						if (cond == 5)
						{
							htmltext = "30285-01.htm";
							checkItem(player, st, COBENDELL_MEDICINE_5);
						}
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
		
		if (dropItems(player, DRYAD_TEARS, 1, 10, 300000))
			st.setCond(3);
	}
	
	private static void checkItem(Player player, QuestState st, int itemId)
	{
		if (player.getInventory().hasItem(itemId))
		{
			takeItems(player, itemId, 1);
			
			int medicinesLeft = st.getInteger("medicines") - 1;
			if (medicinesLeft == 0)
			{
				st.setCond(6);
				playSound(player, SOUND_MIDDLE);
			}
			else
				st.set("medicines", medicinesLeft);
		}
	}
}