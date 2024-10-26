package com.shnok.javaserver.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.data.manager.FestivalOfDarknessManager;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 * This class manages all attackable festival NPCs, spawned during the Festival of Darkness.
 */
public class FestivalMonster extends Monster
{
	protected int _bonusMultiplier = 1;
	
	public FestivalMonster(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isAggressive()
	{
		return true;
	}
	
	@Override
	public void doItemDrop(Creature attacker)
	{
		final Player player = attacker.getActingPlayer();
		if (player == null || !player.isInParty())
			return;
		
		player.getParty().getLeader().addItem(FestivalOfDarknessManager.FESTIVAL_OFFERING_ID, _bonusMultiplier, true);
		
		super.doItemDrop(attacker);
	}
	
	public void setOfferingBonus(int bonusMultiplier)
	{
		_bonusMultiplier = bonusMultiplier;
	}
}