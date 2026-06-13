package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.xml.AdminData;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionState;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionType;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.Petition;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.CreatureSay;

/**
 * Store all existing {@link Petition}s.<br>
 * <br>
 * An "active" {@link Petition} stands for its {@link PetitionState} being either PENDING or ACCEPTED.
 */
public final class PetitionManager
{
	private static final CLogger LOGGER = new CLogger(PetitionManager.class.getName());
	
	private static final String SELECT_PETITIONS = "SELECT * FROM petition ORDER BY oid ASC";
	private static final String TRUNCATE_PETITIONS = "TRUNCATE petition";
	private static final String INSERT_PETITION = "INSERT INTO petition (oid, type, petitioner_oid, submit_date, content, is_unread, state, rate, feedback, responders) VALUES (?,?,?,?,?,?,?,?,?,?)";
	
	private static final String SELECT_PETITION_MESSAGES = "SELECT * FROM petition_message ORDER BY id ASC, petition_oid ASC";
	private static final String TRUNCATE_PETITION_MESSAGES = "TRUNCATE petition_message";
	private static final String INSERT_PETITION_MESSAGE = "INSERT INTO petition_message (id, petition_oid, player_oid, type, player_name, content) VALUES (?,?,?,?,?,?)";
	
	private final Map<Integer, Petition> _petitions = new ConcurrentSkipListMap<>();
	
	protected PetitionManager()
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(SELECT_PETITIONS);
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
					_petitions.put(rs.getInt("oid"), new Petition(rs));
			}
			
			try (PreparedStatement ps = con.prepareStatement(SELECT_PETITION_MESSAGES);
				ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final Petition petition = _petitions.get(rs.getInt("petition_oid"));
					if (petition == null)
						continue;
					
					petition.addMessage(new CreatureSay(rs));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't load petitions.", e);
		}
		LOGGER.info("Loaded {} petitions.", _petitions.size());
	}
	
	public Map<Integer, Petition> getPetitions()
	{
		return _petitions;
	}
	
	public List<Petition> getPetitions(Predicate<Petition> predicate)
	{
		return _petitions.values().stream().filter(predicate).toList();
	}
	
	/**
	 * @return The total {@link Petition} count under either {@link PetitionState#PENDING} or {@link PetitionState#ACCEPTED}.
	 */
	public int getActivePetitionsCount()
	{
		if (_petitions.isEmpty())
			return 0;
		
		return (int) _petitions.values().stream().filter(p -> p.getState() == PetitionState.PENDING || p.getState() == PetitionState.ACCEPTED).count();
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return The {@link Petition} count associated to the {@link Player} set as parameter. His self canceled {@link Petition}s aren't counted.
	 */
	public int getPetitionsCount(Player player)
	{
		if (player == null)
			return 0;
		
		return (int) _petitions.values().stream().filter(p -> p.getPetitionerObjectId() == player.getObjectId() && p.getState() != PetitionState.CANCELLED).count();
	}
	
	/**
	 * @return True if any {@link Petition} is under {@link PetitionState#ACCEPTED}, or false otherwise.
	 */
	public boolean isAnyPetitionInProcess()
	{
		return _petitions.values().stream().anyMatch(p -> p.getState() == PetitionState.ACCEPTED);
	}
	
	/**
	 * @param id : The {@link Petition} id to test.
	 * @return True if the {@link Petition} holding the id set as parameter is under {@link PetitionState#ACCEPTED}, or false otherwise.
	 */
	public boolean isPetitionInProcess(int id)
	{
		final Petition petition = _petitions.get(id);
		return petition != null && petition.getState() == PetitionState.ACCEPTED;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return The {@link Petition} associated to the {@link Player} set as parameter under {@link PetitionState#ACCEPTED}, or null if not found.
	 */
	public Petition getPetitionInProcess(Player player)
	{
		if (player == null)
			return null;
		
		for (Petition petition : getPetitions(p -> p.getState() == PetitionState.ACCEPTED))
		{
			if (petition.getPetitionerObjectId() == player.getObjectId() || petition.getResponders().contains(player.getObjectId()))
				return petition;
		}
		return null;
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return The {@link Petition} associated to the {@link Player} set as parameter under {@link PetitionState#CLOSED} and with _isUnderFeedback flag, or null if not found.
	 */
	public Petition getFeedbackPetition(Player player)
	{
		if (player == null)
			return null;
		
		return _petitions.values().stream().filter(p -> p.getState() == PetitionState.CLOSED && p.isUnderFeedback() && p.getPetitionerObjectId() == player.getObjectId()).findAny().orElse(null);
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return True if a {@link Petition} is associated to the {@link Player} set as parameter under either {@link PetitionState#PENDING} or {@link PetitionState#ACCEPTED}, or false otherwise.
	 */
	public boolean isActivePetition(Player player)
	{
		if (player == null)
			return false;
		
		return _petitions.values().stream().anyMatch(p -> p.getPetitionerObjectId() == player.getObjectId() && (p.getState() == PetitionState.PENDING || p.getState() == PetitionState.ACCEPTED));
	}
	
	/**
	 * Generate a {@link Petition} and store it, then warn GMs.
	 * @param type : The {@link PetitionType} to use.
	 * @param player : The {@link Player} who petitioned.
	 * @param content : The initial {@link String} message to send.
	 * @return the objectId of the newly created {@link Petition}.
	 */
	public int submitPetition(PetitionType type, Player player, String content)
	{
		// Create a new petition instance and add it to the list of pending petitions.
		final Petition petition = new Petition(type, player.getObjectId(), content);
		
		_petitions.put(petition.getId(), petition);
		
		// Notify all GMs that a new petition has been submitted.
		AdminData.getInstance().broadcastToGMs(new CreatureSay(player.getObjectId(), SayType.HERO_VOICE, "Petition System", player.getName() + " has submitted a new petition."));
		
		return petition.getId();
	}
	
	/**
	 * Abort the active {@link Petition} of the {@link Player} set as parameter.
	 * @param player : The {@link Player} to test.
	 */
	public void abortActivePetition(Player player)
	{
		final Petition activePetition = getPetitionInProcess(player);
		if (activePetition != null)
			activePetition.abortConsultation(player);
	}
	
	/**
	 * Attempt to join a {@link Petition}. Abort the precedent active {@link Petition} if existing (setting it back to PENDING).
	 * @param player : The {@link Player} to test.
	 * @param id : The {@link Petition} id to test.
	 * @param isEnforcing : If True, send messages related to //force_peti, otherwise send the classic messages.
	 * @return True if the {@link Player} set as parameter successfully joined, or false otherwise.
	 */
	public boolean joinPetition(Player player, int id, boolean isEnforcing)
	{
		// An active Petition exists, replace it as PENDING.
		abortActivePetition(player);
		
		final Petition petition = _petitions.get(id);
		return petition != null && petition.join(player, isEnforcing);
	}
	
	/**
	 * Reject the {@link Petition} id.
	 * @param player : The {@link Player} to test.
	 * @param id : The {@link Petition} id to test.
	 * @return True if the {@link Petition} was successfully rejected, or false otherwise.
	 */
	public boolean rejectPetition(Player player, int id)
	{
		final Petition petition = _petitions.get(id);
		if (petition == null || petition.getState() == PetitionState.REJECTED)
			return false;
		
		petition.addResponder(player);
		petition.endConsultation(PetitionState.REJECTED);
		return true;
	}
	
	public boolean cancelPendingPetition(Player player)
	{
		final Petition petition = _petitions.values().stream().filter(p -> p.getState() == PetitionState.PENDING && p.getPetitionerObjectId() == player.getObjectId()).findAny().orElse(null);
		if (petition == null)
			return false;
		
		petition.endConsultation(PetitionState.CANCELLED);
		return true;
	}
	
	/**
	 * Check active {@link Petition} for the {@link Player} set as parameter, and send it.<br>
	 * <br>
	 * Used during EnterWorld call to retrieve automatically the {@link Petition}.
	 * @param player : The {@link Player} to test.
	 */
	public void checkActivePetition(Player player)
	{
		if (player == null)
			return;
		
		for (Petition petition : getPetitions(p -> p.getState() == PetitionState.PENDING || p.getState() == PetitionState.ACCEPTED))
		{
			if (petition.getPetitionerObjectId() == player.getObjectId() || petition.getResponders().contains(player.getObjectId()))
			{
				petition.showCompleteLog(player);
				return;
			}
		}
	}
	
	public void showCompleteLog(Player player, int id)
	{
		final Petition petition = _petitions.get(id);
		if (petition == null)
			return;
		
		petition.showCompleteLog(player);
	}
	
	/**
	 * Release all ids and clear the Map.
	 */
	public void clearPetitions()
	{
		// Release all ids.
		_petitions.values().forEach(p -> IdFactory.getInstance().releaseId(p.getId()));
		
		// Clear the Map.
		_petitions.clear();
	}
	
	public void store()
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			// Delete all entries from database.
			try (PreparedStatement ps = con.prepareStatement(TRUNCATE_PETITIONS))
			{
				ps.execute();
			}
			
			// Save petitions.
			try (PreparedStatement ps = con.prepareStatement(INSERT_PETITION))
			{
				for (Petition petition : _petitions.values())
				{
					final boolean mustBeReset = petition.getState() == PetitionState.ACCEPTED && petition.getMessages().isEmpty();
					
					ps.setInt(1, petition.getId());
					ps.setString(2, petition.getType().toString());
					ps.setInt(3, petition.getPetitionerObjectId());
					ps.setLong(4, petition.getSubmitDate());
					ps.setString(5, petition.getContent());
					ps.setInt(6, petition.isUnread() ? 1 : 0);
					ps.setString(7, (mustBeReset) ? "PENDING" : petition.getState().toString());
					ps.setString(8, petition.getRate().toString());
					ps.setString(9, petition.getFeedback());
					ps.setString(10, (mustBeReset) ? "" : petition.getResponders().stream().map(String::valueOf).collect(Collectors.joining(";")));
					ps.addBatch();
				}
				ps.executeBatch();
			}
			
			// Delete all entries from database.
			try (PreparedStatement ps = con.prepareStatement(TRUNCATE_PETITION_MESSAGES))
			{
				ps.execute();
			}
			
			// Save petitions messages.
			try (PreparedStatement ps = con.prepareStatement(INSERT_PETITION_MESSAGE))
			{
				for (Petition petition : _petitions.values())
				{
					int id = 0;
					
					for (CreatureSay cs : petition.getMessages())
					{
						ps.setInt(1, id++);
						ps.setInt(2, petition.getId());
						ps.setInt(3, cs.getObjectId());
						ps.setString(4, cs.getSayType().toString());
						ps.setString(5, cs.getName());
						ps.setString(6, cs.getContent());
						ps.addBatch();
					}
					ps.executeBatch();
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to save petitions data.", e);
		}
	}
	
	public static PetitionManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PetitionManager INSTANCE = new PetitionManager();
	}
}