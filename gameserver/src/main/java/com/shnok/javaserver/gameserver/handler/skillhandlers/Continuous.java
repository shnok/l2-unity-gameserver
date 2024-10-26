package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.commons.util.ArraysUtil;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.enums.AiEventType;
import net.sf.l2j.gameserver.enums.items.ShotType;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.ShieldDefense;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Attackable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.ClanHallManagerNpc;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.effects.EffectFear;

public class Continuous implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.BUFF,
		SkillType.DEBUFF,
		SkillType.DOT,
		SkillType.MDOT,
		SkillType.POISON,
		SkillType.BLEED,
		SkillType.HOT,
		SkillType.MPHOT,
		SkillType.FEAR,
		SkillType.CONT,
		SkillType.WEAKNESS,
		SkillType.REFLECT,
		SkillType.AGGDEBUFF,
		SkillType.FUSION
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		final Player player = creature.getActingPlayer();
		
		if (skill.getEffectId() != 0)
		{
			L2Skill sk = SkillTable.getInstance().getInfo(skill.getEffectId(), skill.getEffectLvl() == 0 ? 1 : skill.getEffectLvl());
			if (sk != null)
				skill = sk;
		}
		
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject obj : targets)
		{
			if (!(obj instanceof Creature targetCreature))
				continue;
			
			if (Formulas.calcSkillReflect(targetCreature, skill) == Formulas.SKILL_REFLECT_SUCCEED)
				targetCreature = creature;
			
			switch (skill.getSkillType())
			{
				case BUFF:
					// Target under buff immunity.
					if (targetCreature.getFirstEffect(EffectType.BLOCK_BUFF) != null)
						continue;
					
					// Player holding a cursed weapon can't be buffed and can't buff
					if (!(creature instanceof ClanHallManagerNpc) && targetCreature != creature)
					{
						if (targetCreature instanceof Player targetPlayer && targetPlayer.isCursedWeaponEquipped())
							continue;
						
						if (player != null && player.isCursedWeaponEquipped())
							continue;
					}
					break;
				
				case HOT, MPHOT:
					if (creature.isInvul())
						continue;
					break;
				case FEAR:
					if (targetCreature instanceof Playable && ArraysUtil.contains(EffectFear.DOESNT_AFFECT_PLAYABLE, skill.getId()))
						continue;
			}
			
			// Target under debuff immunity.
			if (skill.isOffensive() && targetCreature.getFirstEffect(EffectType.BLOCK_DEBUFF) != null)
				continue;
			
			boolean acted = true;
			ShieldDefense sDef = ShieldDefense.FAILED;
			
			if (skill.isOffensive() || skill.isDebuff())
			{
				sDef = Formulas.calcShldUse(creature, targetCreature, skill, false);
				acted = Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps);
			}
			
			if (acted)
			{
				// TODO Not necessary
				if (skill.isToggle())
					targetCreature.stopSkillEffects(skill.getId());
				
				skill.getEffects(creature, targetCreature, sDef, bsps);
				
				if (skill.getSkillType() == SkillType.AGGDEBUFF)
				{
					if (targetCreature instanceof Attackable targetAttackable)
						targetAttackable.getAI().notifyEvent(AiEventType.AGGRESSION, creature, (int) skill.getPower());
					else if (targetCreature instanceof Playable targetPlayable)
					{
						if (targetPlayable.getTarget() == creature)
							targetPlayable.getAI().tryToAttack(creature, false, false);
						else
							targetPlayable.setTarget(creature);
					}
				}
			}
			else
				creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
			
			// Possibility of a lethal strike
			Formulas.calcLethalHit(creature, targetCreature, skill);
		}
		
		if (skill.hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			skill.getEffectsSelf(creature);
		}
		
		if (!skill.isPotion() && !skill.isToggle())
			creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}