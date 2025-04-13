package com.shnok.javaserver.gameserver.taskmanager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Controls game time, informs spawn manager about day/night spawns and players about daytime change. Informs players about their extended activity in game.
 */
public final class GameTimeTaskManager implements Runnable
{
	private static final int MINUTES_PER_DAY = 24 * 60; // 24h * 60m
	
	public static final int HOURS_PER_GAME_DAY = 4; // 4h is 1 game day
	public static final int MINUTES_PER_GAME_DAY = HOURS_PER_GAME_DAY * 60; // 240m is 1 game day
	public static final int SECONDS_PER_GAME_DAY = MINUTES_PER_GAME_DAY * 60; // 14400s is 1 game day
	private static final int MILLISECONDS_PER_GAME_MINUTE = SECONDS_PER_GAME_DAY / (MINUTES_PER_DAY) * 1000; // 10000ms is 1 game minute
	
	private static final int TAKE_BREAK_HOURS = 1; // each 1h
	private static final int TAKE_BREAK_GAME_MINUTES = TAKE_BREAK_HOURS * MINUTES_PER_DAY / HOURS_PER_GAME_DAY; // 2h of real time is 720 game minutes
	
	private final Map<Player, GameTimeHandler> _players = new ConcurrentHashMap<>();
	
	private List<Quest> _questEvents = Collections.emptyList();
	
	private int _time;
	protected boolean _isNight;
	private long _startTime;
	
	protected GameTimeTaskManager()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		_time = (int) (System.currentTimeMillis() - cal.getTimeInMillis()) / MILLISECONDS_PER_GAME_MINUTE;
		_isNight = isNight();
		_startTime = System.currentTimeMillis();
		
		// Run task each 10 seconds.
		ThreadPool.scheduleAtFixedRate(this, MILLISECONDS_PER_GAME_MINUTE, MILLISECONDS_PER_GAME_MINUTE);
	}
	
	@Override
	public final void run()
	{
		// Tick time.
		_time++;
		
		// Quest listener.
		final int gameTime = getGameTime();
		for (Quest quest : _questEvents)
			quest.onGameTime(gameTime);
		
		// Shadow Sense skill, if set then perform day/night info.
		L2Skill skill = null;
		
		// Day/night has changed.
		if (_isNight != isNight())
		{
			// Change day/night.
			_isNight = !_isNight;
			
			// Set Shadow Sense skill to apply/remove effect from players.
			skill = SkillTable.getInstance().getInfo(L2Skill.SKILL_SHADOW_SENSE, 1);
		}
		
		// List is empty, skip.
		if (_players.isEmpty())
			return;
		
		// Loop all players.
		for (Map.Entry<Player, GameTimeHandler> entry : _players.entrySet())
		{
			// Get player.
			final Player player = entry.getKey();
			
			// Player isn't online, skip.
			if (!player.isOnline())
				continue;
			
			// Shadow Sense skill is set and player has Shadow Sense skill, activate/deactivate its effect.
			if (skill != null && player.hasSkill(L2Skill.SKILL_SHADOW_SENSE))
			{
				// Remove and add Shadow Sense to activate/deactivate effect.
				player.removeSkill(L2Skill.SKILL_SHADOW_SENSE, false);
				player.addSkill(skill, false);
				
				// Inform player about effect change.
				player.sendPacket(SystemMessage.getSystemMessage(_isNight ? SystemMessageId.NIGHT_S1_EFFECT_APPLIES : SystemMessageId.DAY_S1_EFFECT_DISAPPEARS).addSkillName(L2Skill.SKILL_SHADOW_SENSE));
			}

			GameTimeHandler value = entry.getValue();
			// Activity time has passed already.
			if (_time >= value.getNextBreakAlert())
			{
				// Inform player about his activity.
//				player.sendPacket(SystemMessageId.PLAYING_FOR_LONG_TIME);

				value.setLoginHours((short) (value.getLoginHours() + 1));
				value.setNextBreakAlert(_time + TAKE_BREAK_GAME_MINUTES);

				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PLAYING_FOR_LONG_TIME).addNumber(value.getLoginHours()));

				// Update activity time.
				entry.setValue(value);
			}
		}
	}
	
	public void addQuestEvent(Quest quest)
	{
		if (_questEvents.isEmpty())
			_questEvents = new ArrayList<>(3);
		
		_questEvents.add(quest);
	}
	
	/**
	 * Returns how many game days have left since last server start.
	 * @return int : Game day.
	 */
	public final int getGameDay()
	{
		return _time / MINUTES_PER_DAY;
	}
	
	/**
	 * Returns game time in minute format (0-1439).
	 * @return int : Game time.
	 */
	public final int getGameTime()
	{
		return _time % MINUTES_PER_DAY;
	}
	
	/**
	 * Returns game hour (0-23).
	 * @return int : Game hour.
	 */
	public final int getGameHour()
	{
		return (_time % MINUTES_PER_DAY) / 60;
	}
	
	/**
	 * Returns game minute (0-59).
	 * @return int : Game minute.
	 */
	public final int getGameMinute()
	{
		return _time % 60;
	}
	
	/**
	 * Returns game time standard format (00:00-23:59).
	 * @return String : Game time.
	 */
	public final String getGameTimeFormated()
	{
		return String.format("%02d:%02d", getGameHour(), getGameMinute());
	}
	
	/**
	 * Returns game daytime. Night is between 00:00 and 06:00.
	 * @return boolean : True, when there is night.
	 */
	public final boolean isNight()
	{
		return getGameTime() < 360;
	}
	
	/**
	 * Adds {@link Player} to the GameTimeTask to control is activity.
	 * @param player : {@link Player} to be added and checked.
	 */
	public final void add(Player player)
	{
		_players.put(player, new GameTimeHandler(_time + TAKE_BREAK_GAME_MINUTES));
	}
	
	/**
	 * Removes {@link Player} from the GameTimeTask.
	 * @param player : {@link Player} to be removed.
	 */
	public final void remove(Creature player)
	{
		_players.remove(player);
	}
	
	/**
	 * This method is used as a monotonic clockwall.
	 * @return The elapsed time since server startup, in seconds.
	 */
	public int getCurrentTick()
	{
		return (int) ((System.currentTimeMillis() - _startTime) / 1000);
	}
	
	public static final GameTimeTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}

	@Getter
	@Setter
	private static class GameTimeHandler {
		private short loginHours;
		private int nextBreakAlert;

		public GameTimeHandler(int nextBreakAlert) {
			this.nextBreakAlert = nextBreakAlert;
		}
	}
	private static class SingletonHolder
	{
		protected static final GameTimeTaskManager INSTANCE = new GameTimeTaskManager();
	}
}
