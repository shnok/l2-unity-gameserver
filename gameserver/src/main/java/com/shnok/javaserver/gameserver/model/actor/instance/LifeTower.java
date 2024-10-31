package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.actor.AbstractNpcInfo.NpcInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ServerObjectInfo;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class LifeTower extends Npc
{
	public LifeTower(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isAttackableBy(Creature attacker)
	{
		if (!super.isAttackableBy(attacker))
			return false;
		
		if (!(attacker instanceof Playable))
			return false;
		
		if (getCastle() != null && getCastle().getSiege().isInProgress())
			return getPolymorphTemplate() != null && getCastle().getSiege().checkSides(attacker.getActingPlayer().getClan(), SiegeSide.ATTACKER);
		
		return false;
	}
	
	@Override
	public boolean isAttackableWithoutForceBy(Playable attacker)
	{
		return isAttackableBy(attacker);
	}
	
	@Override
	public void onInteract(Player player)
	{
		// Do nothing.
	}
	
	@Override
	public void reduceCurrentHp(double damage, Creature attacker, boolean awake, boolean isDOT, L2Skill skill)
	{
		super.reduceCurrentHp(damage, attacker, awake, isDOT, skill);
		
		if (getCastle() != null && getCastle().getSiege().isInProgress() && getPolymorphTemplate() != null && getStatus().getHp() <= 1)
		{
			unpolymorph();
			
			// If Life Control Tower amount reach 0, broadcast a message to defenders.
			if (getCastle().getAliveLifeTowerCount() == 0)
				getCastle().getSiege().announce(SystemMessageId.TOWER_DESTROYED_NO_RESURRECTION, SiegeSide.DEFENDER);
		}
	}
	
	@Override
	public void sendInfo(Player player)
	{
		if (getPolymorphTemplate() != null)
			player.sendPacket(new NpcInfo(this, player));
		else
			player.sendPacket(new ServerObjectInfo(this, player));
	}
}