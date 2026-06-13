package com.shnok.javaserver.gameserver.model.actor.cast;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.Quest;

/**
 * This class groups all cast data related to a {@link Npc}.
 */
public class NpcCast extends CreatureCast<Npc>
{
	public NpcCast(Npc actor)
	{
		super(actor);
	}
	
	@Override
	protected final void onMagicHitTimer()
	{
		// Content was cleaned meantime, simply return doing nothing.
		if (!isCastingNow())
			return;
		
		final double mpConsume = _actor.getStatus().getMpConsume(_skill);
		if (mpConsume > 0)
		{
			if (mpConsume > _actor.getStatus().getMp())
			{
				stop();
				return;
			}
			
			_actor.getStatus().reduceMp(mpConsume);
		}
		
		final double hpConsume = _skill.getHpConsume();
		if (hpConsume > 0)
		{
			if (hpConsume > _actor.getStatus().getHp())
			{
				stop();
				return;
			}
			
			_actor.getStatus().reduceHp(hpConsume, _actor, true);
		}
		
		callSkill(_skill, _targets, _item);
		
		_castTask = ThreadPool.schedule(this::onMagicFinalizer, _coolTime + 250L);
	}
	
	@Override
	protected void notifyCastFinishToAI(boolean isInterrupted)
	{
		// Stop the current Desire.
		_actor.getAI().clearCurrentDesire();
		
		// Notify the actor with USE_SKILL_FINISHED EventHandler.
		final Creature target = (_targets != null && _targets.length > 0) ? _targets[0] : _target;
		
		for (Quest quest : _actor.getTemplate().getEventQuests(EventHandler.USE_SKILL_FINISHED))
			quest.onUseSkillFinished(_actor, target, _skill, !isInterrupted);
		
		if (!isInterrupted)
			_actor.getAI().notifyEvent(AiEventType.FINISHED_CASTING, null, null);
	}
}