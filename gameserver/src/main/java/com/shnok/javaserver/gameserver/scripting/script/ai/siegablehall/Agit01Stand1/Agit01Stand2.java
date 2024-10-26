package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall.Agit01Stand1;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;

public class Agit01Stand2 extends Agit01Stand1
{
	private static final int GUARD_SWORD_NPC_ID = 35377;
	private static final int GUARD_BOW_NPC_ID = 35378;
	
	private static final int[][] GUARD_SWORD =
	{
		{
			40,
			0,
			45842,
			109981,
			-1970,
			44565,
			108867,
			-2020,
		},
		{
			28,
			28,
			45830,
			110009,
			-1970,
			44553,
			108895,
			-2020,
		},
		{
			0,
			28,
			45802,
			110009,
			-1970,
			44525,
			108895,
			-2020,
		},
		{
			-28,
			28,
			45774,
			110009,
			-1970,
			44497,
			108895,
			-2020,
		},
		{
			-40,
			0,
			45762,
			109981,
			-1970,
			44485,
			108867,
			-2020,
		},
		{
			-28,
			-28,
			45774,
			109953,
			-1970,
			44497,
			108839,
			-2020,
		},
		{
			0,
			-40,
			45802,
			109941,
			-1970,
			44525,
			108827,
			-2020,
		},
		{
			28,
			-28,
			45830,
			109953,
			-1970,
			44553,
			108839,
			-2020,
		}
	};
	
	private static final int[][] GUARD_BOW =
	{
		{
			10,
			17,
			45812,
			109998,
			-1970,
			44535,
			108884,
			-2020,
		},
		{
			-10,
			17,
			45792,
			109998,
			-1970,
			44515,
			108884,
			-2020,
		},
		{
			-10,
			-17,
			45792,
			109964,
			-1970,
			44515,
			108850,
			-2020,
		},
		{
			10,
			-17,
			45812,
			109964,
			-1970,
			44535,
			108850,
			-2020,
		},
	};
	
	private int[] _swordGuards = new int[8];
	private int[] _bowGuards = new int[4];
	
	public Agit01Stand2()
	{
		super("ai/siegeablehall/Agit01Stand1");
	}
	
	public Agit01Stand2(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35377,
		35378,
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc.getNpcId() == GUARD_SWORD_NPC_ID)
		{
			final int slotIndex = findNearestSlotIndexByValue(_swordGuards, 0);
			if (slotIndex != -1)
				_swordGuards[slotIndex] = npc.getObjectId();
		}
		
		if (npc.getNpcId() == GUARD_BOW_NPC_ID)
		{
			final int slotIndex = findNearestSlotIndexByValue(_bowGuards, 0);
			if (slotIndex != -1)
				_bowGuards[slotIndex] = npc.getObjectId();
		}
		
		npc._i_ai0 = 0;
		startQuestTimerAtFixedRate("2001", npc, null, 10000, 10000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (npc.hasMaster())
			{
				final boolean isSwordGuard = npc.getNpcId() == GUARD_BOW_NPC_ID;
				final int npcSlotIndex = findNearestSlotIndexByValue(isSwordGuard ? _swordGuards : _bowGuards, npc.getObjectId());
				final int b02_x0 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][0] : GUARD_BOW[npcSlotIndex][0];
				final int b02_y0 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][1] : GUARD_BOW[npcSlotIndex][1];
				final int b02_x1 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][2] : GUARD_BOW[npcSlotIndex][2];
				final int b02_y1 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][3] : GUARD_BOW[npcSlotIndex][3];
				final int b02_z1 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][4] : GUARD_BOW[npcSlotIndex][4];
				final int b02_x2 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][5] : GUARD_BOW[npcSlotIndex][5];
				final int b02_y2 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][6] : GUARD_BOW[npcSlotIndex][6];
				final int b02_z2 = isSwordGuard ? GUARD_SWORD[npcSlotIndex][7] : GUARD_BOW[npcSlotIndex][7];
				if (npc.getMaster()._flag == 0)
				{
					if (npc._i_ai0 < 1)
					{
						npc.getAI().addMoveToDesire(new Location(npc.getMaster().getX() + b02_x0, npc.getMaster().getY() + b02_y0, npc.getMaster().getZ()), 100000000);
						npc._i_ai0 = 1;
					}
				}
				else if (npc.getMaster()._flag == 1)
				{
					if (npc._i_ai0 < 2)
					{
						npc.getAI().addMoveToDesire(new Location(b02_x1, b02_y1, b02_z1), 100000000);
						npc._i_ai0 = 2;
					}
				}
				else if (npc._i_ai0 < 3)
				{
					npc.getAI().addMoveToDesire(new Location(b02_x2, b02_y2, b02_z2), 100000000);
					npc._i_ai0 = 3;
				}
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (npc.getNpcId() == GUARD_SWORD_NPC_ID)
		{
			final int slotIndex = findNearestSlotIndexByValue(_swordGuards, npc.getObjectId());
			if (slotIndex != -1)
				_swordGuards[slotIndex] = 0;
		}
		
		if (npc.getNpcId() == GUARD_BOW_NPC_ID)
		{
			final int slotIndex = findNearestSlotIndexByValue(_bowGuards, npc.getObjectId());
			if (slotIndex != -1)
				_bowGuards[slotIndex] = 0;
		}
	}
	
	private static int findNearestSlotIndexByValue(int[] slotArray, int value)
	{
		for (int i = 0; i < slotArray.length; i++)
		{
			if (slotArray[i] == value)
				return i;
		}
		
		return -1;
	}
}
