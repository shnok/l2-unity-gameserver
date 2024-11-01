package com.shnok.javaserver.gameserver.communitybbs.manager;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.data.cache.HtmCache;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ShowBoard;

public abstract class BaseBBSManager
{
	protected static final CLogger LOGGER = new CLogger(BaseBBSManager.class.getName());
	
	protected static final String CB_PATH = "data/html/CommunityBoard/";
	
	public void parseCmd(String command, Player player)
	{
		separateAndSend("<html><body><br><br><center>The command: " + command + " isn't implemented.</center></body></html>", player);
	}
	
	public void parseWrite(String ar1, String ar2, String ar3, String ar4, String ar5, Player player)
	{
		separateAndSend("<html><body><br><br><center>The command: " + ar1 + " isn't implemented.</center></body></html>", player);
	}
	
	public static void separateAndSend(String html, Player player)
	{
		if (html == null || player == null)
			return;
		
		if (html.length() < 4090)
		{
			player.sendPacket(new ShowBoard(html, "101"));
			player.sendPacket(ShowBoard.STATIC_SHOWBOARD_102);
			player.sendPacket(ShowBoard.STATIC_SHOWBOARD_103);
		}
		else if (html.length() < 8180)
		{
			player.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			player.sendPacket(new ShowBoard(html.substring(4090, html.length()), "102"));
			player.sendPacket(ShowBoard.STATIC_SHOWBOARD_103);
		}
		else if (html.length() < 12270)
		{
			player.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			player.sendPacket(new ShowBoard(html.substring(4090, 8180), "102"));
			player.sendPacket(new ShowBoard(html.substring(8180, html.length()), "103"));
		}
	}
	
	protected static void send1001(String html, Player player)
	{
		if (html.length() < 8180)
			player.sendPacket(new ShowBoard(html, "1001"));
	}
	
	protected static void send1002(Player player)
	{
		send1002(player, " ", " ", "0");
	}
	
	protected static void send1002(Player player, String string, String string2, String string3)
	{
		final List<String> params = new ArrayList<>();
		params.add("0");
		params.add("0");
		params.add("0");
		params.add("0");
		params.add("0");
		params.add("0");
		params.add(player.getName());
		params.add(Integer.toString(player.getObjectId()));
		params.add(player.getAccountName());
		params.add("9");
		params.add(string2);
		params.add(string2);
		params.add(string);
		params.add(string3);
		params.add(string3);
		params.add("0");
		params.add("0");
		
		player.sendPacket(new ShowBoard(params));
	}
	
	/**
	 * Loads an HTM located in the default CB path.
	 * @param file : the file to load.
	 * @param player : the requester.
	 */
	protected void loadStaticHtm(String file, Player player)
	{
		separateAndSend(HtmCache.getInstance().getHtm(CB_PATH + getFolder() + file), player);
	}
	
	/**
	 * That method is overidden in every board type. It allows to switch of folders following the board.
	 * @return the folder.
	 */
	protected String getFolder()
	{
		return "";
	}
}