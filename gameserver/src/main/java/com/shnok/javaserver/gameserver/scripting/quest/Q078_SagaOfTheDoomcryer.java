package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q078_SagaOfTheDoomcryer extends ThirdClassQuest
{
	public Q078_SagaOfTheDoomcryer()
	{
		super(78, "Saga of the Doomcryer", ClassId.DOOMCRYER);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7101;
		_itemOptional = 0;
		_itemReward = 7539;
		_itemAmulet1st = 7276;
		_itemAmulet2nd = 7307;
		_itemAmulet3rd = 7338;
		_itemAmulet4th = 7369;
		_itemHalishaMark = 7493;
		_itemAmulet5th = 7400;
		_itemAmulet6th = 7431;
		
		_npcMain = 31336;
		_npc1st = 31589;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31650;
		_npc3rd = 31290;
		_npcTablet4th = 31654;
		_npc4th = 31290;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31642;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27295;
		_mobHalisha = 27227;
		_mobAttacker = 27285;
		
		_locCorrupted = new SpawnLocation(191046, -40640, -3042, -1);
		_locAttacker = new SpawnLocation(46087, -36372, -1685, -1);
		_locDefender = new SpawnLocation(46066, -36396, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7850;
		_msgCorruptedDespawn = NpcStringId.ID_7851;
		_msgCorruptedKill = NpcStringId.ID_7852;
		_msgHalishaSpawn = NpcStringId.ID_7853;
		_msgHalishaDespawn = NpcStringId.ID_7856;
		_msgHalishaKill = NpcStringId.ID_7854;
		_msgHalishaKillOther = NpcStringId.ID_7855;
		_msgAttackerSpawn = NpcStringId.ID_7864;
		_msgAttackerDespawn = NpcStringId.ID_7865;
		_msgAttackerAttack1 = NpcStringId.ID_7866;
		_msgAttackerAttack16 = NpcStringId.ID_7867;
		_msgDefenderSpawn = NpcStringId.ID_7857;
		_msgDefenderDespawnWon = NpcStringId.ID_7861;
		_msgDefenderDespawnLost = NpcStringId.ID_7862;
		_msgDefenderCombat = NpcStringId.ID_7858;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7859;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7860;
		_msgDefenderReward = NpcStringId.ID_7863;
	}
}