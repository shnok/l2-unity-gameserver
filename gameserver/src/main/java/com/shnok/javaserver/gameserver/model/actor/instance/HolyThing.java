package com.shnok.javaserver.gameserver.model.actor.instance;

import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.network.serverpackets.ServerObjectInfo;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class HolyThing extends Folk
{
	public HolyThing(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public boolean isAttackableBy(Creature attacker)
	{
		return false;
	}
	
	@Override
	public void reduceCurrentHp(double damage, Creature attacker, L2Skill skill)
	{
		// Do nothing.
	}
	
	@Override
	public void reduceCurrentHp(double damage, Creature attacker, boolean awake, boolean isDOT, L2Skill skill)
	{
		// Do nothing.
	}
	
	@Override
	public void onInteract(Player player)
	{
		// Do nothing.
	}
	
	@Override
	public void sendInfo(Player player)
	{
		player.sendPacket(new ServerObjectInfo(this, player));
	}
}