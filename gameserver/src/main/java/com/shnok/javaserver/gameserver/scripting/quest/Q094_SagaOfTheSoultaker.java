package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q094_SagaOfTheSoultaker extends ThirdClassQuest
{
	public Q094_SagaOfTheSoultaker()
	{
		super(94, "Saga of the Soultaker", ClassId.SOULTAKER);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7085;
		_itemOptional = 0;
		_itemReward = 7533;
		_itemAmulet1st = 7292;
		_itemAmulet2nd = 7323;
		_itemAmulet3rd = 7354;
		_itemAmulet4th = 7385;
		_itemHalishaMark = 7509;
		_itemAmulet5th = 7416;
		_itemAmulet6th = 7447;
		
		_npcMain = 30832;
		_npc1st = 31279;
		_npc2nd = 31623;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31650;
		_npc3rd = 31279;
		_npcTablet4th = 31654;
		_npc4th = 31279;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31645;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27257;
		_mobHalisha = 27243;
		_mobAttacker = 27265;
		
		_locCorrupted = new SpawnLocation(191046, -40640, -3042, -1);
		_locAttacker = new SpawnLocation(46066, -36396, -1685, -1);
		_locDefender = new SpawnLocation(46087, -36372, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9450;
		_msgCorruptedDespawn = NpcStringId.ID_9451;
		_msgCorruptedKill = NpcStringId.ID_9452;
		_msgHalishaSpawn = NpcStringId.ID_9453;
		_msgHalishaDespawn = NpcStringId.ID_9456;
		_msgHalishaKill = NpcStringId.ID_9454;
		_msgHalishaKillOther = NpcStringId.ID_9455;
		_msgAttackerSpawn = NpcStringId.ID_9464;
		_msgAttackerDespawn = NpcStringId.ID_9465;
		_msgAttackerAttack1 = NpcStringId.ID_9466;
		_msgAttackerAttack16 = NpcStringId.ID_9467;
		_msgDefenderSpawn = NpcStringId.ID_9457;
		_msgDefenderDespawnWon = NpcStringId.ID_9461;
		_msgDefenderDespawnLost = NpcStringId.ID_9462;
		_msgDefenderCombat = NpcStringId.ID_9458;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9459;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9460;
		_msgDefenderReward = NpcStringId.ID_9463;
	}
}