package com.shnok.javaserver.gameserver.scripting.script.teleport;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class ElrokiTeleporter extends Quest
{
	public ElrokiTeleporter()
	{
		super(-1, "teleport");
		
		addTalkId(32111, 32112);
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		if (npc.getNpcId() == 32111)
		{
			if (player.isInCombat())
				return "32111-no.htm";
			
			player.teleportTo(4990, -1879, -3178, 0);
		}
		else
			player.teleportTo(7557, -5513, -3221, 0);
		
		return null;
	}
}