package com.shnok.javaserver.gameserver.skills.conditions;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class ConditionPlayerHasClanHall extends Condition
{
	private final List<Integer> _clanHall;
	
	public ConditionPlayerHasClanHall(List<Integer> clanHall)
	{
		_clanHall = clanHall;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		if (!(effector instanceof Player player))
			return false;
		
		final Clan clan = player.getClan();
		if (clan == null)
			return (_clanHall.size() == 1 && _clanHall.get(0) == 0);
		
		// All Clan Hall
		if (_clanHall.size() == 1 && _clanHall.get(0) == -1)
			return clan.hasClanHall();
		
		return _clanHall.contains(clan.getClanHallId());
	}
}