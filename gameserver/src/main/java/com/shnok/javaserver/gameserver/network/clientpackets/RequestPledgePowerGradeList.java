package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.network.serverpackets.PledgePowerGradeList;

public final class RequestPledgePowerGradeList extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
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
		
		// Feed array with count of members based on their power grade.
		final int[] membersPerRank = new int[10];
		for (ClanMember member : clan.getMembers())
			membersPerRank[member.getPowerGrade()]++;
		
		player.sendPacket(new PledgePowerGradeList(membersPerRank));
	}
}