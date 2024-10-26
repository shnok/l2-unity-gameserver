package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import net.sf.l2j.commons.random.Rnd;
import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.container.attackable.HateList;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardCastClanBuffCurseStoneAggressive extends WizardDDMagic2
{
	public WizardCastClanBuffCurseStoneAggressive()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardCastClanBuffCurseStoneAggressive(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21338,
		21329,
		21364,
		21355
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (maybeCastPetrify(npc, attacker))
			return;
		
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable && npc._i_ai0 == 0)
		{
			final HateList hateList = npc.getAI().getHateList();
			if (!hateList.isEmpty())
			{
				if (npc._i_ai2 == 0 && Rnd.get(100) < 33 && npc.getStatus().getHpRatio() > 0.5)
				{
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF1), 1000000);
					npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.BUFF2), 1000000);
					
					npc._i_ai2 = 1;
				}
				
				final Creature mostHated = hateList.getMostHatedCreature();
				if (attacker == mostHated)
				{
					final L2Skill debuff = getNpcSkillByType(npc, NpcSkillType.DEBUFF);
					if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
					{
						if (npc.getCast().meetsHpMpConditions(attacker, debuff))
							npc.getAI().addCastDesire(attacker, debuff, 1000000);
						else
						{
							npc._i_ai0 = 1;
							
							npc.getAI().addAttackDesire(attacker, 1000);
						}
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (maybeCastPetrify(called, attacker))
			return;
		
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7)
		{
			if (called._i_ai2 == 0 && Rnd.get(100) < 33 && called.getStatus().getHpRatio() > 0.5)
			{
				final L2Skill buff1 = getNpcSkillByType(called, NpcSkillType.BUFF1);
				called.getAI().addCastDesire(called, buff1, 1000000);
				
				final L2Skill buff2 = getNpcSkillByType(called, NpcSkillType.BUFF2);
				called.getAI().addCastDesire(called, buff2, 1000000);
				
				called._i_ai2 = 1;
			}
			
			final L2Skill debuff = getNpcSkillByType(called, NpcSkillType.DEBUFF);
			if (Rnd.get(100) < 33 && getAbnormalLevel(attacker, debuff) <= 0)
			{
				if (called.getCast().meetsHpMpConditions(attacker, debuff))
					called.getAI().addCastDesire(called, debuff, 1000000);
				else
				{
					called._i_ai0 = 1;
					
					called.getAI().addAttackDesire(attacker, 1000);
				}
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai2 = 0;
		
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