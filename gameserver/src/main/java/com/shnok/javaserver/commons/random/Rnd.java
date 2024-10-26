package com.shnok.javaserver.commons.random;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A central randomness provider. Currently all methods delegate to {@link ThreadLocalRandom}.
 */
public final class Rnd
{
	private Rnd()
	{
		throw new IllegalStateException("Utility class");
	}
	
	public static double nextDouble(double n)
	{
		return ThreadLocalRandom.current().nextDouble(n);
	}
	
	public static double nextDouble()
	{
		return ThreadLocalRandom.current().nextDouble();
	}
	
	public static double get(double n)
	{
		return nextDouble(n);
	}
	
	public static int nextInt(int n)
	{
		return ThreadLocalRandom.current().nextInt(n);
	}
	
	public static int nextInt()
	{
		return ThreadLocalRandom.current().nextInt();
	}
	
	public static int get(int n)
	{
		return nextInt(n);
	}
	
	public static int get(int min, int max)
	{
		return ThreadLocalRandom.current().nextInt(min, max == Integer.MAX_VALUE ? max : max + 1);
	}
	
	public static long nextLong(long n)
	{
		return ThreadLocalRandom.current().nextLong(n);
	}
	
	public static long nextLong()
	{
		return ThreadLocalRandom.current().nextLong();
	}
	
	public static long get(long n)
	{
		return nextLong(n);
	}
	
	public static long get(long min, long max)
	{
		return ThreadLocalRandom.current().nextLong(min, max == Long.MAX_VALUE ? max : max + 1L);
	}
	
	public static boolean calcChance(double applicableUnits, int totalUnits)
	{
		return applicableUnits > nextInt(totalUnits);
	}
	
	public static double nextGaussian()
	{
		return ThreadLocalRandom.current().nextGaussian();
	}
	
	public static boolean nextBoolean()
	{
		return ThreadLocalRandom.current().nextBoolean();
	}
	
	public static byte[] nextBytes(int count)
	{
		return nextBytes(new byte[count]);
	}
	
	public static byte[] nextBytes(byte[] array)
	{
		ThreadLocalRandom.current().nextBytes(array);
		return array;
	}
	
	/**
	 * Returns a randomly selected element taken from the given list.
	 * @param <T> type of list elements.
	 * @param list a list.
	 * @return a randomly selected element.
	 */
	public static final <T> T get(List<T> list)
	{
		if (list == null || list.isEmpty())
			return null;
		
		return list.get(get(list.size()));
	}
	
	/**
	 * Returns a randomly selected element taken from the given array.
	 * @param array an array.
	 * @return a randomly selected element.
	 */
	public static final int get(int[] array)
	{
		return array[get(array.length)];
	}
	
	/**
	 * Returns a randomly selected element taken from the given array.
	 * @param <T> type of array elements.
	 * @param array an array.
	 * @return a randomly selected element.
	 */
	public static final <T> T get(T[] array)
	{
		return array[get(array.length)];
	}
}