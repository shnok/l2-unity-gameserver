package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorDDMagicHold.WarriorDDMagicHoldAggressive;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class ValakasPrivateLavasaurus extends WarriorDDMagicHoldAggressive
{
	public ValakasPrivateLavasaurus()
	{
		super("ai/individual/Monster/WarriorDDMagicHold/WarriorDDMagicHoldAggressive");
	}
	
	public ValakasPrivateLavasaurus(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		29029, // valakas_lavasaurus
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		final Creature c0 = (Creature) World.getInstance().getObject(npc._param1);
		if (c0 != null)
			npc.getAI().addCastDesireHold(c0, getNpcSkillByType(npc, NpcSkillType.DD_MAGIC), 1000000);
		
		startQuestTimer("3001", npc, null, 60000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("3001"))
		{
			final IntentionType currentIntentionType = npc.getAI().getCurrentIntention().getType();
			if (currentIntentionType != IntentionType.ATTACK && currentIntentionType != IntentionType.CAST)
				npc.deleteMe();
			else
				startQuestTimer("3001", npc, null, 60000);
		}
		
		return super.onTimer(name, npc, player);
	}
}