package com.shnok.javaserver.gameserver.model.actor.status;

import java.util.Map;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.data.manager.ClanHallManager;
import com.shnok.javaserver.gameserver.data.manager.DuelManager;
import com.shnok.javaserver.gameserver.data.manager.ZoneManager;
import com.shnok.javaserver.gameserver.data.xml.PlayerLevelData;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.enums.StatusType;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.enums.actors.WeightPenalty;
import com.shnok.javaserver.gameserver.enums.duels.DuelState;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.container.npc.RewardInfo;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.actor.instance.Servitor;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameManager;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameTask;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;
import com.shnok.javaserver.gameserver.model.records.PlayerLevel;
import com.shnok.javaserver.gameserver.model.residence.castle.Siege;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHall;
import com.shnok.javaserver.gameserver.model.residence.clanhall.ClanHallFunction;
import com.shnok.javaserver.gameserver.model.zone.type.MotherTreeZone;
import com.shnok.javaserver.gameserver.model.zone.type.SwampZone;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.ExDuelUpdateUserInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PartySmallWindowUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowMemberListUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.StatusUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.UserInfo;
import com.shnok.javaserver.gameserver.scripting.QuestState;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class PlayerStatus extends PlayableStatus<Player>
{
	private double _cp = .0;
	
	private double _cpUpdateIncCheck = .0;
	private double _cpUpdateDecCheck = .0;
	private double _cpUpdateInterval = .0;
	
	private double _mpUpdateIncCheck = .0;
	private double _mpUpdateDecCheck = .0;
	private double _mpUpdateInterval = .0;
	
	private int _oldMaxHp;
	private int _oldMaxMp;
	private int _oldMaxCp;
	
	public PlayerStatus(Player actor)
	{
		super(actor);
	}
	
	@Override
	public void initializeValues()
	{
		super.initializeValues();
		
		final double maxCp = getMaxCp();
		final double maxMp = getMaxMp();
		
		_cpUpdateInterval = maxCp / BAR_SIZE;
		_cpUpdateIncCheck = maxCp;
		_cpUpdateDecCheck = maxCp - _cpUpdateInterval;
		
		_mpUpdateInterval = maxMp / BAR_SIZE;
		_mpUpdateIncCheck = maxMp;
		_mpUpdateDecCheck = maxMp - _mpUpdateInterval;
	}
	
	@Override
	public final void reduceHp(double value, Creature attacker)
	{
		reduceHp(value, attacker, true, false, false, false);
	}
	
	@Override
	public final void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption)
	{
		reduceHp(value, attacker, awake, isDOT, isHPConsumption, false);
	}
	
	public final void reduceHp(double value, Creature attacker, boolean awake, boolean isDOT, boolean isHPConsumption, boolean ignoreCP)
	{
		if (_actor.isDead())
			return;
		
		// invul handling
		if (_actor.isInvul())
		{
			// other chars can't damage
			if (attacker != _actor)
				return;
			
			// only DOT and HP consumption allowed for damage self
			if (!isDOT && !isHPConsumption)
				return;
		}
		
		if (!isHPConsumption)
		{
			_actor.stopEffects(EffectType.SLEEP);
			_actor.stopEffects(EffectType.IMMOBILE_UNTIL_ATTACKED);
			
			// When taking a hit, stand up - except if under shop mode.
			if (_actor.isSitting() && !_actor.isInStoreMode())
				_actor.standUp();
			
			if (!isDOT && _actor.isStunned() && Rnd.get(10) == 0)
			{
				_actor.stopEffects(EffectType.STUN);
				
				// Refresh abnormal effects.
				_actor.updateAbnormalEffect();
			}
		}
		
		if (attacker != null && attacker != _actor)
		{
			final Player attackerPlayer = attacker.getActingPlayer();
			if (attackerPlayer != null && !attackerPlayer.getAccessLevel().canGiveDamage())
				return;
			
			if (_actor.isInDuel())
			{
				final DuelState playerState = _actor.getDuelState();
				if (playerState == DuelState.DEAD || playerState == DuelState.WINNER)
					return;
				
				// Cancel duel if player got hit by another player that is not part of the duel or if player isn't in duel state.
				if (attackerPlayer == null || attackerPlayer.getDuelId() != _actor.getDuelId() || playerState != DuelState.DUELLING)
					_actor.setDuelState(DuelState.INTERRUPTED);
			}
			
			int fullValue = (int) value;
			int tDmg = 0;
			
			// Check and calculate transfered damage, if any.
			final Summon summon = _actor.getSummon();
			if (summon instanceof Servitor && summon.isIn3DRadius(_actor, 900))
			{
				tDmg = (int) (value * calcStat(Stats.TRANSFER_DAMAGE_PERCENT, 0, null, null) / 100.);
				
				// Only transfer dmg up to current HP, it should not be killed
				tDmg = Math.min((int) summon.getStatus().getHp() - 1, tDmg);
				if (tDmg > 0)
				{
					summon.reduceCurrentHp(tDmg, attacker, null);
					value -= tDmg;
					fullValue = (int) value; // reduce the announced value here as player will get a message about summon damage
				}
			}
			
			if (!ignoreCP && attacker instanceof Playable)
			{
				if (_cp >= value)
				{
					setCp(_cp - value); // Set Cp to diff of Cp vs value
					value = 0; // No need to subtract anything from Hp
				}
				else
				{
					value -= _cp; // Get diff from value vs Cp; will apply diff to Hp
					setCp(0, false); // Set Cp to 0
				}
			}
			
			if (fullValue > 0 && !isDOT)
			{
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_GAVE_YOU_S2_DMG).addCharName(attacker).addNumber(fullValue));
				
				if (tDmg > 0 && attackerPlayer != null)
					attackerPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.GIVEN_S1_DAMAGE_TO_YOUR_TARGET_AND_S2_DAMAGE_TO_SERVITOR).addNumber(fullValue).addNumber(tDmg));
			}
		}
		
		if (value > 0)
		{
			value = _hp - value;
			if (value <= 0)
			{
				// During a Duel, limit the value to 1. Handle Duel end.
				if (_actor.isInDuel())
				{
					if (_actor.getDuelState() == DuelState.DUELLING)
					{
						_actor.disableAllSkills();
						
						stopHpMpRegeneration();
						
						// let the DuelManager know of his defeat
						DuelManager.getInstance().onPlayerDefeat(_actor);
					}
					value = 1;
				}
				// Reduce HPs. The value is blocked to 1 if the Creature isn't mortal.
				else
					value = (_actor.isMortal()) ? 0 : 1;
			}
			setHp(value);
		}
		
		// Handle die process if value is too low.
		if (_hp < 0.5)
		{
			if (_actor.isInOlympiadMode())
			{
				// Abort attack, cast and move.
				_actor.abortAll(false);
				
				stopHpMpRegeneration();
				_actor.setIsDead(true);
				
				final Summon summon = _actor.getSummon();
				if (summon != null)
					summon.getAI().tryToIdle();
				
				return;
			}
			
			_actor.doDie(attacker);
			
			final QuestState qs = _actor.getQuestList().getQuestState("Tutorial");
			if (qs != null)
				qs.getQuest().notifyEvent("CE30", null, _actor);
		}
	}
	
	@Override
	public final void setHp(double newHp, boolean broadcastPacket)
	{
		super.setHp(newHp, broadcastPacket);
		
		final QuestState qs = _actor.getQuestList().getQuestState("Tutorial");
		if (qs != null && getHpRatio() < 0.3)
			qs.getQuest().notifyEvent("CE45", null, _actor);
	}
	
	public final double getCp()
	{
		return _cp;
	}
	
	public final void setCp(double newCp)
	{
		setCp(newCp, true);
	}
	
	/**
	 * Set current CPs to the amount set as parameter. We also start or stop the regeneration task if needed.
	 * @param newCp : The new amount to set.
	 * @param broadcastPacket : If true, call {@link #broadcastStatusUpdate()}.
	 */
	public final void setCp(double newCp, boolean broadcastPacket)
	{
		final int maxCp = getMaxCp();
		
		synchronized (this)
		{
			if (_actor.isDead())
				return;
			
			if (newCp < 0)
				newCp = 0;
			
			if (newCp >= maxCp)
			{
				// Set the RegenActive flag to false
				_cp = maxCp;
				_flagsRegenActive &= ~REGEN_FLAG_CP;
				
				// Stop the HP/MP/CP Regeneration task
				if (_flagsRegenActive == 0)
					stopHpMpRegeneration();
			}
			else
			{
				// Set the RegenActive flag to true
				_cp = newCp;
				_flagsRegenActive |= REGEN_FLAG_CP;
				
				// Start the HP/MP/CP Regeneration task with Medium priority
				startHpMpRegeneration();
			}
		}
		
		if (broadcastPacket)
			broadcastStatusUpdate();
	}
	
	/**
	 * Set both CPs, HPs and MPs to given values set as parameters. The udpate is called only one time, during MPs allocation.
	 * @param newCp : The new HP value.
	 * @param newHp : The new HP value.
	 * @param newMp : The new MP value.
	 */
	public final void setCpHpMp(double newCp, double newHp, double newMp)
	{
		setCp(newCp, false);
		
		super.setHpMp(newHp, newMp);
	}
	
	/**
	 * Set both CPs, HPs and MPs to the maximum values. The udpate is called only one time, during MPs allocation.
	 */
	public final void setMaxCpHpMp()
	{
		setCp(getMaxCp(), false);
		
		super.setMaxHpMp();
	}
	
	@Override
	protected void doRegeneration()
	{
		// Modify the current CP of the Creature.
		if (_cp < getMaxCp())
			setCp(_cp + Math.max(1, getRegenCp()), false);
		
		super.doRegeneration();
	}
	
	/**
	 * @return True if a CP update should be done, otherwise false.
	 */
	private boolean needCpUpdate()
	{
		final double cp = _cp;
		final double maxCp = getMaxCp();
		
		if (cp <= 1.0 || maxCp < BAR_SIZE)
			return true;
		
		if (cp <= _cpUpdateDecCheck || cp >= _cpUpdateIncCheck)
		{
			if (cp == maxCp)
			{
				_cpUpdateIncCheck = cp + 1;
				_cpUpdateDecCheck = cp - _cpUpdateInterval;
			}
			else
			{
				final double doubleMulti = cp / _cpUpdateInterval;
				int intMulti = (int) doubleMulti;
				
				_cpUpdateDecCheck = _cpUpdateInterval * (doubleMulti < intMulti ? intMulti-- : intMulti);
				_cpUpdateIncCheck = _cpUpdateDecCheck + _cpUpdateInterval;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * @return True if a MP update should be done, otherwise false.
	 */
	private boolean needMpUpdate()
	{
		final double mp = _mp;
		final double maxMp = getMaxMp();
		
		if (mp <= 1.0 || maxMp < BAR_SIZE)
			return true;
		
		if (mp <= _mpUpdateDecCheck || mp >= _mpUpdateIncCheck)
		{
			if (mp == maxMp)
			{
				_mpUpdateIncCheck = mp + 1;
				_mpUpdateDecCheck = mp - _mpUpdateInterval;
			}
			else
			{
				final double doubleMulti = mp / _mpUpdateInterval;
				int intMulti = (int) doubleMulti;
				
				_mpUpdateDecCheck = _mpUpdateInterval * (doubleMulti < intMulti ? intMulti-- : intMulti);
				_mpUpdateIncCheck = _mpUpdateDecCheck + _mpUpdateInterval;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Send {@link StatusUpdate} packet to current {@link Player} and current HP, MP and Level to all other {@link Player}s of the {@link Party}.
	 */
	@Override
	public void broadcastStatusUpdate()
	{
		// Send StatusUpdate with current HP, MP and CP to this Player
		final StatusUpdate su = new StatusUpdate(_actor);
		su.addAttribute(StatusType.CUR_HP, (int) _hp);
		su.addAttribute(StatusType.CUR_MP, (int) _mp);
		su.addAttribute(StatusType.CUR_CP, (int) _cp);
		su.addAttribute(StatusType.MAX_CP, getMaxCp());
		_actor.sendPacket(su);
		
		final boolean needCpUpdate = needCpUpdate();
		final boolean needHpUpdate = needHpUpdate();
		
		// Check if a party is in progress and party window update is needed.
		final Party party = _actor.getParty();
		if (party != null && (needCpUpdate || needHpUpdate || needMpUpdate()))
			party.broadcastToPartyMembers(_actor, new PartySmallWindowUpdate(_actor));
		
		if (_actor.isInOlympiadMode() && _actor.isOlympiadStart() && (needCpUpdate || needHpUpdate))
		{
			final OlympiadGameTask game = OlympiadGameManager.getInstance().getOlympiadTask(_actor.getOlympiadGameId());
			if (game != null && game.isBattleStarted())
				game.getZone().broadcastStatusUpdate(_actor);
		}
		
		// In duel, MP updated only with CP or HP
		if (_actor.isInDuel() && (needCpUpdate || needHpUpdate))
		{
			final ExDuelUpdateUserInfo update = new ExDuelUpdateUserInfo(_actor);
			DuelManager.getInstance().broadcastToOppositeTeam(_actor, update);
		}
	}
	
	@Override
	public final int getSTR()
	{
		return (int) calcStat(Stats.STAT_STR, _actor.getTemplate().getBaseSTR(), null, null);
	}
	
	@Override
	public final int getDEX()
	{
		return (int) calcStat(Stats.STAT_DEX, _actor.getTemplate().getBaseDEX(), null, null);
	}
	
	@Override
	public final int getCON()
	{
		return (int) calcStat(Stats.STAT_CON, _actor.getTemplate().getBaseCON(), null, null);
	}
	
	@Override
	public int getINT()
	{
		return (int) calcStat(Stats.STAT_INT, _actor.getTemplate().getBaseINT(), null, null);
	}
	
	@Override
	public final int getMEN()
	{
		return (int) calcStat(Stats.STAT_MEN, _actor.getTemplate().getBaseMEN(), null, null);
	}
	
	@Override
	public final int getWIT()
	{
		return (int) calcStat(Stats.STAT_WIT, _actor.getTemplate().getBaseWIT(), null, null);
	}
	
	@Override
	public boolean addExp(long value)
	{
		if (!super.addExp(value))
			return false;
		
		_actor.sendPacket(new UserInfo(_actor));
		return true;
	}
	
	/**
	 * Add Experience and SP rewards to the Player, remove its Karma (if necessary) and Launch increase level task.
	 * <ul>
	 * <li>Remove Karma when the player kills Monster</li>
	 * <li>Send StatusUpdate to the Player</li>
	 * <li>Send a Server->Client System Message to the Player</li>
	 * <li>If the Player increases its level, send SocialAction (broadcast)</li>
	 * <li>If the Player increases its level, manage the increase level task (Max MP, Max MP, Recommandation, Expertise and beginner skills...)</li>
	 * <li>If the Player increases its level, send UserInfo to the Player</li>
	 * </ul>
	 * @param addToExp The Experience value to add
	 * @param addToSp The SP value to add
	 */
	@Override
	public boolean addExpAndSp(long addToExp, int addToSp)
	{
		if (!super.addExpAndSp(addToExp, addToSp))
			return false;
		
		SystemMessage sm;
		
		if (addToExp == 0 && addToSp > 0)
			sm = SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_SP).addNumber(addToSp);
		else if (addToExp > 0 && addToSp == 0)
			sm = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S1_EXPERIENCE).addNumber((int) addToExp);
		else
			sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_EARNED_S1_EXP_AND_S2_SP).addNumber((int) addToExp).addNumber(addToSp);
		
		_actor.sendPacket(sm);
		
		return true;
	}
	
	/**
	 * Add Experience and SP rewards to the Player, remove its Karma (if necessary) and Launch increase level task.
	 * <ul>
	 * <li>Remove Karma when the player kills Monster</li>
	 * <li>Send StatusUpdate to the Player</li>
	 * <li>Send a Server->Client System Message to the Player</li>
	 * <li>If the Player increases its level, send SocialAction (broadcast)</li>
	 * <li>If the Player increases its level, manage the increase level task (Max MP, Max MP, Recommandation, Expertise and beginner skills...)</li>
	 * <li>If the Player increases its level, send UserInfo to the Player</li>
	 * </ul>
	 * @param addToExp The Experience value to add
	 * @param addToSp The SP value to add
	 * @param rewards The list of players and summons, who done damage
	 * @return
	 */
	public boolean addExpAndSp(long addToExp, int addToSp, Map<Creature, RewardInfo> rewards)
	{
		// If this player has a pet, give the xp to the pet now (if any).
		if (_actor.hasPet())
		{
			final Pet pet = (Pet) _actor.getSummon();
			if (pet.getStatus().getExp() <= (pet.getTemplate().getPetDataEntry(81).maxExp() + 10000) && !pet.isDead() && pet.isIn3DRadius(_actor, Config.PARTY_RANGE))
			{
				long petExp = 0;
				int petSp = 0;
				
				int ratio = pet.getPetData().expType();
				if (ratio == -1)
				{
					RewardInfo r = rewards.get(pet);
					RewardInfo reward = rewards.get(_actor);
					if (r != null && reward != null)
					{
						final double damageDoneByPet = r.getDamage() / reward.getDamage();
						
						petExp = (long) (addToExp * damageDoneByPet);
						petSp = (int) (addToSp * damageDoneByPet);
					}
				}
				else
				{
					// now adjust the max ratio to avoid the owner earning negative exp/sp
					if (ratio > 100)
						ratio = 100;
					
					petExp = Math.round(addToExp * (1 - (ratio / 100.0)));
					petSp = (int) Math.round(addToSp * (1 - (ratio / 100.0)));
				}
				
				addToExp -= petExp;
				addToSp -= petSp;
				pet.addExpAndSp(petExp, petSp);
			}
		}
		return addExpAndSp(addToExp, addToSp);
	}
	
	@Override
	public boolean removeExpAndSp(long removeExp, int removeSp)
	{
		return removeExpAndSp(removeExp, removeSp, true);
	}
	
	public boolean removeExpAndSp(long removeExp, int removeSp, boolean sendMessage)
	{
		final int oldLevel = getLevel();
		
		if (!super.removeExpAndSp(removeExp, removeSp))
			return false;
		
		// Send messages.
		if (sendMessage)
		{
			if (removeExp > 0)
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EXP_DECREASED_BY_S1).addNumber((int) removeExp));
			
			if (removeSp > 0)
				_actor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.SP_DECREASED_S1).addNumber(removeSp));
			
			if (getLevel() < oldLevel)
				broadcastStatusUpdate();
		}
		return true;
	}
	
	@Override
	public final boolean addLevel(byte value)
	{
		if (getLevel() + value > PlayerLevelData.getInstance().getRealMaxLevel())
			return false;
		
		boolean levelIncreased = super.addLevel(value);
		
		if (levelIncreased)
		{
			final QuestState qs = _actor.getQuestList().getQuestState("Tutorial");
			if (qs != null)
				qs.getQuest().notifyEvent("CE40", null, _actor);
			
			setCp(getMaxCp());
			
			_actor.broadcastPacket(new SocialAction(_actor, 15));
			_actor.sendPacket(SystemMessageId.YOU_INCREASED_YOUR_LEVEL);
		}
		
		// Refresh player skills (autoGet skills or all available skills if Config.AUTO_LEARN_SKILLS is activated).
		_actor.giveSkills();
		
		final Clan clan = _actor.getClan();
		if (clan != null)
		{
			final ClanMember member = clan.getClanMember(_actor.getObjectId());
			if (member != null)
				member.refreshLevel();
			
			clan.broadcastToMembers(new PledgeShowMemberListUpdate(_actor));
		}
		
		// Recalculate the party level
		final Party party = _actor.getParty();
		if (party != null)
			party.recalculateLevel();
		
		// Update the overloaded status of the player
		_actor.refreshWeightPenalty();
		// Update the expertise status of the player
		_actor.refreshExpertisePenalty();
		// Send UserInfo to the player
		_actor.sendPacket(new UserInfo(_actor));
		
		return levelIncreased;
	}
	
	@Override
	public final long getExp()
	{
		if (_actor.isSubClassActive())
			return _actor.getSubClasses().get(_actor.getClassIndex()).getExp();
		
		return super.getExp();
	}
	
	@Override
	public final void setExp(long value)
	{
		if (_actor.isSubClassActive())
			_actor.getSubClasses().get(_actor.getClassIndex()).setExp(value);
		else
			super.setExp(value);
	}
	
	@Override
	public final int getLevel()
	{
		if (_actor.isSubClassActive())
			return _actor.getSubClasses().get(_actor.getClassIndex()).getLevel();
		
		return super.getLevel();
	}
	
	@Override
	public final void setLevel(int value)
	{
		value = Math.min(value, PlayerLevelData.getInstance().getRealMaxLevel());
		
		if (_actor.isSubClassActive())
			_actor.getSubClasses().get(_actor.getClassIndex()).setLevel(value);
		else
			super.setLevel(value);
	}
	
	@Override
	public final int getMaxCp()
	{
		// Get the Max CP (base+modifier) of the player
		int val = (int) calcStat(Stats.MAX_CP, _actor.getTemplate().getBaseCpMax(getLevel()), null, null);
		if (val != _oldMaxCp)
		{
			_oldMaxCp = val;
			
			// Launch a regen task if the new Max CP is higher than the old one
			if (_cp != val)
				setCp(_cp); // trigger start of regeneration
		}
		return val;
	}
	
	@Override
	public final int getMaxHp()
	{
		// Get the Max HP (base+modifier) of the player
		int val = super.getMaxHp();
		if (val != _oldMaxHp)
		{
			_oldMaxHp = val;
			
			// Launch a regen task if the new Max HP is higher than the old one
			if (_hp != val)
				setHp(_hp); // trigger start of regeneration
		}
		
		return val;
	}
	
	@Override
	public final int getMaxMp()
	{
		// Get the Max MP (base+modifier) of the player
		int val = super.getMaxMp();
		
		if (val != _oldMaxMp)
		{
			_oldMaxMp = val;
			
			// Launch a regen task if the new Max MP is higher than the old one
			if (_mp != val)
				setMp(_mp); // trigger start of regeneration
		}
		
		return val;
	}
	
	@Override
	public final double getRegenHp()
	{
		// Get value.
		double value = super.getRegenHp();
		
		final Clan clan = _actor.getClan();
		if (clan != null)
		{
			// Calculate siege bonus.
			final Siege siege = CastleManager.getInstance().getActiveSiege(_actor);
			if (siege != null && siege.checkSide(clan, SiegeSide.ATTACKER))
			{
				final Npc flag = clan.getFlag();
				if (flag != null && _actor.isIn3DRadius(flag, 200))
					value *= 1.5;
			}
			
			// Calculate clan hall bonus.
			if (_actor.isInsideZone(ZoneId.CLAN_HALL))
			{
				final int chId = clan.getClanHallId();
				if (chId > 0)
				{
					final ClanHall ch = ClanHallManager.getInstance().getClanHall(chId);
					if (ch != null)
					{
						final ClanHallFunction chf = ch.getFunction(ClanHall.FUNC_RESTORE_HP);
						if (chf != null)
							value *= 1 + chf.getLvl() / 100.0;
					}
				}
			}
		}
		
		// Calculate movement bonus.
		if (_actor.isSitting())
			value *= 1.5;
		else if (!_actor.isMoving())
			value *= 1.1;
		else if (_actor.isRunning())
			value *= 0.7;
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			value *= wp.getRegenerationMultiplier();
		
		// Calculate Mother Tree bonus.
		if (_actor.isInsideZone(ZoneId.MOTHER_TREE))
		{
			final MotherTreeZone zone = ZoneManager.getInstance().getZone(_actor, MotherTreeZone.class);
			if (zone != null)
				value += zone.getHpRegenBonus();
		}
		
		return value;
	}
	
	@Override
	public final double getRegenMp()
	{
		// Get value.
		double value = super.getRegenMp();
		
		// Calculate clan hall bonus.
		if (_actor.isInsideZone(ZoneId.CLAN_HALL) && _actor.getClan() != null)
		{
			final int chId = _actor.getClan().getClanHallId();
			if (chId > 0)
			{
				final ClanHall ch = ClanHallManager.getInstance().getClanHall(chId);
				if (ch != null)
				{
					final ClanHallFunction chf = ch.getFunction(ClanHall.FUNC_RESTORE_MP);
					if (chf != null)
						value *= 1 + chf.getLvl() / 100.0;
				}
			}
		}
		
		// Calculate movement bonus.
		if (_actor.isSitting())
			value *= 1.5;
		else if (!_actor.isMoving())
			value *= 1.1;
		else if (_actor.isRunning())
			value *= 0.7;
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			value *= wp.getRegenerationMultiplier();
		
		// Calculate Mother Tree bonus.
		if (_actor.isInsideZone(ZoneId.MOTHER_TREE))
		{
			final MotherTreeZone zone = ZoneManager.getInstance().getZone(_actor, MotherTreeZone.class);
			if (zone != null)
				value += zone.getMpRegenBonus();
		}
		
		return value;
	}
	
	/**
	 * @return The CP regeneration of this {@link Creature}.
	 */
	public final double getRegenCp()
	{
		// Get value.
		double value = calcStat(Stats.REGENERATE_CP_RATE, _actor.getTemplate().getBaseCpRegen(getLevel()) * Config.CP_REGEN_MULTIPLIER, null, null);
		
		// Calculate movement bonus.
		if (_actor.isSitting())
			value *= 1.5;
		else if (!_actor.isMoving())
			value *= 1.1;
		else if (_actor.isRunning())
			value *= 0.7;
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			value *= wp.getRegenerationMultiplier();
		
		return value;
	}
	
	@Override
	public final int getSp()
	{
		if (_actor.isSubClassActive())
			return _actor.getSubClasses().get(_actor.getClassIndex()).getSp();
		
		return super.getSp();
	}
	
	@Override
	public final void setSp(int value)
	{
		if (_actor.isSubClassActive())
			_actor.getSubClasses().get(_actor.getClassIndex()).setSp(value);
		else
			super.setSp(value);
		
		StatusUpdate su = new StatusUpdate(_actor);
		su.addAttribute(StatusType.SP, getSp());
		_actor.sendPacket(su);
	}
	
	@Override
	public int getBaseRunSpeed()
	{
		if (_actor.isMounted())
		{
			int base = (_actor.isFlying()) ? _actor.getPetDataEntry().mountFlySpeed() : _actor.getPetDataEntry().mountBaseSpeed();
			
			if (_actor.getMountLevel() - getLevel() > 9)
				base /= 2;
			
			if (_actor.checkFoodState(_actor.getPetTemplate().getHungryLimit()))
				base /= 2;
			
			return base;
		}
		
		return super.getBaseRunSpeed();
	}
	
	public int getBaseSwimSpeed()
	{
		if (_actor.isMounted())
		{
			int base = _actor.getPetDataEntry().mountWaterSpeed();
			
			if (_actor.getMountLevel() - getLevel() > 9)
				base /= 2;
			
			if (_actor.checkFoodState(_actor.getPetTemplate().getHungryLimit()))
				base /= 2;
			
			return base;
		}
		
		return _actor.getTemplate().getBaseSwimSpeed();
	}
	
	@Override
	public float getMoveSpeed()
	{
		// Get base value, use swimming speed in water.
		float baseValue = (_actor.isInWater()) ? getBaseSwimSpeed() : getBaseMoveSpeed();
		
		// Calculate swamp area malus.
		if (_actor.isInsideZone(ZoneId.SWAMP))
		{
			final SwampZone zone = ZoneManager.getInstance().getZone(_actor, SwampZone.class);
			if (zone != null)
				baseValue *= (100 + zone.getMoveBonus()) / 100.0;
		}
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			baseValue *= wp.getSpeedMultiplier();
		
		// Calculate armor grade penalty malus.
		final int agp = _actor.getArmorGradePenalty();
		if (agp > 0)
			baseValue *= Math.pow(0.84, agp);
		
		return (float) calcStat(Stats.RUN_SPEED, baseValue, null, null);
	}
	
	@Override
	public float getRealMoveSpeed(boolean isStillWalking)
	{
		// Get base value, use swimming speed in water.
		float baseValue = (_actor.isInWater()) ? getBaseSwimSpeed() : ((isStillWalking || !_actor.isRunning()) ? getBaseWalkSpeed() : getBaseRunSpeed());
		
		// Calculate swamp area malus.
		if (_actor.isInsideZone(ZoneId.SWAMP))
		{
			final SwampZone zone = ZoneManager.getInstance().getZone(_actor, SwampZone.class);
			if (zone != null)
				baseValue *= (100 + zone.getMoveBonus()) / 100.0;
		}
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			baseValue *= wp.getSpeedMultiplier();
		
		// Calculate armor grade penalty malus.
		final int agp = _actor.getArmorGradePenalty();
		if (agp > 0)
			baseValue *= Math.pow(0.84, agp);
		
		return (float) calcStat(Stats.RUN_SPEED, baseValue, null, null);
	}
	
	@Override
	public int getMAtk(Creature target, L2Skill skill)
	{
		if (_actor.isMounted())
		{
			double base = _actor.getPetDataEntry().mountMatk();
			
			final int diffLevel = _actor.getMountLevel() - getLevel();
			if (diffLevel > 4)
				base *= 0.5 - ((Math.min(diffLevel, 10) - 5) * 0.05);
			
			return (int) calcStat(Stats.MAGIC_ATTACK, base, null, null);
		}
		
		return super.getMAtk(target, skill);
	}
	
	@Override
	public int getMAtkSpd()
	{
		double base = 333;
		
		if (_actor.isMounted() && _actor.checkFoodState(_actor.getPetTemplate().getHungryLimit()))
			base /= 2;
		
		final int penalty = _actor.getArmorGradePenalty();
		if (penalty > 0)
			base *= Math.pow(0.84, penalty);
		
		return (int) calcStat(Stats.MAGIC_ATTACK_SPEED, base, null, null);
	}
	
	@Override
	public int getPAtk(Creature target)
	{
		if (_actor.isMounted())
		{
			double base = _actor.getPetDataEntry().mountPatk();
			
			final int diffLevel = _actor.getMountLevel() - getLevel();
			if (diffLevel > 4)
				base *= 0.5 - ((Math.min(diffLevel, 10) - 5) * 0.05);
			
			return (int) calcStat(Stats.POWER_ATTACK, base, null, null);
		}
		
		return super.getPAtk(target);
	}
	
	@Override
	public int getPAtkSpd()
	{
		if (_actor.isFlying())
			return (_actor.checkFoodState(_actor.getPetTemplate().getHungryLimit())) ? 150 : 300;
		
		if (_actor.isRiding())
		{
			double base = _actor.getPetDataEntry().mountAtkSpd();
			
			if (_actor.checkFoodState(_actor.getPetTemplate().getHungryLimit()))
				base /= 2;
			
			return (int) calcStat(Stats.POWER_ATTACK_SPEED, base, null, null);
		}
		
		return super.getPAtkSpd();
	}
	
	@Override
	public int getEvasionRate(Creature target)
	{
		int val = super.getEvasionRate(target);
		
		final int penalty = _actor.getArmorGradePenalty();
		if (penalty > 0)
			val -= (2 * penalty);
		
		return val;
	}
	
	@Override
	public int getAccuracy()
	{
		int val = super.getAccuracy();
		
		if (_actor.getWeaponGradePenalty())
			val -= 20;
		
		return val;
	}
	
	@Override
	public int getPhysicalAttackRange()
	{
		return (int) calcStat(Stats.POWER_ATTACK_RANGE, _actor.getAttackType().getRange(), null, null);
	}
	
	@Override
	public long getExpForLevel(int level)
	{
		final PlayerLevel pl = PlayerLevelData.getInstance().getPlayerLevel(level);
		if (pl == null)
			return 0;
		
		return pl.requiredExpToLevelUp();
	}
	
	@Override
	public long getExpForThisLevel()
	{
		final PlayerLevel pl = PlayerLevelData.getInstance().getPlayerLevel(getLevel());
		if (pl == null)
			return 0;
		
		return pl.requiredExpToLevelUp();
	}
	
	@Override
	public long getExpForNextLevel()
	{
		final PlayerLevel pl = PlayerLevelData.getInstance().getPlayerLevel(getLevel() + 1);
		if (pl == null)
			return 0;
		
		return pl.requiredExpToLevelUp();
	}
	
	/**
	 * A check used in multiple scenarii (subclass, olympiad registration, quest bypass, etc).
	 * @return True if this {@link Player} exceeded the authorized inventory slots ratio (which is 80%), or false otherwise.
	 */
	public boolean isOverburden()
	{
		return (double) _actor.getInventory().getSize() / getInventoryLimit() >= 0.8;
	}
	
	public int getInventoryLimit()
	{
		return ((_actor.getRace() == ClassRace.DWARF) ? Config.INVENTORY_MAXIMUM_DWARF : Config.INVENTORY_MAXIMUM_NO_DWARF) + (int) calcStat(Stats.INV_LIM, 0, null, null);
	}
	
	public int getWareHouseLimit()
	{
		return ((_actor.getRace() == ClassRace.DWARF) ? Config.WAREHOUSE_SLOTS_DWARF : Config.WAREHOUSE_SLOTS_NO_DWARF) + (int) calcStat(Stats.WH_LIM, 0, null, null);
	}
	
	public int getPrivateSellStoreLimit()
	{
		return ((_actor.getRace() == ClassRace.DWARF) ? Config.MAX_PVTSTORE_SLOTS_DWARF : Config.MAX_PVTSTORE_SLOTS_OTHER) + (int) calcStat(Stats.P_SELL_LIM, 0, null, null);
	}
	
	public int getPrivateBuyStoreLimit()
	{
		return ((_actor.getRace() == ClassRace.DWARF) ? Config.MAX_PVTSTORE_SLOTS_DWARF : Config.MAX_PVTSTORE_SLOTS_OTHER) + (int) calcStat(Stats.P_BUY_LIM, 0, null, null);
	}
	
	public int getFreightLimit()
	{
		return Config.FREIGHT_SLOTS + (int) calcStat(Stats.FREIGHT_LIM, 0, null, null);
	}
	
	public int getDwarfRecipeLimit()
	{
		return Config.DWARF_RECIPE_LIMIT + (int) calcStat(Stats.REC_D_LIM, 0, null, null);
	}
	
	public int getCommonRecipeLimit()
	{
		return Config.COMMON_RECIPE_LIMIT + (int) calcStat(Stats.REC_C_LIM, 0, null, null);
	}
}