package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.awt.Color;
import java.util.List;
import java.util.StringTokenizer;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.geoengine.geodata.ABlock;
import net.sf.l2j.gameserver.geoengine.geodata.GeoStructure;
import net.sf.l2j.gameserver.geoengine.geodata.IGeoObject;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.SpawnLocation;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ExServerPrimitive;

public class AdminGeoEngine implements IAdminCommandHandler
{
	private static final String Y = "x ";
	private static final String N = "   ";
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_geo",
		"admin_path"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		if (command.startsWith("admin_geo"))
		{
			try
			{
				switch (st.nextToken())
				{
					case "bug":
						int geoX = GeoEngine.getGeoX(player.getX());
						int geoY = GeoEngine.getGeoY(player.getY());
						
						if (GeoEngine.getInstance().hasGeoPos(geoX, geoY))
						{
							try
							{
								String comment = command.substring(14);
								if (GeoEngine.getInstance().addGeoBug(player.getPosition(), player.getName() + ": " + comment))
									player.sendMessage("GeoData bug saved.");
							}
							catch (Exception e)
							{
								player.sendMessage("Usage: //geo bug comments");
							}
						}
						else
							player.sendMessage("There is no geodata at this position.");
						break;
					
					case "pos":
						int ox = player.getX();
						int oy = player.getY();
						int oz = player.getZ();
						
						geoX = GeoEngine.getGeoX(ox);
						geoY = GeoEngine.getGeoY(oy);
						
						int rx = (ox - World.WORLD_X_MIN) / World.TILE_SIZE + World.TILE_X_MIN;
						int ry = (oy - World.WORLD_Y_MIN) / World.TILE_SIZE + World.TILE_Y_MIN;
						ABlock block = GeoEngine.getInstance().getBlock(geoX, geoY);
						
						player.sendMessage("Region: " + rx + "_" + ry + "; Block: " + block.getClass().getSimpleName());
						
						if (block.hasGeoPos())
						{
							int geoZ = block.getHeightNearest(geoX, geoY, player.getZ(), null);
							byte nswe = block.getNsweNearest(geoX, geoY, geoZ, null);
							
							player.sendMessage("    " + ((nswe & GeoStructure.CELL_FLAG_N) != 0 && (nswe & GeoStructure.CELL_FLAG_W) != 0 ? Y : N) + ((nswe & GeoStructure.CELL_FLAG_N) != 0 ? Y : N) + ((nswe & GeoStructure.CELL_FLAG_N) != 0 && (nswe & GeoStructure.CELL_FLAG_E) != 0 ? Y : N) + "         GeoX=" + geoX);
							player.sendMessage("    " + ((nswe & GeoStructure.CELL_FLAG_W) != 0 ? Y : N) + "o " + ((nswe & GeoStructure.CELL_FLAG_E) != 0 ? Y : N) + "         GeoY=" + geoY);
							player.sendMessage("    " + ((nswe & GeoStructure.CELL_FLAG_S) != 0 && (nswe & GeoStructure.CELL_FLAG_W) != 0 ? Y : N) + ((nswe & GeoStructure.CELL_FLAG_S) != 0 ? Y : N) + ((nswe & GeoStructure.CELL_FLAG_S) != 0 && (nswe & GeoStructure.CELL_FLAG_E) != 0 ? Y : N) + "         GeoZ=" + geoZ);
							
							ExServerPrimitive debug = player.getDebugPacket("POS");
							debug.reset();
							
							debug.addSquare(Color.GREEN, ox & 0xFFFFFFF0, oy & 0xFFFFFFF0, oz, 15);
							debug.addPoint("POS", Color.RED, true, ox, oy, oz);
							
							debug.sendTo(player);
						}
						else
							player.sendMessage("There is no geodata at this position.");
						break;
					
					case "see":
						Creature targetCreature = getTargetCreature(player, true);
						
						ExServerPrimitive debug = player.getDebugPacket("CAN_SEE");
						debug.reset();
						
						ox = player.getX();
						oy = player.getY();
						oz = player.getZ();
						
						int oh = (int) (2 * player.getCollisionHeight());
						debug.addLine("origin", Color.BLUE, true, ox, oy, oz, ox, oy, oz + oh);
						oh = (oh * Config.PART_OF_CHARACTER_HEIGHT) / 100;
						
						int tx = targetCreature.getX();
						int ty = targetCreature.getY();
						int tz = targetCreature.getZ();
						
						int th = (int) (2 * targetCreature.getCollisionHeight());
						debug.addLine("target", Color.BLUE, true, tx, ty, tz, tx, ty, tz + th);
						th = (th * Config.PART_OF_CHARACTER_HEIGHT) / 100;
						
						IGeoObject ignore = (targetCreature instanceof IGeoObject igo) ? igo : null;
						
						boolean canSee = GeoEngine.getInstance().canSee(tx, ty, tz, th, ox, oy, oz, oh, ignore, debug);
						canSee &= GeoEngine.getInstance().canSee(ox, oy, oz, oh, tx, ty, tz, th, ignore, debug);
						
						oh += oz;
						th += tz;
						
						debug.addLine("Line-of-Sight", canSee ? Color.GREEN : Color.RED, true, ox, oy, oh, tx, ty, th);
						debug.addLine("Geodata limit", Color.MAGENTA, true, ox, oy, oh + Config.MAX_OBSTACLE_HEIGHT, tx, ty, th + Config.MAX_OBSTACLE_HEIGHT);
						debug.sendTo(player);
						break;
					
					case "move":
						final WorldObject targetWorldObject = player.getTarget();
						if (targetWorldObject == null)
						{
							player.sendPacket(SystemMessageId.INVALID_TARGET);
							return;
						}
						
						SpawnLocation aLoc = player.getPosition();
						SpawnLocation tLoc = targetWorldObject.getPosition();
						
						debug = player.getDebugPacket("CAN_MOVE");
						debug.reset();
						
						Location loc = GeoEngine.getInstance().getValidLocation(aLoc.getX(), aLoc.getY(), aLoc.getZ(), tLoc.getX(), tLoc.getY(), tLoc.getZ(), debug);
						debug.addLine("Can move", Color.GREEN, true, aLoc, loc);
						if (loc.getX() == tLoc.getX() && loc.getY() == tLoc.getY() && loc.getZ() == tLoc.getZ())
						{
							player.sendMessage("Can move beeline.");
						}
						else
						{
							debug.addLine(Color.WHITE, aLoc, tLoc);
							debug.addLine("Inaccessible", Color.RED, true, loc, tLoc);
							debug.addPoint("Limit", Color.RED, true, loc);
							player.sendMessage("Can not move beeline!");
						}
						break;
					
					case "fly":
						targetCreature = getTargetCreature(player, true);
						
						debug = player.getDebugPacket("CAN_FLY");
						debug.reset();
						
						ox = player.getX();
						oy = player.getY();
						oz = player.getZ();
						oh = (int) (2 * player.getCollisionHeight());
						debug.addLine("origin", Color.BLUE, true, ox, oy, oz - 32, ox, oy, oz + oh - 32);
						
						tx = targetCreature.getX();
						ty = targetCreature.getY();
						tz = targetCreature.getZ();
						
						loc = GeoEngine.getInstance().getValidFlyLocation(ox, oy, oz, oh, tx, ty, tz, debug);
						int x = loc.getX();
						int y = loc.getY();
						int z = loc.getZ();
						
						boolean canFly = x == tx && y == ty && z == tz;
						
						debug.addLine("Can fly", Color.GREEN, true, ox, oy, oz - 32, x, y, z - 32);
						
						if (canFly)
							player.sendMessage("Can fly beeline.");
						else
						{
							player.sendMessage("Can not fly beeline!");
							
							debug.addLine(Color.WHITE, ox, oy, oz - 32, tx, ty, tz - 32);
							debug.addLine("Inaccessible", Color.RED, true, x, y, z - 32, tx, ty, tz - 32);
							debug.addLine("Last position", Color.RED, true, x, y, z - 32, x, y, z + oh - 32);
						}
						
						debug.addLine("Line-of-Flight MIN", canFly ? Color.GREEN : Color.RED, true, ox, oy, oz - 32, tx, ty, tz - 32);
						debug.addLine("Line-of-Fligth MAX", canFly ? Color.GREEN : Color.RED, true, ox, oy, oz + oh - 32, tx, ty, tz + oh - 32);
						debug.sendTo(player);
						break;
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage: //geo bug|pos|see|move|fly");
			}
		}
		else if (command.startsWith("admin_path"))
		{
			try
			{
				switch (st.nextToken())
				{
					case "find":
						final WorldObject targetWorldObject = player.getTarget();
						if (targetWorldObject == null)
						{
							player.sendPacket(SystemMessageId.INVALID_TARGET);
							return;
						}
						
						final ExServerPrimitive debug = player.getDebugPacket("PATH");
						debug.reset();
						
						final List<Location> path = GeoEngine.getInstance().findPath(player.getX(), player.getY(), player.getZ(), targetWorldObject.getX(), targetWorldObject.getY(), targetWorldObject.getZ(), true, debug);
						if (path.isEmpty())
						{
							player.sendMessage("No route found or pathfinding is disabled.");
							return;
						}
						
						for (Location loc : path)
							player.sendMessage("x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
						
						debug.sendTo(player);
						break;
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage: //path find|info");
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}