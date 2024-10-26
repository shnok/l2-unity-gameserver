package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.manager.CursedWeaponManager;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.entity.CursedWeapon;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminCursedWeapon implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_cw",
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		if (!st.hasMoreTokens())
		{
			showCursedWeaponSelectPage(player);
			return;
		}
		
		try
		{
			final String type = st.nextToken();
			
			int id = 0;
			
			String parameter = st.nextToken();
			if (StringUtil.isDigit(parameter))
				id = Integer.parseInt(parameter);
			else
			{
				parameter = parameter.replace('_', ' ');
				for (CursedWeapon cwp : CursedWeaponManager.getInstance().getCursedWeapons())
				{
					if (cwp.getName().toLowerCase().contains(parameter.toLowerCase()))
					{
						id = cwp.getItemId();
						break;
					}
				}
			}
			
			final CursedWeapon cw = CursedWeaponManager.getInstance().getCursedWeapon(id);
			if (cw == null)
			{
				player.sendMessage("Unknown cursed weapon ID.");
				return;
			}
			
			switch (type)
			{
				case "set":
					if (cw.isActive())
						player.sendMessage("This cursed weapon is already active.");
					else
					{
						final Player targetPlayer = getTargetPlayer(player, true);
						targetPlayer.addItem(id, 1, true);
						
						// Start task
						cw.reActivate(true);
					}
					break;
				
				case "remove":
					cw.endOfLife();
					break;
				
				case "teleportto":
					cw.teleportTo(player);
					break;
			}
			showCursedWeaponSelectPage(player);
		}
		catch (Exception e)
		{
			player.sendMessage("Usage: //cw [set|remove|teleportto itemid|name]");
		}
	}
	
	/**
	 * Show the complete list of {@link CursedWeapon}s.
	 * @param player : The {@link Player} who requested the action.
	 */
	private static void showCursedWeaponSelectPage(Player player)
	{
		final StringBuilder sb = new StringBuilder(2000);
		for (CursedWeapon cursedWeapon : CursedWeaponManager.getInstance().getCursedWeapons())
		{
			StringUtil.append(sb, "<table width=280><tr><td>Name:</td><td>", cursedWeapon.getName(), "</td></tr>");
			
			if (cursedWeapon.isActive())
			{
				long milliToStart = cursedWeapon.getTimeLeft();
				double numSecs = (milliToStart / 1000) % 60;
				double countDown = ((milliToStart / 1000d) - numSecs) / 60;
				int numMins = (int) Math.floor(countDown % 60);
				countDown = (countDown - numMins) / 60;
				int numHours = (int) Math.floor(countDown % 24);
				int numDays = (int) Math.floor((countDown - numHours) / 24);
				
				if (cursedWeapon.isActivated())
				{
					final Player cursedPlayer = cursedWeapon.getPlayer();
					StringUtil.append(sb, "<tr><td>Owner:</td><td>", ((cursedPlayer == null) ? "null" : cursedPlayer.getName()), "</td></tr><tr><td>Stored values:</td><td>Karma=", cursedWeapon.getPlayerKarma(), " PKs=", cursedWeapon.getPlayerPkKills(), "</td></tr><tr><td>Current stage:</td><td>", cursedWeapon.getCurrentStage(), "</td></tr><tr><td>Overall time:</td><td>", numDays, "d. ", numHours, "h. ", numMins, "m.</td></tr><tr><td>Hungry time:</td><td>", cursedWeapon.getHungryTime(), "m.</td></tr><tr><td>Current kills:</td><td>", cursedWeapon.getNbKills(), " / ", cursedWeapon.getNumberBeforeNextStage(), "</td></tr><tr><td><button value=\"Remove CW\" action=\"bypass -h admin_cw remove ", cursedWeapon.getItemId(), "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></td><td><button value=\"Teleport To\" action=\"bypass -h admin_cw teleportto ", cursedWeapon.getItemId(), "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></td></tr>");
				}
				else if (cursedWeapon.isDropped())
					StringUtil.append(sb, "<tr><td>Position:</td><td>Lying on the ground</td></tr><tr><td>Overall time:</td><td>", numDays, "d. ", numHours, "h. ", numMins, "m.</td></tr><tr><td><button value=\"Remove\" action=\"bypass -h admin_cw remove ", cursedWeapon.getItemId(), "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></td><td><button value=\"Go\" action=\"bypass -h admin_cw teleportto ", cursedWeapon.getItemId(), "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></td></tr>");
			}
			else
				StringUtil.append(sb, "<tr><td>Position:</td><td>Doesn't exist.</td></tr><tr><td><button value=\"Set CW\" action=\"bypass -h admin_cw set ", cursedWeapon.getItemId(), "\" width=75 height=21 back=\"L2UI_ch3.Btn1_normalOn\" fore=\"L2UI_ch3.Btn1_normal\"></td><td></td></tr>");
			
			sb.append("</table>");
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/cwinfo.htm");
		html.replace("%cwinfo%", sb.toString());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}