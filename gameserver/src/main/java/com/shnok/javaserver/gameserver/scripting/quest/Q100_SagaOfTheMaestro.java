package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q100_SagaOfTheMaestro extends ThirdClassQuest
{
	public Q100_SagaOfTheMaestro()
	{
		super(100, "Saga of the Maestro", ClassId.MAESTRO);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7108;
		_itemOptional = 0;
		_itemReward = 7607;
		_itemAmulet1st = 7298;
		_itemAmulet2nd = 7329;
		_itemAmulet3rd = 7360;
		_itemAmulet4th = 7391;
		_itemHalishaMark = 7515;
		_itemAmulet5th = 7422;
		_itemAmulet6th = 7453;
		
		_npcMain = 31592;
		_npc1st = 31597;
		_npc2nd = 31273;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31653;
		_npc3rd = 31597;
		_npcTablet4th = 31654;
		_npc4th = 31597;
		_npcTablet5th = 31655;
		_npcTablet6th = 31656;
		_npcDefender = 31596;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27260;
		_mobHalisha = 27249;
		_mobAttacker = 27308;
		
		_locCorrupted = new SpawnLocation(162898, -76492, -3096, -1);
		_locAttacker = new SpawnLocation(47429, -56923, -2383, -1);
		_locDefender = new SpawnLocation(47391, -56929, -2370, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_10050;
		_msgCorruptedDespawn = NpcStringId.ID_10051;
		_msgCorruptedKill = NpcStringId.ID_10052;
		_msgHalishaSpawn = NpcStringId.ID_10053;
		_msgHalishaDespawn = NpcStringId.ID_10056;
		_msgHalishaKill = NpcStringId.ID_10054;
		_msgHalishaKillOther = NpcStringId.ID_10055;
		_msgAttackerSpawn = NpcStringId.ID_10064;
		_msgAttackerDespawn = NpcStringId.ID_10065;
		_msgAttackerAttack1 = NpcStringId.ID_10066;
		_msgAttackerAttack16 = NpcStringId.ID_10067;
		_msgDefenderSpawn = NpcStringId.ID_10057;
		_msgDefenderDespawnWon = NpcStringId.ID_10061;
		_msgDefenderDespawnLost = NpcStringId.ID_10062;
		_msgDefenderCombat = NpcStringId.ID_10058;
		_msgDefenderCombatIdle1 = NpcStringId.ID_10059;
		_msgDefenderCombatIdle2 = NpcStringId.ID_10060;
		_msgDefenderReward = NpcStringId.ID_10063;
	}
}