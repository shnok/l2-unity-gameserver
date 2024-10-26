package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.concurrent.Future;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class SiegeFlag extends Npc
{
	private Clan _clan;
	private Future<?> _task;
	
	public SiegeFlag(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
			return false;
		
		// Reset clan flag to null.
		_clan.setFlag(null);
		
		return true;
	}
	
	@Override
	public void deleteMe()
	{
		// Stop the task.
		if (_task != null)
		{
			_task.cancel(false);
			_task = null;
		}
		
		super.deleteMe();
	}
	
	@Override
	public void onInteract(Player player)
	{
		// Do nothing.
	}
	
	@Override
	public void reduceCurrentHp(double damage, Creature attacker, L2Skill skill)
	{
		// Any SiegeSummon can hurt SiegeFlag (excepted Swoop Cannon - anti-infantery summon).
		if (attacker instanceof SiegeSummon siegeSummon && siegeSummon.getNpcId() == SiegeSummon.SWOOP_CANNON_ID)
			return;
		
		// Send warning to owners of headquarters that theirs base is under attack.
		if (_task == null)
		{
			_task = ThreadPool.schedule(() -> _task = null, 30000);
			
			_clan.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.BASE_UNDER_ATTACK));
		}
		super.reduceCurrentHp(damage, attacker, skill);
	}
	
	@Override
	public void addFuncsToNewCharacter()
	{
		// Do nothing.
	}
	
	@Override
	public boolean canBeHealed()
	{
		return false;
	}
	
	@Override
	public boolean isLethalable()
	{
		return false;
	}
	
	public void setClan(Clan clan)
	{
		_clan = clan;
		_clan.setFlag(this);
	}
}