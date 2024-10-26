package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.RaidBoss.RaidBossAlone.RaidBossType1.RaidBossType1Aggressive;

import net.sf.l2j.gameserver.data.manager.SpawnManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.spawn.NpcMaker;

public class FreyaRoyalGuard extends RaidBossType1Aggressive
{
	public FreyaRoyalGuard()
	{
		super("ai/individual/Monster/RaidBoss/RaidBossAlone/RaidBossType1/RaidBossType1Aggressive");
	}
	
	public FreyaRoyalGuard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29060 // freya_royalguard
	};
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_mb2314_05m1");
		if (maker0 != null)
			maker0.getMaker().onMakerScriptEvent("10005", maker0, 0, 0);
		
		broadcastScriptEvent(npc, 11036, 3, 7000);
	}
}