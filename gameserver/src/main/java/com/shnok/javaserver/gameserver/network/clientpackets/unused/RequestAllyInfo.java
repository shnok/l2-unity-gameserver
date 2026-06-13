package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.ClanInfo;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.AllianceInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestAllyInfo extends L2GameClientPacket
{
	@Override
	public void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		SystemMessage sm;
		
		final int allianceId = player.getAllyId();
		if (allianceId > 0)
		{
			final AllianceInfo ai = new AllianceInfo(allianceId);
			player.sendPacket(ai);
			
			// send for player
			sm = SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_INFO_HEAD);
			player.sendPacket(sm);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_NAME_S1);
			sm.addString(ai.getName());
			player.sendPacket(sm);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_LEADER_S2_OF_S1);
			sm.addString(ai.getClanName());
			sm.addString(ai.getLeaderName());
			player.sendPacket(sm);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.CONNECTION_S1_TOTAL_S2);
			sm.addNumber(ai.getOnline());
			sm.addNumber(ai.getTotal());
			player.sendPacket(sm);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_CLAN_TOTAL_S1);
			sm.addNumber(ai.getAllies().length);
			player.sendPacket(sm);
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFO_HEAD);
			for (final ClanInfo aci : ai.getAllies())
			{
				player.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFO_NAME_S1);
				sm.addString(aci.getClan().getName());
				player.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFO_LEADER_S1);
				sm.addString(aci.getClan().getLeaderName());
				player.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFO_LEVEL_S1);
				sm.addNumber(aci.getClan().getLevel());
				player.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.CONNECTION_S1_TOTAL_S2);
				sm.addNumber(aci.getOnline());
				sm.addNumber(aci.getTotal());
				player.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFO_SEPARATOR);
			}
			
			sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_INFO_FOOT);
			player.sendPacket(sm);
		}
		else
			player.sendPacket(SystemMessageId.NO_CURRENT_ALLIANCES);
	}
}