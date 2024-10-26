package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.Summon;
import net.sf.l2j.gameserver.skills.L2Skill;

public class DoormanOfDrain extends Wizard
{
	private static final int PASS_1 = 8064;
	private static final int PASS_2 = 8065;
	private static final int PASS_3 = 8067;
	private static final int SKILL_RANGE = 500;
	
	public DoormanOfDrain()
	{
		super("ai/individual/Monster/WizardBase/Wizard");
	}
	
	public DoormanOfDrain(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18343,
		22136
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		if (attacker instanceof Playable && !npc.getAI().getHateList().isEmpty())
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
			if (topDesireTarget != null)
			{
				if (npc.getCast().meetsHpMpConditions(topDesireTarget, ddMagic))
					npc.getAI().addCastDesire(topDesireTarget, ddMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					if (npc.canAutoAttack(topDesireTarget))
						npc.getAI().addAttackDesire(topDesireTarget, 1000);
				}
			}
			else if (npc.getCast().meetsHpMpConditions(topDesireTarget, ddMagic))
				npc.getAI().addCastDesire(topDesireTarget, ddMagic, 1000000);
			else
			{
				npc._i_ai0 = 1;
				if (npc.canAutoAttack(topDesireTarget))
					npc.getAI().addAttackDesire(topDesireTarget, 1000);
			}
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		super.onClanAttacked(caller, called, attacker, damage, skill);
		
		if (called.getAI().getLifeTime() > 7 && attacker instanceof Playable)
		{
			if (called.getAI().getHateList().isEmpty())
			{
				double f0 = getHateRatio(called, attacker);
				f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
				called.getAI().addAttackDesire(attacker, f0 * 30 + 300);
			}
			else
			{
				double f0 = getHateRatio(called, attacker);
				f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
				called.getAI().addAttackDesire(attacker, f0 * 30);
			}
		}
		
		if (!called.getAI().getHateList().isEmpty())
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			final L2Skill ddMagic = getNpcSkillByType(called, NpcSkillType.DD_MAGIC);
			
			if (topDesireTarget != null)
			{
				if (called.getCast().meetsHpMpConditions(topDesireTarget, ddMagic))
					called.getAI().addCastDesire(topDesireTarget, ddMagic, 1000000);
				else
				{
					called._i_ai0 = 1;
					if (called.canAutoAttack(topDesireTarget))
						called.getAI().addAttackDesire(topDesireTarget, 1000);
				}
			}
			else if (called.getCast().meetsHpMpConditions(attacker, ddMagic))
				called.getAI().addCastDesire(attacker, ddMagic, 1000000);
			else
			{
				called._i_ai0 = 1;
				if (called.canAutoAttack(attacker))
					called.getAI().addAttackDesire(attacker, 1000);
			}
		}
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (creature.isDead() && creature instanceof Summon)
		{
			final Player master = creature.getActingPlayer();
			final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
			
			if (npc.getCast().meetsHpMpConditions(master, ddMagic))
				npc.getAI().addCastDesire(master, ddMagic, 1000000);
			else
			{
				npc._i_ai0 = 1;
				if (npc.canAutoAttack(master))
					npc.getAI().addAttackDesire(master, 1000);
			}
			
			npc.getAI().getHateList().addHateInfo(master, 200);
		}
		else if (npc.distance2D(creature) < getNpcIntAIParamOrDefault(npc, "SkillRange", SKILL_RANGE))
		{
			if (!npc.getAI().getHateList().isEmpty() && npc._i_ai0 != 1)
			{
				final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
				
				if (npc.getCast().meetsHpMpConditions(creature, ddMagic))
					npc.getAI().addCastDesire(creature, ddMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					if (npc.canAutoAttack(creature))
						npc.getAI().addAttackDesire(creature, 1000);
				}
			}
		}
		else
			npc.removeAllDesire();
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player)
		{
			final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
			
			if (creature.getInventory().hasItem(PASS_1) || creature.getInventory().hasItem(PASS_2) || creature.getInventory().hasItem(PASS_3))
			{
				final L2Skill magicHeal = getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL);
				if (magicHeal != null)
					npc.getAI().addCastDesire(creature, magicHeal, 1000000);
				
				npc.removeAttackDesire(creature);
				npc.getAI().getHateList().stopHate(creature);
			}
			else if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory())
			{
				if (npc.getCast().meetsHpMpConditions(creature, ddMagic))
					npc.getAI().addCastDesire(creature, ddMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					if (npc.canAutoAttack(creature))
						npc.getAI().addAttackDesire(creature, 1000);
				}
				
				npc.getAI().getHateList().addHateInfo(creature, 100);
			}
		}
	}
	
	@Override
	public void onMoveToFinished(Npc npc, int x, int y, int z)
	{
		if (!npc.getSpawnLocation().equals(x, y, z))
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
		else
			npc.getAI().addDoNothingDesire(40, 30);
	}
	
	@Override
	public void onAttackFinished(Npc npc, Creature target)
	{
		if (target.isDead() && target instanceof Summon)
		{
			final Player master = target.getActingPlayer();
			final L2Skill ddMagic = getNpcSkillByType(npc, NpcSkillType.DD_MAGIC);
			
			if (npc.getCast().meetsHpMpConditions(master, ddMagic))
				npc.getAI().addCastDesire(master, ddMagic, 1000000);
			else
			{
				npc._i_ai0 = 1;
				if (npc.canAutoAttack(master))
					npc.getAI().addAttackDesire(master, 1000);
			}
			
			npc.getAI().getHateList().addHateInfo(master, 200);
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (skill.getAggroPoints() > 0 || skill.getPower(npc) > 0 || skill.isOffensive())
		{
			double f0 = getHateRatio(npc, caster);
			npc.getAI().addAttackDesire(caster, f0 * 150);
		}
	}
}
