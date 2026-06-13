package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsApproach;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsApproach extends Warrior
{
	public WarriorCast3SkillsApproach()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsApproach");
	}
	
	public WarriorCast3SkillsApproach(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21177,
		21180,
		21183,
		21186,
		21198,
		21201,
		21204,
		21207,
		21323,
		21009,
		21799,
		21318,
		21315,
		21320
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
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
				if (getAbnormalLevel(npc, selfBuff) <= 0)
					npc.getAI().addCastDesire(npc, selfBuff, 1000000);
			}
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
			npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == attacker)
			{
				if (npc.distance2D(attacker) < 200 && Rnd.get(100) < 33)
				{
					final L2Skill selfRangeDD = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC);
					npc.getAI().addCastDesire(npc, selfRangeDD, 1000000);
				}
				
				if (Rnd.get(100) < 33)
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
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			final Creature mostHated = called.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == attacker && called.distance2D(attacker) < 200 && Rnd.get(100) < 33)
			{
				final L2Skill selfRangeDD = getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DD_MAGIC);
				called.getAI().addCastDesire(called, selfRangeDD, 1000000);
			}
			
			if (Rnd.get(100) < 33)
			{
				final L2Skill physicalSpecial = getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL);
				called.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}