package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioKnight extends GludioHold
{
	public GludioKnight()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioKnight(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35066,
		35108,
		35150,
		35192,
		35235,
		35282,
		35326,
		35471,
		35518
	};
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill == FrequentSkill.SEAL_OF_RULER.getSkill() && getPledgeCastleState(npc, caster) != 2)
			npc.getAI().addAttackDesire(caster, 50000);
	}
}
