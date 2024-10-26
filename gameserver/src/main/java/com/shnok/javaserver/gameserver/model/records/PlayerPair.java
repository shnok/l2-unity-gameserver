package net.sf.l2j.gameserver.model.records;

public record PlayerPair(int id1, int id2)
{
	public PlayerPair
	{
		if (id1 > id2)
		{
			final int temp = id1;
			id1 = id2;
			id2 = temp;
		}
	}
	
	public boolean contains(int playerId)
	{
		return id1 == playerId || id2 == playerId;
	}
}