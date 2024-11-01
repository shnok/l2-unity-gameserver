package com.shnok.javaserver.gameserver.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.xml.AugmentationData;
import com.shnok.javaserver.gameserver.data.xml.AugmentationData.AugStat;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.Timestamp;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SkillCoolTime;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SkillList;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.FuncAdd;

/**
 * A container used to store an Augmentation.<br>
 * <br>
 * Weapon Augmentation, alongside with enchanting and special abilities, adds special, random, features.<br>
 * <br>
 * Augmented items cannot be sold or transferred, they cannot be lost if your character dies, they cannot be dropped, however you can store them in your warehouse.
 */
public final class Augmentation
{
	private int _id;
	private AugmentationStatBoni _boni;
	private L2Skill _skill;
	
	public Augmentation(int id, L2Skill skill)
	{
		_id = id;
		_boni = new AugmentationStatBoni(_id);
		_skill = skill;
	}
	
	public Augmentation(int id, int skill, int skillLevel)
	{
		this(id, skill != 0 ? SkillTable.getInstance().getInfo(skill, skillLevel) : null);
	}
	
	public int getId()
	{
		return _id;
	}
	
	public L2Skill getSkill()
	{
		return _skill;
	}
	
	/**
	 * Applies this {@link Augmentation} boni to the {@link Player} set as parameter.
	 * @param player : The Augmentation boni benefactor.
	 */
	public void applyBonus(Player player)
	{
		// Apply boni to the Player.
		_boni.applyBonus(player);
		
		// Additional actions if a skill exists.
		if (_skill != null)
		{
			boolean updateTimeStamp = false;
			
			// Add the skill to the Player.
			player.addSkill(_skill, false);
			
			// Active skill is detected, we check if a reuse time is active.
			if (_skill.isActive())
			{
				final Timestamp ts = player.getReuseTimeStamp().get(_skill.getReuseHashCode());
				if (ts != null)
				{
					final long delay = ts.getRemaining();
					if (delay > 0)
					{
						player.disableSkill(_skill, delay);
						updateTimeStamp = true;
					}
				}
			}
			
			// Refresh Player skill list.
			player.sendPacket(new SkillList(player));
			
			// Refresh cooldown time, if needed.
			if (updateTimeStamp)
				player.sendPacket(new SkillCoolTime(player));
		}
	}
	
	/**
	 * Removes this {@link Augmentation} boni to the {@link Player} set as parameter.
	 * @param player : The Augmentation boni benefactor.
	 */
	public void removeBonus(Player player)
	{
		// Remove boni from the Player.
		_boni.removeBonus(player);
		
		// Additional actions if a skill exists.
		if (_skill != null)
		{
			// Remove the skill, if any.
			player.removeSkill(_skill.getId(), false, _skill.isPassive() || _skill.isToggle());
			
			// Refresh Player skill list.
			player.sendPacket(new SkillList(player));
		}
	}
	
	public static class AugmentationStatBoni
	{
		private final Stats[] _stats;
		private final float[] _values;
		
		private boolean _active;
		
		public AugmentationStatBoni(int augmentationId)
		{
			_active = false;
			List<AugStat> as = AugmentationData.getInstance().getAugStatsById(augmentationId);
			
			_stats = new Stats[as.size()];
			_values = new float[as.size()];
			
			int i = 0;
			for (AugStat aStat : as)
			{
				_stats[i] = aStat.getStat();
				_values[i] = aStat.getValue();
				i++;
			}
		}
		
		public void applyBonus(Player player)
		{
			// make sure the bonuses are not applied twice..
			if (_active)
				return;
			
			for (int i = 0; i < _stats.length; i++)
				player.addStatFunc(new FuncAdd(this, _stats[i], _values[i], null));
			
			_active = true;
		}
		
		public void removeBonus(Player player)
		{
			// make sure the bonuses are not removed twice
			if (!_active)
				return;
			
			player.removeStatsByOwner(this);
			
			_active = false;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			
			if (obj == null || getClass() != obj.getClass())
				return false;
			
			final AugmentationStatBoni other = (AugmentationStatBoni) obj;
			return Arrays.equals(_stats, other._stats) && Arrays.equals(_values, other._values);
		}
		
		@Override
		public int hashCode()
		{
			return Objects.hash(Arrays.hashCode(_stats), Arrays.hashCode(_values));
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		final Augmentation other = (Augmentation) obj;
		return _id == other._id && Objects.equals(_boni, other._boni) && Objects.equals(_skill, other._skill);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_id, _boni, _skill);
	}
}