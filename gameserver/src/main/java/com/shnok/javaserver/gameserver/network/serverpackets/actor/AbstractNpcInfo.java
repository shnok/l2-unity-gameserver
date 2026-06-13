package com.shnok.javaserver.gameserver.network.serverpackets.actor;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;

public abstract class AbstractNpcInfo extends L2GameServerPacket
{
	protected int _x;
	protected int _y;
	protected int _z;
	protected int _heading;
	
	protected int _idTemplate;
	
	protected boolean _isAttackable;
	protected boolean _isSummoned;
	
	protected int _mAtkSpd;
	protected int _pAtkSpd;
	protected int _runSpd;
	protected int _walkSpd;
	
	protected int _rhand;
	protected int _lhand;
	protected int _chest;
	protected int _enchantEffect;
	
	protected double _collisionHeight;
	protected double _collisionRadius;
	
	protected int _clanCrest;
	protected int _allyCrest;
	protected int _allyId;
	protected int _clanId;
	
	protected String _name = "";
	protected String _title = "";
	
	protected AbstractNpcInfo(Creature creature)
	{
		_x = creature.getX();
		_y = creature.getY();
		_z = creature.getZ();
		_heading = creature.getHeading();
		
		_isSummoned = creature.isShowSummonAnimation();
		
		_mAtkSpd = creature.getStatus().getMAtkSpd();
		_pAtkSpd = creature.getStatus().getPAtkSpd();
		_runSpd = creature.getStatus().getBaseRunSpeed();
		_walkSpd = creature.getStatus().getBaseWalkSpeed();
	}
	
	/**
	 * Packet for Npcs
	 */
	public static class NpcInfo extends AbstractNpcInfo
	{
		private final Npc _npc;
		
		public NpcInfo(Npc npc, Player attacker)
		{
			super(npc);
			
			_npc = npc;
			
			_enchantEffect = _npc.getEnchantEffect();
			_isAttackable = _npc.isAttackableWithoutForceBy(attacker);
			
			// Support for polymorph.
			if (_npc.getPolymorphTemplate() != null)
			{
				_idTemplate = _npc.getPolymorphTemplate().getIdTemplate();
				
				_rhand = _npc.getPolymorphTemplate().getRightHand();
				_lhand = _npc.getPolymorphTemplate().getLeftHand();
				
				_collisionHeight = _npc.getPolymorphTemplate().getCollisionHeight();
				_collisionRadius = _npc.getPolymorphTemplate().getCollisionRadius();
			}
			else
			{
				_idTemplate = _npc.getTemplate().getIdTemplate();
				
				_rhand = _npc.getRightHandItemId();
				_lhand = _npc.getLeftHandItemId();
				
				_collisionHeight = _npc.getCollisionHeight();
				_collisionRadius = _npc.getCollisionRadius();
			}
			
			if (_npc.getTemplate().isUsingServerSideName())
				_name = _npc.getName();
			
			if (_npc.getTemplate().isUsingServerSideTitle())
				_title = _npc.getTitle();
			
			if (Config.SHOW_NPC_LVL && _npc instanceof Monster monster)
				_title = "Lv " + monster.getStatus().getLevel() + (monster.getTemplate().getAggroRange() > 0 ? "* " : " ") + _title;
			
			// NPC crest system
			if (Config.SHOW_NPC_CREST)
			{
				if (_npc.getCastle() != null && _npc.getCastle().getOwnerId() != 0)
				{
					Clan clan = ClanTable.getInstance().getClan(_npc.getCastle().getOwnerId());
					_clanCrest = clan.getCrestId();
					_clanId = clan.getClanId();
					_allyCrest = clan.getAllyCrestId();
					_allyId = clan.getAllyId();
				}
				else if (_npc.getClanId() != 0)
				{
					Clan clan = ClanTable.getInstance().getClan(_npc.getClanId());
					_clanCrest = clan.getCrestId();
					_clanId = clan.getClanId();
					_allyCrest = clan.getAllyCrestId();
					_allyId = clan.getAllyId();
				}
			}
		}
		
		@Override
		protected void writeImpl()
		{
			writeC(0x16);
			
			writeD(_npc.getObjectId());
			writeD(_idTemplate + 1000000);
			writeD(_isAttackable ? 1 : 0);
			
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			
			writeD(0x00);
			
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			
			writeF(_npc.getStatus().getMovementSpeedMultiplier());
			writeF(_npc.getStatus().getAttackSpeedMultiplier());
			
			writeF(_collisionRadius);
			writeF(_collisionHeight);
			
			writeD(_rhand);
			writeD(_chest);
			writeD(_lhand);
			
			writeC(1); // name above char
			writeC(_npc.isRunning() ? 1 : 0);
			writeC(_npc.isInCombat() ? 1 : 0);
			writeC(_npc.isAlikeDead() ? 1 : 0);
			writeC(_isSummoned ? 2 : 2);
			
			writeS(_name);
			writeS(_title);
			
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			
			writeD(_npc.getAbnormalEffect());
			
			writeD(_clanId);
			writeD(_clanCrest);
			writeD(_allyId);
			writeD(_allyCrest);
			
			writeC(_npc.getMove().getMoveType().getId());
			writeC(0x00);
			
			writeF(_collisionRadius);
			writeF(_collisionHeight);
			
			writeD(_enchantEffect);
			writeD(_npc.isFlying() ? 1 : 0);

			writeD(_npc.getTemplate().getBaseAttackRange());
			writeD(_npc.getStatus().getMaxHp());
			writeD(_npc.getStatus().getLevel());
		}
	}
	
	/**
	 * Packet for summons
	 */
	public static class SummonInfo extends AbstractNpcInfo
	{
		private final Summon _summon;
		private final Player _owner;
		private final int _summonAnimation;
		
		public SummonInfo(Summon summon, Player attacker, int val)
		{
			super(summon);
			
			_summon = summon;
			_owner = _summon.getOwner();
			
			_summonAnimation = (_summon.isShowSummonAnimation()) ? 2 : val;
			
			_isAttackable = _summon.isAttackableWithoutForceBy(attacker);
			_rhand = _summon.getWeapon();
			_chest = _summon.getArmor();
			_title = (_owner == null || !_owner.isOnline()) ? "" : _owner.getName();
			_idTemplate = _summon.getTemplate().getIdTemplate();
			
			_collisionHeight = _summon.getCollisionHeight();
			_collisionRadius = _summon.getCollisionRadius();
			
			// NPC crest system
			if (Config.SHOW_SUMMON_CREST && _owner != null && _owner.getClan() != null)
			{
				Clan clan = ClanTable.getInstance().getClan(_owner.getClanId());
				_clanCrest = clan.getCrestId();
				_clanId = clan.getClanId();
				_allyCrest = clan.getAllyCrestId();
				_allyId = clan.getAllyId();
			}
		}
		
		@Override
		protected void writeImpl()
		{
			if (_owner != null && !_owner.getAppearance().isVisible() && _owner != getClient().getPlayer())
				return;
			
			writeC(0x16);
			
			writeD(_summon.getObjectId());
			writeD(_idTemplate + 1000000);
			writeD(_isAttackable ? 1 : 0);
			
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			
			writeD(0x00);
			
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			
			writeF(_summon.getStatus().getMovementSpeedMultiplier());
			writeF(_summon.getStatus().getAttackSpeedMultiplier());
			
			writeF(_collisionRadius);
			writeF(_collisionHeight);
			
			writeD(_rhand);
			writeD(_chest);
			writeD(_lhand);
			
			writeC(1); // name above char
			writeC(_summon.isRunning() ? 1 : 0);
			writeC(_summon.isInCombat() ? 1 : 0);
			writeC(_summon.isAlikeDead() ? 1 : 0);
			writeC(_summonAnimation);
			
			writeS(_name);
			writeS(_title);
			
			writeD(0x01);
			writeD(_summon.getPvpFlag());
			writeD(_summon.getKarma());
			
			writeD(_summon.getAbnormalEffect());
			
			writeD(_clanId);
			writeD(_clanCrest);
			writeD(_allyId);
			writeD(_allyCrest);
			
			writeC(_summon.getMove().getMoveType().getId());
			writeC(_summon.getTeam().getId());
			
			writeF(_collisionRadius);
			writeF(_collisionHeight);
			
			writeD(_enchantEffect);
			writeD(0x00);

			writeD(_summon.getStatus().getPhysicalAttackRange());
		}
	}
	
	/**
	 * Packet for morphed PCs
	 */
	public static class PcMorphInfo extends AbstractNpcInfo
	{
		private final Player _pc;
		private final NpcTemplate _template;
		private final int _swimSpd;
		
		public PcMorphInfo(Player player, NpcTemplate template)
		{
			super(player);
			
			_pc = player;
			_template = template;
			
			_swimSpd = player.getStatus().getBaseSwimSpeed();
			
			_rhand = _template.getRightHand();
			_lhand = _template.getLeftHand();
			
			_collisionHeight = _template.getCollisionHeight();
			_collisionRadius = _template.getCollisionRadius();
		}
		
		@Override
		protected void writeImpl()
		{
			writeC(0x16);
			
			writeD(_pc.getObjectId());
			writeD(_template.getNpcId() + 1000000);
			writeD(1);
			
			writeD(_x);
			writeD(_y);
			writeD(_z);
			writeD(_heading);
			
			writeD(0x00);
			
			writeD(_mAtkSpd);
			writeD(_pAtkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_swimSpd);
			writeD(_swimSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			writeD(_runSpd);
			writeD(_walkSpd);
			
			writeF(_pc.getStatus().getMovementSpeedMultiplier());
			writeF(_pc.getStatus().getAttackSpeedMultiplier());
			
			writeF(_collisionRadius);
			writeF(_collisionHeight);
			
			writeD(_rhand);
			writeD(0);
			writeD(_lhand);
			
			writeC(1); // name above char
			writeC(_pc.isRunning() ? 1 : 0);
			writeC(_pc.isInCombat() ? 1 : 0);
			writeC(_pc.isAlikeDead() ? 1 : 0);
			writeC(0); // 0 = teleported, 1 = default, 2 = summoned
			
			writeS(_name);
			writeS(_title);
			
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			
			writeD(_pc.getAbnormalEffect());
			
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			
			writeC(_pc.getMove().getMoveType().getId());
			writeC(0x00);
			
			writeF(_collisionRadius);
			writeF(_collisionHeight);
			
			writeD(_enchantEffect);
			writeD(0x00);

			writeD(_pc.getStatus().getPhysicalAttackRange());

		}
	}
}
