package com.shnok.javaserver.gameserver.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionRate;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionState;
import com.shnok.javaserver.gameserver.enums.petitions.PetitionType;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.CreatureSay;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PetitionVote;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * A Petition is a report, generally made by a {@link Player} to a Game Master. The categories of report are multiple.
 */
public class Petition
{
	private final List<CreatureSay> _messages = new ArrayList<>();
	private final List<Integer> _responders = new ArrayList<>();
	
	private final int _id;
	private final PetitionType _type;
	private final int _petitionerObjectId;
	private final long _submitDate;
	private final String _content;
	
	private boolean _isUnread = true;
	private boolean _isUnderFeedback = false;
	
	private PetitionState _state = PetitionState.PENDING;
	private PetitionRate _rate = PetitionRate.FAIR;
	private String _feedback = "";
	
	public Petition(PetitionType type, int petitionerObjectId, String content)
	{
		_id = IdFactory.getInstance().getNextId();
		_type = type;
		_petitionerObjectId = petitionerObjectId;
		_submitDate = System.currentTimeMillis();
		_content = content;
	}
	
	public Petition(ResultSet rs) throws SQLException
	{
		_id = rs.getInt("oid");
		_type = Enum.valueOf(PetitionType.class, rs.getString("type"));
		_petitionerObjectId = rs.getInt("petitioner_oid");
		_submitDate = rs.getLong("submit_date");
		_content = rs.getString("content");
		_isUnread = rs.getInt("is_unread") != 0;
		_state = Enum.valueOf(PetitionState.class, rs.getString("state"));
		_rate = Enum.valueOf(PetitionRate.class, rs.getString("rate"));
		_feedback = rs.getString("feedback");
		
		final String responders = rs.getString("responders");
		if (!StringUtil.isEmpty(responders))
		{
			for (String string : responders.split(";"))
				_responders.add(Integer.parseInt(string));
		}
	}
	
	public List<CreatureSay> getMessages()
	{
		return _messages;
	}
	
	public boolean addMessage(CreatureSay cs)
	{
		return _messages.add(cs);
	}
	
	public List<Integer> getResponders()
	{
		return _responders;
	}
	
	public String getFormattedResponders()
	{
		final StringBuilder sb = new StringBuilder(_responders.size() * 20);
		for (int responderId : _responders)
		{
			final String playerName = PlayerInfoTable.getInstance().getPlayerName(responderId);
			if (playerName == null)
				continue;
			
			sb.append(playerName).append(" ");
		}
		return sb.toString();
	}
	
	public int getId()
	{
		return _id;
	}
	
	public PetitionType getType()
	{
		return _type;
	}
	
	public int getPetitionerObjectId()
	{
		return _petitionerObjectId;
	}
	
	public String getPetitionerName()
	{
		return PlayerInfoTable.getInstance().getPlayerName(_petitionerObjectId);
	}
	
	public long getSubmitDate()
	{
		return _submitDate;
	}
	
	public String getContent()
	{
		return _content;
	}
	
	public boolean isUnread()
	{
		return _isUnread;
	}
	
	public void setAsRead()
	{
		_isUnread = false;
	}
	
	public boolean isUnderFeedback()
	{
		return _isUnderFeedback;
	}
	
	public PetitionState getState()
	{
		return _state;
	}
	
	public void setState(PetitionState state)
	{
		_state = state;
	}
	
	public PetitionRate getRate()
	{
		return _rate;
	}
	
	public String getFeedback()
	{
		return _feedback;
	}
	
	public void setFeedback(PetitionRate rate, String feedback)
	{
		_isUnderFeedback = false;
		
		_rate = rate;
		_feedback = feedback;
	}
	
	/**
	 * Add the {@link Player} set as parameter to responders, but only if not null, not already registered as petitioner, or not already part of the responders.
	 * @param player : The {@link Player} to test.
	 * @return True if the operation was sucessful, or false otherwise.
	 */
	public boolean addResponder(Player player)
	{
		return player != null && player.getObjectId() != _petitionerObjectId && !_responders.contains(player.getObjectId()) && _responders.add(player.getObjectId());
	}
	
	public boolean addAdditionalResponder(Player player, Player targetPlayer)
	{
		if (!addResponder(targetPlayer))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_ADDING_S1_TO_PETITION).addCharName(targetPlayer));
			return false;
		}
		
		// Retrieve existing log, and call it for new responder.
		if (!_messages.isEmpty())
			showCompleteLog(targetPlayer);
		
		sendPetitionerPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PARTICIPATE_PETITION).addCharName(targetPlayer));
		sendResponderPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PARTICIPATE_PETITION).addCharName(targetPlayer));
		return true;
	}
	
	public void removeAdditionalResponder(Player player)
	{
		sendPetitionerPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LEFT_PETITION_CHAT).addCharName(player));
		sendResponderPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LEFT_PETITION_CHAT).addCharName(player));
		
		_responders.remove(Integer.valueOf(player.getObjectId()));
	}
	
	/**
	 * Join this {@link Petition}, which must be under PENDING state.<br>
	 * <br>
	 * If already under ACCEPTED state, then register the {@link Player} as additional responder and abort the basic behavior.
	 * @param player : The {@link Player} to add as responder.
	 * @param isEnforcing : If True, send messages related to //force_peti, otherwise send the classic messages.
	 * @return True if this {@link Petition} was successfully accepted, or false otherwise.
	 */
	public boolean join(Player player, boolean isEnforcing)
	{
		// The Petition is under ACCEPTED, simply add the Player as additional Responder.
		if (_state == PetitionState.ACCEPTED)
			return addAdditionalResponder(player, player);
		
		// The Petition isn't either ACCEPTED or PENDING, simply return.
		if (_state != PetitionState.PENDING)
			return false;
		
		// We couldn't properly add the responder, return.
		if (!addResponder(player))
			return false;
		
		setState(PetitionState.ACCEPTED);
		
		// Retrieve existing log, and call it for new responder.
		if (!_messages.isEmpty())
			showCompleteLog(player);
		else
		{
			if (isEnforcing)
			{
				sendPetitionerPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RECEIVED_CONSULTATION_REQUEST).addString(getPetitionerName()));
				sendResponderPacket(SystemMessage.getSystemMessage(SystemMessageId.PETITION_S1_RECEIVED_CODE_IS_S2).addCharName(player).addNumber(_id));
			}
			else
			{
				sendPetitionerPacket(SystemMessage.getSystemMessage(SystemMessageId.PETITION_APP_ACCEPTED));
				sendResponderPacket(SystemMessage.getSystemMessage(SystemMessageId.PETITION_WITH_S1_UNDER_WAY).addString(getPetitionerName()));
			}
		}
		return true;
	}
	
	/**
	 * Abort this {@link Petition} for the given {@link Player} ; if none valid responder is found, end the consultation, setting back the PetitionState to PENDING.
	 * @param player : The {@link Player} which moves out from this {@link Petition}.
	 */
	public void abortConsultation(Player player)
	{
		boolean wasLastRegisteredGm = true;
		for (int responderId : _responders)
		{
			if (responderId == player.getObjectId())
				continue;
			
			final Player responder = World.getInstance().getPlayer(responderId);
			if (responder == null || !responder.isOnline() || !responder.isGM())
				continue;
			
			wasLastRegisteredGm = false;
			break;
		}
		
		// No more responders, or no GM anymore in responders side, end the consultation.
		if (_responders.size() == 1 || wasLastRegisteredGm)
			endConsultation(PetitionState.PENDING);
		// Remove the Player from responders.
		else
			removeAdditionalResponder(player);
	}
	
	/**
	 * End this {@link Petition}, sending messages to responders and petitioner.<br>
	 * <br>
	 * Set aswell the {@link PetitionState} set as parameter.
	 * @param endState : The {@link PetitionState} to set for this {@link Petition}.
	 */
	public void endConsultation(PetitionState endState)
	{
		final Player petitioner = World.getInstance().getPlayer(_petitionerObjectId);
		final String petitionerName = (petitioner == null) ? getPetitionerName() : petitioner.getName();
		
		setState(endState);
		
		for (int responderId : _responders)
		{
			final Player responder = World.getInstance().getPlayer(responderId);
			if (responder == null || !responder.isOnline())
				continue;
			
			// Ending petition consultation with <Player>.
			responder.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PETITION_ENDED_WITH_S1).addString(petitionerName));
			
			// Receipt No. <ID> petition cancelled.
			if (endState == PetitionState.CANCELLED)
				responder.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RECEIPT_NO_S1_CANCELED).addNumber(getId()));
		}
		
		// End petition consultation and inform them, if they are still online.
		if (petitioner != null && petitioner.isOnline())
		{
			if (endState == PetitionState.PENDING)
				petitioner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_LEFT_PETITION_CHAT).addString("The GM"));
			else if (endState == PetitionState.CLOSED)
			{
				_isUnderFeedback = true;
				
				petitioner.sendPacket(SystemMessageId.THIS_END_THE_PETITION_PLEASE_PROVIDE_FEEDBACK);
				petitioner.sendPacket(PetitionVote.STATIC_PACKET);
			}
		}
		
		// Clean responders List if end state is different than CLOSED.
		if (endState != PetitionState.CLOSED)
			_responders.clear();
	}
	
	/**
	 * Send the {@link L2GameServerPacket} set as parameter to the petitioner.
	 * @param packet : The {@link L2GameServerPacket} to send to the petitioner.
	 */
	public void sendPetitionerPacket(L2GameServerPacket packet)
	{
		final Player petitioner = World.getInstance().getPlayer(_petitionerObjectId);
		if (petitioner == null || !petitioner.isOnline())
			return;
		
		petitioner.sendPacket(packet);
	}
	
	/**
	 * Send the {@link L2GameServerPacket} set as parameter to responders.<br>
	 * <br>
	 * If none responder is registered, end this Consultation and set it back to PENDING.
	 * @param packet : The {@link L2GameServerPacket} to broadcast to responders.
	 */
	public void sendResponderPacket(L2GameServerPacket packet)
	{
		if (_responders.isEmpty())
		{
			endConsultation(PetitionState.PENDING);
			return;
		}
		
		for (int responderId : _responders)
		{
			final Player responder = World.getInstance().getPlayer(responderId);
			if (responder == null || !responder.isOnline())
				return;
			
			responder.sendPacket(packet);
		}
	}
	
	public void showCompleteLog(Player player)
	{
		for (CreatureSay cs : _messages)
			player.sendPacket(cs);
	}
	
	/**
	 * Broadcast the {@link String} message on all participants of this {@link Petition}.
	 * @param player : The {@link Player} to test.
	 * @param message : The {@link String} to send.
	 */
	public void sendMessage(Player player, String message)
	{
		CreatureSay cs = null;
		
		if (getPetitionerObjectId() == player.getObjectId())
			cs = new CreatureSay(player, SayType.PETITION_PLAYER, message);
		else if (_responders.contains(player.getObjectId()))
			cs = new CreatureSay(player, (player.isGM()) ? SayType.PETITION_GM : SayType.PETITION_PLAYER, message);
		
		if (cs != null)
		{
			addMessage(cs);
			sendResponderPacket(cs);
			sendPetitionerPacket(cs);
		}
	}
}