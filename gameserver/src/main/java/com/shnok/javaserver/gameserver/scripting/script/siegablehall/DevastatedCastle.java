package com.shnok.javaserver.gameserver.scripting.script.siegablehall;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.random.Rnd;

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

/**
 * The Devastated Castle is an occupied clan hall for which ownership can be acquired by siege battle with NPCs. The siege is set up to take place with the NPCs every week, which is different from other sieges. The clan hall battle for the Devastated Castle is always defended by an NPC clan and the
 * ownership to the castle is maintained for one week by the PC clan that is able to defeat the defending NPC clan and take the castle.<br>
 * <br>
 * <b>Time of the Occupying Battle</b><br>
 * <br>
 * The time when the Devastated Castle occupying battle takes place is at the same time each week after the very first occupation battle.<br>
 * <br>
 * <b>Registration Participation</b><br>
 * <br>
 * Clan that wants to participate in the battle to occupy and acquire the clan hall must register to participate in advance. Participation in the battle to occupy takes place in clan units and only clan leaders having clans of level 4 or above can apply to register. Clans that already own another
 * clan hall cannot register. The clan that had owned the Devastated Castle previously is automatically registered for the siege and the registration for the battle to occupy must be done at least two hours before the start time. Clans that participate in the battle to occupy the Devastated Castle
 * cannot participate in another battle to occupy another clan hall or siege at the same time.<br>
 * <br>
 * <b>Start of the Siege Battle</b><br>
 * <br>
 * When the time set for the siege starts, the area inside and around the Devastated Castle is designated as the battleground and the players that are already inside the castle are all kicked to the outside. Leader of the clans that are registered in the battle to occupy the castle can build a
 * headquarters.The rules of battle during the battle to occupy the castle are the same as ordinary sieges except for the fact that everyone is on the attacking side and not on the defending side. However, even if the headquarters are destroyed or an unregistered character enters the battlefield and
 * is killed, players are not restarted at the second closest village like in a siege.<br>
 * <br>
 * <b>Conditions of Victory</b><br>
 * <br>
 * A battle to occupy lasts for one hour and the clan recorded as having contributed the most to killing the head NPC of the Devastated Castle becomes the owner of the clan hall. If the leader is killed, the battle to occupy ends immediately. If no clan is able to kill the leader within the time
 * period, the Devastated Castle continues to be occupied by the NPCs until the next battle to occupy.
 */
public final class DevastatedCastle extends ClanHallSiege
{
	private static final int DIETRICH = 35408;
	private static final int MIKHAIL = 35409;
	private static final int GUSTAV = 35410;
	
	private static final int DOOM_SERVANT = 35411;
	private static final int DOOM_GUARD = 35412;
	private static final int DOOM_ARCHER = 35413;
	private static final int DOOM_TROOPER = 35414;
	private static final int DOOM_WARRIOR = 35415;
	private static final int DOOM_KNIGHT = 35416;
	
	private final Map<Integer, Integer> _damageToGustav = new ConcurrentHashMap<>();
	
	private Player _speller;
	
	public DevastatedCastle()
	{
		super("siegablehall", DEVASTATED_CASTLE);
		
		addAttacked(GUSTAV);
		addCreated(DIETRICH, MIKHAIL, GUSTAV);
		addMyDying(GUSTAV);
		addNoDesire(DIETRICH, MIKHAIL, GUSTAV, DOOM_SERVANT, DOOM_GUARD, DOOM_ARCHER, DOOM_TROOPER, DOOM_WARRIOR, DOOM_KNIGHT);
		addPartyAttacked(DIETRICH, MIKHAIL, GUSTAV, DOOM_ARCHER, DOOM_KNIGHT);
		addUseSkillFinished(DIETRICH, MIKHAIL);
		addSeeSpell(DIETRICH);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equals("1001"))
		{
			if (!npc.isInMyTerritory() && Rnd.get(3) < 1)
			{
				((Attackable) npc).getAI().getAggroList().cleanAllHate();
				npc.teleportTo(npc.getSpawnLocation(), 0);
			}
			
			if (Rnd.get(5) < 1)
				((Attackable) npc).getAI().getAggroList().randomizeAttack();
		}
		else if (name.equals("1002"))
		{
			npc.doDie(npc);
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
			_damageToGustav.merge(clan.getClanId(), damage, Integer::sum);
		
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		switch (npc.getNpcId())
		{
			case DIETRICH:
				npc.broadcastNpcShout(NpcStringId.ID_1000277);
				break;
			
			case MIKHAIL:
				npc.broadcastNpcShout(NpcStringId.ID_1000276);
				break;
			
			case GUSTAV:
				npc.broadcastNpcShout(NpcStringId.ID_1000275);
				
				createOnePrivateEx(npc, MIKHAIL, 178304, -17712, -2194, 32768, 0, false);
				createOnePrivateEx(npc, DIETRICH, 178306, -17535, -2195, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178178, -17346, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178110, -17768, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178132, -17683, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178180, -17953, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178147, -17172, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178056, -17424, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178120, -17331, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178124, -17577, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178159, -18138, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178396, -18050, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178078, -17917, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178261, -17269, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178152, -18063, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178025, -17541, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_KNIGHT, 178285, -18013, -2226, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178112, -17968, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178079, -18092, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178039, -17732, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178224, -18025, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178111, -17510, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178088, -17620, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178059, -17223, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178043, -17325, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178311, -17946, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178170, -17885, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178317, -18097, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178099, -17823, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178290, -17335, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178116, -17386, -2221, 32768, 0, false);
				createOnePrivateEx(npc, DOOM_ARCHER, 178169, -17266, -2221, 32768, 0, false);
				break;
		}
		
		// Dietrich, Mikhail and Gustav are all registered to this task.
		startQuestTimer("1001", npc, null, 60000);
		
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
	public void onPartyAttacked(Npc caller, Npc called, Creature attacker, int damage)
	{
		switch (called.getNpcId())
		{
			case DIETRICH:
				if (_speller != null && Rnd.get(3) < 1 && called.getSpawn().isInMyTerritory(_speller))
					called.getAI().addCastDesire(_speller, 4238, 1, 1000000);
				
				if (called.isScriptValue(0) && called.getMaster().getStatus().getHpRatio() < 0.05)
				{
					called.setScriptValue(1);
					called.getAI().addCastDesire(called, 4235, 1, 1000000);
					called.broadcastNpcSay(NpcStringId.ID_1000280);
				}
				break;
			
			case MIKHAIL:
				if (Rnd.get(3) < 1)
					called.getAI().addCastDesire(attacker, 4237, 1, 1000000);
				
				if (called.isScriptValue(0) && called.getMaster().getStatus().getHpRatio() < 0.05)
				{
					called.setScriptValue(1);
					called.getAI().addCastDesire(called, 4235, 1, 1000000);
					called.broadcastNpcSay(NpcStringId.ID_1000279);
				}
				break;
			
			case GUSTAV:
				if (Rnd.get(3) < 1)
					called.getAI().addCastDesire(attacker, 4236, 1, 1000000);
				
				if (called.isScriptValue(0) && called.getStatus().getHpRatio() < 0.05)
				{
					called.setScriptValue(1);
					called.getAI().addCastDesire(called, 4235, 1, 1000000);
					called.broadcastNpcSay(NpcStringId.ID_1000278);
				}
				break;
		}
		super.onPartyAttacked(caller, called, attacker, damage);
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		switch (caster.getClassId())
		{
			case BISHOP, PROPHET, ELVEN_ELDER, SHILLIEN_ELDER, OVERLORD, WARCRYER:
				_speller = caster;
				break;
		}
		super.onSeeSpell(npc, caster, skill, targets, isPet);
	}
	
	@Override
	public void onUseSkillFinished(Npc npc, Creature creature, L2Skill skill, boolean success)
	{
		if (skill.getId() == 4235)
		{
			((Attackable) npc).getAI().getAggroList().cleanAllHate();
			npc.teleportTo(177134, -18807, -2263, 0);
			
			if (npc.getNpcId() == GUSTAV)
				startQuestTimer("1002", npc, null, 3000);
		}
		super.onUseSkillFinished(npc, creature, skill, success);
	}
	
	@Override
	public Clan getWinner()
	{
		// If none did damages, simply return null.
		if (_damageToGustav.isEmpty())
			return null;
		
		// Retrieve clanId who did the biggest amount of damage.
		final int clanId = Collections.max(_damageToGustav.entrySet(), Map.Entry.comparingByValue()).getKey();
		
		// Clear the Map for future usage.
		_damageToGustav.clear();
		
		// Return the Clan winner.
		return ClanTable.getInstance().getClan(clanId);
	}
	
	@Override
	public void spawnNpcs()
	{
		SpawnManager.getInstance().startSpawnTime("agit_defend_warfare_start", "34", null, null, true);
	}
	
	@Override
	public void unspawnNpcs()
	{
		SpawnManager.getInstance().stopSpawnTime("agit_defend_warfare_start", "34", null, null, true);
	}
}