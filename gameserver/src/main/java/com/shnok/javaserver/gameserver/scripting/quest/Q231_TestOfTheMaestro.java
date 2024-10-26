package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q231_TestOfTheMaestro extends SecondClassQuest
{
	private static final String QUEST_NAME = "Q231_TestOfTheMaestro";
	
	private static final int RECOMMENDATION_OF_BALANKI = 2864;
	private static final int RECOMMENDATION_OF_FILAUR = 2865;
	private static final int RECOMMENDATION_OF_ARIN = 2866;
	private static final int LETTER_OF_SOLDER_DETACHMENT = 2868;
	private static final int PAINT_OF_KAMURU = 2869;
	private static final int NECKLACE_OF_KAMURU = 2870;
	private static final int PAINT_OF_TELEPORT_DEVICE = 2871;
	private static final int TELEPORT_DEVICE = 2872;
	private static final int ARCHITECTURE_OF_KRUMA = 2873;
	private static final int REPORT_OF_KRUMA = 2874;
	private static final int INGREDIENTS_OF_ANTIDOTE = 2875;
	private static final int STINGER_WASP_NEEDLE = 2876;
	private static final int MARSH_SPIDER_WEB = 2877;
	private static final int BLOOD_OF_LEECH = 2878;
	private static final int BROKEN_TELEPORT_DEVICE = 2916;
	
	// Rewards
	private static final int MARK_OF_MAESTRO = 2867;
	
	// NPCs
	private static final int LOCKIRIN = 30531;
	private static final int SPIRON = 30532;
	private static final int BALANKI = 30533;
	private static final int KEEF = 30534;
	private static final int FILAUR = 30535;
	private static final int ARIN = 30536;
	private static final int TOMA = 30556;
	private static final int CROTO = 30671;
	private static final int DUBABAH = 30672;
	private static final int LORAIN = 30673;
	
	// Monsters
	private static final int KING_BUGBEAR = 20150;
	private static final int GIANT_MIST_LEECH = 20225;
	private static final int STINGER_WASP = 20229;
	private static final int MARSH_SPIDER = 20233;
	private static final int EVIL_EYE_LORD = 27133;
	
	public Q231_TestOfTheMaestro()
	{
		super(231, "Test Of The Maestro");
		
		setItemsIds(RECOMMENDATION_OF_BALANKI, RECOMMENDATION_OF_FILAUR, RECOMMENDATION_OF_ARIN, LETTER_OF_SOLDER_DETACHMENT, PAINT_OF_KAMURU, NECKLACE_OF_KAMURU, PAINT_OF_TELEPORT_DEVICE, TELEPORT_DEVICE, ARCHITECTURE_OF_KRUMA, REPORT_OF_KRUMA, INGREDIENTS_OF_ANTIDOTE, STINGER_WASP_NEEDLE, MARSH_SPIDER_WEB, BLOOD_OF_LEECH, BROKEN_TELEPORT_DEVICE);
		
		addQuestStart(LOCKIRIN);
		addTalkId(LOCKIRIN, SPIRON, BALANKI, KEEF, FILAUR, ARIN, TOMA, CROTO, DUBABAH, LORAIN);
		
		addMyDying(GIANT_MIST_LEECH, STINGER_WASP, MARSH_SPIDER, EVIL_EYE_LORD);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// LOCKIRIN
		if (event.equalsIgnoreCase("30531-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
			
			if (giveDimensionalDiamonds39(player))
				htmltext = "30531-04a.htm";
		}
		// BALANKI
		else if (event.equalsIgnoreCase("30533-02.htm"))
		{
			st.set("bCond", 1);
		}
		// CROTO
		else if (event.equalsIgnoreCase("30671-02.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, PAINT_OF_KAMURU, 1);
		}
		// TOMA
		else if (event.equalsIgnoreCase("30556-05.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, PAINT_OF_TELEPORT_DEVICE, 1);
			giveItems(player, BROKEN_TELEPORT_DEVICE, 1);
			player.teleportTo(140352, -194133, -3146, 0);
			startQuestTimer("spawn_bugbears", null, player, 5000);
		}
		// LORAIN
		else if (event.equalsIgnoreCase("30673-04.htm"))
		{
			st.set("fCond", 2);
			playSound(player, SOUND_ITEMGET);
			takeItems(player, BLOOD_OF_LEECH, -1);
			takeItems(player, INGREDIENTS_OF_ANTIDOTE, 1);
			takeItems(player, MARSH_SPIDER_WEB, -1);
			takeItems(player, STINGER_WASP_NEEDLE, -1);
			giveItems(player, REPORT_OF_KRUMA, 1);
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		// Spawns 3 King Bugbears
		if (name.equalsIgnoreCase("spawn_bugbears"))
		{
			Npc bugbear = addSpawn(KING_BUGBEAR, 140333, -194153, -3138, 0, false, 200000, true);
			bugbear.getAI().addAttackDesire(player, 2000);
			
			bugbear = addSpawn(KING_BUGBEAR, 140395, -194147, -3146, 0, false, 200000, true);
			bugbear.getAI().addAttackDesire(player, 2000);
			
			bugbear = addSpawn(KING_BUGBEAR, 140304, -194082, -3157, 0, false, 200000, true);
			bugbear.getAI().addAttackDesire(player, 2000);
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
				if (player.getClassId() != ClassId.ARTISAN)
					htmltext = "30531-01.htm";
				else if (player.getStatus().getLevel() < 39)
					htmltext = "30531-02.htm";
				else
					htmltext = "30531-03.htm";
				break;
			
			case STARTED:
				switch (npc.getNpcId())
				{
					case LOCKIRIN:
						int cond = st.getCond();
						if (cond == 1)
							htmltext = "30531-05.htm";
						else if (cond == 2)
						{
							htmltext = "30531-06.htm";
							takeItems(player, RECOMMENDATION_OF_ARIN, 1);
							takeItems(player, RECOMMENDATION_OF_BALANKI, 1);
							takeItems(player, RECOMMENDATION_OF_FILAUR, 1);
							giveItems(player, MARK_OF_MAESTRO, 1);
							rewardExpAndSp(player, 46000, 5900);
							player.broadcastPacket(new SocialAction(player, 3));
							playSound(player, SOUND_FINISH);
							st.exitQuest(false);
						}
						break;
					
					case SPIRON:
						htmltext = "30532-01.htm";
						break;
					
					case KEEF:
						htmltext = "30534-01.htm";
						break;
					
					// Part 1
					case BALANKI:
						int bCond = st.getInteger("bCond");
						if (bCond == 0)
							htmltext = "30533-01.htm";
						else if (bCond == 1)
							htmltext = "30533-03.htm";
						else if (bCond == 2)
						{
							htmltext = "30533-04.htm";
							st.set("bCond", 3);
							takeItems(player, LETTER_OF_SOLDER_DETACHMENT, 1);
							giveItems(player, RECOMMENDATION_OF_BALANKI, 1);
							
							if (player.getInventory().hasItems(RECOMMENDATION_OF_ARIN, RECOMMENDATION_OF_FILAUR))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (bCond == 3)
							htmltext = "30533-05.htm";
						break;
					
					case CROTO:
						bCond = st.getInteger("bCond");
						if (bCond == 1)
						{
							if (!player.getInventory().hasItem(PAINT_OF_KAMURU))
								htmltext = "30671-01.htm";
							else if (!player.getInventory().hasItem(NECKLACE_OF_KAMURU))
								htmltext = "30671-03.htm";
							else
							{
								htmltext = "30671-04.htm";
								st.set("bCond", 2);
								playSound(player, SOUND_ITEMGET);
								takeItems(player, NECKLACE_OF_KAMURU, 1);
								takeItems(player, PAINT_OF_KAMURU, 1);
								giveItems(player, LETTER_OF_SOLDER_DETACHMENT, 1);
							}
						}
						else if (bCond > 1)
							htmltext = "30671-05.htm";
						break;
					
					case DUBABAH:
						htmltext = "30672-01.htm";
						break;
					
					// Part 2
					case ARIN:
						int aCond = st.getInteger("aCond");
						if (aCond == 0)
						{
							htmltext = "30536-01.htm";
							st.set("aCond", 1);
							giveItems(player, PAINT_OF_TELEPORT_DEVICE, 1);
						}
						else if (aCond == 1)
							htmltext = "30536-02.htm";
						else if (aCond == 2)
						{
							htmltext = "30536-03.htm";
							st.set("aCond", 3);
							takeItems(player, TELEPORT_DEVICE, -1);
							giveItems(player, RECOMMENDATION_OF_ARIN, 1);
							
							if (player.getInventory().hasItems(RECOMMENDATION_OF_BALANKI, RECOMMENDATION_OF_FILAUR))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (aCond == 3)
							htmltext = "30536-04.htm";
						break;
					
					case TOMA:
						aCond = st.getInteger("aCond");
						if (aCond == 1)
						{
							if (!player.getInventory().hasItem(BROKEN_TELEPORT_DEVICE))
								htmltext = "30556-01.htm";
							else if (!player.getInventory().hasItem(TELEPORT_DEVICE))
							{
								htmltext = "30556-06.htm";
								st.set("aCond", 2);
								playSound(player, SOUND_ITEMGET);
								takeItems(player, BROKEN_TELEPORT_DEVICE, 1);
								giveItems(player, TELEPORT_DEVICE, 5);
							}
						}
						else if (aCond > 1)
							htmltext = "30556-07.htm";
						break;
					
					// Part 3
					case FILAUR:
						int fCond = st.getInteger("fCond");
						if (fCond == 0)
						{
							htmltext = "30535-01.htm";
							st.set("fCond", 1);
							playSound(player, SOUND_ITEMGET);
							giveItems(player, ARCHITECTURE_OF_KRUMA, 1);
						}
						else if (fCond == 1)
							htmltext = "30535-02.htm";
						else if (fCond == 2)
						{
							htmltext = "30535-03.htm";
							st.set("fCond", 3);
							takeItems(player, REPORT_OF_KRUMA, 1);
							giveItems(player, RECOMMENDATION_OF_FILAUR, 1);
							
							if (player.getInventory().hasItems(RECOMMENDATION_OF_BALANKI, RECOMMENDATION_OF_ARIN))
							{
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
							}
							else
								playSound(player, SOUND_ITEMGET);
						}
						else if (fCond == 3)
							htmltext = "30535-04.htm";
						break;
					
					case LORAIN:
						fCond = st.getInteger("fCond");
						if (fCond == 1)
						{
							if (!player.getInventory().hasItem(REPORT_OF_KRUMA))
							{
								if (!player.getInventory().hasItem(INGREDIENTS_OF_ANTIDOTE))
								{
									htmltext = "30673-01.htm";
									playSound(player, SOUND_ITEMGET);
									takeItems(player, ARCHITECTURE_OF_KRUMA, 1);
									giveItems(player, INGREDIENTS_OF_ANTIDOTE, 1);
								}
								else if (player.getInventory().getItemCount(STINGER_WASP_NEEDLE) < 10 || player.getInventory().getItemCount(MARSH_SPIDER_WEB) < 10 || player.getInventory().getItemCount(BLOOD_OF_LEECH) < 10)
									htmltext = "30673-02.htm";
								else
									htmltext = "30673-03.htm";
							}
						}
						else if (fCond > 1)
							htmltext = "30673-05.htm";
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
		
		final QuestState st = checkPlayerCondition(player, npc, 1);
		if (st == null)
			return;
		
		switch (npc.getNpcId())
		{
			case GIANT_MIST_LEECH:
				if (player.getInventory().hasItem(INGREDIENTS_OF_ANTIDOTE))
					dropItemsAlways(player, BLOOD_OF_LEECH, 1, 10);
				break;
			
			case STINGER_WASP:
				if (player.getInventory().hasItem(INGREDIENTS_OF_ANTIDOTE))
					dropItemsAlways(player, STINGER_WASP_NEEDLE, 1, 10);
				break;
			
			case MARSH_SPIDER:
				if (player.getInventory().hasItem(INGREDIENTS_OF_ANTIDOTE))
					dropItemsAlways(player, MARSH_SPIDER_WEB, 1, 10);
				break;
			
			case EVIL_EYE_LORD:
				if (player.getInventory().hasItem(PAINT_OF_KAMURU))
					dropItemsAlways(player, NECKLACE_OF_KAMURU, 1, 1);
				break;
		}
	}
}