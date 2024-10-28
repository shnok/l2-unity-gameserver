package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.sql.ServerMemoTable;
import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * The championship tournament is held on a weekly cycle. During the competition, players vie to catch the biggest fish. The results are ranked based on the size of the fish they caught, with the biggest fish determining the winner.<br>
 * <br>
 * The top five fishers of the first week's tournament will be determined on Tuesday. At that time, a new tournament begins for the second week of the event. Winners for that week will be determined on the following Tuesday. The tournament will continue to run weekly with winners determined every
 * Tuesday.<br>
 * <br>
 * The current status of the tournament can be viewed by clicking the Contest button in the Fishing window. The current results can also be viewed by speaking with a Fisherman's Guild Member after the results of the first week have been calculated.<br>
 * <br>
 * <b>Catching the Big One</b><br>
 * <br>
 * A big fish is measured by length only. Fish between 60.000 and 90.000 can be caught with any normal type of fishing lure, including Lure for Beginners. A Prize-Winning Fishing Lure increases your chances of catching an even larger fish.<br>
 * <br>
 * If you catch a fish worthy of ranking in the tournament, a system message displays to let you know that you have been registered in the current ranking.<br>
 * <br>
 * When more than one competitor has a winning fish that are all the same in size, the person who caught the first fish wins.<br>
 * <br>
 * <b>Claiming the Prize</b><br>
 * <br>
 * Championship prizes are awarded by a Fisherman's Guild Member, and you only have one week to claim your prize. The Fisherman's Guild Member award a weekly prize of Adena based on ranking:
 * <ul>
 * <li>1st Place: 800,000 Adena</li>
 * <li>2nd Place: 500,000 Adena</li>
 * <li>3rd Place: 300,000 Adena</li>
 * <li>4th Place: 200,000 Adena</li>
 * <li>5th Place: 100,000 Adena</li>
 * </ul>
 */
public class FishingChampionshipManager
{
	private static final CLogger LOGGER = new CLogger(FishingChampionshipManager.class.getName());
	
	private static final String INSERT = "INSERT INTO fishing_championship(player_name,fish_length,rewarded) VALUES (?,?,?)";
	private static final String TRUNCATE = "TRUNCATE fishing_championship";
	private static final String SELECT = "SELECT `player_name`, `fish_length`, `rewarded` FROM fishing_championship";
	
	private final List<String> _playersName = new ArrayList<>();
	private final List<String> _fishLength = new ArrayList<>();
	private final List<String> _winPlayersName = new ArrayList<>();
	private final List<String> _winFishLength = new ArrayList<>();
	private final List<Fisher> _tmpPlayers = new ArrayList<>();
	private final List<Fisher> _winPlayers = new ArrayList<>();
	
	private long _endDate = 0;
	private double _minFishLength = 0;
	private boolean _needRefresh = true;
	
	protected FishingChampionshipManager()
	{
		restoreData();
		refreshWinResult();
		recalculateMinLength();
		
		if (_endDate <= System.currentTimeMillis())
		{
			_endDate = System.currentTimeMillis();
			finishChamp();
		}
		else
			ThreadPool.schedule(this::finishChamp, _endDate - System.currentTimeMillis());
	}
	
	private void setEndOfChamp()
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(_endDate);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.DAY_OF_MONTH, 6);
		cal.set(Calendar.DAY_OF_WEEK, 3);
		cal.set(Calendar.HOUR_OF_DAY, 19);
		
		_endDate = cal.getTimeInMillis();
	}
	
	private void restoreData()
	{
		_endDate = ServerMemoTable.getInstance().getLong("fishChampionshipEnd", 0);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT))
		{
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int rewarded = rs.getInt("rewarded");
					if (rewarded == 0)
						_tmpPlayers.add(new Fisher(rs.getString("player_name"), rs.getDouble("fish_length"), 0));
					else if (rewarded > 0)
						_winPlayers.add(new Fisher(rs.getString("player_name"), rs.getDouble("fish_length"), rewarded));
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore fishing championship data.", e);
		}
	}
	
	private synchronized void refreshResult()
	{
		_needRefresh = false;
		
		_playersName.clear();
		_fishLength.clear();
		
		Fisher fisher1;
		Fisher fisher2;
		
		for (int x = 0; x <= _tmpPlayers.size() - 1; x++)
		{
			for (int y = 0; y <= _tmpPlayers.size() - 2; y++)
			{
				fisher1 = _tmpPlayers.get(y);
				fisher2 = _tmpPlayers.get(y + 1);
				if (fisher1.getLength() < fisher2.getLength())
				{
					_tmpPlayers.set(y, fisher2);
					_tmpPlayers.set(y + 1, fisher1);
				}
			}
		}
		
		for (int x = 0; x <= _tmpPlayers.size() - 1; x++)
		{
			_playersName.add(_tmpPlayers.get(x).getName());
			_fishLength.add(String.valueOf(_tmpPlayers.get(x).getLength()));
		}
	}
	
	private void refreshWinResult()
	{
		_winPlayersName.clear();
		_winFishLength.clear();
		
		Fisher fisher1;
		Fisher fisher2;
		
		for (int x = 0; x <= _winPlayers.size() - 1; x++)
		{
			for (int y = 0; y <= _winPlayers.size() - 2; y++)
			{
				fisher1 = _winPlayers.get(y);
				fisher2 = _winPlayers.get(y + 1);
				if (fisher1.getLength() < fisher2.getLength())
				{
					_winPlayers.set(y, fisher2);
					_winPlayers.set(y + 1, fisher1);
				}
			}
		}
		
		for (int x = 0; x <= _winPlayers.size() - 1; x++)
		{
			_winPlayersName.add(_winPlayers.get(x).getName());
			_winFishLength.add(String.valueOf(_winPlayers.get(x).getLength()));
		}
	}
	
	private void finishChamp()
	{
		_winPlayers.clear();
		for (Fisher fisher : _tmpPlayers)
		{
			fisher.setRewardType(1);
			_winPlayers.add(fisher);
		}
		_tmpPlayers.clear();
		
		refreshWinResult();
		setEndOfChamp();
		shutdown();
		
		LOGGER.info("A new Fishing Championship event period has started.");
		ThreadPool.schedule(this::finishChamp, _endDate - System.currentTimeMillis());
	}
	
	private void recalculateMinLength()
	{
		double minLen = 99999.;
		for (Fisher fisher : _tmpPlayers)
		{
			if (fisher.getLength() < minLen)
				minLen = fisher.getLength();
		}
		_minFishLength = minLen;
	}
	
	public synchronized void newFish(Player player, int lureId)
	{
		if (!Config.ALLOW_FISH_CHAMPIONSHIP)
			return;
		
		double len = Rnd.get(60, 89) + (Rnd.get(0, 1000) / 1000.);
		if (lureId >= 8484 && lureId <= 8486)
			len += Rnd.get(0, 3000) / 1000.;
		
		player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CAUGHT_FISH_S1_LENGTH).addString(String.valueOf(len)));
		
		if (_tmpPlayers.size() < 5)
		{
			for (Fisher fisher : _tmpPlayers)
			{
				if (fisher.getName().equalsIgnoreCase(player.getName()))
				{
					if (fisher.getLength() < len)
					{
						fisher.setLength(len);
						player.sendPacket(SystemMessageId.REGISTERED_IN_FISH_SIZE_RANKING);
						recalculateMinLength();
					}
					return;
				}
			}
			_tmpPlayers.add(new Fisher(player.getName(), len, 0));
			player.sendPacket(SystemMessageId.REGISTERED_IN_FISH_SIZE_RANKING);
			recalculateMinLength();
		}
		else if (_minFishLength < len)
		{
			for (Fisher fisher : _tmpPlayers)
			{
				if (fisher.getName().equalsIgnoreCase(player.getName()))
				{
					if (fisher.getLength() < len)
					{
						fisher.setLength(len);
						player.sendPacket(SystemMessageId.REGISTERED_IN_FISH_SIZE_RANKING);
						recalculateMinLength();
					}
					return;
				}
			}
			
			Fisher minFisher = null;
			double minLen = 99999.;
			for (Fisher fisher : _tmpPlayers)
			{
				if (fisher.getLength() < minLen)
				{
					minFisher = fisher;
					minLen = minFisher.getLength();
				}
			}
			_tmpPlayers.remove(minFisher);
			_tmpPlayers.add(new Fisher(player.getName(), len, 0));
			player.sendPacket(SystemMessageId.REGISTERED_IN_FISH_SIZE_RANKING);
			recalculateMinLength();
		}
	}
	
	public long getTimeRemaining()
	{
		return (_endDate - System.currentTimeMillis()) / 60000;
	}
	
	public String getWinnerName(int par)
	{
		if (_winPlayersName.size() >= par)
			return _winPlayersName.get(par - 1);
		
		return "None";
	}
	
	public String getCurrentName(int par)
	{
		if (_playersName.size() >= par)
			return _playersName.get(par - 1);
		
		return "None";
	}
	
	public String getFishLength(int par)
	{
		if (_winFishLength.size() >= par)
			return _winFishLength.get(par - 1);
		
		return "0";
	}
	
	public String getCurrentFishLength(int par)
	{
		if (_fishLength.size() >= par)
			return _fishLength.get(par - 1);
		
		return "0";
	}
	
	public boolean isWinner(String playerName)
	{
		for (String name : _winPlayersName)
		{
			if (name.equals(playerName))
				return true;
		}
		return false;
	}
	
	public void getReward(Player player)
	{
		for (Fisher fisher : _winPlayers)
		{
			if (fisher.getRewardType() != 2 && fisher.getName().equalsIgnoreCase(player.getName()))
			{
				int rewardCnt = 0;
				for (int x = 0; x < _winPlayersName.size(); x++)
				{
					if (_winPlayersName.get(x).equalsIgnoreCase(player.getName()))
					{
						switch (x)
						{
							case 0:
								rewardCnt = Config.FISH_CHAMPIONSHIP_REWARD_1;
								break;
							
							case 1:
								rewardCnt = Config.FISH_CHAMPIONSHIP_REWARD_2;
								break;
							
							case 2:
								rewardCnt = Config.FISH_CHAMPIONSHIP_REWARD_3;
								break;
							
							case 3:
								rewardCnt = Config.FISH_CHAMPIONSHIP_REWARD_4;
								break;
							
							case 4:
								rewardCnt = Config.FISH_CHAMPIONSHIP_REWARD_5;
								break;
						}
					}
				}
				
				fisher.setRewardType(2);
				
				if (rewardCnt > 0)
				{
					player.addItem(Config.FISH_CHAMPIONSHIP_REWARD_ITEM, rewardCnt, true);
					
					final NpcHtmlMessage html = new NpcHtmlMessage(0);
					html.setFile("data/html/fisherman/championship/fish_event_reward001.htm");
					player.sendPacket(html);
				}
			}
		}
	}
	
	public void showMidResult(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		
		if (_needRefresh)
		{
			html.setFile("data/html/fisherman/championship/fish_event003.htm");
			player.sendPacket(html);
			
			refreshResult();
			ThreadPool.schedule(() -> _needRefresh = true, 60000);
			return;
		}
		
		html.setFile("data/html/fisherman/championship/fish_event002.htm");
		
		final StringBuilder sb = new StringBuilder(100);
		for (int x = 1; x <= 5; x++)
		{
			StringUtil.append(sb, "<tr><td width=70 align=center>", x, "</td>");
			StringUtil.append(sb, "<td width=110 align=center>", getCurrentName(x), "</td>");
			StringUtil.append(sb, "<td width=80 align=center>", getCurrentFishLength(x), "</td></tr>");
		}
		html.replace("%TABLE%", sb.toString());
		html.replace("%prizeItem%", ItemData.getInstance().getTemplate(Config.FISH_CHAMPIONSHIP_REWARD_ITEM).getName());
		html.replace("%prizeFirst%", Config.FISH_CHAMPIONSHIP_REWARD_1);
		html.replace("%prizeTwo%", Config.FISH_CHAMPIONSHIP_REWARD_2);
		html.replace("%prizeThree%", Config.FISH_CHAMPIONSHIP_REWARD_3);
		html.replace("%prizeFour%", Config.FISH_CHAMPIONSHIP_REWARD_4);
		html.replace("%prizeFive%", Config.FISH_CHAMPIONSHIP_REWARD_5);
		player.sendPacket(html);
	}
	
	public void showChampScreen(Player player, int objectId)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(objectId);
		html.setFile("data/html/fisherman/championship/fish_event001.htm");
		
		final StringBuilder sb = new StringBuilder(100);
		for (int x = 1; x <= 5; x++)
		{
			StringUtil.append(sb, "<tr><td width=70 align=center>", x, "</td>");
			StringUtil.append(sb, "<td width=110 align=center>", getWinnerName(x), "</td>");
			StringUtil.append(sb, "<td width=80 align=center>", getFishLength(x), "</td></tr>");
		}
		html.replace("%TABLE%", sb.toString());
		html.replace("%prizeItem%", ItemData.getInstance().getTemplate(Config.FISH_CHAMPIONSHIP_REWARD_ITEM).getName());
		html.replace("%prizeFirst%", Config.FISH_CHAMPIONSHIP_REWARD_1);
		html.replace("%prizeTwo%", Config.FISH_CHAMPIONSHIP_REWARD_2);
		html.replace("%prizeThree%", Config.FISH_CHAMPIONSHIP_REWARD_3);
		html.replace("%prizeFour%", Config.FISH_CHAMPIONSHIP_REWARD_4);
		html.replace("%prizeFive%", Config.FISH_CHAMPIONSHIP_REWARD_5);
		html.replace("%refresh%", getTimeRemaining());
		html.replace("%objectId%", objectId);
		player.sendPacket(html);
	}
	
	public void shutdown()
	{
		ServerMemoTable.getInstance().set("fishChampionshipEnd", _endDate);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(TRUNCATE);
			PreparedStatement ps2 = con.prepareStatement(INSERT))
		{
			ps.execute();
			
			for (Fisher fisher : _winPlayers)
			{
				ps2.setString(1, fisher.getName());
				ps2.setDouble(2, fisher.getLength());
				ps2.setInt(3, fisher.getRewardType());
				ps2.addBatch();
			}
			
			for (Fisher fisher : _tmpPlayers)
			{
				ps2.setString(1, fisher.getName());
				ps2.setDouble(2, fisher.getLength());
				ps2.setInt(3, 0);
				ps2.addBatch();
			}
			ps2.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update fishing championship data.", e);
		}
	}
	
	private class Fisher
	{
		private double _length;
		private final String _name;
		private int _reward;
		
		public Fisher(String name, double length, int rewardType)
		{
			_name = name;
			_length = length;
			_reward = rewardType;
		}
		
		public void setLength(double value)
		{
			_length = value;
		}
		
		public void setRewardType(int value)
		{
			_reward = value;
		}
		
		public String getName()
		{
			return _name;
		}
		
		public int getRewardType()
		{
			return _reward;
		}
		
		public double getLength()
		{
			return _length;
		}
	}
	
	public static final FishingChampionshipManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final FishingChampionshipManager INSTANCE = new FishingChampionshipManager();
	}
}