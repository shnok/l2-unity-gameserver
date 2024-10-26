package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q103_SpiritOfCraftsman extends Quest
{
	private static final String QUEST_NAME = "Q103_SpiritOfCraftsman";
	
	// Items
	private static final int KARROD_LETTER = 968;
	private static final int CECKTINON_VOUCHER_1 = 969;
	private static final int CECKTINON_VOUCHER_2 = 970;
	private static final int SOUL_CATCHER = 971;
	private static final int PRESERVING_OIL = 972;
	private static final int ZOMBIE_HEAD = 973;
	private static final int STEELBENDER_HEAD = 974;
	private static final int BONE_FRAGMENT = 1107;
	
	// Rewards
	private static final int BLOOD_SABER = 975;
	private static final int LESSER_HEALING_POT = 1060;
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int SPIRITSHOT_NO_GRADE = 2509;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	// NPCs
	private static final int KARROD = 30307;
	private static final int CECKTINON = 30132;
	private static final int HARNE = 30144;
	
	public Q103_SpiritOfCraftsman()
	{
		super(103, "Spirit of Craftsman");
		
		setItemsIds(KARROD_LETTER, CECKTINON_VOUCHER_1, CECKTINON_VOUCHER_2, BONE_FRAGMENT, SOUL_CATCHER, PRESERVING_OIL, ZOMBIE_HEAD, STEELBENDER_HEAD);
		
		addQuestStart(KARROD);
		addTalkId(KARROD, CECKTINON, HARNE);
		
		addMyDying(20015, 20020, 20455, 20517, 20518);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30307-05.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, KARROD_LETTER, 1);
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
				if (player.getRace() != ClassRace.DARK_ELF)
					htmltext = "30307-00.htm";
				else if (player.getStatus().getLevel() < 11)
					htmltext = "30307-02.htm";
				else
					htmltext = "30307-03.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case KARROD:
						if (cond < 8)
							htmltext = "30307-06.htm";
						else if (cond == 8)
						{
							htmltext = "30307-07.htm";
							
							takeItems(player, STEELBENDER_HEAD, 1);
							giveItems(player, BLOOD_SABER, 1);
							
							if (player.isMageClass())
								rewardItems(player, SPIRITSHOT_NO_GRADE, 500);
							else
								rewardItems(player, SOULSHOT_NO_GRADE, 1000);
							
							rewardNewbieShots(player, 7000, 0);
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
					
					case CECKTINON:
						if (cond == 1)
						{
							htmltext = "30132-01.htm";
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, KARROD_LETTER, 1);
							giveItems(player, CECKTINON_VOUCHER_1, 1);
						}
						else if (cond > 1 && cond < 5)
							htmltext = "30132-02.htm";
						else if (cond == 5)
						{
							htmltext = "30132-03.htm";
							st.setCond(6);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, SOUL_CATCHER, 1);
							giveItems(player, PRESERVING_OIL, 1);
						}
						else if (cond == 6)
							htmltext = "30132-04.htm";
						else if (cond == 7)
						{
							htmltext = "30132-05.htm";
							st.setCond(8);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, ZOMBIE_HEAD, 1);
							giveItems(player, STEELBENDER_HEAD, 1);
						}
						else if (cond == 8)
							htmltext = "30132-06.htm";
						break;
					
					case HARNE:
						if (cond == 2)
						{
							htmltext = "30144-01.htm";
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CECKTINON_VOUCHER_1, 1);
							giveItems(player, CECKTINON_VOUCHER_2, 1);
						}
						else if (cond == 3)
							htmltext = "30144-02.htm";
						else if (cond == 4)
						{
							htmltext = "30144-03.htm";
							st.setCond(5);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CECKTINON_VOUCHER_2, 1);
							takeItems(player, BONE_FRAGMENT, 10);
							giveItems(player, SOUL_CATCHER, 1);
						}
						else if (cond == 5)
							htmltext = "30144-04.htm";
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case 20517, 20518, 20455:
				if (st.getCond() == 3 && dropItems(player, BONE_FRAGMENT, 1, 10, 300000))
					st.setCond(4);
				break;
			
			case 20015, 20020:
				if (st.getCond() == 6 && dropItems(player, ZOMBIE_HEAD, 1, 1, 300000))
				{
					st.setCond(7);
					takeItems(player, PRESERVING_OIL, 1);
				}
				break;
		}
	}
}