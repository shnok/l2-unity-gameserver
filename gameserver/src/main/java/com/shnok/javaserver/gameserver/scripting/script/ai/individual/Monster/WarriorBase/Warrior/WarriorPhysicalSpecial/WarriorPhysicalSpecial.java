package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.WarriorPhysicalSpecial;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.Warrior;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class WarriorPhysicalSpecial extends Warrior
{
	public WarriorPhysicalSpecial()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/WarriorPhysicalSpecial");
	}
	
	public WarriorPhysicalSpecial(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18337,
		20606,
		20607,
		20613,
		20932,
		21016,
		21135,
		21166,
		21169,
		21172,
		21175,
		21178,
		21181,
		21184,
		21187,
		21190,
		21193,
		21196,
		21199,
		21202,
		21205,
		21278,
		21279,
		21280,
		21288,
		21659,
		21682,
		21705,
		21728,
		21751,
		21774,
		21431,
		20199,
		20084,
		21286,
		21397,
		20246,
		20238,
		21104,
		20242,
		20567,
		20667,
		21648,
		21411,
		21798,
		21112,
		21109,
		20641,
		20662,
		20661,
		20241,
		21415,
		20600,
		21274,
		21275,
		21276,
		20604,
		20654,
		20563,
		20230,
		20836,
		20788,
		20999,
		20240,
		20666,
		20665,
		20572,
		21294,
		20684,
		21261,
		21262,
		21263,
		21264,
		21282,
		21283,
		21290,
		21292,
		21352,
		21356,
		21359,
		21363,
		21367,
		22002,
		22005,
		22007,
		22008,
		22020,
		22025,
		22048
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable)
		{
			final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
			if (mostHated == attacker && Rnd.get(100) < 33)
				npc.getAI().addCastDesire(attacker, getNpcSkillByType(npc, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && called.getAI().getLifeTime() > 7 && called.getAI().getCurrentIntention().getType() != IntentionType.ATTACK && Rnd.get(100) < 33)
			called.getAI().addCastDesire(attacker, getNpcSkillByType(called, NpcSkillType.PHYSICAL_SPECIAL), 1000000);
		
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
}
