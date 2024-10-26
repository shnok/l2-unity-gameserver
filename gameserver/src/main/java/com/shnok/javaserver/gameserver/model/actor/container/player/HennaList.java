package com.shnok.javaserver.gameserver.model.actor.container.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.xml.HennaData;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.HennaType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Henna;

/**
 * This class handles dyes (or {@link Henna}s) of a {@link Player}.
 */
public class HennaList
{
	private static final CLogger LOGGER = new CLogger(HennaList.class.getName());
	
	private static final int MAX_HENNA_STAT_VALUE = 5;
	private static final int HENNA_FIRST_SLOT_ID = 1;
	
	private static final int MAX_HENNAS_AMOUNT = 3;
	
	private final Player _owner;
	private final Henna[] _hennas = new Henna[MAX_HENNAS_AMOUNT];
	private final int[] _stats = new int[HennaType.values().length];
	
	public HennaList(Player owner)
	{
		_owner = owner;
	}
	
	/**
	 * Recalculate stats of existing {@link Henna}s.<br>
	 * <br>
	 * A stat can't be superior to 5. Previous stats are dumped.
	 */
	public void recalculateStats()
	{
		for (int i = 0; i < _stats.length; i++)
			_stats[i] = 0;
		
		for (Henna henna : _hennas)
		{
			if (henna == null || !henna.canBeUsedBy(_owner))
				continue;
			
			_stats[0] += henna.INT();
			_stats[1] += henna.STR();
			_stats[2] += henna.CON();
			_stats[3] += henna.MEN();
			_stats[4] += henna.DEX();
			_stats[5] += henna.WIT();
		}
		
		for (int i = 0; i < _stats.length; i++)
			_stats[i] = Math.min(_stats[i], MAX_HENNA_STAT_VALUE);
	}
	
	/**
	 * @param henna : The {@link Henna} to test.
	 * @return The index of an existing {@link Henna}, or -1 if not found.
	 */
	private int indexOf(Henna henna)
	{
		if (henna == null)
			return -1;
		
		for (int i = 0; i < _hennas.length; i++)
		{
			final Henna h = _hennas[i];
			if (h != null && h.symbolId() == henna.symbolId())
				return i;
		}
		return -1;
	}
	
	/**
	 * @return The first empty slot on this {@link HennaList}.
	 */
	private int getEmptySlotIndex()
	{
		for (int i = 0; i < getMaxSize(); i++)
		{
			if (_hennas[i] == null)
				return i;
		}
		return -1;
	}
	
	/**
	 * @return The {@link List} of available {@link Henna}s.
	 */
	public List<Henna> getHennas()
	{
		return Arrays.stream(_hennas).filter(Objects::nonNull).toList();
	}
	
	/**
	 * @param type : The {@link HennaType} we use as reference.
	 * @return The stat value based on an {@link HennaType}, which is between +5 and -15.
	 */
	public int getStat(HennaType type)
	{
		return _stats[type.ordinal()];
	}
	
	/**
	 * Restore the {@link Henna}s associated to this {@link HennaList}, based on {@link Player} class index.
	 */
	public void restore()
	{
		final Henna[] hennas = new Henna[MAX_HENNAS_AMOUNT];
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT slot, symbol_id FROM character_hennas WHERE char_obj_id = ? AND class_index = ?"))
		{
			ps.setInt(1, _owner.getObjectId());
			ps.setInt(2, _owner.getClassIndex());
			
			try (ResultSet rs = ps.executeQuery())
			{
				while (rs.next())
				{
					final int slot = rs.getInt("slot");
					final int symbolId = rs.getInt("symbol_id");
					
					if (slot < HENNA_FIRST_SLOT_ID || slot > HENNA_FIRST_SLOT_ID + MAX_HENNAS_AMOUNT)
					{
						LOGGER.warn("{} has Henna on invalid slot {}.", _owner.toString(), slot);
						continue;
					}
					
					final Henna henna = HennaData.getInstance().getHenna(symbolId);
					if (henna == null)
					{
						LOGGER.warn("{} has unknown Henna Symbol Id: {} in slot {}.", _owner.toString(), symbolId, slot);
						continue;
					}
					
					hennas[slot - HENNA_FIRST_SLOT_ID] = henna;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't restore hennas.", e);
		}
		
		System.arraycopy(hennas, 0, _hennas, 0, hennas.length);
	}
	
	/**
	 * @return True if all slots of this {@link HennaList} are empty, false otherwise.
	 */
	public boolean isEmpty()
	{
		for (Henna h : _hennas)
		{
			if (h != null)
				return false;
		}
		return true;
	}
	
	/**
	 * @return The current amount of equipped {@link Henna}s on this {@link HennaList}.
	 */
	public int size()
	{
		int result = 0;
		for (int i = 0; i < _hennas.length; i++)
		{
			if (_hennas[i] != null)
				result++;
		}
		return result;
	}
	
	/**
	 * @param symbolId : The symbol id used as reference.
	 * @return The {@link Henna} associated to a symbol id, or null if not found.
	 */
	public Henna getBySymbolId(int symbolId)
	{
		for (Henna h : _hennas)
		{
			if (h != null && h.symbolId() == symbolId)
				return h;
		}
		return null;
	}
	
	/**
	 * @return True if the {@link HennaList} is entirely filled, based on maximum amount of {@link Henna}s this {@link Player} can equip.
	 */
	public boolean isFull()
	{
		return getEmptySlotsAmount() <= 0;
	}
	
	/**
	 * @return The number of empty slots of this {@link HennaList}, based on maximum amount of {@link Henna}s this {@link Player} can equip.
	 */
	public int getEmptySlotsAmount()
	{
		int usedSlots = size();
		int maxSlots = getMaxSize();
		
		return Math.max(maxSlots - usedSlots, 0);
	}
	
	/**
	 * @return The maximum amount of {@link Henna}s this {@link Player} can equip.
	 */
	public int getMaxSize()
	{
		final ClassId classId = _owner.getClassId();
		if (classId.getLevel() < 1)
			return 0;
		
		if (classId.getLevel() == 1)
			return 2;
		
		return MAX_HENNAS_AMOUNT;
	}
	
	/**
	 * @param henna : The {@link Henna} to add.
	 * @return True if the {@link Henna} could be added, false otherwise.
	 */
	public boolean add(Henna henna)
	{
		final int slot = getEmptySlotIndex();
		if (slot < 0)
			return false;
		
		_hennas[slot] = henna;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("INSERT INTO character_hennas (char_obj_id,symbol_id,slot,class_index) VALUES (?,?,?,?)"))
		{
			ps.setInt(1, _owner.getObjectId());
			ps.setInt(2, henna.symbolId());
			ps.setInt(3, slot + HENNA_FIRST_SLOT_ID);
			ps.setInt(4, _owner.getClassIndex());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't add Henna.", e);
		}
		
		recalculateStats();
		return true;
	}
	
	/**
	 * @param henna : The {@link Henna} to remove.
	 * @return True if the {@link Henna} could be removed, false otherwise.
	 */
	public boolean remove(Henna henna)
	{
		final int slot = indexOf(henna);
		if (slot < 0)
			return false;
		
		_hennas[slot] = null;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement("DELETE FROM character_hennas WHERE char_obj_id=? AND slot=? AND class_index=?"))
		{
			ps.setInt(1, _owner.getObjectId());
			ps.setInt(2, slot + HENNA_FIRST_SLOT_ID);
			ps.setInt(3, _owner.getClassIndex());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't remove Henna.", e);
		}
		
		recalculateStats();
		
		return true;
	}
	
	/**
	 * @param henna : The {@link Henna} used as reference.
	 * @return True if the tested {@link Henna} can be used by the {@link Player}, false otherwise.
	 */
	public boolean canBeUsedBy(Henna henna)
	{
		return henna.canBeUsedBy(_owner);
	}
	
	@Override
	public String toString()
	{
		return "HennaList{" + "_owner=" + _owner.toString() + ", _hennas=" + Arrays.toString(_hennas) + ", _stats=" + Arrays.toString(_stats) + '}';
	}
}