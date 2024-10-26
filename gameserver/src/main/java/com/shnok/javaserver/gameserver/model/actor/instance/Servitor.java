package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.concurrent.Future;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.actors.NpcRace;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameManager;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SetSummonRemainTime;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.l2skills.L2SkillSummon;
import com.shnok.javaserver.gameserver.taskmanager.DecayTaskManager;

public class Servitor extends Summon
{
	private float _expPenalty = 0;
	
	private int _itemConsumeId = 0;
	private int _itemConsumeCount = 0;
	private int _itemConsumeSteps = 0;
	private int _totalLifeTime = 1200000;
	private int _timeLostIdle = 1000;
	private int _timeLostActive = 1000;
	private int _timeRemaining;
	private int _nextItemConsumeTime;
	private int _lastShownTimeRemaining;
	
	private Future<?> _summonLifeTask;
	
	public Servitor(int objectId, NpcTemplate template, Player owner, L2Skill skill)
	{
		super(objectId, template, owner);
		
		if (skill != null)
		{
			final L2SkillSummon summonSkill = (L2SkillSummon) skill;
			
			_itemConsumeId = summonSkill.getItemConsumeIdOT();
			_itemConsumeCount = summonSkill.getItemConsumeOT();
			_itemConsumeSteps = summonSkill.getItemConsumeSteps();
			_totalLifeTime = summonSkill.getTotalLifeTime();
			_timeLostIdle = summonSkill.getTimeLostIdle();
			_timeLostActive = summonSkill.getTimeLostActive();
		}
		_timeRemaining = _totalLifeTime;
		_lastShownTimeRemaining = _totalLifeTime;
		
		if (_itemConsumeId == 0 || _itemConsumeSteps == 0)
			_nextItemConsumeTime = -1; // do not consume
		else
			_nextItemConsumeTime = _totalLifeTime - _totalLifeTime / (_itemConsumeSteps + 1);
		
		_summonLifeTask = ThreadPool.scheduleAtFixedRate(this::processLifeTime, 1000, 1000);
	}
	
	@Override
	public void addItem(ItemInstance item, boolean sendMessage)
	{
		// Do nothing.
	}
	
	@Override
	public ItemInstance addItem(int itemId, int count, boolean sendMessage)
	{
		return null;
	}
	
	@Override
	public int getSummonType()
	{
		return 1;
	}
	
	@Override
	public void sendDamageMessage(Creature target, int damage, boolean mcrit, boolean pcrit, boolean miss)
	{
		if (miss || getOwner() == null)
			return;
		
		// Prevents the double spam of system messages, if the target is the owning player.
		if (target.getObjectId() != getOwner().getObjectId())
		{
			if (pcrit || mcrit)
				sendPacket(SystemMessageId.CRITICAL_HIT_BY_SUMMONED_MOB);
			
			if (target.isInvul())
			{
				if (target.isParalyzed())
					sendPacket(SystemMessageId.OPPONENT_PETRIFIED);
				else
					sendPacket(SystemMessageId.ATTACK_WAS_BLOCKED);
			}
			else
				sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SUMMON_GAVE_DAMAGE_S1).addNumber(damage));
			
			if (getOwner().isInOlympiadMode() && target instanceof Player targetPlayer && targetPlayer.isInOlympiadMode() && targetPlayer.getOlympiadGameId() == getOwner().getOlympiadGameId())
				OlympiadGameManager.getInstance().notifyCompetitorDamage(getOwner(), damage);
		}
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
			return false;
		
		// Stop the life time task.
		if (_summonLifeTask != null)
		{
			_summonLifeTask.cancel(false);
			_summonLifeTask = null;
		}
		
		// Send message.
		sendPacket(SystemMessageId.SERVITOR_PASSED_AWAY);
		
		// Run the decay task.
		DecayTaskManager.getInstance().add(this, getTemplate().getCorpseTime());
		
		return true;
		
	}
	
	@Override
	public void unSummon(Player owner)
	{
		if (_summonLifeTask != null)
		{
			_summonLifeTask.cancel(false);
			_summonLifeTask = null;
		}
		super.unSummon(owner);
	}
	
	@Override
	public boolean destroyItem(int objectId, int count, boolean sendMessage)
	{
		return getOwner().destroyItem(objectId, count, sendMessage);
	}
	
	@Override
	public boolean destroyItemByItemId(int itemId, int count, boolean sendMessage)
	{
		return getOwner().destroyItemByItemId(itemId, count, sendMessage);
	}
	
	@Override
	public boolean isUndead()
	{
		return getTemplate().getRace() == NpcRace.UNDEAD;
	}
	
	public void setExpPenalty(float expPenalty)
	{
		_expPenalty = expPenalty;
	}
	
	public float getExpPenalty()
	{
		return _expPenalty;
	}
	
	public int getItemConsumeCount()
	{
		return _itemConsumeCount;
	}
	
	public int getItemConsumeId()
	{
		return _itemConsumeId;
	}
	
	public int getItemConsumeSteps()
	{
		return _itemConsumeSteps;
	}
	
	public int getNextItemConsumeTime()
	{
		return _nextItemConsumeTime;
	}
	
	public int getTotalLifeTime()
	{
		return _totalLifeTime;
	}
	
	public int getTimeLostIdle()
	{
		return _timeLostIdle;
	}
	
	public int getTimeLostActive()
	{
		return _timeLostActive;
	}
	
	public int getTimeRemaining()
	{
		return _timeRemaining;
	}
	
	public void setNextItemConsumeTime(int value)
	{
		_nextItemConsumeTime = value;
	}
	
	public void decNextItemConsumeTime(int value)
	{
		_nextItemConsumeTime -= value;
	}
	
	public int decTimeRemaining(int value)
	{
		return _timeRemaining -= value;
	}
	
	public void addExpAndSp(int addToExp, int addToSp)
	{
		getOwner().addExpAndSp(addToExp, addToSp);
	}
	
	private void processLifeTime()
	{
		// Keep old timer.
		final double oldTimeRemaining = getTimeRemaining();
		
		// Decrease remaining time.
		final double newTimeRemaining = decTimeRemaining((isInCombat()) ? getTimeLostActive() : getTimeLostIdle());
		if (newTimeRemaining < 0)
			unSummon(getOwner());
		else if ((newTimeRemaining <= getNextItemConsumeTime()) && (oldTimeRemaining > getNextItemConsumeTime()))
		{
			decNextItemConsumeTime(getTotalLifeTime() / (getItemConsumeSteps() + 1));
			
			// Check if owner has enought itemConsume, if requested.
			if (getItemConsumeCount() > 0 && getItemConsumeId() != 0 && !isDead() && !destroyItemByItemId(getItemConsumeId(), getItemConsumeCount(), true))
				unSummon(getOwner());
		}
		
		// Prevent useless packet-sending when the difference isn't visible.
		if ((_lastShownTimeRemaining - newTimeRemaining) > getTotalLifeTime() / 352)
		{
			sendPacket(new SetSummonRemainTime(getTotalLifeTime(), (int) newTimeRemaining));
			_lastShownTimeRemaining = (int) newTimeRemaining;
			updateEffectIcons();
		}
	}
}