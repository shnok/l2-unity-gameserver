package com.shnok.javaserver.gameserver.skills.effects;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.commons.logging.CLogger;

import net.sf.l2j.gameserver.enums.skills.AbnormalEffect;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.ChanceCondition;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.basefuncs.FuncTemplate;
import net.sf.l2j.gameserver.skills.conditions.Condition;

public class EffectTemplate
{
	private static final CLogger LOGGER = new CLogger(EffectTemplate.class.getName());
	
	private final Class<?> _func;
	private final Constructor<?> _constructor;
	
	private final Condition _attachCond;
	
	private final double _value;
	private final int _counter;
	private final int _period;
	
	private final AbnormalEffect _abnormalEffect;
	private List<FuncTemplate> _funcTemplates;
	
	private final String _stackType;
	private final float _stackOrder;
	
	private final boolean _showIcon;
	
	private final double _effectPower;
	private final SkillType _effectType;
	
	private final int _triggeredId;
	private final int _triggeredLevel;
	private final ChanceCondition _chanceCondition;
	
	public EffectTemplate(Condition attachCond, String funcName, double value, int counter, int period, AbnormalEffect abnormalEffect, String stackType, float stackOrder, boolean showIcon, double effectPower, SkillType effectType, int triggeredId, int triggeredLevel, ChanceCondition chanceCondition)
	{
		_attachCond = attachCond;
		
		_value = value;
		_counter = counter;
		_period = period;
		
		_abnormalEffect = abnormalEffect;
		
		_stackType = stackType;
		_stackOrder = stackOrder;
		
		_showIcon = showIcon;
		
		_effectPower = effectPower;
		_effectType = effectType;
		
		_triggeredId = triggeredId;
		_triggeredLevel = triggeredLevel;
		_chanceCondition = chanceCondition;
		
		try
		{
			_func = Class.forName("net.sf.l2j.gameserver.skills.effects.Effect" + funcName);
		}
		catch (ClassNotFoundException e)
		{
			throw new RuntimeException(e);
		}
		
		try
		{
			_constructor = _func.getConstructor(EffectTemplate.class, L2Skill.class, Creature.class, Creature.class);
		}
		catch (NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	public double getValue()
	{
		return _value;
	}
	
	public int getCounter()
	{
		return _counter;
	}
	
	public int getPeriod()
	{
		return _period;
	}
	
	public AbnormalEffect getAbnormalEffect()
	{
		return _abnormalEffect;
	}
	
	public List<FuncTemplate> getFuncTemplates()
	{
		return _funcTemplates;
	}
	
	public String getStackType()
	{
		return _stackType;
	}
	
	public float getStackOrder()
	{
		return _stackOrder;
	}
	
	public boolean showIcon()
	{
		return _showIcon;
	}
	
	public double getEffectPower()
	{
		return _effectPower;
	}
	
	public SkillType getEffectType()
	{
		return _effectType;
	}
	
	public int getTriggeredId()
	{
		return _triggeredId;
	}
	
	public int getTriggeredLevel()
	{
		return _triggeredLevel;
	}
	
	public ChanceCondition getChanceCondition()
	{
		return _chanceCondition;
	}
	
	public AbstractEffect getEffect(Creature caster, Creature target, L2Skill skill)
	{
		if (_attachCond != null && !_attachCond.test(caster, target, skill))
			return null;
		
		try
		{
			return (AbstractEffect) _constructor.newInstance(this, skill, target, caster);
		}
		catch (Exception e)
		{
			LOGGER.error("Error creating new instance of {}.", e, _func);
			return null;
		}
	}
	
	public void attach(FuncTemplate f)
	{
		if (_funcTemplates == null)
			_funcTemplates = new ArrayList<>();
		
		_funcTemplates.add(f);
	}
	
	/**
	 * @param template : The {@link EffectTemplate} to test.
	 * @return True if the {@link EffectTemplate} set as parameter is of same stack order and type.
	 */
	public boolean isSameStackTypeAndOrderThan(EffectTemplate template)
	{
		return _stackOrder == template.getStackOrder() && _stackType.equals(template.getStackType());
	}
}