package com.shnok.javaserver.gameserver.model.olympiad;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;

/**
 * A model containing all informations related to a single {@link Olympiad} Participant.
 */
public final class Participant
{
	private final int _objectId;
	private final String _name;
	private final int _side;
	private final int _baseClass;
	private final OlympiadNoble _noble;
	
	private boolean _isDisconnected = false;
	private boolean _isDefecting = false;
	private Player _player;
	
	public Participant(Player player, int side)
	{
		_objectId = player.getObjectId();
		_player = player;
		_name = player.getName();
		_side = side;
		_baseClass = player.getBaseClass();
		_noble = Olympiad.getInstance().getNoble(_objectId);
	}
	
	public Participant(int objectId, int side)
	{
		_objectId = objectId;
		_player = null;
		_name = "-";
		_side = side;
		_baseClass = 0;
		_noble = null;
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getSide()
	{
		return _side;
	}
	
	public int getBaseClass()
	{
		return _baseClass;
	}
	
	public OlympiadNoble getNoble()
	{
		return _noble;
	}
	
	public boolean isDisconnected()
	{
		return _isDisconnected;
	}
	
	public void setDisconnection(boolean isDisconnected)
	{
		_isDisconnected = isDisconnected;
	}
	
	public boolean isDefecting()
	{
		return _isDefecting;
	}
	
	public void setDefection(boolean isDefecting)
	{
		_isDefecting = isDefecting;
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public void setPlayer(Player player)
	{
		_player = player;
	}
	
	public final void updatePlayer()
	{
		if (_player == null || !_player.isOnline())
			_player = World.getInstance().getPlayer(_objectId);
	}
}