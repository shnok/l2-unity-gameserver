package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q088_SagaOfTheArchmage extends ThirdClassQuest
{
	public Q088_SagaOfTheArchmage()
	{
		super(88, "Saga of the Archmage", ClassId.ARCHMAGE);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7082;
		_itemOptional = 0;
		_itemReward = 7529;
		_itemAmulet1st = 7286;
		_itemAmulet2nd = 7317;
		_itemAmulet3rd = 7348;
		_itemAmulet4th = 7379;
		_itemHalishaMark = 7503;
		_itemAmulet5th = 7410;
		_itemAmulet6th = 7441;
		
		_npcMain = 30176;
		_npc1st = 31282;
		_npc2nd = 31627;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31647;
		_npcTablet3rd = 31650;
		_npc3rd = 31282;
		_npcTablet4th = 31654;
		_npc4th = 31282;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31590;
		
		_mobGuardian = 27214;
		_mobCorrupted = 27250;
		_mobHalisha = 27237;
		_mobAttacker = 27254;
		
		_locCorrupted = new SpawnLocation(191046, -40640, -3042, -1);
		_locAttacker = new SpawnLocation(46066, -36396, -1685, -1);
		_locDefender = new SpawnLocation(46087, -36372, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_8850;
		_msgCorruptedDespawn = NpcStringId.ID_8851;
		_msgCorruptedKill = NpcStringId.ID_8852;
		_msgHalishaSpawn = NpcStringId.ID_8853;
		_msgHalishaDespawn = NpcStringId.ID_8856;
		_msgHalishaKill = NpcStringId.ID_8854;
		_msgHalishaKillOther = NpcStringId.ID_8855;
		_msgAttackerSpawn = NpcStringId.ID_8864;
		_msgAttackerDespawn = NpcStringId.ID_8865;
		_msgAttackerAttack1 = NpcStringId.ID_8866;
		_msgAttackerAttack16 = NpcStringId.ID_8867;
		_msgDefenderSpawn = NpcStringId.ID_8857;
		_msgDefenderDespawnWon = NpcStringId.ID_8861;
		_msgDefenderDespawnLost = NpcStringId.ID_8862;
		_msgDefenderCombat = NpcStringId.ID_8858;
		_msgDefenderCombatIdle1 = NpcStringId.ID_8859;
		_msgDefenderCombatIdle2 = NpcStringId.ID_8860;
		_msgDefenderReward = NpcStringId.ID_8863;
	}
}