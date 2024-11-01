package com.shnok.javaserver.gameserver.model.actor;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.math.MathUtil;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.ai.type.AttackableAI;
import com.shnok.javaserver.gameserver.model.actor.attack.AttackableAttack;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.AggroList;
import com.shnok.javaserver.gameserver.model.actor.status.AttackableStatus;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.location.Point2D;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * This class manages all {@link Npc}s which can hold an {@link AggroList}.
 */
public class Attackable extends Npc
{
	private final Set<Creature> _attackedBy = ConcurrentHashMap.newKeySet();
	
	private boolean _isNoRndWalk;
	
	public Attackable(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public AttackableAI<? extends Attackable> getAI()
	{
		return (AttackableAI<?>) _ai;
	}
	
	@Override
	public void setAI()
	{
		_ai = new AttackableAI<>(this);
	}
	
	@Override
	public AttackableStatus getStatus()
	{
		return (AttackableStatus) _status;
	}
	
	@Override
	public void setStatus()
	{
		_status = new AttackableStatus(this);
	}
	
	@Override
	public void setAttack()
	{
		_attack = new AttackableAttack(this);
	}
	
	@Override
	public void removeKnownObject(WorldObject object)
	{
		super.removeKnownObject(object);
		
		// Delete the object from aggro list.
		if (object instanceof Creature creature)
			getAI().getAggroList().remove(creature);
	}
	
	@Override
	public void reduceCurrentHp(double damage, Creature attacker, L2Skill skill)
	{
		reduceCurrentHp(damage, attacker, true, false, skill);
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
			return false;
		
		_attackedBy.clear();
		
		return true;
	}
	
	@Override
	public void onSpawn()
	{
		// Clear the aggro/hate list.
		getAI().getAggroList().clear();
		getAI().getHateList().clear();
		
		super.onSpawn();
		
		// Stop the AI if region is inactive.
		if (!isInActiveRegion())
			getAI().stopAITask();
	}
	
	@Override
	public void onInteract(Player player)
	{
		// Attackables cannot be INTERACTed with
	}
	
	@Override
	public void onInactiveRegion()
	{
		// Clear data.
		getAttackByList().clear();
		
		// Stop all AI related tasks.
		super.onInactiveRegion();
	}
	
	@Override
	public boolean isLethalable()
	{
		switch (getNpcId())
		{
			case 22215: // Tyrannosaurus
			case 22216: // Tyrannosaurus
			case 22217: // Tyrannosaurus
			case 35062: // Headquarters
			case 35410: // Gustav
			case 35368: // Bloody Lord Nurka 1
			case 35375: // Bloody Lord Nurka 2
			case 35629: // Lidia von Hellmann
				return false;
		}
		return true;
	}
	
	/**
	 * Add a {@link Creature} attacker on _attackedBy {@link List}.
	 * @param attacker : The {@link Creature} to add.
	 */
	public void addAttacker(Creature attacker)
	{
		if (attacker == null || attacker == this)
			return;
		
		_attackedBy.add(attacker);
	}
	
	/**
	 * @return True if the {@link Attackable} successfully returned to spawn point. In case of minions, they are simply deleted.
	 */
	public boolean returnHome()
	{
		// Do nothing if already on territory.
		if (isInMyTerritory())
			return false;
		
		// Do nothing if hatelist is filled.
		if (!getAI().getHateList().isEmpty())
			return false;
		
		// Do nothing if no spawn location exists, or already in drift range of spawn location.
		if (getSpawnLocation() == null || isIn2DRadius(getSpawnLocation(), getDriftRange()))
			return false;
		
		getAI().getAggroList().cleanAllHate();
		
		forceWalkStance();
		
		// Move to the position.
		if (getMove().getGeoPathFailCount() >= 10)
		{
			teleportTo(getSpawnLocation(), 0);
			getMove().resetGeoPathFailCount();
		}
		else
		{
			getMove().maybeMoveToLocation(getSpawnLocation(), 0, true, false);
			ThreadPool.schedule(() ->
			{
				if (getAI().getCurrentIntention().getType() == IntentionType.WANDER)
				{
					Point2D backwardsPoint = MathUtil.getNewLocationByDistanceAndDegree(getPosition().getX(), getPosition().getY(), MathUtil.convertHeadingToDegree(getHeading()) - 180, Math.min((int) getTemplate().getCollisionRadius() * 2, 50));
					getMove().maybeMoveToLocation(new Location(backwardsPoint.getX(), backwardsPoint.getY(), getPosition().getZ()), 0, true, false);
				}
			}, (int) (Rnd.get(1500, 2500) * (100 / getStatus().getMoveSpeed())));
		}
		
		return true;
	}
	
	public int getDriftRange()
	{
		return Config.MAX_DRIFT_RANGE;
	}
	
	public final Set<Creature> getAttackByList()
	{
		return _attackedBy;
	}
	
	public final boolean isNoRndWalk()
	{
		return _isNoRndWalk;
	}
	
	public final void setNoRndWalk(boolean value)
	{
		_isNoRndWalk = value;
	}
	
	/**
	 * @return The {@link ItemInstance} used as weapon of this {@link Attackable} (null by default).
	 */
	public ItemInstance getActiveWeapon()
	{
		return null;
	}
	
	public boolean isGuard()
	{
		return false;
	}
}