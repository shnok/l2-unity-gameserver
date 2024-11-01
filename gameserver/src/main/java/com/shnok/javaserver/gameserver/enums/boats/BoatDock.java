package com.shnok.javaserver.gameserver.enums.boats;

import com.shnok.javaserver.commons.geometry.basic.Line2D;

import com.shnok.javaserver.gameserver.model.location.BoatLocation;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;

public enum BoatDock
{
	TALKING_ISLAND(new BoatLocation(-96622, 261660, -3610, 150, 1800), new Location(-96777, 258970, -3623), new Line2D(-96622, 261420, -96862, 261420), new Line2D(-96622, 261900, -96862, 261900), new Line2D(230, 0, 230, -260), true),
	GLUDIN(new BoatLocation(-95686, 150514, -3610, 150, 800), new Location(-90015, 150422, -3610), new Line2D(-95446, 150522, -95454, 150762), new Line2D(-95926, 150506, -95934, 150746), new Line2D(-230, 0, -230, -260), true),
	RUNE(new BoatLocation(34381, -37680, -3610, 220, 800), new Location(34513, -38009, -3640), new Line2D(34548, -37853, 34375, -38019), new Line2D(34214, -37507, 34042, -37674), new Line2D(230, 0, 230, -260), true),
	GIRAN(new BoatLocation(48950, 190613, -3610, 150, 800), new Location(46763, 187041, -3451), new Line2D(48845, 190397, 49060, 190292), new Line2D(49055, 190829, 49271, 190723), new Line2D(-230, 0, -230, -260), false),
	PRIMEVAL(new BoatLocation(10342, -27279, -3610, 150, 1800), new Location(10447, -24982, -3664), new Line2D(10395, -27034, 10538, -27035), new Line2D(10342, -27519, 10582, -27519), new Line2D(230, 0, 230, -260), false),
	INNADRIL(new BoatLocation(111384, 226232, -3610, 150, 800), new Location(107092, 219098, -3952), new Line2D(111137, 225989, 111387, 225991), new Line2D(111384, 226472, 111144, 226472), new Line2D(230, -260, 230, 0), false);
	
	public static final BoatDock[] VALUES = values();
	
	private static final double IN_RATIO = 1.2;
	private static final double OUT_RATIO = 0.9;
	
	private final BoatLocation _dockLoc;
	private final Location _oustLoc;
	
	private final Line2D _axisLine;
	private final Line2D _boatEntrance;
	private final Line2D _boatExit;
	
	private final boolean _isBusyOnStart;
	
	private final double _angle;
	private final double _factor;
	
	private boolean _isBusy;
	
	BoatDock(BoatLocation dockLoc, Location oustLoc, Line2D dockEntrance, Line2D boatExit, Line2D boatEntrance, boolean busyOnStart)
	{
		_dockLoc = dockLoc;
		_oustLoc = oustLoc;
		_boatEntrance = dockEntrance;
		_boatExit = boatExit;
		_axisLine = boatEntrance;
		_isBusyOnStart = busyOnStart;
		
		final Point2D boatPoint = _boatEntrance.getPoint();
		final Point2D axisPoint = _axisLine.getPoint();
		
		_angle = boatPoint.calculateRelativeAngle(axisPoint);
		_factor = boatPoint.length() / axisPoint.length();
	}
	
	public BoatLocation getDockLoc()
	{
		return _dockLoc;
	}
	
	public Location getOustLoc()
	{
		return _oustLoc;
	}
	
	public Line2D getBoatEntrance()
	{
		return _boatEntrance;
	}
	
	public Line2D getBoatExit()
	{
		return _boatExit;
	}
	
	public boolean isBusyOnStart()
	{
		return _isBusyOnStart;
	}
	
	public boolean isBusy()
	{
		return _isBusyOnStart && _isBusy;
	}
	
	public void setBusy(boolean status)
	{
		if (_isBusyOnStart)
			_isBusy = status;
	}
	
	public static Point2D getBoardingPoint(Line2D line, Point2D origin, Point2D dest, boolean isInBoat)
	{
		return line.getAdjustedIntersectionPoint(origin, dest, isInBoat ? IN_RATIO : OUT_RATIO);
	}
	
	public Point2D getBoardingPoint(Point2D origin, Point2D dest, boolean isInBoat)
	{
		return getBoardingPoint(_boatEntrance, origin, dest, isInBoat);
	}
	
	public Point2D getAdjustedBoardingPoint(Point2D origin, Point2D dest, boolean isInBoat)
	{
		final Point2D boardingPoint = getBoardingPoint(origin, dest, isInBoat);
		if (boardingPoint == null)
		{
			final double ratio = isInBoat ? IN_RATIO : OUT_RATIO;
			final int x = (int) Math.round(origin.getX() + ratio * (dest.getX() - origin.getX()));
			final int y = (int) Math.round(origin.getY() + ratio * (dest.getY() - origin.getY()));
			return new Point2D(x, y);
		}
		return boardingPoint;
	}
	
	public Point2D convertBoatToWorldCoordinates(int x, int y)
	{
		final Point2D point = new Point2D(x - _axisLine.getP1x(), y - _axisLine.getP1y());
		point.rotate(_angle);
		point.scale(_factor);
		
		point.setX(_boatEntrance.getP1x() + point.getX());
		point.setY(_boatEntrance.getP1y() + point.getY());
		return point;
	}
	
	public Point2D convertWorldToBoatCoordinates(int x, int y)
	{
		final Point2D point = new Point2D(x - _boatEntrance.getP1x(), y - _boatEntrance.getP1y());
		point.rotate(-_angle);
		point.scale(1 / _factor);
		
		point.setX(_axisLine.getP1x() + point.getX());
		point.setY(_axisLine.getP1y() + point.getY());
		return point;
	}
}