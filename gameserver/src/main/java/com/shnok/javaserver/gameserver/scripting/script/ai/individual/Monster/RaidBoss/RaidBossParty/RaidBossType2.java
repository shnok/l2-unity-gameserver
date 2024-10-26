package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossParty;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RaidBossType2 extends RaidBossParty
{
	public RaidBossType2()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossParty/RaidBossType2");
	}
	
	public RaidBossType2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25286,
		25524,
		25339,
		25202,
		25051,
		25312,
		25007,
		25016,
		25020,
		25029,
		25044,
		25073,
		25079,
		25099,
		25134,
		25143,
		25159,
		25166,
		25173,
		25182,
		25226,
		25235,
		25238,
		25277,
		25357,
		25366,
		25375,
		25395,
		25431,
		25447,
		25456,
		25463,
		25481,
		29030
	};
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if ((target instanceof Playable) && Rnd.get(150) < 1)
		{
			final L2Skill rangeDDMagic_a = getNpcSkillByType(called, NpcSkillType.RANGE_DD_MAGIC_A);
			called.getAI().addCastDesire(target, rangeDDMagic_a, 1000000);
		}
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (Rnd.get(150) < 1)
		{
			final L2Skill rangeDDMagic_a = getNpcSkillByType(npc, NpcSkillType.RANGE_DD_MAGIC_A);
			npc.getAI().addCastDesire(caster, rangeDDMagic_a, 1000000);
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}