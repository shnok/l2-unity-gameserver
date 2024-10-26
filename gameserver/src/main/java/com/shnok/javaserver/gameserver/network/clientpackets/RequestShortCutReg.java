package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.enums.ShortcutType;
import com.shnok.javaserver.gameserver.model.Shortcut;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.ShortCutRegister;

public final class RequestShortCutReg extends L2GameClientPacket
{
	private int _type;
	private int _id;
	private int _slot;
	private int _page;
	private int _characterType;
	
	@Override
	protected void readImpl()
	{
		_type = readD();
		int slot = readD();
		_id = readD();
		_characterType = readD();
		
		_slot = slot % 12;
		_page = slot / 12;
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (_page < 0 || _page > 10)
			return;
		
		if (_type < 1 || _type > ShortcutType.VALUES.length)
			return;
		
		final ShortcutType type = ShortcutType.VALUES[_type];
		
		switch (type)
		{
			case ITEM, ACTION, MACRO, RECIPE:
				Shortcut shortcut = new Shortcut(_slot, _page, type, _id, -1, _characterType);
				sendPacket(new ShortCutRegister(player, shortcut));
				player.getShortcutList().addShortcut(shortcut);
				break;
			
			case SKILL:
				final int level = player.getSkillLevel(_id);
				if (level > 0)
				{
					shortcut = new Shortcut(_slot, _page, type, _id, level, _characterType);
					sendPacket(new ShortCutRegister(player, shortcut));
					player.getShortcutList().addShortcut(shortcut);
				}
				break;
		}
	}
}