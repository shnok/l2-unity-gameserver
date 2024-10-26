package com.shnok.javaserver.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ExShowQuestInfo;

public class Adventurer extends Folk
{
	public Adventurer(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("raidInfo"))
		{
			int bossLevel = Integer.parseInt(command.substring(9).trim());
			String filename = "data/html/adventurer_guildsman/raid_info/info.htm";
			if (bossLevel != 0)
				filename = "data/html/adventurer_guildsman/raid_info/level" + bossLevel + ".htm";
			
			showChatWindow(player, filename);
		}
		else if (command.equalsIgnoreCase("questlist"))
			player.sendPacket(ExShowQuestInfo.STATIC_PACKET);
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/adventurer_guildsman/" + filename + ".htm";
	}
}