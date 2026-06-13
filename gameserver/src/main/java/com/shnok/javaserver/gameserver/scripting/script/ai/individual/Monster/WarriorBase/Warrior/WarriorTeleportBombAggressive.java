package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class WarriorTeleportBombAggressive extends WarriorBomb
{
	public WarriorTeleportBombAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorTeleportBombAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22133
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = GameTimeTaskManager.getInstance().getCurrentTick();
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		final int i0 = getElapsedTicks(npc._i_ai0);
		if (i0 >= 10 && npc._i_ai1 == 0)
		{
			npc.teleportTo(npc.getSpawn().getSpawnLocation(), 0);
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT), 1000000);
			
			npc._i_ai0 = GameTimeTaskManager.getInstance().getCurrentTick();
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (npc._i_ai1 == 0 && creature instanceof Playable)
		{
			npc._i_ai1 = 1;
			npc.teleportTo(creature.getPosition(), 0);
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT), 1000000);
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called._i_ai1 == 0 && attacker instanceof Playable)
		{
			called._i_ai1 = 1;
			called.teleportTo(attacker.getPosition(), 0);
			called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.TELEPORT_EFFECT), 1000000);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.TELEPORT_EFFECT) && npc._i_ai1 == 1)
			startQuestTimer("1001", npc, null, 5000);
		else if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC2) && success)
			npc.doDie(npc);
		else if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC3) && success)
			npc.doDie(npc);
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			final int i0 = Rnd.get(100);
			if (i0 < 33)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
			else if (i0 < 66)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC2), 1000000);
			else
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC3), 1000000);
		}
		
		return null;
	}
}
