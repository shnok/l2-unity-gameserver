package com.shnok.javaserver.gameserver.scripting.quest;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Q234_FatesWhisper extends Quest
{
	private static final String QUEST_NAME = "Q234_FatesWhisper";
	
	// Items
	private static final int REIRIA_SOUL_ORB = 4666;
	private static final int KERMON_INFERNIUM_SCEPTER = 4667;
	private static final int GOLKONDA_INFERNIUM_SCEPTER = 4668;
	private static final int HALLATE_INFERNIUM_SCEPTER = 4669;
	
	private static final int INFERNIUM_VARNISH = 4672;
	private static final int REORIN_HAMMER = 4670;
	private static final int REORIN_MOLD = 4671;
	
	private static final int PIPETTE_KNIFE = 4665;
	private static final int RED_PIPETTE_KNIFE = 4673;
	
	private static final int CRYSTAL_B = 1460;
	
	// Reward
	private static final int STAR_OF_DESTINY = 5011;
	
	// Chest Spawn
	private static final Map<Integer, Integer> CHEST_SPAWN = HashMap.newHashMap(4);
	
	// Weapons
	private static final Map<Integer, String> WEAPONS = HashMap.newHashMap(13);
	
	public Q234_FatesWhisper()
	{
		super(234, "Fate's Whispers");
		
		CHEST_SPAWN.put(25035, 31027);
		CHEST_SPAWN.put(25054, 31028);
		CHEST_SPAWN.put(25126, 31029);
		CHEST_SPAWN.put(25220, 31030);
		
		WEAPONS.put(79, "Sword of Damascus");
		WEAPONS.put(97, "Lance");
		WEAPONS.put(171, "Deadman's Glory");
		WEAPONS.put(175, "Art of Battle Axe");
		WEAPONS.put(210, "Staff of Evil Spirits");
		WEAPONS.put(234, "Demon Dagger");
		WEAPONS.put(268, "Bellion Cestus");
		WEAPONS.put(287, "Bow of Peril");
		WEAPONS.put(2626, "Samurai Dual-sword");
		WEAPONS.put(7883, "Guardian Sword");
		WEAPONS.put(7889, "Wizard's Tear");
		WEAPONS.put(7893, "Kaim Vanul's Bones");
		WEAPONS.put(7901, "Star Buster");
		
		setItemsIds(PIPETTE_KNIFE, RED_PIPETTE_KNIFE);
		
		addQuestStart(31002);
		addTalkId(31002, 30182, 30847, 30178, 30833, 31028, 31029, 31030, 31027);
		
		addAttacked(29020);
		addMyDying(25035, 25054, 25126, 25220);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		if (event.equalsIgnoreCase("31002-03.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		else if (event.equalsIgnoreCase("30182-01c.htm"))
		{
			playSound(player, SOUND_ITEMGET);
			giveItems(player, INFERNIUM_VARNISH, 1);
		}
		else if (event.equalsIgnoreCase("30178-01a.htm"))
		{
			st.setCond(6);
			playSound(player, SOUND_MIDDLE);
		}
		else if (event.equalsIgnoreCase("30833-01b.htm"))
		{
			st.setCond(7);
			playSound(player, SOUND_MIDDLE);
			giveItems(player, PIPETTE_KNIFE, 1);
		}
		else if (event.startsWith("selectBGrade_"))
		{
			if (st.getInteger("bypass") == 1)
				return null;
			
			String bGradeId = event.replace("selectBGrade_", "");
			st.set("weaponId", bGradeId);
			htmltext = getHtmlText("31002-13.htm").replace("%weaponname%", WEAPONS.get(st.getInteger("weaponId")));
		}
		else if (event.startsWith("confirmWeapon"))
		{
			st.set("bypass", 1);
			htmltext = getHtmlText("31002-14.htm").replace("%weaponname%", WEAPONS.get(st.getInteger("weaponId")));
		}
		else if (event.startsWith("selectAGrade_"))
		{
			if (st.getInteger("bypass") == 1)
			{
				final int itemId = st.getInteger("weaponId");
				if (player.getInventory().hasItem(itemId))
				{
					int aGradeItemId = Integer.parseInt(event.replace("selectAGrade_", ""));
					
					htmltext = getHtmlText("31002-12.htm").replace("%weaponname%", ItemData.getInstance().getTemplate(aGradeItemId).getName());
					takeItems(player, itemId, 1);
					giveItems(player, aGradeItemId, 1);
					giveItems(player, STAR_OF_DESTINY, 1);
					player.broadcastPacket(new SocialAction(player, 3));
					playSound(player, SOUND_FINISH);
					st.exitQuest(false);
				}
				else
					htmltext = getHtmlText("31002-15.htm").replace("%weaponname%", WEAPONS.get(itemId));
			}
			else
				htmltext = "31002-16.htm";
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
				htmltext = (player.getStatus().getLevel() < 75) ? "31002-01.htm" : "31002-02.htm";
				break;
			
			case STARTED:
				int cond = st.getCond();
				switch (npc.getNpcId())
				{
					case 31002:
						if (cond == 1)
						{
							if (!player.getInventory().hasItem(REIRIA_SOUL_ORB))
								htmltext = "31002-04b.htm";
							else
							{
								htmltext = "31002-05.htm";
								st.setCond(2);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, REIRIA_SOUL_ORB, 1);
							}
						}
						else if (cond == 2)
						{
							if (!player.getInventory().hasItem(KERMON_INFERNIUM_SCEPTER) || !player.getInventory().hasItem(GOLKONDA_INFERNIUM_SCEPTER) || !player.getInventory().hasItem(HALLATE_INFERNIUM_SCEPTER))
								htmltext = "31002-05c.htm";
							else
							{
								htmltext = "31002-06.htm";
								st.setCond(3);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, GOLKONDA_INFERNIUM_SCEPTER, 1);
								takeItems(player, HALLATE_INFERNIUM_SCEPTER, 1);
								takeItems(player, KERMON_INFERNIUM_SCEPTER, 1);
							}
						}
						else if (cond == 3)
						{
							if (!player.getInventory().hasItem(INFERNIUM_VARNISH))
								htmltext = "31002-06b.htm";
							else
							{
								htmltext = "31002-07.htm";
								st.setCond(4);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, INFERNIUM_VARNISH, 1);
							}
						}
						else if (cond == 4)
						{
							if (!player.getInventory().hasItem(REORIN_HAMMER))
								htmltext = "31002-07b.htm";
							else
							{
								htmltext = "31002-08.htm";
								st.setCond(5);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, REORIN_HAMMER, 1);
							}
						}
						else if (cond > 4 && cond < 8)
							htmltext = "31002-08b.htm";
						else if (cond == 8)
						{
							htmltext = "31002-09.htm";
							st.setCond(9);
							playSound(player, SOUND_MIDDLE);
							takeItems(player, REORIN_MOLD, 1);
						}
						else if (cond == 9)
						{
							if (player.getInventory().getItemCount(CRYSTAL_B) < 984)
								htmltext = "31002-09b.htm";
							else
							{
								htmltext = "31002-BGradeList.htm";
								st.setCond(10);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, CRYSTAL_B, 984);
							}
						}
						else if (cond == 10)
						{
							// If a weapon is selected
							if (st.getInteger("bypass") == 1)
							{
								// If you got it in the inventory
								final int itemId = st.getInteger("weaponId");
								htmltext = getHtmlText((player.getInventory().hasItem(itemId)) ? "31002-AGradeList.htm" : "31002-15.htm").replace("%weaponname%", WEAPONS.get(itemId));
							}
							// B weapon is still not selected
							else
								htmltext = "31002-BGradeList.htm";
						}
						break;
					
					case 30182:
						if (cond == 3)
							htmltext = (!player.getInventory().hasItem(INFERNIUM_VARNISH)) ? "30182-01.htm" : "30182-02.htm";
						break;
					
					case 30847:
						if (cond == 4 && !player.getInventory().hasItem(REORIN_HAMMER))
						{
							htmltext = "30847-01.htm";
							playSound(player, SOUND_ITEMGET);
							giveItems(player, REORIN_HAMMER, 1);
						}
						else if (cond >= 4 && player.getInventory().hasItem(REORIN_HAMMER))
							htmltext = "30847-02.htm";
						break;
					
					case 30178:
						if (cond == 5)
							htmltext = "30178-01.htm";
						else if (cond > 5)
							htmltext = "30178-02.htm";
						break;
					
					case 30833:
						if (cond == 6)
							htmltext = "30833-01.htm";
						else if (cond == 7)
						{
							if (player.getInventory().hasItem(PIPETTE_KNIFE) && !player.getInventory().hasItem(RED_PIPETTE_KNIFE))
								htmltext = "30833-02.htm";
							else
							{
								htmltext = "30833-03.htm";
								st.setCond(8);
								playSound(player, SOUND_MIDDLE);
								takeItems(player, RED_PIPETTE_KNIFE, 1);
								giveItems(player, REORIN_MOLD, 1);
							}
						}
						else if (cond > 7)
							htmltext = "30833-04.htm";
						break;
					
					case 31027:
						if (cond == 1 && !player.getInventory().hasItem(REIRIA_SOUL_ORB))
						{
							htmltext = "31027-01.htm";
							playSound(player, SOUND_ITEMGET);
							giveItems(player, REIRIA_SOUL_ORB, 1);
						}
						else
							htmltext = "31027-02.htm";
						break;
					
					case 31028, 31029, 31030:
						final int itemId = npc.getNpcId() - 26361;
						if (cond == 2 && !player.getInventory().hasItem(itemId))
						{
							htmltext = npc.getNpcId() + "-01.htm";
							playSound(player, SOUND_ITEMGET);
							giveItems(player, itemId, 1);
						}
						else
							htmltext = npc.getNpcId() + "-02.htm";
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
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		
		final QuestState st = checkPlayerCondition(player, npc, 7);
		if (st == null)
			return;
		
		if (player.getActiveWeaponItem() != null && player.getActiveWeaponItem().getItemId() == PIPETTE_KNIFE && !player.getInventory().hasItem(RED_PIPETTE_KNIFE))
		{
			playSound(player, SOUND_ITEMGET);
			takeItems(player, PIPETTE_KNIFE, 1);
			giveItems(player, RED_PIPETTE_KNIFE, 1);
			npc.broadcastNpcSay(NpcStringId.ID_23434);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		addSpawn(CHEST_SPAWN.get(npc.getNpcId()), npc, true, 120000, false);
	}
}