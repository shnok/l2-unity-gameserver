package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorGrowth;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WarriorGrowthStep4Aggressive extends Warrior
{
	public WarriorGrowthStep4Aggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorGrowth");
	}
	
	public WarriorGrowthStep4Aggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21468,
		21487,
		21506,
		21824,
		21826,
		21828
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		npc._i_ai4 = 1;
		npc._c_ai0 = (Creature) World.getInstance().getObject(npc._param1);
		npc._i_ai3 = npc._param2;
		
		if (npc._c_ai0 != null)
			npc.getAI().addAttackDesire(npc._c_ai0, 100);
		
		npc._i_ai2 = 0;
		
		broadcastScriptEvent(npc, 10018, npc.getObjectId(), 700);
		
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (!(creature instanceof Playable))
			return;
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if (npc._i_ai1 == 1 && topDesireTarget == attacker)
			npc._i_ai2 = 1;
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable && topDesireTarget == attacker && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable && called.getAI().getTopDesireTarget() == attacker && Rnd.get(100) < 33)
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			{
				npc._i_ai1 = 0;
				npc._i_ai2 = 0;
			}
			else if (npc._i_ai2 == 0)
			{
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				if (topDesireTarget != null && Rnd.get(100) < 50)
				{
					final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
					if (getAbnormalLevel(topDesireTarget, ddMagic) <= 0)
						npc.getAI().addCastDesire(topDesireTarget, ddMagic, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai2 = 0;
		}
		
		return super.onTimer(name, npc, player);
	}
}
