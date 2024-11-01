package com.shnok.javaserver.gameserver.enums.actors;

import com.shnok.javaserver.gameserver.enums.skills.Stats;

public enum NpcRace
{
	DUMMY(null, null, -1),
	UNDEAD(null, null, 4290),
	MAGIC_CREATURE(Stats.PATK_MCREATURES, Stats.PDEF_MCREATURES, 4291),
	BEAST(Stats.PATK_BEASTS, Stats.PDEF_BEASTS, 4292),
	ANIMAL(Stats.PATK_ANIMALS, Stats.PDEF_ANIMALS, 4293),
	PLANT(Stats.PATK_PLANTS, Stats.PDEF_PLANTS, 4294),
	HUMANOID(null, null, 4295),
	SPIRIT(null, null, 4296),
	ANGEL(null, null, 4297),
	DEMON(null, null, 4298),
	DRAGON(Stats.PATK_DRAGONS, Stats.PDEF_DRAGONS, 4299),
	GIANT(Stats.PATK_GIANTS, Stats.PDEF_GIANTS, 4300),
	BUG(Stats.PATK_INSECTS, Stats.PDEF_INSECTS, 4301),
	FAIRIE(null, null, 4302),
	HUMAN(null, null, -1),
	ELVE(null, null, -1),
	DARKELVE(null, null, -1),
	ORC(null, null, -1),
	DWARVE(null, null, -1),
	OTHER(null, null, -1),
	NON_LIVING_BEING(null, null, -1),
	SIEGE_WEAPON(null, null, -1),
	DEFENDING_ARMY(null, null, -1),
	MERCENARIE(null, null, -1),
	UNKNOWN_CREATURE(null, null, -1);
	
	public static final NpcRace[] VALUES = values();
	
	private NpcRace(Stats atkStat, Stats resStat, int secondarySkillId)
	{
		_atkStat = atkStat;
		_resStat = resStat;
		_secondarySkillId = secondarySkillId;
	}
	
	private Stats _atkStat;
	private Stats _resStat;
	private int _secondarySkillId;
	
	public Stats getAtkStat()
	{
		return _atkStat;
	}
	
	public Stats getResStat()
	{
		return _resStat;
	}
	
	public int getSecondarySkillId()
	{
		return _secondarySkillId;
	}
	
	public static NpcRace retrieveBySecondarySkillId(int skillId)
	{
		for (NpcRace nr : VALUES)
		{
			if (nr.getSecondarySkillId() == skillId)
				return nr;
		}
		return DUMMY;
	}
}