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

public class Agit03Giselle extends DefaultNpc
{
	public Agit03Giselle()
	{
		super("ai/siegeablehall");
	}
	
	public Agit03Giselle(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35631
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1010637);
		
		startQuestTimer("1003", npc, null, 1000);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
			npc.getAI().addAttackDesire(attacker, (((damage * 1.0) / npc.getStatus().getMaxHp()) / 0.05) * 1000);
		
		if (npc.distance2D(attacker) > 300 && Rnd.get((25 * 30)) < 1)
			npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1003"))
		{
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.SELF_RANGE_BUFF_A), 1000000);
			
			startQuestTimer("1003", npc, player, 300000);
		}
		
		return null;
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		npc.broadcastNpcShout(NpcStringId.ID_1010625);
		
		// TODO Area
		// gg::Area_SetOnOff(AreaName1,0);
		// gg::Area_SetOnOff(AreaName2,0);
	}
}
