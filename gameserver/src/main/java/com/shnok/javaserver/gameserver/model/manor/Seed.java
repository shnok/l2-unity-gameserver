package com.shnok.javaserver.gameserver.model.manor;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.xml.ItemData;
import com.shnok.javaserver.gameserver.model.item.kind.Item;

public final class Seed
{
	private final int _cropId;
	private final int _seedId;
	private final int _matureId;
	private final int _level;
	private final int _reward1;
	private final int _reward2;
	private final int _castleId;
	private final boolean _isAlternative;
	private final int _seedsLimit;
	private final int _cropsLimit;
	private final int _seedReferencePrice;
	private final int _cropReferencePrice;
	
	public Seed(StatSet set)
	{
		_cropId = set.getInteger("id");
		_seedId = set.getInteger("seedId");
		_matureId = set.getInteger("matureId");
		_level = set.getInteger("level");
		_reward1 = set.getInteger("reward1");
		_reward2 = set.getInteger("reward2");
		_castleId = set.getInteger("castleId");
		_isAlternative = set.getBool("isAlternative");
		_seedsLimit = set.getInteger("seedsLimit");
		_cropsLimit = set.getInteger("cropsLimit");
		
		Item item = ItemData.getInstance().getTemplate(_cropId);
		_cropReferencePrice = (item != null) ? item.getReferencePrice() : 1;
		
		item = ItemData.getInstance().getTemplate(_seedId);
		_seedReferencePrice = (item != null) ? item.getReferencePrice() : 1;
	}
	
	public final int getCropId()
	{
		return _cropId;
	}
	
	public final int getSeedId()
	{
		return _seedId;
	}
	
	public final int getMatureId()
	{
		return _matureId;
	}
	
	public final int getLevel()
	{
		return _level;
	}
	
	public final int getReward1()
	{
		return _reward1;
	}
	
	public final int getReward2()
	{
		return _reward2;
	}
	
	public final int getCastleId()
	{
		return _castleId;
	}
	
	public final boolean isAlternative()
	{
		return _isAlternative;
	}
	
	public final int getSeedsLimit()
	{
		return _seedsLimit * Config.RATE_DROP_MANOR;
	}
	
	public final int getCropsLimit()
	{
		return _cropsLimit * Config.RATE_DROP_MANOR;
	}
	
	public final int getSeedReferencePrice()
	{
		return _seedReferencePrice;
	}
	
	public final int getSeedMaxPrice()
	{
		return _seedReferencePrice * 10;
	}
	
	public final int getSeedMinPrice()
	{
		return (int) (_seedReferencePrice * 0.6);
	}
	
	public final int getCropReferencePrice()
	{
		return _cropReferencePrice;
	}
	
	public final int getCropMaxPrice()
	{
		return _cropReferencePrice * 10;
	}
	
	public final int getCropMinPrice()
	{
		return (int) (_cropReferencePrice * 0.6);
	}
	
	@Override
	public final String toString()
	{
		return "Seed [_id=" + _seedId + ", _level=" + _level + ", _crop=" + _cropId + ", _mature=" + _matureId + ", _reward1=" + _reward1 + ", _reward2=" + _reward2 + ", _manorId=" + _castleId + ", _isAlternative=" + _isAlternative + ", _limitSeeds=" + _seedsLimit + ", _limitCrops=" + _cropsLimit + "]";
	}
}