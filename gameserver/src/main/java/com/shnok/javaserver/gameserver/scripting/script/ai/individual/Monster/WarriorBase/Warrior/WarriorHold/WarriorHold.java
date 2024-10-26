package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorHold;

import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;

public class WarriorHold extends Warrior
{
	public WarriorHold()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorHold");
	}
	
	public WarriorHold(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27109,
		27105,
		27102,
		27139,
		27104,
		27107,
		27106,
		27103
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc.getPosition().distance2D(npc.getSpawnLocation()) > 0)
			npc.getAI().addMoveToDesire(npc.getSpawnLocation(), 30);
		else
			npc.getAI().addDoNothingDesire(40, 30);
	}
}