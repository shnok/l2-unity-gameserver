package com.shnok.javaserver.gameserver.enums;

/**
 * Enumeration of generic intentions of an actor.
 */
public enum IntentionType
{
	/** Move to target if too far, then attack it - may be ignored (another target, invalid zoning, etc). */
	ATTACK,
	/** Move to target if too far, then cast a spell. */
	CAST,
	/** Fake death. */
	FAKE_DEATH,
	/** Flee the target. */
	FLEE,
	/** Check target's movement and follow it. */
	FOLLOW,
	/** Stop all actions and do nothing. */
	IDLE,
	/** Move to target if too far, then interact. */
	INTERACT,
	/** Move to way point route. */
	MOVE_ROUTE,
	/** Move to another location. */
	MOVE_TO,
	/** Do nothing, used as a filler to schedule later desires. */
	NOTHING,
	/** Move to target if too far, then pick up the item. */
	PICK_UP,
	/** Rest (sit until attacked). */
	SIT,
	/** SocialAction call. */
	SOCIAL,
	/** Stand Up. */
	STAND,
	/** Use an Item. */
	USE_ITEM,
	/** Move around your actual location. */
	WANDER
}