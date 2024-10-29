package com.shnok.javaserver.gameserver.model.actor.ai.type;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.CursedWeaponManager;
import com.shnok.javaserver.gameserver.enums.AiEventType;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.LootRule;
import com.shnok.javaserver.gameserver.enums.items.ArmorType;
import com.shnok.javaserver.gameserver.enums.items.EtcItemType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.enums.skills.SkillTargetType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.handler.ItemHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Boat;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.container.player.BoatInfo;
import com.shnok.javaserver.gameserver.model.actor.instance.StaticObject;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.AutoAttackStart;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.MoveToLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.ValidateLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ChairSit;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.MoveToPawn;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.StopMove;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.AttackStanceTaskManager;
import com.shnok.javaserver.gameserver.taskmanager.ItemsOnGroundTaskManager;

public class PlayerAI extends PlayableAI<Player>
{
	public PlayerAI(Player player)
	{
		super(player);
	}
	
	@Override
	protected void onEvtArrived()
	{
		final BoatInfo info = _actor.getBoatInfo();
		
		info.setBoatMovement(false);
		
		if (_currentIntention.getType() == IntentionType.MOVE_TO)
		{
			final Boat boat = _currentIntention.getBoat();
			if (boat != null)
			{
				info.setCanBoard(true);
				
				// Warn the player about summon not allowed on board.
				if (_actor.getSummon() != null)
					_actor.sendPacket(SystemMessageId.RELEASE_PET_ON_BOAT);
			}
		}
		
		super.onEvtArrived();
	}
	
	@Override
	protected void onEvtArrivedBlocked()
	{
		final BoatInfo info = _actor.getBoatInfo();
		
		info.setBoatMovement(false);
		
		if (_currentIntention.getType() == IntentionType.INTERACT)
		{
			clientActionFailed();
			
			final WorldObject target = _currentIntention.getTarget();
			if (_actor.getAI().canDoInteract(target))
			{
				_actor.broadcastPacket(new StopMove(_actor));
				
				target.onInteract(_actor);
			}
			else
				super.onEvtArrivedBlocked();
			
			doIdleIntention();
		}
		else if (_currentIntention.getType() == IntentionType.CAST)
		{
			_actor.sendPacket(SystemMessageId.DIST_TOO_FAR_CASTING_STOPPED);
			super.onEvtArrivedBlocked();
		}
		else {
			System.out.println("SHOULD HANDLE PLAYER BLOCKED!");
			_actor.broadcastPacket(new ValidateLocation(_actor));
		}
	}
	
	@Override
	protected void onEvtSatDown(WorldObject target)
	{
		if (_nextIntention.isBlank())
			doIdleIntention();
		else
			doIntention(_nextIntention);
	}
	
	@Override
	protected void onEvtStoodUp()
	{
		if (_actor.getThroneId() != 0)
		{
			final WorldObject object = World.getInstance().getObject(_actor.getThroneId());
			if (object instanceof StaticObject staticObject)
				staticObject.setBusy(false);
			
			_actor.setThroneId(0);
		}
		
		if (_nextIntention.isBlank())
			doIdleIntention();
		else
			doIntention(_nextIntention);
	}
	
	@Override
	protected void onEvtBowAttackReuse()
	{
		if (_actor.getAttackType() == WeaponType.BOW)
		{
			// Attacks can be scheduled while isAttackingNow
			if (_nextIntention.getType() == IntentionType.ATTACK)
			{
				doIntention(_nextIntention);
				return;
			}
			
			if (_currentIntention.getType() == IntentionType.ATTACK)
			{
				if (_actor.canKeepAttacking(_currentIntention.getFinalTarget()))
					notifyEvent(AiEventType.THINK, null, null);
				else
					doIdleIntention();
			}
		}
	}
	
	@Override
	protected void onEvtAttacked(Creature attacker)
	{
		if (_actor.getTamedBeast() != null)
			_actor.getTamedBeast().getAI().notifyEvent(AiEventType.OWNER_ATTACKED, attacker, null);
		
		if (_actor.isSitting())
			doStandIntention();
		
		super.onEvtAttacked(attacker);
	}
	
	@Override
	protected void onEvtCancel()
	{
		_actor.getCast().stop();
		_actor.getMove().cancelFollowTask();
		
		doIdleIntention();
	}
	
	@Override
	protected void thinkAttack()
	{
		if (_actor.denyAiAction() || _actor.isSitting())
		{
			doIdleIntention();
			clientActionFailed();
			return;
		}
		
		final Creature target = _currentIntention.getFinalTarget();
		if (isTargetLost(target))
		{
			doIdleIntention();
			clientActionFailed();
			return;
		}
		
		boolean isShiftPressed = _currentIntention.isShiftPressed();
		if (_actor.getMove().maybeMoveToPawn(target, _actor.getStatus().getPhysicalAttackRange(), isShiftPressed))
		{
			if (isShiftPressed)
			{
				doIdleIntention();
				clientActionFailed();
			}
			
			return;
		}
		
		_actor.getMove().stop();
		
		if ((_actor.getAttackType() == WeaponType.BOW && _actor.getAttack().isBowCoolingDown()) || _actor.getAttack().isAttackingNow() || _actor.getCast().isCastingNow())
		{
			setNextIntention(_currentIntention);
			clientActionFailed();
			return;
		}
		
		if (!_actor.getAttack().canAttack(target))
		{
			doIdleIntention();
			clientActionFailed();
			return;
		}
		
		_actor.getAttack().doAttack(target);
	}
	
	@Override
	protected void thinkCast()
	{
		if (_actor.denyAiAction() || _actor.getAllSkillsDisabled() || _actor.getCast().isCastingNow())
		{
			doIdleIntention();
			clientActionFailed();
			return;
		}
		
		final Creature target = _currentIntention.getFinalTarget();
		if (target == null)
		{
			doIdleIntention();
			return;
		}
		
		final L2Skill skill = _currentIntention.getSkill();
		if (isTargetLost(target, skill))
		{
			doIdleIntention();
			return;
		}
		
		if (!_actor.getCast().canAttemptCast(target, skill))
			return;
		
		final boolean isShiftPressed = _currentIntention.isShiftPressed();
		if (skill.getTargetType() == SkillTargetType.GROUND)
		{
			if (_actor.getMove().maybeMoveToLocation(_actor.getCast().getSignetLocation(), skill.getCastRange(), false, isShiftPressed))
			{
				if (isShiftPressed)
				{
					_actor.sendPacket(SystemMessageId.TARGET_TOO_FAR);
					doIdleIntention();
				}
				
				return;
			}
		}
		else
		{
			if (_actor.getMove().maybeMoveToPawn(target, skill.getCastRange(), isShiftPressed))
			{
				if (isShiftPressed)
				{
					_actor.sendPacket(SystemMessageId.TARGET_TOO_FAR);
					doIdleIntention();
				}
				
				return;
			}
		}
		
		if (skill.isToggle())
		{
			_actor.getMove().stop();
			_actor.getCast().doToggleCast(skill, target);
		}
		else
		{
			final boolean isCtrlPressed = _currentIntention.isCtrlPressed();
			final int itemObjectId = _currentIntention.getItemObjectId();
			
			// Stop the movement, no matter the cast output.
			if (skill.getHitTime() > 50)
				_actor.getMove().stop();
			
			// In case the cast is unsuccessful, we trigger MoveToPawn to handle the rotation.
			if (!_actor.getCast().canCast(target, skill, isCtrlPressed, itemObjectId))
			{
				if (skill.nextActionIsAttack() && target.isAttackableWithoutForceBy(_actor))
					doAttackIntention(target, isCtrlPressed, isShiftPressed, true);
				
				return;
			}
			
			// Edit the heading.
			if (skill.getHitTime() > 50 && target != _actor)
				_actor.getPosition().setHeadingTo(target);
			
			if (skill.getSkillType() == SkillType.FUSION || skill.getSkillType() == SkillType.SIGNET_CASTTIME)
				_actor.getCast().doFusionCast(skill, target);
			else
				_actor.getCast().doCast(skill, target, _actor.getInventory().getItemByObjectId(itemObjectId));
		}
	}
	
	@Override
	protected void thinkFakeDeath()
	{
		if (_actor.denyAiAction() || _actor.isMounted())
		{
			clientActionFailed();
			return;
		}
		
		// Start fake death hidden in isCtrlPressed.
		if (_currentIntention.isCtrlPressed())
		{
			_actor.getMove().stop();
			_actor.startFakeDeath();
		}
		else
			_actor.stopFakeDeath(false);
	}
	
	@Override
	protected ItemInstance thinkPickUp()
	{
		final ItemInstance item = super.thinkPickUp();
		if (item == null)
			return null;
		
		synchronized (item)
		{
			if (!item.isVisible())
				return null;
			
			if (((_actor.isInParty() && _actor.getParty().getLootRule() == LootRule.ITEM_LOOTER) || !_actor.isInParty()) && !_actor.getInventory().validateCapacity(item))
			{
				_actor.sendPacket(SystemMessageId.SLOTS_FULL);
				return null;
			}
			
			if (_actor.getActiveTradeList() != null)
			{
				_actor.sendPacket(SystemMessageId.CANNOT_PICKUP_OR_USE_ITEM_WHILE_TRADING);
				return null;
			}
			
			if (item.getOwnerId() != 0 && !_actor.isLooterOrInLooterParty(item.getOwnerId()))
			{
				if (item.getItemId() == 57)
					_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1_ADENA).addNumber(item.getCount()));
				else if (item.getCount() > 1)
					_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S2_S1_S).addItemName(item).addNumber(item.getCount()));
				else
					_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.FAILED_TO_PICKUP_S1).addItemName(item));
				
				return null;
			}
			
			if (item.hasDropProtection())
				item.removeDropProtection();
			
			item.pickupMe(_actor);
			
			ItemsOnGroundTaskManager.getInstance().remove(item);
		}
		
		if (item.getItemType() == EtcItemType.HERB)
		{
			final IItemHandler handler = ItemHandler.getInstance().getHandler(item.getEtcItem());
			if (handler != null)
				handler.useItem(_actor, item, false);
			
			item.destroyMe();
		}
		else if (CursedWeaponManager.getInstance().isCursed(item.getItemId()))
		{
			_actor.addItem(item, true);
		}
		else
		{
			if (item.getItemType() instanceof ArmorType || item.getItemType() instanceof WeaponType)
			{
				SystemMessage sm;
				if (item.getEnchantLevel() > 0)
					sm = SystemMessage.getSystemMessage(SystemMessageId.ATTENTION_S1_PICKED_UP_S2_S3).addString(_actor.getName()).addNumber(item.getEnchantLevel()).addItemName(item.getItemId());
				else
					sm = SystemMessage.getSystemMessage(SystemMessageId.ATTENTION_S1_PICKED_UP_S2).addString(_actor.getName()).addItemName(item.getItemId());
				
				_actor.broadcastPacketInRadius(sm, 1400);
			}
			
			if (_actor.isInParty())
				_actor.getParty().distributeItem(_actor, item, null);
			else if (item.getItemId() == 57 && _actor.getInventory().getAdenaInstance() != null)
			{
				_actor.addAdena(item.getCount(), true);
				item.destroyMe();
			}
			else
				_actor.addItem(item, true);
		}
		
		ThreadPool.schedule(() -> _actor.setIsParalyzed(false), 200);
		_actor.setIsParalyzed(true);
		
		return item;
	}
	
	@Override
	protected void thinkInteract()
	{
		clientActionFailed();
		
		if (_actor.denyAiAction() || _actor.isSitting() || _actor.isFlying())
		{
			doIdleIntention();
			return;
		}
		
		final WorldObject target = _currentIntention.getTarget();
		if (isTargetLost(target))
		{
			doIdleIntention();
			return;
		}
		
		if (!canAttemptInteract())
		{
			doIdleIntention();
			return;
		}
		
		final boolean isShiftPressed = _currentIntention.isShiftPressed();
		if (_actor.getMove().maybeMoveToPawn(target, 100, isShiftPressed))
		{
			if (isShiftPressed)
				doIdleIntention();
			
			return;
		}
		
		if (!canDoInteract(target))
		{
			doIdleIntention();
			return;
		}
		
		if (target instanceof Npc targetNpc && targetNpc.isMoving())
			_actor.broadcastPacket(new StopMove(_actor));
		else
		{
			_actor.getPosition().setHeadingTo(target);
			_actor.broadcastPacket(new MoveToPawn(_actor, target, Npc.INTERACTION_DISTANCE));
		}
		
		target.onInteract(_actor);
		
		doIdleIntention();
	}
	
	@Override
	protected void thinkSit()
	{
		if (_actor.denyAiAction() || _actor.isSitting() || _actor.isOperating() || _actor.isMounted())
		{
			doIdleIntention();
			clientActionFailed();
			return;
		}
		
		_actor.getMove().stop();
		
		// sitDown sends the ChangeWaitType packet, which MUST precede the ChairSit packet (sent in this function) in order to properly sit on the throne.
		_actor.sitDown();
		
		final WorldObject target = _currentIntention.getTarget();
		if (target instanceof StaticObject targetStaticObject && targetStaticObject.getType() == 1 && !targetStaticObject.isBusy() && _actor.isIn3DRadius(targetStaticObject, Npc.INTERACTION_DISTANCE))
		{
			_actor.setThroneId(targetStaticObject.getObjectId());
			
			targetStaticObject.setBusy(true);
			_actor.broadcastPacket(new ChairSit(_actor.getObjectId(), targetStaticObject.getStaticObjectId()));
		}
	}
	
	@Override
	protected void thinkStand()
	{
		// no need to _actor.isOperating() here, because it is included in the Player overriden denyAiAction
		if (_actor.denyAiAction() || !_actor.isSitting() || _actor.isMounted())
		{
			doIdleIntention();
			clientActionFailed();
			return;
		}
		
		if (_actor.isFakeDeath())
			_actor.stopFakeDeath(true);
		else
			_actor.standUp();
	}
	
	@Override
	protected void thinkUseItem()
	{
		final ItemInstance itemToTest = _actor.getInventory().getItemByObjectId(_currentIntention.getItemObjectId());
		if (itemToTest == null)
			return;
		
		// Equip or unequip the related ItemInstance.
		_actor.useEquippableItem(itemToTest, false);
		
		// Resolve previous intention.
		if (_previousIntention.getType() != IntentionType.CAST && _previousIntention.getType() != IntentionType.USE_ITEM)
			doIntention(_previousIntention);
	}
	
	@Override
	public boolean canAttemptInteract()
	{
		if (_actor.isOperating() || _actor.isProcessingTransaction())
			return false;
		
		return true;
	}
	
	@Override
	public boolean canDoInteract(WorldObject target)
	{
		// Can't interact in shop mode, or during a transaction or a request.
		if (_actor.isOperating() || _actor.isProcessingTransaction())
			return false;
		
		// Can't interact if regular distance doesn't match.
		return target.isIn3DRadius(_actor, Npc.INTERACTION_DISTANCE);
	}
	
	@Override
	public void startAttackStance()
	{
		if (!AttackStanceTaskManager.getInstance().isInAttackStance(_actor))
		{
			final Summon summon = _actor.getSummon();
			if (summon != null)
				summon.broadcastPacket(new AutoAttackStart(summon.getObjectId()));
			
			_actor.broadcastPacket(new AutoAttackStart(_actor.getObjectId()));
		}
		
		AttackStanceTaskManager.getInstance().add(_actor);
	}
	
	@Override
	public void clientActionFailed()
	{
		_actor.sendPacket(ActionFailed.STATIC_PACKET);
	}
}