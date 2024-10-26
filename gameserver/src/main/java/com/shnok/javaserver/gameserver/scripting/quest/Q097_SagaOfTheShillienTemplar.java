package com.shnok.javaserver.gameserver.scripting.quest;

import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.NpcStringId;

public class Q097_SagaOfTheShillienTemplar extends ThirdClassQuest
{
	public Q097_SagaOfTheShillienTemplar()
	{
		super(97, "Saga of the Shillien Templar", ClassId.SHILLIEN_TEMPLAR);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7091;
		_itemOptional = 0;
		_itemReward = 7526;
		_itemAmulet1st = 7295;
		_itemAmulet2nd = 7326;
		_itemAmulet3rd = 7357;
		_itemAmulet4th = 7388;
		_itemHalishaMark = 7512;
		_itemAmulet5th = 7419;
		_itemAmulet6th = 7450;
		
		_npcMain = 31580;
		_npc1st = 31285;
		_npc2nd = 31623;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31652;
		_npc3rd = 31285;
		_npcTablet4th = 31654;
		_npc4th = 31285;
		_npcTablet5th = 31655;
		_npcTablet6th = 31659;
		_npcDefender = 31610;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27271;
		_mobHalisha = 27246;
		_mobAttacker = 27273;
		
		_locCorrupted = new SpawnLocation(161719, -92823, -1893, -1);
		_locAttacker = new SpawnLocation(124355, 82155, -2803, -1);
		_locDefender = new SpawnLocation(124376, 82127, -2796, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_9750;
		_msgCorruptedDespawn = NpcStringId.ID_9751;
		_msgCorruptedKill = NpcStringId.ID_9752;
		_msgHalishaSpawn = NpcStringId.ID_9753;
		_msgHalishaDespawn = NpcStringId.ID_9756;
		_msgHalishaKill = NpcStringId.ID_9754;
		_msgHalishaKillOther = NpcStringId.ID_9755;
		_msgAttackerSpawn = NpcStringId.ID_9764;
		_msgAttackerDespawn = NpcStringId.ID_9765;
		_msgAttackerAttack1 = NpcStringId.ID_9766;
		_msgAttackerAttack16 = NpcStringId.ID_9767;
		_msgDefenderSpawn = NpcStringId.ID_9757;
		_msgDefenderDespawnWon = NpcStringId.ID_9761;
		_msgDefenderDespawnLost = NpcStringId.ID_9762;
		_msgDefenderCombat = NpcStringId.ID_9758;
		_msgDefenderCombatIdle1 = NpcStringId.ID_9759;
		_msgDefenderCombatIdle2 = NpcStringId.ID_9760;
		_msgDefenderReward = NpcStringId.ID_9763;
	}
}