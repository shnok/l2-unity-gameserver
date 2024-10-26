package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.BuyListManager;
import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.data.xml.ScriptData;
import com.shnok.javaserver.gameserver.enums.DropType;
import com.shnok.javaserver.gameserver.enums.EventHandler;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.enums.skills.ElementType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.ai.Desire;
import com.shnok.javaserver.gameserver.model.actor.container.attackable.AggroList;
import com.shnok.javaserver.gameserver.model.actor.container.npc.AggroInfo;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.actor.instance.StaticObject;
import com.shnok.javaserver.gameserver.model.buylist.NpcBuyList;
import com.shnok.javaserver.gameserver.model.item.DropCategory;
import com.shnok.javaserver.gameserver.model.item.DropData;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.spawn.ASpawn;
import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.scripting.Quest;
import com.shnok.javaserver.gameserver.scripting.QuestTimer;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AdminInfo implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_info"
	};
	
	private static final DecimalFormat PERCENT = new DecimalFormat("#.###");
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		if (command.startsWith("admin_info"))
		{
			final WorldObject targetWorldObject = getTarget(WorldObject.class, player, true);
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			if (targetWorldObject instanceof Door targetDoor)
			{
				html.setFile("data/html/admin/doorinfo.htm");
				html.replace("%name%", targetDoor.getName());
				html.replace("%objid%", targetDoor.getObjectId());
				html.replace("%doorid%", targetDoor.getTemplate().getId());
				html.replace("%doortype%", targetDoor.getTemplate().getType().toString());
				html.replace("%doorlvl%", targetDoor.getTemplate().getLevel());
				html.replace("%residence%", (targetDoor.getResidence() != null) ? targetDoor.getResidence().getName() : "none");
				html.replace("%opentype%", targetDoor.getTemplate().getOpenType().toString());
				html.replace("%initial%", targetDoor.getTemplate().isOpened() ? "Opened" : "Closed");
				html.replace("%ot%", targetDoor.getTemplate().getOpenTime());
				html.replace("%ct%", targetDoor.getTemplate().getCloseTime());
				html.replace("%rt%", targetDoor.getTemplate().getRandomTime());
				html.replace("%controlid%", targetDoor.getTemplate().getTriggerId());
				html.replace("%hp%", (int) targetDoor.getStatus().getHp());
				html.replace("%hpmax%", targetDoor.getStatus().getMaxHp());
				html.replace("%hpratio%", targetDoor.getStatus().getUpgradeHpRatio());
				html.replace("%pdef%", targetDoor.getStatus().getPDef(null));
				html.replace("%mdef%", targetDoor.getStatus().getMDef(null, null));
				html.replace("%spawn%", targetDoor.getPosition().toString());
				html.replace("%height%", targetDoor.getTemplate().getCollisionHeight());
			}
			else if (targetWorldObject instanceof Npc targetNpc)
			{
				final StringTokenizer st = new StringTokenizer(command, " ");
				st.nextToken();
				
				if (!st.hasMoreTokens())
					sendGeneralInfos(targetNpc, html, 0);
				else
				{
					final String subCommand = st.nextToken();
					switch (subCommand)
					{
						case "ai":
							try
							{
								final int page = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 0;
								
								sendAiInfos(targetNpc, html, page);
							}
							catch (Exception e)
							{
								sendAiInfos(targetNpc, html, 0);
							}
							break;
						
						case "aggro":
							sendAggroInfos(targetNpc, html);
							break;
						
						case "desire":
							sendDesireInfos(targetNpc, html);
							break;
						
						case "drop", "spoil":
							try
							{
								final int page = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
								final int subPage = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
								
								sendDropInfos(targetNpc, html, page, subPage, subCommand.equalsIgnoreCase("drop"));
							}
							catch (Exception e)
							{
								sendDropInfos(targetNpc, html, 1, 1, true);
							}
							break;
						
						case "script":
							sendScriptInfos(targetNpc, html);
							break;
						
						case "shop":
							sendShopInfos(targetNpc, html);
							break;
						
						case "skill":
							sendSkillInfos(targetNpc, html);
							break;
						
						case "spawn":
							sendSpawnInfos(player, targetNpc, html);
							break;
						
						case "stat":
							sendStatsInfos(targetNpc, html);
							break;
						
						default:
							sendGeneralInfos(targetNpc, html, StringUtil.isDigit(subCommand) ? Integer.valueOf(subCommand) : 0);
					}
				}
			}
			else if (targetWorldObject instanceof Player targetPlayer)
			{
				AdminEditChar.gatherPlayerInfo(player, targetPlayer, html);
			}
			else if (targetWorldObject instanceof Summon targetSummon)
			{
				final Player owner = targetWorldObject.getActingPlayer();
				
				html.setFile("data/html/admin/petinfo.htm");
				html.replace("%name%", (targetSummon.getName() == null) ? "N/A" : targetSummon.getName());
				html.replace("%level%", targetSummon.getStatus().getLevel());
				html.replace("%exp%", targetSummon.getStatus().getExp());
				html.replace("%owner%", (owner == null) ? "N/A" : " <a action=\"bypass -h admin_debug " + owner.getName() + "\">" + owner.getName() + "</a>");
				html.replace("%class%", targetSummon.getClass().getSimpleName());
				html.replace("%ai%", targetSummon.getAI().getCurrentIntention().getType().name());
				html.replace("%hp%", (int) targetSummon.getStatus().getHp() + "/" + targetSummon.getStatus().getMaxHp());
				html.replace("%mp%", (int) targetSummon.getStatus().getMp() + "/" + targetSummon.getStatus().getMaxMp());
				html.replace("%karma%", targetSummon.getKarma());
				html.replace("%undead%", (targetSummon.isUndead()) ? "yes" : "no");
				
				if (targetWorldObject instanceof Pet targetPet)
				{
					html.replace("%inv%", (owner == null) ? "N/A" : " <a action=\"bypass admin_summon inventory\">view</a>");
					html.replace("%food%", targetPet.getCurrentFed() + "/" + targetPet.getPetData().maxMeal());
					html.replace("%load%", targetPet.getInventory().getTotalWeight() + "/" + targetPet.getWeightLimit());
				}
				else
				{
					html.replace("%inv%", "none");
					html.replace("%food%", "N/A");
					html.replace("%load%", "N/A");
				}
			}
			else if (targetWorldObject instanceof StaticObject targetStaticObject)
			{
				html.setFile("data/html/admin/staticinfo.htm");
				html.replace("%x%", targetStaticObject.getX());
				html.replace("%y%", targetStaticObject.getY());
				html.replace("%z%", targetStaticObject.getZ());
				html.replace("%objid%", targetStaticObject.getObjectId());
				html.replace("%staticid%", targetStaticObject.getStaticObjectId());
				html.replace("%class%", targetStaticObject.getClass().getSimpleName());
			}
			player.sendPacket(html);
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 * @param index : The used index.
	 */
	private static void sendAiInfos(Npc npc, NpcHtmlMessage html, int index)
	{
		html.setFile("data/html/admin/npcinfo/default.htm");
		
		final StringBuilder sb = new StringBuilder(500);
		sb.append("<center><table width=240><tr><td width=70>");
		
		switch (index)
		{
			default:
			case 0:
				sb.append("[AI Path]</td><td width=70><a action=\"bypass -h admin_info ai 1\">Template</a></td><td width=70><a action=\"bypass -h admin_info ai 2\">Spawn</a></td><td width=70><a action=\"bypass -h admin_info ai 3\">Npc</a></td></tr></table></center><br>");
				
				// Retrieve scripts non related to quests.
				final Quest aiScript = npc.getTemplate().getEventQuests().values().stream().flatMap(List::stream).filter(q -> !q.isRealQuest()).findFirst().orElse(null);
				if (aiScript == null)
					StringUtil.append(sb, "This NPC doesn't hold any AI related script.");
				else
				{
					Class<?> checkedClass = aiScript.getClass();
					while (checkedClass != Quest.class)
					{
						StringUtil.append(sb, checkedClass.getSimpleName(), "<br1>");
						checkedClass = checkedClass.getSuperclass();
					}
				}
				break;
			
			case 1:
				sb.append("<a action=\"bypass -h admin_info ai 0\">AI Path</a></td><td width=70>[Template]</a></td><td width=70><a action=\"bypass -h admin_info ai 2\">Spawn</a></td><td width=70><a action=\"bypass -h admin_info ai 3\">Npc</a></td></tr></table></center><br>");
				
				// Feed Npc template AI params.
				if (npc.getTemplate().getAiParams().isEmpty())
					StringUtil.append(sb, "This NPC's template doesn't hold any AI parameters.");
				else
				{
					for (Entry<String, String> aiParam : npc.getTemplate().getAiParams().entrySet())
						StringUtil.append(sb, "<font color=\"LEVEL\">[", aiParam.getKey(), "]</font> ", aiParam.getValue(), "<br1>");
				}
				break;
			
			case 2:
				sb.append("<a action=\"bypass -h admin_info ai 0\">AI Path</a></td><td width=70><a action=\"bypass -h admin_info ai 1\">Template</a></td><td width=70>[Spawn]</td><td width=70><a action=\"bypass -h admin_info ai 3\">Npc</a></td></tr></table></center><br>");
				
				// Feed Npc Memos.
				final ASpawn spawn = npc.getSpawn();
				if (spawn == null)
					StringUtil.append(sb, "This NPC doesn't have any Spawn.");
				else if (spawn.getMemo().isEmpty())
					StringUtil.append(sb, "This NPC Spawn doesn't hold any memos.");
				else
				{
					for (Entry<String, String> aiParam : spawn.getMemo().entrySet())
						StringUtil.append(sb, "<font color=\"LEVEL\">[", aiParam.getKey(), "]</font> ", aiParam.getValue(), "<br1>");
				}
				break;
			
			case 3:
				sb.append("<a action=\"bypass -h admin_info ai 0\">AI Path</a></td><td width=70><a action=\"bypass -h admin_info ai 1\">Template</a></td><td width=70><a action=\"bypass -h admin_info ai 2\">Spawn</a></td><td width=70>[Npc]</td></tr></table></center><br>");
				
				StringUtil.append(sb, "<table width=280>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[i_ai0]</font> ", npc._i_ai0, "</td><td width=140 align=left><font color=\"LEVEL\">[i_quest0]</font> ", npc._i_quest0, "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[i_ai1]</font> ", npc._i_ai1, "</td><td width=140 align=left><font color=\"LEVEL\">[i_quest1]</font> ", npc._i_quest1, "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[i_ai2]</font> ", npc._i_ai2, "</td><td width=140 align=left><font color=\"LEVEL\">[i_quest2]</font> ", npc._i_quest2, "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[i_ai3]</font> ", npc._i_ai3, "</td><td width=140 align=left><font color=\"LEVEL\">[i_quest3]</font> ", npc._i_quest3, "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[i_ai4]</font> ", npc._i_ai4, "</td><td width=140 align=left><font color=\"LEVEL\">[i_quest4]</font> ", npc._i_quest4, "</td></tr>");
				StringUtil.append(sb, "</table><br><table width=280>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[c_ai0]</font> ", StringUtil.getCreatureDescription(sb, npc._c_ai0), "</td><td width=140 align=left><font color=\"LEVEL\">[c_quest0]</font> ", StringUtil.getCreatureDescription(sb, npc._c_quest0), "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[c_ai1]</font> ", StringUtil.getCreatureDescription(sb, npc._c_ai1), "</td><td width=140 align=left><font color=\"LEVEL\">[c_quest1]</font> ", StringUtil.getCreatureDescription(sb, npc._c_quest1), "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[c_ai2]</font> ", StringUtil.getCreatureDescription(sb, npc._c_ai2), "</td><td width=140 align=left><font color=\"LEVEL\">[c_quest2]</font> ", StringUtil.getCreatureDescription(sb, npc._c_quest2), "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[c_ai3]</font> ", StringUtil.getCreatureDescription(sb, npc._c_ai3), "</td><td width=140 align=left><font color=\"LEVEL\">[c_quest3]</font> ", StringUtil.getCreatureDescription(sb, npc._c_quest3), "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[c_ai4]</font> ", StringUtil.getCreatureDescription(sb, npc._c_ai4), "</td><td width=140 align=left><font color=\"LEVEL\">[c_quest4]</font> ", StringUtil.getCreatureDescription(sb, npc._c_quest4), "</td></tr>");
				StringUtil.append(sb, "</table><br><table width=280>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[param1]</font> ", npc._param1, "</td><td width=140 align=left><font color=\"LEVEL\">[flag]</font> ", npc._flag, "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[param2]</font> ", npc._param2, "</td><td width=140 align=left><font color=\"LEVEL\">[respawnTime]</font> ", npc._respawnTime, "</td></tr>");
				StringUtil.append(sb, "<tr><td width=140><font color=\"LEVEL\">[param3]</font> ", npc._param3, "</td><td width=140 align=left><font color=\"LEVEL\">[weightPoint]</font> ", npc._weightPoint, "</td></tr>");
				StringUtil.append(sb, "</table>");
				break;
		}
		html.replace("%content%", sb.toString());
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with {@link AggroList} informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	private static void sendAggroInfos(Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/default.htm");
		if (!(npc instanceof Attackable attackable))
		{
			html.replace("%content%", "This NPC can't build aggro towards targets.<br><button value=\"Refresh\" action=\"bypass -h admin_info aggro\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\">");
			return;
		}
		
		final AggroList aggroList = attackable.getAI().getAggroList();
		if (aggroList.isEmpty())
		{
			html.replace("%content%", "This NPC's AggroList is empty.<br><button value=\"Refresh\" action=\"bypass -h admin_info aggro\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\">");
			return;
		}
		
		final StringBuilder sb = new StringBuilder(500);
		sb.append("<button value=\"Refresh\" action=\"bypass -h admin_info aggro\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\"><br><table width=\"280\"><tr><td><font color=\"LEVEL\">Attacker</font></td><td><font color=\"LEVEL\">Damage</font></td><td><font color=\"LEVEL\">Hate</font></td></tr>");
		
		for (AggroInfo ai : aggroList.values().stream().sorted(Comparator.comparing(AggroInfo::getHate, Comparator.reverseOrder())).limit(15).toList())
			StringUtil.append(sb, "<tr><td>", ai.getAttacker().getName(), "</td><td>", ai.getDamage(), "</td><td>", ai.getHate(), "</td></tr>");
		
		sb.append("</table><img src=\"L2UI.SquareGray\" width=280 height=1>");
		
		html.replace("%content%", sb.toString());
	}
	
	private static void sendDesireInfos(Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/default.htm");
		
		final Set<Desire> desires = npc.getAI().getDesireQueue();
		if (desires.isEmpty())
		{
			html.replace("%content%", "This NPC's Desires are empty.<br><button value=\"Refresh\" action=\"bypass -h admin_info desire\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\">");
			return;
		}
		
		final StringBuilder sb = new StringBuilder(500);
		sb.append("<button value=\"Refresh\" action=\"bypass -h admin_info desire\" width=65 height=19 back=\"L2UI_ch3.smallbutton2_over\" fore=\"L2UI_ch3.smallbutton2\"><br><table width=\"280\"><tr><td><font color=\"LEVEL\">Type</font></td><td><font color=\"LEVEL\">Weight</font></td></tr>");
		
		for (Desire desire : desires)
			StringUtil.append(sb, "<tr><td>", desire.getType(), "</td><td>", desire.getWeight(), "</td></tr>");
		
		sb.append("</table><img src=\"L2UI.SquareGray\" width=280 height=1>");
		
		html.replace("%content%", sb.toString());
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with <b>DROPS</b> or <b>SPOILS</b> informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 * @param page : The current page of categories we are checking.
	 * @param subPage : The current page of drops we are checking.
	 * @param isDrop : If true, we check drops only. If false, we check spoils.
	 */
	private static void sendDropInfos(Npc npc, NpcHtmlMessage html, int page, int subPage, boolean isDrop)
	{
		// Load static htm.
		html.setFile("data/html/admin/npcinfo/default.htm");
		
		// Generate data.
		final Pagination<DropCategory> list = new Pagination<>(npc.getTemplate().getDropData().stream(), page, PAGE_LIMIT_1, dc -> (isDrop) ? dc.getDropType() != DropType.SPOIL : dc.getDropType() == DropType.SPOIL);
		if (list.isEmpty())
		{
			html.replace("%content%", (isDrop) ? "This NPC doesn't hold any drops." : "This NPC doesn't hold any spoils.");
			return;
		}
		
		int row = 0;
		
		for (DropCategory category : list)
		{
			list.append("<br></center>Category: ", category.getDropType(), " - Rate: ", PERCENT.format(category.getChance()), "% - Iterations: x", PERCENT.format(category.getDropType().getDropRate(npc.isRaidBoss())), "<center>");
			
			final Pagination<DropData> droplist = new Pagination<>(category.stream().sorted(Comparator.comparing(DropData::chance).reversed()), subPage, 6);
			for (DropData drop : droplist)
			{
				final double chance = drop.chance();
				final String color = (chance > 80.) ? "90EE90" : (chance > 5.) ? "BDB76B" : "F08080";
				final String percent = PERCENT.format(chance);
				final String amount = (drop.minDrop() == drop.maxDrop()) ? drop.minDrop() + "" : drop.minDrop() + " - " + drop.maxDrop();
				final Item item = ItemData.getInstance().getTemplate(drop.itemId());
				
				String name = item.getName();
				if (name.startsWith("Recipe: "))
					name = "R: " + name.substring(8);
				
				name = StringUtil.trimAndDress(name, 45);
				
				droplist.append(((row % 2) == 0 ? "<table width=280 bgcolor=000000><tr>" : "<table width=280><tr>"));
				droplist.append("<td width=34 height=40><img src=icon.noimage width=32 height=32></td>");
				droplist.append("<td width=246>&nbsp;", name, "<br1>");
				droplist.append("<table width=240><tr><td width=80><font color=B09878>Rate:</font> <font color=", color, ">", percent, "%</font></td><td width=160><font color=B09878>Amount: </font>", amount, "</td></tr></table>");
				droplist.append("</td></tr></table><img src=L2UI.SquareGray width=280 height=1>");
				
				row++;
			}
			
			droplist.generateSpace(41);
			droplist.generatePages("bypass admin_info " + ((isDrop) ? "drop" : "spoil") + " " + page + " %page%");
			
			list.append(droplist.getContent());
		}
		
		list.generateSpace(30);
		list.generatePages("bypass admin_info " + ((isDrop) ? "drop" : "spoil") + " %page% 1");
		
		html.replace("%content%", list.getContent());
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with <b>GENERAL</b> informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 * @param index : The used index.
	 */
	public static void sendGeneralInfos(Npc npc, NpcHtmlMessage html, int index)
	{
		switch (index)
		{
			case 0:
			default:
				html.setFile("data/html/admin/npcinfo/general-0.htm");
				html.replace("%objectId%", npc.getObjectId());
				
				html.replace("%npcId%", npc.getTemplate().getNpcId());
				html.replace("%idTemplate%", npc.getTemplate().getIdTemplate());
				
				html.replace("%name%", npc.getTemplate().getName());
				html.replace("%title%", npc.getTemplate().getTitle());
				html.replace("%alias%", npc.getTemplate().getAlias());
				
				html.replace("%usingServerSideName%", npc.getTemplate().isUsingServerSideName());
				html.replace("%usingServerSideTitle%", npc.getTemplate().isUsingServerSideTitle());
				
				html.replace("%type%", npc.getClass().getSimpleName());
				html.replace("%level%", npc.getTemplate().getLevel());
				
				html.replace("%radius%", npc.getTemplate().getCollisionRadius());
				html.replace("%height%", npc.getTemplate().getCollisionHeight());
				
				html.replace("%hitTimeFactor%", npc.getTemplate().getHitTimeFactor());
				
				html.replace("%rHand%", npc.getTemplate().getRightHand());
				html.replace("%lHand%", npc.getTemplate().getLeftHand());
				break;
			
			case 1:
				html.setFile("data/html/admin/npcinfo/general-1.htm");
				html.replace("%exp%", npc.getTemplate().getRewardExp());
				html.replace("%sp%", npc.getTemplate().getRewardSp());
				
				html.replace("%baseAttackRange%", npc.getTemplate().getBaseAttackRange());
				html.replace("%baseDamageRange%", Arrays.toString(npc.getTemplate().getBaseDamageRange()));
				html.replace("%baseRandomDamage%", npc.getTemplate().getBaseRandomDamage());
				
				html.replace("%race%", npc.getTemplate().getRace().toString());
				
				html.replace("%clan%", (npc.getTemplate().getClans() == null) ? "none" : Arrays.toString(npc.getTemplate().getClans()));
				html.replace("%clanRange%", npc.getTemplate().getClanRange());
				html.replace("%ignoredIds%", (npc.getTemplate().getIgnoredIds() == null) ? "none" : Arrays.toString(npc.getTemplate().getIgnoredIds()));
				break;
			
			case 2:
				html.setFile("data/html/admin/npcinfo/general-2.htm");
				html.replace("%isUndying%", npc.getTemplate().isUndying());
				html.replace("%canBeAttacked%", npc.getTemplate().canBeAttacked());
				html.replace("%isNoSleepMode%", npc.getTemplate().isNoSleepMode());
				html.replace("%aggroRange%", npc.getTemplate().getAggroRange());
				html.replace("%canMove%", npc.getTemplate().canMove());
				html.replace("%isSeedable%", npc.getTemplate().isSeedable());
				
				html.replace("%residence%", (npc.getResidence() != null) ? npc.getResidence().getName() : "none");
				break;
		}
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	private static void sendScriptInfos(Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/script.htm");
		
		final StringBuilder sb = new StringBuilder(500);
		
		// Check scripts.
		if (npc.getTemplate().getEventQuests().isEmpty())
			sb.append("This NPC isn't affected by scripts.");
		else
		{
			EventHandler type = null;
			
			for (Entry<EventHandler, List<Quest>> entry : npc.getTemplate().getEventQuests().entrySet())
			{
				if (type != entry.getKey())
				{
					type = entry.getKey();
					StringUtil.append(sb, "<br><font color=\"LEVEL\">", type.name(), "</font><br1>");
				}
				
				for (Quest quest : entry.getValue())
					StringUtil.append(sb, quest.getName(), "<br1>");
			}
		}
		html.replace("%scripts%", sb.toString());
		
		// Reset the StringBuilder.
		sb.setLength(0);
		
		// Check scheduled tasks affecting this NPC.
		for (Quest quest : ScriptData.getInstance().getQuests())
		{
			final List<QuestTimer> qts = quest.getQuestTimers(npc);
			if (!qts.isEmpty())
			{
				StringUtil.append(sb, "<br><font color=\"LEVEL\">", quest.getName(), "</font><br1>");
				
				for (QuestTimer qt : qts)
					StringUtil.append(sb, qt.getName(), ((qt.getPlayer() == null) ? "" : (" affecting player ") + qt.getPlayer().getName()), "<br1>");
			}
		}
		html.replace("%tasks%", sb.toString());
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with <b>SPAWN</b> informations regarding a {@link Npc}.
	 * @param player : The {@link Player} used as reference.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	private static void sendSpawnInfos(Player player, Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/spawn.htm");
		
		html.replace("%loc%", npc.getX() + " " + npc.getY() + " " + npc.getZ());
		html.replace("%dist%", (int) player.distance3D(npc));
		html.replace("%corpse%", StringUtil.getTimeStamp(npc.getTemplate().getCorpseTime()));
		
		final ASpawn spawn = npc.getSpawn();
		if (spawn != null)
		{
			html.replace("%spawn%", spawn.toString());
			
			if (spawn instanceof MultiSpawn ms)
			{
				html.replace("%spawndesc%", "<a action=\"bypass -h admin_maker " + ms.getNpcMaker().getName() + "\">" + ms.getDescription() + "</a>");
				
				final int[][] coords = ms.getCoords();
				if (coords == null)
					html.replace("%spawninfo%", "loc: anywhere");
				else if (coords.length == 1)
					html.replace("%spawninfo%", "loc: fixed " + coords[0][0] + ", " + coords[0][1] + ", " + coords[0][2]);
				else
					html.replace("%spawninfo%", "loc: fixed random 1 of " + coords.length);
			}
			else
			{
				html.replace("%spawndesc%", spawn.getDescription());
				html.replace("%spawninfo%", "loc: " + spawn.getSpawnLocation());
			}
			
			html.replace("%loc2d%", (int) npc.distance2D(npc.getSpawnLocation()));
			html.replace("%loc3d%", (int) npc.distance3D(npc.getSpawnLocation()));
			html.replace("%resp%", StringUtil.getTimeStamp(spawn.getRespawnDelay()));
			html.replace("%rand_resp%", StringUtil.getTimeStamp(spawn.getRespawnRandom()));
			html.replace("%privates%", spawn.getPrivateData() != null && !spawn.getPrivateData().isEmpty());
		}
		else
		{
			html.replace("%spawn%", "<font color=FF0000>--</font>");
			html.replace("%spawndesc%", "<font color=FF0000>--</font>");
			html.replace("%spawninfo%", "<font color=FF0000>--</font>");
			html.replace("%loc2d%", "<font color=FF0000>--</font>");
			html.replace("%loc3d%", "<font color=FF0000>--</font>");
			html.replace("%resp%", "<font color=FF0000>--</font>");
			html.replace("%rand_resp%", "<font color=FF0000>--</font>");
			html.replace("%privates%", "<font color=FF0000>--</font>");
		}
		
		final StringBuilder sb = new StringBuilder(500);
		
		if (npc.isMaster())
			StringUtil.append(sb, "I'm a master, holding ", npc.getMinions().size(), " crappy minions.");
		else if (npc.hasMaster())
			StringUtil.append(sb, "I'm a crappy minion, my master <font color=LEVEL>", npc.getMaster().getName(), "</font> holds ", npc.getMinions().size(), " minions.");
		else
			StringUtil.append(sb, "I'm a regular NPC, as the most random BR/RU leaker.");
		
		html.replace("%minion%", sb.toString());
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with <b>STATS</b> informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	private static void sendStatsInfos(Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/stat.htm");
		
		html.replace("%hp%", (int) npc.getStatus().getHp());
		html.replace("%hpmax%", npc.getStatus().getMaxHp());
		html.replace("%mp%", (int) npc.getStatus().getMp());
		html.replace("%mpmax%", npc.getStatus().getMaxMp());
		html.replace("%patk%", npc.getStatus().getPAtk(null));
		html.replace("%matk%", npc.getStatus().getMAtk(null, null));
		html.replace("%pdef%", npc.getStatus().getPDef(null));
		html.replace("%mdef%", npc.getStatus().getMDef(null, null));
		html.replace("%accu%", npc.getStatus().getAccuracy());
		html.replace("%evas%", npc.getStatus().getEvasionRate(null));
		html.replace("%crit%", npc.getStatus().getCriticalHit(null, null));
		html.replace("%rspd%", (int) npc.getStatus().getMoveSpeed());
		html.replace("%aspd%", npc.getStatus().getPAtkSpd());
		html.replace("%cspd%", npc.getStatus().getMAtkSpd());
		html.replace("%str%", npc.getStatus().getSTR());
		html.replace("%dex%", npc.getStatus().getDEX());
		html.replace("%con%", npc.getStatus().getCON());
		html.replace("%int%", npc.getStatus().getINT());
		html.replace("%wit%", npc.getStatus().getWIT());
		html.replace("%men%", npc.getStatus().getMEN());
		html.replace("%ele_fire%", npc.getStatus().getDefenseElementValue(ElementType.FIRE));
		html.replace("%ele_water%", npc.getStatus().getDefenseElementValue(ElementType.WATER));
		html.replace("%ele_wind%", npc.getStatus().getDefenseElementValue(ElementType.WIND));
		html.replace("%ele_earth%", npc.getStatus().getDefenseElementValue(ElementType.EARTH));
		html.replace("%ele_holy%", npc.getStatus().getDefenseElementValue(ElementType.HOLY));
		html.replace("%ele_dark%", npc.getStatus().getDefenseElementValue(ElementType.DARK));
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with <b>SHOPS</b> informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	private static void sendShopInfos(Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/default.htm");
		
		final List<NpcBuyList> buyLists = BuyListManager.getInstance().getBuyListsByNpcId(npc.getNpcId());
		if (buyLists.isEmpty())
		{
			html.replace("%content%", "This NPC doesn't hold any buyList.");
			return;
		}
		
		final StringBuilder sb = new StringBuilder(500);
		
		if (npc.getCastle() != null)
			StringUtil.append(sb, "Tax rate: ", npc.getCastle().getCurrentTaxPercent(), "%<br>");
		
		StringUtil.append(sb, "<table width=\"100%\">");
		
		for (NpcBuyList buyList : buyLists)
			StringUtil.append(sb, "<tr><td><a action=\"bypass -h admin_buy ", buyList.getListId(), " 1\">Buylist id: ", buyList.getListId(), "</a></td></tr>");
		
		StringUtil.append(sb, "</table>");
		
		html.replace("%content%", sb.toString());
	}
	
	/**
	 * Feed a {@link NpcHtmlMessage} with <b>SKILLS</b> informations regarding a {@link Npc}.
	 * @param npc : The {@link Npc} used as reference.
	 * @param html : The {@link NpcHtmlMessage} used as reference.
	 */
	private static void sendSkillInfos(Npc npc, NpcHtmlMessage html)
	{
		html.setFile("data/html/admin/npcinfo/default.htm");
		
		if (npc.getTemplate().getSkills().isEmpty())
		{
			html.replace("%content%", "This NPC doesn't hold any skill.");
			return;
		}
		
		final StringBuilder sb = new StringBuilder(500);
		
		NpcSkillType type = null; // Used to see if we moved of type.
		
		// For any type of SkillType
		for (Entry<NpcSkillType, L2Skill> entry : npc.getTemplate().getSkills().entrySet())
		{
			if (type != entry.getKey())
			{
				type = entry.getKey();
				StringUtil.append(sb, "<br><font color=\"LEVEL\">", type.name(), "</font><br1>");
			}
			
			final L2Skill skill = entry.getValue();
			StringUtil.append(sb, ((skill.getSkillType() == SkillType.NOTDONE) ? ("<font color=\"777777\">" + skill.getName() + "</font>") : skill.getName()), " [", skill.getId(), "-", skill.getLevel(), "]<br1>");
		}
		
		html.replace("%content%", sb.toString());
	}
}