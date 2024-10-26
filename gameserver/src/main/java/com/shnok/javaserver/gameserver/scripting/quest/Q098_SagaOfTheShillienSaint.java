package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q098_SagaOfTheShillienSaint extends ThirdClassQuest
{
	public Q098_SagaOfTheShillienSaint()
	{
		super(98, "Saga of the Shillien Saint", ClassId.SHILLIEN_SAINT);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7090;
		_itemOptional = 0;
		_itemReward = 7525;
		_itemAmulet1st = 7296;
		_itemAmulet2nd = 7327;
		_itemAmulet3rd = 7358;
		_itemAmulet4th = 7389;
		_itemHalishaMark = 7513;
		_itemAmulet5th = 7420;
		_itemAmulet6th = 7451;
		
		_npcMain = 31581;
		_npc1st = 31588;
		_npc2nd = 31626;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31647;
		_npcTablet3rd = 31651;
		_npc3rd = 31287;
		_npcTablet4th = 31654;
		_npc4th = 31287;
		_npcTablet5th = 31655;
		_npcTablet6th = 31658;
		_npcDefender = 31621;
		
		_mobGuardian = 27214;
		_mobCorrupted = 27270;
		_mobHalisha = 27247;
		_mobAttacker = 27277;
		
		_locCorrupted = new SpawnLocation(119518, -28658, -3811, -1);
		_locAttacker = new SpawnLocation(181215, 36676, -4812, -1);
		_locDefender = new SpawnLocation(181227, 36703, -4816, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9850;
		_msgCorruptedDespawn = NpcStringId.ID_9851;
		_msgCorruptedKill = NpcStringId.ID_9852;
		_msgHalishaSpawn = NpcStringId.ID_9853;
		_msgHalishaDespawn = NpcStringId.ID_9856;
		_msgHalishaKill = NpcStringId.ID_9854;
		_msgHalishaKillOther = NpcStringId.ID_9855;
		_msgAttackerSpawn = NpcStringId.ID_9864;
		_msgAttackerDespawn = NpcStringId.ID_9865;
		_msgAttackerAttack1 = NpcStringId.ID_9866;
		_msgAttackerAttack16 = NpcStringId.ID_9867;
		_msgDefenderSpawn = NpcStringId.ID_9857;
		_msgDefenderDespawnWon = NpcStringId.ID_9861;
		_msgDefenderDespawnLost = NpcStringId.ID_9862;
		_msgDefenderCombat = NpcStringId.ID_9858;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9859;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9860;
		_msgDefenderReward = NpcStringId.ID_9863;
	}
}