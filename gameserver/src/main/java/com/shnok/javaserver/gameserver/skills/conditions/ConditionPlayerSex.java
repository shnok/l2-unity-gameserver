package com.shnok.javaserver.gameserver.skills.conditions;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ConditionPlayerSex extends Condition
{
	private final int _sex;
	
	public ConditionPlayerSex(int sex)
	{
		_sex = sex;
	}
	
	@Override
	public boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item)
	{
		return effector instanceof Player player && player.getAppearance().getSex().ordinal() == _sex;
	}
}