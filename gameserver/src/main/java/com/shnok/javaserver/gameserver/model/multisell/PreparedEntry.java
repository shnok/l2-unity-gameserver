package com.shnok.javaserver.gameserver.model.multisell;

import java.util.ArrayList;

import net.sf.l2j.gameserver.model.item.instance.ItemInstance;

/**
 * A dynamic layer of {@link Entry}, which holds the tax amount and can retain previous {@link ItemInstance} enchantment.
 */
public class PreparedEntry extends Entry
{
	private int _taxAmount = 0;
	
	public PreparedEntry(Entry template, ItemInstance item, boolean applyTaxes, boolean maintainEnchantment, double taxRate)
	{
		int adenaAmount = 0;
		
		_ingredients = new ArrayList<>(template.getIngredients().size());
		for (Ingredient ing : template.getIngredients())
		{
			if (ing.getItemId() == 57)
			{
				// Tax ingredients added only if taxes enabled
				if (ing.isTaxIngredient())
				{
					// if taxes are to be applied, modify/add the adena count based on the template adena/ancient adena count
					if (applyTaxes)
						_taxAmount += Math.round(ing.getItemCount() * taxRate);
				}
				else
					adenaAmount += ing.getItemCount();
				
				// do not yet add this adena amount to the list as non-taxIngredient adena might be entered later (order not guaranteed)
				continue;
			}
			
			final Ingredient newIngredient = ing.getCopy();
			if (maintainEnchantment && item != null && ing.isArmorOrWeapon())
				newIngredient.setEnchantLevel(item.getEnchantLevel());
			
			_ingredients.add(newIngredient);
		}
		
		// now add the adena, if any.
		adenaAmount += _taxAmount; // do not forget tax
		if (adenaAmount > 0)
			_ingredients.add(new Ingredient(57, adenaAmount, false, false));
		
		// now copy products
		_products = new ArrayList<>(template.getProducts().size());
		for (Ingredient ing : template.getProducts())
		{
			if (!ing.isStackable())
				_stackable = false;
			
			final Ingredient newProduct = ing.getCopy();
			if (maintainEnchantment && item != null && ing.isArmorOrWeapon())
				newProduct.setEnchantLevel(item.getEnchantLevel());
			
			_products.add(newProduct);
		}
	}
	
	@Override
	public final int getTaxAmount()
	{
		return _taxAmount;
	}
}