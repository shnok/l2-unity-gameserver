package com.shnok.javaserver.gameserver.skills.funcs;

import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;

/**
 * @see Func
 */
public class FuncPDefMod extends Func
{
	private static final FuncPDefMod INSTANCE = new FuncPDefMod();
	
	private FuncPDefMod()
	{
		super(null, Stats.POWER_DEFENCE, 10, 0, null);
	}
	
	@Override
	public double calc(Creature effector, Creature effected, L2Skill skill, double base, double value)
	{
		if (effector instanceof Player player)
		{
			if (player.getInventory().hasItemIn(Paperdoll.HEAD))
				value -= 12;
			
			final ItemInstance chestItem = player.getInventory().getItemFrom(Paperdoll.CHEST);
			if (chestItem != null)
				value -= (player.isMageClass()) ? 15 : 31;
			
			final boolean isFullBody = chestItem != null && chestItem.getItem().getBodyPart() == Item.SLOT_FULL_ARMOR;
			if (isFullBody || player.getInventory().hasItemIn(Paperdoll.LEGS))
				value -= (player.isMageClass()) ? 8 : 18;
			
			if (player.getInventory().hasItemIn(Paperdoll.GLOVES))
				value -= 8;
			
			if (player.getInventory().hasItemIn(Paperdoll.FEET))
				value -= 7;
		}
		return value * effector.getStatus().getLevelMod();
	}
	
	public static Func getInstance()
	{
		return INSTANCE;
	}
}