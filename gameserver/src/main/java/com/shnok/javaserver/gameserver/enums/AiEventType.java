package com.shnok.javaserver.gameserver.enums;

/**
 * This class contains each possible event that can happen to an actor.
 */
public enum AiEventType
{
	/** An action is required, due to a previous step being completed/aborted. The actor must think on next action. */
	THINK,
	/** The actor was attacked. It may start attack in response, or ignore this event if it already attacks someone. */
	ATTACKED,
	/** Increase/decrease aggression towards a target, or reduce global aggression if target is null. */
	AGGRESSION,
	/** The actor evaded an hit. */
	EVADED,
	/** The actor completed an action and is now ready to act. */
	FINISHED_ATTACK,
	/** The actor arrived to assigned location, or didn't need to move. */
	ARRIVED,
	/** The actor cannot move anymore due to obstacles. */
	ARRIVED_BLOCKED,
	/** Cancel the actor's current action execution, without changing the intention. */
	CANCEL,
	/** The actor died. */
	DEAD,
	/** The actor has finished a skill cast. */
	FINISHED_CASTING,
	/** The actor has finished sitting down */
	SAT_DOWN,
	/** The actor has finished standing up */
	STOOD_UP,
	/** The actor has finished to attack with a bow */
	FINISHED_ATTACK_BOW,
	/** The actor attack bow reuse tim has now ended */
	BOW_ATTACK_REUSED,
	/** The actor's owner is under attack */
	OWNER_ATTACKED,
	/** The actor has been teleported */
	TELEPORTED
}