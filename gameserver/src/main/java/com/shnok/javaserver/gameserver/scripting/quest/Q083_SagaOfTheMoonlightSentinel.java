package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q083_SagaOfTheMoonlightSentinel extends ThirdClassQuest
{
	public Q083_SagaOfTheMoonlightSentinel()
	{
		super(83, "Saga of the Moonlight Sentinel", ClassId.MOONLIGHT_SENTINEL);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7106;
		_itemOptional = 0;
		_itemReward = 7520;
		_itemAmulet1st = 7281;
		_itemAmulet2nd = 7312;
		_itemAmulet3rd = 7343;
		_itemAmulet4th = 7374;
		_itemHalishaMark = 7498;
		_itemAmulet5th = 7405;
		_itemAmulet6th = 7436;
		
		_npcMain = 30702;
		_npc1st = 31604;
		_npc2nd = 31627;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31652;
		_npc3rd = 31640;
		_npcTablet4th = 31654;
		_npc4th = 31641;
		_npcTablet5th = 31655;
		_npcTablet6th = 31658;
		_npcDefender = 31634;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27297;
		_mobHalisha = 27232;
		_mobAttacker = 27306;
		
		_locCorrupted = new SpawnLocation(161719, -92823, -1893, -1);
		_locAttacker = new SpawnLocation(181227, 36703, -4816, -1);
		_locDefender = new SpawnLocation(181215, 36676, -4812, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_8350;
		_msgCorruptedDespawn = NpcStringId.ID_8351;
		_msgCorruptedKill = NpcStringId.ID_8352;
		_msgHalishaSpawn = NpcStringId.ID_8353;
		_msgHalishaDespawn = NpcStringId.ID_8356;
		_msgHalishaKill = NpcStringId.ID_8354;
		_msgHalishaKillOther = NpcStringId.ID_8355;
		_msgAttackerSpawn = NpcStringId.ID_8364;
		_msgAttackerDespawn = NpcStringId.ID_8365;
		_msgAttackerAttack1 = NpcStringId.ID_8366;
		_msgAttackerAttack16 = NpcStringId.ID_8367;
		_msgDefenderSpawn = NpcStringId.ID_8357;
		_msgDefenderDespawnWon = NpcStringId.ID_8361;
		_msgDefenderDespawnLost = NpcStringId.ID_8362;
		_msgDefenderCombat = NpcStringId.ID_8358;
		_msgDefenderCombatIdle1 = NpcStringId.ID_8359;
		_msgDefenderCombatIdle2 = NpcStringId.ID_8360;
		_msgDefenderReward = NpcStringId.ID_8363;
	}
}