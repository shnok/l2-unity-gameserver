package com.shnok.javaserver.gameserver.network.clientpackets.combat;

import com.shnok.javaserver.gameserver.enums.duels.DuelState;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.TargetUnselected;

public final class RequestTarget extends L2GameClientPacket
{
    private int _objectId;
    private boolean _isShiftAction;

    @Override
    protected void readImpl()
    {
        _objectId = readD();
        _isShiftAction = readC() != 0;
    }

    @Override
    protected void runImpl()
    {
        final Player player = getClient().getPlayer();
        if (player == null)
            return;

        if (player.isInObserverMode())
        {
            player.sendPacket(SystemMessageId.OBSERVERS_CANNOT_PARTICIPATE);
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        if (player.getActiveRequester() != null)
        {
            player.sendPacket(ActionFailed.STATIC_PACKET);
            player.sendPacket(new TargetUnselected(player));
            return;
        }

        final WorldObject target = (player.getTargetId() == _objectId) ? player.getTarget() : World.getInstance().getObject(_objectId);
        if (target == null)
        {
            player.sendPacket(ActionFailed.STATIC_PACKET);
            player.sendPacket(new TargetUnselected(player));
            return;
        }

        final Player targetPlayer = target.getActingPlayer();
        if (targetPlayer != null && targetPlayer.getDuelState() == DuelState.DEAD)
        {
            player.sendPacket(SystemMessageId.OTHER_PARTY_IS_FROZEN);
            player.sendPacket(ActionFailed.STATIC_PACKET);
            return;
        }

        target.onTarget(player, _isShiftAction);
    }

    @Override
    protected boolean triggersOnActionRequest()
    {
        return false;
    }
}