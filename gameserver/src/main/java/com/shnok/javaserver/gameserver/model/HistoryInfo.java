package com.shnok.javaserver.gameserver.model;

public class HistoryInfo
{
	private final int _raceId;
	private int _first;
	private int _second;
	private double _oddRate;
	
	public HistoryInfo(int raceId, int first, int second, double oddRate)
	{
		_raceId = raceId;
		_first = first;
		_second = second;
		_oddRate = oddRate;
	}
	
	public int getRaceId()
	{
		return _raceId;
	}
	
	public int getFirst()
	{
		return _first;
	}
	
	public int getSecond()
	{
		return _second;
	}
	
	public double getOddRate()
	{
		return _oddRate;
	}
	
	public void setFirst(int first)
	{
		_first = first;
	}
	
	public void setSecond(int second)
	{
		_second = second;
	}
	
	public void setOddRate(double oddRate)
	{
		_oddRate = oddRate;
	}
}