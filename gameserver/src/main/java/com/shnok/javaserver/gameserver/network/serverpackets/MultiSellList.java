package com.shnok.javaserver.gameserver.network.serverpackets;

import static net.sf.l2j.gameserver.data.xml.MultisellData.PAGE_SIZE;

import net.sf.l2j.gameserver.model.multisell.Entry;
import net.sf.l2j.gameserver.model.multisell.Ingredient;
import net.sf.l2j.gameserver.model.multisell.ListContainer;

public class MultiSellList extends L2GameServerPacket
{
	private final ListContainer _list;
	
	private int _index;
	private int _size;
	
	private boolean _finished;
	
	public MultiSellList(ListContainer list, int index)
	{
		_list = list;
		_index = index;
		
		_size = list.getEntries().size() - index;
		if (_size > PAGE_SIZE)
		{
			_finished = false;
			_size = PAGE_SIZE;
		}
		else
			_finished = true;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xd0);
		writeD(_list.getId());
		writeD(1 + (_index / PAGE_SIZE));
		writeD(_finished ? 1 : 0);
		writeD(PAGE_SIZE);
		writeD(_size);
		
		while (_size-- > 0)
		{
			Entry ent = _list.getEntries().get(_index++);
			
			writeD(_index);
			writeD(0x00); // C6
			writeD(0x00); // C6
			writeC(ent.isStackable() ? 1 : 0);
			writeH(ent.getProducts().size());
			writeH(ent.getIngredients().size());
			
			for (Ingredient ing : ent.getProducts())
			{
				writeH(ing.getItemId());
				if (ing.getTemplate() != null)
				{
					writeD(ing.getTemplate().getBodyPart());
					writeH(ing.getTemplate().getType2());
				}
				else
				{
					writeD(0);
					writeH(65535);
				}
				writeD(ing.getItemCount());
				writeH(ing.getEnchantLevel());
				writeD(0x00); // Unused (augment)
				writeD(0x00); // Unused (mana left)
			}
			
			for (Ingredient ing : ent.getIngredients())
			{
				writeH(ing.getItemId());
				writeH(ing.getTemplate() != null ? ing.getTemplate().getType2() : 65535);
				writeD(ing.getItemCount());
				writeH(ing.getEnchantLevel());
				writeD(0x00); // Unused (augment)
				writeD(0x00); // Unused (mana left)
			}
		}
	}
}