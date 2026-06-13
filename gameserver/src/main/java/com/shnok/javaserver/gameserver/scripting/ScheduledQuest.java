package com.shnok.javaserver.gameserver.scripting;

import java.util.Calendar;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.gameserver.enums.ScheduleType;

public abstract class ScheduledQuest extends Quest
{
	private ScheduleType _type;
	private Calendar _start;
	private Calendar _end;
	private boolean _started;
	
	private ScheduledFuture<?> _task;
	
	protected ScheduledQuest(int questId, String descr)
	{
		super(questId, descr);
	}
	
	/**
	 * Return true, when a {@link ScheduledQuest} is started.
	 * @return boolean : True, when started.
	 */
	public final boolean isStarted()
	{
		return _started;
	}
	
	/**
	 * Set up schedule system for the script. Returns true, when successfully done.
	 * @param type : Type of the schedule.
	 * @param start : Start information.
	 * @param end : End information.
	 * @return boolean : True, when successfully loaded schedule system.
	 */
	public final boolean setSchedule(String type, String start, String end)
	{
		try
		{
			_type = Enum.valueOf(ScheduleType.class, type);
			_start = parseTimeStamp(start);
			_end = parseTimeStamp(end);
			_started = false;
			
			final long st = _start.getTimeInMillis();
			final long now = System.currentTimeMillis();
			if (_end == null || _end.getTimeInMillis() == st)
			{
				// Start and end events are at same time, consider as one-event script.
				_end = null;
				
				// Schedule next start.
				if (st < now)
					_start.add(_type.getPeriod(), 1);
			}
			else
			{
				// Normal schedule, both events are in same period.
				final long en = _end.getTimeInMillis();
				if (st < en)
				{
					// Last schedule had passed, schedule next start.
					if (en < now)
						_start.add(_type.getPeriod(), 1);
					// Last schedule is running, start script.
					else if (st < now)
						_started = true;
					// Last schedule has not started yet, shift end by 1 period backwards (is updated in notifyAndSchedule() when starting schedule).
					else
						_end.add(_type.getPeriod(), -1);
				}
				// Reverse schedule, each event is in different period (e.g. different day for DAILY - start = 23:00, end = 01:00).
				else
				{
					// Last schedule is running, schedule next end and start script.
					if (st < now)
					{
						_end.add(_type.getPeriod(), 1);
						_started = true;
					}
					// Last schedule is running, shift start by 1 period backwards (is updated in notifyAndSchedule() when starting schedule) and start script.
					else if (now < en)
					{
						_start.add(_type.getPeriod(), -1);
						_started = true;
					}
					// Last schedule has not started yet, do nothing.
				}
			}
			
			// Initialize script and return.
			return init();
		}
		catch (Exception e)
		{
			LOGGER.error("Error loading schedule data for {}.", e, toString());
			
			_type = null;
			_start = null;
			_end = null;
			_started = false;
			return false;
		}
	}
	
	private final Calendar parseTimeStamp(String value)
	{
		if (value == null)
			return null;
		
		final Calendar calendar = Calendar.getInstance();
		String[] timeStamp;
		
		switch (_type)
		{
			case HOURLY:
				// HOURLY, "20:10", "50:00"
				timeStamp = value.split(":");
				calendar.set(Calendar.MINUTE, Integer.valueOf(timeStamp[0]));
				calendar.set(Calendar.SECOND, Integer.valueOf(timeStamp[1]));
				calendar.set(Calendar.MILLISECOND, 0);
				return calendar;
			
			case DAILY:
				// DAILY, "16:20:10", "17:20:00"
				timeStamp = value.split(":");
				break;
			
			case WEEKLY:
				// WEEKLY, "MON 6:20:10", "FRI 17:20:00"
				String[] params = value.split(" ");
				timeStamp = params[1].split(":");
				calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(params[0]));
				break;
			
			case MONTHLY_DAY:
				// MONTHLY_DAY, "1 6:20:10", "2 17:20:00"
				params = value.split(" ");
				timeStamp = params[1].split(":");
				calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(params[0]));
				break;
			
			case MONTHLY_WEEK:
				// MONTHLY_WEEK, "MON-1 6:20:10", "FRI-2 17:20:00"
				params = value.split(" ");
				String[] date = params[0].split("-");
				timeStamp = params[1].split(":");
				calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(date[0]));
				calendar.set(Calendar.WEEK_OF_MONTH, Integer.valueOf(date[1]));
				break;
			
			case YEARLY_DAY:
				// YEARLY_DAY, "23-02 6:20:10", "25-03 17:20:00"
				params = value.split(" ");
				date = params[0].split("-");
				timeStamp = params[1].split(":");
				calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(date[0]));
				calendar.set(Calendar.MONTH, Integer.valueOf(date[1]) - 1);
				break;
			
			case YEARLY_WEEK:
				// YEARLY_WEEK, "MON-1 6:20:10", "FRI-2 17:20:00"
				params = value.split(" ");
				date = params[0].split("-");
				timeStamp = params[1].split(":");
				calendar.set(Calendar.DAY_OF_WEEK, getDayOfWeek(date[0]));
				calendar.set(Calendar.WEEK_OF_YEAR, Integer.valueOf(date[1]));
				break;
			
			default:
				return null;
		}
		
		// set hour, minute and second
		calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(timeStamp[0]));
		calendar.set(Calendar.MINUTE, Integer.valueOf(timeStamp[1]));
		calendar.set(Calendar.SECOND, Integer.valueOf(timeStamp[2]));
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar;
	}
	
	/**
	 * Returns time of next action of the script.
	 * @return long : Time in milliseconds.
	 */
	public final long getTimeNext()
	{
		if (_type == null)
			return 0;
		
		return _started ? _end.getTimeInMillis() : _start.getTimeInMillis();
	}
	
	/**
	 * Returns the last/next start time, regardless the state of the script.
	 * @return long : Time in milliseconds.
	 */
	public final long getStartTime()
	{
		return _start.getTimeInMillis();
	}
	
	/**
	 * Returns the last/next end time, regardless the state of the script.
	 * @return long : Time in milliseconds.
	 */
	public final long getEndTime()
	{
		return _end.getTimeInMillis();
	}
	
	/**
	 * Notify and schedule next action of the script.
	 */
	public final void notifyAndSchedule()
	{
		if (_type == null)
			return;
		
		// Notify one-action script (start).
		if (_end == null)
		{
			// Schedule next start.
			_start.add(_type.getPeriod(), 1);
			print(_start);
			
			// Notify start.
			try
			{
				onStart();
			}
			catch (Exception e)
			{
				LOGGER.error("Error starting {}.", e, toString());
			}
			
			return;
		}
		
		// Notify two-action script (start + end).
		if (_started)
		{
			// Schedule start.
			_start.add(_type.getPeriod(), 1);
			print(_start);
			
			// Notify end.
			try
			{
				_started = false;
				onEnd();
			}
			catch (Exception e)
			{
				LOGGER.error("Error ending {}.", e, toString());
			}
		}
		else
		{
			// Schedule end.
			_end.add(_type.getPeriod(), 1);
			print(_end);
			
			// Notify start.
			try
			{
				_started = true;
				onStart();
			}
			catch (Exception e)
			{
				LOGGER.error("Error starting {}.", e, toString());
			}
		}
	}
	
	/**
	 * Initializes a script and returns information about script to be scheduled or not. Set internal values, parameters, etc...<br>
	 * <br>
	 * Note: Default behavior is to call onStart(), when the script is supposed to be started.
	 * @return boolean : True, when script was initialized and can be scheduled.
	 */
	protected boolean init()
	{
		// The script was initialized as started, run start event.
		if (_started)
			onStart();
		
		return true;
	}
	
	/**
	 * Starts a script. Handles spawns, announcements, loads variables, etc...
	 */
	protected abstract void onStart();
	
	/**
	 * Ends a script. Handles spawns, announcements, saves variables, etc...
	 */
	protected abstract void onEnd();
	
	/**
	 * Stops a script. Clears internal values, parameters, etc...<br>
	 * <br>
	 * Note: Default behavior is to call onEnd(), when the script is started.
	 */
	public void stop()
	{
		// The script is running, run end event.
		if (_started)
			onEnd();
	}
	
	/**
	 * Convert a {@link String} representation of a day into a {@link Calendar} day.
	 * @param day : The {@link String} representation of a day.
	 * @return The {@link Calendar} representation of a day.
	 */
	private final int getDayOfWeek(String day)
	{
		if (day.equals("MON"))
			return Calendar.MONDAY;
		else if (day.equals("TUE"))
			return Calendar.TUESDAY;
		else if (day.equals("WED"))
			return Calendar.WEDNESDAY;
		else if (day.equals("THU"))
			return Calendar.THURSDAY;
		else if (day.equals("FRI"))
			return Calendar.FRIDAY;
		else if (day.equals("SAT"))
			return Calendar.SATURDAY;
		else if (day.equals("SUN"))
			return Calendar.SUNDAY;
		
		LOGGER.error("Error parsing day of week {}, MONDAY will be used for {}.", day, toString());
		return Calendar.MONDAY;
	}
	
	private final void print(Calendar c)
	{
		LOGGER.debug("{}: {} = {}.", toString(), ((c == _start) ? "Next start" : "Next end"), String.format("%d.%d.%d %d:%02d:%02d", c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND)));
	}
	
	public final void setTask(ScheduledFuture<?> task)
	{
		cleanTask();
		
		_task = task;
	}
	
	public final void cleanTask()
	{
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
		}
	}
}