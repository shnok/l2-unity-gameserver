package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Manadam implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.MANADAM
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		if (creature.isAlikeDead())
			return;
		
		final boolean sps = creature.isChargedShot(ShotType.SPIRITSHOT);
		final boolean bsps = creature.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
		
		for (WorldObject target : targets)
		{
			if (!(target instanceof Creature targetCreature))
				continue;
			
			if (Formulas.calcSkillReflect(targetCreature, skill) == Formulas.SKILL_REFLECT_SUCCEED)
				targetCreature = creature;
			
			boolean acted = Formulas.calcMagicAffected(creature, targetCreature, skill);
			if (targetCreature.isInvul() || !acted)
				creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.MISSED_TARGET));
			else
			{
				if (skill.hasEffects())
				{
					targetCreature.stopSkillEffects(skill.getId());
					
					final ShieldDefense sDef = Formulas.calcShldUse(creature, targetCreature, skill, false);
					if (Formulas.calcSkillSuccess(creature, targetCreature, skill, sDef, bsps))
						skill.getEffects(creature, targetCreature, sDef, bsps);
					else
						creature.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(targetCreature).addSkillName(skill));
				}
				
				double damage = Formulas.calcManaDam(creature, targetCreature, skill, sps, bsps);
				
				double mp = (damage > targetCreature.getStatus().getMp() ? targetCreature.getStatus().getMp() : damage);
				targetCreature.getStatus().reduceMp(mp);
				if (damage > 0)
				{
					targetCreature.stopEffects(EffectType.SLEEP);
					targetCreature.stopEffects(EffectType.IMMOBILE_UNTIL_ATTACKED);
				}
				
				if (target instanceof Player targetPlayer)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S2_MP_HAS_BEEN_DRAINED_BY_S1).addCharName(creature).addNumber((int) mp));
				
				if (creature instanceof Player player)
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOUR_OPPONENTS_MP_WAS_REDUCED_BY_S1).addNumber((int) mp));
			}
		}
		
		if (skill.hasSelfEffects())
		{
			final AbstractEffect effect = creature.getFirstEffect(skill.getId());
			if (effect != null && effect.isSelfEffect())
				effect.exit();
			
			skill.getEffectsSelf(creature);
		}
		creature.setChargedShot(bsps ? ShotType.BLESSED_SPIRITSHOT : ShotType.SPIRITSHOT, skill.isStaticReuse());
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}