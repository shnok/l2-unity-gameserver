package com.shnok.javaserver.gameserver.data;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.commons.data.StatSet;

import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.skills.conditions.Condition;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public final class DocumentItem extends DocumentBase
{
	public class NewItem
	{
		public int id;
		public String type;
		public String name;
		public StatSet set;
		public int currentLevel;
		public Item item;
	}
	
	private NewItem _currentItem;
	private final List<Item> _itemsInFile = new ArrayList<>();
	
	public DocumentItem(File file)
	{
		super(file);
	}
	
	@Override
	protected StatSet getStatSet()
	{
		return _currentItem.set;
	}
	
	@Override
	protected String getTableValue(String name)
	{
		return _tables.get(name)[_currentItem.currentLevel];
	}
	
	@Override
	protected String getTableValue(String name, int idx)
	{
		return _tables.get(name)[idx - 1];
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
					if ("item".equalsIgnoreCase(d.getNodeName()))
					{
						try
						{
							_currentItem = new NewItem();
							parseItem(d);
							_itemsInFile.add(_currentItem.item);
							resetTable();
						}
						catch (Exception e)
						{
							LOGGER.error("Cannot create item {}.", e, _currentItem.id);
						}
					}
				}
			}
		}
	}
	
	protected void parseItem(Node n) throws InvocationTargetException
	{
		int itemId = Integer.parseInt(n.getAttributes().getNamedItem("id").getNodeValue());
		String className = n.getAttributes().getNamedItem("type").getNodeValue();
		String itemName = n.getAttributes().getNamedItem("name").getNodeValue();
		
		_currentItem.id = itemId;
		_currentItem.name = itemName;
		_currentItem.type = className;
		_currentItem.set = new StatSet();
		_currentItem.set.set("item_id", itemId);
		_currentItem.set.set("name", itemName);
		
		Node first = n.getFirstChild();
		for (n = first; n != null; n = n.getNextSibling())
		{
			if ("table".equalsIgnoreCase(n.getNodeName()))
			{
				if (_currentItem.item != null)
					throw new IllegalStateException("Item created but table node found! Item " + itemId);
				parseTable(n);
			}
			else if ("set".equalsIgnoreCase(n.getNodeName()))
			{
				if (_currentItem.item != null)
					throw new IllegalStateException("Item created but set node found! Item " + itemId);
				parseBeanSet(n, _currentItem.set, 1);
			}
			else if ("for".equalsIgnoreCase(n.getNodeName()))
			{
				makeItem();
				parseTemplate(n, _currentItem.item);
			}
			else if ("cond".equalsIgnoreCase(n.getNodeName()))
			{
				makeItem();
				Condition condition = parseCondition(n.getFirstChild(), _currentItem.item);
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
				_currentItem.item.attach(condition);
			}
		}
		// bah! in this point item doesn't have to be still created
		makeItem();
	}
	
	private void makeItem() throws InvocationTargetException
	{
		if (_currentItem.item != null)
			return; // item is already created
		try
		{
			Constructor<?> c = Class.forName("com.shnok.javaserver.gameserver.model.item.kind." + _currentItem.type).getConstructor(StatSet.class);
			_currentItem.item = (Item) c.newInstance(_currentItem.set);
		}
		catch (Exception e)
		{
			throw new InvocationTargetException(e);
		}
	}
	
	public List<Item> getItemList()
	{
		return _itemsInFile;
	}
}