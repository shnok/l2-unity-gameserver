package com.shnok.javaserver.gameserver.scripting.script.maker;

import java.util.Calendar;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.model.spawn.MultiSpawn;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;

public class RoyalSpawnTreasureBoxMaker extends RoyalRushMaker
{
	public RoyalSpawnTreasureBoxMaker(String name)
	{
		super(name);
	}
	
	@Override
	public void onMakerScriptEvent(String name, NpcMaker maker, int int1, int int2)
	{
		if (name.equalsIgnoreCase("1002"))
		{
			final MultiSpawn def0 = maker.getSpawns().get(0);
			if (def0 == null)
				return;
			
			final Calendar c = Calendar.getInstance();
			
			final int i1 = c.get(Calendar.MINUTE);
			
			int i2 = Rnd.get(10);
			if (i1 >= 48)
				i2 += 10;
			else if (i1 >= 46)
				i2 += 15;
			else if (i1 >= 44)
				i2 += 20;
			else if (i1 >= 42)
				i2 += 26;
			else if (i1 >= 40)
				i2 += 32;
			else if (i1 >= 38)
				i2 += 39;
			else if (i1 >= 36)
				i2 += 45;
			else if (i1 >= 34)
				i2 += 52;
			else if (i1 >= 32)
				i2 += 60;
			else if (i1 >= 30)
				i2 += 68;
			else if (i1 >= 28)
				i2 += 76;
			else if (i1 >= 26)
				i2 += 85;
			else if (i1 >= 24)
				i2 += 94;
			else if (i1 >= 22)
				i2 += 103;
			else if (i1 >= 20)
				i2 += 113;
			else if (i1 >= 16)
				i2 += 123;
			else if (i1 >= 14)
				i2 += 134;
			else if (i1 >= 12)
				i2 += 145;
			else
				i2 += 157;
			
			if ((maker.getNpcsAlive() + i2) <= maker.getMaximumNpc())
			{
				for (int i = 0; i < i2; i++)
					if (def0.getDecayed() > 0)
						def0.doRespawn();
					else
						def0.doSpawn(false);
			}
		}
	}
}