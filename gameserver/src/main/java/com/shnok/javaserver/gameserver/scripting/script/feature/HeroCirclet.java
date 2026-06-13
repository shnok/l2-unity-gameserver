package com.shnok.javaserver.gameserver.scripting.script.feature;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class HeroCirclet extends Quest
{
	private static final int CIRCLET = 6842;
	
	public HeroCirclet()
	{
		super(-1, "feature");
		
		addTalkId(31690, 31769, 31770, 31771, 31772);
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		if (!player.isHero())
			return "no_hero.htm";
		
		if (player.getInventory().hasItem(CIRCLET))
			return "already_have_circlet.htm";
		
		giveItems(player, 6842, 1);
		return null;
	}
}