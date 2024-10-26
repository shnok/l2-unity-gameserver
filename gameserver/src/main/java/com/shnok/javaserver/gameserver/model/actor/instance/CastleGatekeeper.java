package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcSay;

/**
 * This class manages all Mass Gatekeepers, an entity linked to Castle system. It inherits from {@link Folk}.<br>
 * <br>
 * Mass Gatekeepers allow Castle Defenders Players to teleport back to battle, after 30 seconds. The time can increase to 480 seconds (8 minutes) during an active siege where all ControlTowers shattered.
 */
public class CastleGatekeeper extends Folk
{
	private Future<?> _teleportTask;
	
	public CastleGatekeeper(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("tele"))
		{
			if (_teleportTask == null)
				_teleportTask = ThreadPool.schedule(this::oustPlayers, getTeleportDelay() * 1000L);
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/castleteleporter/MassGK-1.htm");
			html.replace("%delay%", getTeleportDelay());
			player.sendPacket(html);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		
		if (_teleportTask == null)
		{
			if (getCastle().getSiege().isInProgress() && getCastle().getAliveLifeTowerCount() == 0)
				html.setFile("data/html/castleteleporter/MassGK-2.htm");
			else
				html.setFile("data/html/castleteleporter/MassGK.htm");
		}
		else
		{
			html.setFile("data/html/castleteleporter/MassGK-1.htm");
			html.replace("%delay%", getTeleportDelay());
		}
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	/**
	 * Oust all {@link Player}s and broadcast a message to everyone set into the region, during an active siege event.
	 */
	private final void oustPlayers()
	{
		// Region talk is only done during an active siege.
		if (getCastle().getSiege().isInProgress())
			World.broadcastToSameRegion(this, new NpcSay(this, SayType.SHOUT, "The defenders of " + getCastle().getName() + " castle have been teleported to the inner castle."));
		
		// Oust all related players.
		getCastle().oustAllPlayers();
		
		// Reset the variable, in order to properly reuse it.
		_teleportTask = null;
	}
	
	/**
	 * @return The teleport delay, as following : 30 seconds for regular teleport, 480 seconds (8 minutes) during an active siege, and if all ControlTowers have been broken.
	 */
	private final int getTeleportDelay()
	{
		return (getCastle().getSiege().isInProgress() && getCastle().getAliveLifeTowerCount() == 0) ? 480 : 30;
	}
}