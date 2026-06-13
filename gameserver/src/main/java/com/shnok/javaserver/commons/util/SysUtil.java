package com.shnok.javaserver.commons.util;

/**
 * A class holding system oriented methods.
 */
public class SysUtil
{
	private SysUtil()
	{
		throw new IllegalStateException("Utility class");
	}
	
	private static final int MEBIOCTET = 1024 * 1024;
	
	/**
	 * @return the used amount of memory the JVM is using.
	 */
	public static long getUsedMemory()
	{
		return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / MEBIOCTET;
	}
	
	/**
	 * @return the maximum amount of memory the JVM can use.
	 */
	public static long getMaxMemory()
	{
		return Runtime.getRuntime().maxMemory() / MEBIOCTET;
	}
}