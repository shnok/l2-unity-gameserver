package com.shnok.javaserver.gameserver.scripting.script.ai.boss.sirra;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.data.xml.DoorData;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.memo.GlobalMemo;
import com.shnok.javaserver.gameserver.model.spawn.NpcMaker;
import com.shnok.javaserver.gameserver.model.zone.type.BossZone;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.NpcHtmlMessage;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.DefaultNpc;

public class FreyaDeacon extends DefaultNpc
{
	private static final String szName = "freya_deacon_q0656";
	private static final String DoorName1 = "ice_barrier_001";
	private static final String DoorName2 = "ice_barrier_002";
	private static final String fnHi = "freya_deacon001.htm";
	private static final String fnHi2 = "freya_deacon002.htm";
	
	public FreyaDeacon()
	{
		super("ai/boss/sirra");
		
		addTalkId(32029);
	}
	
	public FreyaDeacon(String descr)
	{
		super(descr);
		
		addTalkId(32029);
	}
	
	protected final int[] _npcIds =
	{
		32029 // freya_deacon
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		npc._i_ai1 = 0;
		npc._i_ai2 = 0;
		npc._i_ai3 = 0;
		npc._i_ai4 = 0;
		npc._c_ai0 = null;
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		String htmltext = "";
		
		final Party party0 = player.getParty();
		if (npc._i_ai2 == 1)
		{
			if (party0 != null)
			{
				if (npc._i_ai1 == party0.getLeaderObjectId())
					htmltext = fnHi2;
				else
					htmltext = szName + "_01.htm";
			}
			else
				htmltext = szName + "_01.htm";
		}
		else if (npc._i_ai2 == 0)
			htmltext = fnHi;
		
		return htmltext;
	}
	
	@Override
	public String onAdvEvent(String event, Npc npc, Player player)
	{
		String htmltext = "";
		
		if (event.startsWith("link_"))
		{
			htmltext = szName + event.replace("link", "") + ".htm";
		}
		else if (event.equalsIgnoreCase("enter"))
		{
			final Party party0 = player.getParty();
			if (npc._i_ai2 == 0)
			{
				if (party0 == null)
					htmltext = szName + "_06.htm";
				else if (npc._i_ai1 == 0 || party0.getLeaderObjectId() == npc._i_ai1)
				{
					if (party0.getLeader() == player)
					{
						for (Player partyMember : party0.getMembers())
						{
							if (partyMember.getInventory().getItemCount(8057) < 10)
							{
								final NpcHtmlMessage html = new NpcHtmlMessage(npc.getObjectId());
								html.setFile("data/html/script/" + getDescr() + "/" + getName() + "/" + szName + "_03.htm");
								html.replace("%name%", partyMember.getName());
								partyMember.sendPacket(html);
								partyMember.sendPacket(ActionFailed.STATIC_PACKET);
								
								return "";
							}
						}
						
						if (player.isOverweight())
						{
							player.sendPacket(SystemMessageId.INVENTORY_LESS_THAN_80_PERCENT);
							return "";
						}
						
						for (Player partyMember : party0.getMembers())
							takeItems(partyMember, 8057, 10);
						
						party0.broadcastOnScreen(100000, NpcStringId.ID_1121000);
						
						giveItems(player, 8379, 3);
						
						htmltext = szName + "_05.htm";
						npc._i_ai4 = party0.getMembersCount();
						
						npc.broadcastNpcShout(NpcStringId.ID_1121005);
						
						startQuestTimer("1005", npc, player, 120000);
						
						npc._i_ai2 = 1;
						npc._i_ai1 = party0.getLeaderObjectId();
						npc._c_ai0 = player;
						
					}
					else
						htmltext = szName + "_02.htm";
				}
			}
			else
				htmltext = szName + "_01.htm";
		}
		
		return htmltext;
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10005)
		{
			final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_npc2314_3m1");
			if (maker0 != null)
				maker0.getMaker().onMakerScriptEvent("10025", maker0, 0, 0);
			
			if (npc._c_ai0 != null)
			{
				final Party party0 = npc._c_ai0.getParty();
				if (party0 != null)
					party0.broadcastOnScreen(10000, NpcStringId.ID_1121003);
			}
			
			broadcastScriptEvent(npc, 11039, 0, 8000);
			
			DoorData.getInstance().getDoor(DoorName1).openMe();
			DoorData.getInstance().getDoor(DoorName2).openMe();
			
			npc._i_ai2 = 0;
			npc._i_ai1 = 0;
			npc._c_ai0 = null;
		}
		else if (eventId == 10026)
		{
			if (npc._c_ai0 != null)
			{
				final Party party0 = npc._c_ai0.getParty();
				if (party0 != null)
					party0.broadcastOnScreen(10000, NpcStringId.ID_1121004);
			}
			
			broadcastScriptEvent(npc, 11039, 0, 8000);
			
			DoorData.getInstance().getDoor(DoorName1).openMe();
			DoorData.getInstance().getDoor(DoorName2).openMe();
			
			npc._i_ai3 = 1;
			npc._i_ai2 = 0;
			npc._i_ai1 = 0;
			npc._c_ai0 = null;
		}
		else if (eventId == 11037)
		{
			npc._i_ai3 = 0;
		}
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2002"))
		{
			if (npc._c_ai0 == null)
				return null;
			
			final Party party0 = npc._c_ai0.getParty();
			if (party0 != null)
			{
				if (npc._i_ai4 >= party0.getMembersCount())
				{
					for (Player partyMember : party0.getMembers())
						if (partyMember.isIn3DRadius(npc, 1000))
						{
							allowPlayerEntry(npc, partyMember);
							partyMember.teleportTo(113533, -126159, -3488, 0);
						}
					
					final NpcMaker maker0 = SpawnManager.getInstance().getNpcMaker("schuttgart13_mb2314_05m1");
					if (maker0 != null)
						maker0.getMaker().onMakerScriptEvent("11040", maker0, npc._c_ai0.getObjectId(), 0);
				}
				else
				{
					npc.broadcastNpcShout(NpcStringId.ID_1121007);
					
					npc._i_ai2 = 0;
					npc._i_ai1 = 0;
					npc._c_ai0 = null;
					
					DoorData.getInstance().getDoor(DoorName1).openMe();
					DoorData.getInstance().getDoor(DoorName2).openMe();
				}
			}
		}
		else if (name.equalsIgnoreCase("1005"))
		{
			final Npc c0 = (Npc) GlobalMemo.getInstance().getCreature("7");
			if (c0 != null)
				c0.sendScriptEvent(10027, 0, 0);
			
			DoorData.getInstance().getDoor(DoorName1).closeMe();
			DoorData.getInstance().getDoor(DoorName2).closeMe();
			
			startQuestTimer("1006", npc, null, 5000);
			startQuestTimer("2002", npc, null, 5000);
			
			npc._i_ai3 = 0;
		}
		else if (name.equalsIgnoreCase("1006"))
		{
			if (npc._c_ai0 == null)
				return null;
			
			final Party party0 = npc._c_ai0.getParty();
			if (party0 != null)
				party0.broadcastOnScreen(10000, NpcStringId.ID_1121001);
		}
		
		return super.onTimer(name, npc, player);
	}
	
	private static void allowPlayerEntry(Npc npc, Player player)
	{
		final BossZone sirra1 = ZoneManager.getInstance().getZoneById(110013, BossZone.class);
		if (sirra1 != null)
			sirra1.allowPlayerEntry(player, 2100);
		
		final BossZone sirra2 = ZoneManager.getInstance().getZoneById(110014, BossZone.class);
		if (sirra2 != null)
			sirra2.allowPlayerEntry(player, 2100);
		
		final BossZone sirra3 = ZoneManager.getInstance().getZoneById(110015, BossZone.class);
		if (sirra3 != null)
			sirra3.allowPlayerEntry(player, 2100);
	}
}
