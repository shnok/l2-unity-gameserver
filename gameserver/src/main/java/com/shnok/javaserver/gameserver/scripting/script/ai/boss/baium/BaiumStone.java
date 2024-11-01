package com.shnok.javaserver.gameserver.scripting.script.ai.boss.baium;

import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.scripting.Quest;

public class BaiumStone extends Quest
{
	private static final int GM_ID = 2;
	private static final int BAIUM_STONE = 29025;
	
	public BaiumStone()
	{
		super(-1, "ai/boss/baium");
		
		addCreated(BAIUM_STONE);
		addTalkId(BAIUM_STONE);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai0 = 0;
	}
	
	@Override
	public String onTalk(Npc npc, Player player)
	{
		if (npc._i_ai0 == 0)
		{
			npc._i_ai0 = 1;
			
			final int i0 = GlobalMemo.getInstance().getInteger(String.valueOf(GM_ID));
			if (i0 != -1)
			{
				final Npc c0 = (Npc) World.getInstance().getObject(i0);
				if (c0 != null)
					c0.sendScriptEvent(10025, player.getObjectId(), 0);
			}
			npc.deleteMe();
		}
		return null;
	}
}