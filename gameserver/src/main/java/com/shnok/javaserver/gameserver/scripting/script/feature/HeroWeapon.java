package com.shnok.javaserver.gameserver.scripting.script.feature;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class HeroWeapon extends Quest
{
	private static final int[] WEAPON_IDS =
	{
		6611,
		6612,
		6613,
		6614,
		6615,
		6616,
		6617,
		6618,
		6619,
		6620,
		6621
	};
	
	public HeroWeapon()
	{
		super(-1, "feature");
		
		addTalkId(31690, 31769, 31770, 31771, 31772, 31773);
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		int weaponId = Integer.parseInt(event);
		if (ArraysUtil.contains(WEAPON_IDS, weaponId))
			giveItems(player, weaponId, 1);
		
		return null;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		if (!player.isHero())
			return "no_hero.htm";
		
		if (player.getInventory().hasAtLeastOneItem(WEAPON_IDS))
			return "already_have_weapon.htm";
		
		return "weapon_list.htm";
	}
}