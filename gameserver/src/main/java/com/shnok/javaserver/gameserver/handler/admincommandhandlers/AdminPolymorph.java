package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

public class AdminPolymorph implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_polymorph"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final Creature targetCreature = getTargetCreature(player, true);
		
		// Force dismount.
		final Player targetPlayer = targetCreature.getActingPlayer();
		if (targetPlayer != null && targetPlayer.isMounted())
			targetPlayer.dismount();
		
		if (targetCreature.getPolymorphTemplate() != null)
			targetCreature.unpolymorph();
		else
		{
			try
			{
				final StringTokenizer st = new StringTokenizer(command);
				st.nextToken();
				
				if (!targetCreature.polymorph(Integer.parseInt(st.nextToken())))
					player.sendPacket(SystemMessageId.APPLICANT_INFORMATION_INCORRECT);
			}
			catch (Exception e)
			{
				player.sendMessage("Usage: //polymorph npcId");
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}