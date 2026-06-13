package com.shnok.javaserver.gameserver.model.actor.container.player;

import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * A request between two {@link Player}s. It is associated to a 15 seconds timer, where both partner and packet references are destroyed.<br>
 * <br>
 * On request response, the task is canceled.
 */
public class Request
{
	private static final int REQUEST_TIMEOUT = 15000;
	
	private Player _player;
	private Player _partner;
	
	private L2GameClientPacket _requestPacket;
	
	private ScheduledFuture<?> _requestTimer;
	
	public Request(Player player)
	{
		_player = player;
	}
	
	/**
	 * @return the {@link Player} partner of a request.
	 */
	public synchronized Player getPartner()
	{
		return _partner;
	}
	
	/**
	 * Set the {@link Player} partner of a request.
	 * @param partner : The player to set as partner.
	 */
	private synchronized void setPartner(Player partner)
	{
		_partner = partner;
	}
	
	/**
	 * @return the {@link L2GameClientPacket} originally sent by the requestor.
	 */
	public synchronized L2GameClientPacket getRequestPacket()
	{
		return _requestPacket;
	}
	
	/**
	 * Set the {@link L2GameClientPacket} originally sent by the requestor.
	 * @param packet : The packet to set.
	 */
	private synchronized void setRequestPacket(L2GameClientPacket packet)
	{
		_requestPacket = packet;
	}
	
	private void clear()
	{
		_partner = null;
		_requestPacket = null;
	}
	
	/**
	 * Check if a request can be made ; if successful, put {@link Player}s on request state.
	 * @param partner : The player partner to check.
	 * @param packet : The packet to register.
	 * @return true if the request has succeeded.
	 */
	public synchronized boolean setRequest(Player partner, L2GameClientPacket packet)
	{
		if (partner == null)
		{
			_player.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return false;
		}
		
		if (partner.getRequest().isProcessingRequest())
		{
			_player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_BUSY_TRY_LATER).addCharName(partner));
			return false;
		}
		
		if (isProcessingRequest())
		{
			_player.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			return false;
		}
		
		_partner = partner;
		_requestPacket = packet;
		clearRequestOnTimeout();
		
		_partner.getRequest().setPartner(_player);
		_partner.getRequest().setRequestPacket(packet);
		_partner.getRequest().clearRequestOnTimeout();
		return true;
	}
	
	private void clearRequestOnTimeout()
	{
		_requestTimer = ThreadPool.schedule(this::clear, REQUEST_TIMEOUT);
	}
	
	/**
	 * Clear {@link Player} request state. Should be called after answer packet receive.
	 */
	public void onRequestResponse()
	{
		if (_requestTimer != null)
		{
			_requestTimer.cancel(true);
			_requestTimer = null;
		}
		
		clear();
		
		if (_partner != null)
			_partner.getRequest().clear();
	}
	
	/**
	 * @return true if a request is in progress.
	 */
	public boolean isProcessingRequest()
	{
		return _partner != null;
	}
}