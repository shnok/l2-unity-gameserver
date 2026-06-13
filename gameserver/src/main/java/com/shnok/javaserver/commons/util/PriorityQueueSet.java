package com.shnok.javaserver.commons.util;

import java.util.HashSet;
import java.util.PriorityQueue;

public class PriorityQueueSet<T> extends PriorityQueue<T>
{
	private static final long serialVersionUID = 1L;
	
	private final HashSet<T> _set;
	
	public PriorityQueueSet()
	{
		_set = new HashSet<>();
	}
	
	@Override
	public boolean add(T element)
	{
		if (_set.add(element))
			return super.add(element);
		
		return false;
	}
	
	@Override
	public boolean remove(Object element)
	{
		if (_set.remove(element))
			return super.remove(element);
		
		return false;
	}
	
	@Override
	public boolean contains(Object element)
	{
		return _set.contains(element);
	}
}