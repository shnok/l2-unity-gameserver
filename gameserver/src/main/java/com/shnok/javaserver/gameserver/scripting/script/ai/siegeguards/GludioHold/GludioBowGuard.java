package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioBowGuard extends GludioHold
{
	public GludioBowGuard()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioBowGuard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31401,
		35017,
		35027,
		35037,
		35047,
		35057,
		35072,
		35075,
		35078,
		35114,
		35117,
		35120,
		35156,
		35159,
		35162,
		35198,
		35201,
		35204,
		35241,
		35244,
		35247,
		35288,
		35291,
		35294,
		35332,
		35335,
		35338,
		35477,
		35480,
		35483,
		35524,
		35527,
		35530
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(npc, attacker) != 2 && attacker instanceof Playable)
			npc.getAI().addAttackDesire(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05 * 100));
		
		if (npc.isInsideZone(ZoneId.PEACE))
		{
			npc.teleportTo(npc.getSpawnLocation(), 0);
			npc.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (getPledgeCastleState(called, attacker) != 2)
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05 * 50));
	}
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill == FrequentSkill.SEAL_OF_RULER.getSkill())
			npc.getAI().addAttackDesire(caster, 50000);
	}
}
