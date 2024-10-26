package com.shnok.javaserver.gameserver.scripting.quest;

import net.sf.l2j.gameserver.enums.actors.ClassId;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.NpcStringId;

public class Q071_SagaOfEvasTemplar extends ThirdClassQuest
{
	public Q071_SagaOfEvasTemplar()
	{
		super(71, "Saga of Eva's Templar", ClassId.EVAS_TEMPLAR);
	}
	
	@Override
	protected final void setItemsNpcsMobsLocs()
	{
		_itemMain = 7094;
		_itemOptional = 6482;
		_itemReward = 7535;
		_itemAmulet1st = 7269;
		_itemAmulet2nd = 7300;
		_itemAmulet3rd = 7331;
		_itemAmulet4th = 7362;
		_itemHalishaMark = 7486;
		_itemAmulet5th = 7393;
		_itemAmulet6th = 7424;
		
		_npcMain = 30852;
		_npc1st = 31278;
		_npc2nd = 31624;
		_npcTablet1st = 31646;
		_npcTablet2nd = 31648;
		_npcTablet3rd = 31651;
		_npc3rd = 30852;
		_npcTablet4th = 31654;
		_npc4th = 31281;
		_npcTablet5th = 31655;
		_npcTablet6th = 31658;
		_npcDefender = 31638;
		
		_mobGuardian = 27215;
		_mobCorrupted = 27287;
		_mobHalisha = 27220;
		_mobAttacker = 27279;
		
		_locCorrupted = new SpawnLocation(119518, -28658, -3811, -1);
		_locAttacker = new SpawnLocation(181215, 36676, -4812, -1);
		_locDefender = new SpawnLocation(181227, 36703, -4816, -1);
		
		_msgCorruptedSpawn = NpcStringId.ID_7150;
		_msgCorruptedDespawn = NpcStringId.ID_7151;
		_msgCorruptedKill = NpcStringId.ID_7152;
		_msgHalishaSpawn = NpcStringId.ID_7153;
		_msgHalishaDespawn = NpcStringId.ID_7156;
		_msgHalishaKill = NpcStringId.ID_7154;
		_msgHalishaKillOther = NpcStringId.ID_7155;
		_msgAttackerSpawn = NpcStringId.ID_7164;
		_msgAttackerDespawn = NpcStringId.ID_7165;
		_msgAttackerAttack1 = NpcStringId.ID_7166;
		_msgAttackerAttack16 = NpcStringId.ID_7167;
		_msgDefenderSpawn = NpcStringId.ID_7157;
		_msgDefenderDespawnWon = NpcStringId.ID_7161;
		_msgDefenderDespawnLost = NpcStringId.ID_7162;
		_msgDefenderCombat = NpcStringId.ID_7158;
		_msgDefenderCombatIdle1 = NpcStringId.ID_7159;
		_msgDefenderCombatIdle2 = NpcStringId.ID_7160;
		_msgDefenderReward = NpcStringId.ID_7163;
	}
}