package com.shnok.javaserver.gameserver.handler.itemhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.CastleManorManager;
import net.sf.l2j.gameserver.data.xml.ManorAreaData;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.manor.ManorArea;
import net.sf.l2j.gameserver.model.manor.Seed;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class Seeds implements IItemHandler
{
	@Override
	public void useItem(Playable playable, ItemInstance item, boolean forceUse)
	{
		if (!Config.ALLOW_MANOR || !(playable instanceof Player))
			return;
		
		final WorldObject target = playable.getTarget();
		if (!(target instanceof Monster targetMonster))
		{
			playable.sendPacket(SystemMessageId.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING);
			return;
		}
		
		final ManorArea area = ManorAreaData.getInstance().getManorArea(targetMonster);
		if (!targetMonster.getTemplate().isSeedable() || area == null)
		{
			playable.sendPacket(SystemMessageId.THE_TARGET_IS_UNAVAILABLE_FOR_SEEDING);
			return;
		}
		
		final Seed seed = CastleManorManager.getInstance().getSeed(item.getItemId());
		if (seed == null)
			return;
		
		if (area.getCastleId() != seed.getCastleId())
		{
			playable.sendPacket(SystemMessageId.THIS_SEED_MAY_NOT_BE_SOWN_HERE);
			return;
		}
		
		if (targetMonster.isDead())
		{
			playable.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		if (targetMonster.getSeedState().isSeeded())
		{
			playable.sendPacket(SystemMessageId.THE_SEED_HAS_BEEN_SOWN);
			return;
		}
		
		final IntIntHolder[] skills = item.getEtcItem().getSkills();
		if (skills == null || skills[0] == null)
			return;
		
		playable.getAI().tryToCast(targetMonster, skills[0].getSkill(), false, false, item.getObjectId());
	}
}