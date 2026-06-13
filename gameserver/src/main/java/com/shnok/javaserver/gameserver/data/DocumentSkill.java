package com.shnok.javaserver.gameserver.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.conditions.Condition;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public final class DocumentSkill extends DocumentBase
{
	public class Skill
	{
		public int id;
		public String name;
		public StatSet[] sets;
		public StatSet[] enchsets1;
		public StatSet[] enchsets2;
		public int currentLevel;
		public List<L2Skill> skills = new ArrayList<>();
		public List<L2Skill> currentSkills = new ArrayList<>();
	}
	
	private Skill _currentSkill;
	private final List<L2Skill> _skillsInFile = new ArrayList<>();
	
	public DocumentSkill(File file)
	{
		super(file);
	}
	
	private void setCurrentSkill(Skill skill)
	{
		_currentSkill = skill;
	}
	
	@Override
	protected StatSet getStatSet()
	{
		return _currentSkill.sets[_currentSkill.currentLevel];
	}
	
	public List<L2Skill> getSkills()
	{
		return _skillsInFile;
	}
	
	@Override
	protected String getTableValue(String name)
	{
		try
		{
			return _tables.get(name)[_currentSkill.currentLevel];
		}
		catch (RuntimeException e)
		{
			LOGGER.error("Error in table {} of Skill Id {}.", e, name, _currentSkill.id);
			return "";
		}
	}
	
	@Override
	protected String getTableValue(String name, int idx)
	{
		try
		{
			return _tables.get(name)[idx - 1];
		}
		catch (RuntimeException e)
		{
			LOGGER.error("Wrong level count in skill Id {}.", e, _currentSkill.id);
			return "";
		}
	}
	
	@Override
	protected void parseDocument(Document doc)
	{
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("skill".equalsIgnoreCase(d.getNodeName()))
					{
						setCurrentSkill(new Skill());
						parseSkill(d);
						_skillsInFile.addAll(_currentSkill.skills);
						resetTable();
					}
				}
			}
			else if ("skill".equalsIgnoreCase(n.getNodeName()))
			{
				setCurrentSkill(new Skill());
				parseSkill(n);
				_skillsInFile.addAll(_currentSkill.skills);
			}
		}
	}
	
	protected void parseSkill(Node n)
	{
		NamedNodeMap attrs = n.getAttributes();
		int enchantLevels1 = 0;
		int enchantLevels2 = 0;
		int skillId = Integer.parseInt(attrs.getNamedItem("id").getNodeValue());
		String skillName = attrs.getNamedItem("name").getNodeValue();
		String levels = attrs.getNamedItem("levels").getNodeValue();
		int lastLvl = Integer.parseInt(levels);
		if (attrs.getNamedItem("enchantLevels1") != null)
			enchantLevels1 = Integer.parseInt(attrs.getNamedItem("enchantLevels1").getNodeValue());
		if (attrs.getNamedItem("enchantLevels2") != null)
			enchantLevels2 = Integer.parseInt(attrs.getNamedItem("enchantLevels2").getNodeValue());
		
		_currentSkill.id = skillId;
		_currentSkill.name = skillName;
		_currentSkill.sets = new StatSet[lastLvl];
		_currentSkill.enchsets1 = new StatSet[enchantLevels1];
		_currentSkill.enchsets2 = new StatSet[enchantLevels2];
		
		for (int i = 0; i < lastLvl; i++)
		{
			_currentSkill.sets[i] = new StatSet();
			_currentSkill.sets[i].set("skill_id", _currentSkill.id);
			_currentSkill.sets[i].set("level", i + 1);
			_currentSkill.sets[i].set("name", _currentSkill.name);
		}
		
		if (_currentSkill.sets.length != lastLvl)
			throw new RuntimeException("Skill id=" + skillId + " number of levels missmatch, " + lastLvl + " levels expected");
		
		Node first = n.getFirstChild();
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("table".equalsIgnoreCase(n.getNodeName()))
				parseTable(n);
		}
		for (int i = 1; i <= lastLvl; i++)
		{
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("set".equalsIgnoreCase(n.getNodeName()))
					parseBeanSet(n, _currentSkill.sets[i - 1], i);
			}
		}
		for (int i = 0; i < enchantLevels1; i++)
		{
			_currentSkill.enchsets1[i] = new StatSet();
			_currentSkill.enchsets1[i].set("skill_id", _currentSkill.id);
			// currentSkill.enchsets1[i] = currentSkill.sets[currentSkill.sets.length-1];
			_currentSkill.enchsets1[i].set("level", i + 101);
			_currentSkill.enchsets1[i].set("name", _currentSkill.name);
			// currentSkill.enchsets1[i].set("skillType", "NOTDONE");
			
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("set".equalsIgnoreCase(n.getNodeName()))
					parseBeanSet(n, _currentSkill.enchsets1[i], _currentSkill.sets.length);
			}
			
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("enchant1".equalsIgnoreCase(n.getNodeName()))
					parseBeanSet(n, _currentSkill.enchsets1[i], i + 1);
			}
		}
		
		if (_currentSkill.enchsets1.length != enchantLevels1)
			throw new RuntimeException("Skill id=" + skillId + " number of levels missmatch, " + enchantLevels1 + " levels expected");
		
		for (int i = 0; i < enchantLevels2; i++)
		{
			_currentSkill.enchsets2[i] = new StatSet();
			// currentSkill.enchsets2[i] = currentSkill.sets[currentSkill.sets.length-1];
			_currentSkill.enchsets2[i].set("skill_id", _currentSkill.id);
			_currentSkill.enchsets2[i].set("level", i + 141);
			_currentSkill.enchsets2[i].set("name", _currentSkill.name);
			// currentSkill.enchsets2[i].set("skillType", "NOTDONE");
			
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("set".equalsIgnoreCase(n.getNodeName()))
					parseBeanSet(n, _currentSkill.enchsets2[i], _currentSkill.sets.length);
			}
			
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("enchant2".equalsIgnoreCase(n.getNodeName()))
					parseBeanSet(n, _currentSkill.enchsets2[i], i + 1);
			}
		}
		
		if (_currentSkill.enchsets2.length != enchantLevels2)
			throw new RuntimeException("Skill id=" + skillId + " number of levels missmatch, " + enchantLevels2 + " levels expected");
		
		makeSkills();
		for (int i = 0; i < lastLvl; i++)
		{
			_currentSkill.currentLevel = i;
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("cond".equalsIgnoreCase(n.getNodeName()))
				{
					Condition condition = parseCondition(n.getFirstChild(), _currentSkill.currentSkills.get(i));
					Node msg = n.getAttributes().getNamedItem("msg");
					Node msgId = n.getAttributes().getNamedItem("msgId");
					if (condition != null && msg != null)
						condition.setMessage(msg.getNodeValue());
					else if (condition != null && msgId != null)
					{
						condition.setMessageId(Integer.decode(getValue(msgId.getNodeValue(), null)));
						Node addName = n.getAttributes().getNamedItem("addName");
						if (addName != null && Integer.decode(getValue(msgId.getNodeValue(), null)) > 0)
							condition.addName();
					}
					_currentSkill.currentSkills.get(i).attach(condition, false);
				}
				else if ("for".equalsIgnoreCase(n.getNodeName()))
				{
					parseTemplate(n, _currentSkill.currentSkills.get(i));
				}
			}
		}
		for (int i = lastLvl; i < lastLvl + enchantLevels1; i++)
		{
			_currentSkill.currentLevel = i - lastLvl;
			boolean foundCond = false;
			boolean foundFor = false;
			
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("enchant1cond".equalsIgnoreCase(n.getNodeName()))
				{
					foundCond = true;
					Condition condition = parseCondition(n.getFirstChild(), _currentSkill.currentSkills.get(i));
					Node msg = n.getAttributes().getNamedItem("msg");
					if (condition != null && msg != null)
						condition.setMessage(msg.getNodeValue());
					_currentSkill.currentSkills.get(i).attach(condition, false);
				}
				else if ("enchant1for".equalsIgnoreCase(n.getNodeName()))
				{
					foundFor = true;
					parseTemplate(n, _currentSkill.currentSkills.get(i));
				}
			}
			// If none found, the enchanted skill will take effects from maxLvL of norm skill
			if (!foundCond || !foundFor)
			{
				_currentSkill.currentLevel = lastLvl - 1;
				for (n = first; n != null; n = n.getNextSibling())
				{
					if (!foundCond && "cond".equalsIgnoreCase(n.getNodeName()))
					{
						Condition condition = parseCondition(n.getFirstChild(), _currentSkill.currentSkills.get(i));
						Node msg = n.getAttributes().getNamedItem("msg");
						if (condition != null && msg != null)
							condition.setMessage(msg.getNodeValue());
						_currentSkill.currentSkills.get(i).attach(condition, false);
					}
					else if (!foundFor && "for".equalsIgnoreCase(n.getNodeName()))
					{
						parseTemplate(n, _currentSkill.currentSkills.get(i));
					}
				}
			}
		}
		for (int i = lastLvl + enchantLevels1; i < lastLvl + enchantLevels1 + enchantLevels2; i++)
		{
			boolean foundCond = false;
			boolean foundFor = false;
			
			_currentSkill.currentLevel = i - lastLvl - enchantLevels1;
			for (n = first; n != null; n = n.getNextSibling())
			{
				if ("enchant2cond".equalsIgnoreCase(n.getNodeName()))
				{
					foundCond = true;
					Condition condition = parseCondition(n.getFirstChild(), _currentSkill.currentSkills.get(i));
					Node msg = n.getAttributes().getNamedItem("msg");
					if (condition != null && msg != null)
						condition.setMessage(msg.getNodeValue());
					_currentSkill.currentSkills.get(i).attach(condition, false);
				}
				else if ("enchant2for".equalsIgnoreCase(n.getNodeName()))
				{
					foundFor = true;
					parseTemplate(n, _currentSkill.currentSkills.get(i));
				}
			}
			// If none found, the enchanted skill will take effects from maxLvL of norm skill
			if (!foundCond || !foundFor)
			{
				_currentSkill.currentLevel = lastLvl - 1;
				for (n = first; n != null; n = n.getNextSibling())
				{
					if (!foundCond && "cond".equalsIgnoreCase(n.getNodeName()))
					{
						Condition condition = parseCondition(n.getFirstChild(), _currentSkill.currentSkills.get(i));
						Node msg = n.getAttributes().getNamedItem("msg");
						if (condition != null && msg != null)
							condition.setMessage(msg.getNodeValue());
						_currentSkill.currentSkills.get(i).attach(condition, false);
					}
					else if (!foundFor && "for".equalsIgnoreCase(n.getNodeName()))
					{
						parseTemplate(n, _currentSkill.currentSkills.get(i));
					}
				}
			}
		}
		_currentSkill.skills.addAll(_currentSkill.currentSkills);
	}
	
	private void makeSkills()
	{
		int count = 0;
		_currentSkill.currentSkills = new ArrayList<>(_currentSkill.sets.length + _currentSkill.enchsets1.length + _currentSkill.enchsets2.length);
		
		for (int i = 0; i < _currentSkill.sets.length; i++)
		{
			try
			{
				_currentSkill.currentSkills.add(i, _currentSkill.sets[i].getEnum("skillType", SkillType.class).makeSkill(_currentSkill.sets[i]));
				count++;
			}
			catch (Exception e)
			{
				LOGGER.error("Failed parsing skill.", e);
			}
		}
		int _count = count;
		for (int i = 0; i < _currentSkill.enchsets1.length; i++)
		{
			try
			{
				_currentSkill.currentSkills.add(_count + i, _currentSkill.enchsets1[i].getEnum("skillType", SkillType.class).makeSkill(_currentSkill.enchsets1[i]));
				count++;
			}
			catch (Exception e)
			{
				LOGGER.error("Failed parsing skill.", e);
			}
		}
		_count = count;
		for (int i = 0; i < _currentSkill.enchsets2.length; i++)
		{
			try
			{
				_currentSkill.currentSkills.add(_count + i, _currentSkill.enchsets2[i].getEnum("skillType", SkillType.class).makeSkill(_currentSkill.enchsets2[i]));
				count++;
			}
			catch (Exception e)
			{
				LOGGER.error("Failed parsing skill.", e);
			}
		}
	}
}