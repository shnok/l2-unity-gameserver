package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsMagical;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsMagical extends Warrior
{
	public WarriorCast3SkillsMagical()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsMagical");
	}
	
	public WarriorCast3SkillsMagical(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20610,
		20617,
		21176,
		21179,
		21182,
		21185,
		20797,
		21003,
		20658,
		20639,
		20802,
		20672
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == null)
			{
				super.onAttacked(npc, attacker, damage, skill);
				return;
			}
			
			if (mostHated != attacker)
			{
				if (npc._i_ai0 == 0)
					npc._i_ai0 = 1;
				else if (npc._i_ai0 == 1 && Rnd.get(100) < 30 && npc.getStatus().getHpRatio() > 0.1)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SLEEP_MAGIC), 1000000);
			}
			else if (npc.distance2D(attacker) > 100)
			{
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
			}
			else if (npc._i_ai1 == 0 && Rnd.get(200) < 1 && npc.getStatus().getHpRatio() > 0.06)
			{
				final L2Skill checkMagic = getNpcSkillByType(npc, NpcSkillType.CHECK_MAGIC);
				final L2Skill checkMagic1 = getNpcSkillByType(npc, NpcSkillType.CHECK_MAGIC1);
				final L2Skill checkMagic2 = getNpcSkillByType(npc, NpcSkillType.CHECK_MAGIC2);
				final L2Skill cancelMagic = getNpcSkillByType(npc, NpcSkillType.CANCEL_MAGIC);
				final L2Skill sleepMagic = getNpcSkillByType(npc, NpcSkillType.SLEEP_MAGIC);
				
				if ((sleepMagic == null || getAbnormalLevel(attacker, sleepMagic) <= 0) && (checkMagic == null || getAbnormalLevel(attacker, checkMagic) <= 0) && ((checkMagic1 == null || getAbnormalLevel(attacker, checkMagic1) <= 0) && (checkMagic2 == null || getAbnormalLevel(attacker, checkMagic2) <= 0)))
				{
					npc.getAI().addCastDesire(attacker, cancelMagic, 1000000);
					npc._i_ai1 = 1;
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called.distance2D(attacker) > 100 && Rnd.get(100) < 33)
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}