package com.shnok.javaserver.gameserver.model.multisell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A datatype entry used to store multisell. A multisell is similar to a buylist, but that system has been developped in addition.<br>
 * <br>
 * It holds a List of {@link Entry} and a Set of allowed npcIds for security reasons (lazy initialization).
 */
public class ListContainer
{
	private final int _id;
	
	private boolean _applyTaxes;
	private boolean _maintainEnchantment;
	
	protected List<Entry> _entries = new ArrayList<>();
	protected Set<Integer> _npcsAllowed;
	
	public ListContainer(int id)
	{
		_id = id;
	}
	
	public final List<Entry> getEntries()
	{
		return _entries;
	}
	
	public final int getId()
	{
		return _id;
	}
	
	public final boolean getApplyTaxes()
	{
		return _applyTaxes;
	}
	
	public final void setApplyTaxes(boolean applyTaxes)
	{
		_applyTaxes = applyTaxes;
	}
	
	public final boolean getMaintainEnchantment()
	{
		return _maintainEnchantment;
	}
	
	public final void setMaintainEnchantment(boolean maintainEnchantment)
	{
		_maintainEnchantment = maintainEnchantment;
	}
	
	public void allowNpc(int npcId)
	{
		if (_npcsAllowed == null)
			_npcsAllowed = new HashSet<>();
		
		_npcsAllowed.add(npcId);
	}
	
	public boolean isNpcAllowed(int npcId)
	{
		return _npcsAllowed == null || _npcsAllowed.contains(npcId);
	}
	
	public boolean isNpcOnly()
	{
		return _npcsAllowed != null;
	}
}