package com.shnok.javaserver.gameserver.model.actor.attack;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * This class groups all attack data related to a {@link Creature}.
 * @param <T> : The {@link Playable} used as actor.
 */
public class PlayableAttack<T extends Playable> extends CreatureAttack<T>
{
	public PlayableAttack(T actor)
	{
		super(actor);
	}
	
	@Override
	public boolean canAttack(Creature target)
	{
		if (!super.canAttack(target))
			return false;
		
		if (target instanceof Playable targetPlayable)
		{
			if (_actor.isInsideZone(ZoneId.PEACE))
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.CANT_ATK_PEACEZONE));
				return false;
			}
			
			if (targetPlayable.isInsideZone(ZoneId.PEACE))
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.TARGET_IN_PEACEZONE));
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void stop()
	{
		super.stop();
		
		_actor.getAI().tryToIdle();
	}
}