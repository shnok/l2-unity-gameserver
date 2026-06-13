package com.shnok.javaserver.gameserver.model.actor.container.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.xml.ScriptData;
import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public final class QuestList extends ArrayList<QuestState>
{
	private static final CLogger LOGGER = new CLogger(QuestList.class.getName());
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOAD_PLAYER_QUESTS = "SELECT name,var,value FROM character_quests WHERE charId=?";
	
	private final Player _player;
	
	private int _lastQuestNpcObjectId;
	
	public QuestList(Player player)
	{
		_player = player;
	}
	
	public int getLastQuestNpcObjectId()
	{
		return _lastQuestNpcObjectId;
	}
	
	public void setLastQuestNpcObjectId(int objectId)
	{
		_lastQuestNpcObjectId = objectId;
	}
	
	/**
	 * @param name : The name of the {@link Quest}.
	 * @return The {@link QuestState} corresponding to the name, or null if not found.
	 */
	public QuestState getQuestState(String name)
	{
		return stream().filter(qs -> name.equals(qs.getQuest().getName())).findFirst().orElse(null);
	}
	
	/**
	 * Note: Only real quests are evaluated (they have id > 0).
	 * @param id : The id of the {@link Quest}.
	 * @return The {@link QuestState} corresponding to the quest id, or null if not found.
	 */
	public QuestState getQuestState(int id)
	{
		return stream().filter(qs -> id == qs.getQuest().getQuestId()).findFirst().orElse(null);
	}
	
	/**
	 * @param completed : If true, include completed quests to the {@link List}.
	 * @return A {@link List} of started and eventually completed {@link Quest}s.
	 */
	public List<QuestState> getAllQuests(boolean completed)
	{
		return stream().filter(qs -> qs.getQuest().isRealQuest() && (qs.isStarted() || (qs.isCompleted() && completed))).toList();
	}
	
	/**
	 * @param predicate : The {@link Predicate} defining {@link Quest} matching condition.
	 * @return The {@link List} of quests and scripts matching given {@link Predicate}.
	 */
	public List<Quest> getQuests(Predicate<Quest> predicate)
	{
		return stream().map(QuestState::getQuest).filter(predicate).toList();
	}
	
	/**
	 * Restore {@link QuestState}s of this {@link Player} from the database.
	 */
	public void restore()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_PLAYER_QUESTS))
		{
			ps.setInt(1, _player.getObjectId());
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final String questName = rs.getString("name");
					
					// Test quest existence.
					final Quest quest = ScriptData.getInstance().getQuest(questName);
					if (quest == null)
					{
						LOGGER.warn("Unknown quest {} for player {}.", questName, _player.getName());
						continue;
					}
					
					QuestState qs = getQuestState(questName);
					if (qs == null)
						qs = new QuestState(_player, quest);
					
					qs.loadFromDB(rs);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore quests.", e);
		}
	}
	
	public void processQuestEvent(String questName, String event)
	{
		final Quest quest = ScriptData.getInstance().getQuest(questName);
		if (quest == null)
			return;
		
		final WorldObject object = World.getInstance().getObject(getLastQuestNpcObjectId());
		if (!(object instanceof Npc npc) || !_player.isIn3DRadius(npc, Npc.INTERACTION_DISTANCE))
			return;
		
		for (Quest script : npc.getTemplate().getEventQuests(EventHandler.TALKED))
		{
			if (!script.equals(quest))
				continue;
			
			quest.notifyEvent(event, npc, _player);
			break;
		}
	}
}