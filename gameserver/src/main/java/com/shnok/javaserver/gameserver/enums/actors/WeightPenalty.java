package com.shnok.javaserver.gameserver.enums.actors;

public enum WeightPenalty
{
	NONE(1, 1),
	LEVEL_1(1, 0.5),
	LEVEL_2(0.5, 0.5),
	LEVEL_3(0.5, 0.5),
	LEVEL_4(0, 0.1);
	
	private double _speedMultiplier;
	private double _regenerationMultiplier;
	
	private WeightPenalty(double speedMultiplier, double regenerationMultiplier)
	{
		_speedMultiplier = speedMultiplier;
		_regenerationMultiplier = regenerationMultiplier;
	}
	
	public double getSpeedMultiplier()
	{
		return _speedMultiplier;
	}
	
	public double getRegenerationMultiplier()
	{
		return _regenerationMultiplier;
	}
}