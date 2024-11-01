package com.shnok.javaserver.gameserver.enums.skills;

public enum TriggerType
{
	// You are attacked by enemy
	ON_ATTACKED,
	// You are attacked by enemy - by hit
	ON_ATTACKED_HIT,
	// You hit an enemy - was crit
	ON_CRIT,
	// You hit an enemy
	ON_HIT,
	// You cast a skill - it was a magic one - good magic
	ON_MAGIC_GOOD,
	// You cast a skill - it was a magic one - offensive magic
	ON_MAGIC_OFFENSIVE
}