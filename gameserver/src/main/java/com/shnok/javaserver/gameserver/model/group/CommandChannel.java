package com.shnok.javaserver.gameserver.model.group;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.ExCloseMPCC;
import net.sf.l2j.gameserver.network.serverpackets.ExMPCCPartyInfoUpdate;
import net.sf.l2j.gameserver.network.serverpackets.ExOpenMPCC;
import net.sf.l2j.gameserver.network.serverpackets.ExShowScreenMessage;
import net.sf.l2j.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * Mass events, like sieges or raids require joining several {@link Party}. That's what {@link CommandChannel}s are for in the world of Lineage.<br>
 * <br>
 * Using a command channel:
 * <ul>
 * <li>A command channel can only be created by a character belonging to a clan level 4 and above. The clan needs to learn the Clan Imperium skill.</li>
 * <li>It takes at least 2 parties to create a command channel. One of the leaders need to use the /channelinvite command on any member of the other party.</li>
 * <li>Only the leader of a command channel can accept new parties to the CC.</li>
 * <li>If the main party is disbanded, the CC is disbanded as well.</li>
 * <li>If the CC leader is disconnected from the channel, a random player from the main party will become the new leader.</li>
 * <li>You can change the leader of the CC and the leader of the main party at the same time using the /changepartyleader [Character Name] command or from the Action Window.</li>
 * </ul>
 * More infos regarding Command Channel in general, and looting priorities in particular (official IL patch notes) :
 * <ul>
 * <li>Even without the clan skill Clan Imperium, it is possible to set up an allies channel and to send out invitations through an item, the Strategy Guide. The Strategy Guide item can be purchased in the Town of Aden, Rune Township, and the Town of Schuttgart.</li>
 * <li>While hunting a raid boss monster, the following requirements must be met in order to gain looting rights.
 * <ul>
 * <li>Raid monster: More than 18 members in the command channel.</li>
 * <li>Boss monsters (Queen Ant, Zaken, Orfen, Core, etc): More than 36 members in the command channel. When raiding Antharas, more than 225 members of the command channel must appear at Antharas' Nest in order to obtain first looting rights.</li>
 * </ul>
 * </li>
 * <li>Five minutes after the battle with a raid boss monster ends, the looting right automatically changes into ordinary looting. But if another raid succeeds in hunting the same raid monster before the looting right changes into ordinary looting, the first raid obtains the looting right to that
 * raid boss monster.</li>
 * </ul>
 */
public class CommandChannel extends AbstractGroup
{
	private final List<Party> _parties = new CopyOnWriteArrayList<>();
	
	public CommandChannel(Party requestor, Party target)
	{
		super(requestor.getLeader());
		
		_parties.add(requestor);
		_parties.add(target);
		
		requestor.setCommandChannel(this);
		target.setCommandChannel(this);
		
		recalculateLevel();
		
		for (Player member : requestor.getMembers())
		{
			member.sendPacket(SystemMessageId.COMMAND_CHANNEL_FORMED);
			member.sendPacket(ExOpenMPCC.STATIC_PACKET);
		}
		
		for (Player member : target.getMembers())
		{
			member.sendPacket(SystemMessageId.JOINED_COMMAND_CHANNEL);
			member.sendPacket(ExOpenMPCC.STATIC_PACKET);
		}
	}
	
	/**
	 * <b>BEWARE : create a temporary List. Uses containsPlayer whenever possible.</b>
	 */
	@Override
	public List<Player> getMembers()
	{
		final List<Player> members = new ArrayList<>();
		for (Party party : _parties)
			members.addAll(party.getMembers());
		
		return members;
	}
	
	@Override
	public int getMembersCount()
	{
		int count = 0;
		for (Party party : _parties)
			count += party.getMembersCount();
		
		return count;
	}
	
	@Override
	public boolean containsPlayer(WorldObject player)
	{
		for (Party party : _parties)
		{
			if (party.containsPlayer(player))
				return true;
		}
		return false;
	}
	
	@Override
	public void broadcastPacket(final L2GameServerPacket packet)
	{
		for (Party party : _parties)
			party.broadcastPacket(packet);
	}
	
	@Override
	public void broadcastCreatureSay(final CreatureSay msg, final Player broadcaster)
	{
		for (Party party : _parties)
			party.broadcastCreatureSay(msg, broadcaster);
	}
	
	@Override
	public void broadcastOnScreen(int time, NpcStringId npcStringId)
	{
		broadcastPacket(new ExShowScreenMessage(npcStringId.getMessage(), time));
	}
	
	@Override
	public void broadcastOnScreen(int time, NpcStringId npcStringId, Object... params)
	{
		broadcastPacket(new ExShowScreenMessage(npcStringId.getMessage(params), time));
	}
	
	@Override
	public void recalculateLevel()
	{
		int newLevel = 0;
		for (Party party : _parties)
		{
			if (party.getLevel() > newLevel)
				newLevel = party.getLevel();
		}
		setLevel(newLevel);
	}
	
	@Override
	public void disband()
	{
		for (Party party : _parties)
		{
			party.setCommandChannel(null);
			party.broadcastPacket(ExCloseMPCC.STATIC_PACKET);
			party.broadcastMessage(SystemMessageId.COMMAND_CHANNEL_DISBANDED);
		}
		_parties.clear();
	}
	
	/**
	 * Adds a {@link Party} to this {@link CommandChannel}.
	 * @param party : the Party to add.
	 */
	public void addParty(Party party)
	{
		// Null party or party is already registered in this command channel.
		if (party == null || _parties.contains(party))
			return;
		
		// Update the CCinfo for existing players.
		broadcastPacket(new ExMPCCPartyInfoUpdate(party, 1));
		
		_parties.add(party);
		
		if (party.getLevel() > getLevel())
			setLevel(party.getLevel());
		
		party.setCommandChannel(this);
		
		for (Player member : party.getMembers())
		{
			member.sendPacket(SystemMessageId.JOINED_COMMAND_CHANNEL);
			member.sendPacket(ExOpenMPCC.STATIC_PACKET);
		}
	}
	
	/**
	 * Removes a {@link Party} from this {@link CommandChannel}.
	 * @param party : the Party to remove. Disband the CommandChannel if there was only 2 parties left.
	 * @return true if the Party has been successfully removed from CommandChannel.
	 */
	public boolean removeParty(Party party)
	{
		// Null party or party isn't registered in this command channel.
		if (party == null || !_parties.contains(party))
			return false;
		
		// Don't bother individually drop parties, disband entirely if there is only 2 parties in command channel.
		if (_parties.size() == 2)
			disband();
		else
		{
			_parties.remove(party);
			
			party.setCommandChannel(null);
			party.broadcastPacket(ExCloseMPCC.STATIC_PACKET);
			
			recalculateLevel();
			
			// Update the CCinfo for existing players.
			broadcastPacket(new ExMPCCPartyInfoUpdate(party, 0));
		}
		return true;
	}
	
	/**
	 * @return the {@link List} of {@link Party} registered in this {@link CommandChannel}.
	 */
	public List<Party> getParties()
	{
		return _parties;
	}
	
	/**
	 * Check whether the leader of this {@link CommandChannel} is the same as the leader of the specified CommandChannel (which essentially means they're the same group).
	 * @param cc : The other CommandChannel to check against.
	 * @return true if this CommandChannel equals the specified CommandChannel, false otherwise.
	 */
	public boolean equals(CommandChannel cc)
	{
		return cc != null && getLeaderObjectId() == cc.getLeaderObjectId();
	}
	
	/**
	 * Check the possibility for a {@link Player} to setup a {@link CommandChannel}, and return him message if failed.
	 * @param player : The Player to test.
	 * @param deleteItem : If true, we delete the item, otherwise we simply check if it exists.
	 * @return true if the given Player set as parameter is a level 5 clan member with either Clan Imperium skill (skillId: 391) or a valid Strategy Guide item (itemId: 8871).
	 */
	public static boolean checkAuthority(Player player, boolean deleteItem)
	{
		// The Player isn't a level 5 clan leader.
		final Clan requestorClan = player.getClan();
		if (requestorClan == null || requestorClan.getLeaderId() != player.getObjectId() || requestorClan.getLevel() < 5)
		{
			player.sendPacket(SystemMessageId.COMMAND_CHANNEL_ONLY_BY_LEVEL_5_CLAN_LEADER_PARTY_LEADER);
			return false;
		}
		
		// The Player has Clan Imperium skill, return true instantly.
		if (player.getSkill(391) != null)
			return true;
		
		// The Player is now tested for Strategy Guide.
		final boolean hasItem = (deleteItem) ? player.destroyItemByItemId(8871, 1, true) : player.getInventory().getItemByItemId(8871) != null;
		if (!hasItem)
			player.sendPacket(SystemMessageId.CANNOT_LONGER_SETUP_COMMAND_CHANNEL);
		
		return hasItem;
	}
}