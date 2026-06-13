package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.data.manager.CursedWeaponManager;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.entity.CursedWeapon;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.model.records.CursedWeaponInfo;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExCursedWeaponLocation;

public final class RequestCursedWeaponLocation extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Do nothing.
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		final List<CursedWeaponInfo> list = new ArrayList<>();
		for (CursedWeapon cw : CursedWeaponManager.getInstance().getCursedWeapons())
		{
			if (!cw.isActive())
				continue;
			
			final Location loc = cw.getWorldPosition();
			if (loc != null)
				list.add(new CursedWeaponInfo(loc, cw.getItemId(), (cw.isActivated()) ? 1 : 0));
		}
		
		if (!list.isEmpty())
			player.sendPacket(new ExCursedWeaponLocation(list));
	}
}