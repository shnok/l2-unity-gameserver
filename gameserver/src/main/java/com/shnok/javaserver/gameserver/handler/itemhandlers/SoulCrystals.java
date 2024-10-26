package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class SoulCrystals implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		final IntIntHolder[] skills = item.getEtcItem().getSkills();
		if (skills == null)
			return;
		
		final L2Skill skill = skills[0].getSkill();
		if (skill == null || skill.getId() != 2096)
			return;
		
		final Creature target = (player.getTarget() instanceof Creature targetCreature) ? targetCreature : null;
		if (target == null)
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		player.getAI().tryToCast(target, skill, forceUse, false, 0);
	}
}