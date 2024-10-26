package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.commons.math.MathUtil;
import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.items.WeaponType;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.FlyType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.geoengine.GeoEngine;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.Point2D;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.FlyToLocation;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.ValidateLocation;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.effects.EffectFear;

public class Pdam implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.PDAM,
		SkillType.FATAL
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (creature.isAlikeDead())
			return;
		
		final boolean ss = creature.isChargedShot(ShotType.SOULSHOT);
		final ItemInstance weapon = creature.getActiveWeaponInstance();
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (targetCreature.isDead())
				continue;
			
			if (target instanceof Playable && ArraysUtil.contains(EffectFear.DOESNT_AFFECT_PLAYABLE, skill.getId()))
				continue;
			
			// Calculate skill evasion. As Dodge blocks only melee skills, make an exception with bow weapons.
			if (weapon != null && weapon.getItemType() != WeaponType.BOW && Formulas.calcPhysicalSkillEvasion(targetCreature, skill))
			{
				if (creature instanceof Player player)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DODGES_ATTACK).addCharName(targetCreature));
				
				if (target instanceof Player targetPlayer)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AVOIDED_S1_ATTACK).addCharName(creature));
				
				// no futher calculations needed.
				continue;
			}
			
			final boolean isCrit = skill.getBaseCritRate() > 0 && Formulas.calcCrit(skill.getBaseCritRate() * 10 * Formulas.getSTRBonus(creature));
			final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, skill, isCrit);
			final byte reflect = Formulas.calcSkillReflect(targetCreature, skill);
			
			if (skill.hasEffects() && targetCreature.getFirstEffect(EffectType.BLOCK_DEBUFF) == null)
			{
				if ((reflect & Formulas.SKILL_REFLECT_SUCCEED) != 0)
				{
					creature.stopSkillEffects(skill.getId());
					
					skill.getEffects(targetCreature, creature);
				}
				else
				{
					targetCreature.stopSkillEffects(skill.getId());
					
					skill.getEffects(creature, targetCreature, sDef, false);
				}
			}
			
			double damage = Formulas.calcPhysicalSkillDamage(creature, targetCreature, skill, sDef, isCrit, ss);
			
			if (damage > 0)
			{
				// Skill counter.
				if ((reflect & Formulas.SKILL_COUNTER) != 0)
				{
					if (target instanceof Player targetPlayer)
						targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.COUNTERED_S1_ATTACK).addCharName(creature));
					
					if (creature instanceof Player player)
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PERFORMING_COUNTERATTACK).addCharName(targetCreature));
					
					// Calculate the counter percent.
					damage *= targetCreature.getStatus().calcStat(Stats.COUNTER_SKILL_PHYSICAL, 0, targetCreature, null) / 100.;
					
					// Reduce caster HPs.
					creature.reduceCurrentHp(damage, targetCreature, skill);
					
					// Send damage message.
					targetCreature.sendDamageMessage(creature, (int) damage, false, false, false);
				}
				else
				{
					// Manage cast break of the target (calculating rate, sending message...)
					Formulas.calcCastBreak(targetCreature, damage);
					
					// Reduce target HPs.
					targetCreature.reduceCurrentHp(damage, creature, skill);
					
					// Send damage message.
					creature.sendDamageMessage(targetCreature, (int) damage, false, false, false);
				}
				
				// Possibility of a lethal strike.
				Formulas.calcLethalHit(creature, targetCreature, skill);
			}
			else
				creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
		}
		
		if (skill.hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			skill.getEffectsSelf(creature);
		}
		
		if (skill.getFlyType() == FlyType.CHARGE)
		{
			int heading = creature.getHeading();
			if (targets.length > 0)
				heading = MathUtil.calculateHeadingFrom(creature.getX(), creature.getY(), targets[0].getX(), targets[0].getY());
			
			final Point2D chargePoint = MathUtil.getNewLocationByDistanceAndHeading(creature.getX(), creature.getY(), heading, skill.getFlyRadius());
			
			final Location chargeLoc = GeoEngine.getInstance().getValidLocation(creature, chargePoint.getX(), chargePoint.getY(), creature.getZ());
			
			creature.broadcastPacket(new FlyToLocation(creature, chargeLoc.getX(), chargeLoc.getY(), chargeLoc.getZ(), FlyType.CHARGE));
			
			creature.setXYZ(chargeLoc.getX(), chargeLoc.getY(), chargeLoc.getZ());
			
			creature.broadcastPacket(new ValidateLocation(creature));
		}
		
		if (skill.isSuicideAttack())
			creature.doDie(creature);
		
		creature.setChargedShot(ShotType.SOULSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}