package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q503_PursuitOfClanAmbition extends Quest
{
	private static final String QUEST_NAME = "Q503_PursuitOfClanAmbition";
	
	// NPCs
	private static final int KUSTO = 30512;
	private static final int MARTIEN = 30645;
	private static final int WITCH_ATHREA = 30758;
	private static final int WITCH_KALIS = 30759;
	private static final int SIR_GUSTAV_ATHEBALDT = 30760;
	private static final int CORPSE_OF_FRITZ = 30761;
	private static final int CORPSE_OF_LUTZ = 30762;
	private static final int CORPSE_OF_KURTZ = 30763;
	private static final int BALTHAZAR = 30764;
	private static final int IMPERIAL_COFFER = 30765;
	private static final int WITCH_CLEO = 30766;
	private static final int SIR_ERIC_RODEMAI = 30868;
	
	// Monsters
	private static final int THUNDER_WYRM = 20243;
	private static final int THUNDER_WYRM_HOLD = 20282;
	private static final int BLITZ_WYRM = 27178;
	private static final int DRAKE = 20137;
	private static final int DRAKE_HOLD = 20285;
	private static final int GRAVE_GUARD = 20668;
	private static final int GRAVE_KEYMASTER = 27179;
	private static final int LESSER_GIANT_SOLDIER = 20654;
	private static final int LESSER_GIANT_SCOUT = 20656;
	private static final int IMPERIAL_GRAVEKEEPER = 27181;
	
	// Quest Items
	private static final int RECIPE_TITAN_POWERSTONE = 3838;
	private static final int MIST_DRAKE_EGG = 3839;
	private static final int BLITZ_WYRM_EGG = 3840;
	private static final int DRAKE_EGG = 3841;
	private static final int THUNDER_WYRM_EGG = 3842;
	private static final int BROOCH_OF_THE_MAGPIE = 3843;
	private static final int NEBULITE_CRYSTALS = 3844;
	private static final int BROKEN_TITAN_POWERSTONE = 3845;
	private static final int TITAN_POWERSTONE = 3846;
	private static final int IMPERIAL_KEY = 3847;
	private static final int GUSTAV_1ST_LETTER = 3866;
	private static final int GUSTAV_2ND_LETTER = 3867;
	private static final int GUSTAV_3RD_LETTER = 3868;
	private static final int SCEPTER_OF_JUDGMENT = 3869;
	private static final int BLACK_ANVIL_COIN = 3871;
	
	// Reward
	private static final int SEAL_OF_ASPIRATION = 3870;
	
	public Q503_PursuitOfClanAmbition()
	{
		super(503, "Pursuit Of Clan Ambition");
		
		setItemsIds(MIST_DRAKE_EGG, BLITZ_WYRM_EGG, DRAKE_EGG, THUNDER_WYRM_EGG, BROOCH_OF_THE_MAGPIE, NEBULITE_CRYSTALS, TITAN_POWERSTONE, IMPERIAL_KEY, GUSTAV_1ST_LETTER, GUSTAV_2ND_LETTER, GUSTAV_3RD_LETTER, SCEPTER_OF_JUDGMENT, BLACK_ANVIL_COIN);
		
		addQuestStart(SIR_GUSTAV_ATHEBALDT, MARTIEN, CORPSE_OF_FRITZ, CORPSE_OF_LUTZ, CORPSE_OF_KURTZ, KUSTO, BALTHAZAR, SIR_ERIC_RODEMAI, WITCH_CLEO, WITCH_ATHREA, WITCH_KALIS, IMPERIAL_COFFER);
		addTalkId(SIR_GUSTAV_ATHEBALDT, MARTIEN, CORPSE_OF_FRITZ, CORPSE_OF_LUTZ, CORPSE_OF_KURTZ, KUSTO, BALTHAZAR, SIR_ERIC_RODEMAI, WITCH_CLEO, WITCH_ATHREA, WITCH_KALIS, IMPERIAL_COFFER);
		
		addMyDying(BLITZ_WYRM, DRAKE, DRAKE_HOLD, GRAVE_GUARD, GRAVE_KEYMASTER, LESSER_GIANT_SCOUT, LESSER_GIANT_SOLDIER, THUNDER_WYRM, THUNDER_WYRM_HOLD, IMPERIAL_GRAVEKEEPER);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Sir Gustav Athebaldt
		if (event.equalsIgnoreCase("30760-08.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			giveItems(player, GUSTAV_1ST_LETTER, 1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30760-12.htm"))
		{
			st.setCond(4);
			giveItems(player, GUSTAV_2ND_LETTER, 1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30760-16.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, GUSTAV_3RD_LETTER, 1);
		}
		else if (event.equalsIgnoreCase("30760-20.htm"))
		{
			if (player.getInventory().hasItem(SCEPTER_OF_JUDGMENT))
			{
				takeItems(player, SCEPTER_OF_JUDGMENT, -1);
				giveItems(player, SEAL_OF_ASPIRATION, 1);
				rewardExpAndSp(player, 0, 250000);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				return null;
		}
		else if (event.equalsIgnoreCase("30760-22.htm"))
		{
			if (st.getCond() == 11)
			{
				// Update status only when necessary (repeated approach to this HTM is possible).
				st.setCond(12);
				playSound(player, SOUND_MIDDLE);
			}
		}
		else if (event.equalsIgnoreCase("30760-23.htm"))
		{
			if (player.getInventory().hasItem(SCEPTER_OF_JUDGMENT))
			{
				takeItems(player, SCEPTER_OF_JUDGMENT, -1);
				giveItems(player, SEAL_OF_ASPIRATION, 1);
				rewardExpAndSp(player, 0, 250000);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				return null;
		}
		// Martien
		else if (event.equalsIgnoreCase("30645-03.htm"))
		{
			// Set dwarf corpses flags
			st.set("30761", true);
			st.set("30762", true);
			st.set("30763", true);
			
			st.setCond(2);
			takeItems(player, GUSTAV_1ST_LETTER, -1);
			playSound(player, SOUND_MIDDLE);
		}
		// Corpse of Fritz
		else if (event.equalsIgnoreCase("30761-02.htm"))
		{
			final String flag = String.valueOf(npc.getNpcId());
			if (st.containsKey(flag))
			{
				// Unset NPC flag and give clan leader items.
				st.unset(flag);
				giveItems(player, BLITZ_WYRM_EGG, 3);
			}
			else
				htmltext = "30761-03.htm";
			
			// Spawn two Blitz Wyrms
			createOnePrivate(npc, BLITZ_WYRM, 180000, true);
			createOnePrivate(npc, BLITZ_WYRM, 180000, true);
			
			// Despawn NPC (respawn timer is started)
			startQuestTimer("despawn", npc, null, 10000);
		}
		// Corpse of Lutz
		else if (event.equalsIgnoreCase("30762-02.htm"))
		{
			final String flag = String.valueOf(npc.getNpcId());
			if (st.containsKey(flag))
			{
				// Unset NPC flag and give clan leader items.
				st.unset(flag);
				giveItems(player, BLITZ_WYRM_EGG, 3);
				giveItems(player, MIST_DRAKE_EGG, 4);
			}
			else
				htmltext = "30761-03.htm";
			
			// Spawn two Blitz Wyrms
			createOnePrivate(npc, BLITZ_WYRM, 180000, true);
			createOnePrivate(npc, BLITZ_WYRM, 180000, true);
			
			// Despawn NPC (respawn timer is started)
			startQuestTimer("despawn", npc, null, 10000);
		}
		// Corpse of Kurtz
		else if (event.equalsIgnoreCase("30763-02.htm"))
		{
			// Unset NPC flag and give clan leader items.
			st.unset(String.valueOf(npc.getNpcId()));
			giveItems(player, BROOCH_OF_THE_MAGPIE, 1);
			giveItems(player, MIST_DRAKE_EGG, 6);
			
			npc.deleteMe();
		}
		// Kusto
		else if (event.equalsIgnoreCase("30512-03.htm"))
		{
			if (player.getInventory().hasItem(BROOCH_OF_THE_MAGPIE))
			{
				takeItems(player, BROOCH_OF_THE_MAGPIE, -1);
				giveItems(player, BLACK_ANVIL_COIN, 1);
			}
		}
		// Balthazar
		else if (event.equalsIgnoreCase("30764-03.htm"))
		{
			st.setCond(5);
			takeItems(player, GUSTAV_2ND_LETTER, -1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30764-06.htm"))
		{
			st.setCond(5);
			takeItems(player, GUSTAV_2ND_LETTER, -1);
			takeItems(player, BLACK_ANVIL_COIN, -1);
			giveItems(player, RECIPE_TITAN_POWERSTONE, 1);
			playSound(player, SOUND_MIDDLE);
		}
		// Sir Eric Rodemai
		else if (event.equalsIgnoreCase("30868-04.htm"))
		{
			st.setCond(8);
			takeItems(player, GUSTAV_3RD_LETTER, -1);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30868-10.htm"))
		{
			st.setCond(11);
			playSound(player, SOUND_MIDDLE);
		}
		// Witch Cleo
		else if (event.equalsIgnoreCase("30766-04.htm"))
		{
			npc.broadcastNpcSay(NpcStringId.ID_50338);
			
			Npc witch = createOnePrivateEx(npc, WITCH_ATHREA, 160688, 21296, -3714, 32768, 5000, false);
			witch.broadcastNpcSay(NpcStringId.ID_50340);
			
			witch = createOnePrivateEx(npc, WITCH_KALIS, 160690, 21176, -3712, 32768, 5000, false);
			witch.broadcastNpcSay(NpcStringId.ID_50341);
			
			st.setCond(9);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30766-08.htm"))
		{
			if (player.getInventory().hasItem(SCEPTER_OF_JUDGMENT))
			{
				takeItems(player, SCEPTER_OF_JUDGMENT, -1);
				giveItems(player, SEAL_OF_ASPIRATION, 1);
				rewardExpAndSp(player, 0, 250000);
				playSound(player, SOUND_FINISH);
				st.exitQuest(true);
			}
			else
				return null;
		}
		// Imperial Coffer
		else if (event.equalsIgnoreCase("30765-04.htm"))
		{
			if (player.getInventory().getItemCount(IMPERIAL_KEY) >= 6)
			{
				st.unset("killed");
				takeItems(player, IMPERIAL_KEY, -1);
				giveItems(player, SCEPTER_OF_JUDGMENT, 1);
			}
			else
				htmltext = "30765-05a.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		npc.deleteMe();
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = getNoQuestMsg();
		final QuestState lst = getClanLeaderQuestState(player, null);
		if (lst == null)
		{
			if (npc.getNpcId() == SIR_GUSTAV_ATHEBALDT && !player.isClanLeader())
				htmltext = "30760-04t.htm";
			
			return htmltext;
		}
		
		switch (lst.getState())
		{
			case CREATED:
				switch (npc.getNpcId())
				{
					case SIR_GUSTAV_ATHEBALDT:
						if (player.isClanLeader())
						{
							final int level = player.getClan().getLevel();
							if (level < 4)
								htmltext = "30760-01.htm";
							else if (level == 4)
								htmltext = player.getInventory().hasItem(SEAL_OF_ASPIRATION) ? "30760-03.htm" : "30760-04.htm";
							else
								htmltext = "30760-02.htm";
						}
						else
							htmltext = "30760-04t.htm";
						break;
				}
				break;
			
			case STARTED:
				final int cond = lst.getCond();
				switch (npc.getNpcId())
				{
					case SIR_GUSTAV_ATHEBALDT:
						// Clan leader talks.
						if (player.isClanLeader())
						{
							if (cond == 1)
								htmltext = "30760-09.htm";
							else if (cond == 2)
								htmltext = "30760-10.htm";
							else if (cond == 3)
								htmltext = "30760-11.htm";
							else if (cond == 4)
								htmltext = "30760-13.htm";
							else if (cond == 5)
								htmltext = "30760-14.htm";
							else if (cond == 6)
								htmltext = "30760-15.htm";
							else if (cond == 7)
								htmltext = "30760-17.htm";
							else if (cond == 8 || cond == 9)
								htmltext = "30760-18.htm";
							else if (cond == 10)
								htmltext = player.getInventory().hasItem(SCEPTER_OF_JUDGMENT) ? "30760-19.htm" : "30760-18.htm";
							else if (cond == 11)
								htmltext = "30760-19.htm";
							else if (cond == 12)
								htmltext = "30760-24.htm";
						}
						// Clan member talks.
						else
						{
							if (cond == 3)
								htmltext = "30760-11t.htm";
							else if (cond == 6)
								htmltext = "30760-15t.htm";
							else if (cond == 11)
								htmltext = "30760-19t.htm";
							else if (cond == 12)
								htmltext = "30760-24t.htm";
						}
						break;
					
					case MARTIEN:
						// Clan leader talks.
						if (player.isClanLeader())
						{
							if (cond == 1)
								htmltext = "30645-02.htm";
							else if (cond == 2)
							{
								if (player.getInventory().getItemCount(MIST_DRAKE_EGG) > 9 && player.getInventory().getItemCount(BLITZ_WYRM_EGG) > 9 && player.getInventory().getItemCount(DRAKE_EGG) > 9 && player.getInventory().getItemCount(THUNDER_WYRM_EGG) > 9)
								{
									htmltext = "30645-05.htm";
									takeItems(player, MIST_DRAKE_EGG, -1);
									takeItems(player, BLITZ_WYRM_EGG, -1);
									takeItems(player, DRAKE_EGG, -1);
									takeItems(player, THUNDER_WYRM_EGG, -1);
									lst.setCond(3);
									playSound(player, SOUND_MIDDLE);
								}
								else
									htmltext = "30645-04.htm";
							}
							else if (cond == 3)
								htmltext = "30645-07.htm";
							else
								htmltext = "30645-08.htm";
						}
						// Clan member talks.
						else
						{
							if (cond == 1)
								htmltext = "30645-01.htm";
						}
						break;
					
					case CORPSE_OF_FRITZ:
						// Clan leader talks.
						if (player.isClanLeader() && cond == 2)
							htmltext = "30761-01.htm";
						break;
					
					case CORPSE_OF_LUTZ:
						// Clan leader talks.
						if (player.isClanLeader() && cond == 2)
							htmltext = "30762-01.htm";
						break;
					
					case CORPSE_OF_KURTZ:
						// Clan leader talks.
						if (player.isClanLeader() && cond == 2)
						{
							if (lst.containsKey(String.valueOf(npc.getNpcId())))
								htmltext = "30763-01.htm";
							else
								htmltext = "30763-03.htm";
						}
						break;
					
					case KUSTO:
						// Clan leader talks.
						if (player.isClanLeader())
						{
							if (cond <= 4)
							{
								if (!player.getInventory().hasItem(BROOCH_OF_THE_MAGPIE) && !player.getInventory().hasItem(BLACK_ANVIL_COIN))
									htmltext = "30512-01.htm";
								else if (player.getInventory().hasItem(BROOCH_OF_THE_MAGPIE))
									htmltext = "30512-02.htm";
								else if (player.getInventory().hasItem(BLACK_ANVIL_COIN) && !player.getInventory().hasItem(BROOCH_OF_THE_MAGPIE))
									htmltext = "30512-04.htm";
							}
						}
						// Clan member talks.
						else
						{
							htmltext = "30512-01a.htm";
						}
						break;
					
					case BALTHAZAR:
						// Clan leader talks.
						if (player.isClanLeader())
						{
							if (cond == 4)
								htmltext = player.getInventory().hasItem(BLACK_ANVIL_COIN) ? "30764-04.htm" : "30764-02.htm";
							else if (cond == 5)
							{
								if (player.getInventory().getItemCount(NEBULITE_CRYSTALS) > 9 && player.getInventory().getItemCount(TITAN_POWERSTONE) > 9)
								{
									htmltext = "30764-08.htm";
									lst.setCond(6);
									takeItems(player, NEBULITE_CRYSTALS, -1);
									takeItems(player, TITAN_POWERSTONE, -1);
									playSound(player, SOUND_MIDDLE);
								}
								else
									htmltext = "30764-07.htm";
							}
							else if (cond >= 6)
								htmltext = "30764-09.htm";
						}
						// Clan member talks.
						else
						{
							if (cond == 4)
								htmltext = "30764-01.htm";
						}
						break;
					
					case SIR_ERIC_RODEMAI:
						// Clan leader talks.
						if (player.isClanLeader())
						{
							if (cond == 7)
								htmltext = "30868-02.htm";
							else if (cond == 8)
								htmltext = "30868-05.htm";
							else if (cond == 9)
							{
								htmltext = "30868-06.htm";
								lst.setCond(10);
								playSound(player, SOUND_MIDDLE);
							}
							else if (cond == 10)
								htmltext = player.getInventory().hasItem(SCEPTER_OF_JUDGMENT) ? "30868-09.htm" : "30868-08.htm";
							else if (cond >= 11)
								htmltext = "30868-11.htm";
						}
						// Clan member talks.
						else
						{
							if (cond == 7)
								htmltext = "30868-01.htm";
							else if (cond == 9)
								htmltext = "30868-07.htm";
						}
						break;
					
					case WITCH_CLEO:
						// Clan leader talks.
						if (player.isClanLeader())
						{
							if (cond == 8)
								htmltext = "30766-02.htm";
							else if (cond == 9)
								htmltext = "30766-05.htm";
							else if (cond == 10 || cond == 11)
								htmltext = "30766-06.htm";
							else if (cond == 12)
								htmltext = "30766-07.htm";
						}
						// Clan member talks.
						else
						{
							htmltext = "30766-01.htm";
						}
						break;
					
					case WITCH_ATHREA:
						if (cond == 9)
							htmltext = "30758-01.htm";
						break;
					
					case WITCH_KALIS:
						if (cond == 9)
							htmltext = "30759-01.htm";
						break;
					
					case IMPERIAL_COFFER:
						if (cond == 10)
						{
							// Clan leader talks.
							if (player.isClanLeader())
								htmltext = player.getInventory().hasItem(SCEPTER_OF_JUDGMENT) ? "30765-05.htm" : "30765-03.htm";
							// Clan member talks.
							else
								htmltext = "30765-01.htm";
						}
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
		
		QuestState lst = getClanLeaderQuestState(player, npc);
		if (lst == null)
			return;
		
		final Player leader = lst.getPlayer();
		
		switch (npc.getNpcId())
		{
			case THUNDER_WYRM, THUNDER_WYRM_HOLD:
				if (lst.getCond() == 2)
					dropItems(leader, THUNDER_WYRM_EGG, 1, 10, 500000);
				break;
			
			case DRAKE, DRAKE_HOLD:
				if (lst.getCond() == 2)
				{
					dropItems(leader, MIST_DRAKE_EGG, 1, 10, 100000);
					dropItems(leader, DRAKE_EGG, 1, 10, 500000);
				}
				break;
			
			case BLITZ_WYRM:
				if (lst.getCond() == 2)
					dropItemsAlways(leader, BLITZ_WYRM_EGG, 1, 10);
				break;
			
			case LESSER_GIANT_SCOUT, LESSER_GIANT_SOLDIER:
				if (lst.getCond() == 5)
				{
					final int chance = Rnd.get(100);
					if (chance < 10)
						dropItemsAlways(leader, TITAN_POWERSTONE, 1, 10);
					else if (chance < 30)
						dropItemsAlways(leader, NEBULITE_CRYSTALS, 1, 10);
					else if (chance < 80)
						dropItemsAlways(leader, BROKEN_TITAN_POWERSTONE, 1, 0);
				}
				break;
			
			case GRAVE_GUARD:
				if (lst.getCond() == 10 && !leader.getInventory().hasItem(SCEPTER_OF_JUDGMENT))
				{
					// Get killed count of Grave Guards.
					int killed = lst.getInteger("killed");
					killed++;
					
					// Chance to spawn Grave Keymaster by killed count: 1-4 = 0% chance, 5-9 = 50% chance, 10+ = 100%
					if ((Rnd.get(100) < 50 && killed > 4) || killed > 9)
					{
						killed = 0;
						addSpawn(GRAVE_KEYMASTER, npc, false, 0, false);
					}
					
					// Update killed.
					lst.set("killed", killed);
				}
				break;
			
			case GRAVE_KEYMASTER:
				if (lst.getCond() == 10 && !leader.getInventory().hasItem(SCEPTER_OF_JUDGMENT))
					dropItemsAlways(leader, IMPERIAL_KEY, 1, 6);
				break;
			
			case IMPERIAL_GRAVEKEEPER:
				if (lst.getCond() == 10 && !leader.getInventory().hasItem(SCEPTER_OF_JUDGMENT))
				{
					Npc coffer = addSpawn(IMPERIAL_COFFER, npc, false, 180000, false);
					coffer.broadcastNpcSay(NpcStringId.ID_50339);
				}
				break;
		}
	}
}