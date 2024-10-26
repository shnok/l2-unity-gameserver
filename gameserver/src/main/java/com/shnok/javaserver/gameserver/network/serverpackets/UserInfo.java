package com.shnok.javaserver.gameserver.network.serverpackets;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.CursedWeaponManager;
import com.shnok.javaserver.gameserver.enums.Paperdoll;
import com.shnok.javaserver.gameserver.enums.TeamType;
import com.shnok.javaserver.gameserver.enums.skills.AbnormalEffect;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Cubic;

public class UserInfo extends L2GameServerPacket
{
	private final Player _player;
	private int _relation;
	
	public UserInfo(Player player)
	{
		_player = player;
		
		_relation = _player.isClanLeader() ? 0x40 : 0;
		
		if (_player.getSiegeState() == 1)
			_relation |= 0x180;
		if (_player.getSiegeState() == 2)
			_relation |= 0x80;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x04);
		writeLoc(_player.getPosition());
		writeD(_player.getHeading());
		writeD(_player.getObjectId());
		writeS((_player.getPolymorphTemplate() != null) ? _player.getPolymorphTemplate().getName() : _player.getName());
		writeD(_player.getRace().ordinal());
		writeD(_player.getAppearance().getSex().ordinal());
		writeD((_player.getClassIndex() == 0) ? _player.getClassId().getId() : _player.getBaseClass());
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
		writeD(_player.getActiveWeaponItem() != null ? 40 : 20);
		
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
		writeD(_player.getInventory().getAugmentationIdFrom(Paperdoll.RHAND));
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
		writeD(_player.getInventory().getAugmentationIdFrom(Paperdoll.LHAND));
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		writeH(0x00);
		
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
		writeD(_player.getPvpFlag());
		writeD(_player.getKarma());
		
		final int runSpd = _player.getStatus().getBaseRunSpeed();
		final int walkSpd = _player.getStatus().getBaseWalkSpeed();
		final int swimSpd = _player.getStatus().getBaseSwimSpeed();
		
		writeD(runSpd);
		writeD(walkSpd);
		writeD(swimSpd);
		writeD(swimSpd);
		writeD(0);
		writeD(0);
		writeD((_player.isFlying()) ? runSpd : 0);
		writeD((_player.isFlying()) ? walkSpd : 0);
		
		writeF(_player.getStatus().getMovementSpeedMultiplier());
		writeF(_player.getStatus().getAttackSpeedMultiplier());
		
		final Summon summon = _player.getSummon();
		if (_player.isMounted() && summon != null)
		{
			writeF(summon.getCollisionRadius());
			writeF(summon.getCollisionHeight());
		}
		else
		{
			writeF(_player.getCollisionRadius());
			writeF(_player.getCollisionHeight());
		}
		
		writeD(_player.getAppearance().getHairStyle());
		writeD(_player.getAppearance().getHairColor());
		writeD(_player.getAppearance().getFace());
		writeD((_player.isGM()) ? 1 : 0);
		
		writeS((_player.getPolymorphTemplate() != null) ? "Morphed" : _player.getTitle());
		
		writeD(_player.getClanId());
		writeD(_player.getClanCrestId());
		writeD(_player.getAllyId());
		writeD(_player.getAllyCrestId());
		writeD(_relation);
		writeC(_player.getMountType());
		writeC(_player.getOperateType().getId());
		writeC((_player.hasCrystallize()) ? 1 : 0);
		writeD(_player.getPkKills());
		writeD(_player.getPvpKills());
		
		writeH(_player.getCubicList().size());
		for (final Cubic cubic : _player.getCubicList())
			writeH(cubic.getId());
		
		writeC((_player.isInPartyMatchRoom()) ? 1 : 0);
		writeD((!_player.getAppearance().isVisible() && _player.isGM()) ? (_player.getAbnormalEffect() | AbnormalEffect.STEALTH.getMask()) : _player.getAbnormalEffect());
		writeC(0x00);
		writeD(_player.getClanPrivileges());
		writeH(_player.getRecomLeft());
		writeH(_player.getRecomHave());
		writeD((_player.getMountNpcId() > 0) ? _player.getMountNpcId() + 1000000 : 0);
		writeH(_player.getStatus().getInventoryLimit());
		writeD(_player.getClassId().getId());
		writeD(0x00);
		writeD(_player.getStatus().getMaxCp());
		writeD((int) _player.getStatus().getCp());
		writeC((_player.isMounted()) ? 0 : _player.getEnchantEffect());
		writeC((Config.PLAYER_SPAWN_PROTECTION > 0 && _player.isSpawnProtected()) ? TeamType.BLUE.getId() : _player.getTeam().getId());
		writeD(_player.getClanCrestLargeId());
		writeC((_player.isNoble()) ? 1 : 0);
		writeC((_player.isHero() || (_player.isGM() && Config.GM_HERO_AURA)) ? 1 : 0);
		writeC((_player.isFishing()) ? 1 : 0);
		writeLoc(_player.getFishingStance().getLoc());
		writeD(_player.getAppearance().getNameColor());
		writeC((_player.isRunning()) ? 0x01 : 0x00);
		writeD(_player.getPledgeClass());
		writeD(_player.getPledgeType());
		writeD(_player.getAppearance().getTitleColor());
		writeD(CursedWeaponManager.getInstance().getCurrentStage(_player.getCursedWeaponEquippedId()));
	}
}