package com.shnok.javaserver.gameserver.geoengine;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.shnok.javaserver.commons.config.ExProperties;
import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.GeoType;
import com.shnok.javaserver.gameserver.enums.MoveDirectionType;
import com.shnok.javaserver.gameserver.geoengine.geodata.ABlock;
import com.shnok.javaserver.gameserver.geoengine.geodata.BlockComplex;
import com.shnok.javaserver.gameserver.geoengine.geodata.BlockComplexDynamic;
import com.shnok.javaserver.gameserver.geoengine.geodata.BlockFlat;
import com.shnok.javaserver.gameserver.geoengine.geodata.BlockMultilayer;
import com.shnok.javaserver.gameserver.geoengine.geodata.BlockMultilayerDynamic;
import com.shnok.javaserver.gameserver.geoengine.geodata.BlockNull;
import com.shnok.javaserver.gameserver.geoengine.geodata.GeoStructure;
import com.shnok.javaserver.gameserver.geoengine.geodata.IBlockDynamic;
import com.shnok.javaserver.gameserver.geoengine.geodata.IGeoObject;
import com.shnok.javaserver.gameserver.geoengine.pathfinding.PathFinder;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.ExServerPrimitive;

public class GeoEngine
{
	private static final CLogger LOGGER = new CLogger(GeoEngine.class.getName());
	
	private static final String GEO_BUG = "%d;%d;%d;%d;%d;%d;%d;%s\r\n";
	
	private final ABlock[][] _blocks;
	private final BlockNull _nullBlock;
	
	private final PrintWriter _geoBugReports;
	
	/**
	 * GeoEngine constructor. Loads all geodata files based on geoengine.properties config.
	 */
	public GeoEngine()
	{
		// initialize block container
		_blocks = new ABlock[GeoStructure.GEO_BLOCKS_X][GeoStructure.GEO_BLOCKS_Y];
		
		// load null block
		_nullBlock = new BlockNull();
		
		// initialize multilayer temporarily buffer
		BlockMultilayer.initialize();
		
		// load geo files according to geoengine config setup
		final ExProperties props = Config.initProperties(Config.GEOENGINE_FILE);
		int loaded = 0;
		int failed = 0;
		for (int rx = World.TILE_X_MIN; rx <= World.TILE_X_MAX; rx++)
		{
			for (int ry = World.TILE_Y_MIN; ry <= World.TILE_Y_MAX; ry++)
			{
				if (props.containsKey(rx + "_" + ry))
				{
					// region file is load-able, try to load it
					if (loadGeoBlocks(rx, ry))
						loaded++;
					else
						failed++;
				}
				else
				{
					// region file is not load-able, load null blocks
					loadNullBlocks(rx, ry);
				}
			}
		}
		LOGGER.info("Loaded {} {} region files.", loaded, Config.GEODATA_TYPE);
		
		// release multilayer block temporarily buffer
		BlockMultilayer.release();
		
		if (failed > 0)
		{
			LOGGER.warn("Failed to load {} {} region files. Please consider to check your \"geodata.properties\" settings and location of your geodata files.", failed, Config.GEODATA_TYPE);
			System.exit(1);
		}
		
		// initialize bug reports
		PrintWriter writer = null;
		try
		{
			writer = new PrintWriter(new FileOutputStream(new File(Config.GEODATA_PATH + "geo_bugs.txt"), true), true);
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load \"geo_bugs.txt\" file.", e);
		}
		_geoBugReports = writer;
	}
	
	/**
	 * Loads geodata from a file. When file does not exist, is corrupted or not consistent, loads none geodata.
	 * @param regionX : Geodata file region X coordinate.
	 * @param regionY : Geodata file region Y coordinate.
	 * @return boolean : True, when geodata file was loaded without problem.
	 */
	private final boolean loadGeoBlocks(int regionX, int regionY)
	{
		final String filename = String.format(Config.GEODATA_TYPE.getFilename(), regionX, regionY);
		final String filepath = Config.GEODATA_PATH + filename;
		
		// standard load
		try (RandomAccessFile raf = new RandomAccessFile(filepath, "r");
			FileChannel fc = raf.getChannel())
		{
			// initialize file buffer
			MappedByteBuffer buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load();
			buffer.order(ByteOrder.LITTLE_ENDIAN);
			
			// load 18B header for L2OFF geodata (1st and 2nd byte...region X and Y)
			if (Config.GEODATA_TYPE == GeoType.L2OFF)
			{
				for (int i = 0; i < 18; i++)
					buffer.get();
			}
			
			// get block indexes
			final int blockX = (regionX - World.TILE_X_MIN) * GeoStructure.REGION_BLOCKS_X;
			final int blockY = (regionY - World.TILE_Y_MIN) * GeoStructure.REGION_BLOCKS_Y;
			
			// loop over region blocks
			for (int ix = 0; ix < GeoStructure.REGION_BLOCKS_X; ix++)
			{
				for (int iy = 0; iy < GeoStructure.REGION_BLOCKS_Y; iy++)
				{
					if (Config.GEODATA_TYPE == GeoType.L2J)
					{
						// get block type
						final byte type = buffer.get();
						
						// load block according to block type
						switch (type)
						{
							case GeoStructure.TYPE_FLAT_L2J_L2OFF:
								_blocks[blockX + ix][blockY + iy] = new BlockFlat(buffer, Config.GEODATA_TYPE);
								break;
							
							case GeoStructure.TYPE_COMPLEX_L2J:
								_blocks[blockX + ix][blockY + iy] = new BlockComplex(buffer);
								break;
							
							case GeoStructure.TYPE_MULTILAYER_L2J:
								_blocks[blockX + ix][blockY + iy] = new BlockMultilayer(buffer, Config.GEODATA_TYPE);
								break;
							
							default:
								throw new IllegalArgumentException("Unknown block type: " + type);
						}
					}
					else
					{
						// get block type
						final short type = buffer.getShort();
						
						// load block according to block type
						switch (type)
						{
							case GeoStructure.TYPE_FLAT_L2J_L2OFF:
								_blocks[blockX + ix][blockY + iy] = new BlockFlat(buffer, Config.GEODATA_TYPE);
								break;
							
							case GeoStructure.TYPE_COMPLEX_L2OFF:
								_blocks[blockX + ix][blockY + iy] = new BlockComplex(buffer);
								break;
							
							default:
								_blocks[blockX + ix][blockY + iy] = new BlockMultilayer(buffer, Config.GEODATA_TYPE);
								break;
						}
					}
				}
			}
			
			// check data consistency
			if (buffer.remaining() > 0)
				LOGGER.warn("Region file {} can be corrupted, remaining {} bytes to read.", filename, buffer.remaining());
			
			// loading was successful
			return true;
		}
		catch (Exception e)
		{
			// an error occured while loading, load null blocks
			LOGGER.error("Error loading {} region file.", e, filename);
			
			// replace whole region file with null blocks
			loadNullBlocks(regionX, regionY);
			
			// loading was not successful
			return false;
		}
	}
	
	/**
	 * Loads null blocks. Used when no region file is detected or an error occurs during loading.
	 * @param regionX : Geodata file region X coordinate.
	 * @param regionY : Geodata file region Y coordinate.
	 */
	private final void loadNullBlocks(int regionX, int regionY)
	{
		// get block indexes
		final int blockX = (regionX - World.TILE_X_MIN) * GeoStructure.REGION_BLOCKS_X;
		final int blockY = (regionY - World.TILE_Y_MIN) * GeoStructure.REGION_BLOCKS_Y;
		
		// load all null blocks
		for (int ix = 0; ix < GeoStructure.REGION_BLOCKS_X; ix++)
			for (int iy = 0; iy < GeoStructure.REGION_BLOCKS_Y; iy++)
				_blocks[blockX + ix][blockY + iy] = _nullBlock;
	}
	
	/**
	 * Converts world X to geodata X.
	 * @param worldX
	 * @return int : Geo X
	 */
	public static final int getGeoX(int worldX)
	{
		return (worldX - World.WORLD_X_MIN) >> 4;
	}
	
	/**
	 * Converts world Y to geodata Y.
	 * @param worldY
	 * @return int : Geo Y
	 */
	public static final int getGeoY(int worldY)
	{
		return (worldY - World.WORLD_Y_MIN) >> 4;
	}
	
	/**
	 * Converts geodata X to world X.
	 * @param geoX
	 * @return int : World X
	 */
	public static final int getWorldX(int geoX)
	{
		return (geoX << 4) + World.WORLD_X_MIN + 8;
	}
	
	/**
	 * Converts geodata Y to world Y.
	 * @param geoY
	 * @return int : World Y
	 */
	public static final int getWorldY(int geoY)
	{
		return (geoY << 4) + World.WORLD_Y_MIN + 8;
	}
	
	/**
	 * Returns block of geodata on given coordinates.
	 * @param geoX : Geodata X
	 * @param geoY : Geodata Y
	 * @return {@link ABlock} : Bloack of geodata.
	 */
	public final ABlock getBlock(int geoX, int geoY)
	{
		return _blocks[geoX / GeoStructure.BLOCK_CELLS_X][geoY / GeoStructure.BLOCK_CELLS_Y];
	}
	
	/**
	 * Check if geo coordinates has geo.
	 * @param geoX : Geodata X
	 * @param geoY : Geodata Y
	 * @return boolean : True, if given geo coordinates have geodata
	 */
	public final boolean hasGeoPos(int geoX, int geoY)
	{
		return getBlock(geoX, geoY).hasGeoPos();
	}
	
	/**
	 * Returns the height of cell, which is closest to given coordinates.
	 * @param geoX : Cell geodata X coordinate.
	 * @param geoY : Cell geodata Y coordinate.
	 * @param worldZ : Cell world Z coordinate.
	 * @return short : Cell geodata Z coordinate, closest to given coordinates.
	 */
	public final short getHeightNearest(int geoX, int geoY, int worldZ)
	{
		return getBlock(geoX, geoY).getHeightNearest(geoX, geoY, worldZ, null);
	}
	
	/**
	 * Returns the height of cell, which is closest to given coordinates.<br>
	 * Note: Ignores geodata modification of given {@link IGeoObject}.
	 * @param geoX : Cell geodata X coordinate.
	 * @param geoY : Cell geodata Y coordinate.
	 * @param worldZ : Cell world Z coordinate.
	 * @param ignore : The {@link IGeoObject}, which geodata modification is ignored and original geodata picked instead.
	 * @return short : Cell geodata Z coordinate, closest to given coordinates.
	 */
	public final short getHeightNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return getBlock(geoX, geoY).getHeightNearest(geoX, geoY, worldZ, ignore);
	}
	
	/**
	 * Returns the NSWE flag byte of cell, which is closes to given coordinates.
	 * @param geoX : Cell geodata X coordinate.
	 * @param geoY : Cell geodata Y coordinate.
	 * @param worldZ : Cell world Z coordinate.
	 * @return short : Cell NSWE flag byte coordinate, closest to given coordinates.
	 */
	public final byte getNsweNearest(int geoX, int geoY, int worldZ)
	{
		return getBlock(geoX, geoY).getNsweNearest(geoX, geoY, worldZ, null);
	}
	
	/**
	 * Returns the NSWE flag byte of cell, which is closes to given coordinates.<br>
	 * Note: Ignores geodata modification of given {@link IGeoObject}.
	 * @param geoX : Cell geodata X coordinate.
	 * @param geoY : Cell geodata Y coordinate.
	 * @param worldZ : Cell world Z coordinate.
	 * @param ignore : The {@link IGeoObject}, which geodata modification is ignored and original geodata picked instead.
	 * @return short : Cell NSWE flag byte coordinate, closest to given coordinates.
	 */
	public final byte getNsweNearest(int geoX, int geoY, int worldZ, IGeoObject ignore)
	{
		return getBlock(geoX, geoY).getNsweNearest(geoX, geoY, worldZ, ignore);
	}
	
	/**
	 * Check if world coordinates has geo.
	 * @param worldX : World X
	 * @param worldY : World Y
	 * @return boolean : True, if given world coordinates have geodata
	 */
	public final boolean hasGeo(int worldX, int worldY)
	{
		return hasGeoPos(getGeoX(worldX), getGeoY(worldY));
	}
	
	/**
	 * Returns closest Z coordinate according to geodata.
	 * @param loc : The location used as reference.
	 * @return short : nearest Z coordinates according to geodata
	 */
	public final short getHeight(Location loc)
	{
		return getHeightNearest(getGeoX(loc.getX()), getGeoY(loc.getY()), loc.getZ());
	}
	
	/**
	 * Returns closest Z coordinate according to geodata.
	 * @param worldX : world x
	 * @param worldY : world y
	 * @param worldZ : world z
	 * @return short : nearest Z coordinates according to geodata
	 */
	public final short getHeight(int worldX, int worldY, int worldZ)
	{
		return getHeightNearest(getGeoX(worldX), getGeoY(worldY), worldZ);
	}
	
	/**
	 * Returns calculated NSWE flag byte as a description of {@link IGeoObject}.<br>
	 * The {@link IGeoObject} is defined by boolean 2D array, saying if the object is present on given cell or not.
	 * @param inside : 2D description of {@link IGeoObject}
	 * @return byte[][] : Returns NSWE flags of {@link IGeoObject}.
	 */
	public static final byte[][] calculateGeoObject(boolean[][] inside)
	{
		// get dimensions
		final int width = inside.length;
		final int height = inside[0].length;
		
		// create object flags for geodata, according to the geo object 2D description
		final byte[][] result = new byte[width][height];
		
		// loop over each cell of the geo object
		for (int ix = 0; ix < width; ix++)
			for (int iy = 0; iy < height; iy++)
				if (inside[ix][iy])
				{
					// cell is inside geo object, block whole movement
					result[ix][iy] = GeoStructure.CELL_FLAG_NONE;
				}
				else
				{
					// cell is outside of geo object, block only movement leading inside geo object
					
					// set initial value -> no geodata change
					byte nswe = GeoStructure.CELL_FLAG_ALL;
					
					// perform nswe checks
					if (iy < height - 1 && inside[ix][iy + 1])
						nswe &= ~GeoStructure.CELL_FLAG_S;
					
					if (iy > 0 && inside[ix][iy - 1])
						nswe &= ~GeoStructure.CELL_FLAG_N;
					
					if (ix < width - 1 && inside[ix + 1][iy])
						nswe &= ~GeoStructure.CELL_FLAG_E;
					
					if (ix > 0 && inside[ix - 1][iy])
						nswe &= ~GeoStructure.CELL_FLAG_W;
					
					result[ix][iy] = nswe;
				}
			
		return result;
	}
	
	/**
	 * Add {@link IGeoObject} to the geodata.
	 * @param object : An object using {@link IGeoObject} interface.
	 */
	public final void addGeoObject(IGeoObject object)
	{
		toggleGeoObject(object, true);
	}
	
	/**
	 * Remove {@link IGeoObject} from the geodata.
	 * @param object : An object using {@link IGeoObject} interface.
	 */
	public final void removeGeoObject(IGeoObject object)
	{
		toggleGeoObject(object, false);
	}
	
	/**
	 * Toggles an {@link IGeoObject} in the geodata.
	 * @param object : An object using {@link IGeoObject} interface.
	 * @param add : Add/remove object.
	 */
	private final void toggleGeoObject(IGeoObject object, boolean add)
	{
		// get object geo coordinates and data
		final int minGX = object.getGeoX();
		final int minGY = object.getGeoY();
		final byte[][] geoData = object.getObjectGeoData();
		
		// get min/max block coordinates
		int minBX = minGX / GeoStructure.BLOCK_CELLS_X;
		int maxBX = (minGX + geoData.length - 1) / GeoStructure.BLOCK_CELLS_X;
		int minBY = minGY / GeoStructure.BLOCK_CELLS_Y;
		int maxBY = (minGY + geoData[0].length - 1) / GeoStructure.BLOCK_CELLS_Y;
		
		// loop over affected blocks in X direction
		for (int bx = minBX; bx <= maxBX; bx++)
		{
			// loop over affected blocks in Y direction
			for (int by = minBY; by <= maxBY; by++)
			{
				ABlock block;
				
				// conversion to dynamic block must be synchronized to prevent 2 independent threads converting same block
				synchronized (_blocks)
				{
					// get related block
					block = _blocks[bx][by];
					
					// check for dynamic block
					if (!(block instanceof IBlockDynamic))
					{
						// null block means no geodata (particular region file is not loaded), no geodata means no geobjects
						if (block instanceof BlockNull)
							continue;
						
						// not a dynamic block, convert it
						if (block instanceof BlockFlat bf)
						{
							// convert flat block to the dynamic complex block
							block = new BlockComplexDynamic(bx, by, bf);
							_blocks[bx][by] = block;
						}
						else if (block instanceof BlockComplex bc)
						{
							// convert complex block to the dynamic complex block
							block = new BlockComplexDynamic(bx, by, bc);
							_blocks[bx][by] = block;
						}
						else if (block instanceof BlockMultilayer bm)
						{
							// convert multilayer block to the dynamic multilayer block
							block = new BlockMultilayerDynamic(bx, by, bm);
							_blocks[bx][by] = block;
						}
					}
				}
				
				// add/remove geo object to/from dynamic block
				if (add)
					((IBlockDynamic) block).addGeoObject(object);
				else
					((IBlockDynamic) block).removeGeoObject(object);
			}
		}
	}
	
	/**
	 * Check line of sight from {@link WorldObject} to {@link WorldObject}.<br>
	 * Note: If target is {@link IGeoObject} (e.g. {@link Door}), it ignores its geodata modification.
	 * @param object : The origin object.
	 * @param target : The target object.
	 * @return True, when object can see target.
	 */
	public final boolean canSeeTarget(WorldObject object, WorldObject target)
	{
		// Get object and target coordinates.
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int tx = target.getX();
		int ty = target.getY();
		int tz = target.getZ();
		
		// Get object's and target's line of sight height (if relevant).
		// Note: real creature height = collision height * 2
		double oheight = 0;
		if (object instanceof Creature creature)
			oheight += creature.getCollisionHeight() * 2 * Config.PART_OF_CHARACTER_HEIGHT / 100;
		
		double theight = 0;
		if (target instanceof Creature creature)
			theight += creature.getCollisionHeight() * 2 * Config.PART_OF_CHARACTER_HEIGHT / 100;
		
		// Check if target is geo object. If so, it must be ignored for line of sight check.
		final IGeoObject ignore = (target instanceof IGeoObject igo) ? igo : null;
		
		// Perform geodata check.
		return canSee(ox, oy, oz, oheight, tx, ty, tz, theight, ignore, null) && canSee(tx, ty, tz, theight, ox, oy, oz, oheight, ignore, null);
	}
	
	/**
	 * Check line of sight from {@link WorldObject} to {@link Location}.<br>
	 * Note: The check uses {@link Location}'s real Z coordinate (e.g. point above ground), not its geodata representation.
	 * @param object : The origin object.
	 * @param position : The target position.
	 * @return True, when object can see position.
	 */
	public final boolean canSeeLocation(WorldObject object, Location position)
	{
		// Get object and location coordinates.
		int ox = object.getX();
		int oy = object.getY();
		int oz = object.getZ();
		int tx = position.getX();
		int ty = position.getY();
		int tz = position.getZ();
		
		// Get object's line of sight height (if relevant).
		// Note: real creature height = collision height * 2
		double oheight = 0;
		if (object instanceof Creature creature)
			oheight += creature.getCollisionHeight() * 2 * Config.PART_OF_CHARACTER_HEIGHT / 100;
		
		// Perform geodata check.
		return canSee(ox, oy, oz, oheight, tx, ty, tz, 0, null, null) && canSee(tx, ty, tz, 0, ox, oy, oz, oheight, null, null);
	}
	
	/**
	 * Simple check for origin to target visibility.<br>
	 * Note: Ignores geodata modification of given {@link IGeoObject}.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param oheight : The height of origin, used as start point.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @param theight : The height of target, used as end point.
	 * @param debug : The debug packet to add debug informations in.
	 * @param ignore : The {@link IGeoObject}, which geodata modification is ignored and original geodata picked instead.
	 * @return True, when origin can see target.
	 */
	public final boolean canSee(int ox, int oy, int oz, double oheight, int tx, int ty, int tz, double theight, IGeoObject ignore, ExServerPrimitive debug)
	{
		// Check origin coordinates.
		if (World.isOutOfWorld(ox, oy))
			return false;
		
		// Check target coordinates.
		if (World.isOutOfWorld(tx, ty))
			return false;
		
		// Get geodata coordinates.
		int gox = getGeoX(ox);
		int goy = getGeoY(oy);
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		ABlock block = getBlock(gox, goy);
		
		// Check being on same cell and layer (index).
		// Note: Get index must use origin height increased by cell height, the method returns index to height exclusive self.
		int index = block.getIndexBelow(gox, goy, oz + GeoStructure.CELL_HEIGHT, ignore);
		if (index < 0)
			return false;
		
		if (gox == gtx && goy == gty)
			return index == block.getIndexBelow(gtx, gty, tz + GeoStructure.CELL_HEIGHT, ignore);
		
		// Get ground and nswe flag.
		int groundZ = block.getHeight(index, ignore);
		int nswe = block.getNswe(index, ignore);
		
		// Get delta coordinates, slope of line (XY, XZ) and direction data.
		final int dx = tx - ox;
		final int dy = ty - oy;
		final double dz = (tz + theight) - (oz + oheight);
		final double m = (double) dy / dx;
		final double mz = dz / Math.sqrt(dx * dx + dy * dy);
		final MoveDirectionType mdt = MoveDirectionType.getDirection(gtx - gox, gty - goy);
		
		// Get cell grid coordinates.
		int gridX = ox & 0xFFFFFFF0;
		int gridY = oy & 0xFFFFFFF0;
		
		// Add points to debug packet, if present.
		if (debug != null)
		{
			debug.addSquare(Color.BLUE, gridX, gridY, groundZ + 1, 15);
			debug.addSquare(Color.BLUE, tx & 0xFFFFFFF0, ty & 0xFFFFFFF0, tz, 15);
		}
		
		// Run loop.
		byte dir;
		while (gox != gtx || goy != gty)
		{
			// Calculate intersection with cell's X border.
			int checkX = gridX + mdt.getOffsetX();
			int checkY = (int) (oy + m * (checkX - ox));
			
			if (mdt.getStepX() != 0 && getGeoY(checkY) == goy)
			{
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolX(), Color.CYAN, true, checkX, checkY, groundZ);
					debug.addSquare(Color.GREEN, gridX, gridY, groundZ, 15);
				}
				
				// Set next cell in X direction.
				gridX += mdt.getStepX();
				gox += mdt.getSignumX();
				dir = mdt.getDirectionX();
			}
			else
			{
				// Calculate intersection with cell's Y border.
				checkY = gridY + mdt.getOffsetY();
				checkX = (int) (ox + (checkY - oy) / m);
				checkX = Math.clamp(checkX, gridX, gridX + 15);
				
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolY(), Color.YELLOW, true, checkX, checkY, groundZ);
					debug.addSquare(Color.GREEN, gridX, gridY, groundZ, 15);
				}
				
				// Set next cell in Y direction.
				gridY += mdt.getStepY();
				goy += mdt.getSignumY();
				dir = mdt.getDirectionY();
			}
			
			// Get block of the next cell.
			block = getBlock(gox, goy);
			
			// Get line of sight height (including Z slope).
			double losz = oz + oheight + Config.MAX_OBSTACLE_HEIGHT;
			losz += mz * Math.sqrt((checkX - ox) * (checkX - ox) + (checkY - oy) * (checkY - oy));
			
			// Check line of sight going though wall (vertical check).
			
			// Get index of particular layer, based on last iterated cell conditions.
			boolean canMove = (nswe & dir) != 0;
			if (canMove)
				// No wall present, get next cell below current cell.
				index = block.getIndexBelow(gox, goy, groundZ + GeoStructure.CELL_IGNORE_HEIGHT, ignore);
			else
				// Wall present, get next cell above current cell.
				index = block.getIndexAbove(gox, goy, groundZ - 2 * GeoStructure.CELL_HEIGHT, ignore);
			
			// Next cell's does not exist (no geodata with valid condition), return fail.
			if (index < 0)
			{
				// Show last iterated cell.
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, groundZ, 15);
				
				return false;
			}
			
			// Get next cell's layer height.
			int z = block.getHeight(index, ignore);
			
			// Perform sine of sight check (next cell is above line of sight line), return fail.
			if (z > losz)
			{
				// Show last iterated cell.
				if (debug != null)
				{
					debug.addPoint(Color.RED, checkX, checkY, (int) losz);
					debug.addSquare(Color.RED, gridX, gridY, z, 15);
				}
				
				return false;
			}
			
			// Next cell is accessible, update z and NSWE.
			groundZ = z;
			nswe = block.getNswe(index, ignore);
		}
		
		// Iteration is completed, no obstacle is found.
		return true;
	}
	
	/**
	 * Check movement of {@link WorldObject} to {@link WorldObject} including pathfinding.
	 * @param object : The origin object.
	 * @param target : The target object.
	 * @return True, there is a found path to target.
	 */
	public final boolean canFindPathToTarget(WorldObject object, WorldObject target)
	{
		if (canMoveToTarget(object.getPosition(), target.getPosition()))
			return true;
		
		return findPath(object.getX(), object.getY(), object.getZ(), target.getX(), target.getY(), target.getZ(), object instanceof Playable, null).size() >= 2;
	}
	
	/**
	 * Check movement of {@link WorldObject} to {@link WorldObject}.
	 * @param object : The origin object.
	 * @param target : The target object.
	 * @return True, when the path is clear.
	 */
	public final boolean canMoveToTarget(WorldObject object, WorldObject target)
	{
		return canMoveToTarget(object.getPosition(), target.getPosition());
	}
	
	/**
	 * Check movement of {@link WorldObject} to {@link Location}.
	 * @param object : The origin object.
	 * @param position : The target position.
	 * @return True, when the path is clear.
	 */
	public final boolean canMoveToTarget(WorldObject object, Location position)
	{
		return canMoveToTarget(object.getPosition(), position);
	}
	
	/**
	 * Check movement of {@link Location} to {@link Location}.
	 * @param origin : The origin position.
	 * @param target : The target position.
	 * @return True, when the path is clear.
	 */
	public final boolean canMoveToTarget(Location origin, Location target)
	{
		return canMove(origin.getX(), origin.getY(), origin.getZ(), target.getX(), target.getY(), target.getZ(), null);
	}
	
	/**
	 * Check movement from coordinates to coordinates.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @return True, when target coordinates are reachable from origin coordinates.
	 */
	public final boolean canMoveToTarget(int ox, int oy, int oz, int tx, int ty, int tz)
	{
		return canMove(ox, oy, oz, tx, ty, tz, null);
	}
	
	/**
	 * Check movement from coordinates to coordinates.<br>
	 * Note: The Z coordinates are supposed to be already validated geodata coordinates.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @param debug : The debug packet to add debug informations in.
	 * @return True, when target coordinates are reachable from origin coordinates.
	 */
	public final boolean canMove(int ox, int oy, int oz, int tx, int ty, int tz, ExServerPrimitive debug)
	{
		// Check target coordinates.
		if (World.isOutOfWorld(tx, ty))
			return false;
		
		// Get geodata coordinates.
		int gox = getGeoX(ox);
		int goy = getGeoY(oy);
		int goz = getHeightNearest(gox, goy, oz);
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		
		// Check movement within same cell.
		if (gox == gtx && goy == gty)
			return goz == getHeight(tx, ty, tz);
		
		// Get nswe flag.
		int nswe = getNsweNearest(gox, goy, goz, null);
		
		// Get delta coordinates, slope of line and direction data.
		final int dx = tx - ox;
		final int dy = ty - oy;
		final double m = (double) dy / dx;
		final MoveDirectionType mdt = MoveDirectionType.getDirection(gtx - gox, gty - goy);
		
		// Get cell grid coordinates.
		int gridX = ox & 0xFFFFFFF0;
		int gridY = oy & 0xFFFFFFF0;
		
		// Add points to debug packet, if present.
		if (debug != null)
		{
			debug.addSquare(Color.BLUE, gridX, gridY, goz + 1, 15);
			debug.addSquare(Color.BLUE, tx & 0xFFFFFFF0, ty & 0xFFFFFFF0, tz, 15);
		}
		
		// Run loop.
		byte dir;
		int nx = gox;
		int ny = goy;
		while (gox != gtx || goy != gty)
		{
			// Calculate intersection with cell's X border.
			int checkX = gridX + mdt.getOffsetX();
			int checkY = (int) (oy + m * (checkX - ox));
			
			if (mdt.getStepX() != 0 && getGeoY(checkY) == goy)
			{
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolX(), Color.CYAN, true, checkX, checkY, goz);
					debug.addSquare(Color.GREEN, gridX, gridY, goz, 15);
				}
				
				// Set next cell is in X direction.
				gridX += mdt.getStepX();
				nx += mdt.getSignumX();
				dir = mdt.getDirectionX();
			}
			else
			{
				// Calculate intersection with cell's Y border.
				checkY = gridY + mdt.getOffsetY();
				checkX = (int) (ox + (checkY - oy) / m);
				checkX = Math.clamp(checkX, gridX, gridX + 15);
				
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolY(), Color.YELLOW, true, checkX, checkY, goz);
					debug.addSquare(Color.GREEN, gridX, gridY, goz, 15);
				}
				
				// Set next cell in Y direction.
				gridY += mdt.getStepY();
				ny += mdt.getSignumY();
				dir = mdt.getDirectionY();
			}
			
			// Check point heading into obstacle, if so return current point.
			if ((nswe & dir) == 0)
			{
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, goz, 15);
				return false;
			}
			
			// Check next point for extensive Z difference, if so return current point.
			final ABlock block = getBlock(nx, ny);
			final int i = block.getIndexBelow(nx, ny, goz + GeoStructure.CELL_IGNORE_HEIGHT, null);
			if (i < 0)
			{
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, goz, 15);
				return false;
			}
			
			// Update current point's coordinates and nswe.
			gox = nx;
			goy = ny;
			goz = block.getHeight(i, null);
			nswe = block.getNswe(i, null);
		}
		
		// When origin Z is target Z, the move is successful.
		return goz == getHeight(tx, ty, tz);
	}
	
	/**
	 * Check movement of object to target coordinates. Returns last accessible point in the checked path.<br>
	 * Target X and Y reachable and Z is on same floor:
	 * <ul>
	 * <li>Location of the target with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y reachable but Z is on another floor:
	 * <ul>
	 * <li>Location of the origin with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y not reachable:
	 * <ul>
	 * <li>Last accessible location in destination to target.</li>
	 * </ul>
	 * @param object : Origin object.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @return The {@link Location} representing last point of movement (e.g. just before wall).
	 */
	public final Location getValidLocation(WorldObject object, int tx, int ty, int tz)
	{
		return getValidLocation(object.getX(), object.getY(), object.getZ(), tx, ty, tz, null);
	}
	
	/**
	 * Check movement of object to target. Returns last accessible point in the checked path.<br>
	 * Target X and Y reachable and Z is on same floor:
	 * <ul>
	 * <li>Location of the target with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y reachable but Z is on another floor:
	 * <ul>
	 * <li>Location of the origin with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y not reachable:
	 * <ul>
	 * <li>Last accessible location in destination to target.</li>
	 * </ul>
	 * @param follower : Origin object.
	 * @param pawn : Target object.
	 * @return The {@link Location} representing last point of movement (e.g. just before wall).
	 */
	public final Location getValidLocation(WorldObject follower, WorldObject pawn)
	{
		return getValidLocation(follower.getPosition(), pawn.getPosition());
	}
	
	/**
	 * Check movement of object to target position. Returns last accessible point in the checked path.<br>
	 * Target X and Y reachable and Z is on same floor:
	 * <ul>
	 * <li>Location of the target with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y reachable but Z is on another floor:
	 * <ul>
	 * <li>Location of the origin with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y not reachable:
	 * <ul>
	 * <li>Last accessible location in destination to target.</li>
	 * </ul>
	 * @param object : Origin object.
	 * @param position : Target position.
	 * @return The {@link Location} representing last point of movement (e.g. just before wall).
	 */
	public final Location getValidLocation(WorldObject object, Location position)
	{
		return getValidLocation(object.getPosition(), position);
	}
	
	/**
	 * Check movement from origin to target positions. Returns last accessible point in the checked path.<br>
	 * Target X and Y reachable and Z is on same floor:
	 * <ul>
	 * <li>Location of the target with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y reachable but Z is on another floor:
	 * <ul>
	 * <li>Location of the origin with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y not reachable:
	 * <ul>
	 * <li>Last accessible location in destination to target.</li>
	 * </ul>
	 * @param origin : Origin position.
	 * @param target : Target position.
	 * @return The {@link Location} representing last point of movement (e.g. just before wall).
	 */
	public final Location getValidLocation(Location origin, Location target)
	{
		return getValidLocation(origin.getX(), origin.getY(), origin.getZ(), target.getX(), target.getY(), target.getZ(), null);
	}
	
	/**
	 * Check movement from origin to target coordinates. Returns last available point in the checked path.<br>
	 * Target X and Y reachable and Z is on same floor:
	 * <ul>
	 * <li>Location of the target with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y reachable but Z is on another floor:
	 * <ul>
	 * <li>Location of the origin with corrected Z value from geodata.</li>
	 * </ul>
	 * Target X and Y not reachable:
	 * <ul>
	 * <li>Last accessible location in destination to target.</li>
	 * </ul>
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @param debug : The debug packet to add debug informations in.
	 * @return The {@link Location} representing last point of movement (e.g. just before wall).
	 */
	public final Location getValidLocation(int ox, int oy, int oz, int tx, int ty, int tz, ExServerPrimitive debug)
	{
		// Get geodata coordinates.
		int gox = getGeoX(ox);
		int goy = getGeoY(oy);
		int goz = getHeightNearest(gox, goy, oz);
		int nswe = getNsweNearest(gox, goy, goz, null);
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		final int gtz = getHeightNearest(gtx, gty, tz);
		
		// Get delta coordinates, slope of line and direction data.
		final int dx = tx - ox;
		final int dy = ty - oy;
		final double m = (double) dy / dx;
		final MoveDirectionType mdt = MoveDirectionType.getDirection(gtx - gox, gty - goy);
		
		// Get cell grid coordinates.
		int gridX = ox & 0xFFFFFFF0;
		int gridY = oy & 0xFFFFFFF0;
		
		// Add points to debug packet, if present.
		if (debug != null)
		{
			debug.addSquare(Color.BLUE, gridX, gridY, goz + 1, 15);
			debug.addSquare(Color.BLUE, tx & 0xFFFFFFF0, ty & 0xFFFFFFF0, gtz + 1, 15);
		}
		
		// Run loop.
		byte dir;
		int nx = gox;
		int ny = goy;
		while (gox != gtx || goy != gty)
		{
			// Calculate intersection with cell's X border.
			int checkX = gridX + mdt.getOffsetX();
			int checkY = (int) (oy + m * (checkX - ox));
			
			if (mdt.getStepX() != 0 && getGeoY(checkY) == goy)
			{
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolX(), Color.CYAN, true, checkX, checkY, goz);
					debug.addSquare(Color.GREEN, gridX, gridY, goz, 15);
				}
				
				// Set next cell is in X direction.
				gridX += mdt.getStepX();
				nx += mdt.getSignumX();
				dir = mdt.getDirectionX();
			}
			else
			{
				// Calculate intersection with cell's Y border.
				checkY = gridY + mdt.getOffsetY();
				checkX = (int) (ox + (checkY - oy) / m);
				checkX = Math.clamp(checkX, gridX, gridX + 15);
				
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolY(), Color.YELLOW, true, checkX, checkY, goz);
					debug.addSquare(Color.GREEN, gridX, gridY, goz, 15);
				}
				
				// Set next cell in Y direction.
				gridY += mdt.getStepY();
				ny += mdt.getSignumY();
				dir = mdt.getDirectionY();
			}
			
			// Check target cell is out of geodata grid (world coordinates).
			if (nx < 0 || nx >= GeoStructure.GEO_CELLS_X || ny < 0 || ny >= GeoStructure.GEO_CELLS_Y)
			{
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, goz, 15);
				
				return new Location(checkX, checkY, goz);
			}
			
			// Check point heading into obstacle, if so return current (border) point.
			if ((nswe & dir) == 0)
			{
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, goz, 15);
				
				return new Location(checkX, checkY, goz);
			}
			
			// Check next point for extensive Z difference, if so return current (border) point.
			final ABlock block = getBlock(nx, ny);
			final int i = block.getIndexBelow(nx, ny, goz + GeoStructure.CELL_IGNORE_HEIGHT, null);
			if (i < 0)
			{
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, goz, 15);
				
				return new Location(checkX, checkY, goz);
			}
			
			// Update current point's coordinates and nswe.
			gox = nx;
			goy = ny;
			goz = block.getHeight(i, null);
			nswe = block.getNswe(i, null);
		}
		
		// Compare Z coordinates:
		// If same, path is okay, return target point and fix its Z geodata coordinate.
		// If not same, path is does not exist, return origin point.
		return goz == gtz ? new Location(tx, ty, gtz) : new Location(ox, oy, oz);
	}
	
	/**
	 * Check swimming movement from origin to target coordinates. Returns last available point in the checked path.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @return The {@link Location} representing last point of swimming (e.g. just before wall or on the coast).
	 */
	public final Location getValidSwimLocation(int ox, int oy, int oz, int tx, int ty, int tz)
	{
		return getValidSwimLocation(ox, oy, oz, tx, ty, tz, null);
	}
	
	/**
	 * Check swimming movement from origin to target coordinates. Returns last available point in the checked path.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @param debug : The debug packet to add debug informations in.
	 * @return The {@link Location} representing last point of swimming (e.g. just before wall or on the coast).
	 */
	public final Location getValidSwimLocation(int ox, int oy, int oz, int tx, int ty, int tz, ExServerPrimitive debug)
	{
		// Check target coordinates.
		if (World.isOutOfWorld(tx, ty))
			return new Location(ox, oy, oz);
		
		// Get geodata coordinates.
		int gox = getGeoX(ox);
		int goy = getGeoY(oy);
		ABlock block = getBlock(gox, goy);
		int index = block.getIndexBelow(gox, goy, oz + GeoStructure.CELL_HEIGHT, null);
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		
		// Check movement within same cell and layer.
		if (gox == gtx && goy == gty)
			return index == block.getIndexBelow(gox, goy, tz + GeoStructure.CELL_HEIGHT, null) ? new Location(tx, ty, tz) : new Location(ox, oy, oz);
		
		// Get ground and nswe flags.
		int groundZ = block.getHeight(index, null);
		byte nswe = block.getNswe(index, null);
		
		// Get delta coordinates, slope of line (XY, XZ) and direction data.
		final int dx = tx - ox;
		final int dy = ty - oy;
		final int dz = tz - oz;
		final double m = (double) dy / dx;
		final double mz = dz / Math.sqrt(dx * dx + dy * dy);
		final MoveDirectionType mdt = MoveDirectionType.getDirection(gtx - gox, gty - goy);
		
		// Get cell grid coordinates.
		int gridX = ox & 0xFFFFFFF0;
		int gridY = oy & 0xFFFFFFF0;
		
		// Add points to debug packet, if present.
		if (debug != null)
		{
			debug.addSquare(Color.BLUE, gridX, gridY, groundZ - 32, 15);
			debug.addSquare(Color.BLUE, tx & 0xFFFFFFF0, ty & 0xFFFFFFF0, tz - 32, 15);
		}
		
		// Run loop.
		byte dir;
		while (gox != gtx || goy != gty)
		{
			// Calculate intersection with cell's X border.
			int checkX = gridX + mdt.getOffsetX();
			int checkY = (int) (oy + m * (checkX - ox));
			
			if (mdt.getStepX() != 0 && getGeoY(checkY) == goy)
			{
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolX(), Color.CYAN, true, checkX, checkY, groundZ);
					debug.addSquare(Color.GREEN, gridX, gridY, groundZ, 15);
				}
				
				// Set next cell in X direction.
				gridX += mdt.getStepX();
				gox += mdt.getSignumX();
				dir = mdt.getDirectionX();
			}
			else
			{
				// Calculate intersection with cell's Y border.
				checkY = gridY + mdt.getOffsetY();
				checkX = (int) (ox + (checkY - oy) / m);
				checkX = Math.clamp(checkX, gridX, gridX + 15);
				
				// Show border points.
				if (debug != null)
				{
					debug.addPoint(mdt.getSymbolY(), Color.YELLOW, true, checkX, checkY, groundZ);
					debug.addSquare(Color.GREEN, gridX, gridY, groundZ, 15);
				}
				
				// Set next cell in Y direction.
				gridY += mdt.getStepY();
				goy += mdt.getSignumY();
				dir = mdt.getDirectionY();
			}
			
			// Get block of the next cell.
			block = getBlock(gox, goy);
			
			// Get swim height (including Z slope).
			double swimZ = oz + mz * Math.sqrt((checkX - ox) * (checkX - ox) + (checkY - oy) * (checkY - oy));
			
			// Get index of particular layer, based on last iterated cell conditions.
			boolean canMove = (nswe & dir) != 0;
			if (canMove)
				// No wall present, get next cell below current cell.
				index = block.getIndexBelow(gox, goy, groundZ + GeoStructure.CELL_IGNORE_HEIGHT, null);
			else
				// Wall present, get next cell above current cell.
				index = block.getIndexAbove(gox, goy, groundZ - 2 * GeoStructure.CELL_HEIGHT, null);
			
			// Next cell's does not exist (no geodata with valid condition), return fail.
			if (index < 0)
			{
				// Show last iterated cell.
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, (int) swimZ, 15);
				
				return new Location(gridX, gridY, (int) swimZ);
			}
			
			// Get next cell's layer height.
			int z = block.getHeight(index, null);
			
			// Check swim lane heading.
			if (canMove)
			{
				// Check swim line heading out to the dry land, update ground z and NSWE.
				if (z >= swimZ)
				{
					groundZ = z;
					nswe = block.getNswe(index, null);
					continue;
				}
			}
			else
			{
				// Check swim line heading into wall (next cell is above swim line), return fail.
				if (z > swimZ)
				{
					// Show last iterated cell.
					if (debug != null)
					{
						debug.addPoint(Color.RED, checkX, checkY, (int) swimZ);
						debug.addSquare(Color.RED, gridX, gridY, z, 15);
					}
					
					return new Location(checkX, checkY, (int) swimZ);
				}
			}
			
			// Swim lane is still inside water. Update index by first layer below swim lane and update height and nswe.
			index = block.getIndexBelow(gox, goy, (int) swimZ, null);
			groundZ = block.getHeight(index, null);
			nswe = block.getNswe(index, null);
		}
		
		// Iteration is completed, no obstacle is found.
		return new Location(tx, ty, tz);
	}
	
	/**
	 * Check flying of {@link WorldObject} to {@link WorldObject}.
	 * @param object : The origin object.
	 * @param oheight : The height of origin, used for corridor evaluation.
	 * @param target : The target object.
	 * @return True, when the path is clear.
	 */
	public final boolean canFlyToTarget(WorldObject object, double oheight, WorldObject target)
	{
		return canFlyToTarget(object.getPosition(), oheight, target.getPosition());
	}
	
	/**
	 * Check flying of {@link WorldObject} to {@link Location}.
	 * @param object : The origin object.
	 * @param oheight : The height of origin, used for corridor evaluation.
	 * @param position : The target position.
	 * @return True, when the path is clear.
	 */
	public final boolean canFlyToTarget(WorldObject object, double oheight, Location position)
	{
		return canFlyToTarget(object.getPosition(), oheight, position);
	}
	
	/**
	 * Check flying of {@link Location} to {@link Location}.
	 * @param origin : The origin position.
	 * @param oheight : The height of origin, used for corridor evaluation.
	 * @param target : The target position.
	 * @return True, when the path is clear.
	 */
	public final boolean canFlyToTarget(Location origin, double oheight, Location target)
	{
		return canFly(origin.getX(), origin.getY(), origin.getZ(), oheight, target.getX(), target.getY(), target.getZ(), null);
	}
	
	/**
	 * Check flying from coordinates to coordinates.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param oheight : The height of origin, used for corridor evaluation.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @return True, when target coordinates are reachable from origin coordinates.
	 */
	public final boolean canFlyToTarget(int ox, int oy, int oz, double oheight, int tx, int ty, int tz)
	{
		return canFly(ox, oy, oz, oheight, tx, ty, tz, null);
	}
	
	/**
	 * Check flying from coordinates to coordinates.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param oheight : The height of origin, used for corridor evaluation.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @param debug : The debug packet to add debug informations in.
	 * @return True, when origin can see target.
	 */
	public final boolean canFly(int ox, int oy, int oz, double oheight, int tx, int ty, int tz, ExServerPrimitive debug)
	{
		// Check target coordinates.
		if (World.isOutOfWorld(tx, ty))
			return false;
		
		// Get geodata coordinates.
		int gox = getGeoX(ox);
		int goy = getGeoY(oy);
		int goz = getHeightNearest(gox, goy, oz);
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		
		// Get delta coordinates, slope of line (XY, XZ) and direction data.
		final int dx = tx - ox;
		final int dy = ty - oy;
		final int dz = tz - oz;
		final double m = (double) dy / dx;
		final double mz = dz / Math.sqrt(dx * dx + dy * dy);
		final MoveDirectionType mdt = MoveDirectionType.getDirection(gtx - gox, gty - goy);
		
		// Get cell grid coordinates.
		int gridX = ox & 0xFFFFFFF0;
		int gridY = oy & 0xFFFFFFF0;
		
		// Add points to debug packet, if present.
		if (debug != null)
		{
			debug.addSquare(Color.BLUE, gridX, gridY, goz - 32, 15);
			debug.addSquare(Color.BLUE, tx & 0xFFFFFFF0, ty & 0xFFFFFFF0, tz - 32, 15);
		}
		
		// Run loop.
		int nextZ;
		int index;
		String debugDir = null;
		Color debugColor = null;
		while (gox != gtx || goy != gty)
		{
			// Calculate intersection with cell's X border.
			int checkX = gridX + mdt.getOffsetX();
			int checkY = (int) (oy + m * (checkX - ox));
			
			if (mdt.getStepX() != 0 && getGeoY(checkY) == goy)
			{
				// Set next cell in X direction.
				gridX += mdt.getStepX();
				gox += mdt.getSignumX();
				
				// Set direction and color for debug, if enabled.
				if (debug != null)
				{
					debugDir = mdt.getSymbolX();
					debugColor = Color.CYAN;
				}
			}
			else
			{
				// Calculate intersection with cell's Y border.
				checkY = gridY + mdt.getOffsetY();
				checkX = (int) (ox + (checkY - oy) / m);
				checkX = Math.clamp(checkX, gridX, gridX + 15);
				
				// Set direction and color for debug, if enabled.
				if (debug != null)
				{
					debugDir = mdt.getSymbolY();
					debugColor = Color.YELLOW;
				}
				
				// Set next cell in Y direction.
				gridY += mdt.getStepY();
				goy += mdt.getSignumY();
			}
			
			// Get block of the next cell.
			ABlock block = getBlock(gox, goy);
			
			// Evaluate bottom border (min Z).
			
			// Get next border Z (bottom).
			nextZ = oz + (int) (mz * Math.sqrt((checkX - ox) * (checkX - ox) + (checkY - oy) * (checkY - oy)));
			
			// Get index of geodata below top border (max Z).
			index = block.getIndexBelow(gox, goy, nextZ + (int) oheight, null);
			
			// If no geodata, fail. There always have to be some geodata below character.
			if (index < 0)
			{
				// Show last iterated cell.
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, nextZ - 32, 15);
				
				return false;
			}
			
			// Get real geodata Z.
			goz = block.getHeight(index, null);
			
			// Check geodata to be above bottom border. If so, obstacle in path.
			if (goz > nextZ)
			{
				// Show last iterated cell.
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, nextZ - 32, 15);
				
				return false;
			}
			
			// Show iterated cell.
			if (debug != null)
			{
				debug.addPoint(debugDir, debugColor, true, checkX, checkY, nextZ - 32);
				debug.addSquare(Color.GREEN, gridX, gridY, nextZ - 32, 15);
			}
			
			// Evaluate top border (max Z).
			
			// Get index of geodata below top border (max Z).
			index = block.getIndexAbove(gox, goy, nextZ, null);
			
			// Get next border Z (top).
			nextZ += (int) oheight;
			
			// If there are geodata, check them protruding top border.
			if (index >= 0)
			{
				// Get real geodata Z.
				goz = block.getHeight(index, null);
				
				// Check geodata to be below top border. If so, obstacle in path.
				if (goz < nextZ)
				{
					// Show last iterated cell.
					if (debug != null)
						debug.addSquare(Color.RED, gridX, gridY, nextZ - 32, 15);
					
					return false;
				}
			}
			
			// Show iterated cell.
			if (debug != null)
				debug.addSquare(Color.GREEN, gridX, gridY, nextZ - 32, 15);
		}
		
		// Iteration is completed, no obstacle is found.
		return true;
	}
	
	/**
	 * Check flying movement from origin to target coordinates. Returns last available point in the checked path.
	 * @param ox : Origin X coordinate.
	 * @param oy : Origin Y coordinate.
	 * @param oz : Origin Z coordinate.
	 * @param oheight : The height of origin, used for corridor evaluation.
	 * @param tx : Target X coordinate.
	 * @param ty : Target Y coordinate.
	 * @param tz : Target Z coordinate.
	 * @param debug : The debug packet to add debug informations in.
	 * @return The {@link Location} representing last point of flying (e.g. just before wall).
	 */
	public final Location getValidFlyLocation(int ox, int oy, int oz, double oheight, int tx, int ty, int tz, ExServerPrimitive debug)
	{
		// Get geodata coordinates.
		int gox = getGeoX(ox);
		int goy = getGeoY(oy);
		int goz = getHeightNearest(gox, goy, oz);
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		
		// Get delta coordinates, slope of line (XY, XZ) and direction data.
		final int dx = tx - ox;
		final int dy = ty - oy;
		final int dz = tz - oz;
		final double m = (double) dy / dx;
		final double mz = dz / Math.sqrt(dx * dx + dy * dy);
		final MoveDirectionType mdt = MoveDirectionType.getDirection(gtx - gox, gty - goy);
		
		// Get cell grid coordinates.
		int gridX = ox & 0xFFFFFFF0;
		int gridY = oy & 0xFFFFFFF0;
		
		// Add points to debug packet, if present.
		if (debug != null)
		{
			debug.addSquare(Color.BLUE, gridX, gridY, goz - 32, 15);
			debug.addSquare(Color.BLUE, tx & 0xFFFFFFF0, ty & 0xFFFFFFF0, tz - 32, 15);
		}
		
		// Run loop.
		int checkZ = oz;
		String debugDir = null;
		Color debugColor = null;
		while (gox != gtx || goy != gty)
		{
			// Calculate intersection with cell's X border.
			int checkX = gridX + mdt.getOffsetX();
			int checkY = (int) (oy + m * (checkX - ox));
			
			if (mdt.getStepX() != 0 && getGeoY(checkY) == goy)
			{
				// Set next cell in X direction.
				gridX += mdt.getStepX();
				gox += mdt.getSignumX();
				
				// Set direction and color for debug, if enabled.
				if (debug != null)
				{
					debugDir = mdt.getSymbolX();
					debugColor = Color.CYAN;
				}
			}
			else
			{
				// Calculate intersection with cell's Y border.
				checkY = gridY + mdt.getOffsetY();
				checkX = (int) (ox + (checkY - oy) / m);
				checkX = Math.clamp(checkX, gridX, gridX + 15);
				
				// Set direction and color for debug, if enabled.
				if (debug != null)
				{
					debugDir = mdt.getSymbolY();
					debugColor = Color.YELLOW;
				}
				
				// Set next cell in Y direction.
				gridY += mdt.getStepY();
				goy += mdt.getSignumY();
			}
			
			// Check target cell is out of geodata grid (world coordinates).
			if (gox < 0 || gox >= GeoStructure.GEO_CELLS_X || goy < 0 || goy >= GeoStructure.GEO_CELLS_Y)
			{
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, goz, 15);
				
				return new Location(checkX, checkY, goz);
			}
			
			// Get block of the next cell.
			ABlock block = getBlock(gox, goy);
			
			// Evaluate bottom border (min Z).
			
			// Get next border Z (bottom).
			int bottomZ = oz + (int) (mz * Math.sqrt((checkX - ox) * (checkX - ox) + (checkY - oy) * (checkY - oy)));
			
			// Get index of geodata below top border (max Z).
			int index = block.getIndexBelow(gox, goy, bottomZ + (int) oheight, null);
			
			// If no geodata, fail. There always have to be some geodata below character.
			if (index < 0)
			{
				// Show last iterated cell.
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, bottomZ - 32, 15);
				
				return new Location(checkX, checkY, checkZ);
			}
			
			// Get real geodata Z.
			goz = block.getHeight(index, null);
			
			// Check geodata to be above bottom border. If so, obstacle in path.
			if (goz > bottomZ)
			{
				// Show last iterated cell.
				if (debug != null)
					debug.addSquare(Color.RED, gridX, gridY, bottomZ - 32, 15);
				
				return new Location(checkX, checkY, checkZ);
			}
			
			// Show iterated cell.
			if (debug != null)
			{
				debug.addPoint(debugDir, debugColor, true, checkX, checkY, bottomZ - 32);
				debug.addSquare(Color.GREEN, gridX, gridY, bottomZ - 32, 15);
			}
			
			// Evaluate top border (max Z).
			
			// Get index of geodata below top border (max Z).
			index = block.getIndexAbove(gox, goy, bottomZ, null);
			
			// Get next border Z (top).
			int topZ = bottomZ + (int) oheight;
			
			// If there are geodata, check them protruding top border.
			if (index >= 0)
			{
				// Get real geodata Z.
				goz = block.getHeight(index, null);
				
				// Check geodata to be below top border. If so, obstacle in path.
				if (goz < topZ)
				{
					// Show last iterated cell.
					if (debug != null)
						debug.addSquare(Color.RED, gridX, gridY, topZ - 32, 15);
					
					return new Location(checkX, checkY, checkZ);
				}
			}
			
			// Show iterated cell.
			if (debug != null)
				debug.addSquare(Color.GREEN, gridX, gridY, topZ - 32, 15);
			
			// Set Z coord of current bottom layer intersection.
			checkZ = bottomZ;
		}
		
		// Iteration is completed, no obstacle is found.
		return new Location(tx, ty, tz);
	}
	
	/**
	 * Returns the possibility of free movement in given coordinates and distance.
	 * @param worldX : world x
	 * @param worldY : world y
	 * @param worldZ : world z
	 * @return boolean : Returns true, if free movement in given coordinates and distance is allowed.
	 */
	public final boolean canMoveAround(int worldX, int worldY, int worldZ)
	{
		// Get center coordinates and cell distance.
		final int geoX = getGeoX(worldX);
		final int geoY = getGeoY(worldY);
		
		// Loop each cell in the square.
		for (int ix = -1; ix <= 1; ix++)
		{
			for (int iy = -1; iy <= 1; iy++)
			{
				// Get real geo coordinates.
				final int gx = geoX + ix;
				final int gy = geoY + iy;
				
				// Check NSWE flag for free movement, return if blocked.
				if (getNsweNearest(gx, gy, worldZ) != GeoStructure.CELL_FLAG_ALL)
					return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the list of location objects as a result of complete path calculation.
	 * @param ox : origin x
	 * @param oy : origin y
	 * @param oz : origin z
	 * @param tx : target x
	 * @param ty : target y
	 * @param tz : target z
	 * @param playable : moving object is playable?
	 * @param debug : The debug packet to add debug informations in.
	 * @return {@code List<Location>} : complete path from nodes
	 */
	public List<Location> findPath(int ox, int oy, int oz, int tx, int ty, int tz, boolean playable, ExServerPrimitive debug)
	{
		// Check target coordinates.
		if (World.isOutOfWorld(tx, ty))
			return Collections.emptyList();
		
		// get origin and check existing geo coords
		final int gox = getGeoX(ox);
		final int goy = getGeoY(oy);
		if (!hasGeoPos(gox, goy))
			return Collections.emptyList();
		
		final int goz = getHeightNearest(gox, goy, oz);
		
		// get target and check existing geo coords
		final int gtx = getGeoX(tx);
		final int gty = getGeoY(ty);
		if (!hasGeoPos(gtx, gty))
			return Collections.emptyList();
		
		final int gtz = getHeightNearest(gtx, gty, tz);
		if (Math.abs(gtz - tz) > 500)
			return Collections.emptyList();
		
		final PathFinder pf = new PathFinder();
		
		// find path
		final List<Location> path = pf.findPath(gox, goy, goz, gtx, gty, gtz, debug);
		if (path.isEmpty())
			return Collections.emptyList();
		
		// check path
		if (path.size() < 3)
			return path;
		
		// get path list iterator
		ListIterator<Location> point = path.listIterator();
		
		// get node A (origin)
		int nodeAx = ox;
		int nodeAy = oy;
		int nodeAz = goz;
		
		// get node B
		Location nodeB = point.next();
		
		// iterate thought the path to optimize it
		while (point.hasNext())
		{
			// get node C
			Location nodeC = path.get(point.nextIndex());
			
			// check movement from node A to node C
			if (canMove(nodeAx, nodeAy, nodeAz, nodeC.getX(), nodeC.getY(), nodeC.getZ(), null))
			{
				// can move from node A to node C
				
				// remove node B
				point.remove();
				
				// show skipped nodes
				if (debug != null)
					debug.addPoint(Color.RED, nodeB.getX(), nodeB.getY(), nodeB.getZ());
			}
			else
			{
				// can not move from node A to node C
				
				// set node A (node B is part of path, update A coordinates)
				nodeAx = nodeB.getX();
				nodeAy = nodeB.getY();
				nodeAz = nodeB.getZ();
				
				// show used nodes
				if (debug != null)
					debug.addPoint(Color.GREEN, nodeB.getX(), nodeB.getY(), nodeB.getZ());
			}
			
			// set node B
			nodeB = point.next();
		}
		
		// show final path
		if (debug != null)
		{
			Location prev = new Location(ox, oy, oz);
			int i = 1;
			for (Location next : path)
			{
				debug.addLine("Segment #" + i, Color.GREEN, true, prev, next);
				prev = next;
				i++;
			}
		}
		
		return path;
	}
	
	/**
	 * Record a geodata bug.
	 * @param loc : Location of the geodata bug.
	 * @param comment : Short commentary.
	 * @return boolean : True, when bug was successfully recorded.
	 */
	public final boolean addGeoBug(Location loc, String comment)
	{
		int gox = getGeoX(loc.getX());
		int goy = getGeoY(loc.getY());
		int goz = loc.getZ();
		int rx = gox / GeoStructure.REGION_CELLS_X + World.TILE_X_MIN;
		int ry = goy / GeoStructure.REGION_CELLS_Y + World.TILE_Y_MIN;
		int bx = (gox / GeoStructure.BLOCK_CELLS_X) % GeoStructure.REGION_BLOCKS_X;
		int by = (goy / GeoStructure.BLOCK_CELLS_Y) % GeoStructure.REGION_BLOCKS_Y;
		int cx = gox % GeoStructure.BLOCK_CELLS_X;
		int cy = goy % GeoStructure.BLOCK_CELLS_Y;
		
		try
		{
			_geoBugReports.printf(GEO_BUG, rx, ry, bx, by, cx, cy, goz, comment.replace(";", ":"));
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't save new entry to \"geo_bugs.txt\" file.", e);
			return false;
		}
	}
	
	/**
	 * Returns the instance of the {@link GeoEngine}.
	 * @return {@link GeoEngine} : The instance.
	 */
	public static final GeoEngine getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final GeoEngine INSTANCE = new GeoEngine();
	}
}