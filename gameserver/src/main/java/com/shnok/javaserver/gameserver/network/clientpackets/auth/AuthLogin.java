package com.shnok.javaserver.gameserver.network.clientpackets.auth;

import com.shnok.javaserver.gameserver.LoginServerThread;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;

public final class AuthLogin extends L2GameClientPacket
{
	private String _loginName;
	private int _playKey1;
	private int _playKey2;
	private int _loginKey1;
	private int _loginKey2;
	
	@Override
	protected void readImpl()
	{
		_loginName = readS().toLowerCase();
		_playKey2 = readD();
		_playKey1 = readD();
		_loginKey1 = readD();
		_loginKey2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		LoginServerThread.getInstance().addClient(_loginName, _loginKey1, _loginKey2, _playKey1, _playKey2, getClient());
	}
}