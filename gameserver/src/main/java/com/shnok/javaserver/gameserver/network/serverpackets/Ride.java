package com.shnok.javaserver.gameserver.network.serverpackets;

public class Ride extends L2GameServerPacket
{
	public static final int ACTION_MOUNT = 1;
	public static final int ACTION_DISMOUNT = 0;
	
	private final int _id;
	private final int _bRide;
	private int _rideType;
	private final int _rideClassID;
	
	public Ride(int id, int action, int rideClassId)
	{
		_id = id; // charobjectID
		_bRide = action; // 1 for mount ; 2 for dismount
		_rideClassID = rideClassId + 1000000; // npcID
		
		switch (rideClassId)
		{
			case 12526, 12527, 12528: // Striders
				_rideType = 1;
				break;
			
			case 12621: // Wyvern
				_rideType = 2;
				break;
		}
	}
	
	public int getMountType()
	{
		return _rideType;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x86);
		writeD(_id);
		writeD(_bRide);
		writeD(_rideType);
		writeD(_rideClassID);
	}
}