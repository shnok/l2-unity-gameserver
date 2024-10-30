package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.SealType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class DawnPriest extends SignsPriest
{
	public DawnPriest(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("Chat"))
			showChatWindow(player);
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		String filename = SevenSignsManager.SEVEN_SIGNS_HTML_PATH;
		
		final CabalType winningCabal = SevenSignsManager.getInstance().getWinningCabal();
		
		switch (SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId()))
		{
			case DAWN:
				if (SevenSignsManager.getInstance().isCompResultsPeriod())
					filename += "dawn_priest_5.htm";
				else if (SevenSignsManager.getInstance().isRecruitingPeriod())
					filename += "dawn_priest_6.htm";
				else if (SevenSignsManager.getInstance().isSealValidationPeriod())
				{
					if (winningCabal == CabalType.DAWN)
					{
						if (winningCabal != SevenSignsManager.getInstance().getSealOwner(SealType.GNOSIS))
							filename += "dawn_priest_2c.htm";
						else
							filename += "dawn_priest_2a.htm";
					}
					else if (winningCabal == CabalType.NORMAL)
						filename += "dawn_priest_2d.htm";
					else
						filename += "dawn_priest_2b.htm";
				}
				else
					filename += "dawn_priest_1.htm";
				break;
			
			case DUSK:
				filename += "dawn_priest_3.htm";
				break;
			
			default:
				if (SevenSignsManager.getInstance().isCompResultsPeriod())
					filename += "dawn_priest_5.htm";
				else if (SevenSignsManager.getInstance().isRecruitingPeriod())
					filename += "dawn_priest_6.htm";
				else if (SevenSignsManager.getInstance().isSealValidationPeriod())
				{
					if (winningCabal == CabalType.DAWN)
						filename += "dawn_priest_4.htm";
					else if (winningCabal == CabalType.NORMAL)
						filename += "dawn_priest_2d.htm";
					else
						filename += "dawn_priest_2b.htm";
				}
				else
					filename += "dawn_priest_1.htm";
				break;
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile(filename);
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
}