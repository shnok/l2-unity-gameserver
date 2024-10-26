package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.GMViewPledgeInfo;

public class AdminPledge implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_pledge"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final Player targetPlayer = getTargetPlayer(player, true);
		
		if (command.startsWith("admin_pledge"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			try
			{
				final String action = st.nextToken();
				
				if (action.equals("create"))
				{
					try
					{
						final String parameter = st.nextToken();
						final long cet = targetPlayer.getClanCreateExpiryTime();
						
						targetPlayer.setClanCreateExpiryTime(0);
						
						final Clan clan = ClanTable.getInstance().createClan(targetPlayer, parameter);
						if (clan != null)
							player.sendMessage("Clan " + parameter + " have been created. Clan leader is " + targetPlayer.getName() + ".");
						else
						{
							targetPlayer.setClanCreateExpiryTime(cet);
							player.sendMessage("There was a problem while creating the clan.");
						}
					}
					catch (Exception e)
					{
						player.sendMessage("Invalid string parameter for //pledge create.");
					}
				}
				else
				{
					final Clan targetClan = targetPlayer.getClan();
					if (targetClan == null)
					{
						player.sendPacket(SystemMessageId.TARGET_MUST_BE_IN_CLAN);
						sendFile(player, "game_menu.htm");
						return;
					}
					
					if (action.equals("dismiss"))
					{
						ClanTable.getInstance().destroyClan(targetClan);
						player.sendMessage("The clan is now disbanded.");
					}
					else if (action.equals("info"))
						player.sendPacket(new GMViewPledgeInfo(targetClan, targetPlayer));
					else if (action.equals("level"))
					{
						try
						{
							final int level = Integer.parseInt(st.nextToken());
							if (level >= 0 && level < 9)
							{
								targetClan.changeLevel(level);
								player.sendMessage("You have set clan " + targetClan.getName() + " to level " + level);
							}
							else
								player.sendMessage("This clan level is incorrect. Put a number between 0 and 8.");
						}
						catch (Exception e)
						{
							player.sendMessage("Invalid number parameter for //pledge setlevel.");
						}
					}
					else if (action.startsWith("rep"))
					{
						try
						{
							final int points = Integer.parseInt(st.nextToken());
							
							if (targetClan.getLevel() < 5)
							{
								player.sendMessage("Only clans of level 5 or above may receive reputation points.");
								sendFile(player, "game_menu.htm");
								return;
							}
							
							targetClan.addReputationScore(points);
							player.sendMessage("You " + (points > 0 ? "added " : "removed ") + Math.abs(points) + " points " + (points > 0 ? "to " : "from ") + targetClan.getName() + "'s reputation. Their current score is: " + targetClan.getReputationScore());
						}
						catch (Exception e)
						{
							player.sendMessage("Invalid number parameter for //pledge rep.");
						}
					}
					else if (action.startsWith("transfer"))
					{
						final ClanMember member = targetClan.getClanMember(targetPlayer.getObjectId());
						if (member == null)
						{
							player.sendMessage(targetPlayer.getName() + " can't be set as the new Clan leader of " + targetClan.getName() + ".");
							return;
						}
						
						if (targetClan.getLeader() == member)
						{
							player.sendMessage(targetPlayer.getName() + " is already the Clan leader of " + targetClan.getName() + ".");
							return;
						}
						
						targetClan.setNewLeader(member);
						player.sendMessage("You set " + member.getName() + " as the new Clan leader of " + targetClan.getName() + ".");
					}
				}
			}
			catch (Exception e)
			{
				player.sendMessage("Usage: //pledge create|dismiss|info|level|rep|transfer");
			}
		}
		sendFile(player, "game_menu.htm");
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}