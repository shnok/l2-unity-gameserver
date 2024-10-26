package com.shnok.javaserver.gameserver.communitybbs.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

import com.shnok.javaserver.gameserver.communitybbs.CommunityBoard;
import com.shnok.javaserver.gameserver.communitybbs.model.Forum;
import com.shnok.javaserver.gameserver.communitybbs.model.Post;
import com.shnok.javaserver.gameserver.communitybbs.model.Topic;
import com.shnok.javaserver.gameserver.enums.bbs.ForumType;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class PostBBSManager extends BaseBBSManager
{
	protected PostBBSManager()
	{
	}
	
	@Override
	public void parseCmd(String command, Player player)
	{
		if (command.startsWith("_bbsposts;read;"))
		{
			final StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			
			final int forumId = Integer.parseInt(st.nextToken());
			final int topicId = Integer.parseInt(st.nextToken());
			
			final Forum forum = CommunityBoard.getInstance().getForumByID(forumId);
			if (forum == null)
			{
				separateAndSend("<html><body><br><br><center>This forum doesn't exist.</center></body></html>", player);
				return;
			}
			
			final Topic topic = forum.getTopicById(topicId);
			if (topic == null)
			{
				separateAndSend("<html><body><br><br><center>This topic doesn't exist.</center></body></html>", player);
				return;
			}
			
			if (forum.getType() == ForumType.MEMO)
				showMemoPost(topic, player);
			else
				separateAndSend("<html><body><br><br><center>The forum is off-limits.</center></body></html>", player);
		}
		else if (command.startsWith("_bbsposts;edit;"))
		{
			final StringTokenizer st = new StringTokenizer(command, ";");
			st.nextToken();
			st.nextToken();
			
			final int forumId = Integer.parseInt(st.nextToken());
			final int topicId = Integer.parseInt(st.nextToken());
			
			final Forum forum = CommunityBoard.getInstance().getForumByID(forumId);
			if (forum == null)
			{
				separateAndSend("<html><body><br><br><center>This forum doesn't exist.</center></body></html>", player);
				return;
			}
			
			final Topic topic = forum.getTopicById(topicId);
			if (topic == null)
			{
				separateAndSend("<html><body><br><br><center>This topic doesn't exist.</center></body></html>", player);
				return;
			}
			
			final Post post = topic.getPost(0);
			if (post == null)
			{
				separateAndSend("<html><body><br><br><center>This post doesn't exist.</center></body></html>", player);
				return;
			}
			
			final String html = "<html><body><br><br><table border=0 width=610><tr><td width=10></td><td width=600 align=left><a action=\"bypass _bbshome\">HOME</a>&nbsp;>&nbsp;<a action=\"bypass _bbsmemo\">Memo Form</a></td></tr></table><img src=\"L2UI.squareblank\" width=\"1\" height=\"10\"><center><table border=0 cellspacing=0 cellpadding=0><tr><td width=610><img src=\"sek.cbui355\" width=\"610\" height=\"1\"><br1><img src=\"sek.cbui355\" width=\"610\" height=\"1\"></td></tr></table><table fixwidth=610 border=0 cellspacing=0 cellpadding=0><tr><td><img src=\"l2ui.mini_logo\" width=5 height=20></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=1></td><td align=center FIXWIDTH=60 height=29>&$413;</td><td FIXWIDTH=540>" + topic.getName() + "</td><td><img src=\"l2ui.mini_logo\" width=5 height=1></td></tr></table><table fixwidth=610 border=0 cellspacing=0 cellpadding=0><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=1></td><td align=center FIXWIDTH=60 height=29 valign=top>&$427;</td><td align=center FIXWIDTH=540><MultiEdit var =\"Content\" width=535 height=313></td><td><img src=\"l2ui.mini_logo\" width=5 height=1></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr></table><table fixwidth=610 border=0 cellspacing=0 cellpadding=0><tr><td><img src=\"l2ui.mini_logo\" width=5 height=10></td></tr><tr><td><img src=\"l2ui.mini_logo\" width=5 height=1></td><td align=center FIXWIDTH=60 height=29>&nbsp;</td><td align=center FIXWIDTH=70><button value=\"&$140;\" action=\"Write Post " + forum.getId() + ";" + topic.getId() + ";0 _ Content Content Content\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\" ></td><td align=center FIXWIDTH=70><button value = \"&$141;\" action=\"bypass _bbsmemo\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"> </td><td align=center FIXWIDTH=400>&nbsp;</td><td><img src=\"l2ui.mini_logo\" width=5 height=1></td></tr></table></center></body></html>";
			send1001(html, player);
			send1002(player, post.getText(), topic.getName(), DateFormat.getInstance().format(new Date(topic.getDate())));
		}
		else
			super.parseCmd(command, player);
	}
	
	@Override
	public void parseWrite(String ar1, String ar2, String ar3, String ar4, String ar5, Player player)
	{
		final StringTokenizer st = new StringTokenizer(ar1, ";");
		
		final int forumId = Integer.parseInt(st.nextToken());
		final int topicId = Integer.parseInt(st.nextToken());
		final int pageId = Integer.parseInt(st.nextToken());
		
		final Forum forum = CommunityBoard.getInstance().getForumByID(forumId);
		if (forum == null)
		{
			separateAndSend("<html><body><br><br><center>The forum named '" + forumId + "' doesn't exist.</center></body></html>", player);
			return;
		}
		
		final Topic topic = forum.getTopic(topicId);
		if (topic == null)
		{
			separateAndSend("<html><body><br><br><center>The topic named '" + topicId + "' doesn't exist.</center></body></html>", player);
			return;
		}
		
		final Post post = topic.getPost(pageId);
		if (post == null)
		{
			separateAndSend("<html><body><br><br><center>The post named '" + pageId + "' doesn't exist.</center></body></html>", player);
			return;
		}
		
		post.updateText(pageId, ar4);
		
		parseCmd("_bbsposts;read;" + forum.getId() + ";" + topic.getId(), player);
	}
	
	private static void showMemoPost(Topic topic, Player player)
	{
		final Post post = topic.getPost(0);
		
		String mes = post.getText().replace(">", "&gt;");
		mes = mes.replace("<", "&lt;");
		mes = mes.replace("\n", "<br1>");
		
		final String html = "<html><body><br><br><table border=0 width=610><tr><td width=10></td><td width=600 align=left><a action=\"bypass _bbshome\">HOME</a>&nbsp;>&nbsp;<a action=\"bypass _bbsmemo\">Memo Form</a></td></tr></table><img src=\"L2UI.squareblank\" width=\"1\" height=\"10\"><center><table border=0 cellspacing=0 cellpadding=0 bgcolor=333333><tr><td height=10></td></tr><tr><td fixWIDTH=55 align=right valign=top>&$413; : &nbsp;</td><td fixWIDTH=380 valign=top>" + topic.getName() + "</td><td fixwidth=5></td><td fixwidth=50></td><td fixWIDTH=120></td></tr><tr><td height=10></td></tr><tr><td align=right><font color=\"AAAAAA\" >&$417; : &nbsp;</font></td><td><font color=\"AAAAAA\">" + topic.getOwnerName() + "</font></td><td></td><td><font color=\"AAAAAA\">&$418; :</font></td><td><font color=\"AAAAAA\">" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(post.getDate()) + "</font></td></tr><tr><td height=10></td></tr></table><br><table border=0 cellspacing=0 cellpadding=0><tr><td fixwidth=5></td><td FIXWIDTH=600 align=left>" + mes + "</td><td fixqqwidth=5></td></tr></table><br><img src=\"L2UI.squareblank\" width=\"1\" height=\"5\"><img src=\"L2UI.squaregray\" width=\"610\" height=\"1\"><img src=\"L2UI.squareblank\" width=\"1\" height=\"5\"><table border=0 cellspacing=0 cellpadding=0 FIXWIDTH=610><tr><td width=50><button value=\"&$422;\" action=\"bypass _bbsmemo\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\"></td><td width=560 align=right><table border=0 cellspacing=0><tr><td FIXWIDTH=300></td><td><button value = \"&$424;\" action=\"bypass _bbsposts;edit;" + topic.getForumId() + ";" + topic.getId() + ";0\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\" ></td>&nbsp;<td><button value = \"&$425;\" action=\"bypass _bbstopics;del;" + topic.getForumId() + ";" + topic.getId() + "\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\" ></td>&nbsp;<td><button value = \"&$421;\" action=\"bypass _bbstopics;crea;" + topic.getForumId() + "\" back=\"l2ui_ch3.smallbutton2_down\" width=65 height=20 fore=\"l2ui_ch3.smallbutton2\" ></td>&nbsp;</tr></table></td></tr></table><br><br><br></center></body></html>";
		separateAndSend(html, player);
	}
	
	public static PostBBSManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final PostBBSManager INSTANCE = new PostBBSManager();
	}
}