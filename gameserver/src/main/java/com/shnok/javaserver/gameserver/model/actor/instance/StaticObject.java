package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.ShowTownMap;
import com.shnok.javaserver.gameserver.network.serverpackets.StaticObjectInfo;

/**
 * A static object with low amount of interactions and no AI - such as throne, village town maps, etc.
 */
public class StaticObject extends WorldObject
{
	private int _staticObjectId;
	private int _type = -1; // 0 - map signs, 1 - throne , 2 - arena signs
	private boolean _isBusy; // True - if someone sitting on the throne
	private ShowTownMap _map;
	
	public StaticObject(int objectId)
	{
		super(objectId);
	}
	
	@Override
	public void onInteract(Player player)
	{
		if (getType() == 2)
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/signboard.htm");
			player.sendPacket(html);
		}
		else if (getType() == 0)
			player.sendPacket(getMap());
	}
	
	@Override
	public void onAction(Player player, boolean isCtrlPressed, boolean isShiftPressed)
	{
		if (player.getTarget() != this)
			player.setTarget(this);
		else
			player.getAI().tryToInteract(this, isCtrlPressed, isShiftPressed);
	}
	
	@Override
	public void sendInfo(Player player)
	{
		player.sendPacket(new StaticObjectInfo(this));
	}
	
	public int getStaticObjectId()
	{
		return _staticObjectId;
	}
	
	public void setStaticObjectId(int staticObjectId)
	{
		_staticObjectId = staticObjectId;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public void setType(int type)
	{
		_type = type;
	}
	
	public boolean isBusy()
	{
		return _isBusy;
	}
	
	public void setBusy(boolean isBusy)
	{
		_isBusy = isBusy;
	}
	
	public ShowTownMap getMap()
	{
		return _map;
	}
	
	public void setMap(String texture, int x, int y)
	{
		_map = new ShowTownMap("town_map." + texture, x, y);
	}
}