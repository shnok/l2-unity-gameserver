package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterBehavior.WizardBehavior.WizardUseSkill;

import java.util.stream.IntStream;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterBehavior.WizardBehavior.WizardBehavior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WizardUseSkill extends WizardBehavior
{
	private static final int[] PROBABILITIES =
	{
		10000,
		10000,
		3333,
		3333,
		3333,
		3333
	};
	private static final int[] CHECK_DISTS =
	{
		1,
		1,
		0,
		0,
		0,
		0
	};
	private static final int[] MIN_DISTS =
	{
		300,
		0,
		0,
		0,
		0,
		0
	};
	private static final int[] MAX_DISTS =
	{
		2000,
		300,
		2000,
		2000,
		2000,
		2000
	};
	private static final int[] MAIN_ATTACKS =
	{
		1,
		1,
		0,
		0,
		0,
		0
	};
	
	public WizardUseSkill()
	{
		super("ai/WizardBehavior/WizardUseSkill");
	}
	
	public WizardUseSkill(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20216,
		20220,
		21036,
		21038,
		21039,
		21040,
		21394,
		21395
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		super.onAttacked(npc, attacker, damage, skill);
		
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		
		if (npc._i_ai4 == 0)
		{
			final L2Skill Skill01_ID = getNpcSkillByType(npc, NpcSkillType.SKILL01_ID);
			final L2Skill Skill02_ID = getNpcSkillByType(npc, NpcSkillType.SKILL02_ID);
			final L2Skill Skill03_ID = getNpcSkillByType(npc, NpcSkillType.SKILL03_ID);
			final L2Skill Skill04_ID = getNpcSkillByType(npc, NpcSkillType.SKILL04_ID);
			final L2Skill Skill05_ID = getNpcSkillByType(npc, NpcSkillType.SKILL05_ID);
			final L2Skill Skill06_ID = getNpcSkillByType(npc, NpcSkillType.SKILL06_ID);
			
			final L2Skill[] skillList =
			{
				Skill01_ID,
				Skill02_ID,
				Skill03_ID,
				Skill04_ID,
				Skill05_ID,
				Skill06_ID
			};
			
			IntStream.range(1, 7).forEach(i ->
			{
				if (i == 1 && getNpcIntAIParam(npc, "IsHealer") == 1)
					return;
				
				final L2Skill skillID = skillList[i - 1];
				final int skillProbablity = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Probablity", PROBABILITIES[i - 1]);
				final int skillTarget = getNpcIntAIParam(npc, "Skill0" + i + "_Target");
				final int skillType = getNpcIntAIParam(npc, "Skill0" + i + "_Type");
				final int skillDesire = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Desire", 1000000000);
				final int skillCheckDist = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Check_Dist", CHECK_DISTS[i - 1]);
				final int skillDistMin = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Min", MIN_DISTS[i - 1]);
				final int skillDistMax = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Max", MAX_DISTS[i - 1]);
				final int skillHPTarget = getNpcIntAIParam(npc, "Skill0" + i + "_HPTarget");
				final int skillHighHP = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_HighHP", 100);
				final int skillLowHP = getNpcIntAIParam(npc, "Skill0" + i + "_LowHP");
				final int skillMainAttack = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_MainAttack", MAIN_ATTACKS[i - 1]);
				
				if (attacker instanceof Playable && skillID != null && skillProbablity > 0)
				{
					if (skillTarget == 0 && mostHatedHI instanceof Playable)
						wizardUseSkill(npc, mostHatedHI, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 1)
						wizardUseSkill(npc, attacker, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 2 || skillTarget == 3)
						wizardUseSkill(npc, npc, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				}
			});
		}
		else
		{
			if (getNpcIntAIParam(npc, "IsHealer") != 1)
			{
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				
				if (getNpcIntAIParamOrDefault(npc, "MovingAttack", 1) == 1)
				{
					npc.getAI().addAttackDesire(attacker, ATTACKED_WEIGHT_POINT);
					
					if (topDesireTarget != null)
					{
						if (npc.getMove().getGeoPathFailCount() > 10 && attacker == topDesireTarget && npc.getStatus().getHpRatio() < 1)
							npc.teleportTo(attacker.getPosition(), 0);
						
						if (npc.isRooted() && npc.distance2D(topDesireTarget) > 40)
						{
							if (!npc.canAutoAttack(topDesireTarget))
								npc.removeAttackDesire(topDesireTarget);
							
							npc.getAI().addAttackDesire(attacker, ATTACKED_WEIGHT_POINT);
						}
					}
				}
				else
				{
					if (topDesireTarget != null)
					{
						if (!npc.canAutoAttack(topDesireTarget))
							npc.removeAttackDesire(topDesireTarget);
						
						npc.getAI().addAttackDesireHold(attacker, ATTACKED_WEIGHT_POINT);
					}
				}
			}
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		super.onClanAttacked(caller, called, attacker, damage, skill);
		
		final Creature mostHatedHI = called.getAI().getHateList().getMostHatedCreature();
		
		final int partyType = getNpcIntAIParam(called, "Party_Type");
		
		if (called._i_ai4 == 0)
		{
			final L2Skill Skill01_ID = getNpcSkillByType(called, NpcSkillType.SKILL01_ID);
			final L2Skill Skill02_ID = getNpcSkillByType(called, NpcSkillType.SKILL02_ID);
			final L2Skill Skill03_ID = getNpcSkillByType(called, NpcSkillType.SKILL03_ID);
			final L2Skill Skill04_ID = getNpcSkillByType(called, NpcSkillType.SKILL04_ID);
			final L2Skill Skill05_ID = getNpcSkillByType(called, NpcSkillType.SKILL05_ID);
			final L2Skill Skill06_ID = getNpcSkillByType(called, NpcSkillType.SKILL06_ID);
			
			final int partyLoyalty = getNpcIntAIParam(called, "Party_Loyalty");
			
			final L2Skill[] skillList =
			{
				Skill01_ID,
				Skill02_ID,
				Skill03_ID,
				Skill04_ID,
				Skill05_ID,
				Skill06_ID
			};
			
			IntStream.range(1, 7).forEach(i ->
			{
				if (i <= 2 && (getNpcIntAIParam(called, "IsHealer") == 1 || called.getAI().getLifeTime() > getNpcIntAIParam(called, "Aggressive_Time")))
					return;
				
				final L2Skill skillID = skillList[i - 1];
				final int skillProbablity = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Probablity", PROBABILITIES[i - 1]);
				final int skillTarget = getNpcIntAIParam(called, "Skill0" + i + "_Target");
				final int skillType = getNpcIntAIParam(called, "Skill0" + i + "_Type");
				final int skillDesire = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Desire", 1000000000);
				final int skillCheckDist = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Check_Dist", CHECK_DISTS[i - 1]);
				final int skillDistMin = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Dist_Min", MIN_DISTS[i - 1]);
				final int skillDistMax = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Dist_Max", MAX_DISTS[i - 1]);
				final int skillHPTarget = getNpcIntAIParam(called, "Skill0" + i + "_HPTarget");
				final int skillHighHP = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_HighHP", 100);
				final int skillLowHP = getNpcIntAIParam(called, "Skill0" + i + "_LowHP");
				final int skillMainAttack = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_MainAttack", MAIN_ATTACKS[i - 1]);
				
				if (attacker instanceof Playable && skillID != null && skillProbablity > 0)
				{
					if (skillTarget == 0 && mostHatedHI instanceof Playable)
						wizardUseSkill(called, mostHatedHI, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 1)
						wizardUseSkill(called, attacker, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 2 && (partyType == 0 || (partyType == 1 && partyLoyalty == 0) || partyType == 2))
						wizardUseSkill(called, caller, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 3)
						wizardUseSkill(called, called, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 4 && (partyType == 0 || (partyType == 1 && partyLoyalty == 0) || partyType == 2))
						wizardUseSkill(called, caller, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				}
			});
		}
		else
		{
			if (partyType != 0 && called.getAI().getLifeTime() > getNpcIntAIParam(called, "Aggressive_Time") && getNpcIntAIParam(called, "IsHealer") != 1)
			{
				final Creature topDesireTarget = called.getAI().getTopDesireTarget();
				
				if (getNpcIntAIParamOrDefault(called, "MovingAttack", 1) == 1)
				{
					called.getAI().addAttackDesire(attacker, ATTACKED_WEIGHT_POINT);
					
					if (topDesireTarget != null)
					{
						if (called.getMove().getGeoPathFailCount() > 10 && attacker == topDesireTarget && called.getStatus().getHpRatio() < 1)
							called.teleportTo(attacker.getPosition(), 0);
						
						if (called.isRooted() && called.distance2D(topDesireTarget) > 40)
						{
							if (!called.canAutoAttack(topDesireTarget))
								called.removeAttackDesire(topDesireTarget);
							
							called.getAI().addAttackDesire(attacker, ATTACKED_WEIGHT_POINT);
						}
					}
				}
				else
				{
					if (topDesireTarget != null)
					{
						if (!called.canAutoAttack(topDesireTarget))
							called.removeAttackDesire(topDesireTarget);
						
						called.getAI().addAttackDesireHold(attacker, ATTACKED_WEIGHT_POINT);
					}
				}
			}
		}
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		
		super.onPartyAttacked(caller, called, target, damage);
		
		final int partyType = getNpcIntAIParam(called, "Party_Type");
		final int partyLoyalty = getNpcIntAIParam(called, "Party_Loyalty");
		
		if (partyType == 0)
			return;
		
		final Creature mostHatedHI = called.getAI().getHateList().getMostHatedCreature();
		
		if (called._i_ai4 == 0)
		{
			final L2Skill Skill01_ID = getNpcSkillByType(called, NpcSkillType.SKILL01_ID);
			final L2Skill Skill02_ID = getNpcSkillByType(called, NpcSkillType.SKILL02_ID);
			final L2Skill Skill03_ID = getNpcSkillByType(called, NpcSkillType.SKILL03_ID);
			final L2Skill Skill04_ID = getNpcSkillByType(called, NpcSkillType.SKILL04_ID);
			final L2Skill Skill05_ID = getNpcSkillByType(called, NpcSkillType.SKILL05_ID);
			final L2Skill Skill06_ID = getNpcSkillByType(called, NpcSkillType.SKILL06_ID);
			
			final L2Skill[] skillList =
			{
				Skill01_ID,
				Skill02_ID,
				Skill03_ID,
				Skill04_ID,
				Skill05_ID,
				Skill06_ID
			};
			
			IntStream.range(1, 7).forEach(i ->
			{
				if (i <= 2 && (getNpcIntAIParam(called, "IsHealer") == 1 || called.getAI().getLifeTime() > getNpcIntAIParam(called, "Aggressive_Time")))
					return;
				
				final L2Skill skillID = skillList[i - 1];
				final int skillProbablity = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Probablity", PROBABILITIES[i - 1]);
				final int skillTarget = getNpcIntAIParam(called, "Skill0" + i + "_Target");
				final int skillType = getNpcIntAIParam(called, "Skill0" + i + "_Type");
				final int skillDesire = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Desire", 1000000000);
				final int skillCheckDist = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Check_Dist", CHECK_DISTS[i - 1]);
				final int skillDistMin = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Dist_Min", MIN_DISTS[i - 1]);
				final int skillDistMax = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Dist_Max", MAX_DISTS[i - 1]);
				final int skillHPTarget = getNpcIntAIParam(called, "Skill0" + i + "_HPTarget");
				final int skillHighHP = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_HighHP", 100);
				final int skillLowHP = getNpcIntAIParam(called, "Skill0" + i + "_LowHP");
				final int skillMainAttack = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_MainAttack", MAIN_ATTACKS[i - 1]);
				
				if (target instanceof Playable && skillID != null && skillProbablity > 0)
				{
					if (skillTarget == 0 && mostHatedHI instanceof Playable)
						wizardUseSkill(called, mostHatedHI, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 1)
						wizardUseSkill(called, target, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 2 && (partyType == 1 && (partyLoyalty == 0 || partyLoyalty == 1)) || (partyType == 1 && partyLoyalty == 2 && caller == called.getMaster()) || (partyType == 2 && caller != called.getMaster()))
						wizardUseSkill(called, caller, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 3)
						wizardUseSkill(called, called, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 4 && (partyType == 1 && (partyLoyalty == 0 || partyLoyalty == 1)) || (partyType == 1 && partyLoyalty == 2 && caller == called.getMaster()) || (partyType == 2 && caller != called.getMaster()))
						wizardUseSkill(called, caller, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				}
			});
		}
		else
		{
			if (partyLoyalty == 1 || (partyLoyalty == 2 && caller == called.getMaster()) && called.getAI().getLifeTime() > getNpcIntAIParam(called, "Aggressive_Time") && getNpcIntAIParam(called, "IsHealer") != 1)
			{
				final Creature topDesireTarget = called.getAI().getTopDesireTarget();
				
				if (getNpcIntAIParamOrDefault(called, "MovingAttack", 1) == 1)
				{
					called.getAI().addAttackDesire(target, ATTACKED_WEIGHT_POINT);
					
					if (topDesireTarget != null)
					{
						if (called.getMove().getGeoPathFailCount() > 10 && target == topDesireTarget && called.getStatus().getHpRatio() < 1)
							called.teleportTo(target.getPosition(), 0);
						
						if (called.isRooted() && called.distance2D(topDesireTarget) > 40)
						{
							if (!called.canAutoAttack(topDesireTarget))
								called.removeAttackDesire(topDesireTarget);
							
							called.getAI().addAttackDesire(target, ATTACKED_WEIGHT_POINT);
						}
					}
				}
				else
				{
					if (topDesireTarget != null)
					{
						if (!called.canAutoAttack(topDesireTarget))
							called.removeAttackDesire(topDesireTarget);
						
						called.getAI().addAttackDesireHold(target, ATTACKED_WEIGHT_POINT);
					}
				}
			}
		}
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		super.onSeeCreature(npc, creature);
		
		if (!(creature instanceof Playable) || getNpcIntAIParam(npc, "IsAggressive") == 0 || npc.getAI().getLifeTime() < getNpcIntAIParam(npc, "Aggressive_Time"))
			return;
		
		final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
		
		if (npc._i_ai4 == 0)
		{
			final L2Skill Skill01_ID = getNpcSkillByType(npc, NpcSkillType.SKILL01_ID);
			final L2Skill Skill02_ID = getNpcSkillByType(npc, NpcSkillType.SKILL02_ID);
			final L2Skill Skill03_ID = getNpcSkillByType(npc, NpcSkillType.SKILL03_ID);
			final L2Skill Skill04_ID = getNpcSkillByType(npc, NpcSkillType.SKILL04_ID);
			final L2Skill Skill05_ID = getNpcSkillByType(npc, NpcSkillType.SKILL05_ID);
			final L2Skill Skill06_ID = getNpcSkillByType(npc, NpcSkillType.SKILL06_ID);
			
			final L2Skill[] skillList =
			{
				Skill01_ID,
				Skill02_ID,
				Skill03_ID,
				Skill04_ID,
				Skill05_ID,
				Skill06_ID
			};
			
			IntStream.range(1, 7).forEach(i ->
			{
				final L2Skill skillID = skillList[i - 1];
				final int skillProbablity = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Probablity", PROBABILITIES[i - 1]);
				final int skillTarget = getNpcIntAIParam(npc, "Skill0" + i + "_Target");
				final int skillType = getNpcIntAIParam(npc, "Skill0" + i + "_Type");
				final int skillDesire = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Desire", 1000000000);
				final int skillCheckDist = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Check_Dist", CHECK_DISTS[i - 1]);
				final int skillDistMin = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Min", MIN_DISTS[i - 1]);
				final int skillDistMax = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Max", MAX_DISTS[i - 1]);
				final int skillHPTarget = getNpcIntAIParam(npc, "Skill0" + i + "_HPTarget");
				final int skillHighHP = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_HighHP", 100);
				final int skillLowHP = getNpcIntAIParam(npc, "Skill0" + i + "_LowHP");
				final int skillMainAttack = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_MainAttack", MAIN_ATTACKS[i - 1]);
				
				if (creature instanceof Playable && skillID != null && skillProbablity > 0)
				{
					if (skillTarget == 0 && mostHatedHI instanceof Playable)
						wizardUseSkill(npc, mostHatedHI, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 1)
						wizardUseSkill(npc, creature, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
					else if (skillTarget == 2 || skillTarget == 3)
						wizardUseSkill(npc, npc, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				}
			});
		}
		else if (npc.isInMyTerritory())
		{
			
			if (getNpcIntAIParamOrDefault(npc, "MovingAttack", 1) == 1)
				npc.getAI().addAttackDesire(creature, 300);
			else
				npc.getAI().addAttackDesireHold(creature, 300);
			
			super.onSeeCreature(npc, creature);
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (npc._i_ai4 == 0)
		{
			final Creature mostHatedHI = npc.getAI().getHateList().getMostHatedCreature();
			if (mostHatedHI != null)
			{
				final L2Skill Skill01_ID = getNpcSkillByType(npc, NpcSkillType.SKILL01_ID);
				final L2Skill Skill02_ID = getNpcSkillByType(npc, NpcSkillType.SKILL02_ID);
				
				final int skillProbablity1 = getNpcIntAIParamOrDefault(npc, "Skill01_Probablity", PROBABILITIES[0]);
				final int skillTarget1 = getNpcIntAIParam(npc, "Skill01_Target");
				final int skillType1 = getNpcIntAIParam(npc, "Skill01_Type");
				final int skillDesire1 = getNpcIntAIParamOrDefault(npc, "Skill01_Desire", 1000000000);
				final int skillCheckDist1 = getNpcIntAIParamOrDefault(npc, "Skill01_Check_Dist", CHECK_DISTS[0]);
				final int skillDistMin1 = getNpcIntAIParamOrDefault(npc, "Skill01_Dist_Min", MIN_DISTS[0]);
				final int skillDistMax1 = getNpcIntAIParamOrDefault(npc, "Skill01_Dist_Max", MAX_DISTS[0]);
				final int skillHPTarget1 = getNpcIntAIParam(npc, "Skill01_HPTarget");
				final int skillHighHP1 = getNpcIntAIParamOrDefault(npc, "Skill01_HighHP", 100);
				final int skillLowHP1 = getNpcIntAIParam(npc, "Skill01_LowHP");
				final int skillMainAttack1 = getNpcIntAIParamOrDefault(npc, "Skill01_MainAttack", MAIN_ATTACKS[0]);
				
				final int skillProbablity2 = getNpcIntAIParamOrDefault(npc, "Skill02_Probablity", PROBABILITIES[1]);
				final int skillTarget2 = getNpcIntAIParam(npc, "Skill02_Target");
				final int skillType2 = getNpcIntAIParam(npc, "Skill02_Type");
				final int skillDesire2 = getNpcIntAIParamOrDefault(npc, "Skill02_Desire", 1000000000);
				final int skillCheckDist2 = getNpcIntAIParamOrDefault(npc, "Skill02_Check_Dist", CHECK_DISTS[1]);
				final int skillDistMin2 = getNpcIntAIParamOrDefault(npc, "Skill02_Dist_Min", MIN_DISTS[1]);
				final int skillDistMax2 = getNpcIntAIParamOrDefault(npc, "Skill02_Dist_Max", MAX_DISTS[1]);
				final int skillHPTarget2 = getNpcIntAIParam(npc, "Skill02_HPTarget");
				final int skillHighHP2 = getNpcIntAIParamOrDefault(npc, "Skill02_HighHP", 100);
				final int skillLowHP2 = getNpcIntAIParam(npc, "Skill02_LowHP");
				final int skillMainAttack2 = getNpcIntAIParamOrDefault(npc, "Skill02_MainAttack", MAIN_ATTACKS[1]);
				
				if (Skill02_ID != null && skillProbablity2 > 0 && skillCheckDist2 == 1 && npc.distance2D(mostHatedHI) > skillDistMin2 && npc.distance2D(mostHatedHI) < skillDistMax2 && skillMainAttack2 == 1)
				{
					if ((skillTarget2 == 0 || skillTarget2 == 1) && mostHatedHI instanceof Playable)
						wizardUseSkill(npc, mostHatedHI, Skill02_ID, skillProbablity2, skillType2, skillDesire2, skillCheckDist2, skillDistMin2, skillDistMax2, skillHPTarget2, skillHighHP2, skillLowHP2, skillMainAttack2);
					else if (skillTarget2 == 2 || skillTarget2 == 3)
						wizardUseSkill(npc, npc, Skill02_ID, skillProbablity2, skillType2, skillDesire2, skillCheckDist2, skillDistMin2, skillDistMax2, skillHPTarget2, skillHighHP2, skillLowHP2, skillMainAttack2);
				}
				else if (Skill01_ID != null && skillProbablity1 > 0)
				{
					if ((skillTarget1 == 0 || skillTarget1 == 1) && mostHatedHI instanceof Playable)
						wizardUseSkill(npc, mostHatedHI, Skill01_ID, skillProbablity1, skillType1, skillDesire1, skillCheckDist1, skillDistMin1, skillDistMax1, skillHPTarget1, skillHighHP1, skillLowHP1, skillMainAttack1);
					else if (skillTarget1 == 2 || skillTarget1 == 3)
						wizardUseSkill(npc, npc, Skill01_ID, skillProbablity1, skillType1, skillDesire1, skillCheckDist1, skillDistMin1, skillDistMax1, skillHPTarget1, skillHighHP1, skillLowHP1, skillMainAttack1);
				}
			}
		}
	}
	
	private void wizardUseSkill(Npc npc, Creature creature, L2Skill skill, int skillProbability, int skillType, int desire, int checkDist, int distMin, int distMax, int hpTarget, int highHP, int lowHP, int mainAttack)
	{
		if (skill.isMagic() && npc.isMuted() || !skill.isMagic() && npc.isPhysicalMuted())
			return;
		
		if (Rnd.get(10000) < skillProbability)
		{
			if (desire <= 0.0)
				return;
			
			if ((hpTarget == 0 && ((npc.getStatus().getHpRatio() * 100) > highHP || (npc.getStatus().getHpRatio() * 100) < lowHP)) || (hpTarget == 1 && ((npc.getStatus().getHpRatio() * 100) > highHP || (npc.getStatus().getHpRatio() * 100) < lowHP)))
				return;
			
			if (checkDist == 1 && (npc.distance2D(creature) < distMin || npc.distance2D(creature) >= distMax))
				return;
			
			if (((checkDist == 1 && creature == npc && creature != null && (skillType == 0 || skillType == 2)) && creature != npc) && (npc.distance2D(creature) < distMin || npc.distance2D(creature) >= distMax))
				return;
			
			final int movingAttack = getNpcIntAIParamOrDefault(npc, "MovingAttack", 1);
			
			if (skillType == 0)
			{
				if (movingAttack == 1)
				{
					if (mainAttack == 1)
					{
						if (npc.getCast().meetsHpMpConditions(creature, skill))
							npc.getAI().addCastDesire(creature, skill, desire);
						else
						{
							npc._i_ai4 = 1;
							npc.getAI().addAttackDesire(creature, 1000);
							
							startQuestTimer("5001", npc, null, 10000);
						}
					}
					else
						npc.getAI().addCastDesire(creature, skill, desire);
				}
				else if (movingAttack == 0 && mainAttack == 1)
				{
					if (npc.getCast().meetsHpMpConditions(creature, skill))
						npc.getAI().addCastDesireHold(creature, skill, desire);
					else
					{
						npc._i_ai4 = 1;
						npc.getAI().addAttackDesireHold(creature, 1000);
						
						startQuestTimer("5001", npc, null, 10000);
					}
				}
			}
			else if ((skillType == 1 || skillType == 2) && getAbnormalLevel(creature, skill) <= 0)
			{
				if (movingAttack == 1)
					npc.getAI().addCastDesire(creature, skill, desire);
				else if (movingAttack == 0)
					npc.getAI().addCastDesireHold(creature, skill, desire);
			}
			else if (skillType == 3)
			{
				if (movingAttack == 1)
					npc.getAI().addCastDesire(creature, skill, desire);
				else if (movingAttack == 0)
					npc.getAI().addCastDesireHold(creature, skill, desire);
			}
		}
	}
}