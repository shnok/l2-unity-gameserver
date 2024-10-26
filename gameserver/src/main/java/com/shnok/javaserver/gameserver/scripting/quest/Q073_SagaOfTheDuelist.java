package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q073_SagaOfTheDuelist extends ThirdClassQuest
{
	public Q073_SagaOfTheDuelist()
	{
		super(73, "Saga of the Duelist", ClassId.DUELIST);
	}
	
	@Override
	protected final void setItemsNpcsMobsLocs()
	{
		_itemMain = 7096;
		_itemOptional = 7546;
		_itemReward = 7537;
		_itemAmulet1st = 7271;
		_itemAmulet2nd = 7302;
		_itemAmulet3rd = 7333;
		_itemAmulet4th = 7364;
		_itemHalishaMark = 7488;
		_itemAmulet5th = 7395;
		_itemAmulet6th = 7426;
		
		_npcMain = 30849;
		_npc1st = 31226;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31647;
		_npcTablet3rd = 31653;
		_npc3rd = 31331;
		_npcTablet4th = 31654;
		_npc4th = 31277;
		_npcTablet5th = 31655;
		_npcTablet6th = 31656;
		_npcDefender = 31639;
		
		_mobGuardian = 27214;
		_mobCorrupted = 27289;
		_mobHalisha = 27222;
		_mobAttacker = 27281;
		
		_locCorrupted = new SpawnLocation(162898, -76492, -3096, -1);
		_locAttacker = new SpawnLocation(47429, -56923, -2383, -1);
		_locDefender = new SpawnLocation(47391, -56929, -2370, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7350;
		_msgCorruptedDespawn = NpcStringId.ID_7351;
		_msgCorruptedKill = NpcStringId.ID_7352;
		_msgHalishaSpawn = NpcStringId.ID_7353;
		_msgHalishaDespawn = NpcStringId.ID_7356;
		_msgHalishaKill = NpcStringId.ID_7354;
		_msgHalishaKillOther = NpcStringId.ID_7355;
		_msgAttackerSpawn = NpcStringId.ID_7364;
		_msgAttackerDespawn = NpcStringId.ID_7365;
		_msgAttackerAttack1 = NpcStringId.ID_7366;
		_msgAttackerAttack16 = NpcStringId.ID_7367;
		_msgDefenderSpawn = NpcStringId.ID_7357;
		_msgDefenderDespawnWon = NpcStringId.ID_7361;
		_msgDefenderDespawnLost = NpcStringId.ID_7362;
		_msgDefenderCombat = NpcStringId.ID_7358;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7359;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7360;
		_msgDefenderReward = NpcStringId.ID_7363;
	}
}