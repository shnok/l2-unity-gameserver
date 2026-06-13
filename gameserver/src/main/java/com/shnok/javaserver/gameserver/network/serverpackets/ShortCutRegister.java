package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.Shortcut;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.records.Timestamp;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class ShortCutRegister extends L2GameServerPacket
{
	private final Player _player;
	private final Shortcut _shortcut;
	
	public ShortCutRegister(Player player, Shortcut shortcut)
	{
		_player = player;
		_shortcut = shortcut;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x44);
		
		writeD(_shortcut.getType().ordinal());
		writeD(_shortcut.getSlot() + _shortcut.getPage() * 12);
		
		switch (_shortcut.getType())
		{
			case ITEM:
				writeD(_shortcut.getId());
				writeD(_shortcut.getCharacterType());
				writeD(_shortcut.getSharedReuseGroup());
				
				final ItemInstance item = _player.getInventory().getItemByObjectId(_shortcut.getId());
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
				writeD(_shortcut.getId());
				writeD(_shortcut.getLevel());
				writeC(0x00);
				writeD(_shortcut.getCharacterType());
				break;
			
			default:
				writeD(_shortcut.getId());
				writeD(_shortcut.getCharacterType());
		}
	}
}