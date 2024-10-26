package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.QuestState;

public class GMViewQuestList extends L2GameServerPacket
{
	private final List<QuestState> _questStates;
	private final Player _player;
	
	public GMViewQuestList(Player player)
	{
		_questStates = player.getQuestList().getAllQuests(true);
		_player = player;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x93);
		
		writeS(_player.getName());
		writeH(_questStates.size());
		
		for (QuestState qs : _questStates)
		{
			writeD(qs.getQuest().getQuestId());
			writeD(qs.getFlags());
		}
	}
}