package com.shnok.javaserver.gameserver.geoengine.geodata;

import com.shnok.javaserver.gameserver.model.World;

public final class GeoStructure
{
	private GeoStructure()
	{
		throw new IllegalStateException("Utility class");
	}
	
	// Geo cell direction (nswe) flags.
	public static final byte CELL_FLAG_NONE = 0x00;
	public static final byte CELL_FLAG_E = 0x01;
	public static final byte CELL_FLAG_W = 0x02;
	public static final byte CELL_FLAG_S = 0x04;
	public static final byte CELL_FLAG_N = 0x08;
	public static final byte CELL_FLAG_ALL = 0x0F;
	
	// Geo cell height constants.
	public static final int CELL_SIZE = 16;
	public static final int CELL_HEIGHT = 8;
	public static final int CELL_IGNORE_HEIGHT = CELL_HEIGHT * 6;
	
	// Geo block type identification.
	public static final byte TYPE_FLAT_L2J_L2OFF = 0;
	public static final byte TYPE_COMPLEX_L2J = 1;
	public static final byte TYPE_COMPLEX_L2OFF = 0x40;
	public static final byte TYPE_MULTILAYER_L2J = 2;
	
	// Geo block dimensions.
	public static final int BLOCK_CELLS_X = 8;
	public static final int BLOCK_CELLS_Y = 8;
	public static final int BLOCK_CELLS = BLOCK_CELLS_X * BLOCK_CELLS_Y;
	
	// Geo region dimensions.
	public static final int REGION_BLOCKS_X = 256;
	public static final int REGION_BLOCKS_Y = 256;
	public static final int REGION_BLOCKS = REGION_BLOCKS_X * REGION_BLOCKS_Y;
	
	public static final int REGION_CELLS_X = REGION_BLOCKS_X * BLOCK_CELLS_X;
	public static final int REGION_CELLS_Y = REGION_BLOCKS_Y * BLOCK_CELLS_Y;
	
	// Geo world dimensions.
	public static final int GEO_REGIONS_X = (World.TILE_X_MAX - World.TILE_X_MIN + 1);
	public static final int GEO_REGIONS_Y = (World.TILE_Y_MAX - World.TILE_Y_MIN + 1);
	
	public static final int GEO_BLOCKS_X = GEO_REGIONS_X * REGION_BLOCKS_X;
	public static final int GEO_BLOCKS_Y = GEO_REGIONS_Y * REGION_BLOCKS_Y;
	
	public static final int GEO_CELLS_X = GEO_BLOCKS_X * BLOCK_CELLS_X;
	public static final int GEO_CELLS_Y = GEO_BLOCKS_Y * BLOCK_CELLS_Y;
}