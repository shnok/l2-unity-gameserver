package com.shnok.javaserver.commons.mmocore;

public final class NioNetStackList<E>
{
	private final NioNetStackNode _start = new NioNetStackNode();
	
	private final NioNetStackNodeBuf _buf = new NioNetStackNodeBuf();
	
	private NioNetStackNode _end = new NioNetStackNode();
	
	public NioNetStackList()
	{
		clear();
	}
	
	public final void addLast(final E elem)
	{
		final NioNetStackNode newEndNode = _buf.removeFirst();
		_end._value = elem;
		_end._next = newEndNode;
		_end = newEndNode;
	}
	
	public final E removeFirst()
	{
		final NioNetStackNode old = _start._next;
		final E value = old._value;
		_start._next = old._next;
		_buf.addLast(old);
		return value;
	}
	
	public final boolean isEmpty()
	{
		return _start._next == _end;
	}
	
	public final void clear()
	{
		_start._next = _end;
	}
	
	private final class NioNetStackNode
	{
		protected NioNetStackNode _next;
		
		protected E _value;
		
		protected NioNetStackNode()
		{
			
		}
	}
	
	private final class NioNetStackNodeBuf
	{
		private final NioNetStackNode _start = new NioNetStackNode();
		
		private NioNetStackNode _end = new NioNetStackNode();
		
		NioNetStackNodeBuf()
		{
			_start._next = _end;
		}
		
		final void addLast(final NioNetStackNode node)
		{
			node._next = null;
			node._value = null;
			_end._next = node;
			_end = node;
		}
		
		final NioNetStackNode removeFirst()
		{
			if (_start._next == _end)
				return new NioNetStackNode();
			
			final NioNetStackNode old = _start._next;
			_start._next = old._next;
			return old;
		}
	}
}