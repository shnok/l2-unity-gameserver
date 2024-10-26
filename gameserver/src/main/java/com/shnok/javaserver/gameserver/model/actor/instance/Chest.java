package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;

public final class Chest extends Monster
{
	private static int[] BOXES = new int[]
	{
		18265,
		18266,
		18267,
		18268,
		18269,
		18270,
		18271,
		18272,
		18273,
		18274,
		18275,
		18276,
		18277,
		18278,
		18279,
		18280,
		18281,
		18282,
		18283,
		18284,
		18285,
		18286,
		18287,
		18288,
		18289,
		18290,
		18291,
		18292,
		18293,
		18294,
		18295,
		18296,
		18297,
		18298
	};
	
	private final boolean _isBox;
	
	private volatile boolean _isInteracted;
	
	public Chest(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		
		_isBox = ArraysUtil.contains(BOXES, template.getNpcId());
		
		_isInteracted = false;
		
		setNoRndWalk(true);
		if (_isBox)
			disableCoreAi(true);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		_isInteracted = false;
	}
	
	public boolean isBox()
	{
		return _isBox;
	}
	
	public boolean isInteracted()
	{
		return _isInteracted;
	}
	
	public void setInteracted()
	{
		_isInteracted = true;
	}
}