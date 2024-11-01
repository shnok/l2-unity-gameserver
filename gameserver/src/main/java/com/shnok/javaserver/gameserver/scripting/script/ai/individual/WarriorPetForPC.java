package com.shnok.javaserver.gameserver.scripting.script.ai.individual;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.model.World;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.scripting.quest.Q020_BringUpWithLove;
import com.shnok.javaserver.gameserver.scripting.quest.Q655_AGrandPlanForTamingWildBeasts;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public class WarriorPetForPC extends DefaultNpc
{
	private static final int CRYSTAL_OF_PURITY = 8084;
	private static final int REQUIRED_CRYSTAL_COUNT = 10;
	
	public WarriorPetForPC()
	{
		super("ai/individual");
	}
	
	public WarriorPetForPC(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		16013,
		16014,
		16015,
		16016,
		16017,
		16018,
		16020,
		16022,
		16024
	};
	
	@Override
	public void onCreated(Npc npc)
	{
		if (npc._param2 == 2188)
			npc._i_ai3 = 6643;
		else
			npc._i_ai3 = 6644;
		
		npc._c_ai0 = (Creature) World.getInstance().getObject(npc._param1);
		if (!(npc._c_ai0 instanceof Player))
			return;
		
		if (Rnd.get(100) < 5)
		{
			int i0 = Rnd.get(((2028 - 2024) + 1));
			i0 = (i0 + 2024);
			npc.broadcastNpcSay(NpcStringId.get(i0), npc._c_ai0.getName());
		}
		
		startQuestTimer("2001", npc, null, 5000);
		
		QuestState st = ((Player) npc._c_ai0).getQuestList().getQuestState(Q020_BringUpWithLove.QUEST_NAME);
		if (st != null && Rnd.get(100) < 5 && !((Player) npc._c_ai0).getInventory().hasItem(7185))
		{
			giveItems(((Player) npc._c_ai0), 7185, 1);
			st.setCond(2);
		}
		
		testCrystalOfPurity((Player) npc._c_ai0, npc);
		
		npc._i_ai1 = GameTimeTaskManager.getInstance().getCurrentTick();
		broadcastScriptEventEx(npc, 10022, npc._c_ai0.getObjectId(), npc._i_ai1, 1500);
		startQuestTimer("2002", npc, null, 15000);
		startQuestTimer("2003", npc, null, 60000);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("2001"))
		{
			if (!npc.isDead())
			{
				int i0 = 5;
				if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF1)) <= 0)
					i0 = (i0 - 1);
				
				if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF2)) <= 0)
					i0 = (i0 - 1);
				
				if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF3)) <= 0)
					i0 = (i0 - 1);
				
				if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF4)) <= 0)
					i0 = (i0 - 1);
				
				if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF5)) <= 0)
					i0 = (i0 - 1);
				
				if (npc._c_ai0 == null)
					return null;
				
				if (i0 < 3)
				{
					final int i1 = Rnd.get(100);
					if (i1 < 20)
					{
						if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF1)) <= 0)
							npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF1), 1000000);
					}
					else if (i1 < 40)
					{
						if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF2)) <= 0)
							npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF2), 1000000);
					}
					else if (i1 < 60)
					{
						if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF3)) <= 0)
							npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF3), 1000000);
					}
					else if (i1 < 80)
					{
						if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF4)) <= 0)
							npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF4), 1000000);
					}
					else if (getAbnormalLevel(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF5)) <= 0)
					{
						npc.getAI().addCastDesire(npc._c_ai0, getNpcSkillByType(npc, NpcSkillType.BUFF5), 1000000);
					}
				}
			}
			
			broadcastScriptEvent(npc, 10017, npc.getObjectId(), 700);
			
			if (npc.distance2D(npc._c_ai0) > 2000)
				npc.deleteMe();
			
			startQuestTimer("2001", npc, player, 5000);
		}
		else if (name.equalsIgnoreCase("2002"))
		{
			if (npc._c_ai0 != null)
				broadcastScriptEventEx(npc, 10022, npc._c_ai0.getObjectId(), npc._i_ai1, 500);
			
			startQuestTimer("2002", npc, player, 15000);
		}
		else if (name.equalsIgnoreCase("2003"))
		{
			if (npc._c_ai0 != null)
			{
				if (npc._c_ai0.getInventory().getItemCount(npc._i_ai3) >= getNpcIntAIParamOrDefault(npc, "ConsumeFeedNum", 1) && !npc._c_ai0.isDead())
				{
					takeItems(((Player) npc._c_ai0), npc._i_ai3, getNpcIntAIParamOrDefault(npc, "ConsumeFeedNum", 1));
					if (getNpcIntAIParam(npc, "TakeSocial") != 0)
						npc.getAI().addSocialDesire(1, (getNpcIntAIParam(npc, "TakeSocial") * 1000) / 30, 200);
					
					int i0 = Rnd.get(((2038 - 2029) + 1));
					i0 = (i0 + 2029);
					npc.broadcastNpcSay(NpcStringId.get(i0));
				}
				else
				{
					npc.deleteMe();
				}
			}
			
			startQuestTimer("2003", npc, player, 60000);
		}
		
		return null;
	}
	
	@Override
	public void onScriptEvent(Npc npc, int eventId, int arg1, int arg2)
	{
		if (eventId == 10022)
		{
			if (npc._c_ai0 == null)
				return;
			
			if (arg1 == npc._c_ai0.getObjectId() && arg2 > npc._i_ai1)
				npc.deleteMe();
		}
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (caller == called)
			return;
		
		final L2Skill debuff1 = getNpcSkillByType(called, NpcSkillType.DEBUFF1);
		final L2Skill debuff2 = getNpcSkillByType(called, NpcSkillType.DEBUFF2);
		
		int i0 = 2;
		if (getAbnormalLevel(called._c_ai0, debuff1) <= 0)
			i0 = (i0 - 1);
		
		if (getAbnormalLevel(called._c_ai0, debuff2) <= 0)
			i0 = (i0 - 1);
		
		if (i0 < 1 && caller.getStatus().getHpRatio() >= 0.8)
		{
			if (getAbnormalLevel(caller, debuff1) <= 0 && Rnd.get(100) < 33)
				called.getAI().addCastDesire(caller, debuff1, 1000000);
			
			if (getAbnormalLevel(caller, debuff2) <= 0 && Rnd.get(100) < 33)
				called.getAI().addCastDesire(caller, debuff2, 1000000);
		}
		
		if (called._c_ai0 == null)
			return;
		
		if (called._c_ai0.getStatus().getHpRatio() < 0.25)
		{
			if (Rnd.get(100) < 40)
				called.getAI().addCastDesire(called._c_ai0, getNpcSkillByType(called, NpcSkillType.HEAL), 1000000);
		}
		else if (called._c_ai0.getStatus().getHpRatio() < 0.5)
		{
			if (Rnd.get(100) < 20)
				called.getAI().addCastDesire(called._c_ai0, getNpcSkillByType(called, NpcSkillType.HEAL), 1000000);
		}
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		if (npc._c_ai0 != null)
		{
			double deltaX = npc._c_ai0.getX() - npc.getX();
			double deltaY = npc._c_ai0.getY() - npc.getY();
			
			double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			
			double unitX = deltaX / magnitude;
			double unitY = deltaY / magnitude;
			
			int desiredX = (int) (npc._c_ai0.getX() - 150 * unitX);
			int desiredY = (int) (npc._c_ai0.getY() - 150 * unitY);
			
			npc.setWalkOrRun(true);
			npc.getAI().addMoveToDesire(new Location(desiredX, desiredY, npc._c_ai0.getZ()), 10000);
		}
	}
	
	private static void testCrystalOfPurity(Player player, Npc npc)
	{
		// No valid player instance is passed, there is nothing to check.
		if (player == null || npc == null)
			return;
		
		// Player killer must be in range with npc.
		if (!player.isIn3DRadius(npc, 2000))
			return;
		
		QuestState st = null;
		
		// If player is the leader, retrieves directly the qS and bypass others checks
		if (player.isClanLeader())
			st = player.getQuestList().getQuestState(Q655_AGrandPlanForTamingWildBeasts.QUEST_NAME);
		else
		{
			// Verify if the player got a clan
			final Clan clan = player.getClan();
			if (clan == null)
				return;
			
			// Verify if the leader is online
			final Player leader = clan.getLeader().getPlayerInstance();
			if (leader == null)
				return;
			
			// Verify if the leader is on the radius of the npc. If true, send leader's quest state.
			if (leader.isIn3DRadius(npc, 2000))
				st = leader.getQuestList().getQuestState(Q655_AGrandPlanForTamingWildBeasts.QUEST_NAME);
		}
		
		// We didn't found any valid QuestState ; stop here.
		if (st == null)
			return;
		
		// Condition exists? Condition has correct value?
		if (st.getCond() != 1)
			return;
		
		// Reward clan leader with a crystal of purity. Once reached 10, trigger cond 2.
		if (dropItemsAlways(player, CRYSTAL_OF_PURITY, 1, REQUIRED_CRYSTAL_COUNT))
			st.setCond(2);
	}
}