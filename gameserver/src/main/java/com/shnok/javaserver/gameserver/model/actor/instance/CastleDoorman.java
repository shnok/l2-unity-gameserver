package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.residence.clanhall.SiegableHall;

/**
 * An instance type extending {@link Doorman}, used by castle doorman.<br>
 * <br>
 * isUnderSiege() checks current siege state associated to the doorman castle, while isOwnerClan() checks if the user is part of clan owning the castle and got the rights to open/close doors.
 */
public class CastleDoorman extends Doorman
{
	public CastleDoorman(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	protected void openDoors(Player player, String command)
	{
		if (getResidence() == null)
			return;
		
		for (String doorId : command.substring(11).split(", "))
			getResidence().openDoor(player, Integer.parseInt(doorId));
	}
	
	@Override
	protected final void closeDoors(Player player, String command)
	{
		if (getResidence() == null)
			return;
		
		for (String doorId : command.substring(12).split(", "))
			getResidence().closeDoor(player, Integer.parseInt(doorId));
	}
	
	@Override
	protected final boolean isOwnerClan(Player player)
	{
		if (player.getClan() != null)
		{
			if (getSiegableHall() != null)
				return player.getClanId() == getSiegableHall().getOwnerId() && player.hasClanPrivileges(PrivilegeType.CHP_ENTRY_EXIT_RIGHTS);
			
			if (getCastle() != null)
				return player.getClanId() == getCastle().getOwnerId() && player.hasClanPrivileges(PrivilegeType.CP_ENTRY_EXIT_RIGHTS);
		}
		return false;
	}
	
	@Override
	protected final boolean isUnderSiege()
	{
		final SiegableHall hall = getSiegableHall();
		if (hall != null)
			return hall.isInSiege();
		
		return getCastle() != null && getCastle().getSiegeZone().isActive();
	}
}