package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.olympiad.OlympiadGameManager;
import net.sf.l2j.gameserver.model.olympiad.OlympiadGameTask;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public final class RequestOlympiadMatchList extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null || !player.isInObserverMode())
			return;
		
		int i = 0;
		
		final StringBuilder sb = new StringBuilder(1500);
		for (OlympiadGameTask task : OlympiadGameManager.getInstance().getOlympiadTasks())
		{
			StringUtil.append(sb, "<tr><td fixwidth=10><a action=\"bypass arenachange ", i, "\">", ++i, "</a></td><td fixwidth=80>");
			
			if (task.isGameStarted())
			{
				if (task.isInTimerTime())
					StringUtil.append(sb, "&$907;"); // Counting In Progress
				else if (task.isBattleStarted())
					StringUtil.append(sb, "&$829;"); // In Progress
				else
					StringUtil.append(sb, "&$908;"); // Terminate
					
				StringUtil.append(sb, "</td><td>", task.getGame().getPlayerNames()[0], "&nbsp; / &nbsp;", task.getGame().getPlayerNames()[1]);
			}
			else
				StringUtil.append(sb, "&$906;", "</td><td>&nbsp;"); // Initial State
				
			StringUtil.append(sb, "</td><td><font color=\"aaccff\"></font></td></tr>");
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "olympiad_arena_observe_list.htm");
		html.replace("%list%", sb.toString());
		player.sendPacket(html);
	}
}