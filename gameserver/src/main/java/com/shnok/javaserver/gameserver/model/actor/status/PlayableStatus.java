package com.shnok.javaserver.gameserver.model.actor.status;

import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.data.xml.PlayerLevelData;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.zone.type.SwampZone;

public class PlayableStatus<T extends Playable> extends CreatureStatus<T>
{
	private long _exp = 0;
	private int _sp = 0;
	
	protected int _level = 1;
	
	public PlayableStatus(T actor)
	{
		super(actor);
	}
	
	@Override
	public int getLevel()
	{
		return _level;
	}
	
	public void setLevel(int value)
	{
		_level = value;
	}
	
	@Override
	public float getMoveSpeed()
	{
		// Get base value.
		float baseValue = getBaseMoveSpeed();
		
		// Calculate swamp area malus.
		if (_actor.isInsideZone(ZoneId.SWAMP))
		{
			final SwampZone zone = ZoneManager.getInstance().getZone(_actor, SwampZone.class);
			if (zone != null)
				baseValue *= (100 + zone.getMoveBonus()) / 100.0;
		}
		
		return (float) calcStat(Stats.RUN_SPEED, baseValue, null, null);
	}
	
	public long getExp()
	{
		return _exp;
	}
	
	public void setExp(long value)
	{
		_exp = value;
	}
	
	public int getSp()
	{
		return _sp;
	}
	
	public void setSp(int value)
	{
		_sp = value;
	}
	
	public boolean addExp(long value)
	{
		if ((getExp() + value) < 0)
			return true;
		
		if (getExp() + value >= PlayerLevelData.getInstance().getRequiredExpForHighestLevel())
			value = PlayerLevelData.getInstance().getRequiredExpForHighestLevel() - 1 - getExp();
		
		setExp(getExp() + value);
		
		byte level = 0;
		for (level = 1; level <= PlayerLevelData.getInstance().getMaxLevel(); level++)
		{
			if (getExp() >= getExpForLevel(level))
				continue;
			
			level--;
			break;
		}
		
		if (level != getLevel())
			addLevel((byte) (level - getLevel()));
		
		return true;
	}
	
	public boolean removeExp(long value)
	{
		if ((getExp() - value) < 0)
			value = getExp() - 1;
		
		setExp(getExp() - value);
		
		byte level = 0;
		for (level = 1; level <= PlayerLevelData.getInstance().getMaxLevel(); level++)
		{
			if (getExp() >= getExpForLevel(level))
				continue;
			
			level--;
			break;
		}
		
		if (level != getLevel())
			addLevel((byte) (level - getLevel()));
		
		return true;
	}
	
	public boolean addExpAndSp(long addToExp, int addToSp)
	{
		boolean expAdded = false;
		boolean spAdded = false;
		
		if (addToExp >= 0)
			expAdded = addExp(addToExp);
		
		if (addToSp >= 0)
			spAdded = addSp(addToSp);
		
		return expAdded || spAdded;
	}
	
	public boolean removeExpAndSp(long removeExp, int removeSp)
	{
		boolean expRemoved = false;
		boolean spRemoved = false;
		
		if (removeExp > 0)
			expRemoved = removeExp(removeExp);
		
		if (removeSp > 0)
			spRemoved = removeSp(removeSp);
		
		return expRemoved || spRemoved;
	}
	
	public boolean addLevel(byte value)
	{
		if (getLevel() + value > PlayerLevelData.getInstance().getRealMaxLevel())
		{
			if (getLevel() < PlayerLevelData.getInstance().getRealMaxLevel())
				value = (byte) (PlayerLevelData.getInstance().getRealMaxLevel() - getLevel());
			else
				return false;
		}
		
		boolean levelIncreased = (getLevel() + value > getLevel());
		value += getLevel();
		setLevel(value);
		
		// Sync up exp with current level
		if (getExp() >= getExpForLevel(getLevel() + 1) || getExpForLevel(getLevel()) > getExp())
			setExp(getExpForLevel(getLevel()));
		
		if (!levelIncreased)
			return false;
		
		setMaxHpMp();
		
		return true;
	}
	
	public boolean addSp(int value)
	{
		if (value < 0)
			return false;
		
		int currentSp = getSp();
		if (currentSp == Integer.MAX_VALUE)
			return false;
		
		if (currentSp > Integer.MAX_VALUE - value)
			value = Integer.MAX_VALUE - currentSp;
		
		setSp(currentSp + value);
		return true;
	}
	
	public boolean removeSp(int value)
	{
		setSp(Math.max(0, getSp() - value));
		return true;
	}
	
	public long getExpForLevel(int level)
	{
		return 0;
	}
	
	public long getExpForThisLevel()
	{
		return 0;
	}
	
	public long getExpForNextLevel()
	{
		return 0;
	}
}