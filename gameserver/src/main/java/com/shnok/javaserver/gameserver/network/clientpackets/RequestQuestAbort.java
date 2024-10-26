package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.gameserver.data.xml.ScriptData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.scripting.QuestState;

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