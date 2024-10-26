package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassRace;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q104_SpiritOfMirrors extends Quest
{
	private static final String QUEST_NAME = "Q104_SpiritOfMirrors";
	
	// Items
	private static final int GALLINS_OAK_WAND = 748;
	private static final int WAND_SPIRITBOUND_1 = 1135;
	private static final int WAND_SPIRITBOUND_2 = 1136;
	private static final int WAND_SPIRITBOUND_3 = 1137;
	
	// Rewards
	private static final int WAND_OF_ADEPT = 747;
	private static final int LESSER_HEALING_POT = 1060;
	private static final int SOULSHOT_NO_GRADE = 1835;
	private static final int SPIRITSHOT_NO_GRADE = 2509;
	private static final int ECHO_BATTLE = 4412;
	private static final int ECHO_LOVE = 4413;
	private static final int ECHO_SOLITUDE = 4414;
	private static final int ECHO_FEAST = 4415;
	private static final int ECHO_CELEBRATION = 4416;
	
	// NPCs
	private static final int GALLINT = 30017;
	private static final int ARNOLD = 30041;
	private static final int JOHNSTONE = 30043;
	private static final int KENYOS = 30045;
	
	public Q104_SpiritOfMirrors()
	{
		super(104, "Spirit of Mirrors");
		
		setItemsIds(GALLINS_OAK_WAND, WAND_SPIRITBOUND_1, WAND_SPIRITBOUND_2, WAND_SPIRITBOUND_3);
		
		addQuestStart(GALLINT);
		addTalkId(GALLINT, ARNOLD, JOHNSTONE, KENYOS);
		
		addMyDying(27003, 27004, 27005);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30017-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			giveItems(player, GALLINS_OAK_WAND, 1);
			giveItems(player, GALLINS_OAK_WAND, 1);
			giveItems(player, GALLINS_OAK_WAND, 1);
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
				if (player.getRace() != ClassRace.HUMAN)
					htmltext = "30017-00.htm";
				else if (player.getStatus().getLevel() < 10)
					htmltext = "30017-01.htm";
				else
					htmltext = "30017-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case GALLINT:
						if (cond == 1 || cond == 2)
							htmltext = "30017-04.htm";
						else if (cond == 3)
						{
							htmltext = "30017-05.htm";
							
							takeItems(player, WAND_SPIRITBOUND_1, -1);
							takeItems(player, WAND_SPIRITBOUND_2, -1);
							takeItems(player, WAND_SPIRITBOUND_3, -1);
							
							giveItems(player, WAND_OF_ADEPT, 1);
							
							if (player.isMageClass())
								rewardItems(player, SPIRITSHOT_NO_GRADE, 500);
							else
								rewardItems(player, SOULSHOT_NO_GRADE, 1000);
							
							rewardNewbieShots(player, 0, 3000);
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
					
					case KENYOS, JOHNSTONE, ARNOLD:
						htmltext = npc.getNpcId() + "-01.htm";
						if (cond == 1)
						{
							st.setCond(2);
							playSound(player, SOUND_MIDDLE);
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
		
		final QuestState st = checkPlayerState(player, npc, QuestStatus.STARTED);
		if (st == null)
			return;
		
		if (player.getInventory().getItemIdFrom(Paperdoll.RHAND) == GALLINS_OAK_WAND)
		{
			switch (npc.getNpcId())
			{
				case 27003:
					if (!player.getInventory().hasItem(WAND_SPIRITBOUND_1))
					{
						takeItems(player, GALLINS_OAK_WAND, 1);
						giveItems(player, WAND_SPIRITBOUND_1, 1);
						
						if (player.getInventory().hasItems(WAND_SPIRITBOUND_2, WAND_SPIRITBOUND_3))
						{
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
						}
						else
							playSound(player, SOUND_ITEMGET);
					}
					break;
				
				case 27004:
					if (!player.getInventory().hasItem(WAND_SPIRITBOUND_2))
					{
						takeItems(player, GALLINS_OAK_WAND, 1);
						giveItems(player, WAND_SPIRITBOUND_2, 1);
						
						if (player.getInventory().hasItems(WAND_SPIRITBOUND_1, WAND_SPIRITBOUND_3))
						{
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
						}
						else
							playSound(player, SOUND_ITEMGET);
					}
					break;
				
				case 27005:
					if (!player.getInventory().hasItem(WAND_SPIRITBOUND_3))
					{
						takeItems(player, GALLINS_OAK_WAND, 1);
						giveItems(player, WAND_SPIRITBOUND_3, 1);
						
						if (player.getInventory().hasItems(WAND_SPIRITBOUND_1, WAND_SPIRITBOUND_2))
						{
							st.setCond(3);
							playSound(player, SOUND_MIDDLE);
						}
						else
							playSound(player, SOUND_ITEMGET);
					}
					break;
			}
		}
	}
}