package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyPrivateWarrior;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.network.NpcStringId;

public class FreyaBear extends PartyPrivateWarrior
{
	public FreyaBear()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyPrivateWarrior");
	}
	
	public FreyaBear(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22103 // freya_bear
	};
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (npc.isDead())
			return;
		
		if (eventId == 10002)
		{
			if (!npc.hasMaster())
				return;
			
			final Creature c0 = (Creature) World.getInstance().getObject(arg1);
			if (c0 == npc.getMaster())
			{
				final Creature c1 = (Creature) World.getInstance().getObject(npc.getMaster()._flag);
				
				final Creature topDesireTarget = npc.getAI().getTopDesireTarget();
				if (topDesireTarget != null && c1 == topDesireTarget)
					return;
				
				switch (Rnd.get(4))
				{
					case 0:
						npc.broadcastNpcSay(NpcStringId.ID_1000292);
						break;
					
					case 1:
						npc.broadcastNpcSay(NpcStringId.ID_1000400);
						break;
					
					case 2:
						npc.broadcastNpcSay(NpcStringId.ID_1000401);
						break;
					
					case 3:
						npc.broadcastNpcSay(NpcStringId.ID_1000402);
						break;
				}
				
				if (c1 != null)
				{
					npc.getAI().getAggroList().cleanAllHate();
					npc.getAI().addAttackDesire(c1, 1000000);
				}
			}
		}
		else if (eventId == 10034)
		{
			npc.getAI().addCastDesire(npc.getMaster(), getNpcSkillByType(npc, NpcSkillType.MAGIC_HEAL), 1000000);
		}
		else if (eventId == 11039)
		{
			npc.deleteMe();
		}
	}
}