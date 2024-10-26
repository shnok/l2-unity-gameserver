package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.data.xml.PlayerData;
import com.shnok.javaserver.gameserver.data.xml.PlayerLevelData;
import com.shnok.javaserver.gameserver.enums.actors.ClassId;
import com.shnok.javaserver.gameserver.enums.actors.Sex;
import com.shnok.javaserver.gameserver.enums.skills.AbnormalEffect;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.records.PlayerLevel;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.SkillCoolTime;

public class AdminEditChar implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_debug",
		"admin_party_info",
		"admin_remove",
		"admin_set"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		if (command.startsWith("admin_debug"))
		{
			final Player targetPlayer = (st.hasMoreTokens()) ? getTargetPlayer(player, st.nextToken(), true) : getTargetPlayer(player, true);
			
			gatherPlayerInfo(player, targetPlayer);
		}
		else if (command.startsWith("admin_party_info"))
		{
			final Player targetPlayer = (st.hasMoreTokens()) ? getTargetPlayer(player, st.nextToken(), true) : getTargetPlayer(player, true);
			
			final Party party = targetPlayer.getParty();
			if (party == null)
			{
				player.sendMessage(targetPlayer.getName() + " isn't in a party.");
				return;
			}
			
			final StringBuilder sb = new StringBuilder(400);
			for (Player member : party.getMembers())
			{
				if (!party.isLeader(member))
					StringUtil.append(sb, "<tr><td width=150><a action=\"bypass -h admin_debug ", member.getName(), "\">", member.getName(), " (", member.getStatus().getLevel(), ")</a></td><td width=120 align=right>", member.getClassId().toString(), "</td></tr>");
				else
					StringUtil.append(sb, "<tr><td width=150><a action=\"bypass -h admin_debug ", member.getName(), "\"><font color=\"LEVEL\">", member.getName(), " (", member.getStatus().getLevel(), ")</font></a></td><td width=120 align=right>", member.getClassId().toString(), "</td></tr>");
			}
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setFile("data/html/admin/partyinfo.htm");
			html.replace("%name%", targetPlayer.getName());
			html.replace("%party%", sb.toString());
			player.sendPacket(html);
		}
		else if (command.startsWith("admin_remove"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendMessage("Usage: //remove <clan_penalty|death_penalty|skill_reuse>");
				return;
			}
			
			final Player targetPlayer = getTargetPlayer(player, true);
			
			switch (st.nextToken())
			{
				case "clan_penalty":
					if (!st.hasMoreTokens())
					{
						player.sendMessage("Usage: //remove clan_penalty join|create");
						return;
					}
					
					final String param = st.nextToken();
					if (param.contains("create"))
						targetPlayer.setClanCreateExpiryTime(0);
					else if (param.contains("join"))
						targetPlayer.setClanJoinExpiryTime(0);
					
					player.sendMessage("Clan penalty is successfully removed for " + targetPlayer.getName() + ".");
					break;
				
				case "death_penalty":
					targetPlayer.removeDeathPenaltyBuffLevel();
					
					if (targetPlayer != player)
						player.sendMessage(targetPlayer.getName() + "'s Death Penalty has been lifted.");
					break;
				
				case "skill_reuse":
					targetPlayer.getReuseTimeStamp().clear();
					targetPlayer.getDisabledSkills().clear();
					targetPlayer.sendPacket(new SkillCoolTime(targetPlayer));
					
					player.sendMessage(targetPlayer.getName() + "'s skills reuse timers are now cleaned.");
					break;
				
				default:
					player.sendMessage("Usage: //remove <clan_penalty|death_penalty|skill_reuse>");
					break;
			}
		}
		else if (command.startsWith("admin_set"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendMessage("Usage: //set <access|class|color|exp|karma|level>");
				player.sendMessage("Usage: //set <name|noble|rec|sex|sp|tcolor|title>");
				return;
			}
			
			final WorldObject targetWorldObject = getTarget(WorldObject.class, player, true);
			
			switch (st.nextToken())
			{
				case "access":
					try
					{
						final int paramCount = st.countTokens();
						if (paramCount == 1)
						{
							final int lvl = Integer.parseInt(st.nextToken());
							if (targetWorldObject instanceof Player targetPlayer)
							{
								targetPlayer.setAccessLevel(lvl);
								
								if (lvl < 0)
									targetPlayer.logout(false);
								
								player.sendMessage(targetPlayer.getName() + "'s access level is now set to " + lvl + ".");
							}
							else
								player.sendPacket(SystemMessageId.INVALID_TARGET);
						}
						else if (paramCount == 2)
						{
							final String name = st.nextToken();
							final int lvl = Integer.parseInt(st.nextToken());
							
							final Player worldPlayer = World.getInstance().getPlayer(name);
							if (worldPlayer != null)
							{
								worldPlayer.setAccessLevel(lvl);
								
								if (lvl < 0)
									worldPlayer.logout(false);
								
								player.sendMessage(worldPlayer.getName() + "'s access level is now set to " + lvl + ".");
							}
							else
							{
								try (Connection con = ConnectionPool.getConnection();
									PreparedStatement ps = con.prepareStatement("UPDATE characters SET accesslevel=? WHERE char_name=?"))
								{
									ps.setInt(1, lvl);
									ps.setString(2, name);
									ps.execute();
									
									final int count = ps.getUpdateCount();
									if (count == 0)
										player.sendMessage(name + "couldn't be found - its access level is unaltered.");
									else
										player.sendMessage(name + "'s access level is now set to " + lvl + ".");
								}
								catch (Exception e)
								{
									LOGGER.error("Couldn't update access.", e);
								}
							}
						}
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set access <level> | <name> <level>");
					}
					break;
				
				case "class":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final int newClassId = Integer.parseInt(st.nextToken());
						if (newClassId < 0 || newClassId > ClassId.VALUES.length)
							return;
						
						final ClassId newClass = ClassId.VALUES[newClassId];
						
						// Don't bother with dummy classes.
						if (newClass.getLevel() == -1)
						{
							player.sendMessage("You tried to set an invalid class for " + targetPlayer.getName() + ".");
							return;
						}
						
						// Don't bother edit ClassId if already set the same.
						if (targetPlayer.getClassId() == newClass)
						{
							player.sendMessage(targetPlayer.getName() + " is already a(n) " + newClass.toString() + ".");
							return;
						}
						
						targetPlayer.abortAll(false);
						targetPlayer.startAbnormalEffect(AbnormalEffect.HOLD_2);
						targetPlayer.removeKnownObject(targetPlayer);
						targetPlayer.decayMe();
						
						ThreadPool.schedule(() ->
						{
							targetPlayer.setClassId(newClass.getId());
							if (!targetPlayer.isSubClassActive())
								targetPlayer.setBaseClass(newClass);
							
							targetPlayer.spawnMe();
							targetPlayer.store();
							targetPlayer.refreshWeightPenalty();
							targetPlayer.refreshHennaList();
							targetPlayer.broadcastUserInfo();
							targetPlayer.stopAbnormalEffect(AbnormalEffect.HOLD_2);
							targetPlayer.sendPacket(ActionFailed.STATIC_PACKET);
							
							player.sendMessage("You successfully set " + targetPlayer.getName() + " class to " + newClass.toString() + ".");
							
						}, 4000);
					}
					catch (Exception e)
					{
						sendFile(player, "charclasses.htm");
					}
					break;
				
				case "color":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						targetPlayer.getAppearance().setNameColor(Integer.decode("0x" + st.nextToken()));
						targetPlayer.broadcastUserInfo();
						
						player.sendMessage("You successfully set color name of " + targetPlayer.getName() + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set color <number>");
					}
					break;
				
				case "exp":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final long newExp = Long.parseLong(st.nextToken());
						final long currentExp = targetPlayer.getStatus().getExp();
						
						if (currentExp < newExp)
							targetPlayer.addExpAndSp(newExp - currentExp, 0);
						else if (currentExp > newExp)
							targetPlayer.removeExpAndSp(currentExp - newExp, 0);
						
						targetPlayer.broadcastUserInfo();
						
						player.sendMessage("You successfully set " + targetPlayer.getName() + "'s XP to " + newExp + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set exp <number>");
					}
					break;
				
				case "karma":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final int newKarma = Integer.parseInt(st.nextToken());
						if (newKarma < 0)
						{
							player.sendMessage("The karma value must be greater or equal to 0.");
							return;
						}
						
						targetPlayer.setKarma(newKarma);
						
						player.sendMessage("You successfully set " + targetPlayer.getName() + "'s karma to " + newKarma + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set karma <number>");
					}
					break;
				
				case "level":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final int newLevel = Integer.parseInt(st.nextToken());
						final PlayerLevel pl = PlayerLevelData.getInstance().getPlayerLevel(newLevel);
						if (pl == null)
						{
							player.sendMessage("Invalid used level for //set level.");
							return;
						}
						
						final long pXp = targetPlayer.getStatus().getExp();
						final long tXp = pl.requiredExpToLevelUp();
						
						if (pXp > tXp)
							targetPlayer.removeExpAndSp(pXp - tXp, 0);
						else if (pXp < tXp)
							targetPlayer.addExpAndSp(tXp - pXp, 0);
						
						player.sendMessage("You successfully set " + targetPlayer.getName() + "'s level to " + newLevel + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set level <number>");
					}
					break;
				
				case "name":
					try
					{
						final String newName = st.nextToken();
						
						switch (targetWorldObject)
						{
							case Player targetPlayer:
								// Invalid pattern.
								if (!StringUtil.isValidString(newName, "^[A-Za-z0-9]{1,16}$"))
								{
									player.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
									return;
								}
								
								// Name is a npc name.
								if (NpcData.getInstance().getTemplateByName(newName) != null)
								{
									player.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
									return;
								}
								
								// Name already exists.
								if (PlayerInfoTable.getInstance().getPlayerObjectId(newName) > 0)
								{
									player.sendPacket(SystemMessageId.INCORRECT_NAME_TRY_AGAIN);
									return;
								}
								
								targetPlayer.setName(newName);
								PlayerInfoTable.getInstance().updatePlayerData(targetPlayer, false);
								targetPlayer.broadcastUserInfo();
								targetPlayer.store();
								
								player.sendMessage("You successfully set your target's name to " + targetPlayer.getName() + ".");
								break;
							
							case Npc targetNpc:
								targetNpc.setName(newName);
								targetNpc.broadcastPacket(new NpcInfo(targetNpc, null));
								
								player.sendMessage("You successfully set your target's name to " + targetNpc.getName() + ".");
								break;
							
							default:
								player.sendPacket(SystemMessageId.INVALID_TARGET);
								break;
						}
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set name <name>");
					}
					break;
				
				case "noble":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						targetPlayer.setNoble(!targetPlayer.isNoble(), true);
						player.sendMessage("You have modified " + targetPlayer.getName() + "'s noble status.");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set rec <number>");
					}
					break;
				
				case "rec":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final int newRec = Integer.parseInt(st.nextToken());
						
						targetPlayer.setRecomHave(newRec);
						targetPlayer.broadcastUserInfo();
						
						player.sendMessage("You successfully set " + targetPlayer.getName() + " to " + newRec + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set rec <number>");
					}
					break;
				
				case "sex":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final Sex sex = Enum.valueOf(Sex.class, st.nextToken().toUpperCase());
						
						if (sex == targetPlayer.getAppearance().getSex())
						{
							player.sendMessage(targetPlayer.getName() + "'s sex is already defined as " + sex.toString() + ".");
							return;
						}
						
						targetPlayer.abortAll(false);
						targetPlayer.startAbnormalEffect(AbnormalEffect.HOLD_2);
						targetPlayer.removeKnownObject(targetPlayer);
						targetPlayer.decayMe();
						
						ThreadPool.schedule(() ->
						{
							targetPlayer.getAppearance().setSex(sex);
							targetPlayer.spawnMe();
							targetPlayer.store();
							targetPlayer.broadcastUserInfo();
							targetPlayer.stopAbnormalEffect(AbnormalEffect.HOLD_2);
							targetPlayer.sendPacket(ActionFailed.STATIC_PACKET);
							
							player.sendMessage("You successfully set " + targetPlayer.getName() + " gender to " + sex.toString() + ".");
							
						}, 4000);
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set sex <sex>");
					}
					break;
				
				case "sp":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						final int newSp = Integer.parseInt(st.nextToken());
						final int currentSp = targetPlayer.getStatus().getSp();
						
						if (currentSp < newSp)
							targetPlayer.addExpAndSp(0, newSp - currentSp);
						else if (currentSp > newSp)
							targetPlayer.removeExpAndSp(0, currentSp - newSp);
						
						targetPlayer.broadcastUserInfo();
						
						player.sendMessage("You successfully set " + targetPlayer.getName() + "'s SP to " + newSp + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set sp <number>");
					}
					break;
				
				case "tcolor":
					try
					{
						if (!(targetWorldObject instanceof Player targetPlayer))
							return;
						
						targetPlayer.getAppearance().setTitleColor(Integer.decode("0x" + command.substring(16)));
						targetPlayer.broadcastUserInfo();
						
						player.sendMessage("You successfully set title color name of " + targetPlayer.getName() + ".");
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set tcolor <number>");
					}
					break;
				
				case "title":
					try
					{
						final String newTitle = st.nextToken();
						
						switch (targetWorldObject)
						{
							case Player targetPlayer:
								targetPlayer.setTitle(newTitle);
								targetPlayer.broadcastTitleInfo();
								
								player.sendMessage("You successfully set your target's title to " + targetPlayer.getTitle() + ".");
								break;
							
							case Npc targetNpc:
								targetNpc.setTitle(newTitle);
								targetNpc.broadcastPacket(new NpcInfo(targetNpc, null));
								
								player.sendMessage("You successfully set your target's title to " + targetNpc.getTitle() + ".");
								break;
							
							default:
								player.sendPacket(SystemMessageId.INVALID_TARGET);
								break;
						}
					}
					catch (Exception e)
					{
						player.sendMessage("Usage: //set title <title>");
					}
					break;
				
				default:
					player.sendMessage("Usage: //set access|class|color|exp|karma");
					player.sendMessage("Usage: //set level|name|rec|sex|sp|tcolor|title");
					break;
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Gather {@link Player} informations and send them on an existing {@link NpcHtmlMessage}. Additionally, set the target to this {@link Player}.
	 * @param player : The {@link Player} who requested that action.
	 * @param targetPlayer : The {@link Player} target to gather informations from.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	public static void gatherPlayerInfo(Player player, Player targetPlayer, NpcHtmlMessage html)
	{
		player.setTarget(targetPlayer);
		
		html.setFile("data/html/admin/charinfo.htm");
		html.replace("%name%", targetPlayer.getName());
		html.replace("%objid%", targetPlayer.getObjectId());
		html.replace("%clan%", (targetPlayer.getClan() != null) ? "<a action=\"bypass -h admin_pledge info " + targetPlayer.getClan().getName() + "\">" + targetPlayer.getClan().getName() + "</a>" : "N/A");
		html.replace("%party%", (targetPlayer.getParty() != null) ? "<a action=\"bypass -h admin_party_info " + targetPlayer.getName() + "\">" + targetPlayer.getParty().getMembers().size() + " members</a>" : "N/A");
		html.replace("%baseclass%", PlayerData.getInstance().getClassNameById(targetPlayer.getBaseClass()));
		html.replace("%xp%", targetPlayer.getStatus().getExp());
		html.replace("%curai%", targetPlayer.getAI().getCurrentIntention().getType().toString());
		html.replace("%nextai%", targetPlayer.getAI().getNextIntention().getType().toString());
		html.replace("%loc%", targetPlayer.getPosition().toString());
		html.replace("%account%", targetPlayer.getAccountName());
		html.replace("%ip%", (targetPlayer.getClient() == null || targetPlayer.getClient().isDetached()) ? "Disconnected" : targetPlayer.getClient().getConnection().getInetAddress().getHostAddress());
	}
	
	/**
	 * Gather {@link Player} informations and send them on a new {@link NpcHtmlMessage}. Additionally, set the target to this {@link Player}.
	 * @param player : The {@link Player} who requested that action.
	 * @param targetPlayer : The {@link Player} target to gather informations from.
	 */
	private static void gatherPlayerInfo(Player player, Player targetPlayer)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		gatherPlayerInfo(player, targetPlayer, html);
		player.sendPacket(html);
	}
}