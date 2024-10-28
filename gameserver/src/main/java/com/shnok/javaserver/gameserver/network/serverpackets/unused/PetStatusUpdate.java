package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.actor.instance.Servitor;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public class PetStatusUpdate extends L2GameServerPacket
{
	private final Summon _summon;
	
	private int _maxFed;
	private int _curFed;
	
	public PetStatusUpdate(Summon summon)
	{
		_summon = summon;
		
		if (_summon instanceof Pet pet)
		{
			_curFed = pet.getCurrentFed();
			_maxFed = pet.getPetData().maxMeal();
		}
		else if (_summon instanceof Servitor servitor)
		{
			_curFed = servitor.getTimeRemaining();
			_maxFed = servitor.getTotalLifeTime();
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xb5);
		writeD(_summon.getSummonType());
		writeD(_summon.getObjectId());
		writeD(_summon.getX());
		writeD(_summon.getY());
		writeD(_summon.getZ());
		writeS(_summon.getTitle());
		writeD(_curFed);
		writeD(_maxFed);
		writeD((int) _summon.getStatus().getHp());
		writeD(_summon.getStatus().getMaxHp());
		writeD((int) _summon.getStatus().getMp());
		writeD(_summon.getStatus().getMaxMp());
		writeD(_summon.getStatus().getLevel());
		writeQ(_summon.getStatus().getExp());
		writeQ(_summon.getStatus().getExpForThisLevel());// 0% absolute value
		writeQ(_summon.getStatus().getExpForNextLevel());// 100% absolute value
	}
}