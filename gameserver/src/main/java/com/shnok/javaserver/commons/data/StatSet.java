package com.shnok.javaserver.commons.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.NpcStringId;

/**
 * This class, extending {@link HashMap}, is used to store pairs :
 * <ul>
 * <li>The key is a {@link String} ;</li>
 * <li>The value is any type of {@link Object}. Complex {@link Object}s such as {@link List}, {@link Map}, arrays or even {@link IntIntHolder} can also be stored.</li>
 * </ul>
 */
@SuppressWarnings("serial")
public class StatSet extends HashMap<String, Object>
{
	public StatSet()
	{
		super();
	}
	
	public StatSet(final int size)
	{
		super(size);
	}
	
	public StatSet(final StatSet set)
	{
		super(set);
	}
	
	public void set(final String key, final Object value)
	{
		put(key, value);
	}
	
	public void set(final String key, final String value)
	{
		put(key, value);
	}
	
	public void set(final String key, final boolean value)
	{
		put(key, (value) ? Boolean.TRUE : Boolean.FALSE);
	}
	
	public void set(final String key, final int value)
	{
		put(key, value);
	}
	
	public void set(final String key, final int[] value)
	{
		put(key, value);
	}
	
	public void set(final String key, final long value)
	{
		put(key, value);
	}
	
	public void set(final String key, final double value)
	{
		put(key, value);
	}
	
	public void set(final String key, final Enum<?> value)
	{
		put(key, value);
	}
	
	public void unset(final String key)
	{
		remove(key);
	}
	
	public boolean getBool(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Boolean bool)
			return bool;
		
		if (val instanceof String string)
			return Boolean.parseBoolean(string);
		
		if (val instanceof Number number)
			return number.intValue() != 0;
		
		throw new IllegalArgumentException("StatSet : Boolean value required, but found: " + val + " for key: " + key + ".");
	}
	
	public boolean getBool(final String key, final boolean defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Boolean bool)
			return bool;
		
		if (val instanceof String string)
			return Boolean.parseBoolean(string);
		
		if (val instanceof Number number)
			return number.intValue() != 0;
		
		return defaultValue;
	}
	
	public byte getByte(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.byteValue();
		
		if (val instanceof String string)
			return Byte.parseByte(string);
		
		throw new IllegalArgumentException("StatSet : Byte value required, but found: " + val + " for key: " + key + ".");
	}
	
	public byte getByte(final String key, final byte defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.byteValue();
		
		if (val instanceof String string)
			return Byte.parseByte(string);
		
		return defaultValue;
	}
	
	public double getDouble(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.doubleValue();
		
		if (val instanceof String string)
			return Double.parseDouble(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1. : 0.;
		
		throw new IllegalArgumentException("StatSet : Double value required, but found: " + val + " for key: " + key + ".");
	}
	
	public double getDouble(final String key, final double defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.doubleValue();
		
		if (val instanceof String string)
			return Double.parseDouble(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1. : 0.;
		
		return defaultValue;
	}
	
	public double[] getDoubleArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof double[] array)
			return array;
		
		if (val instanceof Number number)
			return new double[]
			{
				number.doubleValue()
			};
		
		if (val instanceof String string)
			return Stream.of(string.split(";")).mapToDouble(Double::parseDouble).toArray();
		
		throw new IllegalArgumentException("StatSet : Double array required, but found: " + val + " for key: " + key + ".");
	}
	
	public float getFloat(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.floatValue();
		
		if (val instanceof String string)
			return Float.parseFloat(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1 : 0;
		
		throw new IllegalArgumentException("StatSet : Float value required, but found: " + val + " for key: " + key + ".");
	}
	
	public float getFloat(final String key, final float defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.floatValue();
		
		if (val instanceof String string)
			return Float.parseFloat(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1 : 0;
		
		return defaultValue;
	}
	
	public int getInteger(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.intValue();
		
		if (val instanceof String string)
			return Integer.parseInt(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1 : 0;
		
		throw new IllegalArgumentException("StatSet : Integer value required, but found: " + val + " for key: " + key + ".");
	}
	
	public int getInteger(final String key, final int defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.intValue();
		
		if (val instanceof String string)
			return Integer.parseInt(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1 : 0;
		
		return defaultValue;
	}
	
	public int[] getIntegerArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof int[] array)
			return array;
		
		if (val instanceof Number number)
			return new int[]
			{
				number.intValue()
			};
		
		if (val instanceof String string)
			return Stream.of(string.split(";")).mapToInt(Integer::parseInt).toArray();
		
		throw new IllegalArgumentException("StatSet : Integer array required, but found: " + val + " for key: " + key + ".");
	}
	
	public int[] getIntegerArray(final String key, final int[] defaultArray)
	{
		try
		{
			return getIntegerArray(key);
		}
		catch (IllegalArgumentException e)
		{
			return defaultArray;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getList(final String key)
	{
		final Object val = get(key);
		
		if (val == null)
			return Collections.emptyList();
		
		return (List<T>) val;
	}
	
	public long getLong(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.longValue();
		
		if (val instanceof String string)
			return Long.parseLong(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1L : 0L;
		
		throw new IllegalArgumentException("StatSet : Long value required, but found: " + val + " for key: " + key + ".");
	}
	
	public long getLong(final String key, final long defaultValue)
	{
		final Object val = get(key);
		
		if (val instanceof Number number)
			return number.longValue();
		
		if (val instanceof String string)
			return Long.parseLong(string);
		
		if (val instanceof Boolean bool)
			return (Boolean.TRUE.equals((bool))) ? 1L : 0L;
		
		return defaultValue;
	}
	
	public long[] getLongArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof long[] array)
			return array;
		
		if (val instanceof Number number)
			return new long[]
			{
				number.longValue()
			};
		
		if (val instanceof String string)
			return Stream.of(string.split(";")).mapToLong(Long::parseLong).toArray();
		
		throw new IllegalArgumentException("StatSet : Long array required, but found: " + val + " for key: " + key + ".");
	}
	
	@SuppressWarnings("unchecked")
	public <T, U> Map<T, U> getMap(final String key)
	{
		final Object val = get(key);
		
		if (val == null)
			return Collections.emptyMap();
		
		return (Map<T, U>) val;
	}
	
	public String getString(final String key)
	{
		final Object val = get(key);
		
		if (val != null)
			return String.valueOf(val);
		
		throw new IllegalArgumentException("StatSet : String value required, but unspecified for key: " + key + ".");
	}
	
	public String getString(final String key, final String defaultValue)
	{
		final Object val = get(key);
		
		if (val != null)
			return String.valueOf(val);
		
		return defaultValue;
	}
	
	public String[] getStringArray(final String key, final String[] defaultArray)
	{
		try
		{
			return getStringArray(key);
		}
		catch (Exception e)
		{
			return defaultArray;
		}
	}
	
	public String[] getStringArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String[] array)
			return array;
		
		if (val instanceof String string)
			return string.split(";");
		
		throw new IllegalArgumentException("StatSet : String array required, but found: " + val + " for key: " + key + ".");
	}
	
	public Location getLocation(final String key, final Location defaultLoc)
	{
		try
		{
			return getLocation(key);
		}
		catch (Exception e)
		{
			return defaultLoc;
		}
	}
	
	public Location getLocation(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof Location loc)
			return loc;
		
		if (val instanceof String string)
		{
			final int[] arr = Arrays.stream(string.split(";")).map(String::trim).mapToInt(Integer::parseInt).toArray();
			return new Location(arr[0], arr[1], arr[2]);
		}
		
		throw new IllegalArgumentException("StatSet : Location required, but found: " + val + " for key: " + key + ".");
	}
	
	public IntIntHolder getIntIntHolder(final String key, final IntIntHolder defaultHolder)
	{
		try
		{
			return getIntIntHolder(key);
		}
		catch (Exception e)
		{
			return defaultHolder;
		}
	}
	
	public IntIntHolder getIntIntHolder(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String[] array)
			return new IntIntHolder(Integer.parseInt(array[0]), Integer.parseInt(array[1]));
		
		if (val instanceof String string)
		{
			final String[] toSplit = string.split("-");
			return new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1]));
		}
		
		throw new IllegalArgumentException("StatSet : int-int (IntIntHolder) required, but found: " + val + " for key: " + key + ".");
	}
	
	public IntIntHolder[] getIntIntHolderArray(final String key, final IntIntHolder[] defaultHolderArray)
	{
		try
		{
			return getIntIntHolderArray(key);
		}
		catch (Exception e)
		{
			return defaultHolderArray;
		}
	}
	
	public IntIntHolder[] getIntIntHolderArray(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String[] array)
		{
			final IntIntHolder[] tempArray = new IntIntHolder[array.length];
			
			int index = 0;
			for (String splitted : array)
			{
				final String[] splittedHolder = splitted.split("-");
				tempArray[index++] = new IntIntHolder(Integer.parseInt(splittedHolder[0]), Integer.parseInt(splittedHolder[1]));
			}
			
			return tempArray;
		}
		
		if (val instanceof String string)
		{
			// String exists, but it is empty : return empty array.
			if (string.isEmpty())
				return new IntIntHolder[0];
			
			// Single entry ; return the entry under array form.
			if (!string.contains(";"))
			{
				final String[] toSplit = string.split("-");
				final IntIntHolder[] tempArray = new IntIntHolder[1];
				tempArray[0] = new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1]));
				return tempArray;
			}
			
			final String[] toSplit = string.split(";");
			
			final IntIntHolder[] tempArray = new IntIntHolder[toSplit.length];
			
			int index = 0;
			for (String splitted : toSplit)
			{
				final String[] splittedHolder = splitted.split("-");
				tempArray[index++] = new IntIntHolder(Integer.parseInt(splittedHolder[0]), Integer.parseInt(splittedHolder[1]));
			}
			
			return tempArray;
		}
		
		throw new IllegalArgumentException("StatSet : int-int;int-int (int[] IntIntHolder) required, but found: " + val + " for key: " + key + ".");
	}
	
	public List<IntIntHolder> getIntIntHolderList(final String key)
	{
		final Object val = get(key);
		
		if (val instanceof String string)
		{
			// String exists, but it empty : return a generic empty List.
			if (string.isEmpty())
				return Collections.emptyList();
			
			// Single entry ; return the entry under List form.
			if (!string.contains(";"))
			{
				final String[] toSplit = string.split("-");
				return Arrays.asList(new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1])));
			}
			
			// First split is using ";", second is using "-". Exemple : 1234-12;1234-12.
			final String[] entries = string.split(";");
			final List<IntIntHolder> list = new ArrayList<>(entries.length);
			
			// Feed the List.
			for (String entry : entries)
			{
				final String[] toSplit = entry.split("-");
				list.add(new IntIntHolder(Integer.parseInt(toSplit[0]), Integer.parseInt(toSplit[1])));
			}
			
			return list;
		}
		
		throw new IllegalArgumentException("StatSet : int-int;int-int (List<IntIntHolder>) required, but found: " + val + " for key: " + key + ".");
	}
	
	public List<IntIntHolder> getIntIntHolderList(final String key, final List<IntIntHolder> defaultHolder)
	{
		try
		{
			return getIntIntHolderList(key);
		}
		catch (IllegalArgumentException e)
		{
			return defaultHolder;
		}
	}
	
	@SuppressWarnings("unchecked")
	public <A> A getObject(final String key, final Class<A> type)
	{
		final Object val = get(key);
		
		if (val == null || !type.isAssignableFrom(val.getClass()))
			return null;
		
		return (A) val;
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass)
	{
		final Object val = get(name);
		
		if (val != null && enumClass.isInstance(val))
			return (E) val;
		
		if (val instanceof String string)
			return Enum.valueOf(enumClass, string);
		
		throw new IllegalArgumentException("StatSet : Enum value of type " + enumClass.getName() + " required, but found: " + val + ".");
	}
	
	@SuppressWarnings("unchecked")
	public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass, final E defaultValue)
	{
		final Object val = get(name);
		
		if (val != null && enumClass.isInstance(val))
			return (E) val;
		
		if (val instanceof String string)
			return Enum.valueOf(enumClass, string);
		
		return defaultValue;
	}
	
	public NpcStringId getNpcStringId(final String name)
	{
		return NpcStringId.get(getInteger(name));
	}
	
	public NpcStringId getNpcStringId(final String name, final NpcStringId defaultValue)
	{
		final int id = getInteger(name, 0);
		return (id > 0) ? NpcStringId.get(id) : defaultValue;
	}
}