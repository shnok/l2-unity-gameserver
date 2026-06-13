package com.shnok.javaserver.gameserver.model.item.kind;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.items.ActionType;
import com.shnok.javaserver.gameserver.enums.items.ArmorType;
import com.shnok.javaserver.gameserver.enums.items.CrystalType;
import com.shnok.javaserver.gameserver.enums.items.EtcItemType;
import com.shnok.javaserver.gameserver.enums.items.ItemType;
import com.shnok.javaserver.gameserver.enums.items.MaterialType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;
import com.shnok.javaserver.gameserver.skills.basefuncs.FuncTemplate;
import com.shnok.javaserver.gameserver.skills.conditions.Condition;

/**
 * This container contains all informations concerning an item (weapon, armor, etc).
 */
public abstract class Item
{
	public static final int TYPE1_WEAPON_RING_EARRING_NECKLACE = 0;
	public static final int TYPE1_SHIELD_ARMOR = 1;
	public static final int TYPE1_ITEM_QUESTITEM_ADENA = 4;
	
	public static final int TYPE2_WEAPON = 0;
	public static final int TYPE2_SHIELD_ARMOR = 1;
	public static final int TYPE2_ACCESSORY = 2;
	public static final int TYPE2_QUEST = 3;
	public static final int TYPE2_MONEY = 4;
	public static final int TYPE2_OTHER = 5;
	
	public static final int SLOT_NONE = 0x0000;
	public static final int SLOT_UNDERWEAR = 0x0001;
	public static final int SLOT_R_EAR = 0x0002;
	public static final int SLOT_L_EAR = 0x0004;
	public static final int SLOT_LR_EAR = 0x0006;
	public static final int SLOT_NECK = 0x0008;
	public static final int SLOT_R_FINGER = 0x0010;
	public static final int SLOT_L_FINGER = 0x0020;
	public static final int SLOT_LR_FINGER = 0x0030;
	public static final int SLOT_HEAD = 0x0040;
	public static final int SLOT_R_HAND = 0x0080;
	public static final int SLOT_L_HAND = 0x0100;
	public static final int SLOT_GLOVES = 0x0200;
	public static final int SLOT_CHEST = 0x0400;
	public static final int SLOT_LEGS = 0x0800;
	public static final int SLOT_FEET = 0x1000;
	public static final int SLOT_BACK = 0x2000;
	public static final int SLOT_LR_HAND = 0x4000;
	public static final int SLOT_FULL_ARMOR = 0x8000;
	public static final int SLOT_FACE = 0x010000;
	public static final int SLOT_ALLDRESS = 0x020000;
	public static final int SLOT_HAIR = 0x040000;
	public static final int SLOT_HAIRALL = 0x080000;
	
	public static final int SLOT_WOLF = -100;
	public static final int SLOT_HATCHLING = -101;
	public static final int SLOT_STRIDER = -102;
	public static final int SLOT_BABYPET = -103;
	
	public static final int SLOT_ALLWEAPON = SLOT_LR_HAND | SLOT_R_HAND;
	
	private static final Map<String, Integer> SLOTS = HashMap.newHashMap(24);
	static
	{
		SLOTS.put("chest", SLOT_CHEST);
		SLOTS.put("fullarmor", SLOT_FULL_ARMOR);
		SLOTS.put("alldress", SLOT_ALLDRESS);
		SLOTS.put("head", SLOT_HEAD);
		SLOTS.put("hair", SLOT_HAIR);
		SLOTS.put("face", SLOT_FACE);
		SLOTS.put("hairall", SLOT_HAIRALL);
		SLOTS.put("underwear", SLOT_UNDERWEAR);
		SLOTS.put("back", SLOT_BACK);
		SLOTS.put("neck", SLOT_NECK);
		SLOTS.put("legs", SLOT_LEGS);
		SLOTS.put("feet", SLOT_FEET);
		SLOTS.put("gloves", SLOT_GLOVES);
		SLOTS.put("chest,legs", SLOT_CHEST | SLOT_LEGS);
		SLOTS.put("rhand", SLOT_R_HAND);
		SLOTS.put("lhand", SLOT_L_HAND);
		SLOTS.put("lrhand", SLOT_LR_HAND);
		SLOTS.put("rear;lear", SLOT_R_EAR | SLOT_L_EAR);
		SLOTS.put("rfinger;lfinger", SLOT_R_FINGER | SLOT_L_FINGER);
		SLOTS.put("none", SLOT_NONE);
		SLOTS.put("wolf", SLOT_WOLF); // for wolf
		SLOTS.put("hatchling", SLOT_HATCHLING); // for hatchling
		SLOTS.put("strider", SLOT_STRIDER); // for strider
		SLOTS.put("babypet", SLOT_BABYPET); // for babypet
	}
	
	private final int _itemId;
	private final String _name;
	protected int _type1; // needed for item list (inventory)
	protected int _type2; // different lists for armor, weapon, etc
	private final int _weight;
	private final boolean _stackable;
	private final MaterialType _materialType;
	private final CrystalType _crystalType;
	private final int _duration;
	private final int _bodyPart;
	private final int _referencePrice;
	private final int _crystalCount;
	
	private final boolean _sellable;
	private final boolean _dropable;
	private final boolean _destroyable;
	private final boolean _tradable;
	private final boolean _depositable;
	
	private final boolean _heroItem;
	private final boolean _isOlyRestricted;
	
	private final ActionType _defaultAction;
	
	protected List<FuncTemplate> _funcTemplates;
	
	protected List<Condition> _preConditions;
	private IntIntHolder[] _skillHolder;
	
	private List<Quest> _questEvents = Collections.emptyList();
	
	protected Item(StatSet set)
	{
		_itemId = set.getInteger("item_id");
		_name = set.getString("name");
		_weight = set.getInteger("weight", 0);
		_materialType = set.getEnum("material", MaterialType.class, MaterialType.STEEL);
		_duration = set.getInteger("duration", -1);
		_bodyPart = SLOTS.get(set.getString("bodypart", "none"));
		_referencePrice = set.getInteger("price", 0);
		_crystalType = set.getEnum("crystal_type", CrystalType.class, CrystalType.NONE);
		_crystalCount = set.getInteger("crystal_count", 0);
		
		_stackable = set.getBool("is_stackable", false);
		_sellable = set.getBool("is_sellable", true);
		_dropable = set.getBool("is_dropable", true);
		_destroyable = set.getBool("is_destroyable", true);
		_tradable = set.getBool("is_tradable", true);
		_depositable = set.getBool("is_depositable", true);
		
		_heroItem = (_itemId >= 6611 && _itemId <= 6621) || _itemId == 6842;
		_isOlyRestricted = set.getBool("is_oly_restricted", false);
		
		_defaultAction = set.getEnum("default_action", ActionType.class, ActionType.none);
		
		if (set.containsKey("item_skill"))
			_skillHolder = set.getIntIntHolderArray("item_skill");
	}
	
	/**
	 * @return Enum the itemType.
	 */
	public abstract ItemType getItemType();
	
	/**
	 * @return int the duration of the item
	 */
	public final int getDuration()
	{
		return _duration;
	}
	
	/**
	 * @return int the ID of the item
	 */
	public final int getItemId()
	{
		return _itemId;
	}
	
	public abstract int getItemMask();
	
	/**
	 * @return int the type of material of the item
	 */
	public final MaterialType getMaterialType()
	{
		return _materialType;
	}
	
	/**
	 * @return int the type 2 of the item
	 */
	public final int getType2()
	{
		return _type2;
	}
	
	/**
	 * @return int the weight of the item
	 */
	public final int getWeight()
	{
		return _weight;
	}
	
	/**
	 * @return boolean if the item is crystallizable
	 */
	public final boolean isCrystallizable()
	{
		return _crystalType != CrystalType.NONE && _crystalCount > 0;
	}
	
	/**
	 * @return CrystalType the type of crystal if item is crystallizable
	 */
	public final CrystalType getCrystalType()
	{
		return _crystalType;
	}
	
	/**
	 * @return int the type of crystal if item is crystallizable
	 */
	public final int getCrystalItemId()
	{
		return _crystalType.getCrystalId();
	}
	
	/**
	 * @return int the quantity of crystals for crystallization
	 */
	public final int getCrystalCount()
	{
		return _crystalCount;
	}
	
	/**
	 * @param enchantLevel
	 * @return int the quantity of crystals for crystallization on specific enchant level
	 */
	public final int getCrystalCount(int enchantLevel)
	{
		if (enchantLevel > 3)
		{
			switch (_type2)
			{
				case TYPE2_SHIELD_ARMOR, TYPE2_ACCESSORY:
					return _crystalCount + getCrystalType().getCrystalEnchantBonusArmor() * (3 * enchantLevel - 6);
				
				case TYPE2_WEAPON:
					return _crystalCount + getCrystalType().getCrystalEnchantBonusWeapon() * (2 * enchantLevel - 3);
				
				default:
					return _crystalCount;
			}
		}
		else if (enchantLevel > 0)
		{
			switch (_type2)
			{
				case TYPE2_SHIELD_ARMOR, TYPE2_ACCESSORY:
					return _crystalCount + getCrystalType().getCrystalEnchantBonusArmor() * enchantLevel;
				case TYPE2_WEAPON:
					return _crystalCount + getCrystalType().getCrystalEnchantBonusWeapon() * enchantLevel;
				default:
					return _crystalCount;
			}
		}
		else
			return _crystalCount;
	}
	
	/**
	 * @return String the name of the item
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * @return int the part of the body used with the item.
	 */
	public final int getBodyPart()
	{
		return _bodyPart;
	}
	
	/**
	 * @return int the type 1 of the item
	 */
	public final int getType1()
	{
		return _type1;
	}
	
	/**
	 * @return boolean if the item is stackable
	 */
	public final boolean isStackable()
	{
		return _stackable;
	}
	
	/**
	 * @return boolean if the item is consumable
	 */
	public boolean isConsumable()
	{
		return false;
	}
	
	public boolean isEquipable()
	{
		return getBodyPart() != 0 && !(getItemType() instanceof EtcItemType);
	}
	
	/**
	 * @return int the price of reference of the item
	 */
	public final int getReferencePrice()
	{
		return _referencePrice;
	}
	
	/**
	 * Returns if the item can be sold
	 * @return boolean
	 */
	public final boolean isSellable()
	{
		return _sellable;
	}
	
	/**
	 * Returns if the item can dropped
	 * @return boolean
	 */
	public final boolean isDropable()
	{
		return _dropable;
	}
	
	/**
	 * Returns if the item can destroy
	 * @return boolean
	 */
	public final boolean isDestroyable()
	{
		return _destroyable;
	}
	
	/**
	 * Returns if the item can add to trade
	 * @return boolean
	 */
	public final boolean isTradable()
	{
		return _tradable;
	}
	
	/**
	 * Returns if the item can be put into warehouse
	 * @return boolean
	 */
	public final boolean isDepositable()
	{
		return _depositable;
	}
	
	/**
	 * Get the functions used by this item.
	 * @param item : ItemInstance pointing out the item
	 * @param player : Creature pointing out the player
	 * @return the list of functions
	 */
	public final List<Func> getStatFuncs(ItemInstance item, Creature player)
	{
		if (_funcTemplates == null || _funcTemplates.isEmpty())
			return Collections.emptyList();
		
		final List<Func> funcs = new ArrayList<>(_funcTemplates.size());
		
		for (FuncTemplate template : _funcTemplates)
		{
			final Func func = template.getFunc(player, player, item, item);
			if (func != null)
				funcs.add(func);
		}
		return funcs;
	}
	
	/**
	 * Add the FuncTemplate f to the list of functions used with the item
	 * @param f : FuncTemplate to add
	 */
	public void attach(FuncTemplate f)
	{
		if (_funcTemplates == null)
			_funcTemplates = new ArrayList<>(1);
		
		_funcTemplates.add(f);
	}
	
	public final void attach(Condition c)
	{
		if (_preConditions == null)
			_preConditions = new ArrayList<>();
		
		if (!_preConditions.contains(c))
			_preConditions.add(c);
	}
	
	/**
	 * Method to retrieve skills linked to this item
	 * @return Skills linked to this item as SkillHolder[]
	 */
	public final IntIntHolder[] getSkills()
	{
		return _skillHolder;
	}
	
	public boolean checkCondition(Creature creature, WorldObject object, boolean sendMessage)
	{
		// Don't allow hero equipment and restricted items during Olympiad
		if ((isOlyRestrictedItem() || isHeroItem()) && creature instanceof Player player && player.isInOlympiadMode())
		{
			if (isEquipable())
				player.sendPacket(SystemMessageId.THIS_ITEM_CANT_BE_EQUIPPED_FOR_THE_OLYMPIAD_EVENT);
			else
				player.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
			
			return false;
		}
		
		if (_preConditions == null)
			return true;
		
		final Creature target = (object instanceof Creature targetCreature) ? targetCreature : null;
		for (Condition preCondition : _preConditions)
		{
			if (preCondition == null)
				continue;
			
			if (!preCondition.test(creature, target, null, null))
			{
				if (creature instanceof Summon summon)
				{
					summon.sendPacket(SystemMessageId.PET_CANNOT_USE_ITEM);
					return false;
				}
				
				if (sendMessage)
				{
					final String msg = preCondition.getMessage();
					if (msg != null)
						creature.sendMessage(msg);
					else
					{
						final int msgId = preCondition.getMessageId();
						if (msgId != 0)
						{
							final SystemMessage sm = SystemMessage.getSystemMessage(msgId);
							if (preCondition.isAddName())
								sm.addItemName(_itemId);
							
							creature.sendPacket(sm);
						}
					}
				}
				return false;
			}
		}
		return true;
	}
	
	public boolean isConditionAttached()
	{
		return _preConditions != null && !_preConditions.isEmpty();
	}
	
	public boolean isQuestItem()
	{
		return (getItemType() == EtcItemType.QUEST);
	}
	
	public final boolean isHeroItem()
	{
		return _heroItem;
	}
	
	public boolean isOlyRestrictedItem()
	{
		return _isOlyRestricted;
	}
	
	public boolean isPetItem()
	{
		return (getItemType() == ArmorType.PET || getItemType() == WeaponType.PET);
	}
	
	public boolean isPotion()
	{
		return (getItemType() == EtcItemType.POTION);
	}
	
	public boolean isElixir()
	{
		return (getItemType() == EtcItemType.ELIXIR);
	}
	
	public ActionType getDefaultAction()
	{
		return _defaultAction;
	}
	
	/**
	 * Returns the name of the item
	 * @return String
	 */
	@Override
	public String toString()
	{
		return _name + " (" + _itemId + ")";
	}
	
	public void addQuestEvent(Quest quest)
	{
		if (_questEvents.isEmpty())
			_questEvents = new ArrayList<>(3);
		
		_questEvents.add(quest);
	}
	
	public List<Quest> getQuestEvents()
	{
		return _questEvents;
	}
	
	public boolean isNightLure()
	{
		return ((_itemId >= 8505 && _itemId <= 8513) || _itemId == 8485);
	}
}