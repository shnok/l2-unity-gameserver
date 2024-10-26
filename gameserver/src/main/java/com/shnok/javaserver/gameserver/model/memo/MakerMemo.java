package com.shnok.javaserver.gameserver.model.memo;

import java.util.Map;

import com.shnok.javaserver.commons.data.MemoSet;

/**
 * An implementation of {@link MemoSet} used for Spawn Makers.
 */
public class MakerMemo extends MemoSet
{
	private static final long serialVersionUID = 1L;
	
	public static final MakerMemo DUMMY_SET = new MakerMemo();
	
	public MakerMemo()
	{
		super();
	}
	
	public MakerMemo(final int size)
	{
		super(size);
	}
	
	public MakerMemo(final Map<String, String> m)
	{
		super(m);
	}
	
	@Override
	protected void onSet(String key, String value)
	{
		// Do nothing.
	}
	
	@Override
	protected void onUnset(String key)
	{
		// Do nothing.
	}
}