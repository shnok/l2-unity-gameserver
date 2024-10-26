package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.actor.Player;

public class GMViewCharacterInfo extends L2GameServerPacket
{
	private final Player _player;
	
	public GMViewCharacterInfo(Player player)
	{
		_player = player;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x8f);
		
		writeD(_player.getX());
		writeD(_player.getY());
		writeD(_player.getZ());
		writeD(_player.getHeading());
		writeD(_player.getObjectId());
		writeS(_player.getName());
		writeD(_player.getRace().ordinal());
		writeD(_player.getAppearance().getSex().ordinal());
		writeD(_player.getClassId().getId());
		writeD(_player.getStatus().getLevel());
		writeQ(_player.getStatus().getExp());
		writeD(_player.getStatus().getSTR());
		writeD(_player.getStatus().getDEX());
		writeD(_player.getStatus().getCON());
		writeD(_player.getStatus().getINT());
		writeD(_player.getStatus().getWIT());
		writeD(_player.getStatus().getMEN());
		writeD(_player.getStatus().getMaxHp());
		writeD((int) _player.getStatus().getHp());
		writeD(_player.getStatus().getMaxMp());
		writeD((int) _player.getStatus().getMp());
		writeD(_player.getStatus().getSp());
		writeD(_player.getCurrentWeight());
		writeD(_player.getWeightLimit());
		writeD(0x28); // unknown
		
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.HAIRALL));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.REAR));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.LEAR));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.NECK));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.RFINGER));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.LFINGER));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.HEAD));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.RHAND));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.LHAND));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.GLOVES));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.CHEST));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.LEGS));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.FEET));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.CLOAK));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.RHAND));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.HAIR));
		writeD(_player.getInventory().getItemObjectIdFrom(Paperdoll.FACE));
		
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.HAIRALL));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.REAR));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.LEAR));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.NECK));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.RFINGER));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.LFINGER));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.HEAD));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.RHAND));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.LHAND));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.GLOVES));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.CHEST));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.LEGS));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.FEET));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.CLOAK));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.RHAND));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.HAIR));
		writeD(_player.getInventory().getItemIdFrom(Paperdoll.FACE));
		
		// c6 new h's
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		// end of c6 new h's
		
		writeD(_player.getStatus().getPAtk(null));
		writeD(_player.getStatus().getPAtkSpd());
		writeD(_player.getStatus().getPDef(null));
		writeD(_player.getStatus().getEvasionRate(null));
		writeD(_player.getStatus().getAccuracy());
		writeD(_player.getStatus().getCriticalHit(null, null));
		writeD(_player.getStatus().getMAtk(null, null));
		
		writeD(_player.getStatus().getMAtkSpd());
		writeD(_player.getStatus().getPAtkSpd());
		
		writeD(_player.getStatus().getMDef(null, null));
		
		writeD(_player.getPvpFlag()); // 0-non-pvp 1-pvp = violett name
		writeD(_player.getKarma());
		
		int _runSpd = _player.getStatus().getBaseRunSpeed();
		int _walkSpd = _player.getStatus().getBaseWalkSpeed();
		int _swimSpd = _player.getStatus().getBaseSwimSpeed();
		writeD(_runSpd); // base run speed
		writeD(_walkSpd); // base walk speed
		writeD(_swimSpd); // swim run speed
		writeD(_swimSpd); // swim walk speed
		writeD(0);
		writeD(0);
		writeD(_player.isFlying() ? _runSpd : 0); // fly run speed
		writeD(_player.isFlying() ? _walkSpd : 0); // fly walk speed
		writeF(_player.getStatus().getMovementSpeedMultiplier()); // run speed multiplier
		writeF(_player.getStatus().getAttackSpeedMultiplier()); // attack speed multiplier
		
		writeF(_player.getCollisionRadius()); // scale
		writeF(_player.getCollisionHeight()); // y offset ??!? fem dwarf 4033
		writeD(_player.getAppearance().getHairStyle());
		writeD(_player.getAppearance().getHairColor());
		writeD(_player.getAppearance().getFace());
		writeD(_player.isGM() ? 0x01 : 0x00); // builder level
		
		writeS(_player.getTitle());
		writeD(_player.getClanId()); // pledge id
		writeD(_player.getClanCrestId()); // pledge crest id
		writeD(_player.getAllyId()); // ally id
		writeC(_player.getMountType()); // mount type
		writeC(_player.getOperateType().getId());
		writeC(_player.hasCrystallize() ? 1 : 0);
		writeD(_player.getPkKills());
		writeD(_player.getPvpKills());
		
		writeH(_player.getRecomLeft());
		writeH(_player.getRecomHave()); // Blue value for name (0 = white, 255 = pure blue)
		writeD(_player.getClassId().getId());
		writeD(0x00); // special effects? circles around player...
		writeD(_player.getStatus().getMaxCp());
		writeD((int) _player.getStatus().getCp());
		
		writeC(_player.isRunning() ? 0x01 : 0x00); // changes the Speed display on Status Window
		
		writeC(321);
		
		writeD(_player.getPledgeClass()); // changes the text above CP on Status Window
		
		writeC(_player.isNoble() ? 0x01 : 0x00);
		writeC(_player.isHero() ? 0x01 : 0x00);
		
		writeD(_player.getAppearance().getNameColor());
		writeD(_player.getAppearance().getTitleColor());
	}
}