package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Agit03Lidia extends DefaultNpc
{
	public Agit03Lidia()
	{
		super("ai/siegeablehall");
	}
	
	public Agit03Lidia(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35629
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1010624);
		
		createOnePrivateEx(npc, 35631, 56619, -27866, 569, 54000, 0, false);
		createOnePrivateEx(npc, 35630, 59282, -26496, 569, 48000, 0, false);
		createOnePrivateEx(npc, 35647, 57905, -27648, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 57905, -27712, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27182, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27232, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27282, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27332, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27382, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27432, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27482, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27532, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27582, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27632, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27682, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27732, 608, 33540, 0, false);
		createOnePrivateEx(npc, 35647, 58233, -27782, 608, 33540, 0, false);
		
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
		
		startQuestTimer("1001", npc, null, 30000);
		
		npc._i_ai1 = 0;
		
		// TODO Area
		// gg::Area_SetOnOff(AreaName1,1);
		// gg::Area_SetOnOff(AreaName2,1);
		// gg::Area_SetOnOff(AreaName3,1);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (npc.isInMyTerritory() && attacker instanceof Playable)
			npc.getAI().addAttackDesire(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 1000);
		
		if (Rnd.get((15 * 30)) < 1 || (npc.getStatus().getHpRatio() < 0.2 && Rnd.get((15 * 30)) < 1))
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_DD_MAGIC), 1000000);
		
		if (npc.distance2D(attacker) > 300 && Rnd.get((25 * 30)) < 1)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
		
		if (!npc.isInMyTerritory())
		{
			npc.removeAllAttackDesire();
			npc.teleportTo(npc.getSpawnLocation(), 0);
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
			called.getAI().addAttackDesire(attacker, (((damage * 1.0) / called.getStatus().getMaxHp()) / 0.05) * 1000);
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (caller.getNpcId() == 35631)
			called._i_ai1++;
		
		if (caller.getNpcId() == 35630)
			called._i_ai1++;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_BUFF), 1000000);
			
			if (npc._i_ai1 < 2)
				startQuestTimer("1001", npc, player, 30000);
		}
		
		return null;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1010638);
		
		// TODO Area
		// gg::Area_SetOnOff(AreaName1,0);
		// gg::Area_SetOnOff(AreaName2,0);
		// gg::Area_SetOnOff(AreaName3,0);
	}
}
