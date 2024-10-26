package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.Shortcut;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.records.Timestamp;
import net.sf.l2j.gameserver.skills.L2Skill;

public class ShortCutInit extends L2GameServerPacket
{
	private final Player _player;
	private final Shortcut[] _shortcuts;
	
	public ShortCutInit(Player player)
	{
		_player = player;
		_shortcuts = player.getShortcutList().getShortcuts();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x45);
		writeD(_shortcuts.length);
		
		for (Shortcut shortcut : _shortcuts)
		{
			writeD(shortcut.getType().ordinal());
			writeD(shortcut.getSlot() + shortcut.getPage() * 12);
			
			switch (shortcut.getType())
			{
				case ITEM:
					writeD(shortcut.getId());
					writeD(shortcut.getCharacterType());
					writeD(shortcut.getSharedReuseGroup());
					
					final ItemInstance item = _player.getInventory().getItemByObjectId(shortcut.getId());
					if (item == null)
					{
						writeD(0x00);
						writeD(0x00);
						writeD(0x00);
					}
					else if (!item.isEtcItem())
					{
						writeD(0x00);
						writeD(0x00);
						writeD((item.isAugmented()) ? item.getAugmentation().getId() : 0x00);
					}
					else
					{
						final IntIntHolder[] skills = item.getEtcItem().getSkills();
						if (skills == null)
						{
							writeD(0x00);
							writeD(0x00);
						}
						else
						{
							// Retrieve the first Skill only.
							final L2Skill itemSkill = skills[0].getSkill();
							
							final Timestamp timestamp = _player.getReuseTimeStamp().get(itemSkill.getReuseHashCode());
							if (timestamp == null)
							{
								writeD(0x00);
								writeD(0x00);
							}
							else
							{
								writeD((int) (timestamp.getRemaining() / 1000L));
								writeD((int) (itemSkill.getReuseDelay() / 1000L));
							}
						}
						writeD(0x00);
					}
					break;
				
				case SKILL:
					writeD(shortcut.getId());
					writeD(shortcut.getLevel());
					writeC(0x00);
					writeD(shortcut.getCharacterType());
					break;
				
				default:
					writeD(shortcut.getId());
					writeD(shortcut.getCharacterType());
			}
		}
	}
}