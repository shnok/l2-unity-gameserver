package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WizardBase.Wizard.WizardDDMagic2;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.actors.NpcSkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.NpcStringId;
import net.sf.l2j.gameserver.skills.L2Skill;

public class WizardDDMagic2Solina extends WizardDDMagic2Heal
{
	public WizardDDMagic2Solina()
	{
		super("ai/individual/Monster/WizardBase/Wizard/WizardDDMagic2");
	}
	
	public WizardDDMagic2Solina(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		22134
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		
		super.onCreated(npc);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		
		super.onNoDesire(npc);
	}
	
	@Override
	public void onSeeCreature(Npc npc, Creature creature)
	{
		if (creature instanceof Player player)
		{
			if (checkOccupation(player))
			{
				if (npc._i_ai3 == 0)
				{
					if (Rnd.get(100) < 50)
						npc.broadcastNpcSay(NpcStringId.ID_10075, player.getName());
					else
						npc.broadcastNpcSay(NpcStringId.ID_10076, player.getName());
					
					npc._i_ai3 = 1;
				}
			}
			else if (npc.distance2D(creature) > 100 && Rnd.get(100) < 80)
			{
				final L2Skill longRangeDdMagic = getNpcSkillByType(npc, NpcSkillType.W_LONG_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(creature, longRangeDdMagic))
					npc.getAI().addCastDesire(creature, longRangeDdMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(creature, 1000);
				}
			}
			else
			{
				final L2Skill shortRangeDdMagic = getNpcSkillByType(npc, NpcSkillType.W_SHORT_RANGE_DD_MAGIC);
				if (npc.getCast().meetsHpMpConditions(creature, shortRangeDdMagic))
					npc.getAI().addCastDesire(creature, shortRangeDdMagic, 1000000);
				else
				{
					npc._i_ai0 = 1;
					npc.getAI().addAttackDesire(creature, 1000);
				}
			}
			
			npc.getAI().getHateList().addDefaultHateInfo(creature);
		}
		
		super.onSeeCreature(npc, creature);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Player player && npc._i_ai4 == 0 && checkOccupation(player))
		{
			if (Rnd.get(100) < 50)
				npc.broadcastNpcSay(NpcStringId.ID_10077, player.getName());
			else
				npc.broadcastNpcSay(NpcStringId.ID_10078, player.getName());
			
			npc._i_ai4 = 1;
		}
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	/**
	 * Check if the {@link Player}'s class is one of the specified occupations that meet certain criteria.
	 * @param player : The {@link Player} whose occupation is being checked.
	 * @return True if the {@link Player}'s class matches one of the specified occupations, or false otherwise.
	 */
	private static boolean checkOccupation(Player player)
	{
		switch (player.getClassId())
		{
			case BISHOP, CARDINAL, PALADIN, PHOENIX_KNIGHT, ELVEN_ELDER, EVAS_SAINT:
				return true;
		}
		return false;
	}
}