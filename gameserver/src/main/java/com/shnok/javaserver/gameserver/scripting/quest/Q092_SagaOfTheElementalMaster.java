package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q092_SagaOfTheElementalMaster extends ThirdClassQuest
{
	public Q092_SagaOfTheElementalMaster()
	{
		super(92, "Saga of the Elemental Master", ClassId.ELEMENTAL_MASTER);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7111;
		_itemOptional = 0;
		_itemReward = 7605;
		_itemAmulet1st = 7290;
		_itemAmulet2nd = 7321;
		_itemAmulet3rd = 7352;
		_itemAmulet4th = 7383;
		_itemHalishaMark = 7507;
		_itemAmulet5th = 7414;
		_itemAmulet6th = 7445;
		
		_npcMain = 30174;
		_npc1st = 31614;
		_npc2nd = 31281;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31652;
		_npc3rd = 31614;
		_npcTablet4th = 31654;
		_npc4th = 31614;
		_npcTablet5th = 31655;
		_npcTablet6th = 31659;
		_npcDefender = 31629;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27314;
		_mobHalisha = 27241;
		_mobAttacker = 27311;
		
		_locCorrupted = new SpawnLocation(161719, -92823, -1893, -1);
		_locAttacker = new SpawnLocation(124376, 82127, -2796, -1);
		_locDefender = new SpawnLocation(124355, 82155, -2803, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9250;
		_msgCorruptedDespawn = NpcStringId.ID_9251;
		_msgCorruptedKill = NpcStringId.ID_9252;
		_msgHalishaSpawn = NpcStringId.ID_9253;
		_msgHalishaDespawn = NpcStringId.ID_9256;
		_msgHalishaKill = NpcStringId.ID_9254;
		_msgHalishaKillOther = NpcStringId.ID_9255;
		_msgAttackerSpawn = NpcStringId.ID_9264;
		_msgAttackerDespawn = NpcStringId.ID_9265;
		_msgAttackerAttack1 = NpcStringId.ID_9266;
		_msgAttackerAttack16 = NpcStringId.ID_9267;
		_msgDefenderSpawn = NpcStringId.ID_9257;
		_msgDefenderDespawnWon = NpcStringId.ID_9261;
		_msgDefenderDespawnLost = NpcStringId.ID_9262;
		_msgDefenderCombat = NpcStringId.ID_9258;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9259;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9260;
		_msgDefenderReward = NpcStringId.ID_9263;
	}
}