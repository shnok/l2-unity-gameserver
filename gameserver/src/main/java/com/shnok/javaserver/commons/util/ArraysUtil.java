package com.shnok.javaserver.commons.util;

import java.util.Arrays;

public class ArraysUtil
{
	private ArraysUtil()
	{
		throw new IllegalStateException("Utility class");
	}
	
	public static final int[] EMPTY_INT_ARRAY = {};
	
	/**
	 * @param <T> : The Object type.
	 * @param array : the array to look into.
	 * @return {@code true} if the array is empty or null.
	 */
	public static <T> boolean isEmpty(T[] array)
	{
		return array == null || array.length == 0;
	}
	
	/**
	 * @param <T> : The Object type.
	 * @param array : the array to look into.
	 * @param obj : the object to search for.
	 * @return {@code true} if the array contains the object, {@code false} otherwise.
	 */
	public static <T> boolean contains(T[] array, T obj)
	{
		if (array == null || array.length == 0)
			return false;
		
		for (T element : array)
			if (element.equals(obj))
				return true;
			
		return false;
	}
	
	/**
	 * @param <T> : The Object type.
	 * @param array1 : the array to look into.
	 * @param array2 : the array to search for.
	 * @return {@code true} if both arrays contains a similar value.
	 */
	public static <T> boolean contains(T[] array1, T[] array2)
	{
		if (array1 == null || array1.length == 0)
			return false;
		
		if (array2 == null || array2.length == 0)
			return false;
		
		for (T element1 : array1)
		{
			for (T element2 : array2)
				if (element2.equals(element1))
					return true;
		}
		return false;
	}
	
	/**
	 * @param array : the array to look into.
	 * @param obj : the integer to search for.
	 * @return {@code true} if the array contains the integer, {@code false} otherwise.
	 */
	public static boolean contains(int[] array, int obj)
	{
		if (array == null || array.length == 0)
			return false;
		
		for (int element : array)
			if (element == obj)
				return true;
			
		return false;
	}
	
	/**
	 * Concat two arrays of the same type into a single array.
	 * @param <T> : The Object type.
	 * @param first : The initial array used as recipient.
	 * @param second : The second array to merge.
	 * @return an array of the given type, holding informations of passed arrays.
	 */
	public static <T> T[] concat(T[] first, T[] second)
	{
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	/**
	 * Concat multiple arrays of the same type into a single array.
	 * @param <T> : The Object type.
	 * @param first : The initial array used as recipient.
	 * @param rest : An array vararg.
	 * @return an array of the given type, holding informations of passed arrays.
	 */
	@SafeVarargs
	public static <T> T[] concatAll(T[] first, T[]... rest)
	{
		int totalLength = first.length;
		for (T[] array : rest)
			totalLength += array.length;
		
		T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		for (T[] array : rest)
		{
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}
}