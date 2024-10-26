package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Chests;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.WarriorBase;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TreasureChestMimic extends WarriorBase
{
	public TreasureChestMimic()
	{
		super("ai/individual/Monster/WarriorBase/Chests");
	}
	
	public TreasureChestMimic(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		21671,
		21694,
		21717,
		21740,
		21763,
		21786,
		21801,
		21802,
		21803,
		21804,
		21805,
		21806,
		21807,
		21808,
		21809,
		21810,
		21811,
		21812,
		21813,
		21814,
		21815,
		21816,
		21817,
		21818,
		21819,
		21820,
		21821,
		21822,
		18005
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null && (skill.getId() == 27 || skill.getId() == 2065))
		{
			int skillLvl = npc.getStatus().getLevel() - 5;
			
			L2Skill mimicOfWrath = SkillTable.getInstance().getInfo(4245, 1);
			L2Skill mimicStrongAttack = SkillTable.getInstance().getInfo(4144, skillLvl);
			
			npc.getAI().addCastDesire(npc, mimicOfWrath, 1000000);
			npc.getAI().addCastDesire(attacker, mimicStrongAttack, 1000000);
			npc.getAI().addAttackDesire(attacker, (((skill.getLevel()) / (npc.getStatus().getLevel() + 7)) * 150));
		}
		if (attacker instanceof Playable)
		{
			if (damage == 0)
			{
				damage = 1;
			}
			npc.getAI().addAttackDesire(attacker, ((1.0 * damage) / (npc.getStatus().getLevel() + 7)) * 100);
		}
	}
}
