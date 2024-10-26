package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q070_SagaOfThePhoenixKnight extends ThirdClassQuest
{
	public Q070_SagaOfThePhoenixKnight()
	{
		super(70, "Saga of the Phoenix Knight", ClassId.PHOENIX_KNIGHT);
	}
	
	@Override
	protected final void setItemsNpcsMobsLocs()
	{
		_itemMain = 7093;
		_itemOptional = 6482;
		_itemReward = 7534;
		_itemAmulet1st = 7268;
		_itemAmulet2nd = 7299;
		_itemAmulet3rd = 7330;
		_itemAmulet4th = 7361;
		_itemHalishaMark = 7485;
		_itemAmulet5th = 7392;
		_itemAmulet6th = 7423;
		
		_npcMain = 30849;
		_npc1st = 31277;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31647;
		_npcTablet3rd = 31650;
		_npc3rd = 30849;
		_npcTablet4th = 31654;
		_npc4th = 31277;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31631;
		
		_mobGuardian = 27214;
		_mobCorrupted = 27286;
		_mobHalisha = 27219;
		_mobAttacker = 27278;
		
		_locCorrupted = new SpawnLocation(191046, -40640, -3042, -1);
		_locAttacker = new SpawnLocation(46087, -36372, -1685, -1);
		_locDefender = new SpawnLocation(46066, -36396, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7050;
		_msgCorruptedDespawn = NpcStringId.ID_7051;
		_msgCorruptedKill = NpcStringId.ID_7052;
		_msgHalishaSpawn = NpcStringId.ID_7053;
		_msgHalishaDespawn = NpcStringId.ID_7056;
		_msgHalishaKill = NpcStringId.ID_7054;
		_msgHalishaKillOther = NpcStringId.ID_7055;
		_msgAttackerSpawn = NpcStringId.ID_7064;
		_msgAttackerDespawn = NpcStringId.ID_7065;
		_msgAttackerAttack1 = NpcStringId.ID_7066;
		_msgAttackerAttack16 = NpcStringId.ID_7067;
		_msgDefenderSpawn = NpcStringId.ID_7057;
		_msgDefenderDespawnWon = NpcStringId.ID_7061;
		_msgDefenderDespawnLost = NpcStringId.ID_7062;
		_msgDefenderCombat = NpcStringId.ID_7058;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7059;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7060;
		_msgDefenderReward = NpcStringId.ID_7063;
	}
}