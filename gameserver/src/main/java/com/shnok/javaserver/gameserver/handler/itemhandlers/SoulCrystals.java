package com.shnok.javaserver.gameserver.handler.itemhandlers;

import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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