package com.shnok.javaserver.gameserver.scripting;

import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class QuestTimer
{
	private final Quest _quest;
	private final String _name;
	private final Npc _npc;
	private final Player _player;
	
	private ScheduledFuture<?> _schedular;
	
	QuestTimer(Quest quest, String name, Npc npc, Player player, long initial, long period)
	{
		_quest = quest;
		_name = name;
		_npc = npc;
		_player = player;
		
		if (period > 0)
			_schedular = ThreadPool.scheduleAtFixedRate(this::runTick, initial, period);
		else
			_schedular = ThreadPool.schedule(this::runOnce, initial);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_name, _npc, _player, _quest);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (!(obj instanceof QuestTimer other))
			return false;
		
		return Objects.equals(_name, other._name) && Objects.equals(_npc, other._npc) && Objects.equals(_player, other._player) && Objects.equals(_quest, other._quest);
	}
	
	@Override
	public final String toString()
	{
		return _name;
	}
	
	/**
	 * @return The name of the {@link QuestTimer}.
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * @return The {@link Npc} of the {@link QuestTimer}.
	 */
	public final Npc getNpc()
	{
		return _npc;
	}
	
	/**
	 * @return The {@link Player} of the {@link QuestTimer}.
	 */
	public final Player getPlayer()
	{
		return _player;
	}
	
	private void runTick()
	{
		// Notify.
		_quest.notifyTimer(_name, _npc, _player);
	}
	
	private void runOnce()
	{
		// Remove it from the Quest first (the timer event may create new timer with same name -> it would be duplicate and skipped).
		_quest.removeQuestTimer(this);
		
		// Notify.
		_quest.notifyTimer(_name, _npc, _player);
	}
	
	/**
	 * Cancel the {@link QuestTimer}.
	 */
	public final void cancel()
	{
		if (_schedular != null)
		{
			_schedular.cancel(false);
			_schedular = null;
		}
		
		_quest.removeQuestTimer(this);
	}
}