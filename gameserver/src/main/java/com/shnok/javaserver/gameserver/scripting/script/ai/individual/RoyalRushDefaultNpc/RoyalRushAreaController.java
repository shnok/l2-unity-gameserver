package com.shnok.javaserver.gameserver.scripting.script.ai.individual.RoyalRushDefaultNpc;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class RoyalRushAreaController extends RoyalRushDefaultNpc
{
	public RoyalRushAreaController()
	{
		super("ai/individual/RoyalRushDefaultNpc");
	}
	
	public RoyalRushAreaController(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18196,
		18197,
		18198,
		18199,
		18200,
		18201,
		18202,
		18203,
		18204,
		18205,
		18206,
		18207,
		18208,
		18209,
		18210,
		18211
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		// TODO: Area
		// gg::Area_SetOnOff(AreaName,0);
		final int type = getNpcIntAIParam(npc, "type");
		switch (type)
		{
			case 0:
			{
				startQuestTimer("3001", npc, null, 2 * 60 * 1000L);
				npc.broadcastNpcShout(NpcStringId.ID_1010474);
				break;
			}
			case 1:
			{
				startQuestTimer("3001", npc, null, 60 * 1000L);
				npc.broadcastNpcShout(NpcStringId.ID_1010473);
				break;
			}
			case 2:
			{
				// gg::Area_SetOnOff(AreaName,1);
				npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.STATUS_EFFECT), 1000000);
				npc.broadcastNpcShout(NpcStringId.ID_1010472);
				startQuestTimer("3002", npc, null, 30 * 1000L);
				break;
			}
			case 3:
			{
				startQuestTimer("3001", npc, null, 3 * 60 * 1000L);
				npc.broadcastNpcShout(NpcStringId.ID_1010475);
				break;
			}
		}
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
		{
			// gg::Area_SetOnOff(AreaName,1);
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.STATUS_EFFECT), 1000000);
			final int type = getNpcIntAIParam(npc, "type");
			switch (type)
			{
				case 0:
				{
					npc.broadcastNpcShout(NpcStringId.ID_1010477);
					startQuestTimer("3002", npc, null, (1000 * 30));
					break;
				}
				case 1:
				{
					npc.broadcastNpcShout(NpcStringId.ID_1010476);
					startQuestTimer("3002", npc, null, (1000 * 30));
					break;
				}
				case 2:
				{
					npc.broadcastNpcShout(NpcStringId.ID_1010472);
					break;
				}
				case 3:
				{
					npc.broadcastNpcShout(NpcStringId.ID_1010478);
					startQuestTimer("3002", npc, null, (1000 * 30));
					break;
				}
			}
		}
		if (name.equalsIgnoreCase("3002"))
		{
			npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.STATUS_EFFECT), 1000000);
			startQuestTimer("3002", npc, null, (1000 * 30));
		}
		
		return super.onTimer(name, npc, null);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		// gg::Area_SetOnOff(AreaName,0);
		final int type = getNpcIntAIParam(npc, "type");
		switch (type)
		{
			case 0:
			{
				npc.broadcastNpcShout(NpcStringId.ID_1010481);
				break;
			}
			case 1:
			{
				npc.broadcastNpcShout(NpcStringId.ID_1010480);
				break;
			}
			case 2:
			{
				npc.broadcastNpcShout(NpcStringId.ID_1010479);
				break;
			}
			case 3:
			{
				npc.broadcastNpcShout(NpcStringId.ID_1010482);
				break;
			}
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		npc.getAI().addCastDesire(npc, getNpcSkillByType(npc, NpcSkillType.STATUS_EFFECT), 1000000, false);
	}
}
