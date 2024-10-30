package com.shnok.javaserver.gameserver.network.clientpackets.combat;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;

public final class AttackRequest extends L2GameClientPacket
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
		
		if (player.isOutOfControl())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInObserverMode())
		{
			player.sendPacket(SystemMessageId.OBSERVERS_CANNOT_PARTICIPATE);
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// avoid using expensive operations if not needed
		final WorldObject target;
		if (player.getTargetId() == _objectId)
			target = player.getTarget();
		else
			target = World.getInstance().getObject(_objectId);
		
		if (target == null)
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		// (player.getTarget() == target) -> This happens when you control + click a target without having had it selected beforehand. Behaves as the Action packet and will NOT trigger an attack.
		target.onAction(player, (player.getTarget() == target), _isShiftAction);
	}
}