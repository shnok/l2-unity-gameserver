package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PartyLeaderSplit extends Warrior
{
	public PartyLeaderSplit()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public PartyLeaderSplit(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22088
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (skill != null && !skill.isMagic() && npc._i_ai1 == 0)
		{
			final int silhouette = getNpcIntAIParam(npc, "silhouette");
			final int x = npc.getX();
			final int y = npc.getY();
			final int z = npc.getZ();
			
			createOnePrivateEx(npc, silhouette, x, y, z, 0, 0, false, 1000, attacker.getObjectId(), 0);
			createOnePrivateEx(npc, silhouette, x + 20, y, z, 0, 0, false, 1000, attacker.getObjectId(), 0);
			createOnePrivateEx(npc, silhouette, x + 40, y, z, 0, 0, false, 1000, attacker.getObjectId(), 0);
			createOnePrivateEx(npc, silhouette, x, y + 20, z, 0, 0, false, 1000, attacker.getObjectId(), 0);
			createOnePrivateEx(npc, silhouette, x, y + 40, z, 0, 0, false, 1000, attacker.getObjectId(), 0);
			createOnePrivateEx(npc, silhouette, x, y + 60, z, 0, 0, false, 1000, attacker.getObjectId(), 0);
			
			npc._i_ai1 = 1;
			
			npc.deleteMe();
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
}
