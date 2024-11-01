package com.shnok.javaserver.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;

public class ExProperties extends Properties
{
	private static final CLogger LOGGER = new CLogger(ExProperties.class.getName());
	
	private static final long serialVersionUID = 1L;
	private static final IntIntHolder[] NO_HOLDER = {};
	
	public static final String DEFAULT_DELIMITERS = "[\\s,;]+";
	
	public void load(final String fileName) throws IOException
	{
		load(new File(fileName));
	}
	
	public void load(final File file) throws IOException
	{
		try (InputStream is = new FileInputStream(file))
		{
			load(is);
		}
	}
	
	public boolean getProperty(final String name, final boolean defaultValue)
	{
		boolean val = defaultValue;
		
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
			val = Boolean.parseBoolean(value);
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public int getProperty(final String name, final int defaultValue)
	{
		int val = defaultValue;
		
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
			val = Integer.parseInt(value);
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public long getProperty(final String name, final long defaultValue)
	{
		long val = defaultValue;
		
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
			val = Long.parseLong(value);
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public double getProperty(final String name, final double defaultValue)
	{
		double val = defaultValue;
		
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
			val = Double.parseDouble(value);
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public String[] getProperty(final String name, final String[] defaultValue)
	{
		return getProperty(name, defaultValue, DEFAULT_DELIMITERS);
	}
	
	public String[] getProperty(final String name, final String[] defaultValue, final String delimiter)
	{
		String[] val = defaultValue;
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
			val = value.split(delimiter);
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public boolean[] getProperty(final String name, final boolean[] defaultValue)
	{
		return getProperty(name, defaultValue, DEFAULT_DELIMITERS);
	}
	
	public boolean[] getProperty(final String name, final boolean[] defaultValue, final String delimiter)
	{
		boolean[] val = defaultValue;
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
		{
			final String[] values = value.split(delimiter);
			val = new boolean[values.length];
			for (int i = 0; i < val.length; i++)
				val[i] = Boolean.parseBoolean(values[i]);
		}
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public int[] getProperty(final String name, final int[] defaultValue)
	{
		return getProperty(name, defaultValue, DEFAULT_DELIMITERS);
	}
	
	public int[] getProperty(final String name, final int[] defaultValue, final String delimiter)
	{
		int[] val = defaultValue;
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
		{
			final String[] values = value.split(delimiter);
			val = new int[values.length];
			for (int i = 0; i < val.length; i++)
				val[i] = Integer.parseInt(values[i]);
		}
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public long[] getProperty(final String name, final long[] defaultValue)
	{
		return getProperty(name, defaultValue, DEFAULT_DELIMITERS);
	}
	
	public long[] getProperty(final String name, final long[] defaultValue, final String delimiter)
	{
		long[] val = defaultValue;
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
		{
			final String[] values = value.split(delimiter);
			val = new long[values.length];
			for (int i = 0; i < val.length; i++)
				val[i] = Long.parseLong(values[i]);
		}
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	public double[] getProperty(final String name, final double[] defaultValue)
	{
		return getProperty(name, defaultValue, DEFAULT_DELIMITERS);
	}
	
	public double[] getProperty(final String name, final double[] defaultValue, final String delimiter)
	{
		double[] val = defaultValue;
		final String value;
		
		if ((value = super.getProperty(name, null)) != null)
		{
			final String[] values = value.split(delimiter);
			val = new double[values.length];
			for (int i = 0; i < val.length; i++)
				val[i] = Double.parseDouble(values[i]);
		}
		else
			LOGGER.warn("The following property key '{}' is missing. It will use default value '{}'.", name, defaultValue);
		
		return val;
	}
	
	/**
	 * @param key : the hashtable key.
	 * @param defaultValue : a default value.
	 * @return an {@link IntIntHolder} array consisting of parsed items.
	 */
	public final IntIntHolder[] parseIntIntList(String key, String defaultValue)
	{
		final String[] propertySplit = getProperty(key, defaultValue).split(";");
		if (propertySplit.length == 0)
			return NO_HOLDER;
		
		int i = 0;
		final IntIntHolder[] result = new IntIntHolder[propertySplit.length];
		for (String value : propertySplit)
		{
			final String[] valueSplit = value.split("-");
			if (valueSplit.length != 2)
			{
				LOGGER.warn("Error parsing entry '{}', it should be itemId-itemNumber.", key);
				return NO_HOLDER;
			}
			
			try
			{
				result[i] = new IntIntHolder(Integer.parseInt(valueSplit[0]), Integer.parseInt(valueSplit[1]));
			}
			catch (Exception e)
			{
				LOGGER.error("Error parsing entry '{}', one of the value isn't a number.", e, key);
				return NO_HOLDER;
			}
			
			i++;
		}
		return result;
	}
}