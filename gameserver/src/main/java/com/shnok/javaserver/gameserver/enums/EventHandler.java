package com.shnok.javaserver.gameserver.enums;

/**
 * The enum listing all events type.
 */
public enum EventHandler
{
	/**
	 * An event triggered when a NPC abnormal status changes.
	 */
	ABNORMAL_STATUS_CHANGED(true),
	
	/**
	 * An event triggered when a NPC is attacked by someone.
	 */
	ATTACKED(true),
	
	/**
	 * An event triggered when a NPC ends an attack over someone.
	 */
	ATTACK_FINISHED(true),
	
	/**
	 * An event triggered when a NPC calls members of its clan upon attack.
	 */
	CLAN_ATTACKED(true),
	
	/**
	 * An event triggered when a NPC calls members of its clan upon own death.
	 */
	CLAN_DIED(true),
	
	/**
	 * An event triggered when a NPC is spawned or respawned.
	 */
	CREATED(true),
	
	/**
	 * An event triggered when a NPC decays.
	 */
	DECAYED(true),
	
	/**
	 * An event controlling the first dialog shown by NPCs when they are clicked.
	 */
	FIRST_TALK(false),
	
	/**
	 * An event triggered when a finishes a movement.
	 */
	MOVE_TO_FINISHED(true),
	
	/**
	 * An event triggered when a mob gets killed.
	 */
	MY_DYING(true),
	
	/**
	 * An event triggered when a NPC got nothing to do (peace mode).
	 */
	NO_DESIRE(true),
	
	/**
	 * An event triggered when a NPC is out of his territory.
	 */
	OUT_OF_TERRITORY(true),
	
	/**
	 * An event triggered when a NPC calls members of its party upon attack.
	 */
	PARTY_ATTACKED(true),
	
	/**
	 * An event triggered when a NPC calls members of its party upon own death.
	 */
	PARTY_DIED(true),
	
	/**
	 * An event triggered when a NPC picks up an item.
	 */
	PICKED_ITEM(true),
	
	/**
	 * An event controlling onTalk action from start npcs.
	 */
	QUEST_START(true),
	
	/**
	 * An event triggered when a NPC see another creature.
	 */
	SEE_CREATURE(true),
	
	/**
	 * An event triggered when a NPC see a specific item.
	 */
	SEE_ITEM(true),
	
	/**
	 * An event triggered when a NPC see a spell being casted.
	 */
	SEE_SPELL(true),
	
	/**
	 * An event triggered when a NPC is being spelled.
	 */
	SPELLED(true),
	
	/**
	 * An event triggered when a Door/Wall calls members of its clan upon attack.
	 */
	STATIC_OBJECT_CLAN_ATTACKED(true),
	
	/**
	 * An event controlling onTalk action from npcs participating in a quest.
	 */
	TALKED(true),
	
	/**
	 * An event triggered when a spell goes to the end, once casted. Used for exotic skills.
	 */
	USE_SKILL_FINISHED(true),
	
	/**
	 * An event triggered when a Creature exits a zone.
	 */
	ZONE_ENTER(true),
	
	/**
	 * An event triggered when a Creature exits a zone.
	 */
	ZONE_EXIT(true),
	
	/**
	 * An event triggered when an event script is called.
	 */
	SCRIPT_EVENT(true);
	
	private boolean _allowMultipleRegistration;
	
	EventHandler(boolean allowMultipleRegistration)
	{
		_allowMultipleRegistration = allowMultipleRegistration;
	}
	
	/**
	 * @return true if the {@link EventHandler} allows multiple registrations.
	 */
	public boolean isMultipleRegistrationAllowed()
	{
		return _allowMultipleRegistration;
	}
}