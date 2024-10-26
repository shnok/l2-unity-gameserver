package com.shnok.javaserver.gameserver.model.actor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable.FrequentSkill;
import net.sf.l2j.gameserver.data.manager.CastleManager;
import net.sf.l2j.gameserver.enums.AiEventType;
import net.sf.l2j.gameserver.enums.SiegeSide;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.duels.DuelState;
import net.sf.l2j.gameserver.enums.skills.EffectFlag;
import net.sf.l2j.gameserver.enums.skills.EffectType;
import net.sf.l2j.gameserver.model.actor.ai.type.PlayableAI;
import net.sf.l2j.gameserver.model.actor.attack.PlayableAttack;
import net.sf.l2j.gameserver.model.actor.cast.PlayableCast;
import net.sf.l2j.gameserver.model.actor.container.npc.AggroInfo;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.actor.instance.SiegeGuard;
import net.sf.l2j.gameserver.model.actor.status.PlayableStatus;
import net.sf.l2j.gameserver.model.actor.template.CreatureTemplate;
import net.sf.l2j.gameserver.model.entity.Duel;
import net.sf.l2j.gameserver.model.group.CommandChannel;
import net.sf.l2j.gameserver.model.group.Party;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.EtcItem;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.location.Point2D;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.residence.castle.Siege;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ExUseSharedGroupItem;
import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
import net.sf.l2j.gameserver.network.serverpackets.Revive;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.scripting.Quest;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

/**
 * This class represents all {@link Playable} actors in the world : {@link Player}s and their different {@link Summon} types.
 */
public abstract class Playable extends Creature
{
	private final Map<Integer, Long> _disabledItems = new ConcurrentHashMap<>();
	
	protected Playable(int objectId, CreatureTemplate template)
	{
		super(objectId, template);
	}
	
	/**
	 * @return The max weight this {@link Playable} can carry.
	 */
	public abstract int getWeightLimit();
	
	/**
	 * @return The karma value of this {@link Playable} - in case of Summon, his owner.
	 */
	public abstract int getKarma();
	
	/**
	 * @return The pvp flag value of this {@link Playable} - in case of Summon, his owner.
	 */
	public abstract byte getPvpFlag();
	
	/**
	 * @return The {@link Clan} of this {@link Playable} - in case of Summon, his owner.
	 */
	public abstract Clan getClan();
	
	/**
	 * @return The {@link Clan} id of this {@link Playable} - in case of Summon, his owner.
	 */
	public abstract int getClanId();
	
	/**
	 * Add an {@link ItemInstance} to this {@link Playable}'s inventory.
	 * @param item : The {@link ItemInstance} to add.
	 * @param sendMessage : If true, send client message.
	 */
	public abstract void addItem(ItemInstance item, boolean sendMessage);
	
	/**
	 * Add an item to this {@link Playable}.
	 * @param itemId : The itemId of item to add.
	 * @param count : The quantity of items to add.
	 * @param sendMessage : Send {@link SystemMessage} client notification if set to true.
	 * @return an {@link ItemInstance} of a newly generated item for this {@link Playable}, using itemId and count.
	 */
	public abstract ItemInstance addItem(int itemId, int count, boolean sendMessage);
	
	@Override
	public PlayableAI<?> getAI()
	{
		return (PlayableAI<?>) _ai;
	}
	
	@Override
	public PlayableStatus<? extends Playable> getStatus()
	{
		return (PlayableStatus<?>) _status;
	}
	
	@Override
	public void setStatus()
	{
		_status = new PlayableStatus<>(this);
	}
	
	@Override
	public void setCast()
	{
		_cast = new PlayableCast<>(this);
	}
	
	@Override
	public void setAttack()
	{
		_attack = new PlayableAttack<>(this);
	}
	
	@Override
	public boolean doDie(Creature killer)
	{
		// killing is only possible one time
		synchronized (this)
		{
			if (isDead())
				return false;
			
			// now reset currentHp to zero
			getStatus().setHp(0);
			
			setIsDead(true);
		}
		
		// Stop movement, cast and attack. Reset the target.
		abortAll(true);
		
		// Stop HP/MP/CP Regeneration task
		getStatus().stopHpMpRegeneration();
		
		// Stop all active skills effects in progress
		if (isPhoenixBlessed())
		{
			// remove Lucky Charm if player has SoulOfThePhoenix/Salvation buff
			if (getCharmOfLuck())
				stopCharmOfLuck(null);
			if (isNoblesseBlessed())
				stopNoblesseBlessing(null);
		}
		// Same thing if the Character isn't a Noblesse Blessed L2Playable
		else if (isNoblesseBlessed())
		{
			stopNoblesseBlessing(null);
			
			// remove Lucky Charm if player have Nobless blessing buff
			if (getCharmOfLuck())
				stopCharmOfLuck(null);
		}
		else
			stopAllEffectsExceptThoseThatLastThroughDeath();
		
		// Send the Server->Client packet StatusUpdate with current HP and MP to all other Player to inform
		getStatus().broadcastStatusUpdate();
		
		// Notify Creature AI
		getAI().notifyEvent(AiEventType.DEAD, null, null);
		
		// Notify Quest of Playable's death
		final Player actingPlayer = getActingPlayer();
		actingPlayer.getQuestList().getQuests(Quest::isTriggeredOnDeath).forEach(q -> q.onDeath((killer == null ? this : killer), actingPlayer));
		
		if (killer != null)
		{
			final Player player = killer.getActingPlayer();
			if (player != null)
				player.onKillUpdatePvPKarma(this);
		}
		
		return true;
	}
	
	@Override
	public void doRevive()
	{
		if (!isDead() || isTeleporting())
			return;
		
		setIsDead(false);
		
		if (isPhoenixBlessed())
		{
			stopPhoenixBlessing(null);
			
			getStatus().setMaxHpMp();
		}
		else
			getStatus().setHp(getStatus().getMaxHp() * Config.RESPAWN_RESTORE_HP);
		
		// Start broadcast status
		broadcastPacket(new Revive(this));
	}
	
	@Override
	public boolean isMovementDisabled()
	{
		return super.isMovementDisabled() || getStatus().getMoveSpeed() == 0;
	}
	
	public boolean checkIfPvP(Playable target)
	{
		if (target == null || target == this)
			return false;
		
		if (getKarma() != 0)
			return false;
		
		if (target.getKarma() != 0 || target.getPvpFlag() == 0)
			return false;
		
		return true;
	}
	
	/**
	 * Send a {@link SystemMessage} packet using a {@link SystemMessageId} to the {@link Player} associated to this {@link Playable}.
	 * @param id : The {@link SystemMessageId} to send.
	 */
	public void sendPacket(SystemMessageId id)
	{
	}
	
	public final boolean isNoblesseBlessed()
	{
		return _effects.isAffected(EffectFlag.NOBLESS_BLESSING);
	}
	
	public final void stopNoblesseBlessing(AbstractEffect effect)
	{
		if (effect == null)
			stopEffects(EffectType.NOBLESSE_BLESSING);
		else
			removeEffect(effect);
		updateAbnormalEffect();
	}
	
	public final boolean isPhoenixBlessed()
	{
		return _effects.isAffected(EffectFlag.PHOENIX_BLESSING);
	}
	
	public final void stopPhoenixBlessing(AbstractEffect effect)
	{
		if (effect == null)
			stopEffects(EffectType.PHOENIX_BLESSING);
		else
			removeEffect(effect);
		
		updateAbnormalEffect();
	}
	
	@Override
	public boolean isSilentMoving()
	{
		return _effects.isAffected(EffectFlag.SILENT_MOVE);
	}
	
	public final boolean getProtectionBlessing()
	{
		return _effects.isAffected(EffectFlag.PROTECTION_BLESSING);
	}
	
	public void stopProtectionBlessing(AbstractEffect effect)
	{
		if (effect == null)
			stopEffects(EffectType.PROTECTION_BLESSING);
		else
			removeEffect(effect);
		
		updateAbnormalEffect();
	}
	
	public final boolean getCharmOfLuck()
	{
		return _effects.isAffected(EffectFlag.CHARM_OF_LUCK);
	}
	
	public final void stopCharmOfLuck(AbstractEffect effect)
	{
		if (effect == null)
			stopEffects(EffectType.CHARM_OF_LUCK);
		else
			removeEffect(effect);
		
		updateAbnormalEffect();
	}
	
	@Override
	public void updateEffectIcons(boolean partyOnly)
	{
		_effects.updateEffectIcons(partyOnly);
	}
	
	/**
	 * This method allows to easily send relations. Overridden in L2Summon and Player.
	 */
	public void broadcastRelationsChanges()
	{
	}
	
	@Override
	public boolean isInArena()
	{
		return isInsideZone(ZoneId.PVP) && !isInsideZone(ZoneId.SIEGE);
	}
	
	public void addItemSkillTimeStamp(L2Skill itemSkill, ItemInstance itemInstance)
	{
		final EtcItem etcItem = itemInstance.getEtcItem();
		final int reuseDelay = Math.max(itemSkill.getReuseDelay(), etcItem.getReuseDelay());
		
		addTimeStamp(itemSkill, reuseDelay);
		if (reuseDelay != 0)
			disableSkill(itemSkill, reuseDelay);
		
		final int group = etcItem.getSharedReuseGroup();
		if (group >= 0)
			sendPacket(new ExUseSharedGroupItem(etcItem.getItemId(), group, reuseDelay, reuseDelay));
	}
	
	/**
	 * Disable this ItemInstance id for the duration of the delay in milliseconds.
	 * @param item
	 * @param delay (seconds * 1000)
	 */
	public void disableItem(ItemInstance item, long delay)
	{
		if (item == null)
			return;
		
		_disabledItems.put(item.getObjectId(), System.currentTimeMillis() + delay);
	}
	
	/**
	 * Check if an item is disabled. All skills disabled are identified by their reuse objectIds in <B>_disabledItems</B>.
	 * @param item The ItemInstance to check
	 * @return true if the item is currently disabled.
	 */
	public boolean isItemDisabled(ItemInstance item)
	{
		if (_disabledItems.isEmpty())
			return false;
		
		if (item == null || isAllSkillsDisabled())
			return true;
		
		final int hashCode = item.getObjectId();
		
		final Long timeStamp = _disabledItems.get(hashCode);
		if (timeStamp == null)
			return false;
		
		if (timeStamp < System.currentTimeMillis())
		{
			_disabledItems.remove(hashCode);
			return false;
		}
		
		return true;
	}
	
	/**
	 * Check pvp conditions for a {@link Playable}->{@link Playable} offensive {@link L2Skill} cast.
	 * @param target : The {@link Playable} instance used as target.
	 * @param skill : The {@link L2Skill} being casted.
	 * @param isCtrlPressed : If true, the control key was used to cast.
	 * @return True if the {@link L2Skill} is a pvp {@link L2Skill} and target is a valid pvp target, false otherwise.
	 */
	public boolean canCastOffensiveSkillOnPlayable(Playable target, L2Skill skill, boolean isCtrlPressed)
	{
		final Player targetPlayer = target.getActingPlayer();
		
		// No cast upon self/owner.
		if (targetPlayer == getActingPlayer())
			return false;
		
		// No checks for players in Olympiad.
		if (isInSameActiveOlympiadMatch(targetPlayer))
			return true;
		
		// No checks for players in Duel.
		if (isInSameActiveDuel(targetPlayer))
			return true;
		
		final boolean sameParty = isInSameParty(targetPlayer);
		final boolean sameCommandChannel = isInSameCommandChannel(targetPlayer);
		
		// No checks for Playables in arena.
		if (isInArena() && target.isInArena() && !(sameParty || sameCommandChannel))
			return true;
			
		// Players in the same CC/party/alliance/clan may only damage each other with ctrlPressed.
		// If it's an AOE skill, only the mainTarget will be hit. PvpFlag / Karma do not influence these checks.
		final boolean isMainTarget = getAI().getCurrentIntention().getFinalTarget() == target;
		final boolean isCtrlDamagingTheMainTarget = isCtrlPressed && skill.isDamage() && isMainTarget;
		if (sameParty || sameCommandChannel || isInSameClan(targetPlayer) || isInSameAlly(targetPlayer) || isInSameActiveSiegeSide(targetPlayer))
			return isCtrlDamagingTheMainTarget;
		
		// If the target not from the same CC/party/alliance/clan/SiegeSide is in a PVP area, you can do anything.
		if (isInsideZone(ZoneId.PVP) && target.isInsideZone(ZoneId.PVP))
			return true;
		
		if (targetPlayer.getProtectionBlessing() && (getActingPlayer().getStatus().getLevel() - targetPlayer.getStatus().getLevel() >= 10) && getActingPlayer().getKarma() > 0)
			return false;
		
		if (getActingPlayer().getProtectionBlessing() && (targetPlayer.getStatus().getLevel() - getActingPlayer().getStatus().getLevel() >= 10) && targetPlayer.getKarma() > 0)
			return false;
		
		if (targetPlayer.isCursedWeaponEquipped() && getActingPlayer().getStatus().getLevel() <= 20)
			return false;
		
		if (getActingPlayer().isCursedWeaponEquipped() && targetPlayer.getStatus().getLevel() <= 20)
			return false;
		
		// If the target not from the same CC/party/alliance/clan/SiegeSide is flagged / PK, you can do anything.
		if (targetPlayer.getPvpFlag() > 0 || targetPlayer.getKarma() > 0)
			return true;
			
		// If the caster not from the same CC/party/alliance/clan is at war with the target, then With CTRL he may damage and debuff.
		// CTRL is still necessary for damaging. You can do anything so long as you have CTRL pressed.
		// pvpFlag / Karma do not influence these checks
		if (isAtWarWith(targetPlayer))
			return isCtrlPressed;
		
		// If the target is not clan war enemy or pvpFlag / Karma do not
		if (skill.isDebuff())
			return false;
		
		return isCtrlDamagingTheMainTarget;
	}
	
	@Override
	public boolean isAttackableBy(Creature attacker)
	{
		if (!super.isAttackableBy(attacker))
			return false;
		
		// Attackables can attack Playables anytime, anywhere
		if (attacker instanceof Monster)
			return true;
		
		// SiegeGuards cannot attack defenders/owners
		if (attacker instanceof SiegeGuard)
		{
			if (getClan() != null)
			{
				final Siege siege = CastleManager.getInstance().getActiveSiege(this);
				if (siege != null && siege.checkSides(getClan(), SiegeSide.DEFENDER, SiegeSide.OWNER))
					return false;
			}
			
			return true;
		}
		
		if (attacker instanceof Playable attackerPlayable)
		{
			// You cannot be attacked by a Playable in Olympiad before the start of the game.
			if (getActingPlayer().isInOlympiadMode() && !getActingPlayer().isOlympiadStart())
				return false;
			
			if (isInsideZone(ZoneId.PVP))
				return true;
			
			// One cannot be attacked if any of the two has Blessing of Protection and the other is >=10 levels higher and is PK
			if (getProtectionBlessing() && (attackerPlayable.getStatus().getLevel() - getStatus().getLevel() >= 10) && attackerPlayable.getKarma() > 0)
				return false;
			
			if (attackerPlayable.getProtectionBlessing() && (getStatus().getLevel() - attackerPlayable.getStatus().getLevel() >= 10) && getKarma() > 0)
				return false;
			
			// One cannot be attacked if any of the two is wielding a Cursed Weapon and the other is under level 20
			if (getActingPlayer().isCursedWeaponEquipped() && attackerPlayable.getStatus().getLevel() <= 20)
				return false;
			
			if (attackerPlayable.getActingPlayer().isCursedWeaponEquipped() && getStatus().getLevel() <= 20)
				return false;
		}
		
		return true;
	}
	
	@Override
	public boolean isAttackableWithoutForceBy(Playable attacker)
	{
		final Player attackerPlayer = attacker.getActingPlayer();
		
		// No cast upon self/owner.
		if (attackerPlayer == getActingPlayer())
			return false;
		
		// No checks for players in Olympiad.
		if (isInSameActiveOlympiadMatch(attackerPlayer))
			return true;
		
		// No checks for players in Duel.
		if (isInSameActiveDuel(attackerPlayer))
			return true;
		
		final boolean sameParty = isInSameParty(attackerPlayer);
		final boolean sameCommandChannel = isInSameCommandChannel(attackerPlayer);
		
		// No checks for Playables in arena.
		if (isInArena() && attacker.isInArena() && !(sameParty || sameCommandChannel))
			return true;
		
		// Players in the same CC/party/alliance/clan cannot attack without CTRL
		if (sameParty || sameCommandChannel || isInSameClan(attackerPlayer) || isInSameAlly(attackerPlayer) || isInSameActiveSiegeSide(attackerPlayer))
			return false;
		
		// CTRL is not needed if both are in a PVP area
		if (isInsideZone(ZoneId.PVP) && attacker.isInsideZone(ZoneId.PVP))
			return true;
		
		// CTRL is not needed if the target (this) is flagged / PK
		if (getKarma() > 0 || getPvpFlag() > 0)
			return true;
		
		// Any other case returns false, even clan war. You need CTRL to attack.
		return false;
	}
	
	/**
	 * @param target : The {@link Creature} used as target.
	 * @return True if this {@link Playable} can continue to attack the {@link Creature} set as target, false otherwise.
	 */
	public boolean canKeepAttacking(Creature target)
	{
		if (target == null)
			return false;
		
		if (target instanceof Playable)
		{
			final Player targetPlayer = target.getActingPlayer();
			
			// Karma players can be kept attacked.
			if (targetPlayer.getKarma() > 0)
				return true;
			
			// Playables in Olympiad can be kept attacked.
			if (isInSameActiveOlympiadMatch(targetPlayer))
				return true;
			
			// Playables in Duel can be kept attacked.
			if (isInSameActiveDuel(targetPlayer))
				return true;
			
			// Playables in a PVP area can be kept attacked.
			if (isInsideZone(ZoneId.PVP) && target.isInsideZone(ZoneId.PVP))
				return true;
			
			// Betrayer Summon will continue the attack.
			if (this instanceof Summon && isBetrayed())
				return true;
			
			return false;
		}
		return true;
	}
	
	@Override
	public boolean testCursesOnAttack(Npc npc, int npcId)
	{
		if (Config.RAID_DISABLE_CURSE || !(npc instanceof Attackable attackable))
			return false;
		
		// Petrification curse.
		if (getStatus().getLevel() - attackable.getStatus().getLevel() > 8)
		{
			final L2Skill curse = FrequentSkill.RAID_CURSE2.getSkill();
			if (getFirstEffect(curse) == null)
			{
				broadcastPacket(new MagicSkillUse(attackable, this, curse.getId(), curse.getLevel(), 300, 0));
				curse.getEffects(attackable, this);
				
				attackable.getAI().getAggroList().stopHate(this);
				return true;
			}
		}
		
		// Antistrider slow curse.
		if (attackable.getNpcId() == npcId && this instanceof Player player && player.isMounted())
		{
			final L2Skill curse = FrequentSkill.RAID_ANTI_STRIDER_SLOW.getSkill();
			if (getFirstEffect(curse) == null)
			{
				broadcastPacket(new MagicSkillUse(attackable, player, curse.getId(), curse.getLevel(), 300, 0));
				curse.getEffects(attackable, player);
			}
		}
		return false;
	}
	
	@Override
	public boolean testCursesOnAttack(Npc npc)
	{
		return testCursesOnAttack(npc, npc.getNpcId());
	}
	
	@Override
	public boolean testCursesOnAggro(Npc npc)
	{
		return testCursesOnAttack(npc, -1);
	}
	
	@Override
	public boolean testCursesOnSkillSee(L2Skill skill, Creature[] targets)
	{
		if (Config.RAID_DISABLE_CURSE)
			return false;
		
		final boolean isAggressive = skill.isOffensive() || skill.isDebuff();
		
		if (isAggressive)
		{
			// Petrification.
			for (final Creature target : targets)
			{
				// Must be called by a raid related Attackable.
				if (!(target instanceof Attackable targetAttackable) || !targetAttackable.isRaidRelated())
					continue;
				
				if (getStatus().getLevel() - targetAttackable.getStatus().getLevel() > 8)
				{
					final L2Skill curse = FrequentSkill.RAID_CURSE2.getSkill();
					if (getFirstEffect(curse) == null)
					{
						broadcastPacket(new MagicSkillUse(targetAttackable, this, curse.getId(), curse.getLevel(), 300, 0));
						curse.getEffects(targetAttackable, this);
						
						targetAttackable.getAI().getAggroList().stopHate(this);
						return true;
					}
				}
			}
			return false;
		}
		
		// Silence - must be called by a raid related, the target must be in aggrolist with hate > 0, the effect must be beneficial.
		final List<Attackable> list = getKnownTypeInRadius(Attackable.class, 1000);
		if (!list.isEmpty())
		{
			for (final Creature target : targets)
			{
				// Tested target must be a Playable.
				if (!(target instanceof Playable))
					continue;
				
				for (Attackable attackable : list)
				{
					// Must be called by a raid related Attackable.
					if (!attackable.isRaidRelated())
						continue;
					
					if (getStatus().getLevel() - attackable.getStatus().getLevel() > 8)
					{
						final AggroInfo ai = attackable.getAI().getAggroList().get(target);
						if (ai != null && ai.getHate() > 0)
						{
							final L2Skill curse = FrequentSkill.RAID_CURSE.getSkill();
							if (getFirstEffect(curse) == null)
							{
								broadcastPacket(new MagicSkillUse(attackable, this, curse.getId(), curse.getLevel(), 300, 0));
								curse.getEffects(attackable, this);
								
								attackable.getAI().getAggroList().stopHate(this);
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if both this {@link Playable} and tested {@link Playable} share the same active {@link Duel}.
	 */
	public final boolean isInSameActiveDuel(Playable playable)
	{
		return getActingPlayer().getDuelState() == DuelState.DUELLING && playable.getActingPlayer().getDuelState() == DuelState.DUELLING && getActingPlayer().getDuelId() == playable.getActingPlayer().getDuelId();
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if both this {@link Playable} and tested {@link Playable} share the same active {@link Duel}.
	 */
	public final boolean isInSameActiveOlympiadMatch(Playable playable)
	{
		return getActingPlayer().isOlympiadStart() && playable.getActingPlayer().isOlympiadStart() && getActingPlayer().getOlympiadGameId() == playable.getActingPlayer().getOlympiadGameId();
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if both this {@link Playable} and tested {@link Playable} share the same {@link Party}.
	 */
	public final boolean isInSameParty(Playable playable)
	{
		return isInParty() && getParty().containsPlayer(playable.getActingPlayer());
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if both this {@link Playable} and tested {@link Playable} share the same {@link CommandChannel}.
	 */
	public final boolean isInSameCommandChannel(Playable playable)
	{
		return isInParty() && getParty().getCommandChannel() != null && getParty().getCommandChannel().containsPlayer(playable.getActingPlayer());
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if both this {@link Playable} and tested {@link Playable} share the same {@link Clan}.
	 */
	public final boolean isInSameClan(Playable playable)
	{
		return getClanId() > 0 && getClanId() == playable.getClanId();
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if both this {@link Playable} and tested {@link Playable} share the same alliance id.
	 */
	public final boolean isInSameAlly(Playable playable)
	{
		return getActingPlayer().getAllyId() > 0 && getActingPlayer().getAllyId() == playable.getActingPlayer().getAllyId();
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if and only if an active siege is set, where both this {@link Playable} and tested {@link Playable} share the same {@link SiegeSide}.
	 */
	public final boolean isInSameActiveSiegeSide(Playable playable)
	{
		// This or tested Playable isn't on a SIEGE zoneId, return false.
		if (!isInsideZone(ZoneId.SIEGE) || !playable.isInsideZone(ZoneId.SIEGE))
			return false;
		
		// No active siege is found, return false.
		final Siege siege = CastleManager.getInstance().getActiveSiege(this);
		if (siege == null)
			return false;
		
		// Return true if both this and tested Playable share same side, false otherwise.
		return !siege.isOnOppositeSide(getClan(), playable.getClan());
	}
	
	/**
	 * @param playable : The {@link Playable} to test.
	 * @return True if this {@link Playable} is at war with tested {@link Playable}.
	 */
	public final boolean isAtWarWith(Playable playable)
	{
		final Clan aClan = getClan();
		final Clan tClan = playable.getClan();
		return aClan != null && tClan != null && aClan.isAtWarWith(tClan.getClanId()) && tClan.isAtWarWith(aClan.getClanId());
	}
	
	@Override
	public void fleeFrom(Creature attacker, int distance)
	{
		// No attacker or distance isn't noticeable ; return instantly.
		if (attacker == null || attacker == this || distance < 10)
			return;
		
		// Enforce running state.
		forceRunStance();
		
		// Generate a Location and calculate the destination.
		final Location loc = getPosition().clone();
		loc.setFleeing(attacker.getPosition(), distance);
		
		// Try to move to the position.
		getAI().tryToMoveTo(loc, null);
	}
	
	@Override
	public void moveUsingRandomOffset(int offset)
	{
		// Offset isn't noticeable ; return instantly.
		if (offset < 10)
			return;
		
		// Generate a new Location and calculate the destination.
		final Location loc = getPosition().clone();
		loc.addRandomOffset(offset);
		
		// Try to move to the position.
		getAI().tryToMoveTo(loc, null);
	}
	
	public ItemInstance checkItemManipulation(int objectId, int count)
	{
		return null;
	}
	
	public ItemInstance transferItem(int objectId, int amount, Playable target)
	{
		final ItemInstance newItem = getInventory().transferItem(objectId, amount, target);
		if (newItem == null)
			return null;
		
		return newItem;
	}
	
	public Boat getDockedBoat()
	{
		return getKnownType(Boat.class).stream().filter(b -> b.getEngine().getDock() != null).findFirst().orElse(null);
	}
	
	public boolean tryToPassBoatEntrance(Point2D targetLoc)
	{
		final Boat boat = getDockedBoat();
		if (boat == null || boat.getDock() == null)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		final Point2D point = boat.getDock().getBoardingPoint(getPosition(), targetLoc, isInBoat());
		
		if (point == null)
		{
			sendPacket(ActionFailed.STATIC_PACKET);
			return false;
		}
		
		moveToBoatEntrance(point, boat);
		return true;
	}
	
	public void moveToBoatEntrance(Point2D point, Boat boat)
	{
		final Location destination = new Location(point.getX(), point.getY(), -3624);
		
		if (distance2D(destination) > 50)
		{
			getAI().tryToMoveTo(destination, boat);
			return;
		}
		
		if (this instanceof Player player)
			player.getBoatInfo().setCanBoard(true);
		
		sendPacket(ActionFailed.STATIC_PACKET);
	}
}