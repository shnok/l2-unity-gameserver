package com.shnok.javaserver.gameserver.enums.skills;

import java.lang.reflect.Constructor;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillAppearance;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillChargeDmg;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillCreateItem;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillDefault;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillDrain;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSeed;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSiegeFlag;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSignet;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSignetCasttime;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSpawn;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSummon;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillTeleport;

public enum SkillType
{
	// Damage
	PDAM,
	FATAL,
	MDAM,
	CPDAMPERCENT,
	MANADAM,
	DOT,
	MDOT,
	DRAIN_SOUL,
	DRAIN(L2SkillDrain.class),
	DEATHLINK,
	BLOW,
	SIGNET(L2SkillSignet.class),
	SIGNET_CASTTIME(L2SkillSignetCasttime.class),
	SEED(L2SkillSeed.class),
	REAL_DAMAGE,
	
	// Disablers
	BLEED,
	POISON,
	STUN,
	ROOT,
	CONFUSION,
	FEAR,
	SLEEP,
	MUTE,
	PARALYZE,
	WEAKNESS,
	
	// hp, mp, cp
	HEAL,
	MANAHEAL,
	COMBATPOINTHEAL,
	HOT,
	MPHOT,
	BALANCE_LIFE,
	HEAL_STATIC,
	MANARECHARGE,
	HEAL_PERCENT,
	MANAHEAL_PERCENT,
	
	GIVE_SP,
	
	// Aggro
	AGGDAMAGE,
	AGGREDUCE,
	AGGREMOVE,
	AGGREDUCE_CHAR,
	AGGDEBUFF,
	
	// Fishing
	FISHING,
	PUMPING,
	REELING,
	
	// MISC
	UNLOCK,
	UNLOCK_SPECIAL,
	DELUXE_KEY_UNLOCK,
	ENCHANT_ARMOR,
	ENCHANT_WEAPON,
	SOULSHOT,
	SPIRITSHOT,
	SIEGE_FLAG(L2SkillSiegeFlag.class),
	TAKE_CASTLE,
	SOW,
	HARVEST,
	GET_PLAYER,
	DUMMY,
	INSTANT_JUMP,
	
	// Creation
	COMMON_CRAFT,
	DWARVEN_CRAFT,
	CREATE_ITEM(L2SkillCreateItem.class),
	EXTRACTABLE,
	EXTRACTABLE_FISH,
	
	// Summons
	SUMMON(L2SkillSummon.class),
	FEED_PET,
	STRIDER_SIEGE_ASSAULT,
	ERASE,
	BETRAY,
	SPAWN(L2SkillSpawn.class),
	
	// Cancel
	CANCEL,
	MAGE_BANE,
	WARRIOR_BANE,
	
	NEGATE,
	CANCEL_DEBUFF,
	
	BUFF,
	DEBUFF,
	PASSIVE,
	CONT,
	
	RESURRECT,
	CHARGEDAM(L2SkillChargeDmg.class),
	LUCK,
	RECALL(L2SkillTeleport.class),
	TELEPORT(L2SkillTeleport.class),
	SUMMON_FRIEND,
	SUMMON_PARTY,
	SUMMON_CREATURE,
	REFLECT,
	SPOIL,
	SWEEP,
	FAKE_DEATH,
	BEAST_FEED,
	FUSION,
	
	CHANGE_APPEARANCE(L2SkillAppearance.class),
	
	// Skill is done within the core.
	COREDONE,
	
	// unimplemented
	NOTDONE;
	
	private final Class<? extends L2Skill> _class;
	
	public L2Skill makeSkill(StatSet set)
	{
		try
		{
			Constructor<? extends L2Skill> c = _class.getConstructor(StatSet.class);
			
			return c.newInstance(set);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private SkillType()
	{
		_class = L2SkillDefault.class;
	}
	
	private SkillType(Class<? extends L2Skill> classType)
	{
		_class = classType;
	}
}