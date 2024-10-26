package com.shnok.javaserver.gameserver.geoengine.geodata;

import java.nio.ByteBuffer;

import com.shnok.javaserver.gameserver.enums.GeoType;

public class BlockFlat extends ABlock
{
	protected final short _height;
	protected byte _nswe;
	
	/**
	 * Creates FlatBlock.
	 * @param bb : Input byte buffer.
	 * @param type : The type of loaded geodata.
	 */
	public BlockFlat(ByteBuffer bb, GeoType type)
	{
		// Get height and nswe.
		_height = bb.getShort();
		_nswe = GeoStructure.CELL_FLAG_ALL;
		
		// Read dummy data.
		if (type == GeoType.L2OFF)
			bb.getShort();
	}
	
	@Override
	public final boolean hasGeoPos()
	{
		return true;
	}
	
	@Override
	public final short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return _height;
	}
	
	@Override
	public final byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return _nswe;
	}
	
	@Override
	public final int getIndexNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final int getIndexAbove(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Check height and return index.
		return _height > worldZ ? 0 : -1;
	}
	
	@Override
	public final int getIndexBelow(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Check height and return index.
		return _height < worldZ ? 0 : -1;
	}
	
	@Override
	public final short getHeight(int index, IGeoObject ignore)
	{
		return _height;
	}
	
	@Override
	public final byte getNswe(int index, IGeoObject ignore)
	{
		return _nswe;
	}
}