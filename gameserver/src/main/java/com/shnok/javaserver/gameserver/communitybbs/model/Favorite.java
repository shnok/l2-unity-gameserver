package com.shnok.javaserver.gameserver.communitybbs.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class Favorite
{
	private final int _id;
	private final int _playerId;
	
	private final String _title;
	private final String _bypass;
	
	private final Timestamp _date;
	
	public Favorite(ResultSet rs) throws SQLException
	{
		_id = rs.getInt("id");
		_playerId = rs.getInt("player_id");
		_title = rs.getString("title");
		_bypass = rs.getString("bypass");
		_date = rs.getTimestamp("date");
	}
	
	public Favorite(int id, int playerId, String title, String bypass, Timestamp date)
	{
		_id = id;
		_playerId = playerId;
		_title = title;
		_bypass = bypass;
		_date = date;
	}
	
	public int getId()
	{
		return _id;
	}
	
	public int getPlayerId()
	{
		return _playerId;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public String getBypass()
	{
		return _bypass;
	}
	
	public Timestamp getDate()
	{
		return _date;
	}
}