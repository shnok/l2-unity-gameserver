package com.shnok.javaserver.gameserver.model.actor.status;

import com.shnok.javaserver.gameserver.enums.duels.DuelState;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class NpcStatus<T extends Npc> extends CreatureStatus<T>
{
	public NpcStatus(T actor)
	{
		super(actor);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHpConsumption)
	{
		if (_actor.isDead())
			return;
		
		if (attacker != null)
		{
			final Player attackerPlayer = attacker.getActingPlayer();
			if (attackerPlayer != null && attackerPlayer.isInDuel())
				attackerPlayer.setDuelState(DuelState.INTERRUPTED);
		}
		
		super.reduceHp(value, attacker, awake, isDOT, isHpConsumption);
	}
	
	@Override
	public int getLevel()
	{
		return _actor.getTemplate().getLevel();
	}
	
	@Override
	public int getPhysicalAttackRange()
	{
		return _actor.getTemplate().getBaseAttackRange();
	}
}