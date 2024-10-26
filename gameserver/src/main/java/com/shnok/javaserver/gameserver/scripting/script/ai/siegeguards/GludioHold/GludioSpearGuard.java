package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioSpearGuard extends GludioHold
{
	public GludioSpearGuard()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioSpearGuard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35016,
		35026,
		35036,
		35046,
		35056,
		35070,
		35073,
		35076,
		35112,
		35115,
		35118,
		35154,
		35157,
		35160,
		35196,
		35199,
		35202,
		35239,
		35242,
		35245,
		35286,
		35289,
		35292,
		35330,
		35333,
		35336,
		35475,
		35478,
		35481,
		35522,
		35525,
		35528
	};
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill == FrequentSkill.SEAL_OF_RULER.getSkill())
			npc.getAI().addAttackDesire(caster, 50000);
	}
}
