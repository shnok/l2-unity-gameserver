package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.actor.UserInfo;

public final class RequestEvaluate extends L2GameClientPacket
{
	private int _targetId;
	
	@Override
	protected void readImpl()
	{
		_targetId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player target = World.getInstance().getPlayer(_targetId);
		if (target == null)
		{
			player.sendPacket(SystemMessageId.SELECT_TARGET);
			return;
		}
		
		if (player.getTarget() != target)
			return;
		
		if (player.equals(target))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_RECOMMEND_YOURSELF);
			return;
		}
		
		if (player.getStatus().getLevel() < 10)
		{
			player.sendPacket(SystemMessageId.ONLY_LEVEL_SUP_10_CAN_RECOMMEND);
			return;
		}
		
		if (player.getRecomLeft() <= 0)
		{
			player.sendPacket(SystemMessageId.NO_MORE_RECOMMENDATIONS_TO_HAVE);
			return;
		}
		
		if (target.getRecomHave() >= 255)
		{
			player.sendPacket(SystemMessageId.YOUR_TARGET_NO_LONGER_RECEIVE_A_RECOMMENDATION);
			return;
		}
		
		if (!player.canRecom(target))
		{
			player.sendPacket(SystemMessageId.THAT_CHARACTER_IS_RECOMMENDED);
			return;
		}
		
		player.giveRecom(target);
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_RECOMMENDED_S1_YOU_HAVE_S2_RECOMMENDATIONS_LEFT).addCharName(target).addNumber(player.getRecomLeft()));
		player.sendPacket(new UserInfo(player));
		
		target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_BEEN_RECOMMENDED_BY_S1).addCharName(player));
		target.broadcastUserInfo();
	}
}