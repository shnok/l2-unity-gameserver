package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorGrowth;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorGrowthStep2 extends Warrior
{
	public WarriorGrowthStep2()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorGrowth");
	}
	
	public WarriorGrowthStep2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21452,
		21454,
		21456,
		21458,
		21471,
		21473,
		21475,
		21477,
		21490,
		21492,
		21494,
		21496
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = 1;
		npc._c_ai0 = (Creature) World.getInstance().getObject(npc._param1);
		npc._i_ai3 = npc._param2;
		
		if (npc._c_ai0 != null)
			npc.getAI().addAttackDesire(npc._c_ai0, 100);
		
		npc._i_ai2 = 0;
		
		if (Rnd.get(100) < 33)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final int takeSocial = getNpcIntAIParamOrDefault(npc, "TakeSocial", 5);
		int i0 = 0;
		int i1 = 0;
		if (skill != null && skill.getId() == npc._i_ai3)
		{
			if (takeSocial != 0)
			{
				final int moveAroundSocial = getNpcIntAIParam(npc, "MoveAroundSocial");
				npc.getAI().addSocialDesire(1, (moveAroundSocial * 1000) / 30, 200);
			}
			i0 = 1;
		}
		
		if (i0 == 1)
		{
			if (Rnd.get(100) < 5)
			{
				i0 = Rnd.get(5) + 2014;
				npc.broadcastNpcSay(NpcStringId.get(i0));
			}
			
			if (npc._c_ai0 != null && npc._c_ai0 == attacker)
			{
				npc._i_ai2 = (npc._i_ai2 + 1);
				i1 = 0;
				if (npc._i_ai4 == 1 && npc._c_ai0 == attacker)
				{
					if (Rnd.get(100) <= (npc._i_ai2 * 33))
					{
						startQuestTimer("2001", npc, null, (takeSocial * 1000) / 30);
						npc._i_ai4 = 3;
					}
				}
				else if (npc._i_ai4 != 3)
				{
					npc._i_ai4 = 2;
					i1 = 1;
				}
			}
			
			if (i1 == 1 && attacker instanceof Playable)
			{
				double hateRatio = getHateRatio(npc, attacker);
				hateRatio = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
				npc.getAI().addAttackDesire(attacker, hateRatio);
			}
		}
		else if (attacker instanceof Playable && npc.getAI().getTopDesireTarget() == attacker && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getTopDesireTarget() == attacker && Rnd.get(100) < 33)
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
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
			
			if (Rnd.get(100) < 50)
				createOnePrivateEx(npc, silhouette1, npc.getX(), npc.getY(), npc.getZ(), heading, 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
			else
				createOnePrivateEx(npc, silhouette2, npc.getX(), npc.getY(), npc.getZ(), heading, 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
			
			npc.deleteMe();
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10018)
		{
			if (Rnd.get(100) < 20)
			{
				createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette_ex2"), npc.getX(), npc.getY(), npc.getZ(), npc.getHeading(), 0, true, npc._c_ai0.getObjectId(), npc._i_ai3, 0);
				npc.deleteMe();
			}
		}
		
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
}
