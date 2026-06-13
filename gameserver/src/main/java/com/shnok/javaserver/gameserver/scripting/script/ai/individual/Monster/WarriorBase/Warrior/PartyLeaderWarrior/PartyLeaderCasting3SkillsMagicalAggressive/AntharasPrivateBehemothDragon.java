package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior.PartyLeaderWarrior.PartyLeaderCasting3SkillsMagicalAggressive;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class AntharasPrivateBehemothDragon extends PartyLeaderCasting3SkillsMagicalAggressive
{
	public AntharasPrivateBehemothDragon()
	{
		super("ai/individual/Monster/WarriorBase/Warrior/PartyLeaderWarrior/PartyLeaderCasting3SkillsMagicalAggressive");
	}
	
	public AntharasPrivateBehemothDragon(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29069 // behemoth_dragon1
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc._param1 == 4)
			createPrivates(npc, 29070, 3);
		else if (npc._param1 == 5)
			createPrivates(npc, 29070, 5);
		else if (npc._param1 == 6)
			createPrivates(npc, 29070, 7);
		
		if (getNpcIntAIParam(npc, "MoveAroundSocial") > 0 || getNpcIntAIParam(npc, "ShoutMsg2") > 0 || getNpcIntAIParam(npc, "ShoutMsg3") > 0)
			startQuestTimer("1001", npc, null, 10000);
		
		startQuestTimer("1007", npc, null, 120000);
		startQuestTimer("1155", npc, null, 240 * 60 * 1000L);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1155"))
			npc.deleteMe();
		
		return super.onTimer(name, npc, player);
	}
	
	private void createPrivates(Npc master, int npcId, int count)
	{
		for (int i = 0; i < count; i++)
		{
			Npc bomber = createOnePrivate(master, npcId, 0, false);
			bomber.getSpawn().setRespawnDelay(0);
		}
	}
}