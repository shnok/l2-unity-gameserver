package com.shnok.javaserver.gameserver.model.multisell;

import java.util.List;

/**
 * A datatype which is part of multisell system. A multisell list can hold multiple Products.<br>
 * Each Product owns a List of "required part(s)" and "result(s)" known both as {@link Ingredient}s.
 */
public class Entry
{
	protected List<Ingredient> _ingredients;
	protected List<Ingredient> _products;
	protected boolean _stackable = true;
	
	public Entry(final List<Ingredient> ingredients, final List<Ingredient> products)
	{
		_ingredients = ingredients;
		_products = products;
		_stackable = products.stream().allMatch(Ingredient::isStackable);
	}
	
	/**
	 * This constructor used in PreparedEntry only, ArrayLists not created.
	 */
	protected Entry()
	{
	}
	
	public List<Ingredient> getProducts()
	{
		return _products;
	}
	
	public List<Ingredient> getIngredients()
	{
		return _ingredients;
	}
	
	public boolean isStackable()
	{
		return _stackable;
	}
	
	public int getTaxAmount()
	{
		return 0;
	}
}