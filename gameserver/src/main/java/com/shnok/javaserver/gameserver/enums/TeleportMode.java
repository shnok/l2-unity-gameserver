package com.shnok.javaserver.gameserver.enums;

public enum TeleportMode
{
	NONE,
	ONE_TIME,
	FULL_TIME,
	CAMERA_MODE;
	
	public static final TeleportMode[] VALUES = values();
}