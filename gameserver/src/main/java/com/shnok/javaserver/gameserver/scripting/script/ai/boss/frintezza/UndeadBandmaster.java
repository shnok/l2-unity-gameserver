package com.shnok.javaserver.gameserver.scripting.script.ai.boss.frintezza;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.scripting.script.ai.individual.DefaultNpc;
import net.sf.l2j.gameserver.skills.L2Skill;

public class UndeadBandmaster extends DefaultNpc
{
	// private static final String dmgzone1 = "25_15_frintessaE_02";
	// private static final String dmgzone2 = "25_15_frintessaE_03";
	// private static final String dmgzone3 = "25_15_frintessaE_04";
	
	private static final int SOUL_BREAKING_ARROW = 8192;
	
	public UndeadBandmaster()
	{
		super("ai/boss/frintezza");
	}
	
	public UndeadBandmaster(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18334 // undeadband_bandmaster
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		if (getNpcIntAIParam(npc, "Shoutman") == 1)
			npc.broadcastNpcShout(NpcStringId.ID_1010644);
		
		npc._i_ai0 = 1;
		
		// TODO Zone
		// gg::Area_SetOnOff(dmgzone1,1);
		// gg::Area_SetOnOff(dmgzone2,1);
		// gg::Area_SetOnOff(dmgzone3,1);
		super.onCreated(npc);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (Rnd.get(100) < 33)
			broadcastScriptEventEx(npc, 0, 10033, attacker.getObjectId(), 1500);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10032)
		{
			if (arg1 == 0 || arg1 == 2 || arg1 == 4 || arg1 == 6)
				npc._i_ai0 = arg1;
		}
		
		if (getNpcIntAIParam(npc, "Shoutman") == 1)
		{
			if (arg1 == 10042)
				npc.broadcastNpcShout(NpcStringId.ID_1010645);
			else if (arg1 == 10043)
				npc.broadcastNpcShout(NpcStringId.ID_1010646);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (npc._i_ai0 == 0 || npc._i_ai0 == 2 || npc._i_ai0 == 4 || npc._i_ai0 == 6)
			npc.dropItem(killer, SOUL_BREAKING_ARROW, 1);
	}
}