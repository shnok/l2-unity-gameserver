package com.shnok.javaserver.gameserver.model.boat;

import com.shnok.javaserver.gameserver.enums.boats.BoatDock;
import com.shnok.javaserver.gameserver.model.location.BoatLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;

public class BoatItinerary
{
	private final BoatRouteInfo[] _info = new BoatRouteInfo[2];
	private final int _heading;
	
	public BoatItinerary(BoatDock dock1, BoatDock dock2, int item1, int item2, int heading, BoatLocation[][] routes)
	{
		_info[0] = new BoatRouteInfo(routes[0], dock1, item1);
		_info[1] = (dock2 == null) ? null : new BoatRouteInfo(routes[1], dock2, item2);
		
		_heading = heading;
	}
	
	public int getHeading()
	{
		return _heading;
	}
	
	public BoatRouteInfo[] getInfo()
	{
		return _info;
	}
	
	public boolean isOneWay()
	{
		return _info[1] == null;
	}
	
	public void visualize(ExServerPrimitive debug)
	{
		_info[0].visualize(debug);
		
		if (!isOneWay())
			_info[1].visualize(debug);
	}
}