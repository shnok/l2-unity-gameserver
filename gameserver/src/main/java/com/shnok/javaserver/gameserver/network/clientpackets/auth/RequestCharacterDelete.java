package com.shnok.javaserver.gameserver.network.clientpackets.auth;

import com.shnok.javaserver.gameserver.enums.FloodProtector;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.auth.CharDeleteFail;
import com.shnok.javaserver.gameserver.network.serverpackets.auth.CharDeleteOk;
import com.shnok.javaserver.gameserver.network.serverpackets.auth.CharSelectInfo;

public final class RequestCharacterDelete extends L2GameClientPacket
{
	private int _slot;
	
	@Override
	protected void readImpl()
	{
		_slot = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (!getClient().performAction(FloodProtector.CHARACTER_SELECT))
		{
			sendPacket(CharDeleteFail.REASON_DELETION_FAILED);
			return;
		}
		
		switch (getClient().markToDeleteChar(_slot))
		{
			default:
			case -1: // Error
				break;
			
			case 0: // Success!
				sendPacket(CharDeleteOk.STATIC_PACKET);
				break;
			
			case 1:
				sendPacket(CharDeleteFail.REASON_YOU_MAY_NOT_DELETE_CLAN_MEMBER);
				break;
			
			case 2:
				sendPacket(CharDeleteFail.REASON_CLAN_LEADERS_MAY_NOT_BE_DELETED);
				break;
		}
		
		final CharSelectInfo csi = new CharSelectInfo(getClient().getAccountName(), getClient().getSessionId().playOkId1(), 0);
		sendPacket(csi);
		getClient().setCharSelectSlot(csi.getCharacterSlots());
	}
}
