package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.TyrannoPrimeval;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class TyrannoPrimeval extends Warrior
{
	public TyrannoPrimeval()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/TyrannoPrimeval");
	}
	
	public TyrannoPrimeval(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22215, // tyrannosaurus
		22216 // tyrannosaurus_soul
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 1;
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		npc._i_quest0 = 0;
		npc._i_quest1 = 0;
		npc._i_quest2 = 0;
		
		if (getNpcIntAIParam(npc, "CollectGhost") == 1)
			startQuestTimer("9009", npc, null, ((getNpcIntAIParamOrDefault(npc, "CollectGhostDespawnTime", 30) * 60) * 1000L));
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (!npc.isInCombat())
		{
			if (!npc.isDead())
				npc._i_quest0 = 0;
			
			npc._i_quest1 = 0;
			
			if (npc._i_ai3 > 1)
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF1), 1000000);
				
				npc._i_ai3 = 1;
			}
			
			if (getNpcIntAIParam(npc, "mobile_type") == 0)
				npc.getAI().addMoveToDesire(npc.getSpawnLocation().clone(), 30);
		}
		
		if (npc._i_quest2 == 1 && getElapsedTicks(npc._i_ai4) > 600)
			broadcastScriptEvent(npc, 11051, 0, 8000);
		
		super.onNoDesire(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("9001"))
		{
			if (npc._i_quest0 > 0 && npc.isInCombat() && Rnd.get(100) < getNpcIntAIParamOrDefault(npc, "ProbTimer", 100))
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_ALL), 10000000);
				
				if (!npc.isDead())
					npc._i_quest0 = 0;
				
				npc._i_quest1 = Rnd.get(3);
				
				switch (npc._i_quest1)
				{
					case 0:
						final int social1Frame = getNpcIntAIParamOrDefault(npc, "Social1_Frame", 10);
						if (social1Frame > 0)
							npc.getAI().addSocialDesire(getNpcIntAIParam(npc, "Social1"), social1Frame, 10000000);
						break;
					
					case 1:
						final int social2Frame = getNpcIntAIParamOrDefault(npc, "Social2_Frame", 10);
						if (social2Frame > 0)
							npc.getAI().addSocialDesire(getNpcIntAIParamOrDefault(npc, "Social2", 1), social2Frame, 10000000);
						break;
					
					case 2:
						final int social3Frame = getNpcIntAIParamOrDefault(npc, "Social3_Frame", 10);
						if (social3Frame > 0)
							npc.getAI().addSocialDesire(getNpcIntAIParamOrDefault(npc, "Social3", 2), social3Frame, 10000000);
						break;
				}
			}
		}
		else if (name.equalsIgnoreCase("9002"))
		{
			if (getElapsedTicks(npc._i_ai4) > 60 && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				npc.getAI().getHateList().refresh();
				
				broadcastScriptEvent(npc, 11051, 0, 8000);
			}
			startQuestTimer("9002", npc, null, 300000);
		}
		else if (name.equalsIgnoreCase("9009"))
		{
			if (!npc.isInCombat())
				npc.deleteMe();
			else if (getNpcIntAIParam(npc, "CollectGhost") == 1)
				startQuestTimer("9009", npc, null, (getNpcIntAIParamOrDefault(npc, "CollectGhostDespawnTime", 30) * 60) * 1000L);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (npc.isInCombat())
		{
			final double hpRatio = npc.getStatus().getHpRatio();
			
			if (hpRatio < getNpcIntAIParamOrDefault(npc, "HpChkRate5", 30))
			{
				if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_BUFF1))
				{
					npc._i_ai3 = 1;
					
					if (npc._c_ai1 != null)
					{
						if (npc._c_ai1 instanceof Playable)
							npc.getAI().addAttackDesire(npc._c_ai1, 100);
						
						npc._c_ai1 = null;
					}
				}
				else if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_BUFF2))
				{
					npc._i_ai3 = getNpcIntAIParamOrDefault(npc, "ProbMultiplier2", 5);
					
					if (npc._c_ai1 != null)
					{
						if (npc._c_ai1 instanceof Playable)
							npc.getAI().addAttackDesire(npc._c_ai1, 100);
						
						npc._c_ai1 = null;
					}
				}
			}
			else if (hpRatio < getNpcIntAIParamOrDefault(npc, "HpChkRate3", 60))
			{
				if (skill == getNpcSkillByType(npc, NpcSkillType.SELF_BUFF1))
				{
					npc._i_ai3 = getNpcIntAIParamOrDefault(npc, "ProbMultiplier2", 3);
					
					if (npc._c_ai1 != null)
					{
						if (npc._c_ai1 instanceof Playable)
							npc.getAI().addAttackDesire(npc._c_ai1, 100);
						
						npc._c_ai1 = null;
					}
				}
			}
		}
		
		if (success)
		{
			if (npc._c_ai0 != null && (skill == getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_A) || skill == getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_B) || skill == getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_C)))
				npc._c_ai0.getActingPlayer().sendPacket(SystemMessageId.TRAP_FAILED);
			
			if (creature != null && (skill == getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1) || skill == getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1) || skill == getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2) || skill == getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL3)) && Rnd.get(100) < 3)
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_ALL), 10000000);
				
				if (!npc.isDead())
					npc._i_quest0 = 0;
				
				npc._i_quest1 = Rnd.get(3);
				
				switch (npc._i_quest1)
				{
					case 0:
						final int social1Frame = getNpcIntAIParamOrDefault(npc, "Social1_Frame", 10);
						if (social1Frame > 0)
							npc.getAI().addSocialDesire(getNpcIntAIParamOrDefault(npc, "Social1", 0), social1Frame, 10000000);
						break;
					
					case 1:
						final int social2Frame = getNpcIntAIParamOrDefault(npc, "Social2_Frame", 10);
						if (social2Frame > 0)
							npc.getAI().addSocialDesire(getNpcIntAIParamOrDefault(npc, "Social2", 1), social2Frame, 10000000);
						break;
					
					case 2:
						final int social3Frame = getNpcIntAIParamOrDefault(npc, "Social3_Frame", 10);
						if (social3Frame > 0)
							npc.getAI().addSocialDesire(getNpcIntAIParamOrDefault(npc, "Social3", 2), social3Frame, 10000000);
						break;
				}
			}
		}
	}
	
	@Override
	public void onAbnormalStatusChanged(Npc npc, Creature caster, L2Skill skill)
	{
		if (skill != null && (skill.getId() == 5106 || skill.getId() == 5107 || skill.getId() == 5108))
		{
			if (skill.getLevel() == 0 && npc._i_quest0 > 0)
			{
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_ALL), 10000000);
				
				if (!npc.isDead())
					npc._i_quest0 = 0;
			}
			else if (skill.getLevel() > 0)
			{
				final int i0 = skill.getId() - 5106;
				if (i0 != npc._i_quest1)
				{
					if (i0 == 0)
						npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_A), 10000000);
					else if (i0 == 1)
						npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_B), 10000000);
					else if (i0 == 2)
						npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.CAPTURE_CANCEL_C), 10000000);
					
					npc._c_ai0 = caster;
				}
				else if (i0 == npc._i_quest1)
				{
					if (skill.getLevel() == 1 && npc._i_quest0 == 0)
					{
						npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEBUFF1), 10000000);
						
						if (!npc.isDead())
							npc._i_quest0 = 1;
						
						startQuestTimer("9001", npc, null, ((getNpcIntAIParamOrDefault(npc, "CaptureCycle", 10) + Rnd.get(getNpcIntAIParamOrDefault(npc, "CaptureCycleRand", 4))) * 1000));
					}
					else if (skill.getLevel() >= 2 && npc._i_quest0 >= 1)
					{
						npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.DEBUFF2), 10000000);
						
						if (!npc.isDead())
							npc._i_quest0 = 1;
						
						startQuestTimer("9001", npc, null, ((getNpcIntAIParamOrDefault(npc, "CaptureCycle", 10) + Rnd.get(getNpcIntAIParamOrDefault(npc, "CaptureCycleRand", 4))) * 1000));
					}
				}
			}
		}
		super.onAbnormalStatusChanged(npc, caster, skill);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		final double hpRatio = npc.getStatus().getHpRatio();
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		
		if (hpRatio <= getNpcIntAIParamOrDefault(npc, "HpChkRate5", 30))
		{
			if (npc._i_ai3 == getNpcIntAIParamOrDefault(npc, "ProbMultiplier1", 3))
			{
				if (topDesireTarget != null)
					npc._c_ai1 = topDesireTarget;
				
				npc.removeAllAttackDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF1), 1000000);
			}
			else if (npc._i_ai3 == 1)
			{
				if (topDesireTarget != null)
					npc._c_ai1 = topDesireTarget;
				
				npc.removeAllAttackDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF2), 1000000);
				
			}
		}
		else if (hpRatio <= getNpcIntAIParamOrDefault(npc, "HpChkRate5", 60))
		{
			if (npc._i_ai3 == 1)
			{
				if (topDesireTarget != null)
					npc._c_ai1 = topDesireTarget;
				
				npc.removeAllAttackDesire();
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF1), 1000000);
			}
		}
		
		if (npc.distance3D(attacker) > getNpcIntAIParamOrDefault(npc, "LongRangeSkillDist", 0))
		{
			if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbLongRangeDDMagic1", 0) * npc._i_ai3))
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
		}
		else if (topDesireTarget != null && topDesireTarget != npc)
		{
			if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbLongRangeDDMagic1", 0) * npc._i_ai3))
				npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
			
			if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbPhysicalSpecial1", 0) * npc._i_ai3))
				npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 1000000);
			
			if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbPhysicalSpecial2", 0) * npc._i_ai3))
				npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL2), 1000000);
			
			if (Rnd.get(100) <= (getNpcIntAIParamOrDefault(npc, "ProbPhysicalSpecial3", 0) * npc._i_ai3))
				npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL3), 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 11049)
		{
			npc._i_quest2 = 1;
			
			startQuestTimer("9002", npc, null, 600000);
		}
		else if (eventId == 10016)
		{
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 instanceof Player && getNpcIntAIParamOrDefault(npc, "BroadCastReception", 0) == 1)
			{
				npc.removeAllAttackDesire();
				npc.getAI().addAttackDesire(c0, 100);
			}
		}
		super.onScriptEvent(npc, eventId, arg1, arg2);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		npc._i_ai4 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (creature instanceof Player && !npc.isInCombat() && getNpcIntAIParamOrDefault(npc, "ag_type", 0) == 1)
			npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.LONG_RANGE_DD_MAGIC1), 1000000);
		
		tryToAttack(npc, creature);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (npc._i_quest2 == 1)
		{
			npc.broadcastNpcShout(NpcStringId.ID_1800011);
			
			broadcastScriptEventEx(npc, 11044, 0, 0, 5000);
		}
		super.onMyDying(npc, killer);
	}
}