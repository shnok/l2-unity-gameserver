package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q096_SagaOfTheSpectralDancer extends ThirdClassQuest
{
	public Q096_SagaOfTheSpectralDancer()
	{
		super(96, "Saga of the Spectral Dancer", ClassId.SPECTRAL_DANCER);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7092;
		_itemOptional = 0;
		_itemReward = 7527;
		_itemAmulet1st = 7294;
		_itemAmulet2nd = 7325;
		_itemAmulet3rd = 7356;
		_itemAmulet4th = 7387;
		_itemHalishaMark = 7511;
		_itemAmulet5th = 7418;
		_itemAmulet6th = 7449;
		
		_npcMain = 31582;
		_npc1st = 31284;
		_npc2nd = 31623;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31653;
		_npc3rd = 31284;
		_npcTablet4th = 31654;
		_npc4th = 31284;
		_npcTablet5th = 31655;
		_npcTablet6th = 31656;
		_npcDefender = 31611;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27272;
		_mobHalisha = 27245;
		_mobAttacker = 27264;
		
		_locCorrupted = new SpawnLocation(162898, -76492, -3096, -1);
		_locAttacker = new SpawnLocation(47429, -56923, -2383, -1);
		_locDefender = new SpawnLocation(47391, -56929, -2370, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9650;
		_msgCorruptedDespawn = NpcStringId.ID_9651;
		_msgCorruptedKill = NpcStringId.ID_9652;
		_msgHalishaSpawn = NpcStringId.ID_9653;
		_msgHalishaDespawn = NpcStringId.ID_9656;
		_msgHalishaKill = NpcStringId.ID_9654;
		_msgHalishaKillOther = NpcStringId.ID_9655;
		_msgAttackerSpawn = NpcStringId.ID_9664;
		_msgAttackerDespawn = NpcStringId.ID_9665;
		_msgAttackerAttack1 = NpcStringId.ID_9666;
		_msgAttackerAttack16 = NpcStringId.ID_9667;
		_msgDefenderSpawn = NpcStringId.ID_9657;
		_msgDefenderDespawnWon = NpcStringId.ID_9661;
		_msgDefenderDespawnLost = NpcStringId.ID_9662;
		_msgDefenderCombat = NpcStringId.ID_9658;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9659;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9660;
		_msgDefenderReward = NpcStringId.ID_9663;
	}
}