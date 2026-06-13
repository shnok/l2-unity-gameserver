package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class L2SkillDrain extends L2Skill
{
	private final float _absorbPart;
	private final int _absorbAbs;
	
	public L2SkillDrain(StatSet set)
	{
		super(set);
		
		_absorbPart = set.getFloat("absorbPart", 0.f);
		_absorbAbs = set.getInteger("absorbAbs", 0);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		final boolean sps = creature.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		final boolean isPlayable = creature instanceof Playable;
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (targetCreature.isAlikeDead() && getTargetType() != SkillTargetType.CORPSE_MOB)
				continue;
			
			// No effect on invulnerable chars unless they cast it themselves.
			if (creature != targetCreature && targetCreature.isInvul())
				continue;
			
			final boolean isCrit = Formulas.calcMCrit(creature, targetCreature, this);
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, this, false);
			final int damage = (int) Formulas.calcMagicDam(creature, targetCreature, this, sDef, sps, bsps, isCrit);
			
			if (damage > 0)
			{
				int targetCp = 0;
				if (target instanceof Player targetPlayer)
					targetCp = (int) targetPlayer.getStatus().getCp();
				
				final int targetHp = (int) targetCreature.getStatus().getHp();
				
				int drain = 0;
				if (isPlayable && targetCp > 0)
				{
					if (damage < targetCp)
						drain = 0;
					else
						drain = damage - targetCp;
				}
				else if (damage > targetHp)
					drain = targetHp;
				else
					drain = damage;
				
				creature.getStatus().addHp(_absorbAbs + _absorbPart * drain);
				
				// That section is launched for drain skills made on ALIVE targets.
				if (!targetCreature.isDead() || getTargetType() != SkillTargetType.CORPSE_MOB)
				{
					// Manage cast break of the target (calculating rate, sending message...)
					Formulas.calcCastBreak(targetCreature, damage);
					
					creature.sendDamageMessage(targetCreature, damage, isCrit, false, false);
					
					if (hasEffects() && getTargetType() != SkillTargetType.CORPSE_MOB)
					{
						// ignoring vengance-like reflections
						if ((Formulas.calcSkillReflect(targetCreature, this) & Formulas.SKILL_REFLECT_SUCCEED) > 0)
						{
							creature.stopSkillEffects(getId());
							getEffects(targetCreature, creature);
						}
						else
						{
							// activate attacked effects, if any
							targetCreature.stopSkillEffects(getId());
							if (Formulas.calcSkillSuccess(creature, targetCreature, this, sDef, bsps))
								getEffects(creature, targetCreature);
							else
								creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(getId()));
						}
					}
					targetCreature.reduceCurrentHp(damage, creature, this);
				}
			}
		}
		
		if (hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			getEffectsSelf(creature);
		}
		
		creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, isStaticReuse());
	}
	
	public float getAbsorbPart()
	{
		return _absorbPart;
	}
	
	public int getAbsorbAbs()
	{
		return _absorbAbs;
	}
}