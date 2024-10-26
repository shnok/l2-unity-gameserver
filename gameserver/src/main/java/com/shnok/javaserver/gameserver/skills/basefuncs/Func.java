package com.shnok.javaserver.gameserver.skills.basefuncs;

import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.skills.Calculator;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.skills.conditions.Condition;
import net.sf.l2j.gameserver.skills.funcs.FuncMDefMod;
import net.sf.l2j.gameserver.skills.funcs.FuncPAtkMod;

/**
 * A super class for all functions. {@link Func} is container used to calculate a {@link Stats}'s value. It contains the {@link Stats}, order and the value itself.<br>
 * <br>
 * When the calculation is required (via {@link Calculator}), each {@link Func} is called sequentially by its order. Lowest priority order is executed first, while same order are executed in unspecified order.<br>
 * <ul>
 * The order is defined by each inherited {@link Func}, as following:
 * <li>The base for all calculation is given from template.</li>
 * <li>0 = {@link FuncSet} is overriding the base (template value). E.g. weapon's P.Atk.</li>
 * <li>1 = {@link FuncBaseMul} is adding a flat ratio of base value to the result. Used by skills for critical chance only.</li>
 * <li>2 = {@link FuncBaseAdd} is adding a value to the result. Used by items's P.Def. and M.Def.</li>
 * <li>3 = {@link FuncEnchant} is adding a value to the result, based on enchant level of owned item. Used by items.</li>
 * <li>10 = Finalization of base value calculation, prior to other calculations. E.g {@link FuncPAtkMod}, {@link FuncMDefMod}, etc. Used by default in {@link Creature}.</li>
 * <li>20 = {@link FuncMul} and {@link FuncDiv} are providing multiplication operation of the result and value. E.g. skills' percentage bonuses.</li>
 * <li>30 = {@link FuncAdd} and {@link FuncSub} are providing summary operation of the result and value. E.g. skills' bonuses.</li>
 * <li>40 = {@link FuncAddMul} and {@link FuncSubDiv} are providing multiplication operation of the result and percent value. E.g. skills affecting resistances.</li>
 * </ul>
 */
public abstract class Func
{
	private final Object _owner;
	
	private final Stats _stat;
	private final int _order;
	private final double _value;
	
	private final Condition _condition;
	
	protected Func(Object owner, Stats stat, int order, double value, Condition condition)
	{
		_owner = owner;
		
		_stat = stat;
		_order = order;
		_value = value;
		
		_condition = condition;
	}
	
	/**
	 * Run the mathematics function of the {@link Func}.
	 * @param effector : The {@link Creature} who launches the effect.
	 * @param effected : The {@link Creature} who receives the effect.
	 * @param skill : The {@link L2Skill} used to calculate.
	 * @param base : The base value prior any calculation.
	 * @param value : The continuously updated result.
	 * @return The calculated value.
	 */
	public abstract double calc(Creature effector, Creature effected, L2Skill skill, double base, double value);
	
	public final Stats getStat()
	{
		return _stat;
	}
	
	public final int getOrder()
	{
		return _order;
	}
	
	public final Object getFuncOwner()
	{
		return _owner;
	}
	
	public final double getValue()
	{
		return _value;
	}
	
	public final Condition getCond()
	{
		return _condition;
	}
}