package com.shnok.javaserver.commons.pool;

import java.sql.Connection;
import java.sql.SQLException;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.Config;

import org.mariadb.jdbc.MariaDbPoolDataSource;

public final class ConnectionPool
{
	private ConnectionPool()
	{
		throw new IllegalStateException("Utility class");
	}
	
	private static final CLogger LOGGER = new CLogger(ConnectionPool.class.getName());
	
	private static MariaDbPoolDataSource _source;
	
	public static void init()
	{
		try
		{
			_source = new MariaDbPoolDataSource();
			
			// Check if username is not empty because the source checks for null only.
			if (!Config.DATABASE_LOGIN.isEmpty())
			{
				_source.setUser(Config.DATABASE_LOGIN);
				_source.setPassword(Config.DATABASE_PASSWORD);
			}
			
			// Make sure the setUrl is called last as it initializes the pool.
			_source.setUrl(Config.DATABASE_URL);
		}
		catch (SQLException e)
		{
			LOGGER.error("Couldn't initialize connection pooler.", e);
		}
		LOGGER.info("Initializing ConnectionPool.");
	}
	
	public static void shutdown()
	{
		if (_source != null)
		{
			_source.close();
			_source = null;
		}
	}
	
	public static Connection getConnection() throws SQLException
	{
		return _source.getConnection();
	}
}