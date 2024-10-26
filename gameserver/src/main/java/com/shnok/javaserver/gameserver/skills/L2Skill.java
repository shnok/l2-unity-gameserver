package com.shnok.javaserver.gameserver.skills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import net.sf.l2j.commons.data.StatSet;
import net.sf.l2j.commons.logging.CLogger;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.items.ArmorType;
import net.sf.l2j.gameserver.enums.items.WeaponType;
import net.sf.l2j.gameserver.enums.skills.ElementType;
import net.sf.l2j.gameserver.enums.skills.FlyType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillOpType;
import net.sf.l2j.gameserver.enums.skills.SkillTargetType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.handler.ITargetHandler;
import net.sf.l2j.gameserver.handler.TargetHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Cubic;
import net.sf.l2j.gameserver.model.actor.instance.Door;
import net.sf.l2j.gameserver.model.actor.instance.SiegeFlag;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.kind.Armor;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.item.kind.Weapon;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.basefuncs.Func;
import net.sf.l2j.gameserver.skills.basefuncs.FuncTemplate;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.effects.EffectTemplate;
import net.sf.l2j.gameserver.skills.extractable.ExtractableProductItem;
import net.sf.l2j.gameserver.skills.extractable.ExtractableSkill;

public abstract class L2Skill implements IChanceSkillTrigger
{
	protected static final CLogger LOGGER = new CLogger(L2Skill.class.getName());
	
	public static final int SKILL_LUCKY = 194;
	public static final int SKILL_EXPERTISE = 239;
	public static final int SKILL_SHADOW_SENSE = 294;
	public static final int SKILL_CREATE_COMMON = 1320;
	public static final int SKILL_CREATE_DWARVEN = 172;
	public static final int SKILL_CRYSTALLIZE = 248;
	public static final int SKILL_DIVINE_INSPIRATION = 1405;
	public static final int SKILL_NPC_RACE = 4416;
	
	private final int _id;
	private final int _level;
	
	private final String _name;
	private final SkillOpType _operateType;
	
	private final boolean _isMagic;
	
	private final int _mpConsume;
	private final int _mpInitialConsume;
	private final int _hpConsume;
	
	private final int _targetConsume;
	private final int _targetConsumeId;
	
	private final int _itemConsume; // items consumption
	private final int _itemConsumeId;
	
	private final int _castRange;
	private final int _effectRange;
	
	private final int _abnormalLvl; // Abnormal levels for skills and their canceling
	private final int _effectAbnormalLvl;
	
	private final int _hitTime; // all times in milliseconds
	private final int _coolTime;
	
	private final int _reuseDelay;
	private final int _equipDelay;
	
	private final SkillTargetType _targetType;
	
	private final double _power;
	
	private final int _magicLevel;
	
	private final int _negateLvl; // abnormalLvl is negated with negateLvl
	private final int[] _negateId; // cancels the effect of skill ID
	private final SkillType[] _negateStats; // lists the effect types that are canceled
	private final int _maxNegatedEffects; // maximum number of effects to negate
	
	private final int _levelDepend;
	
	private final int _skillRadius; // Effecting area of the skill, in radius.
	
	private final SkillType _skillType;
	private final SkillType _effectType;
	
	private final int _effectId;
	private final int _effectPower;
	private final int _effectLvl;
	
	private final boolean _isPotion;
	private final ElementType _element;
	
	private final boolean _ignoreResists;
	
	private final boolean _staticReuse;
	private final boolean _staticHitTime;
	
	private final int _reuseHashCode;
	
	private final Stats _stat;
	
	private final int _baseLandRate;
	
	private final boolean _overhit;
	private final boolean _killByDOT;
	private final boolean _isSuicideAttack;
	
	private final boolean _isSiegeSummonSkill;
	
	private final int _weaponsAllowed;
	
	private final boolean _nextActionIsAttack;
	
	private final int _minPledgeClass;
	
	private final boolean _isOffensive;
	private final int _maxCharges;
	private final int _numCharges;
	
	private final int _triggeredId;
	private final int _triggeredLevel;
	protected ChanceCondition _chanceCondition = null;
	private final String _chanceType;
	
	private final FlyType _flyType;
	private final int _flyRadius;
	private final float _flyCourse;
	
	private final int _feed;
	
	private final boolean _isHeroSkill; // If true the skill is a Hero Skill
	
	private final int _baseCritRate; // percent of success for skill critical hit (especially for PDAM & BLOW - they're not affected by rCrit values or buffs). Default loads -1 for all other skills but 0 to PDAM & BLOW
	private final int _lethalEffect1; // percent of success for lethal 1st effect (hit cp to 1 or if mob hp to 50%) (only for PDAM skills)
	private final int _lethalEffect2; // percent of success for lethal 2nd effect (hit cp,hp to 1 or if mob hp to 1) (only for PDAM skills)
	private final boolean _directHpDmg; // If true then dmg is being make directly
	private final boolean _isDance; // If true then casting more dances will cost more MP
	private final int _nextDanceCost;
	private final float _sSBoost; // If true skill will have SoulShot boost (power*2)
	private final int _aggroPoints;
	
	protected List<Condition> _preCondition;
	protected List<Condition> _itemPreCondition;
	protected List<FuncTemplate> _funcTemplates;
	protected List<EffectTemplate> _effectTemplates;
	protected List<EffectTemplate> _effectTemplatesSelf;
	
	private final String _attribute;
	
	private final boolean _isDebuff;
	private final boolean _stayAfterDeath; // skill should stay after death
	
	private final boolean _canBeReflected;
	private final boolean _canBeDispeled;
	
	private final boolean _isClanSkill;
	
	private final boolean _ignoreShield;
	
	private final boolean _simultaneousCast;
	
	private ExtractableSkill _extractableItems = null;
	
	protected L2Skill(StatSet set)
	{
		_id = set.getInteger("skill_id");
		_level = set.getInteger("level", 1);
		
		_name = set.getString("name");
		_operateType = set.getEnum("operateType", SkillOpType.class);
		
		_isMagic = set.getBool("isMagic", false);
		_isPotion = set.getBool("isPotion", false);
		
		_mpConsume = set.getInteger("mpConsume", 0);
		_mpInitialConsume = set.getInteger("mpInitialConsume", 0);
		_hpConsume = set.getInteger("hpConsume", 0);
		
		_targetConsume = set.getInteger("targetConsumeCount", 0);
		_targetConsumeId = set.getInteger("targetConsumeId", 0);
		
		_itemConsume = set.getInteger("itemConsumeCount", 0);
		_itemConsumeId = set.getInteger("itemConsumeId", 0);
		
		_castRange = set.getInteger("castRange", 0);
		_effectRange = set.getInteger("effectRange", -1);
		
		_abnormalLvl = set.getInteger("abnormalLvl", -1);
		_effectAbnormalLvl = set.getInteger("effectAbnormalLvl", -1); // support for a separate effect abnormal lvl, e.g. poison inside a different skill
		_negateLvl = set.getInteger("negateLvl", -1);
		
		_hitTime = set.getInteger("hitTime", 0);
		_coolTime = set.getInteger("coolTime", 0);
		
		_reuseDelay = set.getInteger("reuseDelay", 0);
		_equipDelay = set.getInteger("equipDelay", 0);
		
		_skillRadius = set.getInteger("skillRadius", 80);
		
		_targetType = set.getEnum("target", SkillTargetType.class);
		
		_power = set.getFloat("power", 0.f);
		
		_attribute = set.getString("attribute", "");
		String str = set.getString("negateStats", "");
		
		if (str.isEmpty())
			_negateStats = new SkillType[0];
		else
		{
			String[] stats = str.split(" ");
			SkillType[] array = new SkillType[stats.length];
			
			for (int i = 0; i < stats.length; i++)
			{
				SkillType type = null;
				try
				{
					type = Enum.valueOf(SkillType.class, stats[i]);
				}
				catch (Exception e)
				{
					throw new IllegalArgumentException("SkillId: " + _id + "Enum value of type " + SkillType.class.getName() + " required, but found: " + stats[i]);
				}
				
				array[i] = type;
			}
			_negateStats = array;
		}
		
		String negateId = set.getString("negateId", null);
		if (negateId != null)
		{
			String[] valuesSplit = negateId.split(",");
			_negateId = new int[valuesSplit.length];
			for (int i = 0; i < valuesSplit.length; i++)
			{
				_negateId[i] = Integer.parseInt(valuesSplit[i]);
			}
		}
		else
			_negateId = new int[0];
		
		_maxNegatedEffects = set.getInteger("maxNegated", 0);
		
		_magicLevel = set.getInteger("magicLvl", 0);
		_levelDepend = set.getInteger("lvlDepend", 0);
		_ignoreResists = set.getBool("ignoreResists", false);
		
		_staticReuse = set.getBool("staticReuse", false);
		_staticHitTime = set.getBool("staticHitTime", false);
		
		String reuseHash = set.getString("sharedReuse", null);
		if (reuseHash != null)
		{
			try
			{
				String[] valuesSplit = reuseHash.split("-");
				_reuseHashCode = SkillTable.getSkillHashCode(Integer.parseInt(valuesSplit[0]), Integer.parseInt(valuesSplit[1]));
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("SkillId: " + _id + " invalid sharedReuse value: " + reuseHash + ", \"skillId-skillLvl\" required");
			}
		}
		else
			_reuseHashCode = SkillTable.getSkillHashCode(_id, _level);
		
		_stat = set.getEnum("stat", Stats.class, null);
		_ignoreShield = set.getBool("ignoreShld", false);
		
		_skillType = set.getEnum("skillType", SkillType.class);
		_effectType = set.getEnum("effectType", SkillType.class, null);
		
		_effectId = set.getInteger("effectId", 0);
		_effectPower = set.getInteger("effectPower", 0);
		_effectLvl = set.getInteger("effectLevel", 0);
		
		_element = set.getEnum("element", ElementType.class, ElementType.NONE);
		
		_baseLandRate = set.getInteger("baseLandRate", 0);
		
		_overhit = set.getBool("overHit", false);
		_killByDOT = set.getBool("killByDOT", false);
		_isSuicideAttack = set.getBool("isSuicideAttack", false);
		
		_isSiegeSummonSkill = set.getBool("isSiegeSummonSkill", false);
		
		String weaponsAllowedString = set.getString("weaponsAllowed", null);
		if (weaponsAllowedString != null)
		{
			int mask = 0;
			StringTokenizer st = new StringTokenizer(weaponsAllowedString, ",");
			while (st.hasMoreTokens())
			{
				int old = mask;
				String item = st.nextToken();
				for (WeaponType wt : WeaponType.values())
				{
					if (wt.name().equals(item))
					{
						mask |= wt.mask();
						break;
					}
				}
				
				for (ArmorType at : ArmorType.values())
				{
					if (at.name().equals(item))
					{
						mask |= at.mask();
						break;
					}
				}
				
				if (old == mask)
					LOGGER.warn("Unknown item type {} found on weaponsAllowed parse.", item);
			}
			_weaponsAllowed = mask;
		}
		else
			_weaponsAllowed = 0;
		
		_nextActionIsAttack = set.getBool("nextActionAttack", false);
		
		_minPledgeClass = set.getInteger("minPledgeClass", 0);
		
		_triggeredId = set.getInteger("triggeredId", 0);
		_triggeredLevel = set.getInteger("triggeredLevel", 0);
		_chanceType = set.getString("chanceType", "");
		if (!_chanceType.isEmpty())
			_chanceCondition = ChanceCondition.parse(set);
		
		_isDebuff = set.getBool("isDebuff", false);
		_isOffensive = set.getBool("offensive", isSkillTypeOffensive());
		_maxCharges = set.getInteger("maxCharges", 0);
		_numCharges = set.getInteger("numCharges", 0);
		
		_isHeroSkill = SkillTable.isHeroSkill(_id);
		
		_baseCritRate = set.getInteger("baseCritRate", (_skillType == SkillType.PDAM || _skillType == SkillType.BLOW) ? 0 : -1);
		_lethalEffect1 = set.getInteger("lethal1", 0);
		_lethalEffect2 = set.getInteger("lethal2", 0);
		
		_directHpDmg = set.getBool("dmgDirectlyToHp", false);
		_isDance = set.getBool("isDance", false);
		_nextDanceCost = set.getInteger("nextDanceCost", 0);
		_sSBoost = set.getFloat("SSBoost", 0.f);
		_aggroPoints = set.getInteger("aggroPoints", 0);
		
		_stayAfterDeath = set.getBool("stayAfterDeath", false);
		
		_flyType = set.getEnum("flyType", FlyType.class, null);
		_flyRadius = set.getInteger("flyRadius", 0);
		_flyCourse = set.getFloat("flyCourse", 0);
		
		_feed = set.getInteger("feed", 0);
		
		_canBeReflected = set.getBool("canBeReflected", true);
		_canBeDispeled = set.getBool("canBeDispeled", true);
		
		_isClanSkill = set.getBool("isClanSkill", false);
		
		_simultaneousCast = set.getBool("simultaneousCast", false);
		
		final String capsuledItems = set.getString("capsuled_items_skill", null);
		if (capsuledItems != null)
		{
			if (capsuledItems.isEmpty())
				LOGGER.warn("Empty extractable data for skill: {}.", _id);
			
			_extractableItems = parseExtractableSkill(_id, _level, capsuledItems);
		}
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_id, _level);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		final L2Skill other = (L2Skill) obj;
		return _id == other._id && _level == other._level;
	}
	
	public abstract void useSkill(Creature caster, WorldObject[] targets);
	
	public final boolean isPotion()
	{
		return _isPotion;
	}
	
	public final SkillType getSkillType()
	{
		return _skillType;
	}
	
	public final ElementType getElement()
	{
		return _element;
	}
	
	/**
	 * @return the target type of the skill : SELF, PARTY, CLAN, PET...
	 */
	public final SkillTargetType getTargetType()
	{
		return _targetType;
	}
	
	public final int getBaseLandRate()
	{
		return _baseLandRate;
	}
	
	public final boolean isOverhit()
	{
		return _overhit;
	}
	
	public final boolean killByDOT()
	{
		return _killByDOT;
	}
	
	public final boolean isSuicideAttack()
	{
		return _isSuicideAttack;
	}
	
	public final boolean isSiegeSummonSkill()
	{
		return _isSiegeSummonSkill;
	}
	
	/**
	 * @param activeChar
	 * @return the power of the skill.
	 */
	public final double getPower(Creature activeChar)
	{
		if (activeChar == null)
			return _power;
		
		switch (_skillType)
		{
			case DEATHLINK:
				return _power * Math.pow(1.7165 - activeChar.getStatus().getHpRatio(), 2) * 0.577;
			case FATAL:
				return _power + (_power * Math.pow(1.7165 - activeChar.getStatus().getHpRatio(), 3.5) * 0.577);
			default:
				return _power;
		}
	}
	
	public final double getPower()
	{
		return _power;
	}
	
	public final SkillType[] getNegateStats()
	{
		return _negateStats;
	}
	
	public final int getAbnormalLvl()
	{
		return _abnormalLvl;
	}
	
	public final int getNegateLvl()
	{
		return _negateLvl;
	}
	
	public final int[] getNegateId()
	{
		return _negateId;
	}
	
	public final int getMagicLevel()
	{
		return _magicLevel;
	}
	
	public final int getMaxNegatedEffects()
	{
		return _maxNegatedEffects;
	}
	
	public final int getLevelDepend()
	{
		return _levelDepend;
	}
	
	/**
	 * @return true if skill should ignore all resistances.
	 */
	public final boolean ignoreResists()
	{
		return _ignoreResists;
	}
	
	public int getTriggeredId()
	{
		return _triggeredId;
	}
	
	public int getTriggeredLevel()
	{
		return _triggeredLevel;
	}
	
	public boolean triggerAnotherSkill()
	{
		return _triggeredId > 1;
	}
	
	/**
	 * @return the additional effect power or base probability.
	 */
	public final double getEffectPower()
	{
		if (_effectTemplates != null)
		{
			for (EffectTemplate et : _effectTemplates)
			{
				if (et.getEffectPower() > 0)
					return et.getEffectPower();
			}
		}
		
		if (_effectPower > 0)
			return _effectPower;
		
		// Allow damage dealing skills having proper resist even without specified effectPower.
		switch (_skillType)
		{
			case PDAM, MDAM:
				return 20;
			
			default:
				// to let debuffs succeed even without specified power
				return (_power <= 0 || 100 < _power) ? 20 : _power;
		}
	}
	
	/**
	 * @return the additional effect Id.
	 */
	public final int getEffectId()
	{
		return _effectId;
	}
	
	/**
	 * @return the additional effect level.
	 */
	public final int getEffectLvl()
	{
		return _effectLvl;
	}
	
	public final int getEffectAbnormalLvl()
	{
		return _effectAbnormalLvl;
	}
	
	/**
	 * @return the additional effect skill type (ex : STUN, PARALYZE,...).
	 */
	public final SkillType getEffectType()
	{
		if (_effectTemplates != null)
		{
			for (EffectTemplate et : _effectTemplates)
			{
				if (et.getEffectType() != null)
					return et.getEffectType();
			}
		}
		
		if (_effectType != null)
			return _effectType;
		
		// to let damage dealing skills having proper resist even without specified effectType
		switch (_skillType)
		{
			case PDAM:
				return SkillType.STUN;
			case MDAM:
				return SkillType.PARALYZE;
			default:
				return _skillType;
		}
	}
	
	/**
	 * @return true if character should attack target after skill
	 */
	public final boolean nextActionIsAttack()
	{
		return _nextActionIsAttack;
	}
	
	/**
	 * @return Returns the castRange.
	 */
	public final int getCastRange()
	{
		return _castRange;
	}
	
	/**
	 * @return Returns the effectRange.
	 */
	public final int getEffectRange()
	{
		return _effectRange;
	}
	
	/**
	 * @return Returns the hpConsume.
	 */
	public final int getHpConsume()
	{
		return _hpConsume;
	}
	
	/**
	 * @return Returns the boolean _isDebuff.
	 */
	public final boolean isDebuff()
	{
		return _isDebuff;
	}
	
	/**
	 * @return the skill id.
	 */
	public final int getId()
	{
		return _id;
	}
	
	public final Stats getStat()
	{
		return _stat;
	}
	
	/**
	 * @return the _targetConsumeId.
	 */
	public final int getTargetConsumeId()
	{
		return _targetConsumeId;
	}
	
	/**
	 * @return the targetConsume.
	 */
	public final int getTargetConsume()
	{
		return _targetConsume;
	}
	
	/**
	 * @return the itemConsume.
	 */
	public final int getItemConsume()
	{
		return _itemConsume;
	}
	
	/**
	 * @return the itemConsumeId.
	 */
	public final int getItemConsumeId()
	{
		return _itemConsumeId;
	}
	
	/**
	 * @return the level.
	 */
	public final int getLevel()
	{
		return _level;
	}
	
	/**
	 * @return the magic.
	 */
	public final boolean isMagic()
	{
		return _isMagic;
	}
	
	/**
	 * @return true to set static reuse.
	 */
	public final boolean isStaticReuse()
	{
		return _staticReuse;
	}
	
	/**
	 * @return true to set static hittime.
	 */
	public final boolean isStaticHitTime()
	{
		return _staticHitTime;
	}
	
	/**
	 * @return Returns the mpConsume.
	 */
	public final int getMpConsume()
	{
		return _mpConsume;
	}
	
	/**
	 * @return Returns the mpInitialConsume.
	 */
	public final int getMpInitialConsume()
	{
		return _mpInitialConsume;
	}
	
	/**
	 * @return Returns the name.
	 */
	public final String getName()
	{
		return _name;
	}
	
	/**
	 * @return Returns the reuseDelay.
	 */
	public final int getReuseDelay()
	{
		return _reuseDelay;
	}
	
	public final int getEquipDelay()
	{
		return _equipDelay;
	}
	
	public final int getReuseHashCode()
	{
		return _reuseHashCode;
	}
	
	public final int getHitTime()
	{
		return _hitTime;
	}
	
	/**
	 * @return Returns the coolTime.
	 */
	public final int getCoolTime()
	{
		return _coolTime;
	}
	
	public final int getSkillRadius()
	{
		return _skillRadius;
	}
	
	public final boolean isActive()
	{
		return _operateType == SkillOpType.ACTIVE;
	}
	
	public final boolean isPassive()
	{
		return _operateType == SkillOpType.PASSIVE;
	}
	
	public final boolean isToggle()
	{
		return _operateType == SkillOpType.TOGGLE;
	}
	
	public boolean isChance()
	{
		return _chanceCondition != null && isPassive();
	}
	
	public final boolean isDance()
	{
		return _isDance;
	}
	
	public final int getNextDanceMpCost()
	{
		return _nextDanceCost;
	}
	
	public final float getSSBoost()
	{
		return _sSBoost;
	}
	
	public final int getAggroPoints()
	{
		return _aggroPoints;
	}
	
	public final boolean useSoulShot()
	{
		switch (_skillType)
		{
			case BLOW, PDAM, STUN, CHARGEDAM:
				return true;
		}
		return false;
	}
	
	public final boolean useSpiritShot()
	{
		return isMagic();
	}
	
	public final int getWeaponsAllowed()
	{
		return _weaponsAllowed;
	}
	
	public boolean isSimultaneousCast()
	{
		return _simultaneousCast;
	}
	
	public int getMinPledgeClass()
	{
		return _minPledgeClass;
	}
	
	public String getAttributeName()
	{
		return _attribute;
	}
	
	public boolean ignoreShield()
	{
		return _ignoreShield;
	}
	
	public boolean canBeReflected()
	{
		return _canBeReflected;
	}
	
	public boolean canBeDispeled()
	{
		return _canBeDispeled;
	}
	
	public boolean isClanSkill()
	{
		return _isClanSkill;
	}
	
	public final FlyType getFlyType()
	{
		return _flyType;
	}
	
	public final int getFlyRadius()
	{
		return _flyRadius;
	}
	
	public int getFeed()
	{
		return _feed;
	}
	
	public final float getFlyCourse()
	{
		return _flyCourse;
	}
	
	public final int getMaxCharges()
	{
		return _maxCharges;
	}
	
	@Override
	public boolean triggersChanceSkill()
	{
		return _triggeredId > 0 && isChance();
	}
	
	@Override
	public int getTriggeredChanceId()
	{
		return _triggeredId;
	}
	
	@Override
	public int getTriggeredChanceLevel()
	{
		return _triggeredLevel;
	}
	
	@Override
	public ChanceCondition getTriggeredChanceCondition()
	{
		return _chanceCondition;
	}
	
	public final boolean is7Signs()
	{
		return _id > 4360 && _id < 4367;
	}
	
	public final boolean isStayAfterDeath()
	{
		return _stayAfterDeath;
	}
	
	public final boolean isOffensive()
	{
		return _isOffensive;
	}
	
	public final boolean isHeroSkill()
	{
		return _isHeroSkill;
	}
	
	public final int getNumCharges()
	{
		return _numCharges;
	}
	
	public final int getBaseCritRate()
	{
		return _baseCritRate;
	}
	
	public final int getLethalChance1()
	{
		return _lethalEffect1;
	}
	
	public final int getLethalChance2()
	{
		return _lethalEffect2;
	}
	
	public final boolean getDmgDirectlyToHP()
	{
		return _directHpDmg;
	}
	
	public final boolean isSkillTypeOffensive()
	{
		switch (_skillType)
		{
			case PDAM:
			case MDAM:
			case CPDAMPERCENT:
			case DOT:
			case BLEED:
			case POISON:
			case AGGDAMAGE:
			case DEBUFF:
			case AGGDEBUFF:
			case STUN:
			case ROOT:
			case CONFUSION:
			case ERASE:
			case BLOW:
			case FATAL:
			case FEAR:
			case DRAIN:
			case SLEEP:
			case CHARGEDAM:
			case DEATHLINK:
			case MANADAM:
			case MDOT:
			case MUTE:
			case SOULSHOT:
			case SPIRITSHOT:
			case SPOIL:
			case WEAKNESS:
			case SWEEP:
			case PARALYZE:
			case DRAIN_SOUL:
			case AGGREDUCE:
			case CANCEL:
			case MAGE_BANE:
			case WARRIOR_BANE:
			case AGGREMOVE:
			case AGGREDUCE_CHAR:
			case BEAST_FEED:
			case BETRAY:
			case DELUXE_KEY_UNLOCK:
			case SOW:
			case HARVEST:
			case INSTANT_JUMP:
				return true;
			default:
				return isDebuff() || _targetType == SkillTargetType.CORPSE_MOB;
		}
	}
	
	public final boolean getWeaponDependancy(Creature activeChar)
	{
		// check to see if skill has a weapon dependency.
		final int weaponsAllowed = getWeaponsAllowed();
		if (weaponsAllowed == 0)
			return true;
		
		int mask = 0;
		
		final Weapon weapon = activeChar.getActiveWeaponItem();
		if (weapon != null)
			mask |= weapon.getItemType().mask();
		
		final Item shield = activeChar.getSecondaryWeaponItem();
		if (shield instanceof Armor armor)
			mask |= armor.getItemType().mask();
		
		if ((mask & weaponsAllowed) != 0)
			return true;
		
		activeChar.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(this));
		return false;
	}
	
	public boolean checkCondition(Creature activeChar, Creature target, boolean itemOrWeapon)
	{
		final List<Condition> preCondition = (itemOrWeapon) ? _itemPreCondition : _preCondition;
		if (preCondition == null || preCondition.isEmpty())
			return true;
		
		for (Condition cond : preCondition)
		{
			if (!cond.test(activeChar, target, this))
			{
				final int msgId = cond.getMessageId();
				if (msgId != 0)
				{
					SystemMessage sm = SystemMessage.getSystemMessage(msgId);
					if (cond.isAddName())
						sm.addSkillName(_id);
					activeChar.sendPacket(sm);
				}
				else
				{
					final String msg = cond.getMessage();
					if (msg != null)
						activeChar.sendMessage(msg);
				}
				return false;
			}
		}
		return true;
	}
	
	public final List<Func> getStatFuncs(Creature player)
	{
		if (_funcTemplates == null)
			return Collections.emptyList();
		
		if (!(player instanceof Playable) && !(player instanceof Attackable))
			return Collections.emptyList();
		
		final List<Func> funcs = new ArrayList<>(_funcTemplates.size());
		
		for (FuncTemplate t : _funcTemplates)
		{
			final Func f = t.getFunc(player, null, this, this);
			if (f != null)
				funcs.add(f);
		}
		return funcs;
	}
	
	public boolean hasEffects()
	{
		return (_effectTemplates != null && !_effectTemplates.isEmpty());
	}
	
	public List<EffectTemplate> getEffectTemplates()
	{
		return _effectTemplates;
	}
	
	public boolean hasSelfEffects()
	{
		return (_effectTemplatesSelf != null && !_effectTemplatesSelf.isEmpty());
	}
	
	public final List<AbstractEffect> getEffects(Creature effector, Creature effected)
	{
		return getEffects(effector, effected, ShieldDefense.FAILED, false);
	}
	
	public final List<AbstractEffect> getEffects(Creature effector, Creature effected, ShieldDefense sDef, boolean isBlessedSpiritShot)
	{
		if (!hasEffects() || isPassive())
			return Collections.emptyList();
		
		// Doors, siege flags and dead creatures cannot receive any effects.
		if (effected instanceof Door || effected instanceof SiegeFlag || effected.isDead())
			return Collections.emptyList();
		
		// If caster isn't the target.
		if (effector != effected)
		{
			// Check range distance.
			if (getEffectRange() > 0 && effector != null && !effector.isIn3DRadius(effected, getEffectRange()))
				return Collections.emptyList();
			
			if (isOffensive() || isDebuff())
			{
				// Check if target is invulnerable.
				if (effected.isInvul())
					return Collections.emptyList();
				
				// Check if caster can give damage.
				if (effector != null)
				{
					final Player effectorPlayer = effector.getActingPlayer();
					if (effectorPlayer != null && !effectorPlayer.getAccessLevel().canGiveDamage())
						return Collections.emptyList();
				}
			}
		}
		
		// Perfect block, don't bother going further.
		if (sDef == ShieldDefense.PERFECT)
			return Collections.emptyList();
		
		final List<AbstractEffect> effects = new ArrayList<>(_effectTemplates.size());
		
		for (EffectTemplate template : _effectTemplates)
		{
			boolean success = true;
			
			if (template.getEffectPower() > -1)
				success = Formulas.calcEffectSuccess(effector, effected, template, this, isBlessedSpiritShot);
			
			if (success)
			{
				final AbstractEffect effect = template.getEffect(effector, effected, this);
				if (effect != null)
				{
					effect.scheduleEffect();
					effects.add(effect);
				}
			}
			// display fail message only for effects with icons
			else if (template.showIcon() && effector instanceof Player player)
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(effected).addSkillName(this));
		}
		return effects;
	}
	
	public final List<AbstractEffect> getEffects(Cubic effector, Creature effected)
	{
		return getEffects(effector, effected, ShieldDefense.FAILED, false);
	}
	
	public final List<AbstractEffect> getEffects(Cubic effector, Creature effected, ShieldDefense sDef, boolean isBlessedSpiritShot)
	{
		return getEffects(effector.getOwner(), effected, sDef, isBlessedSpiritShot);
	}
	
	public final List<AbstractEffect> getEffectsSelf(Creature effector)
	{
		if (!hasSelfEffects() || isPassive())
			return Collections.emptyList();
		
		final List<AbstractEffect> effects = new ArrayList<>(_effectTemplatesSelf.size());
		
		for (EffectTemplate template : _effectTemplatesSelf)
		{
			final AbstractEffect effect = template.getEffect(effector, effector, this);
			if (effect != null)
			{
				effect.setSelfEffect();
				effect.scheduleEffect();
				
				effects.add(effect);
			}
		}
		return effects;
	}
	
	public final void attach(FuncTemplate f)
	{
		if (_funcTemplates == null)
			_funcTemplates = new ArrayList<>(1);
		
		_funcTemplates.add(f);
	}
	
	public final void attach(EffectTemplate effect)
	{
		if (_effectTemplates == null)
			_effectTemplates = new ArrayList<>(1);
		
		_effectTemplates.add(effect);
	}
	
	public final void attachSelf(EffectTemplate effect)
	{
		if (_effectTemplatesSelf == null)
			_effectTemplatesSelf = new ArrayList<>(1);
		
		_effectTemplatesSelf.add(effect);
	}
	
	public final void attach(Condition c, boolean itemOrWeapon)
	{
		if (itemOrWeapon)
		{
			if (_itemPreCondition == null)
				_itemPreCondition = new ArrayList<>();
			
			_itemPreCondition.add(c);
		}
		else
		{
			if (_preCondition == null)
				_preCondition = new ArrayList<>();
			
			_preCondition.add(c);
		}
	}
	
	private ExtractableSkill parseExtractableSkill(int skillId, int skillLvl, String values)
	{
		final List<ExtractableProductItem> products = new ArrayList<>();
		
		for (String prodList : values.split(";"))
		{
			try
			{
				final String[] prodData = prodList.split(",");
				final int length = prodData.length - 1;
				
				final List<IntIntHolder> items = new ArrayList<>(length / 2);
				for (int j = 0; j < length; j++)
				{
					final int prodId = Integer.parseInt(prodData[j]);
					final int quantity = Integer.parseInt(prodData[j += 1]);
					
					items.add(new IntIntHolder(prodId, quantity));
				}
				final double chance = Double.parseDouble(prodData[length]);
				
				products.add(new ExtractableProductItem(items, chance));
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't properly parse extractable skill data for id: {} and level: {}.", skillId, skillLvl);
			}
		}
		
		if (products.isEmpty())
			LOGGER.warn("No production items were found for id: {} and level: {}.", skillId, skillLvl);
		
		return new ExtractableSkill(SkillTable.getSkillHashCode(this), products);
	}
	
	public ExtractableSkill getExtractableSkill()
	{
		return _extractableItems;
	}
	
	public boolean isDamage()
	{
		switch (_skillType)
		{
			case PDAM:
			case MDAM:
			case DRAIN:
			case BLOW:
			case CPDAMPERCENT:
			case DEATHLINK:
			case CHARGEDAM:
			case FATAL:
			case SIGNET_CASTTIME:
				return true;
		}
		return false;
	}
	
	public boolean isAOE()
	{
		switch (_targetType)
		{
			case AREA:
			case AURA:
			case BEHIND_AURA:
			case FRONT_AREA:
			case FRONT_AURA:
				return true;
		}
		return false;
	}
	
	public boolean canTargetCorpse()
	{
		switch (_targetType)
		{
			case AREA_CORPSE_MOB:
			case CORPSE:
			case CORPSE_MOB:
			case CORPSE_PET:
			case CORPSE_PLAYER:
			case CORPSE_ALLY:
				return true;
		}
		return false;
	}
	
	public final Creature[] getTargetList(Creature caster, Creature target)
	{
		final ITargetHandler handler = TargetHandler.getInstance().getHandler(getTargetType());
		if (handler != null)
			return handler.getTargetList(caster, target, this);
		
		caster.sendMessage(getTargetType() + " skill target type isn't currently handled.");
		return ITargetHandler.EMPTY_TARGET_ARRAY;
	}
	
	public final Creature getFinalTarget(Creature caster, Creature target)
	{
		final ITargetHandler handler = TargetHandler.getInstance().getHandler(getTargetType());
		if (handler != null)
			return handler.getFinalTarget(caster, target, this);
		
		caster.sendMessage(getTargetType() + " skill target type isn't currently handled.");
		return null;
	}
	
	public final boolean meetCastConditions(Playable caster, Creature target, boolean isCtrlPressed)
	{
		final ITargetHandler handler = TargetHandler.getInstance().getHandler(getTargetType());
		if (handler != null)
			return handler.meetCastConditions(caster, target, this, isCtrlPressed);
		
		caster.sendMessage(getTargetType() + " skill target type isn't currently handled.");
		return false;
	}
	
	@Override
	public String toString()
	{
		return "" + _name + "[id=" + _id + ",lvl=" + _level + "]";
	}
}