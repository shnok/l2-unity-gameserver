package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardFiendArcher;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardFiendArcherSelfBuff extends WizardFiendArcher
{
	public WizardFiendArcherSelfBuff()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardFiendArcher");
	}
	
	public WizardFiendArcherSelfBuff(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
		{
			final L2Skill selfBuff = getNpcSkillByType(npc, NpcSkillType.SELF_BUFF);
			if (getAbnormalLevel(npc, selfBuff) <= 0)
				npc.getAI().addCastDesire(npc, selfBuff, 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
}