package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorGrowth;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import net.sf.l2j.gameserver.skills.L2Skill;

public class FarmGrowthStep extends Warrior
{
	public FarmGrowthStep()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorGrowth");
	}
	
	public FarmGrowthStep(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		16019,
		16021,
		16023
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = 0;
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null && skill == getNpcSkillAIParam(npc, "FeedID1"))
		{
			final int takeSocial = getNpcIntAIParamOrDefault(npc, "TakeSocial", 5);
			final int moveAroundSocial = getNpcIntAIParam(npc, "MoveAroundSocial");
			
			npc._i_ai3 = skill.getId();
			if (npc._i_ai4 == 0)
			{
				startQuestTimer("2001", npc, null, (takeSocial * 1000) / 30);
				npc._i_ai4 = 1;
				npc._c_ai0 = attacker;
				int i0 = 0;
				if (skill.getId() == npc._i_ai3)
				{
					if (takeSocial != 0)
						npc.getAI().addSocialDesire(1, (moveAroundSocial * 1000) / 30, 200);
					
					i0 = 1;
				}
				
				if (Rnd.get(100) < 5)
				{
					i0 = Rnd.get(10) + 2004;
					npc.broadcastNpcSay(NpcStringId.get(i0));
				}
			}
		}
		else if (attacker instanceof Playable)
		{
			double hateRatio = getHateRatio(npc, attacker);
			hateRatio = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
			npc.getAI().addAttackDesire(attacker, hateRatio);
		}
		
		if (attacker instanceof Playable && npc.getAI().getTopDesireTarget() == attacker)
		{
			if (npc.distance2D(attacker) < 200 && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
			
			if (Rnd.get(100) < 33)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable && called.getAI().getTopDesireTarget() == attacker)
		{
			if (called.distance2D(attacker) < 200 && Rnd.get(100) < 33)
				called.getAI().addCastDesire(called, getNpcSkillByType(called, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
			
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if ((name.equalsIgnoreCase("2001") && (npc._i_ai4 == 1 || npc._i_ai4 == 3)) && !npc.isDead())
		{
			final int heading = npc.getHeading();
			final int silhouette1 = getNpcIntAIParam(npc, "silhouette1");
			final int silhouette2 = getNpcIntAIParam(npc, "silhouette2");
			final int silhouette_ex = getNpcIntAIParam(npc, "silhouette_ex");
			final int silhouette_ex2 = getNpcIntAIParam(npc, "silhouette_ex2");
			
			if (Rnd.get(100) > 50)
			{
				if (Rnd.get(100) < 50)
					createOnePrivateEx(npc, silhouette1, npc.getX(), npc.getY(), npc.getZ(), heading, 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
				else
					createOnePrivateEx(npc, silhouette2, npc.getX(), npc.getY(), npc.getZ(), heading, 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
			}
			else if (ClassId.isSameOccupation((Player) npc._c_ai0, "@fighter"))
				createOnePrivateEx(npc, silhouette_ex, npc.getX(), npc.getY(), npc.getZ(), heading, 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
			else
				createOnePrivateEx(npc, silhouette_ex2, npc.getX(), npc.getY(), npc.getZ(), heading, 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
			
			npc.deleteMe();
		}
		
		return super.onTimer(name, npc, player);
	}
}