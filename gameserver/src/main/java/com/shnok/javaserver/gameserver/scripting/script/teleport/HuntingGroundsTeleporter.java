package com.shnok.javaserver.gameserver.scripting.script.teleport;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.TeleportType;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class HuntingGroundsTeleporter extends Quest
{
	private static final int[] PRIESTS =
	{
		31078,
		31079,
		31080,
		31081,
		31082,
		31083,
		31084,
		31085,
		31086,
		31087,
		31088,
		31089,
		31090,
		31091,
		31168,
		31169,
		31692,
		31693,
		31694,
		31695,
		31997,
		31998
	};
	
	private static final int[] DAWN_NPCS =
	{
		31078,
		31079,
		31080,
		31081,
		31082,
		31083,
		31084,
		31168,
		31692,
		31694,
		31997
	};
	
	public HuntingGroundsTeleporter()
	{
		super(-1, "teleport");
		
		addTalkId(PRIESTS);
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		final CabalType playerCabal = SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId());
		if (playerCabal == CabalType.NORMAL)
			return ArraysUtil.contains(DAWN_NPCS, npc.getNpcId()) ? "dawn_tele-no.htm" : "dusk_tele-no.htm";
		
		npc.showTeleportWindow(player, TeleportType.STANDARD);
		return null;
	}
}