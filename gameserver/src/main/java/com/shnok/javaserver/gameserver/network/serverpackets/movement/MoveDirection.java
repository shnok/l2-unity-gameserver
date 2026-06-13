package com.shnok.javaserver.gameserver.network.serverpackets.movement;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class MoveDirection extends L2GameServerPacket
{
    private final int _objectId;
    private final Location _currentPosition;
    private final Location _direction;
    private int _verticalVelocity;
    private final Location _lastGamePosition;
    private final long _timestamp;

    public MoveDirection(Creature creature, Location direction, int verticalVelocity, long timestamp)
    {
        _objectId = creature.getObjectId();
        _currentPosition = creature.getPosition().clone();
        _direction = direction;
        _lastGamePosition  = _currentPosition;
        _timestamp =  timestamp;
        _verticalVelocity = verticalVelocity;
        //System.out.println("PlayerMoveDirection sharing _direction: " + _direction + " timestamp: " + _timestamp );

    }

    @Override
    protected final void writeImpl()
    {
        writeC(0xC6);
        writeD(_objectId);
        writeD(_direction.getY());
        writeD(_direction.getX());
        writeD(_verticalVelocity);
        writeLoc(_lastGamePosition);
        writeQ(_timestamp);
    }
}
