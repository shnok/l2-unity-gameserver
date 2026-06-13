package com.shnok.javaserver.gameserver.scripting.script.ai.siegablehall;

import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class AzitWateringRecycleCan extends DefaultNpc
{
	public AzitWateringRecycleCan()
	{
		super("ai/siegeablehall");
	}
	
	public AzitWateringRecycleCan(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		35600
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		startQuestTimer("1011", npc, null, ((5 * 60) * 1000));
		startQuestTimerAtFixedRate("1002", npc, null, 30000, 30000);
		npc.lookItem(450, 20);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		npc.broadcastNpcSay(NpcStringId.ID_1010639);
		
		return null;
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1002"))
		{
			npc.lookItem(450, 20);
		}
		else if (name.equalsIgnoreCase("1011"))
		{
			npc.deleteMe();
		}
		
		return null;
	}
	
	@Override
	public void onSeeItem(Npc npc, int quantity, List<ItemInstance> items)
	{
		if (items.isEmpty())
			return;
		
		for (int i = 0; i < items.size(); i++)
		{
			final ItemInstance item = items.get(i);
			npc.getAI().addPickUpDesire(item.getObjectId(), item.getItemId() >= 8035 && item.getItemId() <= 8055 ? (10000 - i) * 2 : (10000 - i));
		}
	}
	
	@Override
	public void onPickedItem(Npc npc, ItemInstance item)
	{
		if (item.getItemId() == 8190 || item.getItemId() == 8689)
			npc.broadcastNpcSay(NpcStringId.ID_1800023);
	}
}
