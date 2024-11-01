package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.handler.IAdminCommandHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Sequence;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SpecialCamera;

public class AdminMovieMaker implements IAdminCommandHandler
{
	private static final Map<Integer, Sequence> _sequences = new TreeMap<>();
	
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_movie",
		"admin_sequence"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		if (command.startsWith("admin_movie"))
		{
			try
			{
				final String param = st.nextToken();
				switch (param)
				{
					case "play":
						playMovie(0, player);
						break;
					
					case "broadcast":
						playMovie(1, player);
						break;
				}
			}
			catch (Exception e)
			{
				mainHtm(player);
			}
		}
		else if (command.startsWith("admin_sequence"))
		{
			if (!st.hasMoreTokens())
			{
				mainHtm(player);
				return;
			}
			
			final String param = st.nextToken();
			switch (param)
			{
				case "add":
					if (!st.hasMoreTokens())
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(0);
						html.setFile("data/html/admin/movie/add_sequence.htm");
						player.sendPacket(html);
						return;
					}
					
					try
					{
						final int sequenceId = Integer.parseInt(st.nextToken());
						if (sequenceId < 0)
						{
							player.sendMessage("The sequence id is invalid.");
							mainHtm(player);
							return;
						}
						
						// Only one param, we send edit panel with restored parameters for that given Sequence.
						if (!st.hasMoreTokens())
						{
							final Sequence sequence = _sequences.get(sequenceId);
							if (sequence == null)
							{
								player.sendMessage("The sequence couldn't be updated.");
								mainHtm(player);
								return;
							}
							
							final NpcHtmlMessage html = new NpcHtmlMessage(0);
							html.setFile("data/html/admin/movie/edit_sequence.htm");
							html.replace("%sId%", sequence.sequenceId());
							html.replace("%sDist%", sequence.dist());
							html.replace("%sYaw%", sequence.yaw());
							html.replace("%sPitch%", sequence.pitch());
							html.replace("%sTime%", sequence.time());
							html.replace("%sDuration%", sequence.duration());
							html.replace("%sTurn%", sequence.turn());
							html.replace("%sRise%", sequence.rise());
							html.replace("%sWidescreen%", sequence.widescreen());
							player.sendPacket(html);
						}
						else
						{
							if (st.countTokens() != 8)
							{
								player.sendMessage("Some arguments are missing.");
								mainHtm(player);
								return;
							}
							
							final WorldObject targetWorldObject = getTarget(WorldObject.class, player, true);
							final int dist = Integer.parseInt(st.nextToken());
							final int yaw = Integer.parseInt(st.nextToken());
							final int pitch = Integer.parseInt(st.nextToken());
							final int time = Integer.parseInt(st.nextToken());
							final int duration = Integer.parseInt(st.nextToken());
							final int turn = Integer.parseInt(st.nextToken());
							final int rise = Integer.parseInt(st.nextToken());
							final int widescreen = Integer.parseInt(st.nextToken());
							
							_sequences.computeIfAbsent(sequenceId, s -> new Sequence(sequenceId, targetWorldObject.getObjectId(), dist, yaw, pitch, time, duration, turn, rise, widescreen));
							
							mainHtm(player);
						}
					}
					catch (Exception e)
					{
						mainHtm(player);
					}
					break;
				
				case "delete":
					try
					{
						if (_sequences.remove(Integer.parseInt(st.nextToken())) == null)
							player.sendMessage("The sequence id doesn't exist.");
					}
					catch (Exception e)
					{
						player.sendMessage("You entered an invalid sequence id.");
					}
					mainHtm(player);
					break;
				
				case "play":
					try
					{
						final Sequence sequence = _sequences.get(Integer.parseInt(st.nextToken()));
						if (sequence == null)
						{
							player.sendMessage("The sequence id doesn't exist.");
							mainHtm(player);
							return;
						}
						
						player.sendPacket(new SpecialCamera(sequence));
					}
					catch (Exception e)
					{
						player.sendMessage("You entered an invalid sequence id.");
						mainHtm(player);
					}
					break;
				
				case "broadcast":
					try
					{
						final Sequence sequence = _sequences.get(Integer.parseInt(st.nextToken()));
						if (sequence == null)
						{
							player.sendMessage("The sequence id doesn't exist.");
							mainHtm(player);
							return;
						}
						
						player.broadcastPacket(new SpecialCamera(sequence));
					}
					catch (Exception e)
					{
						player.sendMessage("You entered an invalid sequence id.");
						mainHtm(player);
					}
					break;
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private static void mainHtm(Player player)
	{
		final NpcHtmlMessage html = new NpcHtmlMessage(0);
		
		if (_sequences.isEmpty())
			html.setFile("data/html/admin/movie/main_empty.htm");
		else
		{
			final StringBuilder sb = new StringBuilder();
			for (Sequence sequence : _sequences.values())
				StringUtil.append(sb, "<tr><td>", sequence.sequenceId(), "</td><td>", sequence.dist(), "</td><td>", sequence.yaw(), "</td><td>", sequence.pitch(), "</td><td>", sequence.time(), "</td><td>", sequence.duration(), "</td><td>", sequence.turn(), "</td><td>", sequence.rise(), "</td><td>", sequence.widescreen(), "</td></tr>");
			
			html.setFile("data/html/admin/movie/main_notempty.htm");
			html.replace("%sequences%", sb.toString());
		}
		player.sendPacket(html);
	}
	
	private static void playMovie(int broadcast, Player player)
	{
		if (_sequences.isEmpty())
		{
			player.sendMessage("There is nothing to play.");
			mainHtm(player);
			return;
		}
		
		long timer = 500;
		for (Sequence sequence : _sequences.values())
		{
			ThreadPool.schedule(() ->
			{
				if (broadcast == 1)
					player.broadcastPacket(new SpecialCamera(sequence));
				else
					player.sendPacket(new SpecialCamera(sequence));
			}, timer);
			
			// Cumulate duration with previous sequences duration, since all ThreadPool are running in same time.
			timer += sequence.duration() - 100;
		}
	}
}