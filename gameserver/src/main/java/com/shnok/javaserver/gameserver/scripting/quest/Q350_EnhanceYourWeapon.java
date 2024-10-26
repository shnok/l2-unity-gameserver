package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.data.xml.SoulCrystalData;
import net.sf.l2j.gameserver.enums.QuestStatus;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.container.npc.AbsorbInfo;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.records.LevelingInfo;
import net.sf.l2j.gameserver.model.records.SoulCrystal;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

public class Q350_EnhanceYourWeapon extends Quest
{
	private static final String QUEST_NAME = "Q350_EnhanceYourWeapon";
	
	public Q350_EnhanceYourWeapon()
	{
		super(350, "Enhance Your Weapon");
		
		addQuestStart(30115, 30194, 30856);
		addTalkId(30115, 30194, 30856);
		
		for (int npcId : SoulCrystalData.getInstance().getLevelingInfos().keySet())
			addMyDying(npcId);
		
		for (int crystalId : SoulCrystalData.getInstance().getSoulCrystals().keySet())
			addItemUse(crystalId);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = event;
		QuestState st = player.getQuestList().getQuestState(QUEST_NAME);
		if (st == null)
			return htmltext;
		
		// Start the quest.
		if (event.endsWith("-04.htm"))
		{
			st.setState(QuestStatus.STARTED);
			st.setCond(1);
			playSound(player, SOUND_ACCEPT);
		}
		// Give Red Soul Crystal.
		else if (event.endsWith("-09.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			giveItems(player, 4629, 1);
		}
		// Give Green Soul Crystal.
		else if (event.endsWith("-10.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			giveItems(player, 4640, 1);
		}
		// Give Blue Soul Crystal.
		else if (event.endsWith("-11.htm"))
		{
			playSound(player, SOUND_MIDDLE);
			giveItems(player, 4651, 1);
		}
		// Terminate the quest.
		else if (event.endsWith("-exit.htm"))
			st.exitQuest(true);
		
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
				if (player.getStatus().getLevel() < 40)
					htmltext = npc.getNpcId() + "-lvl.htm";
				else
					htmltext = npc.getNpcId() + "-01.htm";
				break;
			
			case STARTED:
				// Check inventory for soul crystals.
				for (ItemInstance item : player.getInventory().getItems())
				{
					// Crystal found, show "how to" html.
					if (SoulCrystalData.getInstance().getSoulCrystals().get(item.getItemId()) != null)
						return npc.getNpcId() + "-03.htm";
				}
				// No crystal found, offer a new crystal.
				htmltext = npc.getNpcId() + "-21.htm";
				break;
		}
		
		return htmltext;
	}
	
	@Override
	public void onItemUse(ItemInstance item, Player user, WorldObject target)
	{
		// Caster is dead.
		if (user.isDead())
			return;
		
		// No target, or target isn't an Monster.
		if (!(target instanceof Monster monster))
			return;
		
		// Mob is dead or not registered in _npcInfos.
		if (monster.isDead() || !SoulCrystalData.getInstance().getLevelingInfos().containsKey(monster.getNpcId()))
			return;
		
		// Add user to mob's absorber list.
		monster.addAbsorber(user, item);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final Player player = killer.getActingPlayer();
		if (player == null)
			return;
		
		// Retrieve individual mob informations.
		final LevelingInfo npcInfo = SoulCrystalData.getInstance().getLevelingInfos().get(npc.getNpcId());
		if (npcInfo == null)
			return;
		
		final int chance = Rnd.get(1000);
		final Monster monster = (Monster) npc;
		
		// Handle npc leveling info type.
		switch (npcInfo.absorbCrystalType())
		{
			case FULL_PARTY:
				for (QuestState st : getPartyMembersState(player, npc, QuestStatus.STARTED))
					tryToStageCrystal(st.getPlayer(), monster, npcInfo, chance);
				break;
			
			case PARTY_ONE_RANDOM:
				final QuestState st = getRandomPartyMemberState(player, npc, QuestStatus.STARTED);
				if (st != null)
					tryToStageCrystal(st.getPlayer(), monster, npcInfo, chance);
				break;
			
			case LAST_HIT:
				if (checkPlayerState(player, npc, QuestStatus.STARTED) != null)
					tryToStageCrystal(player, monster, npcInfo, chance);
				break;
		}
	}
	
	/**
	 * Define the Soul Crystal and try to stage it.<br>
	 * <br>
	 * Check for quest enabled, crystal(s) in inventory, required usage of crystal, mob's ability to level crystal and {@link Monster} vs {@link Player} level gap.
	 * @param player : The {@link Player} to make checks on.
	 * @param monster : The {@link Monster} to make checks on.
	 * @param npcInfo : The {@link LevelingInfo} informations.
	 * @param chance : Input variable used to determine keep/stage/break of the crystal.
	 */
	private static void tryToStageCrystal(Player player, Monster monster, LevelingInfo npcInfo, int chance)
	{
		SoulCrystal crystalData = null;
		ItemInstance crystalItem = null;
		
		// Iterate through player's inventory to find crystal(s).
		for (ItemInstance item : player.getInventory().getItems())
		{
			SoulCrystal data = SoulCrystalData.getInstance().getSoulCrystals().get(item.getItemId());
			if (data == null)
				continue;
			
			// More crystals found.
			if (crystalData != null)
			{
				// Leveling requires soul crystal being used?
				if (npcInfo.isSkillRequired())
				{
					// Absorb list contains killer and his AbsorbInfo is registered.
					final AbsorbInfo ai = monster.getAbsorbInfo(player.getObjectId());
					if (ai != null && ai.isRegistered())
						player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_FAILED_RESONATION);
				}
				else
					player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_FAILED_RESONATION);
				
				return;
			}
			
			crystalData = data;
			crystalItem = item;
		}
		
		// No crystal found, return without any notification.
		if (crystalData == null || crystalItem == null)
			return;
		
		// Leveling requires soul crystal being used?
		if (npcInfo.isSkillRequired())
		{
			// Absorb list doesn't contain killer or his AbsorbInfo is not registered.
			final AbsorbInfo ai = monster.getAbsorbInfo(player.getObjectId());
			if (ai == null || !ai.isRegistered())
				return;
			
			// Check if Absorb list contains valid crystal and whether it was used properly.
			if (!ai.isValid(crystalItem.getObjectId()))
			{
				player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_REFUSED);
				return;
			}
		}
		
		// Check, if npc stages this type of crystal.
		if (!ArraysUtil.contains(npcInfo.levelList(), crystalData.level()))
		{
			player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_REFUSED);
			return;
		}
		
		// Check level difference limitation, dark blue monsters does not stage.
		if (player.getStatus().getLevel() - monster.getStatus().getLevel() > 8)
		{
			player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_REFUSED);
			return;
		}
		
		// Lucky, crystal successfully stages.
		if (chance < npcInfo.chanceStage())
			exchangeCrystal(player, crystalData, true);
		// Bad luck, crystal accidentally breaks.
		else if (chance < (npcInfo.chanceStage() + npcInfo.chanceBreak()))
			exchangeCrystal(player, crystalData, false);
		// Bad luck, crystal doesn't stage.
		else
			player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_FAILED);
	}
	
	/**
	 * Remove the old crystal and add new one if stage, broken crystal if break. Send messages in both cases.
	 * @param player : The {@link Player} to check on (inventory and send messages).
	 * @param sc : The {@link SoulCrystal} to take information from.
	 * @param stage : Switch to determine success or fail.
	 */
	private static void exchangeCrystal(Player player, SoulCrystal sc, boolean stage)
	{
		takeItems(player, sc.initialItemId(), 1);
		if (stage)
		{
			player.sendPacket(SystemMessageId.SOUL_CRYSTAL_ABSORBING_SUCCEEDED);
			giveItems(player, sc.stagedItemId(), 1);
			playSound(player, SOUND_ITEMGET);
		}
		else
		{
			int broken = sc.brokenItemId();
			if (broken != 0)
			{
				player.sendPacket(SystemMessageId.SOUL_CRYSTAL_BROKE);
				giveItems(player, broken, 1);
			}
		}
	}
}