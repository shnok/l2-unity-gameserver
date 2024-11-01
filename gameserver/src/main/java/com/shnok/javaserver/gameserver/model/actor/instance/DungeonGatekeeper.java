package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.data.manager.SevenSignsManager;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.SealType;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;

public class DungeonGatekeeper extends Folk
{
	public DungeonGatekeeper(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		final CabalType playerCabal = SevenSignsManager.getInstance().getPlayerCabal(player.getObjectId());
		
		if (command.startsWith("necro"))
		{
			boolean canPort = true;
			if (SevenSignsManager.getInstance().isSealValidationPeriod())
			{
				final CabalType winningCabal = SevenSignsManager.getInstance().getWinningCabal();
				final CabalType sealAvariceOwner = SevenSignsManager.getInstance().getSealOwner(SealType.AVARICE);
				
				if (winningCabal == CabalType.DAWN && (playerCabal != CabalType.DAWN || sealAvariceOwner != CabalType.DAWN))
				{
					player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DAWN);
					canPort = false;
				}
				else if (winningCabal == CabalType.DUSK && (playerCabal != CabalType.DUSK || sealAvariceOwner != CabalType.DUSK))
				{
					player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DUSK);
					canPort = false;
				}
				else if (winningCabal == CabalType.NORMAL && playerCabal != CabalType.NORMAL)
					canPort = true;
				else if (playerCabal == CabalType.NORMAL)
					canPort = false;
			}
			else
			{
				if (playerCabal == CabalType.NORMAL)
					canPort = false;
			}
			
			if (!canPort)
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(SevenSignsManager.SEVEN_SIGNS_HTML_PATH + "necro_no.htm");
				player.sendPacket(html);
			}
			else
			{
				instantTeleport(player, 0);
				player.setIsIn7sDungeon(true);
			}
		}
		else if (command.startsWith("cata"))
		{
			boolean canPort = true;
			if (SevenSignsManager.getInstance().isSealValidationPeriod())
			{
				final CabalType winningCabal = SevenSignsManager.getInstance().getWinningCabal();
				final CabalType sealGnosisOwner = SevenSignsManager.getInstance().getSealOwner(SealType.GNOSIS);
				
				if (winningCabal == CabalType.DAWN && (playerCabal != CabalType.DAWN || sealGnosisOwner != CabalType.DAWN))
				{
					player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DAWN);
					canPort = false;
				}
				else if (winningCabal == CabalType.DUSK && (playerCabal != CabalType.DUSK || sealGnosisOwner != CabalType.DUSK))
				{
					player.sendPacket(SystemMessageId.CAN_BE_USED_BY_DUSK);
					canPort = false;
				}
				else if (winningCabal == CabalType.NORMAL && playerCabal != CabalType.NORMAL)
					canPort = true;
				else if (playerCabal == CabalType.NORMAL)
					canPort = false;
			}
			else
			{
				if (playerCabal == CabalType.NORMAL)
					canPort = false;
			}
			
			if (!canPort)
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile(SevenSignsManager.SEVEN_SIGNS_HTML_PATH + "cata_no.htm");
				player.sendPacket(html);
			}
			else
			{
				instantTeleport(player, 0);
				player.setIsIn7sDungeon(true);
			}
		}
		else if (command.startsWith("exit"))
		{
			instantTeleport(player, 0);
			player.setIsIn7sDungeon(false);
		}
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
		
		return "data/html/gatekeeper/" + filename + ".htm";
	}
}