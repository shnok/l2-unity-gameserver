package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;

/**
 * This class is here to avoid hardcoded IDs.<br>
 * It refers to mobs that can be attacked but can also be fed.<br>
 * This class is only used by handlers in order to check the correctness of the target.<br>
 * However, no additional tasks are needed, since they are all handled by scripted AI.
 */
public class FeedableBeast extends Monster
{
	public FeedableBeast(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
}