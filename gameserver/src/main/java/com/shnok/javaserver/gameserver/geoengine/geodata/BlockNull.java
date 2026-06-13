package com.shnok.javaserver.gameserver.geoengine.geodata;

public class BlockNull extends ABlock
{
	@Override
	public final boolean hasGeoPos()
	{
		return false;
	}
	
	@Override
	public final short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return (short) worldZ;
	}
	
	@Override
	public final byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return GeoStructure.CELL_FLAG_ALL;
	}
	
	@Override
	public final int getIndexNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final int getIndexAbove(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final int getIndexBelow(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final short getHeight(int index, IGeoObject ignore)
	{
		return 0;
	}
	
	@Override
	public final byte getNswe(int index, IGeoObject ignore)
	{
		return GeoStructure.CELL_FLAG_ALL;
	}
}