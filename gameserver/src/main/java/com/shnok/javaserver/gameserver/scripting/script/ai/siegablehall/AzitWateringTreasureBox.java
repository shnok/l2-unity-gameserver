package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class AzitWateringTreasureBox extends DefaultNpc
{
	private static final int ITEM_A = 8035;
	private static final int ITEM_B = 8036;
	private static final int ITEM_C = 8037;
	private static final int ITEM_D = 8038;
	private static final int ITEM_E = 8039;
	private static final int ITEM_F = 8040;
	private static final int ITEM_G = 8041;
	private static final int ITEM_H = 8042;
	private static final int ITEM_I = 8043;
	private static final int ITEM_K = 8045;
	private static final int ITEM_L = 8046;
	private static final int ITEM_N = 8047;
	private static final int ITEM_O = 8048;
	private static final int ITEM_P = 8049;
	private static final int ITEM_R = 8050;
	private static final int ITEM_S = 8051;
	private static final int ITEM_T = 8052;
	private static final int ITEM_U = 8053;
	private static final int ITEM_W = 8054;
	private static final int ITEM_Y = 8055;
	
	public AzitWateringTreasureBox()
	{
		super("ai/siegeablehall");
	}
	
	public AzitWateringTreasureBox(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35595
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.setInvul(true);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (attacker instanceof Playable && attacker.getActiveWeaponInstance() == null && skill == null)
		{
			final boolean shouldDie = (npc.getStatus().getHp() - damage) <= 0;
			if (shouldDie)
				npc.doDie(attacker);
			else
				npc.getStatus().setHp(npc.getStatus().getHp() - damage, true);
		}
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final int i2 = (Rnd.get(2) + 1);
		for (int i = 0; i < i2; i++)
		{
			final int i0 = Rnd.get(100);
			if (i0 <= 5)
				npc.dropItem(killer, ITEM_A, 1);
			else if (i0 > 5 && i0 <= 10)
				npc.dropItem(killer, ITEM_B, 1);
			else if (i0 > 10 && i0 <= 15)
				npc.dropItem(killer, ITEM_C, 1);
			else if (i0 > 15 && i0 <= 20)
				npc.dropItem(killer, ITEM_D, 1);
			else if (i0 > 20 && i0 <= 25)
				npc.dropItem(killer, ITEM_E, 1);
			else if (i0 > 25 && i0 <= 30)
				npc.dropItem(killer, ITEM_F, 1);
			else if (i0 > 30 && i0 <= 35)
				npc.dropItem(killer, ITEM_G, 1);
			else if (i0 > 35 && i0 <= 40)
				npc.dropItem(killer, ITEM_H, 1);
			else if (i0 > 40 && i0 <= 45)
				npc.dropItem(killer, ITEM_I, 1);
			else if (i0 > 45 && i0 <= 50)
				npc.dropItem(killer, ITEM_K, 1);
			else if (i0 > 50 && i0 <= 55)
				npc.dropItem(killer, ITEM_L, 1);
			else if (i0 > 55 && i0 <= 60)
				npc.dropItem(killer, ITEM_N, 1);
			else if (i0 > 60 && i0 <= 65)
				npc.dropItem(killer, ITEM_O, 1);
			else if (i0 > 65 && i0 <= 70)
				npc.dropItem(killer, ITEM_P, 1);
			else if (i0 > 70 && i0 <= 75)
				npc.dropItem(killer, ITEM_R, 1);
			else if (i0 > 75 && i0 <= 80)
				npc.dropItem(killer, ITEM_S, 1);
			else if (i0 > 80 && i0 <= 85)
				npc.dropItem(killer, ITEM_T, 1);
			else if (i0 > 85 && i0 <= 90)
				npc.dropItem(killer, ITEM_U, 1);
			else if (i0 > 90 && i0 <= 95)
				npc.dropItem(killer, ITEM_W, 1);
			else if (i0 < 95)
				npc.dropItem(killer, ITEM_Y, 1);
		}
	}
}