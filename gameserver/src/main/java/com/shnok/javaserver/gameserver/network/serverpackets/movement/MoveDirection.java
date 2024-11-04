package com.shnok.javaserver.gameserver.network.serverpackets.movement;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public final class MoveDirection extends L2GameServerPacket
{
    private final int _objectId;
    private final Location _currentPosition;
    private final Location _direction;

    public MoveDirection(Creature creature, Location direction)
    {
        _objectId = creature.getObjectId();
        _currentPosition = creature.getPosition().clone();
        _direction = direction;
    }

    @Override
    protected final void writeImpl()
    {
        writeC(0xC6);
        writeD(_objectId);
        writeD(_direction.getY());
        writeD(_direction.getX());
        writeLoc(_currentPosition);
    }
}
