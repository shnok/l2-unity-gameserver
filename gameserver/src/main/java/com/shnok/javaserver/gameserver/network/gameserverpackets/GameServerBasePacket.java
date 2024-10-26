package com.shnok.javaserver.gameserver.network.gameserverpackets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class GameServerBasePacket
{
	private final ByteArrayOutputStream _bao;
	
	protected GameServerBasePacket()
	{
		_bao = new ByteArrayOutputStream();
	}
	
	protected void writeD(int value)
	{
		_bao.write(value & 0xFF);
		_bao.write(value >> 8 & 0xFF);
		_bao.write(value >> 16 & 0xFF);
		_bao.write(value >> 24 & 0xFF);
	}
	
	protected void writeH(int value)
	{
		_bao.write(value & 0xFF);
		_bao.write(value >> 8 & 0xFF);
	}
	
	protected void writeC(int value)
	{
		_bao.write(value & 0xFF);
	}
	
	protected void writeF(double org)
	{
		long value = Double.doubleToRawLongBits(org);
		_bao.write((int) (value & 0xff));
		_bao.write((int) (value >> 8 & 0xFF));
		_bao.write((int) (value >> 16 & 0xFF));
		_bao.write((int) (value >> 24 & 0xFF));
		_bao.write((int) (value >> 32 & 0xFF));
		_bao.write((int) (value >> 40 & 0xFF));
		_bao.write((int) (value >> 48 & 0xFF));
		_bao.write((int) (value >> 56 & 0xFF));
	}
	
	protected void writeS(String text)
	{
		if (text != null && !text.isEmpty())
		{
			try
			{
				_bao.write(text.getBytes(StandardCharsets.UTF_16LE));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		// Write the null terminator.
		_bao.write(0);
		_bao.write(0);
	}
	
	protected void writeB(byte[] array)
	{
		if (array != null && array.length > 0)
		{
			try
			{
				_bao.write(array);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public int getLength()
	{
		return _bao.size() + 2;
	}
	
	public byte[] getBytes()
	{
		// Reserve 4 bytes for a checksum (currently just writing 0).
		writeD(0x00);
		
		// Padding to ensure the byte array size is a multiple of 8.
		int padding = _bao.size() % 8;
		if (padding != 0)
		{
			for (int i = padding; i < 8; i++)
				writeC(0x00);
		}
		
		return _bao.toByteArray();
	}
	
	public abstract byte[] getContent() throws IOException;
}