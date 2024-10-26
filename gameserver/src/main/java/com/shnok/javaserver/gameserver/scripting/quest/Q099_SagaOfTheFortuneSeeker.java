package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q099_SagaOfTheFortuneSeeker extends ThirdClassQuest
{
	public Q099_SagaOfTheFortuneSeeker()
	{
		super(99, "Saga of the Fortune Seeker", ClassId.FORTUNE_SEEKER);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7109;
		_itemOptional = 0;
		_itemReward = 7608;
		_itemAmulet1st = 7297;
		_itemAmulet2nd = 7328;
		_itemAmulet3rd = 7359;
		_itemAmulet4th = 7390;
		_itemHalishaMark = 7514;
		_itemAmulet5th = 7421;
		_itemAmulet6th = 7452;
		
		_npcMain = 31594;
		_npc1st = 31600;
		_npc2nd = 31623;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31649;
		_npcTablet3rd = 31650;
		_npc3rd = 31600;
		_npcTablet4th = 31654;
		_npc4th = 31600;
		_npcTablet5th = 31655;
		_npcTablet6th = 31657;
		_npcDefender = 31601;
		
		_mobGuardian = 27216;
		_mobCorrupted = 27259;
		_mobHalisha = 27248;
		_mobAttacker = 27309;
		
		_locCorrupted = new SpawnLocation(191046, -40640, -3042, -1);
		_locAttacker = new SpawnLocation(46066, -36396, -1685, -1);
		_locDefender = new SpawnLocation(46087, -36372, -1685, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9950;
		_msgCorruptedDespawn = NpcStringId.ID_9951;
		_msgCorruptedKill = NpcStringId.ID_9952;
		_msgHalishaSpawn = NpcStringId.ID_9953;
		_msgHalishaDespawn = NpcStringId.ID_9956;
		_msgHalishaKill = NpcStringId.ID_9954;
		_msgHalishaKillOther = NpcStringId.ID_9955;
		_msgAttackerSpawn = NpcStringId.ID_9964;
		_msgAttackerDespawn = NpcStringId.ID_9965;
		_msgAttackerAttack1 = NpcStringId.ID_9966;
		_msgAttackerAttack16 = NpcStringId.ID_9967;
		_msgDefenderSpawn = NpcStringId.ID_9957;
		_msgDefenderDespawnWon = NpcStringId.ID_9961;
		_msgDefenderDespawnLost = NpcStringId.ID_9962;
		_msgDefenderCombat = NpcStringId.ID_9958;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9959;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9960;
		_msgDefenderReward = NpcStringId.ID_9963;
	}
}