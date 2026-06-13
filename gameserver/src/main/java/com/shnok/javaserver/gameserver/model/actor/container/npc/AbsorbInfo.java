package com.shnok.javaserver.gameserver.model.actor.container.npc;

/**
 * This class contains all infos of the Attackable against the absorber Creature.
 * <ul>
 * <li>_absorbedHP : The amount of HP at the moment attacker used the item.</li>
 * <li>_itemObjectId : The item id of the Soul Crystal used.</li>
 * </ul>
 */
public final class AbsorbInfo
{
	private boolean _registered;
	private int _itemId;
	private int _absorbedHpPercent;
	
	public AbsorbInfo(int itemId)
	{
		_itemId = itemId;
	}
	
	public boolean isRegistered()
	{
		return _registered;
	}
	
	public void setRegistered(boolean state)
	{
		_registered = state;
	}
	
	public int getItemId()
	{
		return _itemId;
	}
	
	public void setItemId(int itemId)
	{
		_itemId = itemId;
	}
	
	public void setAbsorbedHpPercent(int percent)
	{
		_absorbedHpPercent = percent;
	}
	
	public boolean isValid(int itemId)
	{
		return _itemId == itemId && _absorbedHpPercent < 50;
	}
}