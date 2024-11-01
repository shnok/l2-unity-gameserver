package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.PartyLeaderWizard;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderWizardCastRangeCurseStoneAggressive extends PartyLeaderWizardDD2
{
	public PartyLeaderWizardCastRangeCurseStoneAggressive()
	{
		super("ai/individual/Monster/WizardBase/PartyLeaderWizard");
	}
	
	public PartyLeaderWizardCastRangeCurseStoneAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21347,
		21373
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (maybeCastPetrify(npc, attacker))
				return;
			
			if (npc._i_ai3 == 0 && npc.getStatus().getHpRatio() > 0.5)
			{
				final L2Skill buff1 = getNpcSkillByType(npc, NpcSkillType.BUFF1);
				npc.getAI().addCastDesire(npc, buff1, 9999999);
				
				final L2Skill buff2 = getNpcSkillByType(npc, NpcSkillType.BUFF2);
				npc.getAI().addCastDesire(npc, buff2, 9999999);
				
				npc._i_ai3 = 1;
			}
			
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && topDesireTarget != attacker)
			{
				final L2Skill rangeDebuff = getNpcSkillByType(npc, NpcSkillType.RANGE_DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, rangeDebuff) <= 0)
				{
					if (npc.getCast().meetsHpMpConditions(attacker, rangeDebuff))
						npc.getAI().addCastDesire(npc, rangeDebuff, 9999999);
					else
					{
						npc._i_ai0 = 1;
						
						npc.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			if (maybeCastPetrify(called, attacker))
				return;
			
			if (called.getAI().getLifeTime() > 7)
			{
				if (called._i_ai3 == 0 && called.getStatus().getHpRatio() > 0.5)
				{
					final L2Skill buff1 = getNpcSkillByType(called, NpcSkillType.BUFF1);
					called.getAI().addCastDesire(called, buff1, 9999999);
					
					final L2Skill buff2 = getNpcSkillByType(called, NpcSkillType.BUFF2);
					called.getAI().addCastDesire(called, buff2, 9999999);
					
					called._i_ai3 = 1;
				}
				
				final L2Skill rangeDebuff = getNpcSkillByType(called, NpcSkillType.RANGE_DEBUFF);
				if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, rangeDebuff) <= 0)
				{
					if (called.getCast().meetsHpMpConditions(attacker, rangeDebuff))
						called.getAI().addCastDesire(called, rangeDebuff, 9999999);
					else
					{
						called._i_ai0 = 1;
						
						called.getAI().addAttackDesire(attacker, 1000);
					}
				}
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		
		super.onCreated(npc);
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
		
		tryToAttack(npc, creature);
		
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