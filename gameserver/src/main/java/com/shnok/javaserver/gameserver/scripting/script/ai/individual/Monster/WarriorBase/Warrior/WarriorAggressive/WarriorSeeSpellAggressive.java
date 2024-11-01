package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorAggressive;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorSeeSpellAggressive extends WarriorAggressive
{
	public WarriorSeeSpellAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorAggressive");
	}
	
	public WarriorSeeSpellAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21295,
		21297,
		21299
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimerAtFixedRate("2001", npc, null, 10000, 10000);
		
		if (getNpcIntAIParam(npc, "IsTransform") == 0)
		{
			final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
			
			npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		}
		else
		{
			startQuestTimer("2002", npc, null, 3000);
			
			if (npc._param1 == 1000)
			{
				final Creature c0 = (Creature) World.getInstance().getObject(npc._param2);
				if (c0 != null)
				{
					npc.getAI().addCastDesire(c0, 4663, 1, 10000);
					npc.getAI().addAttackDesire(c0, 500);
				}
			}
		}
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
		
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && getAbnormalLevel(npc, selfBuff) <= 0)
				npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
		if (getAbnormalLevel(npc, selfBuff) <= 0)
			npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		
		if (attacker instanceof Playable)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated != null)
			{
				if (Rnd.get(100) < 33 && mostHated == attacker)
				{
					final L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
					
					npc.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null)
			super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}