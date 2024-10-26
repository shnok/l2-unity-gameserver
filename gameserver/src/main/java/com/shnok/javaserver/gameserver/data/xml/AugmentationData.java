package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.Augmentation;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.network.clientpackets.AbstractRefinePacket;
import com.shnok.javaserver.gameserver.skills.L2Skill;

import org.w3c.dom.Document;

/**
 * This class loads and stores :
 * <ul>
 * <li>{@link AugmentationStat} under 4 different tables of stats (pDef, etc)</li>
 * <li>Augmentation skills based on colors (blue, purple and red)</li>
 * </ul>
 * It is also used to generate new {@link Augmentation}, based on stored content.
 */
public class AugmentationData implements IXmlReader
{
	// stats
	private static final int STAT_START = 1;
	private static final int STAT_END = 14560;
	private static final int STAT_BLOCKSIZE = 3640;
	private static final int STAT_SUBBLOCKSIZE = 91;
	private static final int STAT_NUM = 13;
	
	private static final byte[] STATS1_MAP = new byte[STAT_SUBBLOCKSIZE];
	private static final byte[] STATS2_MAP = new byte[STAT_SUBBLOCKSIZE];
	
	// skills
	private static final int BLUE_START = 14561;
	private static final int SKILLS_BLOCKSIZE = 178;
	
	// basestats
	private static final int BASESTAT_STR = 16341;
	private static final int BASESTAT_CON = 16342;
	private static final int BASESTAT_INT = 16343;
	private static final int BASESTAT_MEN = 16344;
	
	private final List<List<AugmentationStat>> _augStats = new ArrayList<>(4);
	
	private final List<List<Integer>> _blueSkills = new ArrayList<>(10);
	private final List<List<Integer>> _purpleSkills = new ArrayList<>(10);
	private final List<List<Integer>> _redSkills = new ArrayList<>(10);
	
	private final Map<Integer, IntIntHolder> _allSkills = new HashMap<>();
	
	protected AugmentationData()
	{
		// Lookup tables structure: STAT1 represent first stat, STAT2 - second.
		// If both values are the same - use solo stat, if different - combined.
		byte idx;
		
		// weapon augmentation block: solo values first
		for (idx = 0; idx < STAT_NUM; idx++)
		{
			// solo stats
			STATS1_MAP[idx] = idx;
			STATS2_MAP[idx] = idx;
		}
		
		// combined values next.
		for (int i = 0; i < STAT_NUM; i++)
		{
			for (int j = i + 1; j < STAT_NUM; idx++, j++)
			{
				// combined stats
				STATS1_MAP[idx] = (byte) i;
				STATS2_MAP[idx] = (byte) j;
			}
		}
		
		for (int i = 0; i < 4; i++)
			_augStats.add(new ArrayList<>());
		
		for (int i = 0; i < 10; i++)
		{
			_blueSkills.add(new ArrayList<>());
			_purpleSkills.add(new ArrayList<>());
			_redSkills.add(new ArrayList<>());
		}
		
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/augmentation");
		LOGGER.info("Loaded {} sets of augmentation stats.", _augStats.size());
		
		final int blue = _blueSkills.stream().mapToInt(List::size).sum();
		final int purple = _purpleSkills.stream().mapToInt(List::size).sum();
		final int red = _redSkills.stream().mapToInt(List::size).sum();
		LOGGER.info("Loaded {} blue, {} purple and {} red Life-Stone skills.", blue, purple, red);
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode ->
		{
			forEach(listNode, "augmentation", augmentationNode ->
			{
				final StatSet set = parseAttributes(augmentationNode);
				final int augmentationId = set.getInteger("id");
				final int k = (augmentationId - BLUE_START) / SKILLS_BLOCKSIZE;
				
				switch (set.getString("type"))
				{
					case "blue":
						_blueSkills.get(k).add(augmentationId);
						break;
					
					case "purple":
						_purpleSkills.get(k).add(augmentationId);
						break;
					
					case "red":
						_redSkills.get(k).add(augmentationId);
						break;
				}
				_allSkills.put(augmentationId, new IntIntHolder(set.getInteger("skillId"), set.getInteger("skillLevel")));
			});
			forEach(listNode, "set", setNode ->
			{
				final int order = parseInteger(setNode.getAttributes(), "order");
				final List<AugmentationStat> statList = _augStats.get(order);
				forEach(setNode, "stat", statNode ->
				{
					final String statName = parseString(statNode.getAttributes(), "name");
					final List<Float> soloValues = new ArrayList<>();
					final List<Float> combinedValues = new ArrayList<>();
					forEach(statNode, "table", tableNode ->
					{
						final String tableName = parseString(tableNode.getAttributes(), "name");
						final StringTokenizer data = new StringTokenizer(tableNode.getFirstChild().getNodeValue());
						if ("#soloValues".equalsIgnoreCase(tableName))
							while (data.hasMoreTokens())
								soloValues.add(Float.parseFloat(data.nextToken()));
						else
							while (data.hasMoreTokens())
								combinedValues.add(Float.parseFloat(data.nextToken()));
					});
					
					final float[] soloValuesArr = new float[soloValues.size()];
					for (int i = 0; i < soloValuesArr.length; i++)
						soloValuesArr[i] = soloValues.get(i);
					
					final float[] combinedValuesArr = new float[combinedValues.size()];
					for (int i = 0; i < combinedValuesArr.length; i++)
						combinedValuesArr[i] = combinedValues.get(i);
					statList.add(new AugmentationStat(Stats.valueOfXml(statName), soloValuesArr, combinedValuesArr));
				});
			});
		});
	}
	
	public Augmentation generateRandomAugmentation(int lifeStoneLevel, int lifeStoneGrade)
	{
		// Note that stat12 stands for stat 1 AND 2 (same for stat34 ;p )
		// this is because a value can contain up to 2 stat modifications
		// (there are two short values packed in one integer value, meaning 4 stat modifications at max)
		// for more info take a look at getAugStatsById(...)
		
		// Note: lifeStoneGrade: (0 means low grade, 3 top grade)
		// First: determine whether we will add a skill/baseStatModifier or not
		// because this determine which color could be the result
		int stat12 = 0;
		int stat34 = 0;
		boolean generateSkill = false;
		boolean generateGlow = false;
		
		// lifestonelevel is used for stat Id and skill level, but here the max level is 9
		lifeStoneLevel = Math.min(lifeStoneLevel, 9);
		
		switch (lifeStoneGrade)
		{
			case AbstractRefinePacket.GRADE_NONE:
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_NG_SKILL_CHANCE)
					generateSkill = true;
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_NG_GLOW_CHANCE)
					generateGlow = true;
				break;
			case AbstractRefinePacket.GRADE_MID:
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_MID_SKILL_CHANCE)
					generateSkill = true;
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_MID_GLOW_CHANCE)
					generateGlow = true;
				break;
			case AbstractRefinePacket.GRADE_HIGH:
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_HIGH_SKILL_CHANCE)
					generateSkill = true;
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_HIGH_GLOW_CHANCE)
					generateGlow = true;
				break;
			case AbstractRefinePacket.GRADE_TOP:
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_TOP_SKILL_CHANCE)
					generateSkill = true;
				if (Rnd.get(1, 100) <= Config.AUGMENTATION_TOP_GLOW_CHANCE)
					generateGlow = true;
				break;
		}
		
		if (!generateSkill && Rnd.get(1, 100) <= Config.AUGMENTATION_BASESTAT_CHANCE)
			stat34 = Rnd.get(BASESTAT_STR, BASESTAT_MEN);
			
		// Second: decide which grade the augmentation result is going to have:
		// 0:yellow, 1:blue, 2:purple, 3:red
		// The chances used here are most likely custom,
		// whats known is: you cant have yellow with skill(or baseStatModifier)
		// noGrade stone can not have glow, mid only with skill, high has a chance(custom), top allways glow
		int resultColor = Rnd.get(0, 100);
		if (stat34 == 0 && !generateSkill)
		{
			if (resultColor <= (15 * lifeStoneGrade) + 40)
				resultColor = 1;
			else
				resultColor = 0;
		}
		else
		{
			if (resultColor <= (10 * lifeStoneGrade) + 5 || stat34 != 0)
				resultColor = 3;
			else if (resultColor <= (10 * lifeStoneGrade) + 10)
				resultColor = 1;
			else
				resultColor = 2;
		}
		
		// generate a skill if neccessary
		L2Skill skill = null;
		if (generateSkill)
		{
			switch (resultColor)
			{
				case 1: // blue skill
					stat34 = _blueSkills.get(lifeStoneLevel).get(Rnd.get(0, _blueSkills.get(lifeStoneLevel).size() - 1));
					break;
				case 2: // purple skill
					stat34 = _purpleSkills.get(lifeStoneLevel).get(Rnd.get(0, _purpleSkills.get(lifeStoneLevel).size() - 1));
					break;
				case 3: // red skill
					stat34 = _redSkills.get(lifeStoneLevel).get(Rnd.get(0, _redSkills.get(lifeStoneLevel).size() - 1));
					break;
			}
			skill = _allSkills.get(stat34).getSkill();
		}
		
		// Third: Calculate the subblock offset for the choosen color,
		// and the level of the lifeStone
		// from large number of retail augmentations:
		// no skill part
		// Id for stat12:
		// A:1-910 B:911-1820 C:1821-2730 D:2731-3640 E:3641-4550 F:4551-5460 G:5461-6370 H:6371-7280
		// Id for stat34(this defines the color):
		// I:7281-8190(yellow) K:8191-9100(blue) L:10921-11830(yellow) M:11831-12740(blue)
		// you can combine I-K with A-D and L-M with E-H
		// using C-D or G-H Id you will get a glow effect
		// there seems no correlation in which grade use which Id except for the glowing restriction
		// skill part
		// Id for stat12:
		// same for no skill part
		// A same as E, B same as F, C same as G, D same as H
		// A - no glow, no grade LS
		// B - weak glow, mid grade LS?
		// C - glow, high grade LS?
		// D - strong glow, top grade LS?
		
		// is neither a skill nor basestat used for stat34? then generate a normal stat
		int offset;
		if (stat34 == 0)
		{
			int temp = Rnd.get(2, 3);
			int colorOffset = resultColor * (10 * STAT_SUBBLOCKSIZE) + temp * STAT_BLOCKSIZE + 1;
			offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + colorOffset;
			
			stat34 = Rnd.get(offset, offset + STAT_SUBBLOCKSIZE - 1);
			if (generateGlow && lifeStoneGrade >= 2)
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (temp - 2) * STAT_BLOCKSIZE + lifeStoneGrade * (10 * STAT_SUBBLOCKSIZE) + 1;
			else
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + (temp - 2) * STAT_BLOCKSIZE + Rnd.get(0, 1) * (10 * STAT_SUBBLOCKSIZE) + 1;
		}
		else
		{
			if (!generateGlow)
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + Rnd.get(0, 1) * STAT_BLOCKSIZE + 1;
			else
				offset = (lifeStoneLevel * STAT_SUBBLOCKSIZE) + Rnd.get(0, 1) * STAT_BLOCKSIZE + (lifeStoneGrade + resultColor) / 2 * (10 * STAT_SUBBLOCKSIZE) + 1;
		}
		stat12 = Rnd.get(offset, offset + STAT_SUBBLOCKSIZE - 1);
		
		return new Augmentation(((stat34 << 16) + stat12), skill);
	}
	
	/**
	 * Returns the stat and basestat boni for a given augmentation id
	 * @param augmentationId
	 * @return
	 */
	public List<AugStat> getAugStatsById(int augmentationId)
	{
		List<AugStat> temp = new ArrayList<>();
		// An augmentation id contains 2 short vaues so we gotta seperate them here
		// both values contain a number from 1-16380, the first 14560 values are stats
		// the 14560 stats are divided into 4 blocks each holding 3640 values
		// each block contains 40 subblocks holding 91 stat values
		// the first 13 values are so called Solo-stats and they have the highest stat increase possible
		// after the 13 Solo-stats come 78 combined stats (thats every possible combination of the 13 solo stats)
		// the first 12 combined stats (14-26) is the stat 1 combined with stat 2-13
		// the next 11 combined stats then are stat 2 combined with stat 3-13 and so on...
		// to get the idea have a look @ optiondata_client-e.dat - thats where the data came from :)
		int[] stats = new int[2];
		stats[0] = 0x0000FFFF & augmentationId;
		stats[1] = (augmentationId >> 16);
		
		for (int i = 0; i < 2; i++)
		{
			// weapon augmentation - stats
			if (stats[i] >= STAT_START && stats[i] <= STAT_END)
			{
				int base = stats[i] - STAT_START;
				int color = base / STAT_BLOCKSIZE; // 4 color blocks
				int subblock = base % STAT_BLOCKSIZE; // offset in color block
				int level = subblock / STAT_SUBBLOCKSIZE; // stat level (sub-block number)
				int stat = subblock % STAT_SUBBLOCKSIZE; // offset in sub-block - stat
				
				byte stat1 = STATS1_MAP[stat];
				byte stat2 = STATS2_MAP[stat];
				
				if (stat1 == stat2) // solo stat
				{
					AugmentationStat as = _augStats.get(color).get(stat1);
					temp.add(new AugStat(as.getStat(), as.getSingleStatValue(level)));
				}
				else // combined stat
				{
					AugmentationStat as = _augStats.get(color).get(stat1);
					temp.add(new AugStat(as.getStat(), as.getCombinedStatValue(level)));
					
					as = _augStats.get(color).get(stat2);
					temp.add(new AugStat(as.getStat(), as.getCombinedStatValue(level)));
				}
			}
			// its a base stat
			else if (stats[i] >= BASESTAT_STR && stats[i] <= BASESTAT_MEN)
			{
				switch (stats[i])
				{
					case BASESTAT_STR:
						temp.add(new AugStat(Stats.STAT_STR, 1.0f));
						break;
					case BASESTAT_CON:
						temp.add(new AugStat(Stats.STAT_CON, 1.0f));
						break;
					case BASESTAT_INT:
						temp.add(new AugStat(Stats.STAT_INT, 1.0f));
						break;
					case BASESTAT_MEN:
						temp.add(new AugStat(Stats.STAT_MEN, 1.0f));
						break;
				}
			}
		}
		return temp;
	}
	
	public static class AugStat
	{
		private final Stats _stat;
		private final float _value;
		
		public AugStat(Stats stat, float value)
		{
			_stat = stat;
			_value = value;
		}
		
		public Stats getStat()
		{
			return _stat;
		}
		
		public float getValue()
		{
			return _value;
		}
	}
	
	public static class AugmentationStat
	{
		private final Stats _stat;
		private final int _singleSize;
		private final int _combinedSize;
		private final float[] _singleValues;
		private final float[] _combinedValues;
		
		public AugmentationStat(Stats stat, float[] singleValues, float[] combinedValues)
		{
			_stat = stat;
			_singleSize = singleValues.length;
			_singleValues = singleValues;
			_combinedSize = combinedValues.length;
			_combinedValues = combinedValues;
		}
		
		public int getSingleStatSize()
		{
			return _singleSize;
		}
		
		public int getCombinedStatSize()
		{
			return _combinedSize;
		}
		
		public float getSingleStatValue(int i)
		{
			if (i >= _singleSize || i < 0)
				return _singleValues[_singleSize - 1];
			
			return _singleValues[i];
		}
		
		public float getCombinedStatValue(int i)
		{
			if (i >= _combinedSize || i < 0)
				return _combinedValues[_combinedSize - 1];
			
			return _combinedValues[i];
		}
		
		public Stats getStat()
		{
			return _stat;
		}
	}
	
	public static final AugmentationData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final AugmentationData INSTANCE = new AugmentationData();
	}
}