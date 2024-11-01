package com.shnok.javaserver.gameserver.skills.conditions;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public final class ConditionPlayerHasCastle extends Condition
{
	private final int _castle;
	
	public ConditionPlayerHasCastle(int castle)
	{
		_castle = castle;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		if (!(effector instanceof Player player))
			return false;
		
		final Clan clan = player.getClan();
		if (clan == null)
			return _castle == 0;
		
		// Any castle
		if (_castle == -1)
			return clan.hasCastle();
		
		return clan.getCastleId() == _castle;
	}
}