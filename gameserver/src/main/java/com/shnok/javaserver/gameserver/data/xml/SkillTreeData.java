package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.holder.skillnode.ClanSkillNode;
import com.shnok.javaserver.gameserver.model.holder.skillnode.EnchantSkillNode;
import com.shnok.javaserver.gameserver.model.holder.skillnode.FishingSkillNode;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.skills.L2Skill;

import org.w3c.dom.Document;

/**
 * This class loads and stores datatypes extending SkillNode, such as {@link FishingSkillNode}, {@link EnchantSkillNode} and {@link ClanSkillNode}.
 */
public class SkillTreeData implements IXmlReader
{
	private final List<FishingSkillNode> _fishingSkills = new LinkedList<>();
	private final List<ClanSkillNode> _clanSkills = new LinkedList<>();
	private final List<EnchantSkillNode> _enchantSkills = new LinkedList<>();
	
	protected SkillTreeData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/skillstrees");
		LOGGER.info("Loaded {} fishing skills.", _fishingSkills.size());
		LOGGER.info("Loaded {} clan skills.", _clanSkills.size());
		LOGGER.info("Loaded {} enchant skills.", _enchantSkills.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "clanSkill", clanSkillNode -> _clanSkills.add(new ClanSkillNode(parseAttributes(clanSkillNode))));
			forEach(listNode, "fishingSkill", fishingSkillNode -> _fishingSkills.add(new FishingSkillNode(parseAttributes(fishingSkillNode))));
			forEach(listNode, "enchantSkill", enchantSkillNode -> _enchantSkills.add(new EnchantSkillNode(parseAttributes(enchantSkillNode))));
		});
	}
	
	/**
	 * @param player : The player to check.
	 * @return the {@link List} of available {@link FishingSkillNode} for the associated {@link Player}.
	 */
	public List<FishingSkillNode> getFishingSkillsFor(Player player)
	{
		final List<FishingSkillNode> result = new ArrayList<>();
		
		_fishingSkills.stream().filter(s -> s.getMinLvl() <= player.getStatus().getLevel() && (!s.isDwarven() || (player.hasDwarvenCraft() && s.isDwarven()))).forEach(s ->
		{
			if (player.getSkillLevel(s.getId()) == s.getValue() - 1)
				result.add(s);
		});
		
		return result;
	}
	
	/**
	 * @param player : The player to check.
	 * @param skillId : The skill id to check.
	 * @param skillLevel : The skill level to check.
	 * @return the {@link FishingSkillNode} for the associated {@link Player}.
	 */
	public FishingSkillNode getFishingSkillFor(Player player, int skillId, int skillLevel)
	{
		// We first retrieve skill. If it doesn't exist for this id and level, return null.
		final FishingSkillNode fsn = _fishingSkills.stream().filter(s -> s.getId() == skillId && s.getValue() == skillLevel && (!s.isDwarven() || (player.hasDwarvenCraft() && s.isDwarven()))).findFirst().orElse(null);
		if (fsn == null)
			return null;
		
		// Integrity check ; we check if minimum template skill node is ok for player level.
		if (fsn.getMinLvl() > player.getStatus().getLevel())
			return null;
		
		// We find current known player skill level, if any. If the level is respected, we return the skill.
		if (player.getSkillLevel(skillId) == fsn.getValue() - 1)
			return fsn;
		
		return null;
	}
	
	/**
	 * @param player : The player to check.
	 * @return the required level for next {@link FishingSkillNode} for the associated {@link Player}.
	 */
	public int getRequiredLevelForNextFishingSkill(Player player)
	{
		return _fishingSkills.stream().filter(s -> s.getMinLvl() > player.getStatus().getLevel() && (!s.isDwarven() || (player.hasDwarvenCraft() && s.isDwarven()))).min((s1, s2) -> Integer.compare(s1.getMinLvl(), s2.getMinLvl())).map(s -> s.getMinLvl()).orElse(0);
	}
	
	/**
	 * @param player : The player to check.
	 * @return the {@link List} of available {@link ClanSkillNode} for the associated {@link Player}.
	 */
	public List<ClanSkillNode> getClanSkillsFor(Player player)
	{
		// Clan check integrity.
		final Clan clan = player.getClan();
		if (clan == null)
			return Collections.emptyList();
		
		final List<ClanSkillNode> result = new ArrayList<>();
		
		_clanSkills.stream().filter(s -> s.getMinLvl() <= clan.getLevel()).forEach(s ->
		{
			final L2Skill clanSkill = clan.getClanSkills().get(s.getId());
			if ((clanSkill == null && s.getValue() == 1) || (clanSkill != null && clanSkill.getLevel() == s.getValue() - 1))
				result.add(s);
		});
		
		return result;
	}
	
	/**
	 * @param player : The player to check.
	 * @param skillId : The skill id to check.
	 * @param skillLevel : The skill level to check.
	 * @return the {@link ClanSkillNode} for the associated {@link Player}.
	 */
	public ClanSkillNode getClanSkillFor(Player player, int skillId, int skillLevel)
	{
		// Clan check integrity.
		final Clan clan = player.getClan();
		if (clan == null)
			return null;
		
		// We first retrieve skill. If it doesn't exist for this id and level, return null.
		final ClanSkillNode csn = _clanSkills.stream().filter(s -> s.getId() == skillId && s.getValue() == skillLevel).findFirst().orElse(null);
		if (csn == null)
			return null;
		
		// Integrity check ; we check if minimum template skill node is ok for clan level.
		if (csn.getMinLvl() > clan.getLevel())
			return null;
		
		// We find current known clan skill level, if any. If the level is respected, we return the skill.
		final L2Skill clanSkill = clan.getClanSkills().get(skillId);
		if ((clanSkill == null && csn.getValue() == 1) || (clanSkill != null && clanSkill.getLevel() == csn.getValue() - 1))
			return csn;
		
		return null;
	}
	
	/**
	 * @param player : The player to check.
	 * @return the {@link List} of available {@link EnchantSkillNode} for the associated {@link Player}.
	 */
	public List<EnchantSkillNode> getEnchantSkillsFor(Player player)
	{
		final List<EnchantSkillNode> result = new ArrayList<>();
		
		for (EnchantSkillNode esn : _enchantSkills)
		{
			final L2Skill skill = player.getSkill(esn.getId());
			if (skill != null && ((skill.getLevel() == SkillTable.getInstance().getMaxLevel(skill.getId()) && (esn.getValue() == 101 || esn.getValue() == 141)) || (skill.getLevel() == esn.getValue() - 1)))
				result.add(esn);
		}
		return result;
	}
	
	/**
	 * @param player : The player to check.
	 * @param skillId : The skill id to check.
	 * @param skillLevel : The skill level to check.
	 * @return the {@link EnchantSkillNode} for the associated {@link Player}.
	 */
	public EnchantSkillNode getEnchantSkillFor(Player player, int skillId, int skillLevel)
	{
		// We first retrieve skill. If it doesn't exist for this id and level, return null.
		final EnchantSkillNode esn = _enchantSkills.stream().filter(s -> s.getId() == skillId && s.getValue() == skillLevel).findFirst().orElse(null);
		if (esn == null)
			return null;
		
		// We now test player current skill level.
		final int currentSkillLevel = player.getSkillLevel(skillId);
		if ((currentSkillLevel == SkillTable.getInstance().getMaxLevel(skillId) && (skillLevel == 101 || skillLevel == 141)) || (currentSkillLevel == skillLevel - 1))
			return esn;
		
		return null;
	}
	
	public static SkillTreeData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final SkillTreeData INSTANCE = new SkillTreeData();
	}
}