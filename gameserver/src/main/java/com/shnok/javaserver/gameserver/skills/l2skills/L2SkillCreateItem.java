package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class L2SkillCreateItem extends L2Skill
{
	private final int[] _createItemId;
	private final int _createItemCount;
	private final int _randomCount;
	
	public L2SkillCreateItem(StatSet set)
	{
		super(set);
		
		_createItemId = set.getIntegerArray("create_item_id");
		_createItemCount = set.getInteger("create_item_count", 0);
		_randomCount = set.getInteger("random_count", 1);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (!(creature instanceof Playable playable))
			return;
		
		if (playable.isAlikeDead())
			return;
		
		if (_createItemId == null || _createItemCount == 0)
		{
			playable.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_PREPARED_FOR_REUSE).addSkillName(this));
			return;
		}
		
		playable.addItem(Rnd.get(_createItemId), _createItemCount + Rnd.get(_randomCount), true);
	}
}