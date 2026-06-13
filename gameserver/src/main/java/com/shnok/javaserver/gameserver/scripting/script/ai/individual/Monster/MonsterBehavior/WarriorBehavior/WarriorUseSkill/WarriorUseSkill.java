package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterBehavior.WarriorBehavior.WarriorUseSkill;

import java.util.stream.IntStream;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterBehavior.WarriorBehavior.WarriorBehavior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorUseSkill extends WarriorBehavior
{
	public WarriorUseSkill()
	{
		super("ai/WarriorBehavior/WarriorUseSkill");
	}
	
	public WarriorUseSkill(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20136,
		20214,
		20215,
		20217,
		20218,
		20219,
		20222,
		20751,
		20752,
		20753,
		20754,
		21035,
		21037
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
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
			final int skillProbablity = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Probablity", 3333);
			final int skillTarget = getNpcIntAIParam(npc, "Skill0" + i + "_Target");
			final int skillType = getNpcIntAIParam(npc, "Skill0" + i + "_Type");
			final int skillDesire = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Desire", 1000000000);
			final int skillCheckDist = getNpcIntAIParam(npc, "Skill0" + i + "_Check_Dist");
			final int skillDistMin = getNpcIntAIParam(npc, "Skill0" + i + "_Dist_Min");
			final int skillDistMax = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Max", 2000);
			final int skillHPTarget = getNpcIntAIParam(npc, "Skill0" + i + "_HPTarget");
			final int skillHighHP = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_HighHP", 100);
			final int skillLowHP = getNpcIntAIParam(npc, "Skill0" + i + "_LowHP");
			final int skillMainAttack = getNpcIntAIParam(npc, "Skill0" + i + "_MainAttack");
			
			if (skillID != null && skillProbablity > 0)
			{
				if (skillTarget == 0 && topDesireTarget instanceof Playable)
					warriorUseSkill(npc, topDesireTarget, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 1 && attacker instanceof Playable)
					warriorUseSkill(npc, attacker, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 2 || skillTarget == 3)
					warriorUseSkill(npc, npc, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
			}
		});
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() < getNpcIntAIParam(called, "Aggressive_Time"))
		{
			super.onClanAttacked(caller, called, attacker, damage, skill);
			return;
		}
		
		final Creature topDesireTarget = called.getAI().getTopDesireTarget();
		
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
		
		final int partyType = getNpcIntAIParam(called, "Party_Type");
		final int partyLoyalty = getNpcIntAIParam(called, "Party_Loyalty");
		
		IntStream.range(1, 7).forEach(i ->
		{
			if (i == 1 && getNpcIntAIParam(called, "IsHealer") == 1)
				return;
			
			final L2Skill skillID = skillList[i - 1];
			final int skillProbablity = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Probablity", 3333);
			final int skillTarget = getNpcIntAIParam(called, "Skill0" + i + "_Target");
			final int skillType = getNpcIntAIParam(called, "Skill0" + i + "_Type");
			final int skillDesire = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Desire", 1000000000);
			final int skillCheckDist = getNpcIntAIParam(called, "Skill0" + i + "_Check_Dist");
			final int skillDistMin = getNpcIntAIParam(called, "Skill0" + i + "_Dist_Min");
			final int skillDistMax = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Dist_Max", 2000);
			final int skillHPTarget = getNpcIntAIParam(called, "Skill0" + i + "_HPTarget");
			final int skillHighHP = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_HighHP", 100);
			final int skillLowHP = getNpcIntAIParam(called, "Skill0" + i + "_LowHP");
			final int skillMainAttack = getNpcIntAIParam(called, "Skill0" + i + "_MainAttack");
			
			if (skillID != null && skillProbablity > 0)
			{
				if (skillTarget == 0 && topDesireTarget instanceof Playable)
					warriorUseSkill(called, topDesireTarget, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 1 && attacker instanceof Playable)
					warriorUseSkill(called, attacker, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 2 && (partyType == 0 || (partyType == 1 && partyLoyalty == 0) || partyType == 2))
					warriorUseSkill(called, caller, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 3)
					warriorUseSkill(called, called, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 4 && (partyType == 0 || (partyType == 1 && partyLoyalty == 0) || partyType == 2))
					warriorUseSkill(called, caller, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
			}
		});
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		final int partyType = getNpcIntAIParam(called, "Party_Type");
		
		if (partyType == 0)
		{
			super.onPartyAttacked(caller, called, target, damage);
			return;
		}
		
		final Creature topDesireTarget = called.getAI().getTopDesireTarget();
		
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
		
		final int partyLoyalty = getNpcIntAIParam(called, "Party_Loyalty");
		
		IntStream.range(1, 7).forEach(i ->
		{
			if (i == 1 && getNpcIntAIParam(called, "IsHealer") == 1)
				return;
			
			final L2Skill skillID = skillList[i - 1];
			final int skillProbablity = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Probablity", 3333);
			final int skillTarget = getNpcIntAIParam(called, "Skill0" + i + "_Target");
			final int skillType = getNpcIntAIParam(called, "Skill0" + i + "_Type");
			final int skillDesire = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Desire", 1000000000);
			final int skillCheckDist = getNpcIntAIParam(called, "Skill0" + i + "_Check_Dist");
			final int skillDistMin = getNpcIntAIParam(called, "Skill0" + i + "_Dist_Min");
			final int skillDistMax = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_Dist_Max", 2000);
			final int skillHPTarget = getNpcIntAIParam(called, "Skill0" + i + "_HPTarget");
			final int skillHighHP = getNpcIntAIParamOrDefault(called, "Skill0" + i + "_HighHP", 100);
			final int skillLowHP = getNpcIntAIParam(called, "Skill0" + i + "_LowHP");
			final int skillMainAttack = getNpcIntAIParam(called, "Skill0" + i + "_MainAttack");
			
			if (skillID != null && skillProbablity > 0)
			{
				if (skillTarget == 0 && topDesireTarget instanceof Playable)
					warriorUseSkill(called, topDesireTarget, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 1 && target instanceof Playable)
					warriorUseSkill(called, target, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 2)
				{
					if ((partyType == 1 && (partyLoyalty == 0 || partyLoyalty == 1)) || (partyType == 1 && partyLoyalty == 2 && caller == called.getMaster()) || (partyType == 2 && caller != called.getMaster()))
						warriorUseSkill(caller, target, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				}
				else if (skillTarget == 3)
					warriorUseSkill(called, target, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 4)
					if ((partyType == 1 && (partyLoyalty == 0 || partyLoyalty == 1)) || (partyType == 1 && partyLoyalty == 2 && caller == called.getMaster()) || (partyType == 2 && caller != called.getMaster()))
						warriorUseSkill(caller, target, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
			}
		});
		
		super.onPartyAttacked(caller, called, target, damage);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (getNpcIntAIParam(npc, "IsAggressive") == 0 || npc.getAI().getLifeTime() < getNpcIntAIParam(npc, "Aggressive_Time"))
		{
			super.onSeeCreature(npc, creature);
			return;
		}
		
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
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
			final int skillProbablity = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Probablity", 3333);
			final int skillTarget = getNpcIntAIParam(npc, "Skill0" + i + "_Target");
			final int skillType = getNpcIntAIParam(npc, "Skill0" + i + "_Type");
			final int skillDesire = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Desire", 1000000000);
			final int skillCheckDist = getNpcIntAIParam(npc, "Skill0" + i + "_Check_Dist");
			final int skillDistMin = getNpcIntAIParam(npc, "Skill0" + i + "_Dist_Min");
			final int skillDistMax = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Max", 2000);
			final int skillHPTarget = getNpcIntAIParam(npc, "Skill0" + i + "_HPTarget");
			final int skillHighHP = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_HighHP", 100);
			final int skillLowHP = getNpcIntAIParam(npc, "Skill0" + i + "_LowHP");
			final int skillMainAttack = getNpcIntAIParam(npc, "Skill0" + i + "_MainAttack");
			
			if (skillID != null && skillProbablity > 0)
			{
				if (skillTarget == 0 && topDesireTarget instanceof Playable)
					warriorUseSkill(npc, topDesireTarget, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 1 && creature instanceof Playable)
					warriorUseSkill(npc, creature, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 2 || skillTarget == 3)
					warriorUseSkill(npc, npc, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
			}
		});
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
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
			final int skillProbablity = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Probablity", 3333);
			final int skillTarget = getNpcIntAIParam(npc, "Skill0" + i + "_Target");
			final int skillType = getNpcIntAIParam(npc, "Skill0" + i + "_Type");
			final int skillDesire = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Desire", 1000000000);
			final int skillCheckDist = getNpcIntAIParam(npc, "Skill0" + i + "_Check_Dist");
			final int skillDistMin = getNpcIntAIParam(npc, "Skill0" + i + "_Dist_Min");
			final int skillDistMax = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_Dist_Max", 2000);
			final int skillHPTarget = getNpcIntAIParam(npc, "Skill0" + i + "_HPTarget");
			final int skillHighHP = getNpcIntAIParamOrDefault(npc, "Skill0" + i + "_HighHP", 100);
			final int skillLowHP = getNpcIntAIParam(npc, "Skill0" + i + "_LowHP");
			final int skillMainAttack = getNpcIntAIParam(npc, "Skill0" + i + "_MainAttack");
			
			if (skillID != null && skillProbablity > 0)
			{
				if (skillTarget == 0 && topDesireTarget instanceof Playable)
					warriorUseSkill(npc, topDesireTarget, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 1)
					warriorUseSkill(npc, caster, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
				else if (skillTarget == 2 || skillTarget == 3)
					warriorUseSkill(npc, npc, skillID, skillProbablity, skillType, skillDesire, skillCheckDist, skillDistMin, skillDistMax, skillHPTarget, skillHighHP, skillLowHP, skillMainAttack);
			}
		});
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	// EventHandler DESIRE_MANIPULATION(speller, desire)
	// {
	// onAttacked(npc, spelled, 200, skill);
	// super;
	// }
	
	private static void warriorUseSkill(Npc npc, Creature creature, L2Skill skill, int skillProbability, int skillType, int desire, int checkDist, int distMin, int distMax, int hpTarget, int highHP, int lowHP, int mainAttack)
	{
		if (skill.isMagic() && npc.isMuted() || !skill.isMagic() && npc.isPhysicalMuted())
			return;
		
		if (desire <= 0.0)
			return;
		
		if (Rnd.get(10000) < skillProbability)
		{
			final double hpPercent = npc.getStatus().getHpRatio() * 100;
			if ((hpTarget == 0 && (hpPercent > highHP || hpPercent < lowHP)) || (hpTarget == 1 && (hpPercent > highHP || hpPercent < lowHP)))
				return;
			
			final double dist = npc.distance2D(creature);
			if (checkDist == 1 && (dist < distMin || dist >= distMax))
				return;
			
			if (((checkDist == 1 && creature == npc && (skillType == 0 || skillType == 2)) && creature != npc) && (dist < distMin || dist >= distMax))
				return;
			
			if (skillType == 0 || ((skillType == 1 || skillType == 2) && getAbnormalLevel(creature, skill) <= 0) || skillType == 3)
			{
				final int movingAttack = getNpcIntAIParamOrDefault(npc, "MovingAttack", 1);
				if (movingAttack == 1)
					npc.getAI().addCastDesire(creature, skill, desire);
				else if (movingAttack == 0)
					npc.getAI().addCastDesireHold(creature, skill, desire);
			}
		}
	}
}