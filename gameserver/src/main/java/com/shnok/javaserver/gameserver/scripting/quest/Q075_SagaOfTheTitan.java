package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q075_SagaOfTheTitan extends ThirdClassQuest
{
	public Q075_SagaOfTheTitan()
	{
		super(75, "Saga of the Titan", ClassId.TITAN);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7098;
		_itemOptional = 0;
		_itemReward = 7539;
		_itemAmulet1st = 7273;
		_itemAmulet2nd = 7304;
		_itemAmulet3rd = 7335;
		_itemAmulet4th = 7366;
		_itemHalishaMark = 7490;
		_itemAmulet5th = 7397;
		_itemAmulet6th = 7428;
		
		_npcMain = 31327;
		_npc1st = 31289;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31651;
		_npc3rd = 31290;
		_npcTablet4th = 31654;
		_npc4th = 31290;
		_npcTablet5th = 31655;
		_npcTablet6th = 31658;
		_npcDefender = 31607;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27292;
		_mobHalisha = 27224;
		_mobAttacker = 27283;
		
		_locCorrupted = new SpawnLocation(119518, -28658, -3811, -1);
		_locAttacker = new SpawnLocation(181215, 36676, -4812, -1);
		_locDefender = new SpawnLocation(181227, 36703, -4816, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7550;
		_msgCorruptedDespawn = NpcStringId.ID_7551;
		_msgCorruptedKill = NpcStringId.ID_7552;
		_msgHalishaSpawn = NpcStringId.ID_7553;
		_msgHalishaDespawn = NpcStringId.ID_7556;
		_msgHalishaKill = NpcStringId.ID_7554;
		_msgHalishaKillOther = NpcStringId.ID_7555;
		_msgAttackerSpawn = NpcStringId.ID_7564;
		_msgAttackerDespawn = NpcStringId.ID_7565;
		_msgAttackerAttack1 = NpcStringId.ID_7566;
		_msgAttackerAttack16 = NpcStringId.ID_7567;
		_msgDefenderSpawn = NpcStringId.ID_7557;
		_msgDefenderDespawnWon = NpcStringId.ID_7561;
		_msgDefenderDespawnLost = NpcStringId.ID_7562;
		_msgDefenderCombat = NpcStringId.ID_7558;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7559;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7560;
		_msgDefenderReward = NpcStringId.ID_7563;
	}
}