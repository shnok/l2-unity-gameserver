package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastDDMagic;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WarriorCastDDMagicSelfBuff extends WarriorCastDDMagic
{
	public WarriorCastDDMagicSelfBuff()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastDDMagic");
	}
	
	public WarriorCastDDMagicSelfBuff(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21405
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
		
		npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
			{
				L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
				
				if (getAbnormalLevel(npc, selfBuff) <= 0)
				{
					npc.getAI().addCastDesire(npc, selfBuff, 1000000);
				}
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}
