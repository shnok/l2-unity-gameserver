package com.shnok.javaserver.gameserver.scripting.script.siegablehall;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.actor.Attackable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHallSiege;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

/**
 * The Fortress of the Dead is located southeast of the Rune Township and is a contested hideout similar to the siege style of the Devastated Castle clan hall. It is of the highest grade among all contested clan halls.<br>
 * <br>
 * Only a clan level 4 or higher may participate.<br>
 * <br>
 * Siege registration is open up to two hours before a war and is scheduled through the messenger NPC outside of the clan hall.<br>
 * <br>
 * The siege war follows the same rules as Devastated Castle. The siege war goes on for one hour, and the clan that contributes the most to killing Lidia von Hellmann takes possession of the clan hall. If the followers of Lidia von Hellmann, Alfred von Hellmann, and Giselle von Hellmann are killed,
 * the clan hall war will be a lot easier.<br>
 * <br>
 * The siege war ends upon the death of Lidia von Hellmann; just as in Devastated Castle, and if there is no clan that has killed the applicable NPC, the clan hall is under the NPC's possession until the next siege war.<br>
 * <br>
 * The clan that owned the clan hall previously will be automatically registered in the next clan hall war.<br>
 * <br>
 * The possessing clan hall leader can ride on a wyvern.
 */
public final class FortressOfTheDead extends ClanHallSiege
{
	private static final int LIDIA = 35629;
	private static final int ALFRED = 35630;
	private static final int GISELLE = 35631;
	
	private static final int VAMPIRE_SOLDIER = 35633;
	private static final int VAMPIRE_CASTER = 35634;
	private static final int VAMPIRE_MAGISTER = 35635;
	private static final int VAMPIRE_WARLORD = 35636;
	private static final int VAMPIRE_LEADER_1 = 35637;
	private static final int VAMPIRE_LEADER_2 = 35647;
	
	private final Map<Integer, Integer> _damageToLidia = new ConcurrentHashMap<>();
	
	public FortressOfTheDead()
	{
		super("siegablehall", FORTRESS_OF_DEAD);
		
		addAttacked(LIDIA);
		addCreated(LIDIA, ALFRED, GISELLE);
		addMyDying(LIDIA, ALFRED, GISELLE);
		addNoDesire(LIDIA, ALFRED, GISELLE, VAMPIRE_SOLDIER, VAMPIRE_CASTER, VAMPIRE_MAGISTER, VAMPIRE_WARLORD, VAMPIRE_LEADER_1, VAMPIRE_LEADER_2);
		addPartyDied(LIDIA, ALFRED, GISELLE);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equals("1001"))
		{
			npc.getAI().addCastDesire(npc, 4997, 1, 1000000);
			
			if (npc.getScriptValue() > 1)
				cancelQuestTimer("1001", npc, null);
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (!_hall.isInSiege() || !(attacker instanceof Playable))
			return;
		
		final Clan clan = attacker.getActingPlayer().getClan();
		if (clan != null && getAttackerClans().contains(clan))
			_damageToLidia.merge(clan.getClanId(), damage, Integer::sum);
		
		if (!npc.isInMyTerritory())
		{
			((Attackable) npc).getAI().getAggroList().cleanAllHate();
			npc.teleportTo(npc.getSpawnLocation(), 0);
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		switch (npc.getNpcId())
		{
			case LIDIA:
				npc.broadcastNpcShout(NpcStringId.ID_1010624);
				
				createOnePrivateEx(npc, GISELLE, 56619, -27866, 569, 54000, 0, false);
				createOnePrivateEx(npc, ALFRED, 59282, -26496, 569, 48000, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 57905, -27648, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 57905, -27712, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27182, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27232, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27282, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27332, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27382, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27432, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27482, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27532, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27582, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27632, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27682, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27732, 608, 33540, 0, false);
				createOnePrivateEx(npc, VAMPIRE_LEADER_2, 58233, -27782, 608, 33540, 0, false);
				
				startQuestTimerAtFixedRate("1001", npc, null, 0, 30000);
				break;
			
			case ALFRED:
				npc.broadcastNpcShout(NpcStringId.ID_1010636);
				break;
			
			case GISELLE:
				npc.broadcastNpcShout(NpcStringId.ID_1010637);
				break;
		}
		super.onCreated(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		switch (npc.getNpcId())
		{
			case LIDIA:
				npc.broadcastNpcShout(NpcStringId.ID_1010638);
				
				if (_hall.isInSiege())
				{
					_missionAccomplished = true;
					
					cancelSiegeTask();
					endSiege();
				}
				break;
			
			case ALFRED:
				npc.broadcastNpcShout(NpcStringId.ID_1010625);
				break;
			
			case GISELLE:
				npc.broadcastNpcShout(NpcStringId.ID_1010625);
				break;
		}
		super.onMyDying(npc, killer);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing.
	}
	
	@Override
	public void onPartyDied(Npc caller, Npc called)
	{
		if (called.getNpcId() == LIDIA)
		{
			switch (caller.getNpcId())
			{
				case ALFRED, GISELLE:
					called.setScriptValue(called.getScriptValue() + 1);
					break;
			}
		}
		super.onPartyDied(caller, called);
	}
	
	@Override
	public Clan getWinner()
	{
		// If none did damages, simply return null.
		if (_damageToLidia.isEmpty())
			return null;
		
		// Retrieve clanId who did the biggest amount of damage.
		final int clanId = Collections.max(_damageToLidia.entrySet(), Map.Entry.comparingByValue()).getKey();
		
		// Clear the Map for future usage.
		_damageToLidia.clear();
		
		// Return the Clan winner.
		return ClanTable.getInstance().getClan(clanId);
	}
	
	@Override
	public void startSiege()
	{
		// Siege must start at night
		final int hoursLeft = (GameTimeTaskManager.getInstance().getGameTime() / 60) % 24;
		if (hoursLeft < 0 || hoursLeft > 6)
		{
			cancelSiegeTask();
			
			long scheduleTime = (24 - hoursLeft) * 600000L;
			_siegeTask = ThreadPool.schedule(this::startSiege, scheduleTime);
		}
		else
			super.startSiege();
	}
	
	@Override
	public void spawnNpcs()
	{
		SpawnManager.getInstance().startSpawnTime("agit_defend_warfare_start", "64", null, null, true);
	}
	
	@Override
	public void unspawnNpcs()
	{
		SpawnManager.getInstance().stopSpawnTime("agit_defend_warfare_start", "64", null, null, true);
	}
}