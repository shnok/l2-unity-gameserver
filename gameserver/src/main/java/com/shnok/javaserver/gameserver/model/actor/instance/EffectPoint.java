package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.TargetUnselected;

public class EffectPoint extends Npc
{
	private final Player _owner;
	
	public EffectPoint(int objectId, NpcTemplate template, Creature owner)
	{
		super(objectId, template);
		
		_owner = (owner == null) ? null : owner.getActingPlayer();
	}
	
	@Override
	public Player getActingPlayer()
	{
		return _owner;
	}
	
	@Override
	public void onAction(Player player, boolean isCtrlPressed, boolean isShiftPressed)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public void onTarget(Player player, boolean isShiftPressed)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		player.sendPacket(new TargetUnselected(player));
	}
	
	@Override
	public boolean isAttackableBy(Creature attacker)
	{
		return false;
	}
}