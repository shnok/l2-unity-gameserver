package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.manager.HeroManager;
import net.sf.l2j.gameserver.data.xml.MultisellData;
import net.sf.l2j.gameserver.enums.OlympiadType;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.olympiad.Olympiad;
import net.sf.l2j.gameserver.model.olympiad.OlympiadGameManager;
import net.sf.l2j.gameserver.model.olympiad.OlympiadGameTask;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExHeroList;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class OlympiadManagerNpc extends Folk
{
	private static final List<OlympiadManagerNpc> _managers = new CopyOnWriteArrayList<>();
	
	private static final int GATE_PASS = 6651;
	
	public OlympiadManagerNpc(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	public static List<OlympiadManagerNpc> getInstances()
	{
		return _managers;
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		// Only used by Olympiad managers. Monument of Heroes don't use "Chat" bypass.
		String filename = "noble";
		
		if (val > 0)
			filename = "noble_" + val;
		
		return filename + ".htm";
	}
	
	@Override
	public void showChatWindow(Player player, int val)
	{
		int npcId = getTemplate().getNpcId();
		String filename = getHtmlPath(npcId, val);
		
		switch (npcId)
		{
			case 31688: // Olympiad managers
				if (player.isNoble() && val == 0)
					filename = "noble_main.htm";
				break;
			
			// Monuments of Heroes
			case 31690, 31769, 31770, 31771, 31772:
				if (player.isHero() || HeroManager.getInstance().isInactiveHero(player.getObjectId()))
					filename = "hero_main.htm";
				else
					filename = "hero_main2.htm";
				break;
		}
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/olympiad/" + filename);
		
		// Hidden option for players who are in inactive mode.
		if (filename.equals("hero_main.htm"))
		{
			String hiddenText = "";
			if (HeroManager.getInstance().isInactiveHero(player.getObjectId()))
				hiddenText = "<a action=\"bypass -h npc_%objectId%_Olympiad 5\">\"I want to be a Hero.\"</a><br>";
			
			html.replace("%hero%", hiddenText);
		}
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
		
		// Send a Server->Client ActionFailed to the Player in order to avoid that the client wait another packet
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		if (command.startsWith("OlympiadNoble"))
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			if (player.isCursedWeaponEquipped())
			{
				html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_cant_cw.htm");
				player.sendPacket(html);
				return;
			}
			
			if (player.getClassIndex() != 0)
			{
				html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_cant_sub.htm");
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
				return;
			}
			
			if (!player.isNoble() || (player.getClassId().getLevel() < 3))
			{
				html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_cant_thirdclass.htm");
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
				return;
			}
			
			int val = Integer.parseInt(command.substring(14));
			switch (val)
			{
				case 1: // Unregister
					OlympiadManager.getInstance().unRegisterNoble(player);
					break;
				
				case 2: // Show waiting list
					final int nonClassed = OlympiadManager.getInstance().getNonClassBasedParticipants().size();
					final int classed = OlympiadManager.getInstance().getClassBasedParticipants().size();
					
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_registered.htm");
					html.replace("%listClassed%", classed);
					html.replace("%listNonClassed%", nonClassed);
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					break;
				
				case 3: // There are %points% Grand Olympiad points granted for this event.
					int points = Olympiad.getInstance().getNoblePoints(player.getObjectId());
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_points1.htm");
					html.replace("%points%", points);
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					break;
				
				case 4: // register non classed based
					OlympiadManager.getInstance().registerNoble(this, player, OlympiadType.NON_CLASSED);
					break;
				
				case 5: // register classed based
					OlympiadManager.getInstance().registerNoble(this, player, OlympiadType.CLASSED);
					break;
				
				case 6: // request tokens reward
					points = Olympiad.getInstance().getNoblePoints(player.getObjectId());
					if (points >= 50 || player.isHero() || HeroManager.getInstance().isInactiveHero(player.getObjectId()))
						html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_settle.htm");
					else
						html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_nopoints2.htm");
					
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					break;
				
				case 7: // Rewards
					MultisellData.getInstance().separateAndSend("102", player, this, false);
					break;
				
				case 10: // Give tokens to player
					points = Olympiad.getInstance().getNoblessePasses(player);
					if (points > 0)
						player.addItem(GATE_PASS, points, true);
					break;
				
				default:
					break;
			}
		}
		else if (command.startsWith("Olympiad"))
		{
			int val = Integer.parseInt(command.substring(9, 10));
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			switch (val)
			{
				case 2: // Show rank for a specific class, example >> Olympiad 1_88
					int classId = Integer.parseInt(command.substring(11));
					if (classId >= 88 && classId <= 118)
					{
						List<String> names = Olympiad.getInstance().getClassLeaderBoard(classId);
						html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "noble_ranking.htm");
						
						int index = 1;
						for (String name : names)
						{
							html.replace("%place" + index + "%", index);
							html.replace("%rank" + index + "%", name);
							
							index++;
							if (index > 10)
								break;
						}
						
						for (; index <= 10; index++)
						{
							html.replace("%place" + index + "%", "");
							html.replace("%rank" + index + "%", "");
						}
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					break;
				
				case 3: // Spectator overview
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "olympiad_observe_list.htm");
					
					int i = 0;
					
					final StringBuilder sb = new StringBuilder(2000);
					for (OlympiadGameTask task : OlympiadGameManager.getInstance().getOlympiadTasks())
					{
						StringUtil.append(sb, "<a action=\"bypass arenachange ", i, "\">Arena ", ++i, "&nbsp;");
						
						if (task.isGameStarted())
						{
							if (task.isInTimerTime())
								StringUtil.append(sb, "(&$907;)"); // Counting In Progress
							else if (task.isBattleStarted())
								StringUtil.append(sb, "(&$829;)"); // In Progress
							else
								StringUtil.append(sb, "(&$908;)"); // Terminate
								
							StringUtil.append(sb, "&nbsp;", task.getGame().getPlayerNames()[0], "&nbsp; : &nbsp;", task.getGame().getPlayerNames()[1]);
						}
						else
							StringUtil.append(sb, "(&$906;)</td><td>&nbsp;"); // Initial State
							
						StringUtil.append(sb, "</a><br>");
					}
					html.replace("%list%", sb.toString());
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					break;
				
				case 4: // Send heroes list.
					player.sendPacket(new ExHeroList());
					break;
				
				case 5: // Hero pending state.
					if (HeroManager.getInstance().isInactiveHero(player.getObjectId()))
					{
						html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "hero_confirm.htm");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					break;
				
				case 6: // Hero confirm action.
					if (HeroManager.getInstance().isInactiveHero(player.getObjectId()))
					{
						if (player.isSubClassActive() || player.getStatus().getLevel() < 76)
						{
							player.sendMessage("You may only become an hero on a main class whose level is 75 or more.");
							return;
						}
						
						HeroManager.getInstance().activateHero(player);
					}
					break;
				
				case 7: // Main panel
					html.setFile(Olympiad.OLYMPIAD_HTML_PATH + "hero_main.htm");
					
					String hiddenText = "";
					if (HeroManager.getInstance().isInactiveHero(player.getObjectId()))
						hiddenText = "<a action=\"bypass -h npc_%objectId%_Olympiad 5\">\"I want to be a Hero.\"</a><br>";
					
					html.replace("%hero%", hiddenText);
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					break;
				
				default:
					break;
			}
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		if (getNpcId() == 31688)
			_managers.add(this);
	}
	
	@Override
	public void onDecay()
	{
		_managers.remove(this);
		super.onDecay();
	}
}