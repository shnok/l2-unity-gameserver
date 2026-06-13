package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.gameserver.data.xml.ScriptData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestState;

public final class RequestQuestAbort extends L2GameClientPacket
{
	private int _questId;
	
	@Override
	protected void readImpl()
	{
		_questId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final Quest quest = ScriptData.getInstance().getQuest(_questId);
		if (quest == null)
			return;
		
		final QuestState qs = player.getQuestList().getQuestState(_questId);
		if (qs != null)
			qs.exitQuest(true);
	}
}