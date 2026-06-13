package com.shnok.javaserver.gameserver.model.buylist;

import java.util.LinkedHashMap;

/**
 * A datatype used to hold buylists. Each buylist got a Map of {@link Product}.<br>
 * For security reasons and to avoid crafted packets, we added npcId aswell.
 */
public class NpcBuyList extends LinkedHashMap<Integer, Product>
{
	private static final long serialVersionUID = 1L;
	
	private final int _listId;
	
	private int _npcId;
	
	public NpcBuyList(int listId)
	{
		_listId = listId;
	}
	
	public int getListId()
	{
		return _listId;
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public void setNpcId(int id)
	{
		_npcId = id;
	}
	
	public void addProduct(Product product)
	{
		put(product.getItemId(), product);
	}
	
	public boolean isNpcAllowed(int npcId)
	{
		return _npcId == npcId;
	}
}