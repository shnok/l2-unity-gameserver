package com.shnok.javaserver.gameserver.scripting.task;

import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.pledge.ClanMember;
import net.sf.l2j.gameserver.scripting.ScheduledQuest;

public class ClanLeaderTransfer extends ScheduledQuest
{
	public ClanLeaderTransfer()
	{
		super(-1, "task");
	}
	
	@Override
	public final void onStart()
	{
		for (Clan clan : ClanTable.getInstance().getClans())
		{
			if (clan.getNewLeaderId() <= 0)
				continue;
			
			final ClanMember member = clan.getClanMember(clan.getNewLeaderId());
			if (member == null)
			{
				clan.setNewLeaderId(0, true);
				continue;
			}
			
			clan.setNewLeader(member);
		}
	}
	
	@Override
	public final void onEnd()
	{
		// Do nothing.
	}
}