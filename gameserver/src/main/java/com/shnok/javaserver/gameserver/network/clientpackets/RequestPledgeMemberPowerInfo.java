package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.network.serverpackets.PledgeReceivePowerInfo;

public final class RequestPledgeMemberPowerInfo extends L2GameClientPacket
{
	private String _player;
	
	@Override
	protected void readImpl()
	{
		readD(); // Not used for security reason. Pledge type.
		_player = readS();
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
		
		final ClanMember member = clan.getClanMember(_player);
		if (member == null)
			return;
		
		player.sendPacket(new PledgeReceivePowerInfo(member));
	}
}