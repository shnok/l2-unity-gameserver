package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;

/**
 * Custom wedding system implementation.<br>
 * <br>
 * Loads and stores couples using {@link IntIntHolder}, id being requesterId and value being partnerId.
 */
public class CoupleManager
{
	private static final CLogger LOGGER = new CLogger(CoupleManager.class.getName());
	
	private static final String LOAD_COUPLES = "SELECT * FROM mods_wedding";
	private static final String TRUNCATE_COUPLES = "TRUNCATE mods_wedding";
	private static final String ADD_COUPLE = "INSERT INTO mods_wedding (id, requesterId, partnerId) VALUES (?,?,?)";
	
	private final Map<Integer, IntIntHolder> _couples = new ConcurrentHashMap<>();
	
	protected CoupleManager()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_COUPLES);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
				_couples.put(rs.getInt("id"), new IntIntHolder(rs.getInt("requesterId"), rs.getInt("partnerId")));
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load couples.", e);
		}
		LOGGER.info("Loaded {} couples.", _couples.size());
	}
	
	public final Map<Integer, IntIntHolder> getCouples()
	{
		return _couples;
	}
	
	public final IntIntHolder getCouple(int coupleId)
	{
		return _couples.get(coupleId);
	}
	
	/**
	 * Add a couple to the couples map. Both players must be logged.
	 * @param requester : The wedding requester.
	 * @param partner : The wedding partner.
	 */
	public void addCouple(Player requester, Player partner)
	{
		if (requester == null || partner == null)
			return;
		
		final int coupleId = IdFactory.getInstance().getNextId();
		
		_couples.put(coupleId, new IntIntHolder(requester.getObjectId(), partner.getObjectId()));
		
		requester.setCoupleId(coupleId);
		partner.setCoupleId(coupleId);
	}
	
	/**
	 * Delete the couple. If players are logged, reset wedding variables.
	 * @param coupleId : The couple id to delete.
	 */
	public void deleteCouple(int coupleId)
	{
		final IntIntHolder couple = _couples.remove(coupleId);
		if (couple == null)
			return;
		
		// Inform and reset the couple id of requester.
		final Player requester = World.getInstance().getPlayer(couple.getId());
		if (requester != null)
		{
			requester.setCoupleId(0);
			requester.sendMessage("You are now divorced.");
		}
		
		// Inform and reset the couple id of partner.
		final Player partner = World.getInstance().getPlayer(couple.getValue());
		if (partner != null)
		{
			partner.setCoupleId(0);
			partner.sendMessage("You are now divorced.");
		}
		
		// Release the couple id.
		IdFactory.getInstance().releaseId(coupleId);
	}
	
	/**
	 * Save all couples on shutdown. Delete previous SQL infos.
	 */
	public void save()
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(TRUNCATE_COUPLES))
			{
				ps.execute();
			}
			
			try (PreparedStatement ps = con.prepareStatement(ADD_COUPLE))
			{
				for (Entry<Integer, IntIntHolder> coupleEntry : _couples.entrySet())
				{
					final IntIntHolder couple = coupleEntry.getValue();
					
					ps.setInt(1, coupleEntry.getKey());
					ps.setInt(2, couple.getId());
					ps.setInt(3, couple.getValue());
					ps.addBatch();
				}
				ps.executeBatch();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't add a couple.", e);
		}
	}
	
	/**
	 * @param coupleId : The couple id to check.
	 * @param objectId : The player objectId to check.
	 * @return the partner objectId, or 0 if not found.
	 */
	public final int getPartnerId(int coupleId, int objectId)
	{
		final IntIntHolder couple = _couples.get(coupleId);
		if (couple == null)
			return 0;
		
		return (couple.getId() == objectId) ? couple.getValue() : couple.getId();
	}
	
	public static final CoupleManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CoupleManager INSTANCE = new CoupleManager();
	}
}