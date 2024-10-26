package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1.RaidBossType1Aggressive;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.serverpackets.SpecialCamera;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.GameTimeTaskManager;

public class DrChaosGiganticGolem extends RaidBossType1Aggressive
{
	public DrChaosGiganticGolem()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1/RaidBossType1Aggressive");
	}
	
	public DrChaosGiganticGolem(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		25512 // dr_chaos_gigantic_golem
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimer("1001", npc, null, 1000);
		
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		npc._i_ai2 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			npc.broadcastPacket(new SpecialCamera(npc.getObjectId(), 30, -200, 20, 6000, 8000, 0, 0, 0, 0));
			
			startQuestTimer("1002", npc, null, 10000);
		}
		else if (name.equalsIgnoreCase("1002"))
		{
			npc._i_ai2 = 1;
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		
		if (!(creature instanceof Playable))
			return;
		
		if (npc._i_ai2 == 1)
			npc.getAI().addAttackDesire(creature, 200);
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (getElapsedTicks(npc._i_ai1) > 1200)
		{
			npc.broadcastNpcSay(NpcStringId.ID_1010582);
			
			if (npc.hasMaster())
				npc.getMaster().sendScriptEvent(10029, 0, 0);
			
			npc.deleteMe();
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastNpcSay(NpcStringId.ID_1010583);
		
		if (npc.hasMaster())
			npc.getMaster().sendScriptEvent(10029, 0, 0);
	}
}