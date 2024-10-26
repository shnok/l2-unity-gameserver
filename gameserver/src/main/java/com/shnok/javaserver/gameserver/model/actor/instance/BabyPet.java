package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.concurrent.Future;

import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * A BabyPet can heal his owner. It got 2 heal power, weak or strong.
 * <ul>
 * <li>If the owner's HP is more than 80%, do nothing.</li>
 * <li>If the owner's HP is under 15%, have 75% chances of using a strong heal.</li>
 * <li>Otherwise, have 25% chances for weak heal.</li>
 * </ul>
 */
public final class BabyPet extends Pet
{
	private Future<?> _castTask;
	
	public BabyPet(int objectId, NpcTemplate template, Player owner, ItemInstance control)
	{
		super(objectId, template, owner, control);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		startCastTask();
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
			return false;
		
		stopCastTask();
		
		return true;
	}
	
	@Override
	public synchronized void unSummon(Player owner)
	{
		stopCastTask();
		
		super.unSummon(owner);
	}
	
	@Override
	public void doRevive()
	{
		super.doRevive();
		
		startCastTask();
	}
	
	@Override
	public final int getSkillLevel(int skillId)
	{
		if (getStatus().getLevel() < 70)
			return Math.max(1, getStatus().getLevel() / 10);
		
		return Math.min(12, 7 + ((getStatus().getLevel() - 70) / 5));
	}
	
	private final void startCastTask()
	{
		if (_castTask == null && !isDead())
			_castTask = ThreadPool.scheduleAtFixedRate(this::castSkill, 3000, 1000);
	}
	
	private final void stopCastTask()
	{
		if (_castTask != null)
		{
			_castTask.cancel(false);
			_castTask = null;
		}
	}
	
	private void castSkill()
	{
		final Player owner = getOwner();
		if (owner == null || owner.isDead() || owner.isInvul())
			return;
		
		final double hpRatio = owner.getStatus().getHpRatio();
		
		if (Rnd.get(100) <= 25)
		{
			final L2Skill playfulHeal = SkillTable.getInstance().getInfo(4717, getSkillLevel(4717));
			if (!isSkillDisabled(playfulHeal) && getStatus().getMp() >= playfulHeal.getMpConsume() && hpRatio < 0.8)
			{
				getAI().tryToCast(owner, playfulHeal);
				owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_USES_S1).addSkillName(playfulHeal));
				return;
			}
		}
		
		if (Rnd.get(100) <= 75)
		{
			final L2Skill urgentHeal = SkillTable.getInstance().getInfo(4718, getSkillLevel(4718));
			if (!isSkillDisabled(urgentHeal) && getStatus().getMp() >= urgentHeal.getMpConsume() && hpRatio < 0.15)
			{
				getAI().tryToCast(owner, urgentHeal);
				owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_USES_S1).addSkillName(urgentHeal));
				return;
			}
		}
	}
}