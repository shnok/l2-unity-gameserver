package com.shnok.javaserver.gameserver.idfactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.math.PrimeFinder;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.commons.pool.ThreadPool;

/**
 * This class ensure data integrity and correct allocation of unique object ids towards objects.
 */
public class IdFactory
{
	private static final CLogger LOGGER = new CLogger(IdFactory.class.getName());
	
	public static final int FIRST_OID = 0x10000000;
	public static final int LAST_OID = 0x7FFFFFFF;
	public static final int FREE_OBJECT_ID_SIZE = LAST_OID - FIRST_OID;
	
	private BitSet _freeIds;
	private AtomicInteger _freeIdCount;
	private AtomicInteger _nextFreeId;
	
	protected IdFactory()
	{
		// Ensure data integrity, possibly cleaning objectIds.
		setAllCharacterOffline();
		cleanUpDB();
		cleanUpTimeStamps();
		
		// Initialize the IdFactory.
		initialize();
		
		// Run a 30sec task to see if the BitSet holding objectIds need to be raised.
		ThreadPool.scheduleAtFixedRate(() ->
		{
			if (reachingBitSetCapacity())
				increaseBitSetCapacity();
		}, 30000, 30000);
	}
	
	/**
	 * Initialize main parts of IdFactory.
	 * <ul>
	 * <li>Generate a new BitSet, and the associated counter.</li>
	 * <li>Allocate the used objectIds using database.</li>
	 * </ul>
	 */
	private void initialize()
	{
		_freeIds = new BitSet(PrimeFinder.nextPrime(100000));
		_freeIdCount = new AtomicInteger(FREE_OBJECT_ID_SIZE);
		
		// Get all used objectIds.
		final List<Integer> usedObjectIds = new ArrayList<>();
		
		try (Connection con = ConnectionPool.getConnection())
		{
			try (Statement st = con.createStatement())
			{
				try (ResultSet rs = st.executeQuery("SELECT obj_Id FROM characters"))
				{
					while (rs.next())
						usedObjectIds.add(rs.getInt(1));
				}
				
				try (ResultSet rs = st.executeQuery("SELECT object_id FROM items"))
				{
					while (rs.next())
						usedObjectIds.add(rs.getInt(1));
				}
				
				try (ResultSet rs = st.executeQuery("SELECT clan_id FROM clan_data"))
				{
					while (rs.next())
						usedObjectIds.add(rs.getInt(1));
				}
				
				try (ResultSet rs = st.executeQuery("SELECT object_id FROM items_on_ground"))
				{
					while (rs.next())
						usedObjectIds.add(rs.getInt(1));
				}
				
				try (ResultSet rs = st.executeQuery("SELECT id FROM mods_wedding"))
				{
					while (rs.next())
						usedObjectIds.add(rs.getInt(1));
				}
				
				try (ResultSet rs = st.executeQuery("SELECT oid FROM petition"))
				{
					while (rs.next())
						usedObjectIds.add(rs.getInt(1));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't properly initialize objectIds.", e);
		}
		
		// Register used objectIds.
		for (int usedObjectId : usedObjectIds)
		{
			final int objectId = usedObjectId - FIRST_OID;
			if (objectId < 0)
			{
				LOGGER.warn("Found invalid objectId {}. It is less than minimum of {}.", usedObjectId, FIRST_OID);
				continue;
			}
			
			_freeIds.set(objectId);
			_freeIdCount.decrementAndGet();
		}
		
		_nextFreeId = new AtomicInteger(_freeIds.nextClearBit(0));
		
		LOGGER.info("Initializing {} objectIds pool, with {} used ids.", _freeIds.size(), usedObjectIds.size());
	}
	
	/**
	 * Release the given objectId, making it accessible and allocable.
	 * @param objectID : The objectId to release.
	 */
	public synchronized void releaseId(int objectID)
	{
		if ((objectID - FIRST_OID) > -1)
		{
			_freeIds.clear(objectID - FIRST_OID);
			_freeIdCount.incrementAndGet();
		}
		else
			LOGGER.warn("Release objectId {} failed (< {}).", objectID, FIRST_OID);
	}
	
	/**
	 * @return the next available objectId. If there aren't anymore available ids, increase {@link BitSet} capacity.
	 */
	public synchronized int getNextId()
	{
		int newId = _nextFreeId.get();
		
		_freeIds.set(newId);
		_freeIdCount.decrementAndGet();
		
		int nextFree = _freeIds.nextClearBit(newId);
		
		if (nextFree < 0)
			nextFree = _freeIds.nextClearBit(0);
		
		if (nextFree < 0)
		{
			if (_freeIds.size() < FREE_OBJECT_ID_SIZE)
				increaseBitSetCapacity();
			else
				throw new NullPointerException("Ran out of valid Id's.");
		}
		
		_nextFreeId.set(nextFree);
		
		return newId + FIRST_OID;
	}
	
	/**
	 * @return the amount of used ids.
	 */
	protected int usedIdCount()
	{
		return _freeIdCount.get() - FIRST_OID;
	}
	
	/**
	 * @return true if the {@link BitSet} is almost full.
	 */
	protected synchronized boolean reachingBitSetCapacity()
	{
		return PrimeFinder.nextPrime(usedIdCount() * 11 / 10) > _freeIds.size();
	}
	
	/**
	 * Increase the {@link BitSet} capacity.
	 */
	protected synchronized void increaseBitSetCapacity()
	{
		BitSet newBitSet = new BitSet(PrimeFinder.nextPrime(usedIdCount() * 11 / 10));
		newBitSet.or(_freeIds);
		_freeIds = newBitSet;
	}
	
	/**
	 * Sets all Players offline.
	 */
	private static void setAllCharacterOffline()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("UPDATE characters SET online = 0"))
		{
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.warn("Couldn't set characters offline.", e);
		}
		LOGGER.info("Updated characters online status.");
	}
	
	/**
	 * Cleanup Players related data. Used to ensure data integrity.
	 */
	private static void cleanUpDB()
	{
		int cleanCount = 0;
		
		try (Connection con = ConnectionPool.getConnection())
		{
			try (Statement stmt = con.createStatement())
			{
				// Character related
				cleanCount += stmt.executeUpdate("DELETE FROM augmentations WHERE augmentations.item_oid NOT IN (SELECT object_id FROM items);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_hennas WHERE character_hennas.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_macroses WHERE character_macroses.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_memo WHERE character_memo.charId NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_quests WHERE character_quests.charId NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_raid_points WHERE character_raid_points.char_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_recipebook WHERE character_recipebook.charId NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_relations WHERE character_relations.char_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_relations WHERE character_relations.friend_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_shortcuts WHERE character_shortcuts.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_skills WHERE character_skills.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_skills_save WHERE character_skills_save.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM character_subclasses WHERE character_subclasses.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM cursed_weapons WHERE cursed_weapons.playerId NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM pets WHERE pets.item_obj_id NOT IN (SELECT object_id FROM items UNION SELECT object_id FROM items_on_ground);");
				cleanCount += stmt.executeUpdate("DELETE FROM seven_signs WHERE seven_signs.char_obj_id NOT IN (SELECT obj_Id FROM characters);");
				
				// Olympiads & Heroes
				cleanCount += stmt.executeUpdate("DELETE FROM heroes WHERE heroes.char_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM olympiad_nobles WHERE olympiad_nobles.char_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM olympiad_nobles_eom WHERE olympiad_nobles_eom.char_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM olympiad_fights WHERE olympiad_fights.charOneId NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM olympiad_fights WHERE olympiad_fights.charTwoId NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM heroes_diary WHERE heroes_diary.char_id NOT IN (SELECT obj_Id FROM characters);");
				
				// Auction
				cleanCount += stmt.executeUpdate("DELETE FROM auctions WHERE clanhall_id IN (SELECT id FROM clanhall WHERE ownerId <> 0 AND sellerClanName='');");
				
				// Clan related
				cleanCount += stmt.executeUpdate("DELETE FROM clan_data WHERE clan_data.leader_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM auctions WHERE auctions.clan_oid NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM clanhall_functions WHERE clanhall_functions.hall_id NOT IN (SELECT id FROM clanhall WHERE ownerId <> 0);");
				cleanCount += stmt.executeUpdate("DELETE FROM clan_privs WHERE clan_privs.clan_id NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM clan_skills WHERE clan_skills.clan_id NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM clan_subpledges WHERE clan_subpledges.clan_id NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM clan_wars WHERE clan_wars.clan1 NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM clan_wars WHERE clan_wars.clan2 NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM siege_clans WHERE siege_clans.clan_id NOT IN (SELECT clan_id FROM clan_data);");
				
				// Items
				cleanCount += stmt.executeUpdate("DELETE FROM items WHERE items.owner_id NOT IN (SELECT obj_Id FROM characters) AND items.owner_id NOT IN (SELECT clan_id FROM clan_data);");
				
				// Forum related
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_forum WHERE bbs_forum.type='CLAN' AND bbs_forum.owner_id NOT IN (SELECT clan_id FROM clan_data);");
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_forum WHERE bbs_forum.type='MEMO' AND bbs_forum.owner_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_topic WHERE bbs_topic.forum_id NOT IN (SELECT id FROM bbs_forum);");
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_post WHERE bbs_post.forum_id NOT IN (SELECT id FROM bbs_forum);");
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_post WHERE bbs_post.topic_id NOT IN (SELECT id FROM bbs_topic);");
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_favorite WHERE bbs_favorite.player_id NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM bbs_mail WHERE bbs_mail.receiver_id NOT IN (SELECT obj_Id FROM characters);");
				
				// Petition
				cleanCount += stmt.executeUpdate("DELETE FROM petition WHERE petition.petitioner_oid NOT IN (SELECT obj_Id FROM characters);");
				cleanCount += stmt.executeUpdate("DELETE FROM petition_message WHERE petition_message.petition_oid NOT IN (SELECT oid FROM petition);");
				
				stmt.executeUpdate("UPDATE clan_data SET auction_bid_at = 0 WHERE auction_bid_at NOT IN (SELECT clanhall_id FROM auctions);");
				stmt.executeUpdate("UPDATE clan_data SET new_leader_id = 0 WHERE new_leader_id NOT IN (SELECT obj_Id FROM characters);");
				stmt.executeUpdate("UPDATE clan_subpledges SET leader_id=0 WHERE clan_subpledges.leader_id NOT IN (SELECT obj_Id FROM characters) AND leader_id > 0;");
				stmt.executeUpdate("UPDATE castle SET currentTaxPercent=0, nextTaxPercent=0 WHERE castle.id NOT IN (SELECT hasCastle FROM clan_data);");
				stmt.executeUpdate("UPDATE characters SET clanid=0 WHERE characters.clanid NOT IN (SELECT clan_id FROM clan_data);");
				stmt.executeUpdate("UPDATE clanhall SET ownerId=0, paidUntil=0, paid=0 WHERE clanhall.ownerId NOT IN (SELECT clan_id FROM clan_data);");
			}
		}
		catch (Exception e)
		{
			LOGGER.warn("Couldn't cleanup database.", e);
		}
		LOGGER.info("Cleaned {} elements from database.", cleanCount);
	}
	
	/**
	 * Cleanup Players skills timestamps. Used to ensure data integrity.
	 */
	private static void cleanUpTimeStamps()
	{
		int cleanCount = 0;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM character_skills_save WHERE restore_type = 1 AND systime <= ?"))
		{
			ps.setLong(1, System.currentTimeMillis());
			
			cleanCount += ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.warn("Couldn't cleanup timestamps.", e);
		}
		LOGGER.info("Cleaned {} expired timestamps from database.", cleanCount);
	}
	
	public static final IdFactory getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final IdFactory INSTANCE = new IdFactory();
	}
}