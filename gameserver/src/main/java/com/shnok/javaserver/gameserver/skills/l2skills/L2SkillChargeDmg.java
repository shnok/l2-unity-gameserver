package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class L2SkillChargeDmg extends L2Skill
{
	public L2SkillChargeDmg(StatSet set)
	{
		super(set);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (creature.isAlikeDead())
			return;
		
		double modifier = 0;
		
		if (creature instanceof Player player)
			modifier = 0.8 + 0.2 * (player.getCharges() + getNumCharges());
		
		final boolean ss = creature.isChargedShot(ShotType.SOULSHOT);
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (targetCreature.isAlikeDead())
				continue;
			
			// Calculate skill evasion.
			boolean skillIsEvaded = Formulas.calcPhysicalSkillEvasion(targetCreature, this);
			if (skillIsEvaded)
			{
				if (creature instanceof Player player)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DODGES_ATTACK).addCharName(targetCreature));
				
				if (target instanceof Player targetPlayer)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(creature));
				
				continue;
			}
			
			final boolean isCrit = getBaseCritRate() > 0 && Formulas.calcCrit(getBaseCritRate() * 10 * Formulas.getSTRBonus(creature));
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, this, isCrit);
			final byte reflect = Formulas.calcSkillReflect(targetCreature, this);
			
			if (hasEffects())
			{
				if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0)
				{
					creature.stopSkillEffects(getId());
					
					getEffects(targetCreature, creature);
				}
				else
				{
					targetCreature.stopSkillEffects(getId());
					
					if (Formulas.calcSkillSuccess(creature, targetCreature, this, sDef, true))
						getEffects(creature, targetCreature, sDef, false);
					else
						creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(this));
				}
			}
			
			double damage = Formulas.calcPhysicalSkillDamage(creature, targetCreature, this, sDef, isCrit, ss);
			damage *= modifier;
			
			// Skill counter.
			if ((reflect & Formulas.SKILL_COUNTER) != 0)
			{
				if (target instanceof Player targetPlayer)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COUNTERED_S1_ATTACK).addCharName(creature));
				
				if (creature instanceof Player player)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PERFORMING_COUNTERATTACK).addCharName(targetCreature));
				
				// Calculate the counter percent.
				final double counteredPercent = targetCreature.getStatus().calcStat(Stats.COUNTER_SKILL_PHYSICAL, 0, targetCreature, null) / 100.;
				
				damage *= counteredPercent;
				
				// Reduce caster HPs.
				creature.reduceCurrentHp(damage, targetCreature, this);
				
				// Send damage message.
				targetCreature.sendDamageMessage(creature, (int) damage, false, false, false);
			}
			else
			{
				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(targetCreature, damage);
				
				// Reduce target HPs.
				targetCreature.reduceCurrentHp(damage, creature, this);
				
				// Send damage message.
				creature.sendDamageMessage(targetCreature, (int) damage, false, false, false);
			}
		}
		
		if (hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			getEffectsSelf(creature);
		}
		
		creature.setChargedShot(ShotType.SOULSHOT, isStaticReuse());
	}
}