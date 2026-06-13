package com.shnok.javaserver.commons.mmocore;

public interface IMMOExecutor<T extends MMOClient<?>>
{
	public void execute(ReceivablePacket<T> packet);
}