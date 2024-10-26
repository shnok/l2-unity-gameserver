package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Chests;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.scripting.script.ai.individual.Monster.WarriorBase.WarriorBase;
import net.sf.l2j.gameserver.skills.L2Skill;

public class TreasureChest extends WarriorBase
{
	public TreasureChest()
	{
		super("ai/individual/Monster/WarriorBase/Chests");
	}
	
	public TreasureChest(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		18265,
		18266,
		18267,
		18268,
		18269,
		18270,
		18271,
		18272,
		18273,
		18274,
		18275,
		18276,
		18277,
		18278,
		18279,
		18280,
		18281,
		18282,
		18283,
		18284,
		18285,
		18286,
		18287,
		18288,
		18289,
		18290,
		18291,
		18292,
		18293,
		18294,
		18295,
		18296,
		18297,
		18298
	};
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		int i0 = 0;
		int i1 = skill != null ? skill.getLevel() : 0;
		int i2 = 0;
		int i3 = 0;
		int npcLvl = npc.getStatus().getLevel();
		
		if (skill != null && skill.getId() == 27)
		{
			if (i1 == 1)
			{
				i0 = 98;
			}
			else if (i1 == 2)
			{
				i0 = 84;
			}
			else if (i1 == 3)
			{
				i0 = 99;
			}
			else if (i1 == 4)
			{
				i0 = 84;
			}
			else if (i1 == 5)
			{
				i0 = 88;
			}
			else if (i1 == 6)
			{
				i0 = 90;
			}
			else if (i1 == 7)
			{
				i0 = 89;
			}
			else if (i1 == 8)
			{
				i0 = 88;
			}
			else if (i1 == 9)
			{
				i0 = 86;
			}
			else if (i1 == 10)
			{
				i0 = 90;
			}
			else if (i1 == 11)
			{
				i0 = 87;
			}
			else if (i1 == 12)
			{
				i0 = 89;
			}
			else if (i1 == 13)
			{
				i0 = 89;
			}
			else if (i1 == 14)
			{
				i0 = 89;
			}
			else if (i1 == 15)
			{
				i0 = 89;
			}
			i2 = (i0 - (((npcLvl - (i1 * 4)) - 16) * 6));
			if (i2 > i0)
			{
				i2 = i0;
			}
		}
		else if (skill != null && skill.getId() == 2065)
		{
			i2 = (int) (60 - ((npcLvl - ((i1 - 1) * 10)) * 1.5));
			if (i2 > 60)
			{
				i2 = 60;
			}
		}
		else if (skill != null && skill.getId() == 2229)
		{
			if (i1 == 1)
			{
				i3 = (npcLvl - 19);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) ((((0.000200 * (i3 * i3)) - (0.026400 * i3)) + 0.769500) * 100);
				}
			}
			else if (i1 == 2)
			{
				i3 = (npcLvl - 29);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) (((((0.000300 * i3) * i3) - (0.027900 * i3)) + 0.756800) * 100);
				}
			}
			else if (i1 == 3)
			{
				i3 = (npcLvl - 39);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) (((((0.000300 * i3) * i3) - (0.026900 * i3)) + 0.733400) * 100);
				}
			}
			else if (i1 == 4)
			{
				i3 = (npcLvl - 49);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) (((((0.000300 * i3) * i3) - (0.028400 * i3)) + 0.803400) * 100);
				}
			}
			else if (i1 == 5)
			{
				i3 = (npcLvl - 59);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) (((((0.000500 * i3) * i3) - (0.035600 * i3)) + 0.906500) * 100);
				}
			}
			else if (i1 == 6)
			{
				i3 = (npcLvl - 69);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) (((((0.000900 * i3) * i3) - (0.037300 * i3)) + 0.857200) * 100);
				}
			}
			else if (i1 == 7)
			{
				i3 = (npcLvl - 79);
				if (i3 < 0)
				{
					i2 = 100;
				}
				else
				{
					i2 = (int) (((((0.004300 * i3) * i3) - (0.067100 * i3)) + 0.959300) * 100);
				}
			}
			else if (i1 == 8)
			{
				i2 = 100;
			}
		}
		else
		{
			i0 = Math.min(10, Math.round(npcLvl / 10) + 1);
			L2Skill treasureBomb = SkillTable.getInstance().getInfo(4143, i0);
			
			npc.getAI().addCastDesire(attacker, treasureBomb, 1000000);
			return;
		}
		
		if (Rnd.get(100) < i2)
			npc.doDie(attacker);
		else
		{
			npc.deleteMe();
			
			final Player player = attacker.getActingPlayer();
			if (player != null)
				playSound(player, "ItemSound2.broken_key");
		}
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill != null)
			npc.deleteMe();
	}
}