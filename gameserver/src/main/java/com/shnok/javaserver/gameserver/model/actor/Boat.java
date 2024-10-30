package com.shnok.javaserver.gameserver.model.actor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.xml.RestartPointData;
import com.shnok.javaserver.gameserver.enums.RestartType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.actors.OperateType;
import com.shnok.javaserver.gameserver.enums.boats.BoatDock;
import com.shnok.javaserver.gameserver.enums.boats.BoatState;
import com.shnok.javaserver.gameserver.model.actor.ai.type.BoatAI;
import com.shnok.javaserver.gameserver.model.actor.move.BoatMove;
import com.shnok.javaserver.gameserver.model.actor.status.BoatStatus;
import com.shnok.javaserver.gameserver.model.actor.template.CreatureTemplate;
import com.shnok.javaserver.gameserver.model.boat.BoatEngine;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.TargetUnselected;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.VehicleInfo;

public class Boat extends Creature
{
	private final Set<Player> _passengers = ConcurrentHashMap.newKeySet();
	
	private BoatEngine _engine;
	private Future<?> _payTask;
	
	public Boat(int objectId, CreatureTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public BoatAI getAI()
	{
		return (BoatAI) _ai;
	}
	
	@Override
	public void setAI()
	{
		_ai = new BoatAI(this);
	}
	
	@Override
	public BoatStatus getStatus()
	{
		return (BoatStatus) _status;
	}
	
	@Override
	public void setStatus()
	{
		_status = new BoatStatus(this);
	}
	
	@Override
	public BoatMove getMove()
	{
		return (BoatMove) _move;
	}
	
	@Override
	public void setMove()
	{
		_move = new BoatMove(this);
	}
	
	@Override
	public boolean teleportTo(int x, int y, int z, int randomOffset)
	{
		// Don't teleport if previous teleporting is not finished yet
		if (!_isTeleporting.compareAndSet(false, true))
			return false;
		
		stopPayTask();
		
		getMove().stop();
		
		for (Player player : _passengers)
			player.teleportTo(x, y, z, randomOffset);
		
		decayMe();
		setXYZ(x, y, z);
		
		onTeleported();
		revalidateZone(true);
		return true;
	}
	
	@Override
	public void deleteMe()
	{
		stopPayTask();
		
		_engine = null;
		
		getMove().stop();
		
		// Oust all players.
		oustPlayers();
		
		// Decay the vehicle.
		decayMe();
		
		super.deleteMe();
	}
	
	@Override
	public void updateAbnormalEffect()
	{
		// Do nothing.
	}
	
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public Weapon getActiveWeaponItem()
	{
		return null;
	}
	
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public Weapon getSecondaryWeaponItem()
	{
		return null;
	}
	
	@Override
	public void sendInfo(Player player)
	{
		player.sendPacket(new VehicleInfo(this));
	}
	
	@Override
	public boolean isFlying()
	{
		return true;
	}
	
	@Override
	public void onInteract(Player actor)
	{
		// Do nothing.
	}
	
	@Override
	public void onAction(Player player, boolean isCtrlPressed, boolean isShiftPressed)
	{
		// Do nothing.
	}

	@Override
	public void onTarget(Player player, boolean isShiftPressed)
	{
		player.sendPacket(new TargetUnselected(player));
	}
	
	@Override
	public boolean isAttackableBy(Creature attacker)
	{
		return false;
	}
	
	public Set<Player> getPassengers()
	{
		return _passengers;
	}
	
	/**
	 * Oust all {@Player}s set as passengers to TeleportType.TOWN.
	 */
	public void oustPlayers()
	{
		for (Player player : _passengers)
			oustPlayer(player, RestartPointData.getInstance().getLocationToTeleport(player, RestartType.TOWN));
	}
	
	/**
	 * Oust a {@Player} out of this {@link Boat}.
	 * <ul>
	 * <li>Remove him from peace zone.</li>
	 * <li>Teleport him back to a valid zone, or Location set as parameter. setBoat(null) is embedded into teleportTo, no need to care about it.</li>
	 * </ul>
	 * In case he is offline, his position is forced to be edited, and setBoat(null) applies.
	 * @param player : The Player to oust.
	 * @param loc : The Location used as oust.
	 */
	public void oustPlayer(Player player, Location loc)
	{
		// Player in shop mode got their shop broken. They're still sit down.
		if (player.isInStoreMode())
		{
			player.setOperateType(OperateType.NONE);
			player.broadcastUserInfo();
		}
		
		if (player.isOnline())
			player.teleportTo(loc.getX(), loc.getY(), loc.getZ(), 0);
		else
		{
			removePassenger(player);
			
			player.setXYZInvisible(loc);
		}
	}
	
	/**
	 * Test and add a {@link Player} passenger to this {@link Boat} if conditions matched.
	 * @param player : The {@link Player} to test.
	 */
	public void addPassenger(Player player)
	{
		final Boat boat = player.getBoatInfo().getBoat();
		
		// Player is already set on another Boat, or isn't set on any Boat.
		if (boat != this)
			return;
		
		// Can't add the passenger.
		if (!_passengers.add(player))
			return;
		
		player.setInsideZone(ZoneId.PEACE, true);
		player.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true);
		
		player.sendPacket(SystemMessageId.ENTER_PEACEFUL_ZONE);
	}
	
	/**
	 * Remove a {@link Player} passenger from this {@link Boat}.
	 * @param player : The {@link Player} to test.
	 */
	public void removePassenger(Player player)
	{
		// Stop eventual running pay task.
		stopPayTask();
		
		// Set the Player's Boat as null.
		player.getBoatInfo().setBoat(null);
		
		// Set zones off.
		player.setInsideZone(ZoneId.PEACE, false);
		player.setInsideZone(ZoneId.NO_SUMMON_FRIEND, false);
		
		// Send message.
		player.sendPacket(SystemMessageId.EXIT_PEACEFUL_ZONE);
		
		// Delete the Player from Boat passengers' list.
		_passengers.remove(player);
	}
	
	/**
	 * Consume passengers tickets from this {@link Boat} and teleport {@link Player}s if they don't own one.
	 * @param itemId : The itemId to check.
	 * @param loc : The {@link Location} used as oust in case a {@link Player} can't pay.
	 */
	public void payForRide(int itemId, Location loc)
	{
		// Stop eventual running pay task.
		stopPayTask();
		
		// No ticket set on server, don't bother running the pay task.
		if (itemId <= 0)
			return;
		
		// Start task after 5sec.
		_payTask = ThreadPool.schedule(() ->
		{
			for (Player player : _passengers)
			{
				// Test if item can be destroyed. If not found, oust the Player out of the Boat.
				if (player.destroyItemByItemId(itemId, 1, false))
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DISAPPEARED).addItemName(itemId));
				else
				{
					oustPlayer(player, loc);
					player.sendPacket(SystemMessageId.NOT_CORRECT_BOAT_TICKET);
				}
			}
		}, 5000);
	}
	
	/**
	 * Stop task if already running.
	 */
	private void stopPayTask()
	{
		if (_payTask != null)
		{
			_payTask.cancel(false);
			_payTask = null;
		}
	}
	
	public void setEngine(BoatEngine engine)
	{
		_engine = engine;
	}
	
	public BoatEngine getEngine()
	{
		return _engine;
	}
	
	public BoatDock getDock()
	{
		return (_engine == null) ? null : _engine.getDock();
	}
	
	public boolean isDocked()
	{
		return _engine != null && _engine.getState() == BoatState.DOCKED;
	}
}