package com.shnok.javaserver.gameserver.model.actor.container.creature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.group.Party;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameManager;
import com.shnok.javaserver.gameserver.model.olympiad.OlympiadGameTask;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.AbnormalStatusUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.ExOlympiadSpelledInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.PartySpelled;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.effects.EffectTemplate;

public class EffectList
{
	private static final AbstractEffect[] EMPTY_EFFECTS = new AbstractEffect[0];
	
	private List<AbstractEffect> _buffs;
	private List<AbstractEffect> _debuffs;
	
	// The table containing the List of all stacked effect in progress for each Stack group Identifier
	private Map<String, List<AbstractEffect>> _stackedEffects;
	
	private boolean _queuesInitialized = false;
	private LinkedBlockingQueue<AbstractEffect> _addQueue;
	private LinkedBlockingQueue<AbstractEffect> _removeQueue;
	private final AtomicBoolean queueLock = new AtomicBoolean();
	private int _effectFlags;
	
	// only party icons need to be updated
	private boolean _partyOnly = false;
	
	// Owner of this list
	private final Creature _owner;
	
	private AbstractEffect[] _effectCache;
	private volatile boolean _rebuildCache = true;
	private final Object _buildEffectLock = new Object();
	
	public EffectList(Creature owner)
	{
		_owner = owner;
	}
	
	/**
	 * Returns all effects affecting stored in this CharEffectList
	 * @return
	 */
	public final AbstractEffect[] getAllEffects()
	{
		// If no effect is active, return EMPTY_EFFECTS
		if ((_buffs == null || _buffs.isEmpty()) && (_debuffs == null || _debuffs.isEmpty()))
			return EMPTY_EFFECTS;
		
		synchronized (_buildEffectLock)
		{
			// If we dont need to rebuild the cache, just return the current one.
			if (!_rebuildCache)
				return _effectCache;
			
			_rebuildCache = false;
			
			// Create a copy of the effects
			List<AbstractEffect> temp = new ArrayList<>();
			
			// Add all buffs and all debuffs
			if (_buffs != null && !_buffs.isEmpty())
				temp.addAll(_buffs);
			if (_debuffs != null && !_debuffs.isEmpty())
				temp.addAll(_debuffs);
			
			// Return all effects in an array
			AbstractEffect[] tempArray = new AbstractEffect[temp.size()];
			temp.toArray(tempArray);
			return (_effectCache = tempArray);
		}
	}
	
	/**
	 * Returns the first effect matching the given EffectType
	 * @param tp
	 * @return
	 */
	public final AbstractEffect getFirstEffect(EffectType tp)
	{
		AbstractEffect effectNotInUse = null;
		
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				if (e == null)
					continue;
				
				if (e.getEffectType() == tp)
				{
					if (e.getInUse())
						return e;
					
					effectNotInUse = e;
				}
			}
		}
		
		if (effectNotInUse == null && _debuffs != null && !_debuffs.isEmpty())
		{
			for (AbstractEffect e : _debuffs)
			{
				if (e == null)
					continue;
				
				if (e.getEffectType() == tp)
				{
					if (e.getInUse())
						return e;
					
					effectNotInUse = e;
				}
			}
		}
		return effectNotInUse;
	}
	
	/**
	 * Returns the first effect matching the given L2Skill
	 * @param skill
	 * @return
	 */
	public final AbstractEffect getFirstEffect(L2Skill skill)
	{
		AbstractEffect effectNotInUse = null;
		
		if (skill.isDebuff())
		{
			if (_debuffs != null && !_debuffs.isEmpty())
			{
				for (AbstractEffect e : _debuffs)
				{
					if (e == null)
						continue;
					
					if (e.getSkill() == skill)
					{
						if (e.getInUse())
							return e;
						
						effectNotInUse = e;
					}
				}
			}
		}
		else
		{
			if (_buffs != null && !_buffs.isEmpty())
			{
				for (AbstractEffect e : _buffs)
				{
					if (e == null)
						continue;
					
					if (e.getSkill() == skill)
					{
						if (e.getInUse())
							return e;
						
						effectNotInUse = e;
					}
				}
			}
		}
		return effectNotInUse;
	}
	
	/**
	 * @param skillId The skill id to check.
	 * @return the first effect matching the given skillId.
	 */
	public final AbstractEffect getFirstEffect(int skillId)
	{
		AbstractEffect effectNotInUse = null;
		
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				if (e == null)
					continue;
				
				if (e.getSkill().getId() == skillId)
				{
					if (e.getInUse())
						return e;
					
					effectNotInUse = e;
				}
			}
		}
		
		if (effectNotInUse == null && _debuffs != null && !_debuffs.isEmpty())
		{
			for (AbstractEffect e : _debuffs)
			{
				if (e == null)
					continue;
				if (e.getSkill().getId() == skillId)
				{
					if (e.getInUse())
						return e;
					
					effectNotInUse = e;
				}
			}
		}
		return effectNotInUse;
	}
	
	/**
	 * Checks if the given skill stacks with an existing one.
	 * @param checkSkill the skill to be checked
	 * @return Returns whether or not this skill will stack
	 */
	private boolean doesStack(L2Skill checkSkill)
	{
		if (_buffs == null || _buffs.isEmpty())
			return false;
		
		final List<EffectTemplate> templates = checkSkill.getEffectTemplates();
		if (templates == null || templates.isEmpty())
			return false;
		
		final String stackType = templates.get(0).getStackType();
		if (stackType == null || "none".equals(stackType))
			return false;
		
		for (AbstractEffect e : _buffs)
		{
			if (e.getTemplate().getStackType() != null && e.getTemplate().getStackType().equals(stackType))
				return true;
		}
		return false;
	}
	
	/**
	 * Return the number of buffs in this CharEffectList not counting Songs/Dances
	 * @return
	 */
	public int getBuffCount()
	{
		if (_buffs == null || _buffs.isEmpty())
			return 0;
		
		int buffCount = 0;
		for (AbstractEffect e : _buffs)
		{
			if (e != null && e.getTemplate().showIcon() && !e.getSkill().is7Signs())
			{
				switch (e.getSkill().getSkillType())
				{
					case BUFF, COMBATPOINTHEAL, REFLECT, HEAL_PERCENT, HEAL_STATIC, MANAHEAL_PERCENT:
						buffCount++;
				}
			}
		}
		return buffCount;
	}
	
	/**
	 * Return the number of Songs/Dances in this CharEffectList
	 * @return
	 */
	public int getDanceCount()
	{
		if (_buffs == null || _buffs.isEmpty())
			return 0;
		
		int danceCount = 0;
		for (AbstractEffect e : _buffs)
		{
			if (e != null && e.getSkill().isDance() && e.getInUse())
				danceCount++;
		}
		return danceCount;
	}
	
	/**
	 * Exits all effects in this CharEffectList
	 */
	public final void stopAllEffects()
	{
		// Get all active skills effects from this list
		AbstractEffect[] effects = getAllEffects();
		
		// Exit them
		for (AbstractEffect e : effects)
		{
			if (e != null)
				e.exit(true);
		}
	}
	
	/**
	 * Exits all effects in this CharEffectList
	 */
	public final void stopAllEffectsExceptThoseThatLastThroughDeath()
	{
		// Get all active skills effects from this list
		AbstractEffect[] effects = getAllEffects();
		
		// Exit them
		for (AbstractEffect e : effects)
		{
			if (e != null && !e.getSkill().isStayAfterDeath())
				e.exit(true);
		}
	}
	
	/**
	 * Exit all toggle-type effects
	 */
	public void stopAllToggles()
	{
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				if (e != null && e.getSkill().isToggle())
					e.exit();
			}
		}
	}
	
	/**
	 * Exit all effects having a specified type
	 * @param type
	 */
	public final void stopEffects(EffectType type)
	{
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				// Get active skills effects of the selected type
				if (e != null && e.getEffectType() == type)
					e.exit();
			}
		}
		
		if (_debuffs != null && !_debuffs.isEmpty())
		{
			for (AbstractEffect e : _debuffs)
			{
				// Get active skills effects of the selected type
				if (e != null && e.getEffectType() == type)
					e.exit();
			}
		}
	}
	
	/**
	 * Exits all effects created by a specific skillId
	 * @param skillId
	 */
	public final void stopSkillEffects(int skillId)
	{
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				if (e != null && e.getSkill().getId() == skillId)
					e.exit();
			}
		}
		
		if (_debuffs != null && !_debuffs.isEmpty())
		{
			for (AbstractEffect e : _debuffs)
			{
				if (e != null && e.getSkill().getId() == skillId)
					e.exit();
			}
		}
	}
	
	/**
	 * Exits all effects created by a specific skill type
	 * @param skillType skill type
	 * @param negateLvl
	 */
	public final void stopSkillEffects(SkillType skillType, int negateLvl)
	{
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				if (e != null && (e.getSkill().getSkillType() == skillType || (e.getSkill().getEffectType() != null && e.getSkill().getEffectType() == skillType)) && (negateLvl == -1 || (e.getSkill().getEffectType() != null && e.getSkill().getEffectAbnormalLvl() >= 0 && e.getSkill().getEffectAbnormalLvl() <= negateLvl) || (e.getSkill().getAbnormalLvl() >= 0 && e.getSkill().getAbnormalLvl() <= negateLvl)))
					e.exit();
			}
		}
		
		if (_debuffs != null && !_debuffs.isEmpty())
		{
			for (AbstractEffect e : _debuffs)
			{
				if (e != null && (e.getSkill().getSkillType() == skillType || (e.getSkill().getEffectType() != null && e.getSkill().getEffectType() == skillType)) && (negateLvl == -1 || (e.getSkill().getEffectType() != null && e.getSkill().getEffectAbnormalLvl() >= 0 && e.getSkill().getEffectAbnormalLvl() <= negateLvl) || (e.getSkill().getAbnormalLvl() >= 0 && e.getSkill().getAbnormalLvl() <= negateLvl)))
					e.exit();
			}
		}
	}
	
	public void updateEffectIcons(boolean partyOnly)
	{
		if (_buffs == null && _debuffs == null)
			return;
		
		if (partyOnly)
			_partyOnly = true;
		
		queueRunner();
	}
	
	public void queueEffect(AbstractEffect effect, boolean remove)
	{
		if (effect == null)
			return;
		
		if (!_queuesInitialized)
			init();
		
		if (remove)
			_removeQueue.offer(effect);
		else
			_addQueue.offer(effect);
		
		queueRunner();
	}
	
	private synchronized void init()
	{
		if (_queuesInitialized)
			return;
		
		_addQueue = new LinkedBlockingQueue<>();
		_removeQueue = new LinkedBlockingQueue<>();
		_queuesInitialized = true;
	}
	
	private void queueRunner()
	{
		if (!queueLock.compareAndSet(false, true))
			return;
		
		try
		{
			AbstractEffect effect;
			do
			{
				// remove has more priority than add so removing all effects from queue first
				while ((effect = _removeQueue.poll()) != null)
				{
					removeEffectFromQueue(effect);
					_partyOnly = false;
				}
				
				if ((effect = _addQueue.poll()) != null)
				{
					addEffectFromQueue(effect);
					_partyOnly = false;
				}
			}
			while (!_addQueue.isEmpty() || !_removeQueue.isEmpty());
			
			computeEffectFlags();
			updateEffectIcons();
		}
		finally
		{
			queueLock.set(false);
		}
	}
	
	protected void removeEffectFromQueue(AbstractEffect effect)
	{
		if (effect == null)
			return;
		
		List<AbstractEffect> effectList;
		
		// array modified, then rebuild on next request
		_rebuildCache = true;
		
		if (effect.getSkill().isDebuff())
		{
			if (_debuffs == null)
				return;
			
			effectList = _debuffs;
		}
		else
		{
			if (_buffs == null)
				return;
			
			effectList = _buffs;
		}
		
		final String stackType = effect.getTemplate().getStackType();
		
		if ("none".equals(stackType))
		{
			// Remove Func added by this effect from the Creature Calculator
			_owner.removeStatsByOwner(effect);
		}
		else
		{
			if (_stackedEffects == null)
				return;
			
			// Get the list of all stacked effects corresponding to the stack type to add.
			final List<AbstractEffect> stackQueue = _stackedEffects.get(stackType);
			if (stackQueue == null || stackQueue.isEmpty())
				return;
			
			final int index = stackQueue.indexOf(effect);
			if (index >= 0)
			{
				// Remove the effect from the stack group
				stackQueue.remove(effect);
				
				// Check if the first stacked effect was the effect to remove
				if (index == 0)
				{
					// Remove all its Func objects from the Creature calculator set
					_owner.removeStatsByOwner(effect);
					
					// Check if there's another effect in the Stack Group
					if (!stackQueue.isEmpty())
					{
						// Set the effect to In Use and add its Funcs to the Calculator set of the Creature.
						final AbstractEffect newStackedEffect = listsContains(stackQueue.get(0));
						if (newStackedEffect != null && newStackedEffect.setInUse(true))
							_owner.addStatFuncs(newStackedEffect.getStatFuncs());
					}
				}
				
				// Update the Stack Group table _stackedEffects of the Creature.
				if (stackQueue.isEmpty())
					_stackedEffects.remove(stackType);
				else
					_stackedEffects.put(stackType, stackQueue);
			}
		}
		
		// Remove the active skill L2effect from _effects of the Creature
		if (effectList.remove(effect) && _owner instanceof Player && effect.getTemplate().showIcon())
		{
			SystemMessage sm;
			if (effect.getSkill().isToggle())
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_BEEN_ABORTED);
			else if (effect.getCount() == 0)
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_WORN_OFF);
			else
				sm = SystemMessage.getSystemMessage(SystemMessageId.EFFECT_S1_DISAPPEARED);
			
			sm.addSkillName(effect);
			_owner.sendPacket(sm);
		}
	}
	
	protected void addEffectFromQueue(AbstractEffect newEffect)
	{
		if (newEffect == null)
			return;
		
		L2Skill newSkill = newEffect.getSkill();
		
		// array modified, then rebuild on next request
		_rebuildCache = true;
		
		if (isAffected(newEffect.getEffectFlags()) && !newEffect.onSameEffect(null))
		{
			newEffect.stopEffectTask();
			return;
		}
		
		if (newSkill.isDebuff())
		{
			if (_debuffs == null)
				_debuffs = new CopyOnWriteArrayList<>();
			
			for (AbstractEffect e : _debuffs)
			{
				// Started scheduled timer needs to be canceled.
				if (e.isIdentical(newEffect))
				{
					newEffect.stopEffectTask();
					return;
				}
			}
			_debuffs.add(newEffect);
		}
		else
		{
			if (_buffs == null)
				_buffs = new CopyOnWriteArrayList<>();
			
			// Started scheduled timer needs to be canceled.
			for (AbstractEffect e : _buffs)
			{
				if (e.isIdentical(newEffect))
					e.exit();
			}
			
			// if max buffs, no herb effects are used, even if they would replace one old
			if (newEffect.isHerbEffect() && getBuffCount() >= _owner.getMaxBuffCount())
			{
				newEffect.stopEffectTask();
				return;
			}
			
			// Remove first buff when buff list is full
			if (!doesStack(newSkill) && !newSkill.is7Signs())
			{
				int effectsToRemove = getBuffCount() - _owner.getMaxBuffCount();
				if (effectsToRemove >= 0)
				{
					switch (newSkill.getSkillType())
					{
						case BUFF, REFLECT, HEAL_PERCENT, HEAL_STATIC, MANAHEAL_PERCENT, COMBATPOINTHEAL:
							for (AbstractEffect e : _buffs)
							{
								if (e == null)
									continue;
								
								switch (e.getSkill().getSkillType())
								{
									case BUFF, REFLECT, HEAL_PERCENT, HEAL_STATIC, MANAHEAL_PERCENT, COMBATPOINTHEAL:
										e.exit();
										effectsToRemove--;
										break;
									
									default:
										continue;
								}
								
								if (effectsToRemove < 0)
									break;
							}
							break;
					}
				}
			}
			
			// Icons order: buffs then toggles
			if (newSkill.isToggle())
				_buffs.add(newEffect);
			else
			{
				int pos = 0;
				for (AbstractEffect e : _buffs)
				{
					if (e == null || e.getSkill().isToggle() || e.getSkill().is7Signs())
						continue;
					
					pos++;
				}
				_buffs.add(pos, newEffect);
			}
		}
		
		final String stackType = newEffect.getTemplate().getStackType();
		
		// Check if a stack group is defined for this effect
		if ("none".equals(stackType))
		{
			if (newEffect.setInUse(true))
				_owner.addStatFuncs(newEffect.getStatFuncs());
			
			return;
		}
		
		AbstractEffect effectToAdd = null;
		AbstractEffect effectToRemove = null;
		
		if (_stackedEffects == null)
			_stackedEffects = new HashMap<>();
		
		// Get the list of all stacked effects corresponding to the stack type to add.
		List<AbstractEffect> stackQueue = _stackedEffects.get(stackType);
		if (stackQueue != null)
		{
			int pos = 0;
			if (!stackQueue.isEmpty())
			{
				// Get the first stacked effect of the Stack group selected
				effectToRemove = listsContains(stackQueue.get(0));
				
				// Create an Iterator to go through the list of stacked effects in progress on the Creature
				Iterator<AbstractEffect> queueIterator = stackQueue.iterator();
				
				while (queueIterator.hasNext())
				{
					if (newEffect.getTemplate().getStackOrder() < queueIterator.next().getTemplate().getStackOrder())
						pos++;
					else
						break;
				}
				// Add the new effect to the Stack list in function of its position in the Stack group
				stackQueue.add(pos, newEffect);
				
				// skill.exit() could be used, if the users don't wish to see "effect
				// removed" always when a timer goes off, even if the buff isn't active
				// any more (has been replaced). but then check e.g. npc hold and raid petrification.
				if (Config.EFFECT_CANCELING && !newEffect.isHerbEffect() && stackQueue.size() > 1)
				{
					if (newSkill.isDebuff())
						_debuffs.remove(stackQueue.remove(1));
					else
						_buffs.remove(stackQueue.remove(1));
				}
			}
			else
				stackQueue.add(0, newEffect);
		}
		else
		{
			stackQueue = new ArrayList<>();
			stackQueue.add(0, newEffect);
		}
		
		// Update the Stack Group table _stackedEffects of the Creature
		_stackedEffects.put(stackType, stackQueue);
		
		// Get the first stacked effect of the Stack group selected
		if (!stackQueue.isEmpty())
			effectToAdd = listsContains(stackQueue.get(0));
		
		if (effectToRemove != effectToAdd)
		{
			if (effectToRemove != null)
			{
				// Remove all Func objects corresponding to this stacked effect from the Calculator set of the Creature
				_owner.removeStatsByOwner(effectToRemove);
				
				effectToRemove.setInUse(false);
				
				if (_owner instanceof Player && effectToRemove.getTemplate().showIcon())
					_owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.EFFECT_S1_DISAPPEARED).addSkillName(effectToRemove.getSkill()));
			}
			
			if (effectToAdd != null && effectToAdd.setInUse(true))
			{
				_owner.addStatFuncs(effectToAdd.getStatFuncs());
				
				if (_owner instanceof Player && effectToAdd.getTemplate().showIcon())
					_owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT).addSkillName(effectToAdd.getSkill()));
			}
		}
	}
	
	protected void updateEffectIcons()
	{
		if (!(_owner instanceof Playable))
			return;
		
		AbnormalStatusUpdate mi = null;
		PartySpelled ps = null;
		ExOlympiadSpelledInfo os = null;
		
		if (_owner instanceof Player player)
		{
			if (_partyOnly)
				_partyOnly = false;
			else
				mi = new AbnormalStatusUpdate();
			
			if (player.isInParty())
				ps = new PartySpelled(player);
			
			if (player.isInOlympiadMode() && player.isOlympiadStart())
				os = new ExOlympiadSpelledInfo(player);
		}
		else if (_owner instanceof Summon summon)
			ps = new PartySpelled(summon);
		
		if (_buffs != null && !_buffs.isEmpty())
		{
			for (AbstractEffect e : _buffs)
			{
				if (e == null || !e.getTemplate().showIcon() || e.getEffectType() == EffectType.SIGNET_GROUND)
					continue;
				
				if (e.getInUse())
				{
					if (mi != null)
						e.addIcon(mi);
					
					if (ps != null)
						e.addPartySpelledIcon(ps);
					
					if (os != null)
						e.addOlympiadSpelledIcon(os);
				}
			}
		}
		
		if (_debuffs != null && !_debuffs.isEmpty())
		{
			for (AbstractEffect e : _debuffs)
			{
				if (e == null || !e.getTemplate().showIcon() || e.getEffectType() == EffectType.SIGNET_GROUND)
					continue;
				
				if (e.getInUse())
				{
					if (mi != null)
						e.addIcon(mi);
					
					if (ps != null)
						e.addPartySpelledIcon(ps);
					
					if (os != null)
						e.addOlympiadSpelledIcon(os);
				}
			}
		}
		
		if (mi != null)
			_owner.sendPacket(mi);
		
		if (ps != null)
		{
			if (_owner instanceof Summon summon)
			{
				final Player summonOwner = summon.getOwner();
				if (summonOwner != null)
				{
					final Party party = summonOwner.getParty();
					if (party != null)
						party.broadcastPacket(ps);
					else
						summonOwner.sendPacket(ps);
				}
			}
			else if (_owner instanceof Player player && player.isInParty())
				player.getParty().broadcastPacket(ps);
		}
		
		if (os != null)
		{
			final OlympiadGameTask game = OlympiadGameManager.getInstance().getOlympiadTask(((Player) _owner).getOlympiadGameId());
			if (game != null && game.isBattleStarted())
				game.getZone().broadcastPacketToObservers(os);
		}
	}
	
	/**
	 * Returns effect if contains in _buffs or _debuffs and null if not found
	 * @param effect
	 * @return
	 */
	private AbstractEffect listsContains(AbstractEffect effect)
	{
		if (_buffs != null && !_buffs.isEmpty() && _buffs.contains(effect))
			return effect;
		
		if (_debuffs != null && !_debuffs.isEmpty() && _debuffs.contains(effect))
			return effect;
		
		return null;
	}
	
	/**
	 * Recalculate effect bits flag.<br>
	 * Please no concurrency access
	 */
	private final void computeEffectFlags()
	{
		int flags = 0;
		
		if (_buffs != null)
		{
			for (AbstractEffect e : _buffs)
			{
				if (e == null)
					continue;
				
				flags |= e.getEffectFlags();
			}
		}
		
		if (_debuffs != null)
		{
			for (AbstractEffect e : _debuffs)
			{
				if (e == null)
					continue;
				
				flags |= e.getEffectFlags();
			}
		}
		
		_effectFlags = flags;
	}
	
	/**
	 * Check if target is affected with special buff
	 * @param flag flag of special buff
	 * @return boolean true if affected
	 */
	public boolean isAffected(EffectFlag flag)
	{
		return isAffected(flag.getMask());
	}
	
	public boolean isAffected(int mask)
	{
		return (_effectFlags & mask) != 0;
	}
	
	/**
	 * Clear and null all queues and lists Use only during delete character from the world.
	 */
	public void clear()
	{
		_addQueue = null;
		_removeQueue = null;
		_buffs = null;
		_debuffs = null;
		_stackedEffects = null;
		_queuesInitialized = false;
	}
}