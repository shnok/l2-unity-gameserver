package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyLeaderWizard;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartyLeaderWizardDD2Summon extends PartyLeaderWizardDD2
{
	public PartyLeaderWizardDD2Summon()
	{
		super("ai/individual/Monster/WizardBase/PartyLeaderWizard");
	}
	
	public PartyLeaderWizardDD2Summon(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22033,
		22035,
		22041,
		22043
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (npc._i_ai1 == 0)
		{
			int i1 = (Rnd.get(50) - 25);
			int i2 = (Rnd.get(50) - 25);
			createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette"), npc.getX() + i1, npc.getY() + i2, npc.getZ(), 0, 0, true, 1000, attacker.getObjectId(), 0);
			i1 = (Rnd.get(100) - 50);
			i2 = (Rnd.get(100) - 50);
			createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette2"), npc.getX() + i1, npc.getY() + i2, npc.getZ(), 0, 0, true, 1000, attacker.getObjectId(), 0);
			i1 = (Rnd.get(100) - 50);
			i2 = (Rnd.get(100) - 50);
			createOnePrivateEx(npc, getNpcIntAIParam(npc, "silhouette3"), npc.getX() + i1, npc.getY() + i2, npc.getZ(), 0, 0, true, 1000, attacker.getObjectId(), 0);
			npc._i_ai1 = 1;
		}
	}
}
