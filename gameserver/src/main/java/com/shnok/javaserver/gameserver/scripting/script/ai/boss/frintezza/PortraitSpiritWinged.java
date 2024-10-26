package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class PortraitSpiritWinged extends DefaultNpc
{
	private static final L2Skill BOMB = SkillTable.getInstance().getInfo(5011, 1);
	private static final L2Skill HOLD = SkillTable.getInstance().getInfo(5012, 1);
	private static final L2Skill VAMPIRIC = SkillTable.getInstance().getInfo(5013, 1);
	
	public PortraitSpiritWinged()
	{
		super("ai/boss/frintezza");
	}
	
	public PortraitSpiritWinged(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29051
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai4 = 0;
		npc._i_quest1 = 0;
		npc._i_quest2 = 0;
		
		npc.getAI().addWanderDesire(200, 5);
		
		startQuestTimer("5000", npc, null, 10 * 1000L);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc._i_ai4 == 0)
		{
			if (attacker instanceof Playable)
			{
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
				npc.getAI().addAttackDesire(attacker, 50);
				
				npc._i_quest1 = GameTimeTaskManager.getInstance().getCurrentTick();
				
				startQuestTimer("2001", npc, null, 5000);
			}
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called._i_ai4 == 0 && called.getAI().getLifeTime() > 7)
		{
			if (attacker instanceof Playable)
			{
				called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.DD_MAGIC), 1000000);
				called.getAI().addAttackDesire(attacker, 50);
			}
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill == BOMB && success)
		{
			npc.decayMe();
			return;
		}
		
		if (npc._i_ai4 == 0)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget instanceof Player)
			{
				if (getAbnormalLevel(topDesireTarget, HOLD) > 0)
				{
					if (npc._i_quest2 < 3)
					{
						npc._i_quest2++;
						
						npc.getAI().addCastDesire(topDesireTarget, VAMPIRIC, 1000000);
					}
				}
				else
					npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
				
				npc.getAI().addAttackDesire(topDesireTarget, 50);
			}
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (npc._i_ai4 == 0)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			
			if (name.equalsIgnoreCase("2001"))
			{
				if (topDesireTarget != null && getElapsedTicks(npc._i_quest1) > 5)
					npc.getAI().addCastDesire(topDesireTarget, HOLD, 1000000);
			}
			else if (name.equalsIgnoreCase("2002"))
			{
				if (topDesireTarget != null)
				{
					if (getAbnormalLevel(topDesireTarget, HOLD) > 0)
					{
						if (npc._i_quest2 < 3)
						{
							npc._i_quest2++;
							
							npc.getAI().addCastDesire(topDesireTarget, VAMPIRIC, 1000000);
						}
						
						startQuestTimer("2002", npc, null, 5000);
					}
					else
						npc.getAI().addCastDesire(topDesireTarget, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
					
					npc.getAI().addAttackDesire(topDesireTarget, 50);
				}
			}
			else if (name.equalsIgnoreCase("5000"))
			{
				if (!npc.hasMaster() || npc.getMaster().isDead())
				{
					if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
						npc.deleteMe();
					else if (topDesireTarget != null && npc.distance2D(topDesireTarget) > 5000)
						npc.deleteMe();
					else
						startQuestTimer("5000", npc, player, 10000);
				}
				else
					startQuestTimer("5000", npc, player, 10000);
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Playable)
		{
			if (npc._i_ai4 == 0 && npc.getAI().getLifeTime() > 7)
			{
				npc.getAI().addCastDesire(creature, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
				npc.getAI().addAttackDesire(creature, 50);
			}
		}
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (arg1 == 20000)
		{
			npc.removeAllDesire();
			
			npc.getAI().addCastDesire(npc, BOMB, 1000000);
		}
		else if (arg1 == 40000)
		{
			npc.removeAllDesire();
			
			npc.deleteMe();
		}
		else if (arg1 == 50000)
		{
			npc._i_ai4 = 1;
			
			npc.removeAllDesire();
		}
	}
}