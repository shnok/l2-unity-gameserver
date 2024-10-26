package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;

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