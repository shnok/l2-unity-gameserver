package com.shnok.javaserver.gameserver.model.zone.type;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;

/**
 * A zone extending {@link ZoneType} used by Derby Track system.<br>
 * <br>
 * The zone shares peace, no summon and monster track behaviors.
 */
public class DerbyTrackZone extends ZoneType
{
	public DerbyTrackZone(int id)
	{
		super(id);
	}
	
	@Override
	protected void onEnter(Creature creature)
	{
		if (creature instanceof Playable playable)
		{
			playable.setInsideZone(ZoneId.MONSTER_TRACK, true);
			playable.setInsideZone(ZoneId.PEACE, true);
			playable.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
		}
	}
	
	@Override
	protected void onExit(Creature creature)
	{
		if (creature instanceof Playable playable)
		{
			playable.setInsideZone(ZoneId.MONSTER_TRACK, false);
			playable.setInsideZone(ZoneId.PEACE, false);
			playable.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
		}
	}
}