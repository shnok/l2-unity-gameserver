package net.sf.l2j.gameserver.network;

public record SessionKey(int playOkId1, int playOkId2, int loginOkId1, int loginOkId2)
{
	@Override
	public String toString()
	{
		return "PlayOk: " + playOkId1 + " " + playOkId2 + " LoginOk:" + loginOkId1 + " " + loginOkId2;
	}
}