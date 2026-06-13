package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.records.Recipe;

import org.w3c.dom.Document;

/**
 * This class loads and stores {@link Recipe}s. Recipes are part of craft system, which uses a Recipe associated to items (materials) to craft another item (product).
 */
public class RecipeData implements IXmlReader
{
	private final Map<Integer, Recipe> _recipes = new HashMap<>();
	
	protected RecipeData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/recipes.xml");
		LOGGER.info("Loaded {} recipes.", _recipes.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "recipe", recipeNode ->
		{
			final StatSet set = parseAttributes(recipeNode);
			_recipes.put(set.getInteger("id"), new Recipe(set));
		}));
	}
	
	public Recipe getRecipeList(int listId)
	{
		return _recipes.get(listId);
	}
	
	public Recipe getRecipeByItemId(int itemId)
	{
		return _recipes.values().stream().filter(r -> r.recipeId() == itemId).findFirst().orElse(null);
	}
	
	public static RecipeData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RecipeData INSTANCE = new RecipeData();
	}
}