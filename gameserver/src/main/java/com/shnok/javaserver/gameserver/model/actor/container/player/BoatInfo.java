package com.shnok.javaserver.gameserver.model.actor.container.player;

import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.boat.BoatEngine;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GetOnVehicle;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.StopMoveInVehicle;

public class BoatInfo
{
	private final Player _player;
	
	private Boat _boat;
	
	private boolean _isBoatMovement = false;
	private boolean _canBoard = false;
	
	private final SpawnLocation _boatPosition = new SpawnLocation(0, 0, 0, 0);
	
	public BoatInfo(Player player)
	{
		_player = player;
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public void stopMoveInVehicle(int boatId)
	{
		_player.sendPacket(new StopMoveInVehicle(_player, boatId));
	}
	
	public boolean isBoatMovement()
	{
		return _isBoatMovement;
	}
	
	public void setBoatMovement(boolean isBoatMovement)
	{
		_isBoatMovement = isBoatMovement;
	}
	
	public boolean canBoard()
	{
		return _canBoard;
	}
	
	public void setCanBoard(boolean canBoard)
	{
		_canBoard = canBoard;
	}
	
	/**
	 * @return true if the current {@link Player} is linked to a {@link Boat}.
	 */
	public boolean isInBoat()
	{
		return _boat != null;
	}
	
	/**
	 * @return the {@link Boat} linked to the current {@link Player}.
	 */
	public Boat getBoat()
	{
		return _boat;
	}
	
	/**
	 * Set the {@link Boat} for the current {@link Player}.<br>
	 * <br>
	 * If the parameter is null but Player is registered into a Boat, we delete the passenger from the Boat.
	 * @param boat : The Boat to set, or null to clean it.
	 */
	public void setBoat(Boat boat)
	{
		if (boat == null && _boat != null)
		{
			// Remove passenger out from the Boat.
			_boat.getPassengers().remove(_player);
			
			// Clear the boat position.
			_boatPosition.clean();
		}
		_boat = boat;
	}
	
	/**
	 * @return the {@link SpawnLocation} related to Boat.
	 */
	public SpawnLocation getBoatPosition()
	{
		return _boatPosition;
	}
	
	public void sendInfo(Player player)
	{
		if (_boat != null)
			player.sendPacket(new GetOnVehicle(this));
	}
	
	/**
	 * @return the {@link Location} related to the docked {@link Boat}, if any, or {@link Location#DUMMY_LOC} if not found.
	 */
	public Location getDockLocation()
	{
		if (_boat == null)
			return Location.DUMMY_LOC;
		
		final BoatEngine engine = _boat.getEngine();
		if (engine == null)
			return Location.DUMMY_LOC;
		
		return engine.getDock().getOustLoc();
	}
}