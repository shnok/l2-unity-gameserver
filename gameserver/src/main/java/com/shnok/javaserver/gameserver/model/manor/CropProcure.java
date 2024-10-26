package com.shnok.javaserver.gameserver.model.manor;

public final class CropProcure extends SeedProduction
{
	private final int _rewardType;
	
	public CropProcure(int id, int amount, int type, int startAmount, int price)
	{
		super(id, amount, price, startAmount);
		
		_rewardType = type;
	}
	
	public final int getReward()
	{
		return _rewardType;
	}
}