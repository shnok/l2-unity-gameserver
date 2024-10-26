package com.shnok.javaserver.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;

import net.sf.l2j.commons.lang.StringUtil;

import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.taskmanager.DecayTaskManager;

public class AdminManage implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_cancel",
		"admin_heal",
		"admin_kill",
		"admin_res"
	};
	
	@Override
	public void useAdminCommand(String command, Player player)
	{
		final StringTokenizer st = new StringTokenizer(command, " ");
		st.nextToken();
		
		String name = null;
		int radius = 0;
		
		final int paramCount = st.countTokens();
		if (paramCount == 2)
		{
			name = st.nextToken();
			radius = Integer.parseInt(st.nextToken());
		}
		else if (paramCount == 1)
		{
			final String paramToTest = st.nextToken();
			if (StringUtil.isDigit(paramToTest))
				radius = Integer.parseInt(paramToTest);
			else
				name = paramToTest;
		}
		
		// Retrieve the target if it's a Creature, or self otherwise.
		Creature targetCreature = getTargetCreature(player, true);
		
		// If name exists and the Player can be retrieved, he becomes the new target.
		if (!StringUtil.isEmpty(name))
		{
			final Player worldPlayer = World.getInstance().getPlayer(name);
			if (worldPlayer != null)
				targetCreature = worldPlayer;
		}
		
		// After all tests, if command target is null, abort.
		if (targetCreature == null)
		{
			player.sendPacket(SystemMessageId.INVALID_TARGET);
			return;
		}
		
		if (command.startsWith("admin_cancel"))
		{
			// Stop effects over target.
			targetCreature.stopAllEffects();
			
			// Stop effects over all creatures surrounding the target.
			if (radius > 0)
				targetCreature.forEachKnownTypeInRadius(Creature.class, radius, Creature::stopAllEffects);
		}
		else if (command.startsWith("admin_heal"))
		{
			// Heal target.
			heal(targetCreature);
			
			// Heal all creatures surrounding the target.
			if (radius > 0)
				targetCreature.forEachKnownTypeInRadius(Creature.class, radius, c -> heal(c));
		}
		else if (command.startsWith("admin_kill"))
		{
			// Kill target.
			kill(targetCreature, player);
			
			// Kill all creatures surrounding the target.
			if (radius > 0)
				targetCreature.forEachKnownTypeInRadius(Creature.class, radius, c -> kill(c, player));
		}
		else if (command.startsWith("admin_res"))
		{
			// Resurrect target.
			resurrect(targetCreature);
			
			// Resurrect all creatures surrounding the target.
			if (radius > 0)
				targetCreature.forEachKnownTypeInRadius(Creature.class, radius, c -> resurrect(c));
		}
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
	
	private static void heal(Creature creature)
	{
		if (creature.isDead())
			return;
		
		if (creature instanceof Player player)
			player.getStatus().setMaxCpHpMp();
		else
			creature.getStatus().setMaxHpMp();
	}
	
	private static void kill(Creature creature, Player player)
	{
		if (creature.isDead() || creature == player)
			return;
		
		creature.stopAllEffects();
		creature.reduceCurrentHp(creature.getStatus().getMaxHp() + creature.getStatus().getMaxCp() + 1d, player, null);
	}
	
	private static void resurrect(Creature creature)
	{
		if (!creature.isDead())
			return;
		
		// If the target is a player, then restore the XP lost on death.
		if (creature instanceof Player player)
			player.restoreExp(100.0);
		// If the target is an NPC, then abort it's auto decay and respawn.
		else
			DecayTaskManager.getInstance().cancel(creature);
		
		creature.doRevive();
	}
}