package com.shnok.javaserver.gameserver.skills.basefuncs;

import java.lang.reflect.Constructor;

import com.shnok.javaserver.commons.logging.CLogger;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.skills.L2Skill;
import com.shnok.javaserver.gameserver.skills.conditions.Condition;

public final class FuncTemplate
{
	private static final CLogger LOGGER = new CLogger(FuncTemplate.class.getName());
	
	private final Condition _attachCond;
	private final Condition _applyCond;
	private final Constructor<?> _constructor;
	private final Stats _stat;
	private final double _value;
	
	public FuncTemplate(Condition attachCond, Condition applyCond, String function, Stats stat, double value)
	{
		_attachCond = attachCond;
		_applyCond = applyCond;
		_stat = stat;
		_value = value;
		
		try
		{
			final Class<?> functionClass = Class.forName("com.shnok.javaserver.gameserver.skills.basefuncs.Func" + function);
			_constructor = functionClass.getConstructor(Object.class, Stats.class, Double.TYPE, Condition.class);
		}
		catch (ClassNotFoundException | NoSuchMethodException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the functions for skills.
	 * @param caster the caster
	 * @param target the target
	 * @param skill the skill
	 * @param owner the owner
	 * @return the function if conditions are met, {@code null} otherwise
	 */
	public Func getFunc(Creature caster, Creature target, L2Skill skill, Object owner)
	{
		return getFunc(caster, target, skill, null, owner);
	}
	
	/**
	 * Gets the functions for items.
	 * @param caster the caster
	 * @param target the target
	 * @param item the item
	 * @param owner the owner
	 * @return the function if conditions are met, {@code null} otherwise
	 */
	public Func getFunc(Creature caster, Creature target, ItemInstance item, Object owner)
	{
		return getFunc(caster, target, null, item, owner);
	}
	
	/**
	 * Gets the functions for skills and items.
	 * @param caster the caster
	 * @param target the target
	 * @param skill the skill
	 * @param item the item
	 * @param owner the owner
	 * @return the function if conditions are met, {@code null} otherwise
	 */
	private Func getFunc(Creature caster, Creature target, L2Skill skill, ItemInstance item, Object owner)
	{
		if (_attachCond != null && !_attachCond.test(caster, target, skill))
			return null;
		
		try
		{
			return (Func) _constructor.newInstance(owner, _stat, _value, _applyCond);
		}
		catch (Exception e)
		{
			LOGGER.error("An error occured during getFunc.", e);
		}
		return null;
	}
}