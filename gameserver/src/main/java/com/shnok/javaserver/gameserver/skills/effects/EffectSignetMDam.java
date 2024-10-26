package com.shnok.javaserver.gameserver.skills.effects;

import java.util.List;

import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.EffectPoint;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillLaunched;
import com.shnok.javaserver.gameserver.network.serverpackets.MagicSkillUse;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSignetCasttime;

public class EffectSignetMDam extends AbstractEffect
{
	private EffectPoint _actor;
	
	public EffectSignetMDam(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.SIGNET_GROUND;
	}
	
	@Override
	public boolean onStart()
	{
		if (!(_skill instanceof L2SkillSignetCasttime ssc))
			return false;
		
		final NpcTemplate template = NpcData.getInstance().getTemplate(ssc.effectNpcId);
		if (template == null)
			return false;
		
		final EffectPoint effectPoint = new EffectPoint(IdFactory.getInstance().getNextId(), template, getEffector());
		effectPoint.getStatus().setMaxHpMp();
		
		Location worldPosition = null;
		if (getEffector() instanceof Player player && getSkill().getTargetType() == SkillTargetType.GROUND)
			worldPosition = player.getCast().getSignetLocation();
		
		effectPoint.setInvul(true);
		effectPoint.spawnMe((worldPosition != null) ? worldPosition : getEffector().getPosition());
		
		_actor = effectPoint;
		return true;
		
	}
	
	@Override
	public boolean onActionTime()
	{
		if (getCount() >= getTemplate().getCounter() - 2)
			return true; // do nothing first 2 times
			
		final Player caster = (Player) getEffector();
		final int mpConsume = getSkill().getMpConsume();
		
		if (mpConsume > caster.getStatus().getMp())
		{
			caster.sendPacket(SystemMessageId.SKILL_REMOVED_DUE_LACK_MP);
			return false;
		}
		
		caster.getStatus().reduceMp(mpConsume);
		
		final List<Creature> list = _actor.getKnownTypeInRadius(Creature.class, _skill.getSkillRadius(), creature -> !creature.isDead() && !(creature instanceof Door) && !creature.isInsideZone(ZoneId.PEACE));
		if (list.isEmpty())
			return true;
		
		final Creature[] targets = list.toArray(new Creature[list.size()]);
		for (Creature target : targets)
		{
			final boolean isCrit = Formulas.calcMCrit(caster, target, getSkill());
			final ShieldDefense sDef = Formulas.calcShldUse(caster, target, getSkill(), false);
			final boolean sps = caster.isChargedShot(ShotType.SPIRITSHOT);
			final boolean bsps = caster.isChargedShot(ShotType.BLESSED_SPIRITSHOT);
			final int damage = (int) Formulas.calcMagicDam(caster, target, getSkill(), sDef, sps, bsps, isCrit);
			
			if (target instanceof Summon targetSummon)
				targetSummon.getStatus().broadcastStatusUpdate();
			
			if (damage > 0)
			{
				// Manage cast break of the target (calculating rate, sending message...)
				Formulas.calcCastBreak(target, damage);
				
				caster.sendDamageMessage(target, damage, isCrit, false, false);
				target.reduceCurrentHp(damage, caster, getSkill());
			}
			
			_actor.broadcastPacket(new MagicSkillUse(_actor, target, _skill.getId(), _skill.getLevel(), 0, 0));
		}
		_actor.broadcastPacket(new MagicSkillLaunched(_actor, _skill, targets));
		return true;
	}
	
	@Override
	public void onExit()
	{
		if (_actor != null)
			_actor.deleteMe();
	}
}