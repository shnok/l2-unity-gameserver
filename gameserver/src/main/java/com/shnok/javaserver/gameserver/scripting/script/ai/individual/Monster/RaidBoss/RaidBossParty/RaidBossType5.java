package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossParty;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RaidBossType5 extends RaidBossParty
{
	public RaidBossType5()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossParty/RaidBossType5");
	}
	
	public RaidBossType5(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25109,
		25322,
		25346,
		25220,
		25296,
		25302,
		25283,
		25328,
		25514,
		25001,
		25032,
		25060,
		25070,
		25082,
		25089,
		25095,
		25112,
		25119,
		25128,
		25137,
		25155,
		25179,
		25189,
		25199,
		25208,
		25214,
		25223,
		25249,
		25263,
		25273,
		25352,
		25360,
		25373,
		25378,
		25383,
		25392,
		25401,
		25404,
		25407,
		25410,
		25418,
		25426,
		25429,
		25434,
		25438,
		25460,
		25470,
		25473,
		25484,
		25487,
		25496,
		29033,
		25506,
		25504
	};
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		L2Skill rangeHold_a = getNpcSkillByType(called, NpcSkillType.RANGE_HOLD_A);
		if ((target instanceof Playable && getAbnormalLevel(target, rangeHold_a) == -1) && Rnd.get((10 * 15)) < 1)
		{
			called.getAI().addCastDesire(target, rangeHold_a, 1000000);
		}
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		L2Skill rangeHold_a = getNpcSkillByType(npc, NpcSkillType.RANGE_HOLD_A);
		if (getAbnormalLevel(caster, rangeHold_a) == -1 && Rnd.get((10 * 15)) < 1)
		{
			npc.getAI().addCastDesire(caster, rangeHold_a, 1000000);
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}
