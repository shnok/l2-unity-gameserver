package com.shnok.javaserver.gameserver.network.clientpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.ExAutoSoulShot;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestAutoSoulShot extends L2GameClientPacket
{
	private int _itemId;
	private int _type; // 1 = on : 0 = off;
	
	@Override
	protected void readImpl()
	{
		_itemId = readD();
		_type = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (!player.isOperating() && player.getActiveRequester() == null && !player.isDead())
		{
			final ItemInstance item = player.getInventory().getItemByItemId(_itemId);
			if (item == null)
				return;
			
			if (_type == 1)
			{
				// Fishingshots are not automatic on retail
				if (_itemId < 6535 || _itemId > 6540)
				{
					// Attempt to charge first shot on activation
					if (_itemId == 6645 || _itemId == 6646 || _itemId == 6647)
					{
						if (player.getSummon() != null)
						{
							// Cannot activate bss automation during Olympiad.
							if (_itemId == 6647 && player.isInOlympiadMode())
							{
								player.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
								return;
							}
							
							if (_itemId == 6645)
							{
								if (player.getSummon().getSoulShotsPerHit() > item.getCount())
								{
									player.sendPacket(SystemMessageId.NOT_ENOUGH_SOULSHOTS_FOR_PET);
									return;
								}
							}
							else
							{
								if (player.getSummon().getSpiritShotsPerHit() > item.getCount())
								{
									player.sendPacket(SystemMessageId.NOT_ENOUGH_SPIRITSHOTS_FOR_PET);
									return;
								}
							}
							
							// start the auto soulshot use
							player.addAutoSoulShot(_itemId);
							player.sendPacket(new ExAutoSoulShot(_itemId, _type));
							player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO).addItemName(_itemId));
							player.rechargeShots(true, true);
							player.getSummon().rechargeShots(true, true);
						}
						else
							player.sendPacket(SystemMessageId.NO_SERVITOR_CANNOT_AUTOMATE_USE);
					}
					else
					{
						// Cannot activate bss automation during Olympiad.
						if (_itemId >= 3947 && _itemId <= 3952 && player.isInOlympiadMode())
						{
							player.sendPacket(SystemMessageId.THIS_ITEM_IS_NOT_AVAILABLE_FOR_THE_OLYMPIAD_EVENT);
							return;
						}
						
						// Activate the visual effect
						player.addAutoSoulShot(_itemId);
						player.sendPacket(new ExAutoSoulShot(_itemId, _type));
						
						// start the auto soulshot use
						if (player.getActiveWeaponInstance() != null && item.getItem().getCrystalType() == player.getActiveWeaponItem().getCrystalType())
							player.rechargeShots(true, true);
						else
						{
							if ((_itemId >= 2509 && _itemId <= 2514) || (_itemId >= 3947 && _itemId <= 3952) || _itemId == 5790)
								player.sendPacket(SystemMessageId.SPIRITSHOTS_GRADE_MISMATCH);
							else
								player.sendPacket(SystemMessageId.SOULSHOTS_GRADE_MISMATCH);
						}
						
						// In both cases (match/mismatch), that message is displayed.
						player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.USE_OF_S1_WILL_BE_AUTO).addItemName(_itemId));
					}
				}
			}
			else if (_type == 0)
			{
				// cancel the auto soulshot use
				player.removeAutoSoulShot(_itemId);
				player.sendPacket(new ExAutoSoulShot(_itemId, _type));
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.AUTO_USE_OF_S1_CANCELLED).addItemName(_itemId));
			}
		}
	}
}