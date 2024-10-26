package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q081_SagaOfTheGhostHunter extends ThirdClassQuest
{
	public Q081_SagaOfTheGhostHunter()
	{
		super(81, "Saga of the Ghost Hunter", ClassId.GHOST_HUNTER);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7104;
		_itemOptional = 0;
		_itemReward = 7518;
		_itemAmulet1st = 7279;
		_itemAmulet2nd = 7310;
		_itemAmulet3rd = 7341;
		_itemAmulet4th = 7372;
		_itemHalishaMark = 7496;
		_itemAmulet5th = 7403;
		_itemAmulet6th = 7434;
		
		_npcMain = 31603;
		_npc1st = 31286;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31653;
		_npc3rd = 31615;
		_npcTablet4th = 31654;
		_npc4th = 31616;
		_npcTablet5th = 31655;
		_npcTablet6th = 31656;
		_npcDefender = 31617;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27301;
		_mobHalisha = 27230;
		_mobAttacker = 27304;
		
		_locCorrupted = new SpawnLocation(162898, -76492, -3096, -1);
		_locAttacker = new SpawnLocation(47391, -56929, -2370, -1);
		_locDefender = new SpawnLocation(47429, -56923, -2383, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_8150;
		_msgCorruptedDespawn = NpcStringId.ID_8151;
		_msgCorruptedKill = NpcStringId.ID_8152;
		_msgHalishaSpawn = NpcStringId.ID_8153;
		_msgHalishaDespawn = NpcStringId.ID_8156;
		_msgHalishaKill = NpcStringId.ID_8154;
		_msgHalishaKillOther = NpcStringId.ID_8155;
		_msgAttackerSpawn = NpcStringId.ID_8164;
		_msgAttackerDespawn = NpcStringId.ID_8165;
		_msgAttackerAttack1 = NpcStringId.ID_8166;
		_msgAttackerAttack16 = NpcStringId.ID_8167;
		_msgDefenderSpawn = NpcStringId.ID_8157;
		_msgDefenderDespawnWon = NpcStringId.ID_8161;
		_msgDefenderDespawnLost = NpcStringId.ID_8162;
		_msgDefenderCombat = NpcStringId.ID_8158;
		_msgDefenderCombatIdle1 = NpcStringId.ID_8159;
		_msgDefenderCombatIdle2 = NpcStringId.ID_8160;
		_msgDefenderReward = NpcStringId.ID_8163;
	}
}