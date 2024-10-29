package com.shnok.javaserver.gameserver.network.clientpackets.movement;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;

public class PlayerMoveDirection extends L2GameClientPacket
{
    private double _moveDirectionX;
    private double _moveDirectionY;
    private double _moveDirectionZ;
    @SuppressWarnings("unused")
    private int _heading;

    @Override
    protected void readImpl()
    {
        _moveDirectionY = readF();
        _moveDirectionZ = 0;
        _moveDirectionX = readF();
        _heading = readD();
    }

    @Override
    protected void runImpl()
    {
        final Player player = getClient().getPlayer();
        if (player == null)
            return;

        // If Player can't be controlled, forget it.
        if (player.isOutOfControl())
        {
            player.sendPacket(ActionFailed.STATIC_PACKET); //validatelocation?
            return;
        }

        // If Player can't move, forget it.
        if (player.getStatus().getMoveSpeed() == 0)
        {
            player.sendPacket(ActionFailed.STATIC_PACKET); //validatelocation?
            player.sendPacket(SystemMessageId.CANT_MOVE_TOO_ENCUMBERED); //validatelocation?
            return;
        }

        // Cancel enchant over movement.
        player.cancelActiveEnchant();

        // Generate a Location based on target coords.
        final Location moveDirection = new Location((int)(_moveDirectionX * 100), (int)(_moveDirectionY * 100), (int)(_moveDirectionZ * 100));

        player.getAI().tryToMoveTo(moveDirection, null);
    }
}