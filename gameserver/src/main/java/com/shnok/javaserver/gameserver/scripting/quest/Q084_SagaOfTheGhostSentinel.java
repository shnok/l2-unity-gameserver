package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q084_SagaOfTheGhostSentinel extends ThirdClassQuest
{
	public Q084_SagaOfTheGhostSentinel()
	{
		super(84, "Saga of the Ghost Sentinel", ClassId.GHOST_SENTINEL);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7107;
		_itemOptional = 0;
		_itemReward = 7521;
		_itemAmulet1st = 7282;
		_itemAmulet2nd = 7313;
		_itemAmulet3rd = 7344;
		_itemAmulet4th = 7375;
		_itemHalishaMark = 7499;
		_itemAmulet5th = 7406;
		_itemAmulet6th = 7437;
		
		_npcMain = 30702;
		_npc1st = 31604;
		_npc2nd = 31587;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31652;
		_npc3rd = 31640;
		_npcTablet4th = 31654;
		_npc4th = 31641;
		_npcTablet5th = 31655;
		_npcTablet6th = 31659;
		_npcDefender = 31635;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27298;
		_mobHalisha = 27233;
		_mobAttacker = 27307;
		
		_locCorrupted = new SpawnLocation(161719, -92823, -1893, -1);
		_locAttacker = new SpawnLocation(124376, 82127, -2796, -1);
		_locDefender = new SpawnLocation(124376, 82127, -2796, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_8450;
		_msgCorruptedDespawn = NpcStringId.ID_8451;
		_msgCorruptedKill = NpcStringId.ID_8452;
		_msgHalishaSpawn = NpcStringId.ID_8453;
		_msgHalishaDespawn = NpcStringId.ID_8456;
		_msgHalishaKill = NpcStringId.ID_8454;
		_msgHalishaKillOther = NpcStringId.ID_8455;
		_msgAttackerSpawn = NpcStringId.ID_8464;
		_msgAttackerDespawn = NpcStringId.ID_8465;
		_msgAttackerAttack1 = NpcStringId.ID_8466;
		_msgAttackerAttack16 = NpcStringId.ID_8467;
		_msgDefenderSpawn = NpcStringId.ID_8457;
		_msgDefenderDespawnWon = NpcStringId.ID_8461;
		_msgDefenderDespawnLost = NpcStringId.ID_8462;
		_msgDefenderCombat = NpcStringId.ID_8458;
		_msgDefenderCombatIdle1 = NpcStringId.ID_8459;
		_msgDefenderCombatIdle2 = NpcStringId.ID_8460;
		_msgDefenderReward = NpcStringId.ID_8463;
	}
}