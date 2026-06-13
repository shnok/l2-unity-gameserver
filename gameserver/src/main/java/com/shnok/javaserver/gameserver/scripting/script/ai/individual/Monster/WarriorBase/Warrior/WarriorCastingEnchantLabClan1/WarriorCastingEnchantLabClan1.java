package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorCastingEnchantLabClan1;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorCastingEnchantLabClan1 extends Warrior
{
	public WarriorCastingEnchantLabClan1()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorCastingEnchantLabClan1");
	}
	
	public WarriorCastingEnchantLabClan1(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		27318
	};
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		final L2Skill buff1 = getNpcSkillByType(called, NpcSkillType.BUFF1);
		final L2Skill buff2 = getNpcSkillByType(called, NpcSkillType.BUFF1);
		final L2Skill buff3 = getNpcSkillByType(called, NpcSkillType.BUFF1);
		final L2Skill buff4 = getNpcSkillByType(called, NpcSkillType.BUFF1);
		
		if (getAbnormalLevel(caller, buff1) <= 0)
			called.getAI().addCastDesire(caller, buff1, 1000000);
		else if (getAbnormalLevel(caller, buff1) >= 0)
			called.getAI().addCastDesire(caller, buff2, 1000000);
		else if (getAbnormalLevel(caller, buff2) >= 0)
			called.getAI().addCastDesire(caller, buff3, 1000000);
		else if (getAbnormalLevel(caller, buff3) >= 0)
			called.getAI().addCastDesire(caller, buff4, 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
