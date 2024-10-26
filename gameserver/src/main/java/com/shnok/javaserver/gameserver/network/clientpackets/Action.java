package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.enums.duels.DuelState;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;

public final class Action extends L2GameClientPacket
{
	private int _objectId;
	private boolean _isShiftAction;
	
	@Override
	protected void readImpl()
	{
		_objectId = readD();
		readD(); // originX
		readD(); // originY
		readD(); // originZ
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
			return;
		}
		
		final WorldObject target = (player.getTargetId() == _objectId) ? player.getTarget() : World.getInstance().getObject(_objectId);
		if (target == null)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		final Player targetPlayer = target.getActingPlayer();
		if (targetPlayer != null && targetPlayer.getDuelState() == DuelState.DEAD)
		{
			player.sendPacket(SystemMessageId.OTHER_PARTY_IS_FROZEN);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		target.onAction(player, false, _isShiftAction);
	}
	
	@Override
	protected boolean triggersOnActionRequest()
	{
		return false;
	}
}