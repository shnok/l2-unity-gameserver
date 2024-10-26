package com.shnok.javaserver.gameserver.model.actor;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.enums.TeamType;
import com.shnok.javaserver.gameserver.enums.items.ActionType;
import com.shnok.javaserver.gameserver.enums.items.ShotType;
import com.shnok.javaserver.gameserver.handler.IItemHandler;
import com.shnok.javaserver.gameserver.handler.ItemHandler;
import com.shnok.javaserver.gameserver.model.actor.ai.type.SummonAI;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.actor.move.SummonMove;
import com.shnok.javaserver.gameserver.model.actor.status.SummonStatus;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.model.itemcontainer.PetInventory;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameManager;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.AbstractNpcInfo.SummonInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.PetDelete;
import com.shnok.javaserver.gameserver.network.serverpackets.PetInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.PetItemList;
import com.shnok.javaserver.gameserver.network.serverpackets.PetStatusShow;
import com.shnok.javaserver.gameserver.network.serverpackets.PetStatusUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.RelationChanged;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public abstract class Summon extends Playable
{
	private Player _owner;
	private boolean _previousFollowStatus = true;
	private int _shotsMask = 0;
	
	public static final int CONTRACT_PAYMENT = 4140;
	
	protected Summon(int objectId, NpcTemplate template, Player owner)
	{
		super(objectId, template);
		
		// Calculate passive skills stats.
		for (L2Skill skill : template.getPassives())
			addStatFuncs(skill.getStatFuncs(this));
		
		// Set the magical circle animation.
		setShowSummonAnimation(true);
		
		// Set the Player owner.
		_owner = owner;
	}
	
	public abstract int getSummonType();
	
	@Override
	public SummonAI getAI()
	{
		return (SummonAI) _ai;
	}
	
	@Override
	public void setAI()
	{
		_ai = new SummonAI(this);
	}
	
	@Override
	public SummonStatus<? extends Summon> getStatus()
	{
		return (SummonStatus<?>) _status;
	}
	
	@Override
	public void setStatus()
	{
		_status = new SummonStatus<>(this);
	}
	
	@Override
	public SummonMove getMove()
	{
		return (SummonMove) _move;
	}
	
	@Override
	public void setMove()
	{
		_move = new SummonMove(this);
	}
	
	@Override
	public NpcTemplate getTemplate()
	{
		return (NpcTemplate) super.getTemplate();
	}
	
	@Override
	public void setWalkOrRun(boolean value)
	{
		super.setWalkOrRun(value);
		
		getStatus().broadcastStatusUpdate();
	}
	
	@Override
	public void updateAbnormalEffect()
	{
		forEachKnownType(Player.class, player -> player.sendPacket(new SummonInfo(this, player, 1)));
	}
	
	@Override
	public boolean isGM()
	{
		return _owner.isGM();
	}
	
	@Override
	public void onInteract(Player player)
	{
		player.sendPacket(new PetStatusShow(this));
	}
	
	@Override
	public void onAction(Player player, boolean isCtrlPressed, boolean isShiftPressed)
	{
		// Set the target of the player
		if (player.getTarget() != this)
			player.setTarget(this);
		else
		{
			if (player == _owner)
			{
				if (isCtrlPressed)
					player.getAI().tryToAttack(this, isCtrlPressed, isShiftPressed);
				else
					player.getAI().tryToInteract(this, isCtrlPressed, isShiftPressed);
			}
			else
			{
				if (isAttackableWithoutForceBy(player) || (isCtrlPressed && isAttackableBy(player)))
					player.getAI().tryToAttack(this, isCtrlPressed, isShiftPressed);
				else
					player.getAI().tryToFollow(this, isShiftPressed);
			}
		}
	}
	
	@Override
	public final int getKarma()
	{
		return (_owner != null) ? _owner.getKarma() : 0;
	}
	
	@Override
	public final byte getPvpFlag()
	{
		return (_owner != null) ? _owner.getPvpFlag() : 0;
	}
	
	@Override
	public int getWeightLimit()
	{
		return 0;
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		if (!super.doDie(killer))
			return false;
		
		// Revive popup request if phoenix blessing buff was on.
		if (isPhoenixBlessed())
			_owner.reviveRequest(_owner, null, true);
		
		// Disable beastshots
		for (int itemId : _owner.getAutoSoulShot())
		{
			switch (ItemData.getInstance().getTemplate(itemId).getDefaultAction())
			{
				case summon_soulshot, summon_spiritshot:
					_owner.disableAutoShot(itemId);
					break;
			}
		}
		return true;
	}
	
	@Override
	public void onDecay()
	{
		if (_owner.getSummon() != this)
			return;
		
		deleteMe(_owner);
	}
	
	@Override
	public PetInventory getInventory()
	{
		return null;
	}
	
	@Override
	public ItemInstance getActiveWeaponInstance()
	{
		return null;
	}
	
	@Override
	public Weapon getActiveWeaponItem()
	{
		return null;
	}
	
	@Override
	public ItemInstance getSecondaryWeaponInstance()
	{
		return null;
	}
	
	@Override
	public Weapon getSecondaryWeaponItem()
	{
		return null;
	}
	
	@Override
	public boolean isInvul()
	{
		return super.isInvul() || _owner.isSpawnProtected();
	}
	
	@Override
	public Party getParty()
	{
		return (_owner == null) ? null : _owner.getParty();
	}
	
	@Override
	public boolean isInParty()
	{
		return _owner != null && _owner.getParty() != null;
	}
	
	@Override
	public void setIsImmobilized(boolean value)
	{
		super.setIsImmobilized(value);
		
		if (value)
		{
			_previousFollowStatus = getAI().getFollowStatus();
			// if immobilized, disable follow mode
			if (_previousFollowStatus)
				getAI().setFollowStatus(false);
		}
		else
		{
			// if not more immobilized, restore follow mode
			getAI().setFollowStatus(_previousFollowStatus);
		}
	}
	
	@Override
	public void sendDamageMessage(Creature target, int damage, boolean mcrit, boolean pcrit, boolean miss)
	{
		if (miss || _owner == null)
			return;
		
		// Prevents the double spam of system messages, if the target is the owning player.
		if (target.getObjectId() != _owner.getObjectId())
		{
			if (pcrit || mcrit)
				sendPacket(SystemMessageId.CRITICAL_HIT_BY_PET);
			
			if (target.isInvul())
			{
				if (target.isParalyzed())
					sendPacket(SystemMessageId.OPPONENT_PETRIFIED);
				else
					sendPacket(SystemMessageId.ATTACK_WAS_BLOCKED);
			}
			else
				sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_HIT_FOR_S1_DAMAGE).addNumber(damage));
			
			if (_owner.isInOlympiadMode() && target instanceof Player targetPlayer && targetPlayer.isInOlympiadMode() && targetPlayer.getOlympiadGameId() == _owner.getOlympiadGameId())
				OlympiadGameManager.getInstance().notifyCompetitorDamage(_owner, damage);
		}
	}
	
	@Override
	public boolean isOutOfControl()
	{
		return super.isOutOfControl() || isBetrayed();
	}
	
	@Override
	public boolean isInCombat()
	{
		return _owner != null && _owner.isInCombat();
	}
	
	@Override
	public Player getActingPlayer()
	{
		return _owner;
	}
	
	@Override
	public void sendPacket(L2GameServerPacket packet)
	{
		if (_owner != null)
			_owner.sendPacket(packet);
	}
	
	@Override
	public void sendPacket(SystemMessageId id)
	{
		if (_owner != null)
			_owner.sendPacket(id);
	}
	
	@Override
	public void deleteMe()
	{
		super.deleteMe();
		
		// We stop effects here and not higher in hierarchy, because Players need to keep their effects.
		stopAllEffects();
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		// Need it only for "crests on summons" custom.
		if (Config.SHOW_SUMMON_CREST)
			sendPacket(new SummonInfo(this, _owner, 0));
		
		sendPacket(new RelationChanged(this, _owner.getRelation(_owner), false));
		broadcastRelationsChanges();
		
		forceSeeCreature();
	}
	
	@Override
	public void broadcastRelationsChanges()
	{
		_owner.forEachKnownType(Player.class, player -> player.sendPacket(new RelationChanged(this, _owner.getRelation(player), isAttackableWithoutForceBy(player))));
	}
	
	@Override
	public void sendInfo(Player player)
	{
		// Check if the Player is the owner of the Pet
		if (player == _owner)
		{
			player.sendPacket(new PetInfo(this, 0));
			
			// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
			updateEffectIcons(true);
			
			// Reset Pet inventory.
			if (this instanceof Pet)
				player.sendPacket(new PetItemList(this));
		}
		else
			player.sendPacket(new SummonInfo(this, player, 0));
	}
	
	@Override
	public void stopAllEffects()
	{
		super.stopAllEffects();
		
		sendPetInfosToOwner();
	}
	
	@Override
	public void stopAllEffectsExceptThoseThatLastThroughDeath()
	{
		super.stopAllEffectsExceptThoseThatLastThroughDeath();
		
		sendPetInfosToOwner();
	}
	
	@Override
	public boolean isChargedShot(ShotType type)
	{
		return (_shotsMask & type.getMask()) == type.getMask();
	}
	
	@Override
	public void setChargedShot(ShotType type, boolean charged)
	{
		if (charged)
			_shotsMask |= type.getMask();
		else
			_shotsMask &= ~type.getMask();
	}
	
	@Override
	public void rechargeShots(boolean physical, boolean magic)
	{
		if (_owner.getAutoSoulShot() == null || _owner.getAutoSoulShot().isEmpty())
			return;
		
		for (int itemId : _owner.getAutoSoulShot())
		{
			ItemInstance item = _owner.getInventory().getItemByItemId(itemId);
			if (item != null)
			{
				if (magic && item.getItem().getDefaultAction() == ActionType.summon_spiritshot)
				{
					final IItemHandler handler = ItemHandler.getInstance().getHandler(item.getEtcItem());
					if (handler != null)
						handler.useItem(_owner, item, false);
				}
				
				if (physical && item.getItem().getDefaultAction() == ActionType.summon_soulshot)
				{
					final IItemHandler handler = ItemHandler.getInstance().getHandler(item.getEtcItem());
					if (handler != null)
						handler.useItem(_owner, item, false);
				}
			}
			else
				_owner.removeAutoSoulShot(itemId);
		}
	}
	
	@Override
	public int getSkillLevel(int skillId)
	{
		final L2Skill skill = getSkill(skillId);
		return (skill == null) ? 0 : skill.getLevel();
	}
	
	@Override
	public L2Skill getSkill(int skillId)
	{
		return getTemplate().getSkills().values().stream().filter(s -> s.getId() == skillId).findFirst().orElse(null);
	}
	
	@Override
	public void onTeleported()
	{
		super.onTeleported();
		
		// Need it only for "crests on summons" custom.
		if (Config.SHOW_SUMMON_CREST)
			sendPacket(new SummonInfo(this, _owner, 0));
	}
	
	@Override
	public Clan getClan()
	{
		return (_owner != null) ? _owner.getClan() : null;
	}
	
	@Override
	public int getClanId()
	{
		return (_owner != null) ? _owner.getClanId() : 0;
	}
	
	@Override
	public boolean checkIfPvP(Playable target)
	{
		if (target == _owner)
			return false;
		
		return super.checkIfPvP(target);
	}
	
	public final TeamType getTeam()
	{
		return (_owner != null) ? _owner.getTeam() : TeamType.NONE;
	}
	
	public final Player getOwner()
	{
		return _owner;
	}
	
	public void setOwner(Player newOwner)
	{
		_owner = newOwner;
	}
	
	public boolean isMountable()
	{
		return false;
	}
	
	public final int getNpcId()
	{
		return getTemplate().getNpcId();
	}
	
	public int getSoulShotsPerHit()
	{
		return getTemplate().getSsCount();
	}
	
	public int getSpiritShotsPerHit()
	{
		return getTemplate().getSpsCount();
	}
	
	public int getAttackRange()
	{
		return 36;
	}
	
	public int getControlItemId()
	{
		return 0;
	}
	
	public Weapon getActiveWeapon()
	{
		return null;
	}
	
	public void store()
	{
	}
	
	public int getWeapon()
	{
		return 0;
	}
	
	public int getArmor()
	{
		return 0;
	}
	
	public void deleteMe(Player owner)
	{
		doUnsummon(owner);
	}
	
	public void unSummon(Player owner)
	{
		if (!isVisible() || isDead())
			return;
		
		doUnsummon(owner);
	}
	
	private void doUnsummon(Player owner)
	{
		abortAll(true);
		
		getStatus().stopHpMpRegeneration();
		stopAllEffects();
		store();
		
		owner.setSummon(null);
		owner.sendPacket(new PetDelete(getSummonType(), getObjectId()));
		
		decayMe();
		
		// Remove Contract Payment effect from owner.
		_owner.stopSkillEffects(CONTRACT_PAYMENT);
		
		// Disable beastshots
		for (int itemId : owner.getAutoSoulShot())
		{
			switch (ItemData.getInstance().getTemplate(itemId).getDefaultAction())
			{
				case summon_soulshot, summon_spiritshot:
					owner.disableAutoShot(itemId);
					break;
			}
		}
		
		super.deleteMe();
	}
	
	public void updateAndBroadcastStatusAndInfos(int val)
	{
		sendPacket(new PetInfo(this, val));
		
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		updateEffectIcons(true);
		
		updateAndBroadcastStatus(val);
	}
	
	public void sendPetInfosToOwner()
	{
		sendPacket(new PetInfo(this, 2));
		
		// The PetInfo packet wipes the PartySpelled (list of active spells' icons). Re-add them
		updateEffectIcons(true);
	}
	
	public void updateAndBroadcastStatus(int val)
	{
		sendPacket(new PetStatusUpdate(this));
		
		if (isVisible())
			forEachKnownType(Player.class, player -> player != _owner, player -> player.sendPacket(new SummonInfo(this, player, val)));
	}
}