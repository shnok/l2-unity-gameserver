package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class L2SkillElemental extends L2Skill
{
	private final int[] _seeds;
	private final boolean _seedAny;
	
	public L2SkillElemental(StatSet set)
	{
		super(set);
		
		_seeds = new int[3];
		_seeds[0] = set.getInteger("seed1", 0);
		_seeds[1] = set.getInteger("seed2", 0);
		_seeds[2] = set.getInteger("seed3", 0);
		
		_seedAny = set.getInteger("seed_any", 0) == 1;
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		final boolean sps = creature.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (targetCreature.isAlikeDead())
				continue;
			
			boolean charged = true;
			if (!_seedAny)
			{
				for (int _seed : _seeds)
				{
					if (_seed != 0)
					{
						final AbstractEffect effect = targetCreature.getFirstEffect(_seed);
						if (effect == null || !effect.getInUse())
						{
							charged = false;
							break;
						}
					}
				}
			}
			else
			{
				charged = false;
				for (int _seed : _seeds)
				{
					if (_seed != 0)
					{
						final AbstractEffect effect = targetCreature.getFirstEffect(_seed);
						if (effect != null && effect.getInUse())
						{
							charged = true;
							break;
						}
					}
				}
			}
			
			if (!charged)
			{
				creature.sendMessage("Target is not charged by elements.");
				continue;
			}
			
			final boolean isCrit = Formulas.calcMCrit(creature, targetCreature, this);
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, this, false);
			
			int damage = (int) Formulas.calcMagicDam(creature, targetCreature, this, sDef, sps, bsps, isCrit);
			if (damage > 0)
			{
				targetCreature.reduceCurrentHp(damage, creature, this);
				
				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(targetCreature, damage);
				
				creature.sendDamageMessage(targetCreature, damage, false, false, false);
			}
			
			// activate attacked effects, if any
			targetCreature.stopSkillEffects(getId());
			getEffects(creature, targetCreature, sDef, bsps);
		}
		
		creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, isStaticReuse());
	}
}