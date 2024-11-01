package com.shnok.javaserver.commons.mmocore;

import java.net.Socket;

public interface IAcceptFilter
{
	public boolean accept(Socket socket);
}