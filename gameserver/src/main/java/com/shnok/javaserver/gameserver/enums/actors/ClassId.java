package com.shnok.javaserver.gameserver.enums.actors;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.shnok.javaserver.gameserver.model.actor.Player;

/**
 * This class defines all classes (ex : human fighter, darkFighter...) that a player can chose.
 * <ul>
 * <li>id : The Identifier of the class</li>
 * <li>isMage : True if the class is a mage class</li>
 * <li>race : The race of this class</li>
 * <li>parent : The parent ClassId or null if this class is the root</li>
 * </ul>
 */
public enum ClassId
{
	HUMAN_FIGHTER(ClassRace.HUMAN, ClassType.FIGHTER, 0, "Human Fighter", null),
	WARRIOR(ClassRace.HUMAN, ClassType.FIGHTER, 1, "Warrior", HUMAN_FIGHTER),
	GLADIATOR(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Gladiator", WARRIOR),
	WARLORD(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Warlord", WARRIOR),
	KNIGHT(ClassRace.HUMAN, ClassType.FIGHTER, 1, "Human Knight", HUMAN_FIGHTER),
	PALADIN(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Paladin", KNIGHT),
	DARK_AVENGER(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Dark Avenger", KNIGHT),
	ROGUE(ClassRace.HUMAN, ClassType.FIGHTER, 1, "Rogue", HUMAN_FIGHTER),
	TREASURE_HUNTER(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Treasure Hunter", ROGUE),
	HAWKEYE(ClassRace.HUMAN, ClassType.FIGHTER, 2, "Hawkeye", ROGUE),
	
	HUMAN_MYSTIC(ClassRace.HUMAN, ClassType.MYSTIC, 0, "Human Mystic", null),
	HUMAN_WIZARD(ClassRace.HUMAN, ClassType.MYSTIC, 1, "Human Wizard", HUMAN_MYSTIC),
	SORCERER(ClassRace.HUMAN, ClassType.MYSTIC, 2, "Sorcerer", HUMAN_WIZARD),
	NECROMANCER(ClassRace.HUMAN, ClassType.MYSTIC, 2, "Necromancer", HUMAN_WIZARD),
	WARLOCK(ClassRace.HUMAN, ClassType.MYSTIC, 2, "Warlock", HUMAN_WIZARD),
	CLERIC(ClassRace.HUMAN, ClassType.PRIEST, 1, "Cleric", HUMAN_MYSTIC),
	BISHOP(ClassRace.HUMAN, ClassType.PRIEST, 2, "Bishop", CLERIC),
	PROPHET(ClassRace.HUMAN, ClassType.PRIEST, 2, "Prophet", CLERIC),
	
	ELVEN_FIGHTER(ClassRace.ELF, ClassType.FIGHTER, 0, "Elven Fighter", null),
	ELVEN_KNIGHT(ClassRace.ELF, ClassType.FIGHTER, 1, "Elven Knight", ELVEN_FIGHTER),
	TEMPLE_KNIGHT(ClassRace.ELF, ClassType.FIGHTER, 2, "Temple Knight", ELVEN_KNIGHT),
	SWORD_SINGER(ClassRace.ELF, ClassType.FIGHTER, 2, "Sword Singer", ELVEN_KNIGHT),
	ELVEN_SCOUT(ClassRace.ELF, ClassType.FIGHTER, 1, "Elven Scout", ELVEN_FIGHTER),
	PLAINS_WALKER(ClassRace.ELF, ClassType.FIGHTER, 2, "Plains Walker", ELVEN_SCOUT),
	SILVER_RANGER(ClassRace.ELF, ClassType.FIGHTER, 2, "Silver Ranger", ELVEN_SCOUT),
	
	ELVEN_MYSTIC(ClassRace.ELF, ClassType.MYSTIC, 0, "Elven Mystic", null),
	ELVEN_WIZARD(ClassRace.ELF, ClassType.MYSTIC, 1, "Elven Wizard", ELVEN_MYSTIC),
	SPELLSINGER(ClassRace.ELF, ClassType.MYSTIC, 2, "Spellsinger", ELVEN_WIZARD),
	ELEMENTAL_SUMMONER(ClassRace.ELF, ClassType.MYSTIC, 2, "Elemental Summoner", ELVEN_WIZARD),
	ELVEN_ORACLE(ClassRace.ELF, ClassType.PRIEST, 1, "Elven Oracle", ELVEN_MYSTIC),
	ELVEN_ELDER(ClassRace.ELF, ClassType.PRIEST, 2, "Elven Elder", ELVEN_ORACLE),
	
	DARK_FIGHTER(ClassRace.DARK_ELF, ClassType.FIGHTER, 0, "Dark Fighter", null),
	PALUS_KNIGHT(ClassRace.DARK_ELF, ClassType.FIGHTER, 1, "Palus Knight", DARK_FIGHTER),
	SHILLIEN_KNIGHT(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Shillien Knight", PALUS_KNIGHT),
	BLADEDANCER(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Bladedancer", PALUS_KNIGHT),
	ASSASSIN(ClassRace.DARK_ELF, ClassType.FIGHTER, 1, "Assassin", DARK_FIGHTER),
	ABYSS_WALKER(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Abyss Walker", ASSASSIN),
	PHANTOM_RANGER(ClassRace.DARK_ELF, ClassType.FIGHTER, 2, "Phantom Ranger", ASSASSIN),
	
	DARK_MYSTIC(ClassRace.DARK_ELF, ClassType.MYSTIC, 0, "Dark Mystic", null),
	DARK_WIZARD(ClassRace.DARK_ELF, ClassType.MYSTIC, 1, "Dark Wizard", DARK_MYSTIC),
	SPELLHOWLER(ClassRace.DARK_ELF, ClassType.MYSTIC, 2, "Spellhowler", DARK_WIZARD),
	PHANTOM_SUMMONER(ClassRace.DARK_ELF, ClassType.MYSTIC, 2, "Phantom Summoner", DARK_WIZARD),
	SHILLIEN_ORACLE(ClassRace.DARK_ELF, ClassType.PRIEST, 1, "Shillien Oracle", DARK_MYSTIC),
	SHILLIEN_ELDER(ClassRace.DARK_ELF, ClassType.PRIEST, 2, "Shillien Elder", SHILLIEN_ORACLE),
	
	ORC_FIGHTER(ClassRace.ORC, ClassType.FIGHTER, 0, "Orc Fighter", null),
	ORC_RAIDER(ClassRace.ORC, ClassType.FIGHTER, 1, "Orc Raider", ORC_FIGHTER),
	DESTROYER(ClassRace.ORC, ClassType.FIGHTER, 2, "Destroyer", ORC_RAIDER),
	MONK(ClassRace.ORC, ClassType.FIGHTER, 1, "Monk", ORC_FIGHTER),
	TYRANT(ClassRace.ORC, ClassType.FIGHTER, 2, "Tyrant", MONK),
	
	ORC_MYSTIC(ClassRace.ORC, ClassType.MYSTIC, 0, "Orc Mystic", null),
	ORC_SHAMAN(ClassRace.ORC, ClassType.MYSTIC, 1, "Orc Shaman", ORC_MYSTIC),
	OVERLORD(ClassRace.ORC, ClassType.MYSTIC, 2, "Overlord", ORC_SHAMAN),
	WARCRYER(ClassRace.ORC, ClassType.MYSTIC, 2, "Warcryer", ORC_SHAMAN),
	
	DWARVEN_FIGHTER(ClassRace.DWARF, ClassType.FIGHTER, 0, "Dwarven Fighter", null),
	SCAVENGER(ClassRace.DWARF, ClassType.FIGHTER, 1, "Scavenger", DWARVEN_FIGHTER),
	BOUNTY_HUNTER(ClassRace.DWARF, ClassType.FIGHTER, 2, "Bounty Hunter", SCAVENGER),
	ARTISAN(ClassRace.DWARF, ClassType.FIGHTER, 1, "Artisan", DWARVEN_FIGHTER),
	WARSMITH(ClassRace.DWARF, ClassType.FIGHTER, 2, "Warsmith", ARTISAN),
	
	DUMMY_1(null, null, -1, "dummy 1", null),
	DUMMY_2(null, null, -1, "dummy 2", null),
	DUMMY_3(null, null, -1, "dummy 3", null),
	DUMMY_4(null, null, -1, "dummy 4", null),
	DUMMY_5(null, null, -1, "dummy 5", null),
	DUMMY_6(null, null, -1, "dummy 6", null),
	DUMMY_7(null, null, -1, "dummy 7", null),
	DUMMY_8(null, null, -1, "dummy 8", null),
	DUMMY_9(null, null, -1, "dummy 9", null),
	DUMMY_10(null, null, -1, "dummy 10", null),
	DUMMY_11(null, null, -1, "dummy 11", null),
	DUMMY_12(null, null, -1, "dummy 12", null),
	DUMMY_13(null, null, -1, "dummy 13", null),
	DUMMY_14(null, null, -1, "dummy 14", null),
	DUMMY_15(null, null, -1, "dummy 15", null),
	DUMMY_16(null, null, -1, "dummy 16", null),
	DUMMY_17(null, null, -1, "dummy 17", null),
	DUMMY_18(null, null, -1, "dummy 18", null),
	DUMMY_19(null, null, -1, "dummy 19", null),
	DUMMY_20(null, null, -1, "dummy 20", null),
	DUMMY_21(null, null, -1, "dummy 21", null),
	DUMMY_22(null, null, -1, "dummy 22", null),
	DUMMY_23(null, null, -1, "dummy 23", null),
	DUMMY_24(null, null, -1, "dummy 24", null),
	DUMMY_25(null, null, -1, "dummy 25", null),
	DUMMY_26(null, null, -1, "dummy 26", null),
	DUMMY_27(null, null, -1, "dummy 27", null),
	DUMMY_28(null, null, -1, "dummy 28", null),
	DUMMY_29(null, null, -1, "dummy 29", null),
	DUMMY_30(null, null, -1, "dummy 30", null),
	
	DUELIST(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Duelist", GLADIATOR),
	DREADNOUGHT(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Dreadnought", WARLORD),
	PHOENIX_KNIGHT(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Phoenix Knight", PALADIN),
	HELL_KNIGHT(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Hell Knight", DARK_AVENGER),
	SAGGITARIUS(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Sagittarius", HAWKEYE),
	ADVENTURER(ClassRace.HUMAN, ClassType.FIGHTER, 3, "Adventurer", TREASURE_HUNTER),
	ARCHMAGE(ClassRace.HUMAN, ClassType.MYSTIC, 3, "Archmage", SORCERER),
	SOULTAKER(ClassRace.HUMAN, ClassType.MYSTIC, 3, "Soultaker", NECROMANCER),
	ARCANA_LORD(ClassRace.HUMAN, ClassType.MYSTIC, 3, "Arcana Lord", WARLOCK),
	CARDINAL(ClassRace.HUMAN, ClassType.PRIEST, 3, "Cardinal", BISHOP),
	HIEROPHANT(ClassRace.HUMAN, ClassType.PRIEST, 3, "Hierophant", PROPHET),
	
	EVAS_TEMPLAR(ClassRace.ELF, ClassType.FIGHTER, 3, "Eva's Templar", TEMPLE_KNIGHT),
	SWORD_MUSE(ClassRace.ELF, ClassType.FIGHTER, 3, "Sword Muse", SWORD_SINGER),
	WIND_RIDER(ClassRace.ELF, ClassType.FIGHTER, 3, "Wind Rider", PLAINS_WALKER),
	MOONLIGHT_SENTINEL(ClassRace.ELF, ClassType.FIGHTER, 3, "Moonlight Sentinel", SILVER_RANGER),
	MYSTIC_MUSE(ClassRace.ELF, ClassType.MYSTIC, 3, "Mystic Muse", SPELLSINGER),
	ELEMENTAL_MASTER(ClassRace.ELF, ClassType.MYSTIC, 3, "Elemental Master", ELEMENTAL_SUMMONER),
	EVAS_SAINT(ClassRace.ELF, ClassType.PRIEST, 3, "Eva's Saint", ELVEN_ELDER),
	
	SHILLIEN_TEMPLAR(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Shillien Templar", SHILLIEN_KNIGHT),
	SPECTRAL_DANCER(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Spectral Dancer", BLADEDANCER),
	GHOST_HUNTER(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Ghost Hunter", ABYSS_WALKER),
	GHOST_SENTINEL(ClassRace.DARK_ELF, ClassType.FIGHTER, 3, "Ghost Sentinel", PHANTOM_RANGER),
	STORM_SCREAMER(ClassRace.DARK_ELF, ClassType.MYSTIC, 3, "Storm Screamer", SPELLHOWLER),
	SPECTRAL_MASTER(ClassRace.DARK_ELF, ClassType.MYSTIC, 3, "Spectral Master", PHANTOM_SUMMONER),
	SHILLIEN_SAINT(ClassRace.DARK_ELF, ClassType.PRIEST, 3, "Shillien Saint", SHILLIEN_ELDER),
	
	TITAN(ClassRace.ORC, ClassType.FIGHTER, 3, "Titan", DESTROYER),
	GRAND_KHAVATARI(ClassRace.ORC, ClassType.FIGHTER, 3, "Grand Khavatari", TYRANT),
	DOMINATOR(ClassRace.ORC, ClassType.MYSTIC, 3, "Dominator", OVERLORD),
	DOOMCRYER(ClassRace.ORC, ClassType.MYSTIC, 3, "Doom Cryer", WARCRYER),
	
	FORTUNE_SEEKER(ClassRace.DWARF, ClassType.FIGHTER, 3, "Fortune Seeker", BOUNTY_HUNTER),
	MAESTRO(ClassRace.DWARF, ClassType.FIGHTER, 3, "Maestro", WARSMITH);
	
	public static final ClassId[] VALUES = values();
	
	private static final Set<ClassId> ATTACKER_GROUP = EnumSet.of(HUMAN_FIGHTER, WARRIOR, GLADIATOR, WARLORD, ROGUE, TREASURE_HUNTER, HAWKEYE, ELVEN_FIGHTER, ELVEN_SCOUT, PLAINS_WALKER, SILVER_RANGER, DARK_FIGHTER, ASSASSIN, ABYSS_WALKER, PHANTOM_RANGER, ORC_FIGHTER, ORC_RAIDER, DESTROYER, MONK, TYRANT, DWARVEN_FIGHTER, DUELIST, DREADNOUGHT, ADVENTURER, SAGGITARIUS, MOONLIGHT_SENTINEL, WIND_RIDER, GHOST_HUNTER, GHOST_SENTINEL, TITAN, GRAND_KHAVATARI);
	private static final Set<ClassId> FIGHTER_GROUP = EnumSet.of(HUMAN_FIGHTER, WARRIOR, GLADIATOR, WARLORD, KNIGHT, PALADIN, DARK_AVENGER, ROGUE, TREASURE_HUNTER, HAWKEYE, ELVEN_FIGHTER, ELVEN_KNIGHT, TEMPLE_KNIGHT, SWORD_SINGER, ELVEN_SCOUT, PLAINS_WALKER, SILVER_RANGER, DARK_FIGHTER, PALUS_KNIGHT, SHILLIEN_KNIGHT, BLADEDANCER, ASSASSIN, ABYSS_WALKER, PHANTOM_RANGER, ORC_FIGHTER, ORC_RAIDER, DESTROYER, MONK, TYRANT, DWARVEN_FIGHTER, SCAVENGER, BOUNTY_HUNTER, ARTISAN, WARSMITH, DUELIST, DREADNOUGHT, PHOENIX_KNIGHT, HELL_KNIGHT, SAGGITARIUS, ADVENTURER, EVAS_TEMPLAR, SWORD_MUSE, WIND_RIDER, MOONLIGHT_SENTINEL, SHILLIEN_TEMPLAR, SPECTRAL_DANCER, GHOST_HUNTER, GHOST_SENTINEL, TITAN, GRAND_KHAVATARI, FORTUNE_SEEKER, MAESTRO);
	private static final Set<ClassId> MAGE_GROUP = EnumSet.of(HUMAN_MYSTIC, HUMAN_WIZARD, SORCERER, NECROMANCER, WARLOCK, CLERIC, BISHOP, PROPHET, ELVEN_MYSTIC, ELVEN_WIZARD, SPELLSINGER, ELEMENTAL_SUMMONER, ELVEN_ORACLE, ELVEN_ELDER, DARK_MYSTIC, DARK_WIZARD, SPELLHOWLER, PHANTOM_SUMMONER, SHILLIEN_ORACLE, SHILLIEN_ELDER, ORC_MYSTIC, ORC_SHAMAN, OVERLORD, WARCRYER, ARCHMAGE, SOULTAKER, ARCANA_LORD, CARDINAL, HIEROPHANT, MYSTIC_MUSE, ELEMENTAL_MASTER, EVAS_SAINT, STORM_SCREAMER, SPECTRAL_MASTER, SHILLIEN_SAINT, DOMINATOR, DOOMCRYER);
	private static final Set<ClassId> WIZARD_GROUP = EnumSet.of(HUMAN_MYSTIC, HUMAN_WIZARD, SORCERER, NECROMANCER, WARLOCK, ELVEN_MYSTIC, ELVEN_WIZARD, SPELLSINGER, ELEMENTAL_SUMMONER, DARK_MYSTIC, DARK_WIZARD, SPELLHOWLER, PHANTOM_SUMMONER, ORC_MYSTIC, ORC_SHAMAN, OVERLORD, WARCRYER, ARCHMAGE, SOULTAKER, ARCANA_LORD, MYSTIC_MUSE, ELEMENTAL_MASTER, STORM_SCREAMER, SPECTRAL_MASTER, DOMINATOR, DOOMCRYER);
	private static final Set<ClassId> CLERIC_GROUP = EnumSet.of(HUMAN_MYSTIC, CLERIC, BISHOP, PROPHET, ELVEN_MYSTIC, ELVEN_ORACLE, ELVEN_ELDER, DARK_MYSTIC, SHILLIEN_ORACLE, SHILLIEN_ELDER, CARDINAL, HIEROPHANT, EVAS_SAINT);
	private static final Set<ClassId> SUBJOB_KNIGHT_GROUP = EnumSet.of(PALADIN, DARK_AVENGER, SHILLIEN_KNIGHT, TEMPLE_KNIGHT, PHOENIX_KNIGHT, HELL_KNIGHT, SHILLIEN_TEMPLAR, EVAS_TEMPLAR);
	
	private final int _id;
	private final ClassRace _race;
	private final ClassType _type;
	private final int _level;
	private final String _name;
	private final ClassId _parent;
	private EnumSet<ClassId> _subclasses;
	
	private ClassId(ClassRace race, ClassType type, int level, String name, ClassId parent)
	{
		_id = ordinal();
		_race = race;
		_type = type;
		_level = level;
		_name = name;
		_parent = parent;
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
	
	/**
	 * @return The associated id of this {@link ClassId}.
	 */
	public final int getId()
	{
		return _id;
	}
	
	/**
	 * @return The associated {@link ClassRace} of this {@link ClassId}.
	 */
	public final ClassRace getRace()
	{
		return _race;
	}
	
	/**
	 * @return The associated {@link ClassType} of this {@link ClassId}.
	 */
	public final ClassType getType()
	{
		return _type;
	}
	
	/**
	 * @return The level of this {@link ClassId} (0=base, 1=1st class, 2=2nd class, 3=3rd class).
	 */
	public final int getLevel()
	{
		return _level;
	}
	
	/**
	 * @return The parent {@link ClassId} of this {@link ClassId}, or null if not set.
	 */
	public final ClassId getParent()
	{
		return _parent;
	}
	
	/**
	 * @param classId : The parent {@link ClassId} to check.
	 * @return True if this {@link ClassId} is a child of the selected {@link ClassId}.
	 */
	public final boolean isChildOf(ClassId classId)
	{
		if (_parent == null)
			return false;
		
		if (_parent == classId)
			return true;
		
		return _parent.isChildOf(classId);
	}
	
	/**
	 * @param classId : The parent {@link ClassId} to check.
	 * @return True if this {@link ClassId} equals tested {@link ClassId} or is a child of the selected {@link ClassId}.
	 */
	public final boolean equalsOrIsChildOf(ClassId classId)
	{
		return this == classId || isChildOf(classId);
	}
	
	private final void createSubclasses()
	{
		// Only 2nd class level can have subclasses.
		if (_level != 2)
		{
			_subclasses = null;
			return;
		}
		
		_subclasses = EnumSet.noneOf(ClassId.class);
		
		for (ClassId classId : VALUES)
		{
			// Only second classes may be taken as subclass.
			if (classId._level != 2)
				continue;
			
			// Overlord, Warsmith or self class may never be taken as subclass.
			if (classId == OVERLORD || classId == WARSMITH || classId == this)
				continue;
			
			// Elves may not sub Dark Elves and vice versa.
			if ((_race == ClassRace.ELF && classId._race == ClassRace.DARK_ELF) || (_race == ClassRace.DARK_ELF && classId._race == ClassRace.ELF))
				continue;
			
			_subclasses.add(classId);
		}
		
		// Remove class restricted classes.
		switch (this)
		{
			case DARK_AVENGER, PALADIN, TEMPLE_KNIGHT, SHILLIEN_KNIGHT:
				_subclasses.removeAll(EnumSet.of(DARK_AVENGER, PALADIN, TEMPLE_KNIGHT, SHILLIEN_KNIGHT));
				break;
			
			case TREASURE_HUNTER, ABYSS_WALKER, PLAINS_WALKER:
				_subclasses.removeAll(EnumSet.of(TREASURE_HUNTER, ABYSS_WALKER, PLAINS_WALKER));
				break;
			
			case HAWKEYE, SILVER_RANGER, PHANTOM_RANGER:
				_subclasses.removeAll(EnumSet.of(HAWKEYE, SILVER_RANGER, PHANTOM_RANGER));
				break;
			
			case WARLOCK, ELEMENTAL_SUMMONER, PHANTOM_SUMMONER:
				_subclasses.removeAll(EnumSet.of(WARLOCK, ELEMENTAL_SUMMONER, PHANTOM_SUMMONER));
				break;
			
			case SORCERER, SPELLSINGER, SPELLHOWLER:
				_subclasses.removeAll(EnumSet.of(SORCERER, SPELLSINGER, SPELLHOWLER));
				break;
		}
	}
	
	/**
	 * Following conditions are tested:<br>
	 * <ul>
	 * <li>If the race of your main class is Elf or Dark Elf, you may not select each class as a subclass to the other class.</li>
	 * <li>You may not select Overlord and Warsmith class as a subclass.</li>
	 * <li>You may not select a similar class as the subclass.</li>
	 * </ul>
	 * The occupations classified as similar classes are as follows:<br>
	 * <br>
	 * <ul>
	 * <li>Paladin, Dark Avenger, Temple Knight and Shillien Knight</li>
	 * <li>Treasure Hunter, Plainswalker and Abyss Walker</li>
	 * <li>Hawkeye, Silver Ranger and Phantom Ranger</li>
	 * <li>Warlock, Elemental Summoner and Phantom Summoner</li>
	 * <li>Sorcerer, Spellsinger and Spellhowler</li>
	 * </ul>
	 * @param player : The {@link Player} to make checks on.
	 * @return All available subclasses for given {@link Player} under a {@link EnumSet} of {@link ClassId}s.
	 */
	public static final Set<ClassId> getAvailableSubclasses(Player player)
	{
		ClassId classId = VALUES[player.getBaseClass()];
		if (classId._level < 2)
			return Collections.emptySet();
		
		// Handle 3rd level class.
		if (classId._level == 3)
			classId = classId._parent;
		
		return EnumSet.copyOf(classId._subclasses);
	}
	
	public static final boolean isInGroup(Player player, String occupation)
	{
		if (player == null || occupation == null)
			return false;
		
		switch (occupation)
		{
			case "@attacker_group":
				return ATTACKER_GROUP.contains(player.getClassId());
			
			case "@fighter_group":
				return FIGHTER_GROUP.contains(player.getClassId());
			
			case "@mage_group":
				return MAGE_GROUP.contains(player.getClassId());
			
			case "@wizard_group":
				return WIZARD_GROUP.contains(player.getClassId());
			
			case "@cleric_group":
				return CLERIC_GROUP.contains(player.getClassId());
			
			case "@subjob_group_knight":
				return SUBJOB_KNIGHT_GROUP.contains(player.getClassId());
		}
		
		return false;
	}
	
	public static final boolean isSameOccupation(Player player, String occupation)
	{
		if (player == null || occupation == null)
			return false;
		
		switch (occupation)
		{
			case "@fighter":
				return player.getClassId() == HUMAN_FIGHTER;
			
			case "@mage":
				return player.getClassId() == HUMAN_MYSTIC;
			
			case "@swordsinger":
				return player.getClassId() == SWORD_SINGER;
			
			case "@elven_wizard":
				return player.getClassId() == ELVEN_WIZARD;
		}
		return false;
	}
	
	static
	{
		// Create subclass lists.
		for (ClassId classId : VALUES)
			classId.createSubclasses();
	}
}