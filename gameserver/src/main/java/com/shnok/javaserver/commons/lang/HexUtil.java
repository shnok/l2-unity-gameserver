package com.shnok.javaserver.commons.lang;

public class HexUtil
{
	private HexUtil()
	{
		throw new IllegalStateException("Utility class");
	}
	
	/**
	 * Convert the given byte array into a formatted hexadecimal string.<br>
	 * <br>
	 * The output format includes both the hex representation and the ASCII equivalent. Each line of output represents 16 bytes.
	 * @param data : The byte array to be converted.
	 * @param len : The number of bytes from the array to be processed.
	 * @return A {@link String} with the hexadecimal and ASCII representation of the byte array.
	 */
	public static String printData(byte[] data, int len)
	{
		StringBuilder result = new StringBuilder();
		
		// Calculate the number of full 16-byte lines and any remaining bytes.
		int fullLines = len / 16;
		int remainingBytes = len % 16;
		
		// Process each full 16-byte line.
		for (int line = 0; line < fullLines; line++)
		{
			int offset = line * 16;
			
			result.append(fillHex(offset, 4)).append(": ");
			appendHexBytes(result, data, offset, 16);
			
			appendAsciiRepresentation(result, data, offset, 16);
			result.append("\n");
		}
		
		// Process any remaining bytes that do not form a full 16-byte line.
		if (remainingBytes > 0)
		{
			int offset = fullLines * 16;
			
			result.append(fillHex(offset, 4)).append(": ");
			appendHexBytes(result, data, offset, remainingBytes);
			
			// Padding to align ASCII output.
			result.append("   ".repeat(16 - remainingBytes));
			
			appendAsciiRepresentation(result, data, offset, remainingBytes);
			result.append("\n");
		}
		
		return result.toString();
	}
	
	/**
	 * Append the hexadecimal representation of the specified bytes to the result.
	 * @param result : The {@link StringBuilder} to append the hex bytes to.
	 * @param data : The byte array containing the data.
	 * @param offset : The starting index in the byte array.
	 * @param length : The number of bytes to process.
	 */
	private static void appendHexBytes(StringBuilder result, byte[] data, int offset, int length)
	{
		for (int i = 0; i < length; i++)
			result.append(fillHex(data[offset + i] & 0xFF, 2)).append(" ");
	}
	
	/**
	 * Append the ASCII representation of the specified bytes to the result. Non-printable characters are represented by a dot.
	 * @param result : The {@link StringBuilder} to append the ASCII characters to.
	 * @param data : The byte array containing the data.
	 * @param offset : The starting index in the byte array.
	 * @param length : The number of bytes to process.
	 */
	private static void appendAsciiRepresentation(StringBuilder result, byte[] data, int offset, int length)
	{
		for (int i = 0; i < length; i++)
		{
			int value = data[offset + i] & 0xFF;
			
			// Printable ASCII range.
			if (value >= 0x20 && value <= 0x7E)
				result.append((char) value);
			else
				result.append('.');
		}
	}
	
	/**
	 * Convert the given integer into a zero-padded hexadecimal string.
	 * @param data : The integer to convert.
	 * @param digits : The minimum number of hexadecimal digits to produce.
	 * @return A zero-padded hexadecimal string representing the integer.
	 */
	public static String fillHex(int data, int digits)
	{
		return String.format("%0" + digits + "x", data);
	}
	
	/**
	 * Convert the entire byte array into a formatted hexadecimal string.
	 * @param raw : The byte array to be converted.
	 * @return A {@link String} with the hexadecimal and ASCII representation of the entire byte array.
	 */
	public static String printData(byte[] raw)
	{
		return printData(raw, raw.length);
	}
}