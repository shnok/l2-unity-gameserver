package com.shnok.javaserver.gameserver.communitybbs.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

public class Post
{
	private static final CLogger LOGGER = new CLogger(Post.class.getName());
	
	private static final String UPDATE_TEXT = "UPDATE bbs_post SET txt=? WHERE id=? AND topic_id=? AND forum_id=?";
	
	private final int _id;
	private final String _owner;
	private final int _ownerId;
	private final long _date;
	private final int _topicId;
	private final int _forumId;
	
	private String _text;
	
	public Post(int id, String owner, int ownerId, long date, int topicId, int forumId, String text)
	{
		_id = id;
		_owner = owner;
		_ownerId = ownerId;
		_date = date;
		_topicId = topicId;
		_forumId = forumId;
		_text = text;
	}
	
	public Post(ResultSet rs) throws SQLException
	{
		_id = rs.getInt("id");
		_owner = rs.getString("owner_name");
		_ownerId = rs.getInt("owner_id");
		_date = rs.getLong("date");
		_topicId = rs.getInt("topic_id");
		_forumId = rs.getInt("forum_id");
		_text = rs.getString("txt");
	}
	
	public int getId()
	{
		return _id;
	}
	
	public String getOwner()
	{
		return _owner;
	}
	
	public int getOwnerId()
	{
		return _ownerId;
	}
	
	public long getDate()
	{
		return _date;
	}
	
	public int getTopicId()
	{
		return _topicId;
	}
	
	public int getForumId()
	{
		return _forumId;
	}
	
	public String getText()
	{
		return _text;
	}
	
	public void setText(String text)
	{
		_text = text;
	}
	
	public void updateText(int index, String text)
	{
		_text = text;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_TEXT))
		{
			ps.setString(1, _text);
			ps.setInt(2, _id);
			ps.setInt(3, _topicId);
			ps.setInt(4, _forumId);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update Post text.", e);
		}
	}
}