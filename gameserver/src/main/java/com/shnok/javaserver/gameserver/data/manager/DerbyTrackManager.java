package com.shnok.javaserver.gameserver.data.manager;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.HistoryInfo;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.zone.type.DerbyTrackZone;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.DeleteObject;
import com.shnok.javaserver.gameserver.network.serverpackets.MonRaceInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.PlaySound;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class DerbyTrackManager
{
	private static final CLogger LOGGER = new CLogger(DerbyTrackManager.class.getName());
	
	private static final String SAVE_HISTORY = "INSERT INTO mdt_history (race_id, first, second, odd_rate) VALUES (?,?,?,?)";
	private static final String LOAD_HISTORY = "SELECT * FROM mdt_history";
	private static final String LOAD_BETS = "SELECT * FROM mdt_bets";
	private static final String SAVE_BETS = "REPLACE INTO mdt_bets (lane_id, bet) VALUES (?,?)";
	private static final String CLEAR_BETS = "UPDATE mdt_bets SET bet = 0";
	
	public enum RaceState
	{
		ACCEPTING_BETS,
		WAITING,
		STARTING_RACE,
		RACE_END
	}
	
	private static final PlaySound SOUND_1 = new PlaySound(1, "S_Race");
	private static final PlaySound SOUND_2 = new PlaySound("ItemSound2.race_start");
	
	private static final int[][] CODES =
	{
		{
			-1,
			0
		},
		{
			0,
			15322
		},
		{
			13765,
			-1
		}
	};
	
	private final List<Npc> _runners = new ArrayList<>(); // List holding initial npcs, shuffled on a new race.
	private final TreeMap<Integer, HistoryInfo> _history = new TreeMap<>(); // List holding old race records.
	private final Map<Integer, Long> _betsPerLane = new ConcurrentHashMap<>(); // Map holding all bets for each lane ; values are set to 0 after every race.
	private final List<Double> _odds = new ArrayList<>(); // List holding sorted odds per lane ; cleared at new odds calculation.
	
	private int _raceNumber = 1;
	private int _finalCountdown = 0;
	private RaceState _state = RaceState.RACE_END;
	
	private MonRaceInfo _packet;
	
	private List<Npc> _chosenRunners; // Holds the actual list of 8 runners.
	private int[][] _speeds;
	private int _firstIndex; // Index going from 0-7.
	private int _secondIndex; // Index going from 0-7.
	
	protected DerbyTrackManager()
	{
		// Feed _history with previous race results.
		loadHistory();
		
		// Feed _betsPerLane with stored informations on bets.
		loadBets();
		
		// Feed _runners, we will only have to shuffle it when needed.
		try
		{
			for (int i = 31003; i < 31027; i++)
			{
				final NpcTemplate template = NpcData.getInstance().getTemplate(i);
				if (template == null)
					continue;
				
				final Constructor<?> constructor = Class.forName("com.shnok.javaserver.gameserver.model.actor.instance." + template.getType()).getConstructors()[0];
				
				_runners.add((Npc) constructor.newInstance(IdFactory.getInstance().getNextId(), template));
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't initialize runners.", e);
		}
		
		_speeds = new int[8][20];
		
		ThreadPool.scheduleAtFixedRate(this::countdown, 0, 1000);
	}
	
	public List<Npc> getRunners()
	{
		return _chosenRunners;
	}
	
	/**
	 * @param index : The actual index of List to check on.
	 * @return the name of the Npc.
	 */
	public String getRunnerName(int index)
	{
		final Npc npc = _chosenRunners.get(index);
		return (npc == null) ? "" : npc.getName();
	}
	
	public int[][] getSpeeds()
	{
		return _speeds;
	}
	
	public int getFirst()
	{
		return _firstIndex;
	}
	
	public int getSecond()
	{
		return _secondIndex;
	}
	
	public MonRaceInfo getRacePacket()
	{
		return _packet;
	}
	
	public RaceState getCurrentRaceState()
	{
		return _state;
	}
	
	public int getRaceNumber()
	{
		return _raceNumber;
	}
	
	public List<HistoryInfo> getLastHistoryEntries()
	{
		return _history.descendingMap().values().stream().limit(8).toList();
	}
	
	public HistoryInfo getHistoryInfo(int raceNumber)
	{
		return _history.get(raceNumber);
	}
	
	public List<Double> getOdds()
	{
		return _odds;
	}
	
	private void newRace()
	{
		// Edit _history.
		_history.put(_raceNumber, new HistoryInfo(_raceNumber, 0, 0, 0));
		
		// Randomize _runners.
		Collections.shuffle(_runners);
		
		// Setup 8 new creatures ; pickup the first 8 from _runners.
		_chosenRunners = _runners.subList(0, 8);
	}
	
	private void newSpeeds()
	{
		_speeds = new int[8][20];
		
		int total = 0;
		int winnerDistance = 0;
		int secondDistance = 0;
		
		// For each lane.
		for (int i = 0; i < 8; i++)
		{
			// Reset value upon new lane.
			total = 0;
			
			// Test the 20 segments.
			for (int j = 0; j < 20; j++)
			{
				if (j == 19)
					_speeds[i][j] = 100;
				else
					_speeds[i][j] = Rnd.get(60) + 65;
				
				// feed actual total to current lane total.
				total += _speeds[i][j];
			}
			
			// The current total for this line is superior or equals to previous winner ; it means we got a new winner, and the old winner becomes second.
			if (total >= winnerDistance)
			{
				// Old winner becomes second.
				_secondIndex = _firstIndex;
				
				// Old winner distance is the second.
				secondDistance = winnerDistance;
				
				// Find the good index.
				_firstIndex = i;
				
				// Set the new limit to bypass winner position.
				winnerDistance = total;
			}
			// The total wasn't enough to
			else if (total >= secondDistance)
			{
				// Find the good index.
				_secondIndex = i;
				
				// Set the new limit to bypass second position.
				secondDistance = total;
			}
		}
	}
	
	/**
	 * Load past races informations, feeding _history arrayList.<br>
	 * Also sets _raceNumber, based on latest HistoryInfo loaded.
	 */
	private void loadHistory()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_HISTORY);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
			{
				final int savedRaceNumber = rs.getInt("race_id");
				
				_history.put(savedRaceNumber, new HistoryInfo(savedRaceNumber, rs.getInt("first"), rs.getInt("second"), rs.getDouble("odd_rate")));
				
				// Calculate the current race number.
				if (_raceNumber <= savedRaceNumber)
					_raceNumber = savedRaceNumber + 1;
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Can't load Derby Track history.", e);
		}
		LOGGER.info("Loaded {} Derby Track records, currently on race #{}.", _history.size(), _raceNumber);
	}
	
	/**
	 * Load current bets per lane ; initialize the map keys.
	 */
	private void loadBets()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_BETS);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
				setBetOnLane(rs.getInt("lane_id"), rs.getLong("bet"), false);
		}
		catch (Exception e)
		{
			LOGGER.error("Can't load Derby Track bets.", e);
		}
	}
	
	/**
	 * Clear all lanes bets, either on database or Map.
	 */
	private void clearBets()
	{
		for (int key : _betsPerLane.keySet())
			_betsPerLane.put(key, 0L);
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(CLEAR_BETS))
		{
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Can't clear Derby Track bets.", e);
		}
	}
	
	/**
	 * Setup lane bet, based on previous value (if any).
	 * @param lane : The lane to edit.
	 * @param amount : The amount to add.
	 * @param saveOnDb : Should it be saved on the database or not.
	 */
	public void setBetOnLane(int lane, long amount, boolean saveOnDb)
	{
		final long sum = _betsPerLane.getOrDefault(lane, 0L) + amount;
		
		_betsPerLane.put(lane, sum);
		
		if (saveOnDb)
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(SAVE_BETS))
			{
				ps.setInt(1, lane);
				ps.setLong(2, sum);
				ps.execute();
			}
			catch (Exception e)
			{
				LOGGER.error("Can't save Derby Track bet.", e);
			}
		}
	}
	
	/**
	 * Calculate odds for every lane, based on others lanes.
	 */
	private void calculateOdds()
	{
		// Clear previous List holding old odds.
		_odds.clear();
		
		// Sort bets lanes per lane.
		final Map<Integer, Long> sortedLanes = new TreeMap<>(_betsPerLane);
		
		// Pass a first loop in order to calculate total sum of all lanes.
		long sumOfAllLanes = 0;
		for (long amount : sortedLanes.values())
			sumOfAllLanes += amount;
		
		// As we get the sum, we can now calculate the odd rate of each lane.
		for (long amount : sortedLanes.values())
			_odds.add((amount == 0) ? 0D : Math.max(1.25, sumOfAllLanes * 0.7 / amount));
	}
	
	private void countdown()
	{
		if (_finalCountdown > 1200)
			_finalCountdown = 0;
		
		switch (_finalCountdown)
		{
			case 0:
				newRace();
				newSpeeds();
				
				_state = RaceState.ACCEPTING_BETS;
				_packet = new MonRaceInfo(CODES[0][0], CODES[0][1], getRunners(), getSpeeds());
				
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, _packet, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_AVAILABLE_FOR_S1_RACE).addNumber(_raceNumber));
				break;
			
			case 30, 60, 90, 120, 150, 180, 210, 240, 270, 330, 360, 390, 420, 450, 480, 510, 540, 570, 630, 660, 690, 720, 750, 780, 810, 870: // steps of 30 sec, 1 min to 14 min 30 sec
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE).addNumber(_raceNumber));
				break;
			
			case 300: // 5 min
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE).addNumber(_raceNumber), SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_STOP_IN_S1_MINUTES).addNumber(10));
				break;
			
			case 600: // 10 min
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE).addNumber(_raceNumber), SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_STOP_IN_S1_MINUTES).addNumber(5));
				break;
			
			case 840: // 14 min
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE).addNumber(_raceNumber), SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_STOP_IN_S1_MINUTES).addNumber(1));
				break;
			
			case 900: // 15 min
				_state = RaceState.WAITING;
				
				calculateOdds();
				
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_TICKETS_NOW_AVAILABLE_FOR_S1_RACE).addNumber(_raceNumber), SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_S1_TICKET_SALES_CLOSED));
				break;
			
			case 960, 1020: // 16, 17 min
				final int minutes = (_finalCountdown == 960) ? 2 : 1;
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_S2_BEGINS_IN_S1_MINUTES).addNumber(minutes));
				break;
			
			case 1050: // 17 min 30 sec
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_S1_BEGINS_IN_30_SECONDS));
				break;
			
			case 1070: // 17 min 50 sec
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_S1_COUNTDOWN_IN_FIVE_SECONDS));
				break;
			
			case 1075, 1076, 1077, 1078, 1079: // 17 min 55, 56, 57, 58, 59 sec
				final int seconds = 1080 - _finalCountdown;
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_BEGINS_IN_S1_SECONDS).addNumber(seconds));
				break;
			
			case 1080: // 18 min
				_state = RaceState.STARTING_RACE;
				_packet = new MonRaceInfo(CODES[1][0], CODES[1][1], getRunners(), getSpeeds());
				
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_RACE_START), SOUND_1, SOUND_2, _packet);
				break;
			
			case 1085: // 18 min 5 sec
				_packet = new MonRaceInfo(CODES[2][0], CODES[2][1], getRunners(), getSpeeds());
				
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, _packet);
				break;
			
			case 1115: // 18 min 35 sec
				_state = RaceState.RACE_END;
				
				// Retrieve current HistoryInfo and populate it with data, then stores it in database.
				final HistoryInfo info = getHistoryInfo(_raceNumber);
				if (info != null)
				{
					info.setFirst(getFirst());
					info.setSecond(getSecond());
					info.setOddRate(_odds.get(getFirst()));
					
					try (Connection con = ConnectionPool.getConnection();
						PreparedStatement ps = con.prepareStatement(SAVE_HISTORY))
					{
						ps.setInt(1, info.getRaceId());
						ps.setInt(2, info.getFirst());
						ps.setInt(3, info.getSecond());
						ps.setDouble(4, info.getOddRate());
						ps.execute();
					}
					catch (Exception e)
					{
						LOGGER.error("Can't save Derby Track history.", e);
					}
				}
				
				// Clear bets.
				clearBets();
				
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_FIRST_PLACE_S1_SECOND_S2).addNumber(getFirst() + 1).addNumber(getSecond() + 1), SystemMessage.getSystemMessage(SystemMessageId.MONSRACE_S1_RACE_END).addNumber(_raceNumber));
				_raceNumber++;
				break;
			
			case 1140: // 19 min
				ZoneManager.toAllPlayersInZoneType(DerbyTrackZone.class, new DeleteObject(getRunners().get(0)), new DeleteObject(getRunners().get(1)), new DeleteObject(getRunners().get(2)), new DeleteObject(getRunners().get(3)), new DeleteObject(getRunners().get(4)), new DeleteObject(getRunners().get(5)), new DeleteObject(getRunners().get(6)), new DeleteObject(getRunners().get(7)));
				break;
		}
		_finalCountdown += 1;
	}
	
	public static DerbyTrackManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final DerbyTrackManager INSTANCE = new DerbyTrackManager();
	}
}