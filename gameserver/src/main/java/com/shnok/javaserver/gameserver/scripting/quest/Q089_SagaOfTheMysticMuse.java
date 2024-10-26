package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q089_SagaOfTheMysticMuse extends ThirdClassQuest
{
	public Q089_SagaOfTheMysticMuse()
	{
		super(89, "Saga of the Mystic Muse", ClassId.MYSTIC_MUSE);
	}
	
	@Override
	protected void setItemsNpcsMobsLocs()
	{
		_itemMain = 7083;
		_itemOptional = 0;
		_itemReward = 7530;
		_itemAmulet1st = 7287;
		_itemAmulet2nd = 7318;
		_itemAmulet3rd = 7349;
		_itemAmulet4th = 7380;
		_itemHalishaMark = 7504;
		_itemAmulet5th = 7411;
		_itemAmulet6th = 7442;
		
		_npcMain = 30174;
		_npc1st = 31283;
		_npc2nd = 31627;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31651;
		_npc3rd = 31283;
		_npcTablet4th = 31654;
		_npc4th = 31283;
		_npcTablet5th = 31655;
		_npcTablet6th = 31658;
		_npcDefender = 31643;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27251;
		_mobHalisha = 27238;
		_mobAttacker = 27255;
		
		_locCorrupted = new SpawnLocation(119518, -28658, -3811, -1);
		_locAttacker = new SpawnLocation(181227, 36703, -4816, -1);
		_locDefender = new SpawnLocation(181215, 36676, -4812, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_8950;
		_msgCorruptedDespawn = NpcStringId.ID_8951;
		_msgCorruptedKill = NpcStringId.ID_8952;
		_msgHalishaSpawn = NpcStringId.ID_8953;
		_msgHalishaDespawn = NpcStringId.ID_8956;
		_msgHalishaKill = NpcStringId.ID_8954;
		_msgHalishaKillOther = NpcStringId.ID_8955;
		_msgAttackerSpawn = NpcStringId.ID_8964;
		_msgAttackerDespawn = NpcStringId.ID_8965;
		_msgAttackerAttack1 = NpcStringId.ID_8966;
		_msgAttackerAttack16 = NpcStringId.ID_8967;
		_msgDefenderSpawn = NpcStringId.ID_8957;
		_msgDefenderDespawnWon = NpcStringId.ID_8961;
		_msgDefenderDespawnLost = NpcStringId.ID_8962;
		_msgDefenderCombat = NpcStringId.ID_8958;
		_msgDefenderCombatIdle1 = NpcStringId.ID_8959;
		_msgDefenderCombatIdle2 = NpcStringId.ID_8960;
		_msgDefenderReward = NpcStringId.ID_8963;
	}
}