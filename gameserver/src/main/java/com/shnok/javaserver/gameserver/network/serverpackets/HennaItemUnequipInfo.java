package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Henna;

public class HennaItemUnequipInfo extends L2GameServerPacket
{
	private final Henna _henna;
	private final int _adena;
	private final int _int;
	private final int _str;
	private final int _con;
	private final int _men;
	private final int _dex;
	private final int _wit;
	
	public HennaItemUnequipInfo(Henna henna, Player player)
	{
		_henna = henna;
		_adena = player.getAdena();
		_int = player.getStatus().getINT();
		_str = player.getStatus().getSTR();
		_con = player.getStatus().getCON();
		_men = player.getStatus().getMEN();
		_dex = player.getStatus().getDEX();
		_wit = player.getStatus().getWIT();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe6);
		writeD(_henna.symbolId());
		writeD(_henna.dyeId());
		writeD(Henna.REMOVE_AMOUNT);
		writeD(_henna.getRemovePrice());
		writeD(1);
		writeD(_adena);
		writeD(_int);
		writeC(_int - _henna.INT());
		writeD(_str);
		writeC(_str - _henna.STR());
		writeD(_con);
		writeC(_con - _henna.CON());
		writeD(_men);
		writeC(_men - _henna.MEN());
		writeD(_dex);
		writeC(_dex - _henna.DEX());
		writeD(_wit);
		writeC(_wit - _henna.WIT());
	}
}