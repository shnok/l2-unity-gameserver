package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.data.xml.ArmorSetData;
import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.ArmorSet;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;

public class AdminItem implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_give",
		"admin_item"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final Player targetPlayer = getTargetPlayer(player, true);
		
		final StringTokenizer st = new StringTokenizer(command);
		command = st.nextToken();
		
		if (command.startsWith("admin_give"))
		{
			if (!st.hasMoreTokens())
			{
				player.sendMessage("Usage: //give itemId count");
				return;
			}
			
			final String param = st.nextToken();
			if (!StringUtil.isDigit(param))
			{
				player.sendMessage("Usage: //give itemId count");
				return;
			}
			
			final int id = Integer.parseInt(param);
			final int count = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
			
			createItem(player, targetPlayer, id, count, 0);
		}
		else if (command.startsWith("admin_item"))
		{
			if (!st.hasMoreTokens())
			{
				sendFile(player, "itemcreation.htm");
				return;
			}
			
			final String param = st.nextToken();
			if (StringUtil.isDigit(param))
			{
				final int id = Integer.parseInt(param);
				final int count = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
				final int radius = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 0;
				
				createItem(player, targetPlayer, id, count, radius);
				
				sendFile(player, "itemcreation.htm");
			}
			else
			{
				switch (param)
				{
					case "coin":
						try
						{
							final int id = getCoinId(st.nextToken());
							if (id <= 0)
							{
								player.sendMessage("Usage: //item coin name [amount] [radius]");
								return;
							}
							
							final int count = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 1;
							final int radius = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 0;
							
							createItem(player, targetPlayer, id, count, radius);
						}
						catch (Exception e)
						{
							player.sendMessage("Usage: //item coin name [amount] [radius]");
						}
						sendFile(player, "itemcreation.htm");
						break;
					
					case "set":
						// More tokens means you try to use the command directly with a chestId.
						if (st.hasMoreTokens())
						{
							try
							{
								final ArmorSet armorSet = ArmorSetData.getInstance().getSet(Integer.parseInt(st.nextToken()));
								if (armorSet == null)
								{
									player.sendMessage("This chest has no set.");
									return;
								}
								
								for (int itemId : armorSet.getSetItemsId())
								{
									if (itemId > 0)
										targetPlayer.getInventory().addItem(itemId, 1);
								}
								
								if (armorSet.getShield() > 0)
									targetPlayer.getInventory().addItem(armorSet.getShield(), 1);
								
								if (player != targetPlayer)
									player.sendMessage("You have spawned " + armorSet.toString() + " in " + targetPlayer.getName() + "'s inventory.");
							}
							catch (Exception e)
							{
								player.sendMessage("Usage: //item set [chestId]");
							}
						}
						
						// Regular case (first HTM with all possible sets).
						int i = 0;
						
						final StringBuilder sb = new StringBuilder();
						for (ArmorSet armorSet : ArmorSetData.getInstance().getSets())
						{
							final boolean isNextLine = i % 2 == 0;
							if (isNextLine)
								sb.append("<tr>");
							
							sb.append("<td><a action=\"bypass -h admin_item set " + armorSet.getSetItemsId()[0] + "\">" + armorSet.toString() + "</a></td>");
							
							if (isNextLine)
								sb.append("</tr>");
							
							i++;
						}
						
						final NpcHtmlMessage html = new NpcHtmlMessage(0);
						html.setFile("data/html/admin/itemsets.htm");
						html.replace("%sets%", sb.toString());
						player.sendPacket(html);
						break;
				}
			}
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private static void createItem(Player player, Player targetPlayer, int id, int num, int radius)
	{
		final Item item = ItemData.getInstance().getTemplate(id);
		if (item == null)
		{
			player.sendMessage("This item doesn't exist.");
			return;
		}
		
		if (!targetPlayer.getInventory().validateCapacityByItemId(id, num))
		{
			player.sendMessage("Your target's inventory is full.");
			return;
		}
		
		if (radius > 0)
		{
			player.forEachKnownTypeInRadius(Player.class, radius, p -> p.addItem(id, num, true));
			player.sendMessage("Surrounding players were rewarded with " + num + " " + item.getName() + " in a " + radius + " radius.");
		}
		else
		{
			targetPlayer.addItem(id, num, true);
			
			if (player != targetPlayer)
				player.sendMessage("You have spawned " + num + " " + item.getName() + " (" + id + ") in " + targetPlayer.getName() + "'s inventory.");
		}
	}
	
	private static int getCoinId(String name)
	{
		if (name.equalsIgnoreCase("adena"))
			return 57;
		
		if (name.equalsIgnoreCase("ancient"))
			return 5575;
		
		if (name.equalsIgnoreCase("festival"))
			return 6673;
		
		return 0;
	}
}