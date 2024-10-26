package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.AskJoinPledge;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestJoinPledge extends L2GameClientPacket
{
	private int _targetId;
	private int _pledgeType;
	
	@Override
	protected void readImpl()
	{
		_targetId = readD();
		_pledgeType = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		final Player target = World.getInstance().getPlayer(_targetId);
		if (target == null)
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
		if (!clan.checkClanJoinCondition(player, target, _pledgeType))
			return;
		
		if (!player.getRequest().setRequest(target, this))
			return;
		
		target.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_INVITED_YOU_TO_JOIN_THE_CLAN_S2).addCharName(player).addString(clan.getName()));
		target.sendPacket(new AskJoinPledge(player.getObjectId(), clan.getName()));
	}
	
	public int getPledgeType()
	{
		return _pledgeType;
	}
}