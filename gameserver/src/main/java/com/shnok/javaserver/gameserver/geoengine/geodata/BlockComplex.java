package com.shnok.javaserver.gameserver.geoengine.geodata;

import java.nio.ByteBuffer;

public class BlockComplex extends ABlock
{
	protected byte[] _buffer;
	
	/**
	 * Implicit constructor for children class.
	 */
	protected BlockComplex()
	{
		// Buffer is initialized in children class.
		_buffer = null;
	}
	
	/**
	 * Creates ComplexBlock.
	 * @param bb : Input byte buffer.
	 */
	public BlockComplex(ByteBuffer bb)
	{
		// Initialize buffer.
		_buffer = new byte[GeoStructure.BLOCK_CELLS * 3];
		
		// Load data.
		for (int i = 0; i < GeoStructure.BLOCK_CELLS; i++)
		{
			// Get data.
			short data = bb.getShort();
			
			// Get nswe.
			_buffer[i * 3] = (byte) (data & 0x000F);
			
			// Get height.
			data = (short) ((short) (data & 0xFFF0) >> 1);
			_buffer[i * 3 + 1] = (byte) (data & 0x00FF);
			_buffer[i * 3 + 2] = (byte) (data >> 8);
		}
	}
	
	@Override
	public final boolean hasGeoPos()
	{
		return true;
	}
	
	@Override
	public short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get cell index.
		final int index = ((geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y)) * 3;
		
		// Get height.
		return (short) (_buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8);
	}
	
	@Override
	public byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get cell index.
		final int index = ((geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y)) * 3;
		
		// Get nswe.
		return _buffer[index];
	}
	
	@Override
	public final int getIndexNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return ((geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y)) * 3;
	}
	
	@Override
	public int getIndexAbove(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get cell index.
		final int index = ((geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y)) * 3;
		
		// Get height.
		final int height = _buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8;
		
		// Check height and return nswe.
		return height > worldZ ? index : -1;
	}
	
	@Override
	public int getIndexBelow(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		// Get cell index.
		final int index = ((geoX % GeoStructure.BLOCK_CELLS_X) * GeoStructure.BLOCK_CELLS_Y + (geoY % GeoStructure.BLOCK_CELLS_Y)) * 3;
		
		// Get height.
		final int height = _buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8;
		
		// Check height and return nswe.
		return height < worldZ ? index : -1;
	}
	
	@Override
	public short getHeight(int index, IGeoObject ignore)
	{
		// Get height.
		return (short) (_buffer[index + 1] & 0x00FF | _buffer[index + 2] << 8);
	}
	
	@Override
	public byte getNswe(int index, IGeoObject ignore)
	{
		// Get nswe.
		return _buffer[index];
	}
}