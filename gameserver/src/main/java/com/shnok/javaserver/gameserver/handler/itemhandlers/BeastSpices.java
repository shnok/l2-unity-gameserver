package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.FeedableBeast;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class BeastSpices implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!(playable instanceof Player player))
			return;
		
		final Creature target = (playable.getTarget() instanceof Creature targetCreature) ? targetCreature : null;
		
		if (!(target instanceof FeedableBeast))
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		final L2Skill skill = item.getEtcItem().getSkills()[0].getSkill();
		if (skill != null)
			player.getAI().tryToCast(target, skill);
	}
}