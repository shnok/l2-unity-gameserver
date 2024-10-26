package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class Sailren extends RaidBossType1
{
	public Sailren()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1/Sailren");
	}
	
	public Sailren(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29065 // sailren
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune16_npc2017_01m1");
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("11041", maker0, 0, 0);
		
		startQuestTimerAtFixedRate("3003", npc, null, 300000, 300000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			if (Rnd.get(5) < 1)
				npc.getAI().getAggroList().randomizeAttack();
			
			if (Rnd.get(5) < 1)
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF_A), 1000000);
		}
		else if (name.equalsIgnoreCase("3000"))
		{
			npc.getAI().addSocialDesire(2, 3000, 50);
		}
		else if (name.equalsIgnoreCase("3003"))
		{
			if (npc.getAI().getLifeTime() > 7 && npc.isInMyTerritory())
			{
				if (getElapsedTicks(npc._i_ai1) > 600)
				{
					npc.getAI().getHateList().refresh();
					
					NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("rune16_npc2017_13m1");
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("11046", maker0, 0, 0);
					
					npc.teleportTo(-113091, -243942, -15536, 0);
					
					npc._i_ai2 = 0;
					npc._i_ai3 = 0;
					
					maker0 = SpawnManager.getInstance().getNpcMaker("rune16_npc2017_01m1");
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("11043", maker0, 0, 0);
				}
			}
		}
		return null;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
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
		if (topDesireTarget != null && topDesireTarget != caster && skill.getAggroPoints() > 0 && npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK)
			npc.getAI().addAttackDesire(caster, (((skill.getAggroPoints() / npc.getStatus().getMaxHp()) * 4000) * 150));
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (npc._i_ai3 == 1)
			npc.getAI().addAttackDesire(creature, 200);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 11042)
		{
			npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
			
			npc.teleportTo(27549, -6638, -2008, 0);
			
			startQuestTimer("3000", npc, null, 18000);
			startQuestTimerAtFixedRate("1001", npc, null, 60000, 60000);
		}
		else if (eventId == 11055)
		{
			npc._i_ai3 = 1;
			
			final Player c0 = (Player) World.getInstance().getObject(arg1);
			final Party party0 = c0.getParty();
			if (party0 != null)
			{
				for (Player partyMember : party0.getMembers())
					if (npc.getSpawn().isInMyTerritory(partyMember) && npc.distance3D(partyMember) < 3000)
						npc.getAI().addAttackDesire(partyMember, 200);
			}
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		broadcastScriptEvent(npc, 11054, 0, 8000);
	}
}