package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.data.xml.RestartPointData;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.restart.RestartPoint;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExServerPrimitive;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class AdminZone implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_zone"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		if (!st.hasMoreTokens())
		{
			showHtml(player);
			return;
		}
		
		switch (st.nextToken().toLowerCase())
		{
			case "show":
				try
				{
					final ExServerPrimitive debug = player.getDebugPacket("ZONE");
					debug.reset();
					
					final String param = st.nextToken().toLowerCase();
					switch (param)
					{
						case "all":
							for (ZoneType zone : player.getZones(false))
								zone.visualizeZone(debug);
							
							debug.sendTo(player);
							
							showHtml(player);
							break;
						
						case "clear":
							debug.sendTo(player);
							
							showHtml(player);
							break;
						
						default:
							ZoneManager.getInstance().getZoneById(Integer.parseInt(param)).visualizeZone(debug);
							
							debug.sendTo(player);
							break;
					}
				}
				catch (Exception e)
				{
					player.sendMessage("Invalid parameter for //zone show.");
				}
				break;
			
			default:
				showHtml(player);
				break;
		}
	}
	
	private static void showHtml(Player player)
	{
		int x = player.getX();
		int y = player.getY();
		int rx = (x - World.WORLD_X_MIN) / World.TILE_SIZE + World.TILE_X_MIN;
		int ry = (y - World.WORLD_Y_MIN) / World.TILE_SIZE + World.TILE_Y_MIN;
		
		final RestartPoint currentRp = RestartPointData.getInstance().getRestartPoint(player);
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/zone.htm");
		html.replace("%GEOREGION%", rx + "_" + ry);
		html.replace("%RA%", RestartPointData.getInstance().getRestartArea(player) != null);
		html.replace("%CALCULATED_RP%", RestartPointData.getInstance().getCalculatedRestartPoint(player).getName());
		html.replace("%CURRENT_RP%", (currentRp == null) ? "N/A" : currentRp.getName());
		html.replace("%CURRENTLOC%", x + ", " + y + ", " + player.getZ());
		
		final StringBuilder sb = new StringBuilder(100);
		
		for (ZoneId zoneId : ZoneId.VALUES)
		{
			if (player.isInsideZone(zoneId))
				StringUtil.append(sb, zoneId, "<br1>");
		}
		html.replace("%ZONES%", sb.toString());
		
		// Reset the StringBuilder for another use.
		sb.setLength(0);
		
		for (ZoneType zoneType : World.getInstance().getRegion(x, y).getZones())
		{
			if (zoneType.isInZone(player))
				StringUtil.append(sb, zoneType.getId(), " ");
		}
		html.replace("%ZLIST%", sb.toString());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}