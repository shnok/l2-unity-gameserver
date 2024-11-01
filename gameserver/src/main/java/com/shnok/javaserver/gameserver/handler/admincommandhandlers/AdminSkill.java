package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SkillList;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AdminSkill implements IAdminCommandHandler
{
	private static final Comparator<L2Skill> COMPARE_SKILLS_BY_ID = Comparator.comparing(L2Skill::getId);
	private static final Comparator<L2Skill> COMPARE_SKILLS_BY_LVL = Comparator.comparing(L2Skill::getLevel);
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_clan_skill",
		"admin_skill"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final Player targetPlayer = getTargetPlayer(player, true);
		
		final StringTokenizer st = new StringTokenizer(command);
		st.nextToken();
		
		int page = 1;
		
		if (command.startsWith("admin_clan_skill"))
		{
			if (!targetPlayer.isClanLeader())
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER).addCharName(targetPlayer));
				showMainPage(player, targetPlayer, page);
				return;
			}
			
			final Clan clan = targetPlayer.getClan();
			
			if (!st.hasMoreTokens())
			{
				showClanSkillList(player, clan, page);
				return;
			}
			
			final String param = st.nextToken();
			if (StringUtil.isDigit(param))
				page = Integer.parseInt(param);
			else
			{
				switch (param)
				{
					case "set":
						try
						{
							final String param2 = st.nextToken();
							if (param2.equals("all"))
							{
								if (clan.addAllClanSkills())
									player.sendMessage("You gave all available skills to " + clan.getName() + " clan.");
							}
							else
							{
								final int id = Integer.parseInt(param2);
								final int level = Integer.parseInt(st.nextToken());
								
								if (id < 370 || id > 391 || level < 1 || level > 3)
								{
									player.sendMessage("Usage: //clan_skill set id level [page]");
									showClanSkillList(player, clan, page);
									return;
								}
								
								final L2Skill skill = SkillTable.getInstance().getInfo(id, level);
								if (skill == null)
								{
									player.sendMessage("Usage: //clan_skill set id level [page]");
									showClanSkillList(player, clan, page);
									return;
								}
								
								if (clan.addClanSkill(skill, false))
									player.sendMessage("You gave " + skill.getName() + " skill to " + clan.getName() + " clan.");
							}
						}
						catch (Exception e)
						{
							player.sendMessage("Usage: //clan_skill set id level [page]");
							
						}
						break;
					
					case "remove":
						try
						{
							final String param2 = st.nextToken();
							if (param2.equals("all"))
							{
								if (clan.removeAllClanSkills())
									player.sendMessage("You removed all skills from " + clan.getName() + " clan.");
							}
							else
							{
								final int skillId = Integer.parseInt(param2);
								
								if (clan.removeClanSkill(skillId))
									player.sendMessage("You removed " + skillId + " skillId from " + clan.getName() + " clan.");
							}
						}
						catch (Exception e)
						{
							player.sendMessage("Usage: //clan_skill remove id|all [page]");
						}
						break;
				}
				
				if (st.hasMoreTokens())
				{
					final String param3 = st.nextToken();
					if (StringUtil.isDigit(param3))
						page = Integer.parseInt(param3);
				}
			}
			
			showClanSkillList(player, clan, page);
		}
		else if (command.startsWith("admin_skill"))
		{
			if (!st.hasMoreTokens())
			{
				showMainPage(player, targetPlayer, page);
				return;
			}
			
			final String param = st.nextToken();
			if (StringUtil.isDigit(param))
				page = Integer.parseInt(param);
			else
			{
				switch (param)
				{
					case "list":
						if (st.hasMoreTokens())
						{
							final String param3 = st.nextToken();
							if (StringUtil.isDigit(param3))
								page = Integer.parseInt(param3);
						}
						showSkillList(player, targetPlayer, page);
						return;
					
					case "set":
						try
						{
							final String param2 = st.nextToken();
							if (param2.equals("all"))
							{
								targetPlayer.rewardSkills();
								player.sendMessage("You gave all available skills to " + targetPlayer.getName() + ".");
							}
							else
							{
								final L2Skill skill = SkillTable.getInstance().getInfo(Integer.parseInt(param2), Integer.parseInt(st.nextToken()));
								if (skill == null)
								{
									player.sendMessage("Usage: //skill set id level [page]");
									return;
								}
								
								targetPlayer.addSkill(skill, true, true);
								targetPlayer.sendPacket(new SkillList(targetPlayer));
								
								player.sendMessage("You gave " + skill.getName() + " skill to " + targetPlayer.getName() + ".");
							}
						}
						catch (Exception e)
						{
							player.sendMessage("Usage: //skill set id level [page]");
						}
						break;
					
					case "remove":
						try
						{
							final String param2 = st.nextToken();
							if (param2.equals("all"))
							{
								for (L2Skill skill : targetPlayer.getSkills().values())
									targetPlayer.removeSkill(skill.getId(), true);
								
								player.sendMessage("You removed all skills from " + targetPlayer.getName() + ".");
								
								targetPlayer.sendPacket(new SkillList(targetPlayer));
							}
							else
							{
								final int skillId = Integer.parseInt(param2);
								
								targetPlayer.removeSkill(skillId, true);
								
								player.sendMessage("You removed " + skillId + " skillId from " + targetPlayer.getName() + ".");
							}
						}
						catch (Exception e)
						{
							player.sendMessage("Usage: //skill remove id [page]");
						}
						break;
				}
				
				if (st.hasMoreTokens())
				{
					final String param3 = st.nextToken();
					if (StringUtil.isDigit(param3))
						page = Integer.parseInt(param3);
				}
			}
			
			showMainPage(player, targetPlayer, page);
		}
	}
	
	private static void showMainPage(Player player, Player targetPlayer, int page)
	{
		// Load static htm.
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/char_skills.htm");
		html.replace("%name%", targetPlayer.getName());
		
		int row = 0;
		
		// Generate data.
		final Pagination<L2Skill> list = new Pagination<>(targetPlayer.getSkills().values().stream(), page, PAGE_LIMIT_12);
		
		for (L2Skill skill : list)
		{
			list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=35>", skill.getId(), "</td><td width=220><a action=\"bypass -h admin_skill remove ", skill.getId(), "\">", skill.getName(), "</a></td><td width=25>", skill.getLevel(), "</td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
			
			row++;
		}
		
		list.generateSpace(20);
		list.generatePages("bypass admin_skill %page%");
		
		html.replace("%content%", list.getContent());
		player.sendPacket(html);
	}
	
	private static void showSkillList(Player player, Player targetPlayer, int page)
	{
		// Load static htm.
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/char_skills_list.htm");
		html.replace("%name%", targetPlayer.getName());
		
		int row = 0;
		
		final Map<Object, Optional<L2Skill>> data = SkillTable.getInstance().getSkills().stream().filter(s -> s.getLevel() < 99).collect(Collectors.groupingBy(L2Skill::getId, Collectors.maxBy(COMPARE_SKILLS_BY_LVL)));
		final List<L2Skill> skills = new ArrayList<>(data.size());
		for (Optional<L2Skill> skill : data.values())
		{
			if (skill.isPresent())
				skills.add(skill.get());
		}
		
		// Generate data.
		final Pagination<L2Skill> list = new Pagination<>(skills.stream(), page, PAGE_LIMIT_12, COMPARE_SKILLS_BY_ID);
		for (L2Skill skill : list)
		{
			list.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
			list.append("<td width=35>", skill.getId(), "</td><td width=220>", skill.getName(), "</td><td width=25>", skill.getLevel(), "</td>");
			list.append("</tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
			
			row++;
		}
		
		list.generateSpace(20);
		list.generatePages("bypass admin_skill list %page%");
		
		html.replace("%content%", list.getContent());
		player.sendPacket(html);
	}
	
	private static void showClanSkillList(Player player, Clan targetClan, int page)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setFile("data/html/admin/clan_skills.htm");
		html.replace("%name%", targetClan.getName());
		
		final Pagination<L2Skill> list = new Pagination<>(Arrays.stream(SkillTable.getInstance().getClanSkills()), page, PAGE_LIMIT_15, COMPARE_SKILLS_BY_ID);
		list.append("<table width=270><tr><td width=220>Name</td><td width=20>Lvl</td><td width=30>Id</td></tr>");
		
		for (L2Skill skill : list)
		{
			final L2Skill currentSkill = targetClan.getClanSkills().get(skill.getId());
			if (currentSkill == null)
				list.append("<tr><td>", skill.getName(), "</td><td>", skill.getLevel(), "</td><td>", skill.getId(), "</td></tr>");
			else
				list.append("<tr><td><a action=\"bypass -h admin_clan_skill remove ", skill.getId(), "\">", skill.getName(), "</a>", "</td><td>", currentSkill.getLevel(), "</td><td>", skill.getId(), "</td></tr>");
		}
		list.append("</table><br>");
		
		list.generateSpace();
		list.generatePages("bypass admin_clan_skill %page%");
		
		html.replace("%content%", list.getContent());
		player.sendPacket(html);
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}