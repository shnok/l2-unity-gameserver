package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorBerserkerPhysicalSpecialStoneAggressive extends WarriorBerserkerPhysicalSpecialAggressive
{
	public WarriorBerserkerPhysicalSpecialStoneAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public WarriorBerserkerPhysicalSpecialStoneAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21332,
		21336,
		21339,
		21340,
		21327,
		21365,
		21366,
		21353,
		21362,
		21358
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (maybeCastPetrify(npc, attacker))
			return;
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (maybeCastPetrify(called, attacker))
			return;
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		final Player player = creature.getActingPlayer();
		if (player == null)
			return;
		
		if (player.isAlliedWithVarka() && ArraysUtil.contains(npc.getTemplate().getClans(), "varka_silenos_clan"))
			return;
		
		if (player.isAlliedWithKetra() && ArraysUtil.contains(npc.getTemplate().getClans(), "ketra_orc_clan"))
			return;
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0 && !skill.isOffensive() && maybeCastPetrify(npc, caster))
			return;
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}