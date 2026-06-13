package com.shnok.javaserver.gameserver.enums.boats;

import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PlaySound;

public enum BoatSound
{
	ARRIVAL_DEPARTURE("itemsound.ship_arrival_departure"),
	LEAVE_5_MIN("itemsound.ship_5min"),
	LEAVE_1_MIN("itemsound.ship_1min");
	
	private String _sound;
	
	BoatSound(String sound)
	{
		_sound = sound;
	}
	
	public PlaySound get(Boat boat)
	{
		return new PlaySound(0, _sound, boat);
	}
}