package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderPatrolPhysicalSpecialStoneAggressive extends PartyLeaderWarriorAggressive
{
	public PartyLeaderPatrolPhysicalSpecialStoneAggressive()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior");
	}
	
	public PartyLeaderPatrolPhysicalSpecialStoneAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21343,
		21369
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (maybeCastPetrify(npc, attacker))
				return;
			
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated != null && mostHated == attacker)
			{
				final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
					npc.getAI().addCastDesire(attacker, debuff, 1000000);
				
				final L2Skill physicalSpecial = getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL);
				if (Rnd.get(100) < 33)
					npc.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
				
				final L2Skill buff = getNpcSkillByType(npc, NpcSkillType.BUFF);
				if (npc._i_ai1 == 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
					npc.getAI().addCastDesire(npc, buff, 1000000);
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (maybeCastPetrify(called, attacker))
			return;
		
		if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			final L2Skill physicalSpecial = getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL);
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(attacker, physicalSpecial, 1000000);
			
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
				called.getAI().addCastDesire(attacker, debuff, 1000000);
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		if (called.getAI().getLifeTime() > 7 && target instanceof Playable && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
		{
			final L2Skill physicalSpecial = getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL);
			if (Rnd.get(100) < 33)
				called.getAI().addCastDesire(target, physicalSpecial, 1000000);
			
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(target, debuff) <= 0)
				called.getAI().addCastDesire(target, debuff, 1000000);
			
			final L2Skill buff = getNpcSkillByType(called, NpcSkillType.BUFF);
			if (called._i_ai1 == 0 && Rnd.get(100) < 33 && called.getStatus().getHpRatio() > 0.5)
				called.getAI().addCastDesire(caller, buff, 1000000);
		}
		super.onPartyAttacked(caller, called, target, damage);
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