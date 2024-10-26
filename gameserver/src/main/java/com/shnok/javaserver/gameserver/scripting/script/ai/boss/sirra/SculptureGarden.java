package com.shnok.javaserver.gameserver.scripting.script.ai.boss.sirra;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class SculptureGarden extends DefaultNpc
{
	private final L2Skill RESIST_COLD = SkillTable.getInstance().getInfo(4479, 1);
	
	public SculptureGarden()
	{
		super("ai/boss/sirra");
	}
	
	public SculptureGarden(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		32030 // sculpture_of_garden
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final Creature c0 = GlobalMemo.getInstance().getCreature("7");
		if (c0 == null)
			GlobalMemo.getInstance().set("7", npc.getObjectId());
		
		super.onCreated(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player)
			npc._c_ai0 = creature;
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10027)
		{
			npc.getSpawn().instantTeleportInMyTerritory(115792, -125760, -3373, 200);
		}
		else if (eventId == 11038)
		{
			npc.lookNeighbor();
			
			if (npc._c_ai0 != null)
			{
				final Party party0 = npc._c_ai0.getParty();
				if (party0 != null)
					for (Player partyMember : party0.getMembers())
						if (npc.getSpawn().isInMyTerritory(partyMember))
							callSkill(npc, partyMember, RESIST_COLD);
			}
		}
	}
}