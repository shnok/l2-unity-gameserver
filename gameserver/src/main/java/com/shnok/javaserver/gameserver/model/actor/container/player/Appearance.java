package com.shnok.javaserver.gameserver.model.actor.container.player;

import net.sf.l2j.gameserver.enums.actors.Sex;

public final class Appearance
{
	private byte _face;
	private byte _hairColor;
	private byte _hairStyle;
	private Sex _sex;
	private boolean _isVisible = true;
	private int _nameColor = 0xFFFFFF;
	private int _titleColor = 0xFFFF77;
	
	public Appearance(byte face, byte hColor, byte hStyle, Sex sex)
	{
		_face = face;
		_hairColor = hColor;
		_hairStyle = hStyle;
		_sex = sex;
	}
	
	public byte getFace()
	{
		return _face;
	}
	
	public void setFace(int value)
	{
		_face = (byte) value;
	}
	
	public byte getHairColor()
	{
		return _hairColor;
	}
	
	public void setHairColor(int value)
	{
		_hairColor = (byte) value;
	}
	
	public byte getHairStyle()
	{
		return _hairStyle;
	}
	
	public void setHairStyle(int value)
	{
		_hairStyle = (byte) value;
	}
	
	public Sex getSex()
	{
		return _sex;
	}
	
	public void setSex(Sex sex)
	{
		_sex = sex;
	}
	
	public boolean isVisible()
	{
		return _isVisible;
	}
	
	public void setVisible(boolean val)
	{
		_isVisible = val;
	}
	
	public int getNameColor()
	{
		return _nameColor;
	}
	
	public void setNameColor(int nameColor)
	{
		_nameColor = nameColor;
	}
	
	public void setNameColor(int red, int green, int blue)
	{
		_nameColor = (red & 0xFF) + ((green & 0xFF) << 8) + ((blue & 0xFF) << 16);
	}
	
	public int getTitleColor()
	{
		return _titleColor;
	}
	
	public void setTitleColor(int titleColor)
	{
		_titleColor = titleColor;
	}
	
	public void setTitleColor(int red, int green, int blue)
	{
		_titleColor = (red & 0xFF) + ((green & 0xFF) << 8) + ((blue & 0xFF) << 16);
	}
}