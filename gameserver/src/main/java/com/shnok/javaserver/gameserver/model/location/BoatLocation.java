package com.shnok.javaserver.gameserver.model.location;

import java.util.stream.Stream;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.BoatSay;

/**
 * A datatype extending {@link Location} used for boats. It notably holds move speed and rotation speed.
 */
public class BoatLocation extends Location
{
	private int _moveSpeed;
	private int _rotationSpeed;
	
	private BoatSay _busyMessage;
	private BoatSay[] _arrivalMessages;
	private BoatSay[] _departureMessages;
	
	private IntIntHolder[] _scheduledMessages;
	
	public BoatLocation(int x, int y, int z, int moveSpeed, int rotationSpeed)
	{
		super(x, y, z);
		
		_moveSpeed = moveSpeed;
		_rotationSpeed = rotationSpeed;
	}
	
	public BoatLocation(StatSet set)
	{
		super(set);
		
		_moveSpeed = set.getInteger("speed", 350);
		_rotationSpeed = set.getInteger("rotation", 4000);
		
		final int busy = set.getInteger("busy", 0);
		if (busy != 0)
			_busyMessage = new BoatSay(SystemMessageId.getSystemMessageId(busy));
		
		_arrivalMessages = getBoatSayArray(set.getStringArray("arrival", null));
		_departureMessages = getBoatSayArray(set.getStringArray("departure", null));
		
		_scheduledMessages = set.getIntIntHolderArray("scheduled", null);
	}
	
	private static BoatSay[] getBoatSayArray(String[] messages)
	{
		if (messages == null)
			return null;
		
		return Stream.of(messages).map(message -> new BoatSay(SystemMessageId.getSystemMessageId(Integer.parseInt(message)))).toArray(BoatSay[]::new);
	}
	
	public int getMoveSpeed()
	{
		return _moveSpeed;
	}
	
	public int getRotationSpeed()
	{
		return _rotationSpeed;
	}
	
	public BoatSay getBusyMessage()
	{
		return _busyMessage;
	}
	
	public BoatSay[] getArrivalMessages()
	{
		return _arrivalMessages;
	}
	
	public BoatSay[] getDepartureMessages()
	{
		return _departureMessages;
	}
	
	public IntIntHolder[] getScheduledMessages()
	{
		return _scheduledMessages;
	}
}