package com.shnok.javaserver.gameserver.data.xml;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shnok.javaserver.commons.data.xml.IXmlReader;

import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.multisell.Entry;
import com.shnok.javaserver.gameserver.model.multisell.Ingredient;
import com.shnok.javaserver.gameserver.model.multisell.ListContainer;
import com.shnok.javaserver.gameserver.model.multisell.PreparedListContainer;
import com.shnok.javaserver.gameserver.network.serverpackets.MultiSellList;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * This class loads and stores multisell lists under {@link ListContainer}.<br>
 * Each ListContainer contains a List of {@link Entry}, and the list of allowed npcIds.<br>
 * <br>
 * File name is used as key, under its String hashCode.
 */
public class MultisellData implements IXmlReader
{
	public static final int PAGE_SIZE = 40;
	
	private final Map<Integer, ListContainer> _entries = new HashMap<>();
	
	public MultisellData()
	{
		load();
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/multisell");
		LOGGER.info("Loaded {} multisell.", _entries.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		final int id = path.toFile().getName().replaceAll(".xml", "").hashCode();
		final ListContainer list = new ListContainer(id);
		forEach(doc, "list", listNode ->
		{
			final NamedNodeMap attrs = listNode.getAttributes();
			
			list.setApplyTaxes(parseBoolean(attrs, "applyTaxes", false));
			list.setMaintainEnchantment(parseBoolean(attrs, "maintainEnchantment", false));
			
			forEach(listNode, "item", itemNode ->
			{
				final List<Ingredient> ingredients = new ArrayList<>();
				final List<Ingredient> products = new ArrayList<>();
				forEach(itemNode, "ingredient", ingredientNode -> ingredients.add(new Ingredient(parseAttributes(ingredientNode))));
				forEach(itemNode, "production", productionNode -> products.add(new Ingredient(parseAttributes(productionNode))));
				list.getEntries().add(new Entry(ingredients, products));
			});
			forEach(listNode, "npcs", npcsNode -> forEach(npcsNode, "npc", npcNode -> list.allowNpc(Integer.parseInt(npcNode.getTextContent()))));
			
			_entries.put(id, list);
		});
	}
	
	public void reload()
	{
		_entries.clear();
		
		load();
	}
	
	/**
	 * Send the correct multisell content to a {@link Player}.<br>
	 * <br>
	 * {@link ListContainer} template is first retrieved, based on its name, then {@link Npc} npcId check is done for security reason. Then the content is sent into {@link PreparedListContainer}, notably to check Player inventory. Finally a {@link MultiSellList} packet is sent to the Player. That
	 * new, prepared list is kept in memory on Player instance, mostly for memory reason.
	 * @param listName : The ListContainer list name.
	 * @param player : The Player to check.
	 * @param npc : The Npc to check (notably used for npcId check).
	 * @param inventoryOnly : if true we check inventory content.
	 */
	public void separateAndSend(String listName, Player player, Npc npc, boolean inventoryOnly)
	{
		final ListContainer template = _entries.get(listName.hashCode());
		if (template == null)
			return;
		
		if ((npc != null && !template.isNpcAllowed(npc.getNpcId())) || (npc == null && template.isNpcOnly()))
			return;
		
		final PreparedListContainer list = new PreparedListContainer(template, inventoryOnly, player, npc);
		
		int index = 0;
		do
		{
			// send list at least once even if size = 0
			player.sendPacket(new MultiSellList(list, index));
			index += PAGE_SIZE;
		}
		while (index < list.getEntries().size());
		
		player.setMultiSell(list);
	}
	
	public ListContainer getList(String listName)
	{
		return _entries.get(listName.hashCode());
	}
	
	public static MultisellData getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final MultisellData INSTANCE = new MultisellData();
	}
}