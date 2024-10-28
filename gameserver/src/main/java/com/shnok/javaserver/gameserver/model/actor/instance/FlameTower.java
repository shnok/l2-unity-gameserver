package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.List;

import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.CastleZoneType;
import com.shnok.javaserver.gameserver.model.zone.type.subtype.ZoneType;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.AbstractNpcInfo.NpcInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ServerObjectInfo;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class FlameTower extends Npc
{
	private int _upgradeLevel;
	private List<Integer> _zoneList;
	
	public FlameTower(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public void deleteMe()
	{
		enableZones(false);
		
		super.deleteMe();
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
			enableZones(false);
			
			// Message occurs only if the trap was triggered first.
			if (_zoneList != null && _upgradeLevel != 0)
				getCastle().getSiege().announce(SystemMessageId.A_TRAP_DEVICE_HAS_BEEN_STOPPED, SiegeSide.DEFENDER);
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
	
	public final void enableZones(boolean state)
	{
		if (_zoneList != null && _upgradeLevel != 0)
		{
			final int maxIndex = _upgradeLevel * 2;
			for (int i = 0; i < maxIndex; i++)
			{
				final ZoneType zone = ZoneManager.getInstance().getZoneById(_zoneList.get(i));
				if (zone instanceof CastleZoneType czt)
					czt.setEnabled(state);
			}
		}
	}
	
	public final void setUpgradeLevel(int level)
	{
		_upgradeLevel = level;
	}
	
	public final void setZoneList(List<Integer> list)
	{
		_zoneList = list;
		
		enableZones(true);
	}
}