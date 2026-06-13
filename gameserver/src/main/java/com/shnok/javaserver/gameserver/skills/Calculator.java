package com.shnok.javaserver.gameserver.skills;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.skills.basefuncs.Func;
import com.shnok.javaserver.gameserver.skills.basefuncs.FuncSet;

/**
 * A {@link Calculator} is created to manage and dynamically calculate the effect of a property. It is a table of {@link Func}s in which each {@link Func} represents a mathematic function.<br>
 * <br>
 * When the calc() method is launched, each mathematic function is called according to its order. Lowest priority order is executed first and {@link Func}s with the same order are executed in unspecified order.
 */
public final class Calculator
{
	private static final Func[] EMPTY_FUNCS = new Func[0];
	
	private Func[] _functions = EMPTY_FUNCS;
	
	public Calculator()
	{
	}
	
	public Calculator(Calculator c)
	{
		_functions = c._functions;
	}
	
	/**
	 * @return the number of Funcs in the Calculator.
	 */
	public int size()
	{
		return _functions.length;
	}
	
	/**
	 * Adds a {@link Func} to the {@link Calculator}.
	 * @param function : The {@link Func} to add.
	 */
	public synchronized void addFunc(Func function)
	{
		Func[] funcs = _functions;
		Func[] tmp = new Func[funcs.length + 1];
		
		final int order = function.getOrder();
		int i;
		
		for (i = 0; i < funcs.length && order >= funcs[i].getOrder(); i++)
			tmp[i] = funcs[i];
		
		tmp[i] = function;
		
		for (; i < funcs.length; i++)
			tmp[i + 1] = funcs[i];
		
		_functions = tmp;
	}
	
	/**
	 * Removes a {@link Func} to the {@link Calculator}.
	 * @param function : The {@link Func} to remove.
	 */
	public synchronized void removeFunc(Func function)
	{
		Func[] funcs = _functions;
		Func[] tmp = new Func[funcs.length - 1];
		
		int i;
		
		for (i = 0; i < funcs.length && function != funcs[i]; i++)
			tmp[i] = funcs[i];
		
		if (i == funcs.length)
			return;
		
		for (i++; i < funcs.length; i++)
			tmp[i - 1] = funcs[i];
		
		if (tmp.length == 0)
			_functions = EMPTY_FUNCS;
		else
			_functions = tmp;
	}
	
	/**
	 * Remove each {@link Func} with the specified {@link Object} owner of the {@link Calculator}.
	 * @param owner : The {@link Object} owner.
	 * @return a {@link List} of modified stats.
	 */
	public synchronized List<Stats> removeOwner(Object owner)
	{
		List<Stats> modifiedStats = new ArrayList<>();
		
		for (Func func : _functions)
		{
			if (func.getFuncOwner() == owner)
			{
				modifiedStats.add(func.getStat());
				removeFunc(func);
			}
		}
		return modifiedStats;
	}
	
	/**
	 * Run each function of the {@link Calculator}.
	 * @param caster : The {@link Creature} who launched the effect.
	 * @param target : The {@link Creature} who receives the effect.
	 * @param skill : The {@link L2Skill} to launch.
	 * @param base : The base value for calculation.
	 * @return The calculated value affected by both {@link Creature}s and {@link L2Skill}.
	 */
	public double calc(Creature caster, Creature target, L2Skill skill, double base)
	{
		double value = base;
		for (Func func : _functions)
		{
			value = func.calc(caster, target, skill, base, value);
			
			// FuncSet is overriding the base value, other functions just update the value.
			if (func instanceof FuncSet)
				base = value;
		}
		
		return value;
	}
}