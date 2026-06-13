package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushWarriorPhysicalSpecial extends WarriorPhysicalSpecial
{
	public RoyalRushWarriorPhysicalSpecial()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public RoyalRushWarriorPhysicalSpecial(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18133,
		18136,
		18148,
		18166,
		18169,
		18187,
		18190,
		18222,
		18225,
		21418
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
		if (selfBuff != null)
		{
			npc.getAI().addCastDesire(npc, selfBuff, 1000000);
			startQuestTimerAtFixedRate("3000", npc, null, 120000, 120000);
		}
		
		npc._i_ai0 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final int weaponID = getNpcIntAIParam(npc, "WeaponID");
		if (npc.getStatus().getHpRatio() < 0.5 && weaponID != 0)
		{
			npc.equipItem(weaponID, 0);
			npc.setEnchantEffect(15);
			npc._i_ai0 = 1;
		}
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				if (Rnd.get(100) < 33 && topDesireTarget == attacker && npc.distance2D(attacker) < 100)
				{
					if (npc._i_ai0 == 0)
					{
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
					}
					else
					{
						if (Rnd.get(100) < 20)
						{
							npc.rechargeShots(true, false);
						}
					}
				}
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if ((called.getAI().getLifeTime() > 7 && attacker instanceof Playable) && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33 && called.distance2D(attacker) > 100)
			{
				if (called._i_ai0 == 0)
				{
					called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
				}
				else
				{
					if (Rnd.get(100) < 20)
					{
						called.rechargeShots(true, false);
					}
				}
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3000"))
		{
			final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
			if (getAbnormalLevel(npc, selfBuff) <= 0)
				npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 1234)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 != null)
			{
				if (Rnd.get(100) < 80)
				{
					npc.getAI().addAttackDesire(c0, 300);
				}
				else
				{
					npc.removeAllAttackDesire();
					npc.getAI().addAttackDesire(c0, 1000);
				}
			}
		}
	}
}
