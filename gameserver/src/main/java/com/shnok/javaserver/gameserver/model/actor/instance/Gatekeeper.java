package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;

/**
 * An instance type extending {@link Folk}, used for teleporters.<br>
 * <br>
 * A teleporter allows {@link Player}s to teleport to a specific location, for a fee.
 */
public final class Gatekeeper extends Folk
{
	public Gatekeeper(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/gatekeeper/" + filename + ".htm";
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		// Generic PK check. Send back the HTM if found and cancel current action.
		if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0 && showPkDenyChatWindow(player, "gatekeeper"))
			return;
		
		super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		// Generic PK check. Send back the HTM if found and cancel current action.
		if (!Config.KARMA_PLAYER_CAN_USE_GK && player.getKarma() > 0 && showPkDenyChatWindow(player, "gatekeeper"))
			return;
		
		showChatWindow(player, getHtmlPath(getNpcId(), val));
	}
}