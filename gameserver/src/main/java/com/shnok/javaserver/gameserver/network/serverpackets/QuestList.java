package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.QuestState;

public class QuestList extends L2GameServerPacket
{
	private final List<QuestState> _questStates;
	
	public QuestList(Player player)
	{
		_questStates = player.getQuestList().getAllQuests(true);
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x80);
		
		writeH(_questStates.size());
		
		for (QuestState qs : _questStates)
		{
			writeD(qs.getQuest().getQuestId());
			writeD(qs.getFlags());
		}
	}
}