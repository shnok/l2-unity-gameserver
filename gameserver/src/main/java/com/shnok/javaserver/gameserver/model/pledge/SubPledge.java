package com.shnok.javaserver.gameserver.model.pledge;

public class SubPledge
{
	private final int _id;
	private String _subPledgeName;
	private int _leaderId;
	
	public SubPledge(int id, String name, int leaderId)
	{
		_id = id;
		_subPledgeName = name;
		_leaderId = leaderId;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _subPledgeName;
	}
	
	public void setName(String name)
	{
		_subPledgeName = name;
	}
	
	public int getLeaderId()
	{
		return _leaderId;
	}
	
	public void setLeaderId(int leaderId)
	{
		_leaderId = leaderId;
	}
}