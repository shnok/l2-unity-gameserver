package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidPrivate;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterAI;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RaidPrivateStandard extends MonsterAI
{
	public RaidPrivateStandard()
	{
		super("ai/individual/Monster/RaidPrivate/RaidPrivateStandard");
	}
	
	public RaidPrivateStandard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25006,
		25009,
		25012,
		25015,
		25018,
		25022,
		25025,
		25028,
		25031,
		25037,
		25040,
		25043,
		25046,
		25049,
		25053,
		25056,
		25059,
		25066,
		25069,
		25075,
		25078,
		25081,
		25087,
		25094,
		25101,
		25105,
		25108,
		25117,
		25124,
		25133,
		25136,
		25142,
		25145,
		25148,
		25151,
		25154,
		25161,
		25168,
		25172,
		25175,
		25178,
		25184,
		25187,
		25194,
		25204,
		25207,
		25213,
		25219,
		25228,
		25232,
		25237,
		25240,
		25247,
		25258,
		25262,
		25267,
		25271,
		25279,
		25288,
		25289,
		25295,
		25301,
		25308,
		25311,
		25314,
		25318,
		25327,
		25341,
		25355,
		25363,
		25371,
		25382,
		25387,
		25390,
		25414,
		25424,
		25433,
		25442,
		25445,
		25448,
		25457,
		25459,
		25468,
		25476,
		25492,
		25495,
		25500,
		29032,
		29035,
		29042,
		29058,
		25503,
		25511,
		25526,
		29097
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc.hasMaster() && !npc.getMaster().isDead())
			npc.getAI().addFollowDesire(npc.getMaster(), 5);
		else
			npc.getAI().addWanderDesire(10, 5);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		if (!npc.hasMaster() || npc.getMaster().isDead())
		{
			npc.deleteMe();
			return;
		}
		
		startQuestTimer("1001", npc, null, 2000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			final Creature master = npc.getMaster();
			
			if (npc.distance2D(master) > 500 && !master.isDead() && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				npc.abortAll(false);
				npc.instantTeleportTo(master.getPosition(), 0);
			}
			
			if (Rnd.get(3) < 1)
				npc.getAI().getAggroList().randomizeAttack();
			
			if (master.isDead())
			{
				npc.deleteMe();
				return null;
			}
			
			startQuestTimerAtFixedRate("1001", npc, player, 60 * 1000L, 60 * 1000L);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onPartyAttacked(Npc caller, Npc called, Creature target, int damage)
	{
		int i0 = 0;
		final Creature master = called.getMaster();
		
		if (master != null && target.getStatus().getLevel() <= (master.getStatus().getLevel() + 8))
		{
			if (target instanceof Playable)
			{
				if (damage == 0)
					damage = 1;
				
				called.getAI().addAttackDesire(target, (int) (((1.0 * damage) / (called.getStatus().getLevel() + 7)) * 20000));
			}
		}
		
		if (caller == called)
		{
			final L2Skill raidCurse = SkillTable.getInstance().getInfo(4515, 1);
			
			i0 = 10;
			if (target.getStatus().getLevel() > (called.getStatus().getLevel() + 8) && getAbnormalLevel(target, raidCurse) == -1)
			{
				called.getAI().addCastDesire(target, raidCurse, 1000000);
				called.getAI().getAggroList().stopHate(target);
			}
		}
		else
			i0 = (8 + Rnd.get(13));
		
		if (called.getMove().getGeoPathFailCount() > i0)
		{
			final Creature topDesireTarget = called.getAI().getTopDesireTarget();
			
			if (topDesireTarget != null && called.distance2D(topDesireTarget) < 1000)
			{
				called.abortAll(false);
				called.instantTeleportTo(topDesireTarget.getPosition(), 0);
			}
			else
			{
				called.removeAllAttackDesire();
				
				if (target instanceof Playable)
				{
					if (damage == 0)
						damage = 1;
					
					called.getAI().addAttackDesire(target, (int) (((1.0 * damage) / (called.getStatus().getLevel() + 7)) * 20000));
				}
				called.abortAll(false);
				called.instantTeleportTo(target.getPosition(), 0);
			}
		}
		
		if (called.isInsideZone(ZoneId.PEACE))
		{
			called.abortAll(false);
			called.instantTeleportTo(called.getSpawnLocation(), 0);
			called.removeAllAttackDesire();
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final L2Skill raidMute = SkillTable.getInstance().getInfo(4515, 1);
		
		if (caster.getStatus().getLevel() > (npc.getStatus().getLevel() + 8) && getAbnormalLevel(caster, raidMute) <= 0)
		{
			npc.getAI().addCastDesire(caster, raidMute, 1000000);
			return;
		}
		
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null)
		{
			if (skill.getAggroPoints() > 0 && npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && topDesireTarget != caster)
				npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() / npc.getStatus().getMaxHp()) * 4000) * 150));
			
			if (npc.getMove().getGeoPathFailCount() > 10 && caster == topDesireTarget && npc.getStatus().getHp() != npc.getStatus().getMaxHp())
			{
				npc.abortAll(false);
				npc.instantTeleportTo(caster.getPosition(), 0);
			}
		}
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller == called.getMaster())
			called.deleteMe();
	}
}