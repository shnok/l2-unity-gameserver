package com.shnok.javaserver.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * Handles the Lottery event, where player can buy tickets to gamble money.
 */
public class LotteryManager
{
	private static final CLogger LOGGER = new CLogger(LotteryManager.class.getName());
	
	public static final long SECOND = 1000;
	public static final long MINUTE = 60000;
	
	private static final String INSERT_LOTTERY = "INSERT INTO games(id, idnr, enddate, prize, newprize) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE_PRICE = "UPDATE games SET prize=?, newprize=? WHERE id = 1 AND idnr = ?";
	private static final String UPDATE_LOTTERY = "UPDATE games SET finished=1, prize=?, newprize=?, number1=?, number2=?, prize1=?, prize2=?, prize3=? WHERE id=1 AND idnr=?";
	private static final String SELECT_LAST_LOTTERY = "SELECT idnr, prize, newprize, enddate, finished FROM games WHERE id = 1 ORDER BY idnr DESC LIMIT 1";
	private static final String SELECT_LOTTERY_ITEM = "SELECT enchant_level, custom_type2 FROM items WHERE item_id = 4442 AND custom_type1 = ?";
	private static final String SELECT_LOTTERY_TICKET = "SELECT number1, number2, prize1, prize2, prize3 FROM games WHERE id = 1 and idnr = ?";
	
	protected int _number;
	protected int _prize;
	protected boolean _isSellingTickets;
	protected boolean _isStarted;
	protected long _endDate;
	
	protected LotteryManager()
	{
		_number = 1;
		_prize = Config.LOTTERY_PRIZE;
		_isSellingTickets = false;
		_isStarted = false;
		_endDate = System.currentTimeMillis();
		
		if (Config.ALLOW_LOTTERY)
			(new StartLottery()).run();
	}
	
	public int getId()
	{
		return _number;
	}
	
	public int getPrize()
	{
		return _prize;
	}
	
	public long getEndDate()
	{
		return _endDate;
	}
	
	public boolean isSellableTickets()
	{
		return _isSellingTickets;
	}
	
	public boolean isStarted()
	{
		return _isStarted;
	}
	
	public void increasePrize(int count)
	{
		_prize += count;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_PRICE))
		{
			ps.setInt(1, getPrize());
			ps.setInt(2, getPrize());
			ps.setInt(3, getId());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't increase current lottery prize.", e);
		}
	}
	
	public static int[] decodeNumbers(int enchant, int type2)
	{
		int[] res = new int[5];
		
		int id = 0;
		int nr = 1;
		
		while (enchant > 0)
		{
			int val = enchant / 2;
			if (val != (double) enchant / 2)
				res[id++] = nr;
			
			enchant /= 2;
			nr++;
		}
		
		nr = 17;
		
		while (type2 > 0)
		{
			int val = type2 / 2;
			if (val != (double) type2 / 2)
				res[id++] = nr;
			
			type2 /= 2;
			nr++;
		}
		
		return res;
	}
	
	public static int[] checkTicket(ItemInstance item)
	{
		return checkTicket(item.getCustomType1(), item.getEnchantLevel(), item.getCustomType2());
	}
	
	public static int[] checkTicket(int id, int enchant, int type2)
	{
		int[] res =
		{
			0,
			0
		};
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_LOTTERY_TICKET))
		{
			ps.setInt(1, id);
			
			try (ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					int curenchant = rs.getInt("number1") & enchant;
					int curtype2 = rs.getInt("number2") & type2;
					
					if (curenchant == 0 && curtype2 == 0)
						return res;
					
					int count = 0;
					
					for (int i = 1; i <= 16; i++)
					{
						int val = curenchant / 2;
						if (val != (double) curenchant / 2)
							count++;
						
						int val2 = curtype2 / 2;
						if (val2 != (double) curtype2 / 2)
							count++;
						
						curenchant = val;
						curtype2 = val2;
					}
					
					switch (count)
					{
						case 0:
							break;
						
						case 5:
							res[0] = 1;
							res[1] = rs.getInt("prize1");
							break;
						
						case 4:
							res[0] = 2;
							res[1] = rs.getInt("prize2");
							break;
						
						case 3:
							res[0] = 3;
							res[1] = rs.getInt("prize3");
							break;
						
						default:
							res[0] = 4;
							res[1] = Config.LOTTERY_2_AND_1_NUMBER_PRIZE;
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't check lottery ticket #{}.", e, id);
		}
		
		return res;
	}
	
	private class StartLottery implements Runnable
	{
		protected StartLottery()
		{
		}
		
		@Override
		public void run()
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT_LAST_LOTTERY);
				ResultSet rs = ps.executeQuery())
			{
				if (rs.next())
				{
					_number = rs.getInt("idnr");
					
					if (rs.getInt("finished") == 1)
					{
						_number++;
						_prize = rs.getInt("newprize");
					}
					else
					{
						_prize = rs.getInt("prize");
						_endDate = rs.getLong("enddate");
						
						if (_endDate <= System.currentTimeMillis() + 2 * MINUTE)
						{
							(new FinishLottery()).run();
							return;
						}
						
						if (_endDate > System.currentTimeMillis())
						{
							_isStarted = true;
							ThreadPool.schedule(new FinishLottery(), _endDate - System.currentTimeMillis());
							
							if (_endDate > System.currentTimeMillis() + 12 * MINUTE)
							{
								_isSellingTickets = true;
								ThreadPool.schedule(new StopSellingTickets(), _endDate - System.currentTimeMillis() - 10 * MINUTE);
							}
							return;
						}
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't restore lottery data.", e);
			}
			
			_isSellingTickets = true;
			_isStarted = true;
			
			World.announceToOnlinePlayers("Lottery tickets are now available for Lucky Lottery #" + getId() + ".");
			
			Calendar finishTime = Calendar.getInstance();
			finishTime.setTimeInMillis(_endDate);
			finishTime.set(Calendar.MINUTE, 0);
			finishTime.set(Calendar.SECOND, 0);
			
			if (finishTime.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			{
				finishTime.set(Calendar.HOUR_OF_DAY, 19);
				_endDate = finishTime.getTimeInMillis();
				_endDate += 604800000;
			}
			else
			{
				finishTime.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
				finishTime.set(Calendar.HOUR_OF_DAY, 19);
				_endDate = finishTime.getTimeInMillis();
			}
			
			ThreadPool.schedule(new StopSellingTickets(), _endDate - System.currentTimeMillis() - 10 * MINUTE);
			ThreadPool.schedule(new FinishLottery(), _endDate - System.currentTimeMillis());
			
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT_LOTTERY))
			{
				ps.setInt(1, 1);
				ps.setInt(2, getId());
				ps.setLong(3, getEndDate());
				ps.setInt(4, getPrize());
				ps.setInt(5, getPrize());
				ps.execute();
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't store new lottery data.", e);
			}
		}
	}
	
	private class StopSellingTickets implements Runnable
	{
		protected StopSellingTickets()
		{
		}
		
		@Override
		public void run()
		{
			_isSellingTickets = false;
			
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.LOTTERY_TICKET_SALES_TEMP_SUSPENDED));
		}
	}
	
	private class FinishLottery implements Runnable
	{
		protected FinishLottery()
		{
		}
		
		@Override
		public void run()
		{
			int[] luckynums = new int[5];
			int luckynum = 0;
			
			for (int i = 0; i < 5; i++)
			{
				boolean found = true;
				
				while (found)
				{
					luckynum = Rnd.get(20) + 1;
					found = false;
					
					for (int j = 0; j < i; j++)
						if (luckynums[j] == luckynum)
							found = true;
				}
				
				luckynums[i] = luckynum;
			}
			
			int enchant = 0;
			int type2 = 0;
			
			for (int i = 0; i < 5; i++)
			{
				if (luckynums[i] < 17)
					enchant += Math.pow(2, luckynums[i] - 1);
				else
					type2 += Math.pow(2, luckynums[i] - 17);
			}
			
			int count1 = 0;
			int count2 = 0;
			int count3 = 0;
			int count4 = 0;
			
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT_LOTTERY_ITEM))
			{
				ps.setInt(1, getId());
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
					{
						int curenchant = rs.getInt("enchant_level") & enchant;
						int curtype2 = rs.getInt("custom_type2") & type2;
						
						if (curenchant == 0 && curtype2 == 0)
							continue;
						
						int count = 0;
						
						for (int i = 1; i <= 16; i++)
						{
							int val = curenchant / 2;
							
							if (val != (double) curenchant / 2)
								count++;
							
							int val2 = curtype2 / 2;
							
							if (val2 != (double) curtype2 / 2)
								count++;
							
							curenchant = val;
							curtype2 = val2;
						}
						
						if (count == 5)
							count1++;
						else if (count == 4)
							count2++;
						else if (count == 3)
							count3++;
						else if (count > 0)
							count4++;
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't restore lottery data.", e);
			}
			
			int prize4 = count4 * Config.LOTTERY_2_AND_1_NUMBER_PRIZE;
			int prize1 = 0;
			int prize2 = 0;
			int prize3 = 0;
			
			if (count1 > 0)
				prize1 = (int) ((getPrize() - prize4) * Config.LOTTERY_5_NUMBER_RATE / count1);
			
			if (count2 > 0)
				prize2 = (int) ((getPrize() - prize4) * Config.LOTTERY_4_NUMBER_RATE / count2);
			
			if (count3 > 0)
				prize3 = (int) ((getPrize() - prize4) * Config.LOTTERY_3_NUMBER_RATE / count3);
			
			// Calculate new prize.
			int newPrize = Config.LOTTERY_PRIZE + getPrize() - (prize1 + prize2 + prize3 + prize4);
			
			if (count1 > 0) // There are winners.
				World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.AMOUNT_FOR_WINNER_S1_IS_S2_ADENA_WE_HAVE_S3_PRIZE_WINNER).addNumber(getId()).addNumber(getPrize()).addNumber(count1));
			else
				// There are no winners.
				World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.AMOUNT_FOR_LOTTERY_S1_IS_S2_ADENA_NO_WINNER).addNumber(getId()).addNumber(getPrize()));
			
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_LOTTERY))
			{
				ps.setInt(1, getPrize());
				ps.setInt(2, newPrize);
				ps.setInt(3, enchant);
				ps.setInt(4, type2);
				ps.setInt(5, prize1);
				ps.setInt(6, prize2);
				ps.setInt(7, prize3);
				ps.setInt(8, getId());
				ps.execute();
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't store finished lottery data.", e);
			}
			
			ThreadPool.schedule(new StartLottery(), MINUTE);
			_number++;
			
			_isStarted = false;
		}
	}
	
	public static LotteryManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final LotteryManager INSTANCE = new LotteryManager();
	}
}