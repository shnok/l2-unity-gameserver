package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMHennaInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMViewCharacterInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMViewItemList;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMViewPledgeInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMViewQuestList;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMViewSkillInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.GMViewWarehouseWithdrawList;

public final class RequestGMCommand extends L2GameClientPacket
{
	private String _targetName;
	private int _command;
	
	@Override
	protected void readImpl()
	{
		_targetName = readS();
		_command = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		// prevent non gm or low level GMs from viewing player stuff
		if (!player.isGM() || !player.getAccessLevel().allowAltG())
			return;
		
		final Player target = World.getInstance().getPlayer(_targetName);
		final Clan clan = ClanTable.getInstance().getClanByName(_targetName);
		
		if (target == null && (clan == null || _command != 6))
			return;
		
		switch (_command)
		{
			case 1: // target status
				sendPacket(new GMViewCharacterInfo(target));
				sendPacket(new GMHennaInfo(target));
				break;
			
			case 2: // target clan
				if (target != null && target.getClan() != null)
					sendPacket(new GMViewPledgeInfo(target.getClan(), target));
				break;
			
			case 3: // target skills
				sendPacket(new GMViewSkillInfo(target));
				break;
			
			case 4: // target quests
				sendPacket(new GMViewQuestList(target));
				break;
			
			case 5: // target inventory
				sendPacket(new GMViewItemList(target));
				sendPacket(new GMHennaInfo(target));
				break;
			
			case 6: // player or clan warehouse
				if (target != null)
					sendPacket(new GMViewWarehouseWithdrawList(target));
				else
					sendPacket(new GMViewWarehouseWithdrawList(clan));
				break;
		}
	}
}