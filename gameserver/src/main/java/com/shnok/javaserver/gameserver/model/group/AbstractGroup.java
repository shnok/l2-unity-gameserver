package com.shnok.javaserver.gameserver.model.group;

import java.util.List;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public abstract class AbstractGroup
{
	private Player _leader;
	private int _level;
	
	protected AbstractGroup(Player leader)
	{
		_leader = leader;
	}
	
	/**
	 * @return a list of all members of this group.
	 */
	public abstract List<Player> getMembers();
	
	/**
	 * @return the count of all players in this group.
	 */
	public abstract int getMembersCount();
	
	/**
	 * Check if this group contains a given player.
	 * @param player : the player to check.
	 * @return {@code true} if this group contains the specified player, {@code false} otherwise.
	 */
	public abstract boolean containsPlayer(final WorldObject player);
	
	/**
	 * Broadcast a {@link L2GameServerPacket} to every member of this group.
	 * @param packet : The {@link L2GameServerPacket} to broadcast.
	 */
	public abstract void broadcastPacket(final L2GameServerPacket packet);
	
	/**
	 * Broadcast a {@link CreatureSay} packet to every member of this group. Similar to broadcastPacket, but with an embbed BlockList check.
	 * @param msg : The {@link CreatureSay} to broadcast.
	 * @param broadcaster : The {@link Player} who broadcasts the message.
	 */
	public abstract void broadcastCreatureSay(final CreatureSay msg, final Player broadcaster);
	
	/**
	 * Broadcast a {@link ExShowScreenMessage} packet to every member of this group.
	 * @param time : The time to show the message on screen.
	 * @param npcStringId : The {@link NpcStringId} to send.
	 */
	public abstract void broadcastOnScreen(int time, NpcStringId npcStringId);
	
	/**
	 * Broadcast a {@link ExShowScreenMessage} packet to every member of this group.
	 * @param time : The time to show the message on screen.
	 * @param npcStringId : The {@link NpcStringId} to send.
	 * @param params : Additional parameters for {@link NpcStringId} construction.
	 */
	public abstract void broadcastOnScreen(int time, NpcStringId npcStringId, Object... params);
	
	/**
	 * Recalculate the group level.
	 */
	public abstract void recalculateLevel();
	
	/**
	 * Destroy that group, resetting all possible values, leading to that group object destruction.
	 */
	public abstract void disband();
	
	/**
	 * @return the level of this group.
	 */
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Change the level of this group. <b>Used only when the group is created.</b>
	 * @param level : the level to set.
	 */
	public void setLevel(int level)
	{
		_level = level;
	}
	
	/**
	 * @return the leader of this group.
	 */
	public Player getLeader()
	{
		return _leader;
	}
	
	/**
	 * Change the leader of this group to the specified player.
	 * @param leader : the player to set as the new leader of this group.
	 */
	public void setLeader(Player leader)
	{
		_leader = leader;
	}
	
	/**
	 * @return the leader objectId.
	 */
	public int getLeaderObjectId()
	{
		return _leader.getObjectId();
	}
	
	/**
	 * Check if a given player is the leader of this group.
	 * @param player : the player to check.
	 * @return {@code true} if the specified player is the leader of this group, {@code false} otherwise.
	 */
	public boolean isLeader(Player player)
	{
		return _leader.getObjectId() == player.getObjectId();
	}
	
	/**
	 * Broadcast a system message to this group.
	 * @param message : the system message to broadcast.
	 */
	public void broadcastMessage(SystemMessageId message)
	{
		broadcastPacket(SystemMessage.getSystemMessage(message));
	}
	
	/**
	 * Broadcast a custom text message to this group.
	 * @param text : the custom string to broadcast.
	 */
	public void broadcastString(String text)
	{
		broadcastPacket(SystemMessage.sendString(text));
	}
	
	/**
	 * @return a random member of this group.
	 */
	public Player getRandomPlayer()
	{
		return Rnd.get(getMembers());
	}
}