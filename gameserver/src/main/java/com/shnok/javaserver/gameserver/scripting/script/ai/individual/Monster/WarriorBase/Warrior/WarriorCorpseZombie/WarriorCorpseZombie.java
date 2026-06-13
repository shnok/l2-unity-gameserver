package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCorpseZombie;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCorpseZombie extends Warrior
{
	public WarriorCorpseZombie()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCorpseZombie");
	}
	
	public WarriorCorpseZombie(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21554,
		21548
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
		
		if (getNpcIntAIParam(npc, "IsPrivate") == 1)
			startQuestTimerAtFixedRate("1004", npc, null, 20000, 20000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (getNpcIntAIParam(npc, "IsPrivate") == 1)
		{
			final Npc master = npc.getMaster();
			if (master != null && !master.isDead())
				npc.getAI().addFollowDesire(master, 50);
			else
				npc.getAI().addWanderDesire(5, 5);
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1004"))
		{
			final Npc master = npc.getMaster();
			if (master != null && master.isDead() && getNpcIntAIParam(npc, "IsPrivate") == 1)
			{
				if (npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && npc.getAI().getCurrentIntention().getType() != IntentionType.CAST)
				{
					npc.decayMe();
					return null;
				}
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (Rnd.get(100) < 10)
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DEBUFF), 1000000);
		
		super.onMyDying(npc, killer);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (getNpcIntAIParam(npc, "IsTeleport") != 0)
		{
			if (npc.distance2D(attacker) > 100)
			{
				final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
				if (mostHated == attacker && Rnd.get(100) < 10)
				{
					npc.abortAll(false);
					npc.instantTeleportTo(attacker.getPosition().clone(), 0);
					
					final L2Skill avTeleport = SkillTable.getInstance().getInfo(4671, 1);
					npc.getAI().addCastDesire(attacker, avTeleport, 1000000);
					
					if (attacker instanceof Playable)
					{
						double f0 = getHateRatio(npc, attacker);
						f0 = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
						
						npc.getAI().addAttackDesire(attacker, f0 * 30);
					}
				}
			}
		}
		
		if (npc.getStatus().getHpRatio() < 0.1 && npc._i_ai0 == 0 && Rnd.get(100) < 10)
		{
			final L2Skill selfRangeDebuff = getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DEBUFF);
			npc.getAI().addCastDesire(npc, selfRangeDebuff, 1000000);
			
			npc._i_ai0 = 1;
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (getNpcIntAIParam(called, "IsTeleport") != 0)
		{
			if (called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && called.distance2D(attacker) > 100 && Rnd.get(100) < 10)
			{
				called.abortAll(false);
				called.instantTeleportTo(attacker.getPosition().clone(), 0);
				
				final L2Skill avTeleport = SkillTable.getInstance().getInfo(4671, 1);
				called.getAI().addCastDesire(attacker, avTeleport, 1000000);
				
				if (attacker instanceof Playable)
				{
					double f0 = getHateRatio(called, attacker);
					f0 = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((f0 / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
					
					called.getAI().addAttackDesire(attacker, (int) (f0 * 30));
				}
			}
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}