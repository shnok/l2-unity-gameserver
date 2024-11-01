package com.shnok.javaserver.gameserver.scripting.script.teleport;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class CharmTeleporter extends Quest
{
	private static final int WHIRPY = 30540;
	private static final int TAMIL = 30576;
	
	private static final int ORC_GATEKEEPER_CHARM = 1658;
	private static final int DWARF_GATEKEEPER_TOKEN = 1659;
	
	public CharmTeleporter()
	{
		super(-1, "teleport");
		
		addTalkId(WHIRPY, TAMIL);
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		String htmltext = "";
		
		int npcId = npc.getNpcId();
		if (npcId == WHIRPY)
		{
			if (player.getInventory().hasItem(DWARF_GATEKEEPER_TOKEN))
			{
				takeItems(player, DWARF_GATEKEEPER_TOKEN, 1);
				player.teleportTo(-80826, 149775, -3043, 0);
			}
			else
				htmltext = "30540-01.htm";
		}
		else if (npcId == TAMIL)
		{
			if (player.getInventory().hasItem(ORC_GATEKEEPER_CHARM))
			{
				takeItems(player, ORC_GATEKEEPER_CHARM, 1);
				player.teleportTo(-80826, 149775, -3043, 0);
			}
			else
				htmltext = "30576-01.htm";
		}
		
		return htmltext;
	}
}