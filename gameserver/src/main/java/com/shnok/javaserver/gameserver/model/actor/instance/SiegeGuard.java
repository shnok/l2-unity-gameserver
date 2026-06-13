package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;

/**
 * This class represents all Castle guards.
 */
public final class SiegeGuard extends Attackable
{
	public SiegeGuard(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isAttackableBy(Creature attacker)
	{
		if (!super.isAttackableBy(attacker))
			return false;
		
		final Player player = attacker.getActingPlayer();
		if (player == null)
			return false;
		
		if (getCastle() != null && getCastle().getSiege().isInProgress())
			return getCastle().getSiege().checkSides(player.getClan(), SiegeSide.ATTACKER);
		
		if (getSiegableHall() != null && getSiegableHall().isInSiege())
			return getSiegableHall().getSiege().checkSides(player.getClan(), SiegeSide.ATTACKER);
		
		return false;
	}
	
	@Override
	public boolean isAttackableWithoutForceBy(Playable attacker)
	{
		return isAttackableBy(attacker);
	}
	
	@Override
	public boolean returnHome()
	{
		// Do nothing if no spawn location exists, or already in drift range of spawn location.
		if (getSpawnLocation() == null || isIn2DRadius(getSpawnLocation(), getDriftRange()))
			return false;
		
		getAI().getAggroList().cleanAllHate();
		
		forceRunStance();
		
		// Move to the position.
		if (getMove().getGeoPathFailCount() >= 10)
		{
			teleportTo(getSpawnLocation(), 0);
			getMove().resetGeoPathFailCount();
		}
		else
			getAI().addMoveToDesire(getSpawnLocation(), 1000000);
		
		return true;
	}
	
	@Override
	public boolean isGuard()
	{
		return true;
	}
	
	@Override
	public int getDriftRange()
	{
		return 20;
	}
	
	@Override
	public boolean canAutoAttack(Creature target)
	{
		final Player player = target.getActingPlayer();
		if (player == null || player.isAlikeDead())
			return false;
		
		// Check if the target is invisible.
		if (!player.getAppearance().isVisible())
			return false;
		
		// Check if the target isn't in silent move mode AND too far
		if (player.isSilentMoving() && !isIn3DRadius(player, 250))
			return false;
		
		return target.isAttackableBy(this) && GeoEngine.getInstance().canSeeTarget(this, target);
	}
}