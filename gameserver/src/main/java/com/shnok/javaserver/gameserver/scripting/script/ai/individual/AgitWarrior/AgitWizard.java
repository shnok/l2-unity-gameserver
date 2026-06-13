package com.shnok.javaserver.gameserver.scripting.script.ai.individual.AgitWarrior;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AgitWizard extends AgitWarrior
{
	private static final L2Skill NPC_AURA_BURN = SkillTable.getInstance().getInfo(4077, 6);
	
	public AgitWizard()
	{
		super("ai/individual/AgitWarrior");
	}
	
	public AgitWizard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35430,
		35620
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Player player = attacker.getActingPlayer();
		if (player != null && (player.getClanId() != npc.getClanId() || player.getClanId() == 0))
		{
			npc.getAI().addAttackDesire(attacker, ((((double) damage) / npc.getStatus().getMaxHp()) / 0.05) * (attacker instanceof Player ? 100 : 10));
			
			if (Rnd.get(100) < 100)
				npc.getAI().addCastDesire(attacker, NPC_AURA_BURN, 1000000);
		}
	}
}