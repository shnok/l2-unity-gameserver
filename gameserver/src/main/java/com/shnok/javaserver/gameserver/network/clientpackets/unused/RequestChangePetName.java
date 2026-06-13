package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.enums.items.ItemState;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class RequestChangePetName extends L2GameClientPacket
{
	private static final String SEARCH_NAME = "SELECT name FROM pets WHERE name=?";
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// No active pet.
		if (!player.hasPet())
			return;
		
		// Name length integrity check.
		if (_name.length() < 1 || _name.length() > 16)
		{
			player.sendPacket(SystemMessageId.NAMING_CHARNAME_UP_TO_16CHARS);
			return;
		}
		
		// Pet is already named.
		final Pet pet = (Pet) player.getSummon();
		if (pet.getName() != null)
		{
			player.sendPacket(SystemMessageId.NAMING_YOU_CANNOT_SET_NAME_OF_THE_PET);
			return;
		}
		
		// Invalid name pattern.
		if (!StringUtil.isValidString(_name, "^[A-Za-z0-9]{1,16}$"))
		{
			player.sendPacket(SystemMessageId.NAMING_PETNAME_CONTAINS_INVALID_CHARS);
			return;
		}
		
		// Name is a npc name.
		if (NpcData.getInstance().getTemplateByName(_name) != null)
			return;
		
		// Name already exists on another pet.
		if (doesPetNameExist(_name))
		{
			player.sendPacket(SystemMessageId.NAMING_ALREADY_IN_USE_BY_ANOTHER_PET);
			return;
		}
		
		pet.setName(_name);
		
		// Refresh control item infos.
		final ItemInstance controlItem = pet.getControlItem();
		if (controlItem != null)
		{
			controlItem.setCustomType2(1);
			controlItem.updateState(player, ItemState.MODIFIED);
		}
		
		pet.sendPetInfosToOwner();
	}
	
	/**
	 * @param name : The name to search.
	 * @return true if such name already exists on database, false otherwise.
	 */
	private static boolean doesPetNameExist(String name)
	{
		boolean result = true;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SEARCH_NAME))
		{
			ps.setString(1, name);
			
			try (ResultSet rs = ps.executeQuery())
			{
				result = rs.next();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't check existing petname.", e);
		}
		return result;
	}
}