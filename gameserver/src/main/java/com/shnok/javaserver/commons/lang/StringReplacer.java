package com.shnok.javaserver.commons.lang;

/**
 * This class is used as a counter to replaceAll. It avoids to generate Strings and is roughly 50% faster than string method format().
 * @author xblx
 */
public final class StringReplacer
{
	private static final String DELIM_STR = "{}";
	
	private final StringBuilder _sb;
	
	public StringReplacer(String source)
	{
		_sb = new StringBuilder(source);
	}
	
	/**
	 * Replace all occurences of a String for another String.
	 * @param pattern : The String to replace.
	 * @param replacement : The new String.
	 */
	public final void replaceAll(String pattern, String replacement)
	{
		int point;
		while ((point = _sb.indexOf(pattern)) != -1)
			_sb.replace(point, point + pattern.length(), replacement);
	}
	
	/**
	 * Replace all delimiters '{}' by the String representation of any objects. Important things to note:
	 * <ul>
	 * <li>If there isn't enough parameters, then the leftover isn't processed.</li>
	 * <li>If there is too much parameters, the loop breaks when it doesn't find anything to replace.</li>
	 * <li>If the object is null, then it sends "null".
	 * </ul>
	 * @param args : The objects to pass.
	 */
	public final void replaceAll(Object... args)
	{
		int index;
		int newIndex = 0;
		
		for (Object obj : args)
		{
			index = _sb.indexOf(DELIM_STR, newIndex);
			if (index == -1)
				break;
			
			newIndex = index + 2;
			_sb.replace(index, newIndex, (obj == null) ? null : obj.toString());
		}
	}
	
	/**
	 * Replace the first occurence of a String for another String.
	 * @param pattern : The String to replace.
	 * @param replacement : The new String.
	 */
	public final void replaceFirst(String pattern, String replacement)
	{
		final int point = _sb.indexOf(pattern);
		_sb.replace(point, point + pattern.length(), replacement);
	}
	
	/**
	 * Replace the last occurence of a String for another String.
	 * @param pattern : The String to replace.
	 * @param replacement : The new String.
	 */
	public final void replaceLast(String pattern, String replacement)
	{
		final int point = _sb.lastIndexOf(pattern);
		_sb.replace(point, point + pattern.length(), replacement);
	}
	
	@Override
	public String toString()
	{
		return _sb.toString();
	}
}