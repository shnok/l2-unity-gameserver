package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.List;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.Pagination;
import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.manager.FenceManager;
import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.xml.AdminData;
import com.shnok.javaserver.gameserver.data.xml.NpcData;
import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Fence;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.spawn.ASpawn;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class AdminSpawn implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_list_spawns",
		"admin_spawn",
		"admin_delete",
		"admin_unspawnall",
		"admin_respawnall",
		"admin_spawnfence",
		"admin_deletefence",
		"admin_listfence"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		if (command.startsWith("admin_list_spawns"))
		{
			final StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			
			int npcId = 0;
			
			final String entry = (st.hasMoreTokens()) ? st.nextToken() : null;
			final int page = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
			
			if (entry == null)
			{
				final Npc npc = getTarget(Npc.class, player, false);
				if (npc == null)
				{
					player.sendPacket(SystemMessageId.INVALID_TARGET);
					return;
				}
				
				npcId = npc.getNpcId();
			}
			else if (StringUtil.isDigit(entry))
				npcId = Integer.parseInt(entry);
			else
			{
				final NpcTemplate template = NpcData.getInstance().getTemplateByName(entry);
				if (template != null)
					npcId = template.getNpcId();
			}
			
			if (npcId == 0)
			{
				player.sendPacket(SystemMessageId.INVALID_TARGET);
				return;
			}
			
			int row = 0 + (8 * (page - 1));
			
			// Generate data.
			final Pagination<Npc> list = new Pagination<>(World.getInstance().getNpcs(npcId).stream(), page, PAGE_LIMIT_8);
			list.append("<html><body>");
			
			for (Npc npc : list)
			{
				list.append((row % 2) == 0 ? "<table width=280 height=41 bgcolor=000000><tr>" : "<table width=280 height=41><tr>");
				list.append("<td><a action=\"bypass -h admin_teleport ", npc.getX(), " ", npc.getY(), " ", npc.getZ(), "\">", row);
				
				final ASpawn spawn = npc.getSpawn();
				if (spawn == null)
					list.append(" - (", npc.getPosition(), ")", "</a>");
				else
					list.append(" - ", spawn, "</a><br1>", spawn.getDescription());
				
				list.append("</td></tr></table><img src=\"L2UI.SquareGray\" width=280 height=1>");
				
				row++;
			}
			
			list.generateSpace(42);
			list.generatePages("bypass admin_list_spawns " + npcId + " %page%");
			list.append("</body></html>");
			
			final NpcHtmlMessage html = new NpcHtmlMessage(0);
			html.setHtml(list.getContent());
			player.sendPacket(html);
		}
		else if (command.startsWith("admin_unspawnall"))
		{
			World.toAllOnlinePlayers(SystemMessage.getSystemMessage(SystemMessageId.NPC_SERVER_NOT_OPERATING));
			SpawnManager.getInstance().despawn();
			World.getInstance().deleteVisibleNpcSpawns();
			AdminData.getInstance().broadcastMessageToGMs("NPCs' unspawn is now complete.");
		}
		else if (command.startsWith("admin_respawnall"))
		{
			// make sure all spawns are deleted
			SpawnManager.getInstance().despawn();
			World.getInstance().deleteVisibleNpcSpawns();
			
			// now respawn all
			NpcData.getInstance().reload();
			SpawnManager.getInstance().reload();
			AdminData.getInstance().broadcastMessageToGMs("NPCs' respawn is now complete.");
		}
		else if (command.startsWith("admin_spawnfence"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			try
			{
				st.nextToken();
				int type = Integer.parseInt(st.nextToken());
				int sizeX = (Integer.parseInt(st.nextToken()) / 100) * 100;
				int sizeY = (Integer.parseInt(st.nextToken()) / 100) * 100;
				int height = 1;
				if (st.hasMoreTokens())
					height = Math.min(Integer.parseInt(st.nextToken()), 3);
				
				FenceManager.getInstance().addFence(player.getX(), player.getY(), player.getZ(), type, sizeX, sizeY, height);
				
				listFences(player);
			}
			catch (Exception e)
			{
				player.sendMessage("Usage: //spawnfence <type> <width> <length> [height]");
			}
		}
		else if (command.startsWith("admin_deletefence"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			st.nextToken();
			try
			{
				final WorldObject worldObject = World.getInstance().getObject(Integer.parseInt(st.nextToken()));
				if (worldObject instanceof Fence fence)
				{
					FenceManager.getInstance().removeFence(fence);
					
					if (st.hasMoreTokens())
						listFences(player);
				}
				else
					player.sendPacket(SystemMessageId.INVALID_TARGET);
			}
			catch (Exception e)
			{
				player.sendMessage("Usage: //deletefence <objectId>");
			}
		}
		else if (command.startsWith("admin_listfence"))
			listFences(player);
		else if (command.startsWith("admin_spawn"))
		{
			StringTokenizer st = new StringTokenizer(command, " ");
			try
			{
				final String cmd = st.nextToken();
				final String idOrName = st.nextToken();
				final int respawnTime = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 60;
				
				final WorldObject targetWorldObject = getTarget(WorldObject.class, player, true);
				
				NpcTemplate template;
				
				// First parameter was an ID number
				if (idOrName.matches("[0-9]*"))
					template = NpcData.getInstance().getTemplate(Integer.parseInt(idOrName));
				// First parameter wasn't just numbers, so go by name not ID
				else
					template = NpcData.getInstance().getTemplateByName(idOrName.replace('_', ' '));
				
				try
				{
					final Spawn spawn = new Spawn(template);
					spawn.setLoc(targetWorldObject.getPosition());
					spawn.setRespawnDelay(respawnTime);
					spawn.doSpawn(false);
					
					player.sendMessage("You spawned " + template.getName() + ". - Cmd: " + cmd);
					
				}
				catch (Exception e)
				{
					player.sendPacket(SystemMessageId.APPLICANT_INFORMATION_INCORRECT);
				}
			}
			catch (Exception e)
			{
				sendFile(player, "spawns.htm");
			}
		}
		else if (command.startsWith("admin_delete"))
		{
			// Target must be a Npc.
			final WorldObject targetWorldObject = player.getTarget();
			if (!(targetWorldObject instanceof Npc targetNpc))
			{
				player.sendPacket(SystemMessageId.INVALID_TARGET);
				return;
			}
			
			// Npc ASpawn must be Spawn type.
			final ASpawn spawn = targetNpc.getSpawn();
			if (!(spawn instanceof Spawn))
			{
				player.sendPacket(SystemMessageId.INVALID_TARGET);
				return;
			}
			
			// Delete the Npc.
			targetNpc.deleteMe();
			
			// Delete the Spawn entry.
			SpawnManager.getInstance().deleteSpawn((Spawn) spawn);
			
			// Send Player log.
			player.sendMessage("You deleted " + targetNpc.getName() + ".");
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private static void listFences(Player player)
	{
		final List<Fence> fences = FenceManager.getInstance().getFences();
		final StringBuilder sb = new StringBuilder();
		
		sb.append("<html><body>Total Fences: " + fences.size() + "<br><br>");
		for (Fence fence : fences)
			sb.append("<a action=\"bypass -h admin_deletefence " + fence.getObjectId() + " 1\">Fence: " + fence.getObjectId() + " [" + fence.getX() + " " + fence.getY() + " " + fence.getZ() + "]</a><br>");
		sb.append("</body></html>");
		
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		html.setHtml(sb.toString());
		player.sendPacket(html);
	}
}