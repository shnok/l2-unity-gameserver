package com.shnok.javaserver.gameserver.skills.l2skills;

import net.sf.l2j.commons.data.StatSet;

import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.data.manager.ClanHallManager;
import net.sf.l2j.gameserver.enums.SiegeSide;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.SiegeFlag;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.residence.castle.Siege;
import net.sf.l2j.gameserver.model.residence.clanhall.ClanHallSiege;
import net.sf.l2j.gameserver.model.spawn.Spawn;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

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