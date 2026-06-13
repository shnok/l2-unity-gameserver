package com.shnok.javaserver.commons.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A {@link ConcurrentHashMap} used to store solely {@link String} pairs.<br>
 * <br>
 * It is used for SQL data storage purpose, and allow {@link String}, boolean, int, long and double manipulation.
 */
public abstract class MemoSet extends ConcurrentHashMap<String, String>
{
	private static final long serialVersionUID = 1L;
	
	protected abstract void onSet(String key, String value);
	
	protected abstract void onUnset(String key);
	
	protected MemoSet()
	{
		super();
	}
	
	protected MemoSet(final int size)
	{
		super(size);
	}
	
	protected MemoSet(final Map<String, String> m)
	{
		super(m);
	}
	
	public final void set(final String key, final String value)
	{
		onSet(key, value);
		
		put(key, value);
	}
	
	public void set(final String key, final boolean value)
	{
		set(key, String.valueOf(value));
	}
	
	public void set(final String key, final int value)
	{
		set(key, String.valueOf(value));
	}
	
	public void set(final String key, final long value)
	{
		set(key, String.valueOf(value));
	}
	
	public void set(final String key, final double value)
	{
		set(key, String.valueOf(value));
	}
	
	public void set(final String key, final Enum<?> value)
	{
		set(key, String.valueOf(value));
	}
	
	public final void unset(String key)
	{
		onUnset(key);
		
		remove(key);
	}
	
	public boolean getBool(final String key)
	{
		final String val = get(key);
		if (val != null)
			return Boolean.parseBoolean(val);
		
		throw new IllegalArgumentException("MemoSet : Boolean value required, but found: " + val + " for key: " + key + ".");
	}
	
	public boolean getBool(final String key, final boolean defaultValue)
	{
		final String val = get(key);
		if (val != null)
			return Boolean.parseBoolean(val);
		
		return defaultValue;
	}
	
	public int getInteger(final String key)
	{
		final String val = get(key);
		if (val != null)
			return Integer.parseInt(val);
		
		throw new IllegalArgumentException("MemoSet : Integer value required, but found: " + val + " for key: " + key + ".");
	}
	
	public int getInteger(final String key, final int defaultValue)
	{
		final String val = get(key);
		if (val != null)
			return Integer.parseInt(val);
		
		return defaultValue;
	}
	
	public long getLong(final String key)
	{
		final String val = get(key);
		if (val != null)
			return Long.parseLong(val);
		
		throw new IllegalArgumentException("MemoSet : Long value required, but found: " + val + " for key: " + key + ".");
	}
	
	public long getLong(final String key, final long defaultValue)
	{
		final String val = get(key);
		if (val != null)
			return Long.parseLong(val);
		
		return defaultValue;
	}
	
	public double getDouble(final String key)
	{
		final String val = get(key);
		if (val != null)
			return Double.parseDouble(val);
		
		throw new IllegalArgumentException("MemoSet : Double value required, but found: " + val + " for key: " + key + ".");
	}
	
	public double getDouble(final String key, final double defaultValue)
	{
		final String val = get(key);
		if (val != null)
			return Double.parseDouble(val);
		
		return defaultValue;
	}
	
	public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass)
	{
		final String val = get(name);
		
		if (val != null)
			return Enum.valueOf(enumClass, val);
		
		throw new IllegalArgumentException("Enum value of type " + enumClass.getName() + " required, but found: " + val + ".");
	}
	
	public <E extends Enum<E>> E getEnum(final String name, final Class<E> enumClass, final E defaultValue)
	{
		final String val = get(name);
		
		if (val != null)
			return Enum.valueOf(enumClass, val);
		
		return defaultValue;
	}
}