package com.shnok.javaserver.gameserver.model.actor.ai.type;

import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.Die;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.MoveToLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.MoveToPawn;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class CreatureAI<T extends Creature> extends AbstractAI<T>
{
	public CreatureAI(T actor)
	{
		super(actor);
	}
	
	@Override
	protected void onEvtFinishedAttack()
	{
		if (_nextIntention.isBlank())
			notifyEvent(AiEventType.THINK, null, null);
		else
			doIntention(_nextIntention);
	}
	
	@Override
	protected void onEvtFinishedAttackBow()
	{
		if (!_nextIntention.isBlank())
			doIntention(_nextIntention);
	}
	
	@Override
	protected void onEvtBowAttackReuse()
	{
		if (_nextIntention.isBlank())
			notifyEvent(AiEventType.THINK, null, null);
	}
	
	@Override
	protected void onEvtFinishedCasting()
	{
		if (_nextIntention.isBlank())
			doIdleIntention();
		else
			doIntention(_nextIntention);
	}
	
	@Override
	protected void onEvtArrived()
	{
		if (_currentIntention.getType() == IntentionType.FOLLOW)
			return;
		
		if (_nextIntention.isBlank())
		{
			if (_currentIntention.getType() == IntentionType.MOVE_TO)
				doIdleIntention();
			else
				notifyEvent(AiEventType.THINK, null, null);
		}
		else
			doIntention(_nextIntention);
	}
	
	@Override
	protected void onEvtArrivedBlocked()
	{
		_actor.broadcastPacket(new MoveToLocation(_actor, _actor.getPosition()));
	}
	
	@Override
	protected void onEvtDead()
	{
		stopAITask();
		
		_actor.broadcastPacket(new Die(_actor));
		
		stopAttackStance();
		
		doIdleIntention();
	}
	
	@Override
	protected void onEvtTeleported()
	{
		doIdleIntention();
	}
	
	@Override
	protected void thinkAttack()
	{
		if (_actor.denyAiAction())
			return;
		
		final Creature target = _currentIntention.getFinalTarget();
		if (isTargetLost(target))
			return;
		
		if (_actor.getMove().maybeStartOffensiveFollow(target, _actor.getStatus().getPhysicalAttackRange()))
			return;
		
		if ((_actor.getAttackType() == WeaponType.BOW && _actor.getAttack().isBowCoolingDown()) || _actor.getAttack().isAttackingNow())
		{
			setNextIntention(_currentIntention);
			return;
		}
		
		if (!_actor.getAttack().canAttack(target))
			return;
		
		_actor.getMove().stop();
		
		_actor.getAttack().doAttack(target);
	}
	
	@Override
	protected void thinkCast()
	{
		if (_actor.denyAiAction() || _actor.getAllSkillsDisabled() || _actor.getCast().isCastingNow())
			return;
		
		final Creature target = _currentIntention.getFinalTarget();
		final L2Skill skill = _currentIntention.getSkill();
		
		if (isTargetLost(target, skill))
			return;
		
		if (!_actor.getCast().canAttemptCast(target, skill))
			return;
		
		if (_actor.getMove().maybeStartOffensiveFollow(target, skill.getCastRange()))
		{
			_actor.setWalkOrRun(true);
			return;
		}
		
		// Stop the movement, no matter the cast output. Edit the heading in the same time.
		if (skill.getHitTime() > 50)
		{
			_actor.getMove().stop();
			
			if (target != _actor)
				_actor.getPosition().setHeadingTo(target);
		}
		
		// In case the cast is unsuccessful, we trigger MoveToPawn to handle the rotation.
		if (!_actor.getCast().canCast(target, skill, _currentIntention.isCtrlPressed(), _currentIntention.getItemObjectId()))
		{
			if (target != _actor)
				_actor.broadcastPacket(new MoveToPawn(_actor, target, (int) _actor.distance3D(target)));
			
			return;
		}
		
		_actor.getCast().doCast(skill, target, null);
	}
	
	@Override
	protected void thinkFakeDeath()
	{
		// Do nothing.
	}
	
	@Override
	protected void thinkFlee()
	{
		if (_actor.isMovementDisabled())
			return;
		
		final Creature target = _currentIntention.getFinalTarget();
		if (_actor == target)
			return;
		
		final int distance = _currentIntention.getItemObjectId();
		if (distance < 10)
			return;
		
		final double passedDistance = _actor.distance2D(_currentIntention.getLoc());
		if (passedDistance >= distance)
			return;
		
		_actor.fleeFrom(target, distance);
	}
	
	@Override
	protected void thinkFollow()
	{
		if (_actor.denyAiAction() || _actor.isMovementDisabled())
			return;
		
		final Creature target = _currentIntention.getFinalTarget();
		if (_actor == target)
			return;
		
		final boolean isShiftPressed = _currentIntention.isShiftPressed();
		if (isShiftPressed)
			return;
		
		_actor.getMove().maybeStartFriendlyFollow(target, 70);
	}
	
	@Override
	protected void thinkIdle()
	{
		_actor.getMove().stop();
	}
	
	@Override
	protected void thinkInteract()
	{
		// Do nothing.
	}
	
	@Override
	protected void thinkMoveRoute()
	{
		// Do nothing.
	}
	
	@Override
	protected void thinkMoveTo()
	{
		if (_actor.denyAiAction() || _actor.isMovementDisabled())
			return;
		
		_actor.getMove().maybeMoveToLocation(_currentIntention.getLoc(), 0, true, false);
	}
	
	@Override
	protected void thinkNothing()
	{
		// Do nothing.
	}
	
	@Override
	protected ItemInstance thinkPickUp()
	{
		return null;
	}
	
	@Override
	protected void thinkSit()
	{
		// Do nothing.
	}
	
	@Override
	protected void thinkSocial()
	{
		//l2-unity random social actions are calculated client side
//		if (_actor.denyAiAction())
//			return;
//
//		_actor.broadcastPacket(new SocialAction(_actor, _currentIntention.getItemObjectId()));
	}
	
	@Override
	protected void thinkStand()
	{
		// Do nothing.
	}
	
	@Override
	protected void thinkUseItem()
	{
		// Do nothing.
	}
	
	@Override
	protected void thinkWander()
	{
		// Do nothing.
	}
	
	@Override
	protected void onEvtSatDown(WorldObject target)
	{
		// Not all Creatures can SIT
	}
	
	@Override
	protected void onEvtStoodUp()
	{
		// Not all Creatures can STAND
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker)
	{
		startAttackStance();
	}
	
	@Override
	protected void onEvtAggression(Creature target, int aggro)
	{
		// Not all Creatures can ATTACK
	}
	
	@Override
	protected void onEvtEvaded(Creature attacker)
	{
		// Not all Creatures have a behaviour after having evaded a shot
	}
	
	@Override
	protected void onEvtOwnerAttacked(Creature attacker)
	{
		// Not all Creatures have a behaviour after their owner has been attacked
	}
	
	@Override
	protected void onEvtCancel()
	{
		// Not all Creatures can CANCEL
	}
	
	public boolean getFollowStatus()
	{
		return false;
	}
	
	public void setFollowStatus(boolean followStatus)
	{
		// Not all Creatures can FOLLOW
	}
	
	public boolean canDoInteract(WorldObject target)
	{
		return false;
	}
	
	public boolean canAttemptInteract()
	{
		return false;
	}
}