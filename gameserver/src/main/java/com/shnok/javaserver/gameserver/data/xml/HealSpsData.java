package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.LinkedList;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.records.HealSps;
import com.shnok.javaserver.gameserver.skills.L2Skill;

import org.w3c.dom.Document;

/**
 * This class loads and stores {@link HealSps}s infos. Those informations are used for Heal calculation.
 */
public class HealSpsData implements IXmlReader
{
	private final LinkedList<HealSps> _healSps = new LinkedList<>();
	
	protected HealSpsData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/healSps.xml");
		LOGGER.info("Loaded {} healSps entries.", _healSps.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "healSps", healSpsNode -> _healSps.add(new HealSps(parseAttributes(healSpsNode)))));
	}
	
	/**
	 * @param skill : The {@link L2Skill} to check.
	 * @param mAtk : The mAtk to check.
	 * @return The heal SPS correction based on casted {@link L2Skill} and caster's mAtk.
	 */
	public double calculateHealSps(L2Skill skill, int mAtk)
	{
		// Enforce skill id/level content first.
		HealSps healSps = _healSps.stream().filter(h -> h.skillId() == skill.getId() && h.skillLevel() == skill.getLevel()).findFirst().orElse(null);
		
		// healSps is still null, check magic level content.
		if (healSps == null && skill.getMagicLevel() > 0)
			healSps = _healSps.stream().filter(h -> h.magicLevel() <= skill.getMagicLevel()).max(Comparator.comparing(HealSps::magicLevel)).orElse(null);
		
		// healSps couldn't be found ; return 0.
		if (healSps == null)
			return 0.;
		
		// Pick default correction value.
		double amount = healSps.correction();
		
		// Calculate correction based on Matk.
		final int mAtkDiff = healSps.neededMatk() - mAtk;
		if (mAtkDiff <= 0)
			return amount;
		
		amount -= (mAtkDiff / 2d);
		
		return amount;
	}
	
	public static HealSpsData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final HealSpsData INSTANCE = new HealSpsData();
	}
}