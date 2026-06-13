package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.RoyalRushRoomBossBasic;

import com.shnok.javaserver.gameserver.model.actor.Npc;

public class RoyalRushRoomBoss2 extends RoyalRushRoomBoss1
{
	public RoyalRushRoomBoss2()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/RoyalRushRoomBossBasic");
	}
	
	public RoyalRushRoomBoss2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18120,
		18123,
		18126,
		18129
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
	}
}
