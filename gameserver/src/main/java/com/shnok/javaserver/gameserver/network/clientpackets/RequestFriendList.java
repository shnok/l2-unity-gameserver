package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.manager.RelationManager;
import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public final class RequestFriendList extends L2GameClientPacket
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
		
		// ======<Friend List>======
		player.sendPacket(SystemMessageId.FRIEND_LIST_HEADER);
		
		for (int id : RelationManager.getInstance().getFriendList(player.getObjectId()))
		{
			final String friendName = PlayerInfoTable.getInstance().getPlayerName(id);
			if (friendName == null)
				continue;
			
			final Player friend = World.getInstance().getPlayer(id);
			
			player.sendPacket(SystemMessage.getSystemMessage((friend == null || !friend.isOnline()) ? SystemMessageId.S1_OFFLINE : SystemMessageId.S1_ONLINE).addString(friendName));
		}
		
		// =========================
		player.sendPacket(SystemMessageId.FRIEND_LIST_FOOTER);
	}
}