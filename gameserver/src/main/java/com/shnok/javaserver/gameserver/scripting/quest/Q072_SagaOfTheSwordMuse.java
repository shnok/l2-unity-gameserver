package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q072_SagaOfTheSwordMuse extends ThirdClassQuest
{
	public Q072_SagaOfTheSwordMuse()
	{
		super(72, "Saga of the Sword Muse", ClassId.SWORD_MUSE);
	}
	
	@Override
	protected final void setItemsNpcsMobsLocs()
	{
		_itemMain = 7095;
		_itemOptional = 6482;
		_itemReward = 7536;
		_itemAmulet1st = 7270;
		_itemAmulet2nd = 7301;
		_itemAmulet3rd = 7332;
		_itemAmulet4th = 7363;
		_itemHalishaMark = 7487;
		_itemAmulet5th = 7394;
		_itemAmulet6th = 7425;
		
		_npcMain = 30853;
		_npc1st = 31583;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31652;
		_npc3rd = 31537;
		_npcTablet4th = 31654;
		_npc4th = 31281;
		_npcTablet5th = 31655;
		_npcTablet6th = 31659;
		_npcDefender = 31618;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27288;
		_mobHalisha = 27221;
		_mobAttacker = 27280;
		
		_locCorrupted = new SpawnLocation(161719, -92823, -1893, -1);
		_locAttacker = new SpawnLocation(124355, 82155, -2803, -1);
		_locDefender = new SpawnLocation(124376, 82127, -2796, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7250;
		_msgCorruptedDespawn = NpcStringId.ID_7251;
		_msgCorruptedKill = NpcStringId.ID_7252;
		_msgHalishaSpawn = NpcStringId.ID_7253;
		_msgHalishaDespawn = NpcStringId.ID_7256;
		_msgHalishaKill = NpcStringId.ID_7254;
		_msgHalishaKillOther = NpcStringId.ID_7255;
		_msgAttackerSpawn = NpcStringId.ID_7264;
		_msgAttackerDespawn = NpcStringId.ID_7265;
		_msgAttackerAttack1 = NpcStringId.ID_7266;
		_msgAttackerAttack16 = NpcStringId.ID_7267;
		_msgDefenderSpawn = NpcStringId.ID_7257;
		_msgDefenderDespawnWon = NpcStringId.ID_7261;
		_msgDefenderDespawnLost = NpcStringId.ID_7262;
		_msgDefenderCombat = NpcStringId.ID_7258;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7259;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7260;
		_msgDefenderReward = NpcStringId.ID_7263;
	}
}