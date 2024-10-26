package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.QuestStatus;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public class Q412_PathToADarkWizard extends Quest
{
	private static final String QUEST_NAME = "Q412_PathToADarkWizard";
	
	// Items
	private static final int SEED_OF_ANGER = 1253;
	private static final int SEED_OF_DESPAIR = 1254;
	private static final int SEED_OF_HORROR = 1255;
	private static final int SEED_OF_LUNACY = 1256;
	private static final int FAMILY_REMAINS = 1257;
	private static final int VARIKA_LIQUOR = 1258;
	private static final int KNEE_BONE = 1259;
	private static final int HEART_OF_LUNACY = 1260;
	private static final int JEWEL_OF_DARKNESS = 1261;
	private static final int LUCKY_KEY = 1277;
	private static final int CANDLE = 1278;
	private static final int HUB_SCENT = 1279;
	
	// NPCs
	private static final int VARIKA = 30421;
	private static final int CHARKEREN = 30415;
	private static final int ANNIKA = 30418;
	private static final int ARKENIA = 30419;
	
	public Q412_PathToADarkWizard()
	{
		super(412, "Path to a Dark Wizard");
		
		setItemsIds(SEED_OF_ANGER, SEED_OF_DESPAIR, SEED_OF_HORROR, SEED_OF_LUNACY, FAMILY_REMAINS, VARIKA_LIQUOR, KNEE_BONE, HEART_OF_LUNACY, LUCKY_KEY, CANDLE, HUB_SCENT);
		
		addQuestStart(VARIKA);
		addTalkId(VARIKA, CHARKEREN, ANNIKA, ARKENIA);
		
		addMyDying(20015, 20022, 20045, 20517, 20518);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("30421-05.htm"))
		{
			if (player.getClassId() != ClassId.DARK_MYSTIC)
				htmltext = (player.getClassId() == ClassId.DARK_WIZARD) ? "30421-02a.htm" : "30421-03.htm";
			else if (player.getStatus().getLevel() < 19)
				htmltext = "30421-02.htm";
			else if (player.getInventory().hasItem(JEWEL_OF_DARKNESS))
				htmltext = "30421-04.htm";
			else
			{
				st.setState(QuestStatus.STARTED);
				st.setCond(1);
				playSound(player, SOUND_ACCEPT);
				giveItems(player, SEED_OF_DESPAIR, 1);
			}
		}
		else if (event.equalsIgnoreCase("30421-07.htm"))
		{
			if (player.getInventory().hasItem(SEED_OF_ANGER))
				htmltext = "30421-06.htm";
			else if (player.getInventory().hasItem(LUCKY_KEY))
				htmltext = "30421-08.htm";
			else if (player.getInventory().getItemCount(FAMILY_REMAINS) == 3)
				htmltext = "30421-18.htm";
		}
		else if (event.equalsIgnoreCase("30421-10.htm"))
		{
			if (player.getInventory().hasItem(SEED_OF_HORROR))
				htmltext = "30421-09.htm";
			else if (player.getInventory().getItemCount(KNEE_BONE) == 2)
				htmltext = "30421-19.htm";
		}
		else if (event.equalsIgnoreCase("30421-13.htm"))
		{
			if (player.getInventory().hasItem(SEED_OF_LUNACY))
				htmltext = "30421-12.htm";
		}
		else if (event.equalsIgnoreCase("30415-03.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			giveItems(player, LUCKY_KEY, 1);
		}
		else if (event.equalsIgnoreCase("30418-02.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			giveItems(player, CANDLE, 1);
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
				htmltext = "30421-01.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case VARIKA:
						if (player.getInventory().hasItems(SEED_OF_ANGER, SEED_OF_HORROR, SEED_OF_LUNACY))
						{
							htmltext = "30421-16.htm";
							takeItems(player, SEED_OF_ANGER, 1);
							takeItems(player, SEED_OF_DESPAIR, 1);
							takeItems(player, SEED_OF_HORROR, 1);
							takeItems(player, SEED_OF_LUNACY, 1);
							giveItems(player, JEWEL_OF_DARKNESS, 1);
							rewardExpAndSp(player, 3200, 1650);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(true);
						}
						else
							htmltext = "30421-17.htm";
						break;
					
					case CHARKEREN:
						if (player.getInventory().hasItem(SEED_OF_ANGER))
							htmltext = "30415-06.htm";
						else if (!player.getInventory().hasItem(LUCKY_KEY))
							htmltext = "30415-01.htm";
						else if (player.getInventory().getItemCount(FAMILY_REMAINS) == 3)
						{
							htmltext = "30415-05.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, FAMILY_REMAINS, -1);
							takeItems(player, LUCKY_KEY, 1);
							giveItems(player, SEED_OF_ANGER, 1);
						}
						else
							htmltext = "30415-04.htm";
						break;
					
					case ANNIKA:
						if (player.getInventory().hasItem(SEED_OF_HORROR))
							htmltext = "30418-04.htm";
						else if (!player.getInventory().hasItem(CANDLE))
							htmltext = "30418-01.htm";
						else if (player.getInventory().getItemCount(KNEE_BONE) == 2)
						{
							htmltext = "30418-04.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, CANDLE, 1);
							takeItems(player, KNEE_BONE, -1);
							giveItems(player, SEED_OF_HORROR, 1);
						}
						else
							htmltext = "30418-03.htm";
						break;
					
					case ARKENIA:
						if (player.getInventory().hasItem(SEED_OF_LUNACY))
							htmltext = "30419-03.htm";
						else if (!player.getInventory().hasItem(HUB_SCENT))
						{
							htmltext = "30419-01.htm";
							playSound(player, SOUND_MIDDLE);
							giveItems(player, HUB_SCENT, 1);
						}
						else if (player.getInventory().getItemCount(HEART_OF_LUNACY) == 3)
						{
							htmltext = "30419-03.htm";
							playSound(player, SOUND_MIDDLE);
							takeItems(player, HEART_OF_LUNACY, -1);
							takeItems(player, HUB_SCENT, 1);
							giveItems(player, SEED_OF_LUNACY, 1);
						}
						else
							htmltext = "30419-02.htm";
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
			case 20015:
				if (player.getInventory().hasItem(LUCKY_KEY))
					dropItems(player, FAMILY_REMAINS, 1, 3, 500000);
				break;
			
			case 20022, 20517, 20518:
				if (player.getInventory().hasItem(CANDLE))
					dropItems(player, KNEE_BONE, 1, 2, 500000);
				break;
			
			case 20045:
				if (player.getInventory().hasItem(HUB_SCENT))
					dropItems(player, HEART_OF_LUNACY, 1, 3, 500000);
				break;
		}
	}
}