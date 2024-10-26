package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.actor.instance.Servitor;
import net.sf.l2j.gameserver.model.records.EffectHolder;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PartySpelled extends L2GameServerPacket
{
	private final int _type;
	private final int _objectId;
	private final List<EffectHolder> _effects = new ArrayList<>();
	
	public PartySpelled(Creature creature)
	{
		_type = (creature instanceof Servitor) ? 2 : (creature instanceof Pet) ? 1 : 0;
		_objectId = creature.getObjectId();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xee);
		
		writeD(_type);
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