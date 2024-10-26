package com.shnok.javaserver.gameserver.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.gameserver.enums.Paperdoll;

/**
 * A datatype used to store character selection screen informations.
 */
public class CharSelectSlot
{
	private static final CLogger LOGGER = new CLogger(CharSelectSlot.class.getName());
	
	private static final String RESTORE_PAPERDOLLS = "SELECT object_id,item_id,loc_data,enchant_level FROM items WHERE owner_id=? AND loc='PAPERDOLL'";
	
	private final int _objectId;
	private final String _name;
	
	private int _charId = 0x00030b7a;
	private long _exp = 0;
	private int _sp = 0;
	private int _clanId = 0;
	private int _race = 0;
	private int _classId = 0;
	private int _baseClassId = 0;
	private long _deleteTimer = 0L;
	private long _lastAccess = 0L;
	private int _face = 0;
	private int _hairStyle = 0;
	private int _hairColor = 0;
	private int _sex = 0;
	private int _level = 1;
	private int _maxHp = 0;
	private double _currentHp = 0;
	private int _maxMp = 0;
	private double _currentMp = 0;
	private final int[][] _paperdoll;
	private int _karma = 0;
	private int _pkKills = 0;
	private int _pvpKills = 0;
	private int _augmentationId = 0;
	private int _x = 0;
	private int _y = 0;
	private int _z = 0;
	private int _accessLevel = 0;
	
	public CharSelectSlot(int objectId, String name)
	{
		_objectId = objectId;
		_name = name;
		_paperdoll = restoreVisibleInventory(objectId);
	}
	
	public int getObjectId()
	{
		return _objectId;
	}
	
	public int getAccessLevel()
	{
		return _accessLevel;
	}
	
	public void setAccessLevel(int level)
	{
		_accessLevel = level;
	}
	
	public int getCharId()
	{
		return _charId;
	}
	
	public void setCharId(int charId)
	{
		_charId = charId;
	}
	
	public int getClanId()
	{
		return _clanId;
	}
	
	public void setClanId(int clanId)
	{
		_clanId = clanId;
	}
	
	public int getClassId()
	{
		return _classId;
	}
	
	public void setClassId(int classId)
	{
		_classId = classId;
	}
	
	public int getBaseClassId()
	{
		return _baseClassId;
	}
	
	public void setBaseClassId(int baseClassId)
	{
		_baseClassId = baseClassId;
	}
	
	public double getCurrentHp()
	{
		return _currentHp;
	}
	
	public void setCurrentHp(double currentHp)
	{
		_currentHp = currentHp;
	}
	
	public double getCurrentMp()
	{
		return _currentMp;
	}
	
	public void setCurrentMp(double currentMp)
	{
		_currentMp = currentMp;
	}
	
	public long getDeleteTimer()
	{
		return _deleteTimer;
	}
	
	public void setDeleteTimer(long deleteTimer)
	{
		_deleteTimer = deleteTimer;
	}
	
	public long getLastAccess()
	{
		return _lastAccess;
	}
	
	public void setLastAccess(long lastAccess)
	{
		_lastAccess = lastAccess;
	}
	
	public long getExp()
	{
		return _exp;
	}
	
	public void setExp(long exp)
	{
		_exp = exp;
	}
	
	public int getFace()
	{
		return _face;
	}
	
	public void setFace(int face)
	{
		_face = face;
	}
	
	public int getHairColor()
	{
		return _hairColor;
	}
	
	public void setHairColor(int hairColor)
	{
		_hairColor = hairColor;
	}
	
	public int getHairStyle()
	{
		return _hairStyle;
	}
	
	public void setHairStyle(int hairStyle)
	{
		_hairStyle = hairStyle;
	}
	
	public int getPaperdollObjectId(Paperdoll slot)
	{
		return _paperdoll[slot.getId()][0];
	}
	
	public int getPaperdollItemId(Paperdoll slot)
	{
		return _paperdoll[slot.getId()][1];
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	public void setLevel(int level)
	{
		_level = level;
	}
	
	public int getMaxHp()
	{
		return _maxHp;
	}
	
	public void setMaxHp(int maxHp)
	{
		_maxHp = maxHp;
	}
	
	public int getMaxMp()
	{
		return _maxMp;
	}
	
	public void setMaxMp(int maxMp)
	{
		_maxMp = maxMp;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getRace()
	{
		return _race;
	}
	
	public void setRace(int race)
	{
		_race = race;
	}
	
	public int getSex()
	{
		return _sex;
	}
	
	public void setSex(int sex)
	{
		_sex = sex;
	}
	
	public int getSp()
	{
		return _sp;
	}
	
	public void setSp(int sp)
	{
		_sp = sp;
	}
	
	public int getEnchantEffect()
	{
		return _paperdoll[Paperdoll.RHAND.getId()][2];
	}
	
	public int getKarma()
	{
		return _karma;
	}
	
	public void setKarma(int k)
	{
		_karma = k;
	}
	
	public int getAugmentationId()
	{
		return _augmentationId;
	}
	
	public void setAugmentationId(int augmentationId)
	{
		_augmentationId = augmentationId;
	}
	
	public int getPkKills()
	{
		return _pkKills;
	}
	
	public void setPkKills(int pkKills)
	{
		_pkKills = pkKills;
	}
	
	public int getPvpKills()
	{
		return _pvpKills;
	}
	
	public void setPvpKills(int pvpKills)
	{
		_pvpKills = pvpKills;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public void setX(int x)
	{
		_x = x;
	}
	
	public int getY()
	{
		return _y;
	}
	
	public void setY(int y)
	{
		_y = y;
	}
	
	public int getZ()
	{
		return _z;
	}
	
	public void setZ(int z)
	{
		_z = z;
	}
	
	private static int[][] restoreVisibleInventory(int objectId)
	{
		int[][] paperdoll = new int[0x12][3];
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_PAPERDOLLS))
		{
			ps.setInt(1, objectId);
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int slot = rs.getInt("loc_data");
					
					paperdoll[slot][0] = rs.getInt("object_id");
					paperdoll[slot][1] = rs.getInt("item_id");
					paperdoll[slot][2] = rs.getInt("enchant_level");
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore paperdolls for {}.", e, objectId);
		}
		return paperdoll;
	}
}