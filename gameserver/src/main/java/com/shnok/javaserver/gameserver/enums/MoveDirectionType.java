package com.shnok.javaserver.gameserver.enums;

import com.shnok.javaserver.gameserver.geoengine.geodata.GeoStructure;

/**
 * Container of movement constants used for various geodata and movement checks.
 */
public enum MoveDirectionType
{
	N(0, -1),
	S(0, 1),
	W(-1, 0),
	E(1, 0),
	NW(-1, -1),
	SW(-1, 1),
	NE(1, -1),
	SE(1, 1);
	
	// Step and signum.
	final int _stepX;
	final int _stepY;
	final int _signumX;
	final int _signumY;
	
	// Cell offset.
	final int _offsetX;
	final int _offsetY;
	
	// Direction flags.
	final byte _directionX;
	final byte _directionY;
	final String _symbolX;
	final String _symbolY;
	
	private MoveDirectionType(int signumX, int signumY)
	{
		// Get step (world -16, 0, 16) and signum (geodata -1, 0, 1) coordinates.
		_stepX = signumX * GeoStructure.CELL_SIZE;
		_stepY = signumY * GeoStructure.CELL_SIZE;
		_signumX = signumX;
		_signumY = signumY;
		
		// Get border offsets in a direction of iteration.
		_offsetX = signumX >= 0 ? GeoStructure.CELL_SIZE - 1 : 0;
		_offsetY = signumY >= 0 ? GeoStructure.CELL_SIZE - 1 : 0;
		
		// Get direction NSWE flag and symbol.
		_directionX = signumX < 0 ? GeoStructure.CELL_FLAG_W : signumX == 0 ? 0 : GeoStructure.CELL_FLAG_E;
		_directionY = signumY < 0 ? GeoStructure.CELL_FLAG_N : signumY == 0 ? 0 : GeoStructure.CELL_FLAG_S;
		_symbolX = signumX < 0 ? "W" : signumX == 0 ? "-" : "E";
		_symbolY = signumY < 0 ? "N" : signumY == 0 ? "-" : "S";
	}
	
	public final int getStepX()
	{
		return _stepX;
	}
	
	public final int getStepY()
	{
		return _stepY;
	}
	
	public final int getSignumX()
	{
		return _signumX;
	}
	
	public final int getSignumY()
	{
		return _signumY;
	}
	
	public final int getOffsetX()
	{
		return _offsetX;
	}
	
	public final int getOffsetY()
	{
		return _offsetY;
	}
	
	public final byte getDirectionX()
	{
		return _directionX;
	}
	
	public final byte getDirectionY()
	{
		return _directionY;
	}
	
	public final String getSymbolX()
	{
		return _symbolX;
	}
	
	public final String getSymbolY()
	{
		return _symbolY;
	}
	
	/**
	 * @param gdx : Geodata X delta coordinate.
	 * @param gdy : Geodata Y delta coordinate.
	 * @return {@link MoveDirectionType} based on given geodata dx and dy delta coordinates.
	 */
	public static final MoveDirectionType getDirection(int gdx, int gdy)
	{
		if (gdx == 0)
			return (gdy < 0) ? MoveDirectionType.N : MoveDirectionType.S;
		
		if (gdy == 0)
			return (gdx < 0) ? MoveDirectionType.W : MoveDirectionType.E;
		
		if (gdx > 0)
			return (gdy < 0) ? MoveDirectionType.NE : MoveDirectionType.SE;
		
		return (gdy < 0) ? MoveDirectionType.NW : MoveDirectionType.SW;
	}
}