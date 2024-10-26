package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.data.xml.IXmlReader;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.ScheduledQuest;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores {@link Quest}s - being regular quests, AI scripts or scheduled scripts.
 */
public final class ScriptData implements IXmlReader, Runnable
{
	public static final int PERIOD = 5 * 60 * 1000; // 5 minutes
	
	private final List<Quest> _quests = new ArrayList<>();
	private final List<ScheduledQuest> _scheduled = new LinkedList<>();
	
	private ScheduledFuture<?> _scheduledTask;
	
	public ScriptData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/scripts.xml");
		LOGGER.info("Loaded {} regular scripts and {} scheduled scripts.", _quests.size(), _scheduled.size());
		
		_scheduledTask = ThreadPool.scheduleAtFixedRate(this, 0, PERIOD);
	}
	
	@Override
	public void parseDocument(Document doc, Path p)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "script", scriptNode ->
		{
			final NamedNodeMap params = scriptNode.getAttributes();
			final String path = parseString(params, "path");
			if (path == null)
			{
				LOGGER.warn("One of the script path isn't defined.");
				return;
			}
			
			try
			{
				// Create the script.
				Quest instance = (Quest) Class.forName("com.shnok.javaserver.gameserver.scripting." + path).getDeclaredConstructor().newInstance();
				
				// Add quest, script, AI or any other custom type of script.
				_quests.add(instance);
				
				// If script is part of AI, try to parse EventHandlers.
				if (instance instanceof DefaultNpc)
					instance.feedEventHandlers();
				
				// The script has been identified as a scheduled script, make proper checks and schedule the launch.
				if (instance instanceof ScheduledQuest sq)
				{
					// Get schedule parameter, when not exist, script is not scheduled.
					final String type = parseString(params, "schedule");
					if (type == null)
						return;
					
					// Get mandatory start parameter, when not exist, script is not scheduled.
					final String start = parseString(params, "start");
					if (start == null)
					{
						LOGGER.warn("Missing 'start' parameter for scheduled script '{}'.", path);
						return;
					}
					
					// Get optional end parameter, when not exist, script is one-event type.
					final String end = parseString(params, "end");
					
					// Schedule script, when successful, register it.
					if (sq.setSchedule(type, start, end))
						_scheduled.add(sq);
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Script '{}' is missing.", e, path);
			}
		}));
	}
	
	@Override
	public void run()
	{
		// For each PERIOD.
		final long next = System.currentTimeMillis() + PERIOD;
		
		// Check all scheduled scripts.
		for (ScheduledQuest script : _scheduled)
		{
			// When next action triggers in closest period, schedule the script action.
			final long eta = next - script.getTimeNext();
			if (eta > 0)
				script.setTask(ThreadPool.schedule(new Scheduler(script), PERIOD - eta));
		}
	}
	
	public void reload()
	{
		// Stop the general 5min task.
		if (_scheduledTask != null)
		{
			_scheduledTask.cancel(false);
			_scheduledTask = null;
		}
		
		_quests.clear();
		
		// Stop the individual scheduled tasks.
		for (ScheduledQuest script : _scheduled)
			script.cleanTask();
		
		_scheduled.clear();
		
		load();
	}
	
	/**
	 * Notifies all {@link ScheduledQuest} to stop.<br>
	 * <br>
	 * Note: Can be used to store script values and variables.
	 */
	public final void stopAllScripts()
	{
		for (ScheduledQuest sq : _scheduled)
			sq.stop();
	}
	
	/**
	 * Returns the {@link Quest} by given quest name.
	 * @param questName : The name of the quest.
	 * @return Quest : Quest to be returned, null if quest does not exist.
	 */
	public final Quest getQuest(String questName)
	{
		return _quests.stream().filter(q -> q.getName().equalsIgnoreCase(questName)).findFirst().orElse(null);
	}
	
	/**
	 * Returns the {@link Quest} by given quest id.
	 * @param questId : The id of the quest.
	 * @return Quest : Quest to be returned, null if quest does not exist.
	 */
	public final Quest getQuest(int questId)
	{
		return _quests.stream().filter(q -> q.getQuestId() == questId).findFirst().orElse(null);
	}
	
	/**
	 * @return the {@link List} of {@link Quest}s.
	 */
	public final List<Quest> getQuests()
	{
		return _quests;
	}
	
	private final class Scheduler implements Runnable
	{
		private final ScheduledQuest _script;
		
		protected Scheduler(ScheduledQuest script)
		{
			_script = script;
		}
		
		@Override
		public void run()
		{
			// Notify script.
			_script.notifyAndSchedule();
			
			// In case the next action is triggered before the resolution, schedule the the action again.
			final long eta = System.currentTimeMillis() + PERIOD - _script.getTimeNext();
			if (eta > 0)
				_script.setTask(ThreadPool.schedule(this, PERIOD - eta));
		}
	}
	
	public static ScriptData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final ScriptData INSTANCE = new ScriptData();
	}
}