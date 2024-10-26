package com.shnok.javaserver.gameserver.model.actor.status;

import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.data.xml.PlayerLevelData;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.enums.actors.WeightPenalty;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.instance.Pet;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.records.PetDataEntry;
import net.sf.l2j.gameserver.model.zone.type.SwampZone;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.skills.L2Skill;

public class PetStatus extends SummonStatus<Pet>
{
	public PetStatus(Pet actor)
	{
		super(actor);
	}
	
	public boolean addExp(int value)
	{
		if (!super.addExp(value))
			return false;
		
		_actor.updateAndBroadcastStatus(1);
		return true;
	}
	
	@Override
	public boolean addExpAndSp(long addToExp, int addToSp)
	{
		if (!super.addExpAndSp(addToExp, addToSp))
			return false;
		
		_actor.getOwner().sendPacket(SystemMessage.getSystemMessage(SystemMessageId.PET_EARNED_S1_EXP).addNumber((int) addToExp));
		return true;
	}
	
	@Override
	public final boolean addLevel(byte value)
	{
		if (getLevel() + value > PlayerLevelData.getInstance().getRealMaxLevel())
			return false;
		
		boolean levelIncreased = super.addLevel(value);
		if (levelIncreased)
			_actor.broadcastPacket(new SocialAction(_actor, 15));
		
		return levelIncreased;
	}
	
	@Override
	public final int getLevel()
	{
		return _level;
	}
	
	@Override
	public void setLevel(int value)
	{
		_actor.setPetData(value);
		
		super.setLevel(value); // Set level.
		
		// If a control item exists and its level is different of the new level.
		final ItemInstance controlItem = _actor.getControlItem();
		if (controlItem != null && controlItem.getEnchantLevel() != getLevel())
		{
			_actor.sendPetInfosToOwner();
			
			controlItem.setEnchantLevel(getLevel(), _actor.getOwner());
		}
	}
	
	@Override
	public float getMoveSpeed()
	{
		// Get base value.
		float baseValue = getBaseMoveSpeed();
		
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
		
		return (float) calcStat(Stats.RUN_SPEED, baseValue, null, null);
	}
	
	@Override
	public int getMaxHp()
	{
		return (int) calcStat(Stats.MAX_HP, _actor.getPetData().maxHp(), null, null);
	}
	
	@Override
	public int getMaxMp()
	{
		return (int) calcStat(Stats.MAX_MP, _actor.getPetData().maxMp(), null, null);
	}
	
	@Override
	public double getRegenHp()
	{
		double value = super.getRegenHp();
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			value *= wp.getRegenerationMultiplier();
		
		return value;
	}
	
	@Override
	public double getRegenMp()
	{
		double value = super.getRegenMp();
		
		// Calculate weight penalty malus.
		final WeightPenalty wp = _actor.getWeightPenalty();
		if (wp != WeightPenalty.NONE)
			value *= wp.getRegenerationMultiplier();
		
		return value;
	}
	
	@Override
	public int getMAtk(Creature target, L2Skill skill)
	{
		return (int) calcStat(Stats.MAGIC_ATTACK, _actor.getPetData().mAtk(), target, skill);
	}
	
	@Override
	public int getMAtkSpd()
	{
		double base = 333;
		
		if (_actor.checkHungryState())
			base /= 2;
		
		return (int) calcStat(Stats.MAGIC_ATTACK_SPEED, base, null, null);
	}
	
	@Override
	public int getMDef(Creature target, L2Skill skill)
	{
		return (int) calcStat(Stats.MAGIC_DEFENCE, _actor.getPetData().mDef(), target, skill);
	}
	
	@Override
	public int getPAtk(Creature target)
	{
		return (int) calcStat(Stats.POWER_ATTACK, _actor.getPetData().pAtk(), target, null);
	}
	
	@Override
	public int getPAtkSpd()
	{
		double base = _actor.getTemplate().getBasePAtkSpd();
		
		if (_actor.checkHungryState())
			base /= 2;
		
		return (int) calcStat(Stats.POWER_ATTACK_SPEED, base, null, null);
	}
	
	@Override
	public int getPDef(Creature target)
	{
		return (int) calcStat(Stats.POWER_DEFENCE, _actor.getPetData().pDef(), target, null);
	}
	
	@Override
	public long getExpForLevel(int level)
	{
		final PetDataEntry pde = _actor.getTemplate().getPetDataEntry(level);
		if (pde == null)
			return 0;
		
		return pde.maxExp();
	}
	
	@Override
	public long getExpForThisLevel()
	{
		final PetDataEntry pde = _actor.getTemplate().getPetDataEntry(getLevel());
		if (pde == null)
			return 0;
		
		return pde.maxExp();
	}
	
	@Override
	public long getExpForNextLevel()
	{
		final PetDataEntry pde = _actor.getTemplate().getPetDataEntry(getLevel() + 1);
		if (pde == null)
			return 0;
		
		return pde.maxExp();
	}
}