package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.manager.ClanHallManager;
import net.sf.l2j.gameserver.enums.PrivilegeType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.residence.castle.Castle;
import net.sf.l2j.gameserver.model.residence.clanhall.SiegableHall;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SiegeInfo;

public final class RequestJoinSiege extends L2GameClientPacket
{
	private int _id;
	private int _isAttacker;
	private int _isJoining;
	
	@Override
	protected void readImpl()
	{
		_id = readD();
		_isAttacker = readD();
		_isJoining = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (!player.hasClanPrivileges(PrivilegeType.CP_MANAGE_SIEGE_WAR))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return;
		}
		
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		// Check Castle entity associated to the id.
		final Castle castle = CastleManager.getInstance().getCastleById(_id);
		if (castle != null)
		{
			if (_isJoining == 1)
			{
				if (System.currentTimeMillis() < clan.getDissolvingExpiryTime())
				{
					player.sendPacket(SystemMessageId.CANT_PARTICIPATE_IN_SIEGE_WHILE_DISSOLUTION_IN_PROGRESS);
					return;
				}
				
				if (_isAttacker == 1)
					castle.getSiege().registerAttacker(player);
				else
					castle.getSiege().registerDefender(player);
			}
			else
				castle.getSiege().unregisterClan(clan);
			
			player.sendPacket(new SiegeInfo(castle));
			return;
		}
		
		// Check SiegableHall entity associated to the id.
		final SiegableHall sh = ClanHallManager.getInstance().getSiegableHall(_id);
		if (sh != null)
		{
			if (_isJoining == 1)
			{
				if (System.currentTimeMillis() < clan.getDissolvingExpiryTime())
				{
					player.sendPacket(SystemMessageId.CANT_PARTICIPATE_IN_SIEGE_WHILE_DISSOLUTION_IN_PROGRESS);
					return;
				}
				
				sh.registerClan(clan, player);
			}
			else
				sh.unregisterClan(clan);
			
			player.sendPacket(new SiegeInfo(sh));
		}
	}
}