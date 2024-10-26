package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.EffectHolder;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ExOlympiadSpelledInfo extends L2GameServerPacket
{
	private final int _objectId;
	private final List<EffectHolder> _effects = new ArrayList<>();
	
	public ExOlympiadSpelledInfo(Player player)
	{
		_objectId = player.getObjectId();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xfe);
		writeH(0x2a);
		
		writeD(_objectId);
		
		writeD(_effects.size());
		
		for (EffectHolder effect : _effects)
			writeEffect(effect, false);
	}
	
	public void addEffect(L2Skill skill, int duration)
	{
		_effects.add(new EffectHolder(skill, duration));
	}
}