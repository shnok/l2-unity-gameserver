package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.serverpackets.ExMPCCShowPartyMemberInfo;

public final class RequestExMPCCShowPartyMembersInfo extends L2GameClientPacket
{
	private int _partyLeaderId;
	
	@Override
	protected void readImpl()
	{
		_partyLeaderId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Player leader = World.getInstance().getPlayer(_partyLeaderId);
		if (leader != null && leader.isInParty())
			player.sendPacket(new ExMPCCShowPartyMemberInfo(leader.getParty()));
	}
}