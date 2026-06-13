package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q087_SagaOfEvasSaint extends ThirdClassQuest
{
	public Q087_SagaOfEvasSaint()
	{
		super(87, "Saga of Eva's Saint", ClassId.EVAS_SAINT);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7088;
		_itemOptional = 0;
		_itemReward = 7524;
		_itemAmulet1st = 7285;
		_itemAmulet2nd = 7316;
		_itemAmulet3rd = 7347;
		_itemAmulet4th = 7378;
		_itemHalishaMark = 7502;
		_itemAmulet5th = 7409;
		_itemAmulet6th = 7440;
		
		_npcMain = 30191;
		_npc1st = 31588;
		_npc2nd = 31626;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31653;
		_npc3rd = 31280;
		_npcTablet4th = 31654;
		_npc4th = 31280;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31620;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27266;
		_mobHalisha = 27236;
		_mobAttacker = 27276;
		
		_locCorrupted = new SpawnLocation(162898, -76492, -3096, -1);
		_locAttacker = new SpawnLocation(46087, -36372, -1685, -1);
		_locDefender = new SpawnLocation(46066, -36396, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_8750;
		_msgCorruptedDespawn = NpcStringId.ID_8751;
		_msgCorruptedKill = NpcStringId.ID_8752;
		_msgHalishaSpawn = NpcStringId.ID_8753;
		_msgHalishaDespawn = NpcStringId.ID_8756;
		_msgHalishaKill = NpcStringId.ID_8754;
		_msgHalishaKillOther = NpcStringId.ID_8755;
		_msgAttackerSpawn = NpcStringId.ID_8764;
		_msgAttackerDespawn = NpcStringId.ID_8765;
		_msgAttackerAttack1 = NpcStringId.ID_8766;
		_msgAttackerAttack16 = NpcStringId.ID_8767;
		_msgDefenderSpawn = NpcStringId.ID_8757;
		_msgDefenderDespawnWon = NpcStringId.ID_8761;
		_msgDefenderDespawnLost = NpcStringId.ID_8762;
		_msgDefenderCombat = NpcStringId.ID_8758;
		_msgDefenderCombatIdle1 = NpcStringId.ID_8759;
		_msgDefenderCombatIdle2 = NpcStringId.ID_8760;
		_msgDefenderReward = NpcStringId.ID_8763;
	}
}