package com.shnok.javaserver.gameserver.model.actor.status;

import com.shnok.javaserver.gameserver.enums.duels.DuelState;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Servitor;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public class SummonStatus<T extends Summon> extends PlayableStatus<T>
{
	public SummonStatus(T actor)
	{
		super(actor);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false);
	}
	
	@Override
	public void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption)
	{
		if (_actor.isDead())
			return;
		
		final Player owner = _actor.getOwner();
		
		// We deny the duel, no matter if damage has been done or not.
		if (attacker != null)
		{
			final Player attackerPlayer = attacker.getActingPlayer();
			if (attackerPlayer != null && (owner == null || owner.getDuelId() != attackerPlayer.getDuelId()))
				attackerPlayer.setDuelState(DuelState.INTERRUPTED);
		}
		
		super.reduceHp(value, attacker, awake, isDOT, isHPConsumption);
		
		// Since damages have been done, we can send damage message and EVT_ATTACKED notification.
		if (attacker != null)
		{
			if (!isDOT && owner != null)
				owner.sendPacket(SystemMessage.getSystemMessage((_actor instanceof Servitor) ? SystemMessageId.SUMMON_RECEIVED_DAMAGE_S2_BY_S1 : SystemMessageId.PET_RECEIVED_S2_DAMAGE_BY_S1).addCharName(attacker).addNumber((int) value));
		}
	}
	
	@Override
	public void broadcastStatusUpdate()
	{
		super.broadcastStatusUpdate();
		
		_actor.updateAndBroadcastStatus(1);
	}
	
	@Override
	public int getLevel()
	{
		return _actor.getTemplate().getLevel();
	}
	
}