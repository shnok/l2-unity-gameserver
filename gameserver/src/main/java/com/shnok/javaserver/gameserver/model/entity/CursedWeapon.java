package com.shnok.javaserver.gameserver.model.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.enums.MessageType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.Earthquake;
import com.shnok.javaserver.gameserver.network.serverpackets.ExRedSky;
import com.shnok.javaserver.gameserver.network.serverpackets.SkillList;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.UserInfo;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * One of these swords can drop from any mob. But only one instance of each sword can exist in the world. When a cursed sword drops, the world becomes red for several seconds, the ground shakes, and there's also an announcement as a system message that a cursed sword is found.<br>
 * <br>
 * The owner automatically becomes chaotic and their HP/CP/MP are fully restored.<br>
 * <br>
 * A cursed sword is equipped automatically when it's found, and the owner doesn't have an option to unequip it, to drop it or to destroy it. With a cursed sword you get some special skills.<br>
 * <br>
 * The cursed swords disappear after a certain period of time, and it doesn't matter how much time the owner spends online. This period of time is reduced if the owner kills another player, but the abilities of the sword increase. However, the owner needs to kill at least one player per day,
 * otherwise the sword disappears in 24 hours. There will be system messages about how much lifetime the sword has and when last murder was committed.<br>
 * <br>
 * If the owner dies, the sword either disappears or drops. When the sword is gone, the owner gains back their skills and characteristics go back to normal.
 */
public class CursedWeapon
{
	private static final CLogger LOGGER = new CLogger(CursedWeapon.class.getName());
	
	private static final String LOAD_CW = "SELECT * FROM cursed_weapons WHERE itemId=?";
	private static final String DELETE_ITEM = "DELETE FROM items WHERE owner_id=? AND item_id=?";
	private static final String UPDATE_PLAYER = "UPDATE characters SET karma=?, pkkills=? WHERE obj_id=?";
	private static final String INSERT_CW = "INSERT INTO cursed_weapons (itemId, playerId, playerKarma, playerPkKills, nbKills, currentStage, numberBeforeNextStage, hungryTime, endTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String DELETE_CW = "DELETE FROM cursed_weapons WHERE itemId = ?";
	private static final String UPDATE_CW = "UPDATE cursed_weapons SET nbKills=?, currentStage=?, numberBeforeNextStage=?, hungryTime=?, endTime=? WHERE itemId=?";
	
	private final String _name;
	
	protected final int _itemId;
	private ItemInstance _item = null;
	
	private int _playerId = 0;
	protected Player _player = null;
	
	// Skill id and max level. Max level is took from skillid (allow custom skills).
	private final int _skillId;
	private final int _skillMaxLevel;
	
	// Drop rate (when a mob is killed) and chance of dissapear (when a CW owner dies).
	private int _dropRate;
	private int _dissapearChance;
	
	// Overall duration (in hours) and hungry - used for daily task - duration (in hours)
	private int _duration;
	private int _durationLost;
	
	// Basic number used to calculate next number of needed victims for a stage (50% to 150% the given value).
	private int _stageKills;
	
	private boolean _isDropped = false;
	private boolean _isActivated = false;
	
	private ScheduledFuture<?> _overallTimer;
	private ScheduledFuture<?> _dailyTimer;
	private ScheduledFuture<?> _dropTimer;
	
	private int _playerKarma = 0;
	private int _playerPkKills = 0;
	
	// Number of current killed, current stage of weapon (1 by default, max is _skillMaxLevel), and number of victims needed for next stage.
	protected int _nbKills = 0;
	protected int _currentStage = 1;
	protected int _numberBeforeNextStage = 0;
	
	// Hungry timer (in minutes) and overall end timer (in ms).
	protected int _hungryTime = 0;
	protected long _endTime = 0;
	
	public CursedWeapon(StatSet set)
	{
		_name = set.getString("name");
		_itemId = set.getInteger("id");
		_skillId = set.getInteger("skillId");
		_dropRate = set.getInteger("dropRate");
		_dissapearChance = set.getInteger("dissapearChance");
		_duration = set.getInteger("duration");
		_durationLost = set.getInteger("durationLost");
		_stageKills = set.getInteger("stageKills");
		
		_skillMaxLevel = SkillTable.getInstance().getMaxLevel(_skillId);
		
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(LOAD_CW))
			{
				ps.setInt(1, _itemId);
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
					{
						_playerId = rs.getInt("playerId");
						_playerKarma = rs.getInt("playerKarma");
						_playerPkKills = rs.getInt("playerPkKills");
						_nbKills = rs.getInt("nbKills");
						_currentStage = rs.getInt("currentStage");
						_numberBeforeNextStage = rs.getInt("numberBeforeNextStage");
						_hungryTime = rs.getInt("hungryTime");
						_endTime = rs.getLong("endTime");
						
						reActivate(false);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore cursed weapons data.", e);
		}
	}
	
	public void setPlayer(Player player)
	{
		_player = player;
	}
	
	public void setItem(ItemInstance item)
	{
		_item = item;
	}
	
	public boolean isActivated()
	{
		return _isActivated;
	}
	
	public boolean isDropped()
	{
		return _isDropped;
	}
	
	public long getEndTime()
	{
		return _endTime;
	}
	
	public long getDuration()
	{
		return _duration;
	}
	
	public int getDurationLost()
	{
		return _durationLost;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public int getSkillId()
	{
		return _skillId;
	}
	
	public int getPlayerId()
	{
		return _playerId;
	}
	
	public Player getPlayer()
	{
		return _player;
	}
	
	public int getPlayerKarma()
	{
		return _playerKarma;
	}
	
	public int getPlayerPkKills()
	{
		return _playerPkKills;
	}
	
	public int getNbKills()
	{
		return _nbKills;
	}
	
	public int getStageKills()
	{
		return _stageKills;
	}
	
	public boolean isActive()
	{
		return _isActivated || _isDropped;
	}
	
	public long getTimeLeft()
	{
		return _endTime - System.currentTimeMillis();
	}
	
	public int getCurrentStage()
	{
		return _currentStage;
	}
	
	public int getNumberBeforeNextStage()
	{
		return _numberBeforeNextStage;
	}
	
	public int getHungryTime()
	{
		return _hungryTime;
	}
	
	/**
	 * This method is used to destroy a {@link CursedWeapon}.<br>
	 * It manages following states :
	 * <ul>
	 * <li><u>item on a online player</u> : drops the cursed weapon from inventory, and set back ancient pk/karma values.</li>
	 * <li><u>item on a offline player</u> : make SQL operations in order to drop item from database.</li>
	 * <li><u>item on ground</u> : destroys the item directly.</li>
	 * </ul>
	 * For all cases, a message is broadcasted, and the different states are reinitialized.
	 */
	public void endOfLife()
	{
		if (_isActivated)
		{
			// Player is online ; unequip weapon && destroy it.
			if (_player != null && _player.isOnline())
			{
				LOGGER.info("{} is being removed online.", _name);
				
				_player.getAttack().stop();
				
				_player.setKarma(_playerKarma);
				_player.setPkKills(_playerPkKills);
				_player.setCursedWeaponEquippedId(0);
				removeDemonicSkills();
				
				// Unequip && remove.
				_player.useEquippableItem(_item, true);
				_player.destroyItemByItemId(_itemId, 1, false);
				
				_player.broadcastUserInfo();
				
				_player.store();
			}
			// Player is offline ; make only SQL operations.
			else
			{
				LOGGER.info("{} is being removed offline.", _name);
				
				try (Connection con = ConnectionPool.getConnection())
				{
					// Delete the item
					try (PreparedStatement ps = con.prepareStatement(DELETE_ITEM))
					{
						ps.setInt(1, _playerId);
						ps.setInt(2, _itemId);
					}
					
					// Restore the karma and PK kills.
					try (PreparedStatement ps = con.prepareStatement(UPDATE_PLAYER))
					{
						ps.setInt(1, _playerKarma);
						ps.setInt(2, _playerPkKills);
						ps.setInt(3, _playerId);
					}
				}
				catch (Exception e)
				{
					LOGGER.error("Couldn't cleanup {} from offline player {}.", e, _name, _playerId);
				}
			}
		}
		else
		{
			// This CW is in the inventory of someone who has another cursed weapon equipped.
			if (_player != null && _player.getInventory().getItemByItemId(_itemId) != null)
			{
				_player.destroyItemByItemId(_itemId, 1, false);
				LOGGER.info("{} has been assimilated.", _name);
			}
			// This CW is on the ground.
			else if (_item != null)
			{
				_item.decayMe();
				LOGGER.info("{} has been removed from world.", _name);
			}
		}
		
		// Drop tasks.
		cancelDailyTimer();
		cancelOverallTimer();
		cancelDropTimer();
		
		// Delete infos from table, if any.
		removeFromDb();
		
		// Inform all ppl.
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_DISAPPEARED).addItemName(_itemId));
		
		// Reset state.
		_player = null;
		_item = null;
		
		_isActivated = false;
		_isDropped = false;
		
		_nbKills = 0;
		_currentStage = 1;
		_numberBeforeNextStage = 0;
		
		_hungryTime = 0;
		_endTime = 0;
		
		_playerId = 0;
		_playerKarma = 0;
		_playerPkKills = 0;
	}
	
	private void cancelDailyTimer()
	{
		if (_dailyTimer != null)
		{
			_dailyTimer.cancel(false);
			_dailyTimer = null;
		}
	}
	
	private void cancelOverallTimer()
	{
		if (_overallTimer != null)
		{
			_overallTimer.cancel(false);
			_overallTimer = null;
		}
	}
	
	private void cancelDropTimer()
	{
		if (_dropTimer != null)
		{
			_dropTimer.cancel(false);
			_dropTimer = null;
		}
	}
	
	/**
	 * This method is used to drop the {@link CursedWeapon} from its {@link Player} owner.<br>
	 * It drops the item on ground, and reset player stats and skills. Finally it broadcasts a message to all online players.
	 * @param killer : The creature who killed the cursed weapon owner.
	 */
	private void dropFromPlayer(Creature killer)
	{
		_player.getAttack().stop();
		
		// Prevent item from being removed by ItemsAutoDestroy.
		_item.setDestroyProtected(true);
		_player.dropItem(_item, true);
		
		_isActivated = false;
		_isDropped = true;
		
		_player.setKarma(_playerKarma);
		_player.setPkKills(_playerPkKills);
		_player.setCursedWeaponEquippedId(0);
		removeDemonicSkills();
		
		// Cancel the daily timer. It will be reactivated when someone will pickup the weapon.
		cancelDailyTimer();
		
		// Activate the "1h dropped CW" timer.
		_dropTimer = ThreadPool.schedule(new DropTimer(), 3600000L);
		
		// Reset current stage to 1.
		_currentStage = 1;
		
		// Drop infos from database.
		removeFromDb();
		
		// Broadcast a message to all online players.
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.S2_WAS_DROPPED_IN_THE_S1_REGION).addZoneName(_player.getPosition()).addItemName(_itemId));
	}
	
	/**
	 * This method is used to drop the {@link CursedWeapon} from a {@link Attackable} monster.<br>
	 * It drops the item on ground, and broadcast earthquake && red sky animations. Finally it broadcasts a message to all online players.
	 * @param attackable : The monster who dropped the cursed weapon.
	 * @param player : The player who killed the monster.
	 */
	private void dropFromMob(Attackable attackable, Player player)
	{
		_isActivated = false;
		
		// Create item and drop it.
		_item = ItemInstance.create(_itemId, 1);
		_item.setDestroyProtected(true);
		_item.dropMe(attackable, 70);
		
		// RedSky and Earthquake
		World.toAllOnlinePlayers(new ExRedSky(10));
		World.toAllOnlinePlayers(new Earthquake(_item, 14, 3));
		
		_isDropped = true;
		
		// Broadcast a message to all online players.
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.S2_WAS_DROPPED_IN_THE_S1_REGION).addZoneName(player.getPosition()).addItemName(_itemId));
	}
	
	/**
	 * Method used to send messages :<br>
	 * <ul>
	 * <li>one is broadcasted to warn players than {@link CursedWeapon} owner is online.</li>
	 * <li>the other shows left timer for the cursed weapon owner (either in hours or minutes).</li>
	 * </ul>
	 */
	public void cursedOnLogin()
	{
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S2_OWNER_HAS_LOGGED_INTO_THE_S1_REGION);
		sm.addZoneName(_player.getPosition());
		sm.addItemName(_player.getCursedWeaponEquippedId());
		World.toAllOnlinePlayers(sm);
		
		final int timeLeft = (int) (getTimeLeft() / 60000);
		if (timeLeft > 60)
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S2_HOUR_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
			sm.addItemName(_player.getCursedWeaponEquippedId());
			sm.addNumber(Math.round(timeLeft / 60F));
		}
		else
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
			sm.addItemName(_player.getCursedWeaponEquippedId());
			sm.addNumber(timeLeft);
		}
		_player.sendPacket(sm);
	}
	
	/**
	 * Rebind the passive skill belonging to the {@link CursedWeapon} owner. Invoke this method if the weapon owner switches to a subclass.
	 */
	public void giveDemonicSkills()
	{
		final L2Skill skill = SkillTable.getInstance().getInfo(_skillId, _currentStage);
		if (skill != null)
		{
			_player.addSkill(skill, false);
			_player.sendPacket(new SkillList(_player));
		}
	}
	
	private void removeDemonicSkills()
	{
		_player.removeSkill(_skillId, false);
		_player.sendPacket(new SkillList(_player));
	}
	
	/**
	 * Reactivate the {@link CursedWeapon}. It can be either coming from a player login, or a GM command.
	 * @param fromZero : if set to true, both _hungryTime and _endTime will be reseted to their default values.
	 */
	public void reActivate(boolean fromZero)
	{
		if (fromZero)
		{
			_hungryTime = _durationLost * 60;
			_endTime = (System.currentTimeMillis() + _duration * 3600000L);
			
			_overallTimer = ThreadPool.scheduleAtFixedRate(new OverallTimer(), 60000L, 60000L);
		}
		else
		{
			_isActivated = true;
			
			if (_endTime - System.currentTimeMillis() <= 0)
				endOfLife();
			else
			{
				_dailyTimer = ThreadPool.scheduleAtFixedRate(new DailyTimer(), 60000L, 60000L);
				_overallTimer = ThreadPool.scheduleAtFixedRate(new OverallTimer(), 60000L, 60000L);
			}
		}
	}
	
	/**
	 * Handles the drop rate of a {@link CursedWeapon}. If successful, launches the different associated tasks (end, overall and drop timers).
	 * @param attackable : The monster who drops the cursed weapon.
	 * @param player : The player who killed the monster.
	 * @return true if the drop rate is a success.
	 */
	public boolean checkDrop(Attackable attackable, Player player)
	{
		if (Rnd.get(1000000) < _dropRate)
		{
			// Drop the item.
			dropFromMob(attackable, player);
			
			// Start timers.
			_endTime = System.currentTimeMillis() + _duration * 3600000L;
			_overallTimer = ThreadPool.scheduleAtFixedRate(new OverallTimer(), 60000L, 60000L);
			_dropTimer = ThreadPool.schedule(new DropTimer(), 3600000L);
			
			return true;
		}
		return false;
	}
	
	/**
	 * Activate the {@link CursedWeapon}. We refresh {@link Player} owner, store related infos, save references, activate cursed weapon skills, expell him from the party (if any).<br>
	 * <br>
	 * Finally it broadcasts a message to all online players.
	 * @param player : The player who pickup the cursed weapon.
	 * @param item : The item used as reference.
	 */
	public void activate(Player player, ItemInstance item)
	{
		// If the Player is mounted, unmount him first.
		if (player.isMounted())
			player.dismount();
		
		_isActivated = true;
		
		// Hold player data.
		_player = player;
		_playerId = _player.getObjectId();
		_playerKarma = _player.getKarma();
		_playerPkKills = _player.getPkKills();
		
		_item = item;
		
		// Generate a random number for next stage.
		_numberBeforeNextStage = Rnd.get((int) Math.round(_stageKills * 0.5), (int) Math.round(_stageKills * 1.5));
		
		// Renew hungry time.
		_hungryTime = _durationLost * 60;
		
		// Activate the daily timer.
		_dailyTimer = ThreadPool.scheduleAtFixedRate(new DailyTimer(), 60000L, 60000L);
		
		// Cancel the "1h dropped CW" timer.
		cancelDropTimer();
		
		// Save data on database.
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(INSERT_CW))
			{
				ps.setInt(1, _itemId);
				ps.setInt(2, _playerId);
				ps.setInt(3, _playerKarma);
				ps.setInt(4, _playerPkKills);
				ps.setInt(5, _nbKills);
				ps.setInt(6, _currentStage);
				ps.setInt(7, _numberBeforeNextStage);
				ps.setInt(8, _hungryTime);
				ps.setLong(9, _endTime);
				ps.executeUpdate();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to insert cursed weapon data.", e);
		}
		
		// Change player stats
		_player.setCursedWeaponEquippedId(_itemId);
		_player.setKarma(9999999);
		_player.setPkKills(0);
		
		if (_player.isInParty())
			_player.getParty().removePartyMember(_player, MessageType.EXPELLED);
		
		// Disable active toggles
		for (AbstractEffect effect : _player.getAllEffects())
		{
			if (effect.getSkill().isToggle())
				effect.exit();
		}
		
		// Add CW skills
		giveDemonicSkills();
		
		// Equip the weapon
		_player.useEquippableItem(_item, true);
		
		// Fully heal player
		_player.getStatus().setMaxCpHpMp();
		
		// Refresh player stats
		_player.broadcastUserInfo();
		
		World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.THE_OWNER_OF_S2_HAS_APPEARED_IN_THE_S1_REGION).addZoneName(_player.getPosition()).addItemName(_item.getItemId()));
	}
	
	/**
	 * Drop dynamic infos regarding {@link CursedWeapon} for the given itemId. Used in endOfLife() method.
	 */
	private void removeFromDb()
	{
		try (Connection con = ConnectionPool.getConnection())
		{
			try (PreparedStatement ps = con.prepareStatement(DELETE_CW))
			{
				ps.setInt(1, _itemId);
				ps.executeUpdate();
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to remove cursed weapon data.", e);
		}
	}
	
	/**
	 * This method checks if the {@link CursedWeapon} is dropped or simply dissapears.
	 * @param killer : The killer of cursed weapon owner.
	 */
	public void dropIt(Creature killer)
	{
		// Remove it
		if (Rnd.get(100) <= _dissapearChance)
			endOfLife();
		// Unequip & Drop
		else
			dropFromPlayer(killer);
	}
	
	/**
	 * Increase the number of kills. If actual counter reaches the number generated to reach next stage, than rank up the {@link CursedWeapon}.
	 */
	public void increaseKills()
	{
		if (_player != null && _player.isOnline())
		{
			_nbKills++;
			_hungryTime = _durationLost * 60;
			
			_player.setPkKills(_player.getPkKills() + 1);
			_player.sendPacket(new UserInfo(_player));
			
			// If current number of kills is >= to the given number, than rankUp the weapon.
			if (_nbKills >= _numberBeforeNextStage)
			{
				// Reset the number of kills to 0.
				_nbKills = 0;
				
				// Setup the new random number.
				_numberBeforeNextStage = Rnd.get((int) Math.round(_stageKills * 0.5), (int) Math.round(_stageKills * 1.5));
				
				// Rank up the CW.
				rankUp();
			}
		}
	}
	
	/**
	 * This method is used to rank up a CW.
	 */
	public void rankUp()
	{
		if (_currentStage >= _skillMaxLevel)
			return;
		
		// Rank up current stage.
		_currentStage++;
		
		// Reward skills for that CW.
		giveDemonicSkills();
		
		// Send level up animation.
		_player.broadcastPacket(new SocialAction(_player, 17));
	}
	
	public void teleportTo(Player player)
	{
		if (player == null)
			return;
		
		// Go to player holding the weapon
		if (_isActivated)
			player.teleportTo(_player.getX(), _player.getY(), _player.getZ(), 0);
		// Go to item on the ground
		else if (_isDropped)
			player.teleportTo(_item.getX(), _item.getY(), _item.getZ(), 0);
		else
			player.sendMessage(_name + " isn't in the world.");
	}
	
	public Location getWorldPosition()
	{
		if (_isActivated && _player != null)
			return _player.getPosition();
		
		if (_isDropped && _item != null)
			return _item.getPosition();
		
		return null;
	}
	
	private class DailyTimer implements Runnable
	{
		// Internal timer to delay messages to the next hour, instead of every minute.
		private int _timer = 0;
		
		protected DailyTimer()
		{
		}
		
		@Override
		public void run()
		{
			_hungryTime--;
			_timer++;
			
			if (_hungryTime <= 0)
				endOfLife();
			else if (_player != null && _player.isOnline() && _timer % 60 == 0)
			{
				SystemMessage msg;
				int timeLeft = (int) (getTimeLeft() / 60000);
				if (timeLeft > 60)
				{
					msg = SystemMessage.getSystemMessage(SystemMessageId.S2_HOUR_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
					msg.addItemName(_player.getCursedWeaponEquippedId());
					msg.addNumber(Math.round(timeLeft / 60F));
				}
				else
				{
					msg = SystemMessage.getSystemMessage(SystemMessageId.S2_MINUTE_OF_USAGE_TIME_ARE_LEFT_FOR_S1);
					msg.addItemName(_player.getCursedWeaponEquippedId());
					msg.addNumber(timeLeft);
				}
				_player.sendPacket(msg);
			}
		}
	}
	
	private class OverallTimer implements Runnable
	{
		protected OverallTimer()
		{
		}
		
		@Override
		public void run()
		{
			// Overall timer is reached, ends the life of CW.
			if (System.currentTimeMillis() >= _endTime)
				endOfLife();
			// Save data.
			else
			{
				try (Connection con = ConnectionPool.getConnection())
				{
					try (PreparedStatement ps = con.prepareStatement(UPDATE_CW))
					{
						ps.setInt(1, _nbKills);
						ps.setInt(2, _currentStage);
						ps.setInt(3, _numberBeforeNextStage);
						ps.setInt(4, _hungryTime);
						ps.setLong(5, _endTime);
						ps.setInt(6, _itemId);
						ps.executeUpdate();
					}
				}
				catch (Exception e)
				{
					LOGGER.error("Failed to update cursed weapon data.", e);
				}
			}
		}
	}
	
	private class DropTimer implements Runnable
	{
		protected DropTimer()
		{
		}
		
		@Override
		public void run()
		{
			if (isDropped())
				endOfLife();
		}
	}
}