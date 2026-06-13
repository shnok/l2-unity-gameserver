package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.RoyalRushRoomBossBasic;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushRoomBossBasic extends Warrior
{
	public RoyalRushRoomBossBasic()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/RoyalRushRoomBossBasic");
	}
	
	public RoyalRushRoomBossBasic(String descr)
	{
		super(descr);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
			if (topDesireTarget != null && topDesireTarget == attacker && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL1), 1000000);
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final int keyBox = getNpcIntAIParam(npc, "KeyBox");
		final int keyBox_X = getNpcIntAIParam(npc, "KeyBox_X");
		final int keyBox_Y = getNpcIntAIParam(npc, "KeyBox_Y");
		final int keyBox_Z = getNpcIntAIParam(npc, "KeyBox_Z");
		
		if (keyBox_X != 0 && keyBox_Y != 0 && keyBox_Z != 0)
			createOnePrivateEx(npc, keyBox, keyBox_X, keyBox_Y, keyBox_Z, 0, 0, false, 0, 0, 0);
		else
			createOnePrivateEx(npc, keyBox, npc.getX(), npc.getY(), npc.getZ(), 0, 0, false, 0, 0, 0);
		
		super.onMyDying(npc, killer);
	}
}
