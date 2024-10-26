package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioWizardStand extends DefaultNpc
{
	public GludioWizardStand()
	{
		super("ai/siegeguards");
	}
	
	public GludioWizardStand(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35014,
		35024,
		35034,
		35044,
		35054,
		35080,
		35122,
		35164,
		35206,
		35249,
		35296,
		35340,
		35485,
		35532
	};
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (getPledgeCastleState(npc, creature) != 2 && creature instanceof Playable)
			npc.getAI().addCastDesireHold(creature, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(called, attacker) != 2 && attacker instanceof Playable && Rnd.get(100) < 10)
			called.getAI().addCastDesireHold(attacker, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
	}
}
