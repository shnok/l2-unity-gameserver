package com.shnok.javaserver.gameserver.scripting.script.ai.siegeguards.GludioHold;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class GludioSwordGuard extends GludioHold
{
	public GludioSwordGuard()
	{
		super("ai/siegeguards/GludioHold");
	}
	
	public GludioSwordGuard(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		31400,
		35015,
		35025,
		35035,
		35045,
		35055,
		35071,
		35074,
		35077,
		35113,
		35116,
		35119,
		35155,
		35158,
		35161,
		35197,
		35200,
		35203,
		35240,
		35243,
		35246,
		35287,
		35290,
		35293,
		35331,
		35334,
		35337,
		35476,
		35479,
		35482,
		35523,
		35526,
		35529
	};
	
	@Override
	public void onSpelled(Npc npc, Player caster, L2Skill skill)
	{
		if (skill == FrequentSkill.SEAL_OF_RULER.getSkill())
			npc.getAI().addAttackDesire(caster, 50000);
	}
}
