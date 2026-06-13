package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioCourtGuard extends GludioHold
{
	public GludioCourtGuard()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioCourtGuard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35069,
		35111,
		35153,
		35195,
		35238,
		35285,
		35329,
		35474,
		35521
	};
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill == FrequentSkill.SEAL_OF_RULER.getSkill())
			npc.getAI().addAttackDesire(caster, 50000);
	}
}
