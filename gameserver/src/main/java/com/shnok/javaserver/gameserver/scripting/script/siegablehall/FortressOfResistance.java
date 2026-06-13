package com.shnok.javaserver.gameserver.scripting.script.siegablehall;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.data.manager.SpawnManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHallSiege;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * Fortress of Resistance clan hall siege Script.
 */
public final class FortressOfResistance extends ClanHallSiege
{
	private static final int BRAKEL = 35382;
	
	private static final int BLOODY_LORD_NURKA_1 = 35368;
	private static final int BLOODY_LORD_NURKA_2 = 35375;
	
	private static final int PARTISAN_HEALER = 35369;
	private static final int PARTISAN_COURT_GUARD_1 = 35370;
	private static final int PARTISAN_COURT_GUARD_2 = 35371;
	private static final int PARTISAN_SOLDIER = 35372;
	private static final int PARTISAN_SORCERER = 35373;
	private static final int PARTISAN_ARCHER = 35374;
	
	private final Map<Integer, Integer> _damageToNurka = new ConcurrentHashMap<>();
	
	public FortressOfResistance()
	{
		super("siegablehall", FORTRESS_OF_RESISTANCE);
		
		addFirstTalkId(BRAKEL);
		
		addAttacked(BLOODY_LORD_NURKA_1, BLOODY_LORD_NURKA_2);
		addClanAttacked(BLOODY_LORD_NURKA_1, BLOODY_LORD_NURKA_2, PARTISAN_HEALER, PARTISAN_COURT_GUARD_1, PARTISAN_COURT_GUARD_2, PARTISAN_SOLDIER, PARTISAN_SORCERER, PARTISAN_ARCHER);
		addCreated(BLOODY_LORD_NURKA_1, BLOODY_LORD_NURKA_2);
		addMyDying(BLOODY_LORD_NURKA_1, BLOODY_LORD_NURKA_2);
		addNoDesire(PARTISAN_SOLDIER);
		addSeeSpell(BLOODY_LORD_NURKA_1, BLOODY_LORD_NURKA_2);
	}
	
	@Override
	public String onFirstTalk(Npc npc, Player player)
	{
		return getHtmlText("partisan_ordery_brakel001.htm").replace("%nextSiege%", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(_hall.getSiegeDate().getTime()));
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		if (!_hall.isInSiege() || !(attacker instanceof Playable))
			return;
		
		if (Rnd.get(100) < 10)
			npc.getAI().addCastDesire(attacker, 4042, 1, 1000000);
		
		final Clan clan = attacker.getActingPlayer().getClan();
		if (clan != null)
			_damageToNurka.merge(clan.getClanId(), damage, Integer::sum);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		switch (called.getNpcId())
		{
			case BLOODY_LORD_NURKA_1, BLOODY_LORD_NURKA_2:
				if (Rnd.get(100) < 2)
					called.getAI().addCastDesire(attacker, 4042, 1, 1000000);
				break;
		}
		super.onClanAttacked(caller, called, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		npc.getSpawn().instantTeleportInMyTerritory(51952, 111060, -1970, 200);
		
		super.onCreated(npc);
	}
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		if (_hall.isInSiege())
		{
			_missionAccomplished = true;
			
			cancelSiegeTask();
			endSiege();
		}
		super.onMyDying(npc, killer);
	}
	
	@Override
	public void onNoDesire(Npc npc)
	{
		// Do nothing.
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		if (Rnd.get(100) < 15)
			npc.getAI().addCastDesire(caster, 4042, 1, 1000000);
		
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public Clan getWinner()
	{
		// If none did damages, simply return null.
		if (_damageToNurka.isEmpty())
			return null;
		
		// Retrieve clanId who did the biggest amount of damage.
		final int clanId = Collections.max(_damageToNurka.entrySet(), Map.Entry.comparingByValue()).getKey();
		
		// Clear the Map for future usage.
		_damageToNurka.clear();
		
		// Return the Clan winner.
		return ClanTable.getInstance().getClan(clanId);
	}
	
	@Override
	public void spawnNpcs()
	{
		SpawnManager.getInstance().startSpawnTime((_wasPreviouslyOwned) ? "agit_attack_warfare_start" : "agit_defend_warfare_start", "21", null, null, true);
	}
	
	@Override
	public void unspawnNpcs()
	{
		SpawnManager.getInstance().stopSpawnTime((_wasPreviouslyOwned) ? "agit_attack_warfare_start" : "agit_defend_warfare_start", "21", null, null, true);
	}
	
	@Override
	public void loadAttackers()
	{
		// Do nothing.
	}
}