package com.shnok.javaserver.gameserver.network.clientpackets.auth;

import com.shnok.javaserver.gameserver.enums.FloodProtector;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.auth.CharSelectInfo;

public final class CharacterRestore extends L2GameClientPacket
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
			return;
		
		getClient().markRestoredChar(_slot);
		
		final CharSelectInfo csi = new CharSelectInfo(getClient().getAccountName(), getClient().getSessionId().playOkId1(), 0);
		sendPacket(csi);
		getClient().setCharSelectSlot(csi.getCharacterSlots());
	}
}
