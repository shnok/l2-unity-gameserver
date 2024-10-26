package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class FreyaGardener extends PartyLeaderWarriorAggressive
{
	public FreyaGardener()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public FreyaGardener(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22100
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._c_ai0 = null;
		npc._c_ai1 = null;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (npc._i_ai1 < 3 && getAbnormalLevel(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF)) == -1)
			{
				if (npc._i_ai1 == 0)
				{
					npc._c_ai0 = attacker;
					
					npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.DEBUFF), 1000000);
				}
				else if (npc._i_ai1 == 1 && npc._c_ai0 != attacker)
				{
					npc._c_ai1 = attacker;
					
					npc.getAI().addCastDesire(npc._c_ai1, getNpcSkillByType(npc, NpcSkillType.DEBUFF), 1000000);
				}
				else if (npc._i_ai1 == 2 && npc._c_ai0 != attacker && npc._c_ai1 != attacker)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF), 1000000);
				
				npc._i_ai1++;
			}
			else if (Rnd.get(100) < 20)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.RANGE_HOLD_A), 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.DEBUFF))
			npc.getAI().getAggroList().stopHate(creature);
		
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_npc2314_2m1");
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("10005", maker0, 0, 0);
	}
}