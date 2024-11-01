package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.LV3Monster;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class LV3SongDance extends LV3Monster
{
	public LV3SongDance()
	{
		super("ai/individual/Monster/LV3Monster");
	}
	
	public LV3SongDance(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27269,
		27270,
		27272,
		27288
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			npc.getAI().addAttackDesire(attacker, ((1.000000 * damage) / (npc.getStatus().getLevel() + 7)) * 100);
		}
		
		if (npc._i_ai0 == 0 && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF1), 1000000);
		
		if (npc._i_ai1 == 0 && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF2), 1000000);
		
		if (npc._i_ai2 == 0 && Rnd.get(100) < 33)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DEBUFF3), 1000000);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == getNpcSkillByType(npc, NpcSkillType.DEBUFF1))
			npc._i_ai0 = 1;
		
		if (skill == getNpcSkillByType(npc, NpcSkillType.DEBUFF2))
			npc._i_ai1 = 1;
		
		if (skill == getNpcSkillByType(npc, NpcSkillType.DEBUFF3))
			npc._i_ai2 = 1;
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster != null && caster.getObjectId() == npc._param2)
		{
			if (skill.getEffectId() == getNpcSkillByType(npc, NpcSkillType.DEBUFF1).getEffectId())
				npc._i_ai0 = 0;
			
			if (skill.getEffectId() == getNpcSkillByType(npc, NpcSkillType.DEBUFF2).getEffectId())
				npc._i_ai1 = 0;
			
			if (skill.getEffectId() == getNpcSkillByType(npc, NpcSkillType.DEBUFF3).getEffectId())
				npc._i_ai2 = 0;
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
}