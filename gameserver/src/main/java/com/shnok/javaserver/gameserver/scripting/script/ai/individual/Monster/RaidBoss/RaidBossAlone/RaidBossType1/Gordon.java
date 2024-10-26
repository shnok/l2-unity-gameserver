package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1;

import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Gordon extends RaidBossType1
{
	public Gordon()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1/Gordon");
	}
	
	public Gordon(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29095
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker.getInventory().hasAtLeastOneItem(8190, 8689))
			npc.getAI().addAttackDesire(attacker, damage * 10);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onAttackFinished(Npc npc, Creature target)
	{
		if (target.isDead() || !(target instanceof Player))
			npc.lookItem(500, 1, 8190, 8689);
		
		super.onAttackFinished(npc, target);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.setWalkOrRun(false);
		npc.getAI().addMoveRouteDesire("gordon", 10);
		
		startQuestTimerAtFixedRate("2001", npc, null, 40000, 40000);
		startQuestTimerAtFixedRate("2003", npc, null, 3000, 3000);
	}
	
	@Override
	public void onPickedItem(Npc npc, ItemInstance item)
	{
		if (item.getItemId() == 8190 || item.getItemId() == 8689)
			startQuestTimer("2002", npc, null, 3000);
		
		super.onPickedItem(npc, item);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		final Player player = creature.getActingPlayer();
		if (player == null)
			return;
		
		if (player.isCursedWeaponEquipped())
			npc.getAI().addAttackDesire(creature, 1000000);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onSeeItem(Npc npc, int quantity, List<ItemInstance> items)
	{
		for (ItemInstance item : items)
			npc.getAI().addPickUpDesire(item.getObjectId(), 1000000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
			npc.lookNeighbor();
		else if (name.equalsIgnoreCase("2002"))
			npc.deleteMe();
		else if (name.equalsIgnoreCase("2003"))
			npc.lookItem(500, 1, 8190, 8689);
		
		return super.onTimer(name, npc, player);
	}
}