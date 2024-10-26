package com.shnok.javaserver.gameserver.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.enums.items.ArmorType;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.enums.skills.AbnormalEffect;
import com.shnok.javaserver.gameserver.enums.skills.PlayerState;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.holder.IntIntHolder;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.zone.form.ZoneNPoly;
import com.shnok.javaserver.gameserver.skills.ChanceCondition;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.basefuncs.FuncTemplate;
import com.shnok.javaserver.gameserver.skills.conditions.Condition;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionElementSeed;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionForceBuff;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionGameTime;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionLogicAnd;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionLogicNot;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionLogicOr;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerActiveEffectId;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerActiveSkillId;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerCharges;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerHasCastle;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerHasClanHall;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerHp;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerInsidePoly;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerInvSize;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerIsHero;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerLevel;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerMp;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerPkCount;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerPledgeClass;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerRace;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerSex;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerState;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionPlayerWeight;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionSkillStats;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionTargetActiveSkillId;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionTargetHpMinMax;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionTargetNpcId;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionTargetRaceId;
import com.shnok.javaserver.gameserver.skills.conditions.ConditionUsingItemType;
import com.shnok.javaserver.gameserver.skills.effects.EffectChanceSkillTrigger;
import com.shnok.javaserver.gameserver.skills.effects.EffectTemplate;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

abstract class DocumentBase
{
	protected static final CLogger LOGGER = new CLogger(DocumentBase.class.getName());
	
	private final File _file;
	protected Map<String, String[]> _tables;
	
	DocumentBase(File pFile)
	{
		_file = pFile;
		_tables = new HashMap<>();
	}
	
	public Document parse()
	{
		Document doc = null;
		try
		{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
			dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
			dbf.setValidating(false);
			dbf.setIgnoringComments(true);
			
			doc = dbf.newDocumentBuilder().parse(_file);
			
			parseDocument(doc);
		}
		catch (Exception e)
		{
			LOGGER.error("Error loading file {}.", e, _file);
		}
		return doc;
	}
	
	protected abstract void parseDocument(Document doc);
	
	protected abstract StatSet getStatSet();
	
	protected abstract String getTableValue(String name);
	
	protected abstract String getTableValue(String name, int idx);
	
	protected void resetTable()
	{
		_tables = new HashMap<>();
	}
	
	protected void setTable(String name, String[] table)
	{
		_tables.put(name, table);
	}
	
	protected void parseTemplate(Node n, Object template)
	{
		n = n.getFirstChild();
		if (n == null)
			return;
		
		Condition condition = null;
		if ("cond".equalsIgnoreCase(n.getNodeName()))
		{
			condition = parseCondition(n.getFirstChild(), template);
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
			n = n.getNextSibling();
		}
		
		for (; n != null; n = n.getNextSibling())
		{
			if ("add".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "Add", condition);
			else if ("addMul".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "AddMul", condition);
			else if ("sub".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "Sub", condition);
			else if ("subDiv".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "SubDiv", condition);
			else if ("mul".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "Mul", condition);
			else if ("basemul".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "BaseMul", condition);
			else if ("div".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "Div", condition);
			else if ("set".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "Set", condition);
			else if ("enchant".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "Enchant", condition);
			else if ("baseadd".equalsIgnoreCase(n.getNodeName()))
				attachFunc(n, template, "BaseAdd", condition);
			else if ("effect".equalsIgnoreCase(n.getNodeName()))
			{
				if (template instanceof EffectTemplate)
					throw new RuntimeException("Nested effects");
				
				attachEffect(n, template, condition);
			}
		}
	}
	
	protected void attachFunc(Node n, Object template, String function, Condition attachCond)
	{
		Stats stat = Stats.valueOfXml(n.getAttributes().getNamedItem("stat").getNodeValue());
		
		String valueString = n.getAttributes().getNamedItem("val").getNodeValue();
		double value;
		if (valueString.charAt(0) == '#')
			value = Double.parseDouble(getTableValue(valueString));
		else
			value = Double.parseDouble(valueString);
		
		final Condition applyCond = parseCondition(n.getFirstChild(), template);
		final FuncTemplate ft = new FuncTemplate(attachCond, applyCond, function, stat, value);
		
		if (template instanceof Item item)
			item.attach(ft);
		else if (template instanceof L2Skill skill)
			skill.attach(ft);
		else if (template instanceof EffectTemplate effect)
			effect.attach(ft);
		else
			throw new RuntimeException("Attaching stat to a non-effect template!!!");
	}
	
	protected void attachEffect(Node n, Object template, Condition attachCond)
	{
		NamedNodeMap attrs = n.getAttributes();
		String name = getValue(attrs.getNamedItem("name").getNodeValue().intern(), template);
		
		// Keep this values as default ones, DP needs it
		int time = 1;
		int count = 1;
		
		if (attrs.getNamedItem("count") != null)
			count = Integer.decode(getValue(attrs.getNamedItem("count").getNodeValue(), template));
		
		if (attrs.getNamedItem("time") != null)
			time = Integer.decode(getValue(attrs.getNamedItem("time").getNodeValue(), template));
		
		boolean self = false;
		if (attrs.getNamedItem("self") != null && Integer.decode(getValue(attrs.getNamedItem("self").getNodeValue(), template)) == 1)
			self = true;
		
		boolean icon = true;
		if (attrs.getNamedItem("noicon") != null && Integer.decode(getValue(attrs.getNamedItem("noicon").getNodeValue(), template)) == 1)
			icon = false;
		
		String valueString = n.getAttributes().getNamedItem("val").getNodeValue();
		double value;
		if (valueString.charAt(0) == '#')
			value = Double.parseDouble(getTableValue(valueString));
		else
			value = Double.parseDouble(valueString);
		
		AbnormalEffect abnormal = AbnormalEffect.NULL;
		if (attrs.getNamedItem("abnormal") != null)
		{
			String abn = attrs.getNamedItem("abnormal").getNodeValue();
			
			if (abn.charAt(0) == '#')
				abnormal = AbnormalEffect.getByName(getTableValue(abn));
			else
				abnormal = AbnormalEffect.getByName(abn);
		}
		
		String stackType = "none";
		if (attrs.getNamedItem("stackType") != null)
			stackType = attrs.getNamedItem("stackType").getNodeValue();
		
		float stackOrder = 0;
		if (attrs.getNamedItem("stackOrder") != null)
			stackOrder = Float.parseFloat(getValue(attrs.getNamedItem("stackOrder").getNodeValue(), template));
		
		double effectPower = -1;
		if (attrs.getNamedItem("effectPower") != null)
			effectPower = Double.parseDouble(getValue(attrs.getNamedItem("effectPower").getNodeValue(), template));
		
		SkillType type = null;
		if (attrs.getNamedItem("effectType") != null)
		{
			String typeName = getValue(attrs.getNamedItem("effectType").getNodeValue(), template);
			
			try
			{
				type = Enum.valueOf(SkillType.class, typeName);
			}
			catch (Exception e)
			{
				throw new IllegalArgumentException("Not skilltype found for: " + typeName);
			}
		}
		
		EffectTemplate lt;
		
		final boolean isChanceSkillTrigger = (name.equals(EffectChanceSkillTrigger.class.getName()));
		int trigId = 0;
		if (attrs.getNamedItem("triggeredId") != null)
			trigId = Integer.parseInt(getValue(attrs.getNamedItem("triggeredId").getNodeValue(), template));
		else if (isChanceSkillTrigger)
			throw new NoSuchElementException(name + " requires triggerId");
		
		int trigLvl = 1;
		if (attrs.getNamedItem("triggeredLevel") != null)
			trigLvl = Integer.parseInt(getValue(attrs.getNamedItem("triggeredLevel").getNodeValue(), template));
		
		String chanceCond = null;
		if (attrs.getNamedItem("chanceType") != null)
			chanceCond = getValue(attrs.getNamedItem("chanceType").getNodeValue(), template);
		else if (isChanceSkillTrigger)
			throw new NoSuchElementException(name + " requires chanceType");
		
		int activationChance = -1;
		if (attrs.getNamedItem("activationChance") != null)
			activationChance = Integer.parseInt(getValue(attrs.getNamedItem("activationChance").getNodeValue(), template));
		
		ChanceCondition chance = ChanceCondition.parse(chanceCond, activationChance);
		
		if (chance == null && isChanceSkillTrigger)
			throw new NoSuchElementException("Invalid chance condition: " + chanceCond + " " + activationChance);
		
		lt = new EffectTemplate(attachCond, name, value, count, time, abnormal, stackType, stackOrder, icon, effectPower, type, trigId, trigLvl, chance);
		
		parseTemplate(n, lt);
		if (template instanceof L2Skill skill)
		{
			if (self)
				skill.attachSelf(lt);
			else
				skill.attach(lt);
		}
	}
	
	protected Condition parseCondition(Node n, Object template)
	{
		while (n != null && n.getNodeType() != Node.ELEMENT_NODE)
			n = n.getNextSibling();
		
		if (n == null)
			return null;
		
		if ("and".equalsIgnoreCase(n.getNodeName()))
			return parseLogicAnd(n, template);
		
		if ("or".equalsIgnoreCase(n.getNodeName()))
			return parseLogicOr(n, template);
		
		if ("not".equalsIgnoreCase(n.getNodeName()))
			return parseLogicNot(n, template);
		
		if ("player".equalsIgnoreCase(n.getNodeName()))
			return parsePlayerCondition(n, template);
		
		if ("target".equalsIgnoreCase(n.getNodeName()))
			return parseTargetCondition(n, template);
		
		if ("skill".equalsIgnoreCase(n.getNodeName()))
			return parseSkillCondition(n);
		
		if ("using".equalsIgnoreCase(n.getNodeName()))
			return parseUsingCondition(n);
		
		if ("game".equalsIgnoreCase(n.getNodeName()))
			return parseGameCondition(n);
		
		return null;
	}
	
	protected Condition parseLogicAnd(Node n, Object template)
	{
		ConditionLogicAnd cond = new ConditionLogicAnd();
		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
			if (n.getNodeType() == Node.ELEMENT_NODE)
				cond.add(parseCondition(n, template));
			
		if (cond.conditions == null || cond.conditions.length == 0)
			LOGGER.error("Empty <and> condition in {}.", _file);
		
		return cond;
	}
	
	protected Condition parseLogicOr(Node n, Object template)
	{
		ConditionLogicOr cond = new ConditionLogicOr();
		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
			if (n.getNodeType() == Node.ELEMENT_NODE)
				cond.add(parseCondition(n, template));
			
		if (cond.conditions == null || cond.conditions.length == 0)
			LOGGER.error("Empty <or> condition in {}.", _file);
		
		return cond;
	}
	
	protected Condition parseLogicNot(Node n, Object template)
	{
		for (n = n.getFirstChild(); n != null; n = n.getNextSibling())
			if (n.getNodeType() == Node.ELEMENT_NODE)
				return new ConditionLogicNot(parseCondition(n, template));
			
		LOGGER.error("Empty <not> condition in {}.", _file);
		return null;
	}
	
	protected Condition parsePlayerCondition(Node n, Object template)
	{
		Condition cond = null;
		int[] ElementSeeds = new int[5];
		byte[] forces = new byte[2];
		NamedNodeMap attrs = n.getAttributes();
		
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("race".equalsIgnoreCase(a.getNodeName()))
			{
				ClassRace race = ClassRace.valueOf(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerRace(race));
			}
			else if ("level".equalsIgnoreCase(a.getNodeName()))
			{
				int lvl = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionPlayerLevel(lvl));
			}
			else if ("resting".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.RESTING, val));
			}
			else if ("riding".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.RIDING, val));
			}
			else if ("flying".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.FLYING, val));
			}
			else if ("moving".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.MOVING, val));
			}
			else if ("running".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.RUNNING, val));
			}
			else if ("behind".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.BEHIND, val));
			}
			else if ("front".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.FRONT, val));
			}
			else if ("olympiad".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerState(PlayerState.OLYMPIAD, val));
			}
			else if ("ishero".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionPlayerIsHero(val));
			}
			else if ("hp".equalsIgnoreCase(a.getNodeName()))
			{
				int hp = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerHp(hp));
			}
			else if ("mp".equalsIgnoreCase(a.getNodeName()))
			{
				int hp = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerMp(hp));
			}
			else if ("pkCount".equalsIgnoreCase(a.getNodeName()))
			{
				int expIndex = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionPlayerPkCount(expIndex));
			}
			else if ("battle_force".equalsIgnoreCase(a.getNodeName()))
			{
				forces[0] = Byte.decode(getValue(a.getNodeValue(), null));
			}
			else if ("spell_force".equalsIgnoreCase(a.getNodeName()))
			{
				forces[1] = Byte.decode(getValue(a.getNodeValue(), null));
			}
			else if ("charges".equalsIgnoreCase(a.getNodeName()))
			{
				int value = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionPlayerCharges(value));
			}
			else if ("weight".equalsIgnoreCase(a.getNodeName()))
			{
				int weight = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerWeight(weight));
			}
			else if ("invSize".equalsIgnoreCase(a.getNodeName()))
			{
				int size = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerInvSize(size));
			}
			else if ("pledgeClass".equalsIgnoreCase(a.getNodeName()))
			{
				int pledgeClass = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerPledgeClass(pledgeClass));
			}
			else if ("clanHall".equalsIgnoreCase(a.getNodeName()))
			{
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				ArrayList<Integer> array = new ArrayList<>(st.countTokens());
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					array.add(Integer.decode(getValue(item, null)));
				}
				cond = joinAnd(cond, new ConditionPlayerHasClanHall(array));
			}
			else if ("castle".equalsIgnoreCase(a.getNodeName()))
			{
				int castle = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerHasCastle(castle));
			}
			else if ("sex".equalsIgnoreCase(a.getNodeName()))
			{
				int sex = Integer.decode(getValue(a.getNodeValue(), null));
				cond = joinAnd(cond, new ConditionPlayerSex(sex));
			}
			else if ("active_effect_id".equalsIgnoreCase(a.getNodeName()))
			{
				int effect_id = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionPlayerActiveEffectId(effect_id));
			}
			else if ("active_effect_id_lvl".equalsIgnoreCase(a.getNodeName()))
			{
				String val = getValue(a.getNodeValue(), template);
				int effect_id = Integer.decode(getValue(val.split(",")[0], template));
				int effect_lvl = Integer.decode(getValue(val.split(",")[1], template));
				cond = joinAnd(cond, new ConditionPlayerActiveEffectId(effect_id, effect_lvl));
			}
			else if ("active_skill_id".equalsIgnoreCase(a.getNodeName()))
			{
				int skill_id = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionPlayerActiveSkillId(skill_id));
			}
			else if ("active_skill_id_lvl".equalsIgnoreCase(a.getNodeName()))
			{
				String val = getValue(a.getNodeValue(), template);
				int skill_id = Integer.decode(getValue(val.split(",")[0], template));
				int skill_lvl = Integer.decode(getValue(val.split(",")[1], template));
				cond = joinAnd(cond, new ConditionPlayerActiveSkillId(skill_id, skill_lvl));
			}
			else if ("seed_fire".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[0] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_water".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[1] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_wind".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[2] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_various".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[3] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("seed_any".equalsIgnoreCase(a.getNodeName()))
			{
				ElementSeeds[4] = Integer.decode(getValue(a.getNodeValue(), null));
			}
			else if ("insidePoly".equalsIgnoreCase(a.getNodeName()))
			{
				final Node zoneNode = n.getFirstChild().getNextSibling();
				final NamedNodeMap pAttrs = zoneNode.getAttributes();
				
				final int minZ = Integer.decode(getValue(pAttrs.getNamedItem("minZ").getNodeValue(), null));
				final int maxZ = Integer.decode(getValue(pAttrs.getNamedItem("maxZ").getNodeValue(), null));
				final boolean checkInside = Boolean.parseBoolean(getValue(a.getNodeValue(), null));
				
				final NodeList nNodes = zoneNode.getChildNodes();
				
				final List<IntIntHolder> pNodes = new ArrayList<>();
				
				for (int j = 0; j < nNodes.getLength(); j++)
					if (nNodes.item(j).getNodeType() == Node.ELEMENT_NODE)
					{
						final NamedNodeMap nodeAttrs = nNodes.item(j).getAttributes();
						final int xCoord = Integer.decode(getValue(nodeAttrs.getNamedItem("x").getNodeValue(), null));
						final int yCoord = Integer.decode(getValue(nodeAttrs.getNamedItem("y").getNodeValue(), null));
						
						pNodes.add(new IntIntHolder(xCoord, yCoord));
					}
				
				final int[] aX = new int[pNodes.size()];
				final int[] aY = new int[pNodes.size()];
				
				for (int k = 0; k < pNodes.size(); k++)
				{
					aX[k] = pNodes.get(k).getId();
					aY[k] = pNodes.get(k).getValue();
				}
				
				cond = joinAnd(cond, new ConditionPlayerInsidePoly(new ZoneNPoly(aX, aY, minZ, maxZ), checkInside));
			}
		}
		
		// Elemental seed condition processing
		for (int elementSeed : ElementSeeds)
		{
			if (elementSeed > 0)
			{
				cond = joinAnd(cond, new ConditionElementSeed(ElementSeeds));
				break;
			}
		}
		
		if (forces[0] + forces[1] > 0)
			cond = joinAnd(cond, new ConditionForceBuff(forces));
		
		if (cond == null)
			LOGGER.error("Unrecognized <player> condition in {}.", _file);
		
		return cond;
	}
	
	protected Condition parseTargetCondition(Node n, Object template)
	{
		Condition cond = null;
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("hp_min_max".equalsIgnoreCase(a.getNodeName()))
			{
				String val = getValue(a.getNodeValue(), template);
				int hpMin = Integer.decode(getValue(val.split(",")[0], template));
				int hpMax = Integer.decode(getValue(val.split(",")[1], template));
				cond = joinAnd(cond, new ConditionTargetHpMinMax(hpMin, hpMax));
			}
			else if ("active_skill_id".equalsIgnoreCase(a.getNodeName()))
			{
				int skill_id = Integer.decode(getValue(a.getNodeValue(), template));
				cond = joinAnd(cond, new ConditionTargetActiveSkillId(skill_id));
			}
			else if ("race_id".equalsIgnoreCase(a.getNodeName()))
			{
				List<Integer> array = new ArrayList<>();
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					array.add(Integer.decode(getValue(item, null)));
				}
				cond = joinAnd(cond, new ConditionTargetRaceId(array));
			}
			else if ("npcId".equalsIgnoreCase(a.getNodeName()))
			{
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				ArrayList<Integer> array = new ArrayList<>(st.countTokens());
				while (st.hasMoreTokens())
				{
					String item = st.nextToken().trim();
					array.add(Integer.decode(getValue(item, null)));
				}
				cond = joinAnd(cond, new ConditionTargetNpcId(array));
			}
		}
		
		if (cond == null)
			LOGGER.error("Unrecognized <target> condition in {}.", _file);
		
		return cond;
	}
	
	protected Condition parseSkillCondition(Node n)
	{
		NamedNodeMap attrs = n.getAttributes();
		Stats stat = Stats.valueOfXml(attrs.getNamedItem("stat").getNodeValue());
		return new ConditionSkillStats(stat);
	}
	
	protected Condition parseUsingCondition(Node n)
	{
		Condition cond = null;
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("kind".equalsIgnoreCase(a.getNodeName()))
			{
				int mask = 0;
				StringTokenizer st = new StringTokenizer(a.getNodeValue(), ",");
				while (st.hasMoreTokens())
				{
					int old = mask;
					String item = st.nextToken();
					for (WeaponType wt : WeaponType.VALUES)
					{
						if (wt.name().equals(item))
						{
							mask |= wt.mask();
							break;
						}
					}
					
					for (ArmorType at : ArmorType.VALUES)
					{
						if (at.name().equals(item))
						{
							mask |= at.mask();
							break;
						}
					}
					
					if (old == mask)
						LOGGER.error("[parseUsingCondition=\"kind\"] Unknown item type name: {}.", item);
				}
				cond = joinAnd(cond, new ConditionUsingItemType(mask));
			}
		}
		
		if (cond == null)
			LOGGER.error("Unrecognized <using> condition in {}.", _file);
		
		return cond;
	}
	
	protected Condition parseGameCondition(Node n)
	{
		Condition cond = null;
		NamedNodeMap attrs = n.getAttributes();
		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item(i);
			if ("night".equalsIgnoreCase(a.getNodeName()))
			{
				boolean val = Boolean.parseBoolean(a.getNodeValue());
				cond = joinAnd(cond, new ConditionGameTime(val));
			}
		}
		
		if (cond == null)
			LOGGER.error("Unrecognized <game> condition in {}.", _file);
		
		return cond;
	}
	
	protected void parseTable(Node n)
	{
		NamedNodeMap attrs = n.getAttributes();
		String name = attrs.getNamedItem("name").getNodeValue();
		
		if (name.charAt(0) != '#')
			throw new IllegalArgumentException("Table name must start with #");
		
		StringTokenizer data = new StringTokenizer(n.getFirstChild().getNodeValue());
		List<String> array = new ArrayList<>(data.countTokens());
		
		while (data.hasMoreTokens())
			array.add(data.nextToken());
		
		setTable(name, array.toArray(new String[array.size()]));
	}
	
	protected void parseBeanSet(Node n, StatSet set, Integer level)
	{
		String name = n.getAttributes().getNamedItem("name").getNodeValue().trim();
		String value = n.getAttributes().getNamedItem("val").getNodeValue().trim();
		char ch = value.length() == 0 ? ' ' : value.charAt(0);
		
		if (ch == '#' || ch == '-' || Character.isDigit(ch))
			set.set(name, String.valueOf(getValue(value, level)));
		else
			set.set(name, value);
	}
	
	protected String getValue(String value, Object template)
	{
		// is it a table?
		if (value.charAt(0) == '#')
		{
			if (template instanceof L2Skill)
				return getTableValue(value);
			
			if (template instanceof Integer intTemplate)
				return getTableValue(value, intTemplate.intValue());
			
			throw new IllegalStateException();
		}
		return value;
	}
	
	protected Condition joinAnd(Condition cond, Condition c)
	{
		if (cond == null)
			return c;
		
		if (cond instanceof ConditionLogicAnd cla)
		{
			cla.add(c);
			return cond;
		}
		
		final ConditionLogicAnd cla = new ConditionLogicAnd();
		cla.add(cond);
		cla.add(c);
		return cla;
	}
}