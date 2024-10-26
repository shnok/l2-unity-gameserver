package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.PlaySound;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.MonsterAI;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RaidBossStandard extends MonsterAI
{
	public RaidBossStandard()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossStandard");
	}
	
	public RaidBossStandard(String descr)
	{
		super(descr);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			if (!npc.isInMyTerritory() && Rnd.nextBoolean() && npc.getAI().getCurrentIntention().getType() != IntentionType.ATTACK)
			{
				npc.abortAll(true);
				npc.removeAllAttackDesire();
				npc.teleportTo(npc.getSpawnLocation(), 0);
			}
			
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Player player && player.getMountType() == 1)
		{
			final L2Skill striderSlow = SkillTable.getInstance().getInfo(4258, 1);
			if (getAbnormalLevel(attacker, striderSlow) <= 0)
				npc.getAI().addCastDesire(attacker, striderSlow, 1000000);
		}
		
		if (attacker.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidCurse = SkillTable.getInstance().getInfo(4515, 1);
			if (getAbnormalLevel(attacker, raidCurse) == -1)
			{
				npc.getAI().addCastDesire(attacker, raidCurse, 1000000);
				npc.getAI().getAggroList().stopHate(attacker);
				return;
			}
		}
		
		if (attacker instanceof Playable)
		{
			if (damage == 0)
				damage = 1;
			
			npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 20000);
		}
		
		if (npc.getMove().getGeoPathFailCount() > 10)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && npc.distance2D(topDesireTarget) < 1000)
			{
				npc.abortAll(false);
				npc.teleportTo(topDesireTarget.getPosition(), 0);
			}
			else
			{
				npc.removeAllAttackDesire();
				
				if (attacker instanceof Playable)
				{
					if (damage == 0)
						damage = 1;
					
					npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 20000);
				}
				npc.abortAll(false);
				npc.teleportTo(attacker.getPosition(), 0);
			}
		}
		
		if (npc.isInsideZone(ZoneId.PEACE))
		{
			npc.abortAll(false);
			npc.removeAllAttackDesire();
			npc.teleportTo(npc.getSpawnLocation(), 0);
		}
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastPacket(new PlaySound(1, npc.getTemplate().getAiParams().getOrDefault("RaidSpawnMusic", "Rm01_A"), npc));
		
		startQuestTimerAtFixedRate("1001", npc, null, 1000, 60000);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (caster.getStatus().getLevel() > (npc.getStatus().getLevel() + 8))
		{
			final L2Skill raidMute = SkillTable.getInstance().getInfo(4215, 1);
			if (getAbnormalLevel(caster, raidMute) <= 0)
			{
				npc.getAI().addCastDesire(caster, raidMute, 1000000);
				return;
			}
		}
		
		final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
		if (topDesireTarget != null)
		{
			if (skill.getAggroPoints() > 0 && npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && topDesireTarget != caster)
				npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() / npc.getStatus().getMaxHp()) * 4000) * 150));
			
			if (npc.getMove().getGeoPathFailCount() > 10 && caster == topDesireTarget && npc.getStatus().getHp() != npc.getStatus().getMaxHp())
			{
				npc.abortAll(false);
				npc.teleportTo(caster.getPosition(), 0);
			}
		}
	}
}