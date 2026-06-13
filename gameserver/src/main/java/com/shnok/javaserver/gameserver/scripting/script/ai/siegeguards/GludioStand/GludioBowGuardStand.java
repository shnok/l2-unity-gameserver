package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioStand;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioBowGuardStand extends GludioStand
{
	public GludioBowGuardStand()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioBowGuardStand(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35012,
		35022,
		35032,
		35042,
		35052,
		35085,
		35088,
		35091,
		35127,
		35130,
		35133,
		35169,
		35172,
		35175,
		35211,
		35214,
		35217,
		35254,
		35257,
		35260,
		35301,
		35304,
		35307,
		35345,
		35348,
		35351,
		35490,
		35493,
		35496,
		35537,
		35540,
		35543
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(npc, attacker) != 2 && attacker instanceof Playable)
			npc.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05 * 100));
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(called, attacker) != 2)
			called.getAI().addAttackDesireHold(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
	}
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill == FrequentSkill.SEAL_OF_RULER.getSkill() && npc.distance2D(caster) < 1000)
			npc.getAI().addAttackDesireHold(caster, 50000);
	}
}