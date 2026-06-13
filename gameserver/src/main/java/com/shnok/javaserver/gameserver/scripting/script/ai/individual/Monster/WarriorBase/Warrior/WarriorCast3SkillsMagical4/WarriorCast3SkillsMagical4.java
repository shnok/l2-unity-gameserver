package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCast3SkillsMagical4;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCast3SkillsMagical4 extends Warrior
{
	public WarriorCast3SkillsMagical4()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCast3SkillsMagical4");
	}
	
	public WarriorCast3SkillsMagical4(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21197,
		21200,
		21203
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final double hpRatio = npc.getStatus().getHpRatio();
		if (Rnd.get(100) < 33 && hpRatio < 0.7)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL), 1000000);
		
		if (attacker instanceof Playable)
		{
			if (npc._i_ai1 == 0 && Rnd.get(100) < 33 && hpRatio > 0.5)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF), 1000000);
			else if (Rnd.get(200) < 1 && npc._i_ai2 == 0 && hpRatio > 0.6)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated != null)
				{
					final L2Skill checkMagic = getNpcSkillByType(npc, NpcSkillType.CHECK_MAGIC);
					final L2Skill checkMagic1 = getNpcSkillByType(npc, NpcSkillType.CHECK_MAGIC1);
					final L2Skill checkMagic2 = getNpcSkillByType(npc, NpcSkillType.CHECK_MAGIC2);
					
					if ((checkMagic == null || getAbnormalLevel(attacker, checkMagic) <= 0) && ((checkMagic1 == null || getAbnormalLevel(attacker, checkMagic1) <= 0) && (checkMagic2 == null || getAbnormalLevel(attacker, checkMagic2) <= 0)))
					{
						npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.CANCEL_MAGIC), 1000000);
						npc._i_ai2 = 1;
					}
				}
			}
			npc._i_ai1 = 1;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			called.getAI().addCastDesire(caller, getNpcSkillByType(called, NpcSkillType.MAGIC_HEAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
