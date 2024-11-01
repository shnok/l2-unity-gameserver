package com.shnok.javaserver.gameserver.network.loginserverpackets;

import java.nio.charset.StandardCharsets;

public abstract class LoginServerBasePacket
{
	private final byte[] _decrypt;
	private int _off;
	
	protected LoginServerBasePacket(byte[] decrypt)
	{
		_decrypt = decrypt;
		_off = 1; // skip packet type id
	}
	
	public int readD()
	{
		return (_decrypt[_off++] & 0xff) | ((_decrypt[_off++] & 0xff) << 8) | ((_decrypt[_off++] & 0xff) << 16) | ((_decrypt[_off++] & 0xff) << 24);
	}
	
	public int readC()
	{
		return _decrypt[_off++] & 0xff;
	}
	
	public int readH()
	{
		return (_decrypt[_off++] & 0xff) | ((_decrypt[_off++] & 0xff) << 8);
	}
	
	public double readF()
	{
		final long result = (_decrypt[_off++] & 0xffL) | ((_decrypt[_off++] & 0xffL) << 8) | ((_decrypt[_off++] & 0xffL) << 16) | ((_decrypt[_off++] & 0xffL) << 24) | ((_decrypt[_off++] & 0xffL) << 32) | ((_decrypt[_off++] & 0xffL) << 40) | ((_decrypt[_off++] & 0xffL) << 48) | ((_decrypt[_off++] & 0xffL) << 56);
		return Double.longBitsToDouble(result);
	}
	
	public String readS()
	{
		final int start = _off;
		
		// Find the null terminator in UTF-16LE encoding.
		int end = start;
		while (end < _decrypt.length - 1 && (_decrypt[end] != 0 || _decrypt[end + 1] != 0))
			end += 2;
		
		// Move the offset past the string and the null terminator.
		_off = end + 2;
		
		// Create a string from the bytes between start and end.
		return new String(_decrypt, start, end - start, StandardCharsets.UTF_16LE);
	}
	
	public final byte[] readB(int length)
	{
		// Create a new byte array to hold the result.
		byte[] result = new byte[length];
		
		// Use System.arraycopy to efficiently copy the specified number of bytes from the _decrypt array starting at the current offset (_off) into the result array.
		System.arraycopy(_decrypt, _off, result, 0, length);
		
		// Update the offset (_off) by the length of the data read.
		_off += length;
		
		// Return the byte array containing the copied data.
		return result;
	}
}