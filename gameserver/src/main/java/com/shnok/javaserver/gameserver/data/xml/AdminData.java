package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.AccessLevel;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.AdminCommand;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

import org.w3c.dom.Document;

/**
 * This class loads and handles following concepts :
 * <ul>
 * <li>{@link AccessLevel}s retain informations such as isGM() and multiple allowed actions.</li>
 * <li>{@link AdminCommand}s with their names, authorized {@link AccessLevel}s and descriptions.</li>
 * <li>GM list holds GM {@link Player}s used by /gmlist. It also stores the hidden state.</li>
 * </ul>
 */
public final class AdminData implements IXmlReader
{
	private final TreeMap<Integer, AccessLevel> _accessLevels = new TreeMap<>();
	private final List<AdminCommand> _adminCommands = new ArrayList<>();
	private final Map<Player, Boolean> _gmList = new ConcurrentHashMap<>();
	
	protected AdminData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/accessLevels.xml");
		LOGGER.info("Loaded {} access levels.", _accessLevels.size());
		
		parseFile("data/xml/adminCommands.xml");
		LOGGER.info("Loaded {} admin command rights.", _adminCommands.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "access", accessNode ->
			{
				final StatSet set = parseAttributes(accessNode);
				_accessLevels.put(set.getInteger("level"), new AccessLevel(set));
			});
			
			forEach(listNode, "aCar", aCarNode -> _adminCommands.add(new AdminCommand(parseAttributes(aCarNode))));
		});
	}
	
	public void reload()
	{
		_accessLevels.clear();
		_adminCommands.clear();
		
		load();
	}
	
	public List<AdminCommand> getAdminCommands()
	{
		return _adminCommands;
	}
	
	/**
	 * @param level : The level to check.
	 * @return the {@link AccessLevel} based on its level.
	 */
	public AccessLevel getAccessLevel(int level)
	{
		return _accessLevels.get((level < 0) ? -1 : level);
	}
	
	/**
	 * @return the master {@link AccessLevel} level. It is always the AccessLevel with the highest level.
	 */
	public int getMasterAccessLevel()
	{
		return (_accessLevels.isEmpty()) ? 0 : _accessLevels.lastKey();
	}
	
	/**
	 * @param level : The level to check.
	 * @return true if an {@link AccessLevel} exists.
	 */
	public boolean hasAccessLevel(int level)
	{
		return _accessLevels.containsKey(level);
	}
	
	/**
	 * @param command : The admin command to check.
	 * @param accessToCheck : The {@link AccessLevel} to check.
	 * @return true if an AccessLevel can use a specific command (set as parameter).
	 */
	public boolean hasAccess(String command, AccessLevel accessToCheck)
	{
		final AdminCommand ac = _adminCommands.stream().filter(c -> c.name().equalsIgnoreCase(command)).findFirst().orElse(null);
		if (ac == null)
		{
			LOGGER.warn("No rights defined for admin command '{}'.", command);
			return false;
		}
		
		final AccessLevel access = getAccessLevel(ac.accessLevel());
		return access != null && (access.getLevel() == accessToCheck.getLevel() || accessToCheck.hasChildAccess(access));
	}
	
	/**
	 * @param includeHidden : If true, we add hidden GMs.
	 * @return the List of GM {@link Player}s. This List can include or not hidden GMs.
	 */
	public List<Player> getAllGms(boolean includeHidden)
	{
		final List<Player> list = new ArrayList<>();
		for (Entry<Player, Boolean> entry : _gmList.entrySet())
		{
			if (includeHidden || entry.getValue() == Boolean.FALSE)
				list.add(entry.getKey());
		}
		return list;
	}
	
	/**
	 * @param includeHidden : If true, we add hidden GMs.
	 * @return the List of GM {@link Player}s names. This List can include or not hidden GMs.
	 */
	public List<String> getAllGmNames(boolean includeHidden)
	{
		final List<String> list = new ArrayList<>();
		for (Entry<Player, Boolean> entry : _gmList.entrySet())
		{
			if (entry.getValue() == Boolean.FALSE)
				list.add(entry.getKey().getName());
			else if (includeHidden)
				list.add(entry.getKey().getName() + " (invis)");
		}
		return list;
	}
	
	/**
	 * Add a {@link Player} to the _gmList map.
	 * @param player : The Player to add on the map.
	 * @param hidden : The hidden state of this Player.
	 */
	public void addGm(Player player, boolean hidden)
	{
		_gmList.put(player, hidden);
	}
	
	/**
	 * Delete a {@link Player} from the _gmList map..
	 * @param player : The Player to remove from the map.
	 */
	public void deleteGm(Player player)
	{
		_gmList.remove(player);
	}
	
	/**
	 * Refresh hidden state for a GM {@link Player}.
	 * @param player : The GM to affect.
	 * @return the current GM state.
	 */
	public boolean showOrHideGm(Player player)
	{
		return _gmList.computeIfPresent(player, (k, v) -> !v);
	}
	
	/**
	 * @param includeHidden : Include or not hidden GM Players.
	 * @return true if at least one GM {@link Player} is online.
	 */
	public boolean isGmOnline(boolean includeHidden)
	{
		for (Entry<Player, Boolean> entry : _gmList.entrySet())
		{
			if (includeHidden || entry.getValue() == Boolean.FALSE)
				return true;
		}
		return false;
	}
	
	/**
	 * @param player : The player to test.
	 * @return true if this {@link Player} is registered as GM.
	 */
	public boolean isRegisteredAsGM(Player player)
	{
		return _gmList.containsKey(player);
	}
	
	/**
	 * Send the GM list of current online GM {@link Player}s to the Player set as parameter.
	 * @param player : The Player to send list.
	 */
	public void sendListToPlayer(Player player)
	{
		if (isGmOnline(player.isGM()))
		{
			player.sendPacket(SystemMessageId.GM_LIST);
			
			for (String name : getAllGmNames(player.isGM()))
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.GM_S1).addString(name));
		}
		else
		{
			player.sendPacket(SystemMessageId.NO_GM_PROVIDING_SERVICE_NOW);
			player.sendPacket(new PlaySound("systemmsg_e.702"));
		}
	}
	
	/**
	 * Broadcast to GM {@link Player}s a specific packet set as parameter.
	 * @param packet : The {@link L2GameServerPacket} packet to broadcast.
	 */
	public void broadcastToGMs(L2GameServerPacket packet)
	{
		for (Player gm : getAllGms(true))
			gm.sendPacket(packet);
	}
	
	/**
	 * Broadcast a message to GM {@link Player}s.
	 * @param message : The String message to broadcast.
	 */
	public void broadcastMessageToGMs(String message)
	{
		for (Player gm : getAllGms(true))
			gm.sendMessage(message);
	}
	
	public static AdminData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AdminData INSTANCE = new AdminData();
	}
}