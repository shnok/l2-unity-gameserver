package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagic;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WarriorCastDDMagic extends Warrior
{
	public WarriorCastDDMagic()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagic");
	}
	
	public WarriorCastDDMagic(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20037,
		20172,
		20177,
		21167,
		21170,
		21173,
		21188,
		21191,
		21194,
		21622,
		21797,
		20277,
		20589,
		20595,
		27126,
		20590,
		20786,
		21644,
		20153,
		20244,
		20245,
		20842,
		21620,
		21621,
		20648,
		20678,
		20997,
		20785,
		20501,
		20599
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		
		if (npc._i_ai1 == 1)
		{
			if (mostHated == attacker)
				npc._i_ai2 = 1;
		}
		else
		{
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai1 = 1;
		}
		
		if (attacker instanceof Playable && mostHated == attacker && Rnd.get(100) < 33)
		{
			final L2Skill DDMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
			if (getAbnormalLevel(attacker, DDMagic) <= 0)
				npc.getAI().addCastDesire(attacker, DDMagic, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			if (Rnd.get(100) < 33 && called.distance2D(attacker) > 100)
			{
				final L2Skill DDMagic = getNpcSkillByType(called, NpcSkillType.DD_MAGIC);
				called.getAI().addCastDesire(attacker, DDMagic, 1000000);
			}
		}
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
				
				return super.onTimer(name, npc, player);
			}
			
			if (npc._i_ai2 == 0)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated != null && Rnd.get(100) < 50)
				{
					final L2Skill DDMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
					if (getAbnormalLevel(mostHated, DDMagic) <= 0)
						npc.getAI().addCastDesire(mostHated, DDMagic, 1000000);
				}
			}
			
			startQuestTimer("2001", npc, null, 5000);
			
			npc._i_ai2 = 0;
		}
		
		return super.onTimer(name, npc, player);
	}
}