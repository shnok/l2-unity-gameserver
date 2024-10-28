package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.shnok.javaserver.gameserver.model.records.EffectHolder;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AbnormalStatusUpdate extends L2GameServerPacket
{
	private final List<EffectHolder> _effects = new ArrayList<>();
	private final Set<EffectHolder> _toggles = new TreeSet<>(Comparator.comparing(EffectHolder::id));
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x7f);
		
		writeH(_effects.size() + _toggles.size());
		
		for (EffectHolder effect : _effects)
			writeEffect(effect, effect.duration() == -1);
		
		for (EffectHolder effect : _toggles)
			writeEffect(effect, true);
	}
	
	public void addEffect(L2Skill skill, int duration)
	{
		final EffectHolder eh = new EffectHolder(skill, duration);
		
		if (skill.isToggle())
			_toggles.add(eh);
		else
			_effects.add(eh);
	}
}