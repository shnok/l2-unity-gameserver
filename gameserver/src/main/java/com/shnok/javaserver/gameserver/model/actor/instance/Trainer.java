package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;

/**
 * This class handles skills trainers.
 */
public final class Trainer extends Folk
{
	public Trainer(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public String getHtmlPath(int npcId, int val)
	{
		String filename = "";
		if (val == 0)
			filename = "" + npcId;
		else
			filename = npcId + "-" + val;
		
		return "data/html/trainer/" + filename + ".htm";
	}
}