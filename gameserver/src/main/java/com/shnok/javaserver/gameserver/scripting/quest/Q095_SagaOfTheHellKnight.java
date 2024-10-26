package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q095_SagaOfTheHellKnight extends ThirdClassQuest
{
	public Q095_SagaOfTheHellKnight()
	{
		super(95, "Saga of the Hell Knight", ClassId.HELL_KNIGHT);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7086;
		_itemOptional = 0;
		_itemReward = 7532;
		_itemAmulet1st = 7293;
		_itemAmulet2nd = 7324;
		_itemAmulet3rd = 7355;
		_itemAmulet4th = 7386;
		_itemHalishaMark = 7510;
		_itemAmulet5th = 7417;
		_itemAmulet6th = 7448;
		
		_npcMain = 31582;
		_npc1st = 31297;
		_npc2nd = 31623;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31647;
		_npcTablet3rd = 31653;
		_npc3rd = 31297;
		_npcTablet4th = 31654;
		_npc4th = 31297;
		_npcTablet5th = 31655;
		_npcTablet6th = 31656;
		_npcDefender = 31599;
		
		_mobGuardian = 27214;
		_mobCorrupted = 27258;
		_mobHalisha = 27244;
		_mobAttacker = 27263;
		
		_locCorrupted = new SpawnLocation(162898, -76492, -3096, -1);
		_locAttacker = new SpawnLocation(47391, -56929, -2370, -1);
		_locDefender = new SpawnLocation(47429, -56923, -2383, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9550;
		_msgCorruptedDespawn = NpcStringId.ID_9551;
		_msgCorruptedKill = NpcStringId.ID_9552;
		_msgHalishaSpawn = NpcStringId.ID_9553;
		_msgHalishaDespawn = NpcStringId.ID_9556;
		_msgHalishaKill = NpcStringId.ID_9554;
		_msgHalishaKillOther = NpcStringId.ID_9555;
		_msgAttackerSpawn = NpcStringId.ID_9564;
		_msgAttackerDespawn = NpcStringId.ID_9565;
		_msgAttackerAttack1 = NpcStringId.ID_9566;
		_msgAttackerAttack16 = NpcStringId.ID_9567;
		_msgDefenderSpawn = NpcStringId.ID_9557;
		_msgDefenderDespawnWon = NpcStringId.ID_9561;
		_msgDefenderDespawnLost = NpcStringId.ID_9562;
		_msgDefenderCombat = NpcStringId.ID_9558;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9559;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9560;
		_msgDefenderReward = NpcStringId.ID_9563;
	}
}