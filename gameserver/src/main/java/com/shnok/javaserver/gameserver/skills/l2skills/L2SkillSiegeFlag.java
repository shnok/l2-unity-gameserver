package com.shnok.javaserver.gameserver.skills.l2skills;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.SiegeFlag;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.castle.Siege;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHallSiege;
import com.shnok.javaserver.gameserver.model.spawn.Spawn;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class L2SkillSiegeFlag extends L2Skill
{
	private final boolean _isAdvanced;
	
	public L2SkillSiegeFlag(StatSet set)
	{
		super(set);
		
		_isAdvanced = set.getBool("isAdvanced", false);
	}
	
	@Override
	public void useSkill(Creature creature, WorldObject[] targets)
	{
		if (!(creature instanceof Player player))
			return;
		
		if (!check(player, true))
			return;
		
		final Clan clan = player.getClan();
		if (clan == null)
			return;
		
		// Template initialization
		final StatSet npcDat = new StatSet();
		
		npcDat.set("id", 35062);
		npcDat.set("type", "SiegeFlag");
		
		npcDat.set("name", clan.getName());
		npcDat.set("usingServerSideName", true);
		
		npcDat.set("hp", (_isAdvanced) ? 100000 : 50000);
		npcDat.set("mp", 0);
		
		npcDat.set("pAtk", 0);
		npcDat.set("mAtk", 0);
		npcDat.set("pDef", 500);
		npcDat.set("mDef", 500);
		
		npcDat.set("runSpd", 0); // Have to keep this, static object MUST BE 0 (critical error otherwise).
		
		npcDat.set("radius", 10);
		npcDat.set("height", 80);
		
		npcDat.set("baseDamageRange", "0;0;80;120");
		
		try
		{
			// Create spawn.
			final Spawn spawn = new Spawn(new NpcTemplate(npcDat));
			spawn.setLoc(player.getPosition());
			
			// Spawn NPC.
			final SiegeFlag flag = (SiegeFlag) spawn.doSpawn(false);
			flag.setClan(clan);
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't spawn SiegeFlag for {}.", e, clan.getName());
		}
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @param isCheckOnly : If false, send a notification to the {@link Player} telling him why the operation failed.
	 * @return True if the {@link Player} can place a {@link SiegeFlag}.
	 */
	public static boolean check(Player player, boolean isCheckOnly)
	{
		boolean isAttackerUnderActiveSiege = false;
		
		// Check first if an active Siege is under process.
		final Siege siege = CastleManager.getInstance().getActiveSiege(player);
		if (siege != null)
			isAttackerUnderActiveSiege = siege.checkSide(player.getClan(), SiegeSide.ATTACKER);
		else
		{
			// If no Siege, check ClanHallSiege.
			final ClanHallSiege chs = ClanHallManager.getInstance().getActiveSiege(player);
			if (chs != null)
				isAttackerUnderActiveSiege = chs.checkSide(player.getClan(), SiegeSide.ATTACKER);
		}
		
		SystemMessage sm = null;
		if (!isAttackerUnderActiveSiege)
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CANNOT_BE_USED).addSkillName(247);
		else if (!player.isClanLeader())
			sm = SystemMessage.getSystemMessage(SystemMessageId.ONLY_CLAN_LEADER_CAN_ISSUE_COMMANDS);
		else if (player.getClan().getFlag() != null)
			sm = SystemMessage.getSystemMessage(SystemMessageId.NOT_ANOTHER_HEADQUARTERS);
		else if (!player.isInsideZone(ZoneId.HQ))
			sm = SystemMessage.getSystemMessage(SystemMessageId.NOT_SET_UP_BASE_HERE);
		else if (!player.getKnownTypeInRadius(SiegeFlag.class, 400).isEmpty())
			sm = SystemMessage.getSystemMessage(SystemMessageId.HEADQUARTERS_TOO_CLOSE);
		
		if (sm != null && !isCheckOnly)
			player.sendPacket(sm);
		
		return sm == null;
	}
}