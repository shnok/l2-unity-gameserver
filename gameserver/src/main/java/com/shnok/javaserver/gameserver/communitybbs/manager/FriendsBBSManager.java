package com.shnok.javaserver.gameserver.communitybbs.manager;

import java.util.Set;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.lang.StringUtil;

import com.shnok.javaserver.gameserver.data.cache.HtmCache;
import com.shnok.javaserver.gameserver.data.manager.RelationManager;
import com.shnok.javaserver.gameserver.data.sql.PlayerInfoTable;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.FriendList;

public class FriendsBBSManager extends BaseBBSManager
{
	private static final String FRIENDLIST_DELETE_BUTTON = "<br>\n<table><tr><td width=10></td><td>Are you sure you want to delete all friends from your Friends List?</td><td width=20></td><td><button value=\"OK\" action=\"bypass _friend;delall\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"></td></tr></table>";
	private static final String BLOCKLIST_DELETE_BUTTON = "<br>\n<table><tr><td width=10></td><td>Are you sure you want to delete all players from your Block List?</td><td width=20></td><td><button value=\"OK\" action=\"bypass _block;delall\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"></td></tr></table>";
	
	protected FriendsBBSManager()
	{
	}
	
	@Override
	public void parseCmd(String command, Player player)
	{
		if (command.startsWith("_friendlist"))
			showFriendsList(player, false);
		else if (command.startsWith("_blocklist"))
			showBlockList(player, false);
		else if (command.startsWith("_friend"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			String action = st.nextToken();
			
			if (action.equals("select"))
			{
				player.selectFriend((st.hasMoreTokens()) ? Integer.valueOf(st.nextToken()) : 0);
				showFriendsList(player, false);
			}
			else if (action.equals("deselect"))
			{
				player.deselectFriend((st.hasMoreTokens()) ? Integer.valueOf(st.nextToken()) : 0);
				showFriendsList(player, false);
			}
			else if (action.equals("delall"))
			{
				RelationManager.getInstance().removeAllFromFriendList(player);
				player.getSelectedFriendList().clear();
				
				showFriendsList(player, false);
				
				player.sendMessage("You have cleared your friends list.");
				player.sendPacket(new FriendList(player));
			}
			else if (action.equals("delconfirm"))
				showFriendsList(player, true);
			else if (action.equals("del"))
			{
				for (int friendId : player.getSelectedFriendList())
				{
					// Update friend's friendlist.
					final Player friend = World.getInstance().getPlayer(friendId);
					if (friend != null)
					{
						RelationManager.getInstance().removeFromFriendList(friend, player.getObjectId());
						friend.sendPacket(new FriendList(friend));
					}
					
					// The friend is deleted from your friendlist.
					RelationManager.getInstance().removeFromFriendList(player, friendId);
				}
				
				player.getSelectedFriendList().clear();
				showFriendsList(player, false);
				
				player.sendPacket(new FriendList(player)); // update friendList *heavy method*
			}
			else if (action.equals("mail"))
			{
				if (!player.getSelectedFriendList().isEmpty())
					showMailWrite(player);
			}
		}
		else if (command.startsWith("_block"))
		{
			StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			String action = st.nextToken();
			
			boolean delMsg = false;
			
			if (action.equals("select"))
				player.selectBlock((st.hasMoreTokens()) ? Integer.valueOf(st.nextToken()) : 0);
			else if (action.equals("deselect"))
				player.deselectBlock((st.hasMoreTokens()) ? Integer.valueOf(st.nextToken()) : 0);
			else if (action.equals("delall"))
			{
				RelationManager.getInstance().removeAllFromBlockList(player);
				player.getSelectedBlocksList().clear();
			}
			else if (action.equals("delconfirm"))
				delMsg = true;
			else if (action.equals("del"))
			{
				for (int blockId : player.getSelectedBlocksList())
					RelationManager.getInstance().removeFromBlockList(player, blockId);
				
				player.getSelectedBlocksList().clear();
			}
			showBlockList(player, delMsg);
		}
		else
			super.parseCmd(command, player);
	}
	
	@Override
	public void parseWrite(String ar1, String ar2, String ar3, String ar4, String ar5, Player player)
	{
		if (ar1.equalsIgnoreCase("mail"))
		{
			MailBBSManager.getInstance().sendMail(ar2, ar4, ar5, player);
			showFriendsList(player, false);
		}
		else
			super.parseWrite(ar1, ar2, ar3, ar4, ar5, player);
	}
	
	@Override
	protected String getFolder()
	{
		return "friend/";
	}
	
	private static void showFriendsList(Player player, boolean delMsg)
	{
		String content = HtmCache.getInstance().getHtm(CB_PATH + "friend/friend-list.htm");
		if (content == null)
			return;
		
		// Retrieve player's friendlist and selected
		final Set<Integer> list = RelationManager.getInstance().getFriendList(player.getObjectId());
		final Set<Integer> selectedList = player.getSelectedFriendList();
		
		final StringBuilder sb = new StringBuilder();
		
		// Friendlist
		for (Integer id : list)
		{
			if (selectedList.contains(id))
				continue;
			
			final String friendName = PlayerInfoTable.getInstance().getPlayerName(id);
			if (friendName == null)
				continue;
			
			final Player friend = World.getInstance().getPlayer(id);
			StringUtil.append(sb, "<a action=\"bypass _friend;select;", id, "\">[Select]</a>&nbsp;", friendName, " ", ((friend != null && friend.isOnline()) ? "(on)" : "(off)"), "<br1>");
		}
		content = content.replace("%friendslist%", sb.toString());
		
		// Cleanup sb.
		sb.setLength(0);
		
		// Selected friendlist
		for (Integer id : selectedList)
		{
			final String friendName = PlayerInfoTable.getInstance().getPlayerName(id);
			if (friendName == null)
				continue;
			
			final Player friend = World.getInstance().getPlayer(id);
			StringUtil.append(sb, "<a action=\"bypass _friend;deselect;", id, "\">[Deselect]</a>&nbsp;", friendName, " ", ((friend != null && friend.isOnline()) ? "(on)" : "(off)"), "<br1>");
		}
		content = content.replace("%selectedFriendsList%", sb.toString());
		
		// Delete button.
		content = content.replace("%deleteMSG%", (delMsg) ? FRIENDLIST_DELETE_BUTTON : "");
		
		separateAndSend(content, player);
	}
	
	private static void showBlockList(Player player, boolean delMsg)
	{
		String content = HtmCache.getInstance().getHtm(CB_PATH + "friend/friend-blocklist.htm");
		if (content == null)
			return;
		
		// Retrieve player's blocklist and selected
		final Set<Integer> list = RelationManager.getInstance().getBlockList(player.getObjectId());
		final Set<Integer> selectedList = player.getSelectedBlocksList();
		
		final StringBuilder sb = new StringBuilder();
		
		// Blocklist
		for (Integer id : list)
		{
			if (selectedList.contains(id))
				continue;
			
			final String blockName = PlayerInfoTable.getInstance().getPlayerName(id);
			if (blockName == null)
				continue;
			
			final Player block = World.getInstance().getPlayer(id);
			StringUtil.append(sb, "<a action=\"bypass _block;select;", id, "\">[Select]</a>&nbsp;", blockName, " ", ((block != null && block.isOnline()) ? "(on)" : "(off)"), "<br1>");
		}
		content = content.replace("%blocklist%", sb.toString());
		
		// Cleanup sb.
		sb.setLength(0);
		
		// Selected Blocklist
		for (Integer id : selectedList)
		{
			final String blockName = PlayerInfoTable.getInstance().getPlayerName(id);
			if (blockName == null)
				continue;
			
			final Player block = World.getInstance().getPlayer(id);
			StringUtil.append(sb, "<a action=\"bypass _block;deselect;", id, "\">[Deselect]</a>&nbsp;", blockName, " ", ((block != null && block.isOnline()) ? "(on)" : "(off)"), "<br1>");
		}
		content = content.replace("%selectedBlocksList%", sb.toString());
		
		// Delete button.
		content = content.replace("%deleteMSG%", (delMsg) ? BLOCKLIST_DELETE_BUTTON : "");
		
		separateAndSend(content, player);
	}
	
	public static final void showMailWrite(Player player)
	{
		String content = HtmCache.getInstance().getHtm(CB_PATH + "friend/friend-mail.htm");
		if (content == null)
			return;
		
		final StringBuilder sb = new StringBuilder();
		for (int id : player.getSelectedFriendList())
		{
			String friendName = PlayerInfoTable.getInstance().getPlayerName(id);
			if (friendName == null)
				continue;
			
			if (sb.length() > 0)
				sb.append(";");
			
			sb.append(friendName);
		}
		
		content = content.replace("%list%", sb.toString());
		
		separateAndSend(content, player);
	}
	
	public static FriendsBBSManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final FriendsBBSManager INSTANCE = new FriendsBBSManager();
	}
}