package com.shnok.javaserver.gameserver.skills.effects;

import net.sf.l2j.gameserver.enums.skills.EffectFlag;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public class EffectChameleonRest extends AbstractEffect
{
	public EffectChameleonRest(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.RELAXING;
	}
	
	@Override
	public boolean onStart()
	{
		((Player) getEffected()).sitDown();
		
		return super.onStart();
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getEffected().isDead())
			return false;
		
		// Only cont skills shouldn't end
		if (getSkill().getSkillType() != SkillType.CONT)
			return false;
		
		if (getEffected() instanceof Player targetPlayer && !targetPlayer.isSitting())
			return false;
		
		if (getTemplate().getValue() > getEffected().getStatus().getMp())
		{
			getEffected().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP));
			return false;
		}
		
		getEffected().getStatus().reduceMp(getTemplate().getValue());
		return true;
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.SILENT_MOVE.getMask() | EffectFlag.RELAXING.getMask();
	}
}