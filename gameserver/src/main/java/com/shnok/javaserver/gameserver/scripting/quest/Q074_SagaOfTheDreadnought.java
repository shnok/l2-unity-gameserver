package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q074_SagaOfTheDreadnought extends ThirdClassQuest
{
	public Q074_SagaOfTheDreadnought()
	{
		super(74, "Saga of the Dreadnought", ClassId.DREADNOUGHT);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7097;
		_itemOptional = 6480;
		_itemReward = 7538;
		_itemAmulet1st = 7272;
		_itemAmulet2nd = 7303;
		_itemAmulet3rd = 7334;
		_itemAmulet4th = 7365;
		_itemHalishaMark = 7489;
		_itemAmulet5th = 7396;
		_itemAmulet6th = 7427;
		
		_npcMain = 30850;
		_npc1st = 31298;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31650;
		_npc3rd = 31276;
		_npcTablet4th = 31654;
		_npc4th = 31522;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31595;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27290;
		_mobHalisha = 27223;
		_mobAttacker = 27282;
		
		_locCorrupted = new SpawnLocation(191046, -40640, -3042, -1);
		_locAttacker = new SpawnLocation(46087, -36372, -1685, -1);
		_locDefender = new SpawnLocation(46066, -36396, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7450;
		_msgCorruptedDespawn = NpcStringId.ID_7451;
		_msgCorruptedKill = NpcStringId.ID_7452;
		_msgHalishaSpawn = NpcStringId.ID_7453;
		_msgHalishaDespawn = NpcStringId.ID_7456;
		_msgHalishaKill = NpcStringId.ID_7454;
		_msgHalishaKillOther = NpcStringId.ID_7455;
		_msgAttackerSpawn = NpcStringId.ID_7464;
		_msgAttackerDespawn = NpcStringId.ID_7465;
		_msgAttackerAttack1 = NpcStringId.ID_7466;
		_msgAttackerAttack16 = NpcStringId.ID_7467;
		_msgDefenderSpawn = NpcStringId.ID_7457;
		_msgDefenderDespawnWon = NpcStringId.ID_7461;
		_msgDefenderDespawnLost = NpcStringId.ID_7462;
		_msgDefenderCombat = NpcStringId.ID_7458;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7459;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7460;
		_msgDefenderReward = NpcStringId.ID_7463;
	}
}