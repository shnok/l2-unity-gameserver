package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2.WizardCorpseNecro;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2.WizardDDMagic2;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardCorpseNecro extends WizardDDMagic2
{
	public WizardCorpseNecro()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2/WizardCorpseNecro");
	}
	
	public WizardCorpseNecro(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21565,
		21580,
		21584,
		21558
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null)
			{
				final double hpRatio = npc.getStatus().getHpRatio();
				if (hpRatio > 0.9 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
				{
					final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
					npc.getAI().addCastDesire(attacker, debuff, 1000000);
					
					npc._c_ai0 = attacker;
				}
				
				if (topDesireTarget == attacker && hpRatio < 0.4 && Rnd.get(100) < 33)
				{
					final L2Skill DDMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
					npc.getAI().addCastDesire(attacker, DDMagic, 1000000);
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (npc._c_ai0 != null && npc.distance2D(npc._c_ai0) < 100)
		{
			final L2Skill cancel = getNpcSkillByType(npc, NpcSkillType.CANCEL);
			if (cancel != null)
				cancel.getEffects(npc._c_ai0, npc._c_ai0);
		}
	}
}