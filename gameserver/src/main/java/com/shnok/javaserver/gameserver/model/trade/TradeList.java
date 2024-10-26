package com.shnok.javaserver.gameserver.model.trade;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import net.sf.l2j.gameserver.data.xml.ItemData;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.itemcontainer.PcInventory;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;

public class TradeList extends CopyOnWriteArrayList<TradeItem>
{
	private static final long serialVersionUID = 1L;
	
	private final Player _owner;
	
	private Player _partner;
	private String _title;
	
	private AtomicBoolean _isPackaged = new AtomicBoolean();
	private AtomicBoolean _isConfirmed = new AtomicBoolean();
	private AtomicBoolean _isLocked = new AtomicBoolean();
	
	public TradeList(Player owner)
	{
		_owner = owner;
	}
	
	@Override
	public void clear()
	{
		super.clear();
		
		_partner = null;
		
		_isPackaged.set(false);
		_isConfirmed.set(false);
		_isLocked.set(false);
	}
	
	@Override
	public String toString()
	{
		return "TradeList [owner=" + _owner + ", partner=" + _partner + ", title=" + _title + ", isPackaged=" + _isPackaged.get() + ", isConfirmed=" + _isConfirmed.get() + ", isLocked=" + _isLocked.get() + "]";
	}
	
	public Player getOwner()
	{
		return _owner;
	}
	
	public Player getPartner()
	{
		return _partner;
	}
	
	public void setPartner(Player partner)
	{
		_partner = partner;
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public void setTitle(String title)
	{
		_title = title;
	}
	
	public boolean isPackaged()
	{
		return _isPackaged.get();
	}
	
	public void setPackaged(boolean value)
	{
		_isPackaged.set(value);
	}
	
	public boolean isConfirmed()
	{
		return _isConfirmed.get();
	}
	
	public AtomicBoolean getLock()
	{
		return _isLocked;
	}
	
	public boolean isLocked()
	{
		return _isLocked.get();
	}
	
	/**
	 * Lock this {@link TradeList}, meaning than no further changes are allowed.
	 */
	public void lock()
	{
		_isLocked.set(true);
	}
	
	/**
	 * @param inventory : The {@link PcInventory} to test.
	 * @return A cloned {@link List} of this {@link TradeList} adjusted to {@link PcInventory} available items.
	 */
	public List<TradeItem> getAvailableItems(PcInventory inventory)
	{
		return stream().peek(inventory::adjustAvailableItem).toList();
	}
	
	/**
	 * Create a {@link TradeItem} based on an existing {@link ItemInstance}, and add it to this {@link TradeList}.
	 * @param objectId : The {@link WorldObject} objectId to test.
	 * @param count : The amount of newly formed {@link TradeItem}.
	 * @param price : The price of newly formed {@link TradeItem}.
	 * @return A {@link TradeItem} based on {@link ItemInstance}, which is itself retrieved from its objectId from {@link World#getObject(int)}.
	 */
	public TradeItem addItem(int objectId, int count, int price)
	{
		if (isConfirmed() || isLocked())
			return null;
		
		final WorldObject object = World.getInstance().getObject(objectId);
		if (!(object instanceof ItemInstance item))
			return null;
		
		if (!item.isTradable() || item.isQuestItem())
			return null;
		
		if (count <= 0 || count > item.getCount())
			return null;
		
		if (!item.isStackable() && count > 1)
			return null;
		
		if ((Integer.MAX_VALUE / count) < price)
			return null;
		
		for (TradeItem checkItem : this)
		{
			if (checkItem.getObjectId() == objectId)
			{
				final int newCount = checkItem.getCount() + count;
				if (item.getCount() < newCount)
					return null;
				
				checkItem.setCount(newCount);
				return checkItem;
			}
		}
		
		final TradeItem tradeItem = new TradeItem(item, count, price);
		add(tradeItem);
		
		return tradeItem;
	}
	
	/**
	 * Create a {@link TradeItem} based on itemId, and add it to this {@link TradeList}.
	 * @param itemId : The itemId of newly formed {@link TradeItem}.
	 * @param count : The amount of newly formed {@link TradeItem}.
	 * @param price : The price of newly formed {@link TradeItem}.
	 * @param enchant : The enchant value of newly formed {@link TradeItem}.
	 * @return A {@link TradeItem} based on itemId.
	 */
	public TradeItem addItemByItemId(int itemId, int count, int price, int enchant)
	{
		if (isConfirmed() || isLocked())
			return null;
		
		final Item item = ItemData.getInstance().getTemplate(itemId);
		if (item == null)
			return null;
		
		if (!item.isTradable() || item.isQuestItem())
			return null;
		
		if (!item.isStackable() && count > 1)
			return null;
		
		if ((Integer.MAX_VALUE / count) < price)
			return null;
		
		final TradeItem tradeItem = new TradeItem(item, count, price, enchant);
		add(tradeItem);
		
		return tradeItem;
	}
	
	/**
	 * Remove or decrease amount of a {@link TradeItem} from this {@link TradeList}, by either its objectId or itemId.
	 * @param objectId : The objectId to test.
	 * @param itemId : The itemId ot test.
	 * @param count : The amount to remove.
	 */
	private void removeItem(int objectId, int itemId, int count)
	{
		if (isConfirmed() || isLocked())
			return;
		
		for (TradeItem tradeItem : this)
		{
			if (tradeItem.getObjectId() == objectId || tradeItem.getItem().getItemId() == itemId)
			{
				// Reduce item count or complete item.
				if (count == -1)
					remove(tradeItem);
				else
				{
					tradeItem.setCount(tradeItem.getCount() - count);
					tradeItem.setQuantity(tradeItem.getQuantity() - count);
					
					if (tradeItem.getQuantity() <= 0)
						remove(tradeItem);
				}
				break;
			}
		}
	}
	
	/**
	 * Update {@link TradeItem}s from this {@link TradeList} according to their quantity in owner inventory.
	 * @param isBuyList : If True, we don't check {@link TradeItem}s item count integrity. We still check if item exists in inventory, though.
	 */
	public void updateItems(boolean isBuyList)
	{
		if (isConfirmed() || isLocked())
			return;
		
		for (TradeItem tradeItem : this)
		{
			if (isBuyList)
			{
				// If itemId can't be found on the inventory, remove the item.
				final ItemInstance item = _owner.getInventory().getItemByItemId(tradeItem.getItemId());
				if (item == null)
					remove(tradeItem);
			}
			else
			{
				// If objectId can't be found on the inventory or TradeItem integrity is invalid, remove the item.
				final ItemInstance item = _owner.getInventory().getItemByObjectId(tradeItem.getObjectId());
				if (item == null || tradeItem.getCount() < 1 || item.isEquipped())
					remove(tradeItem);
				// Otherwise, test the count and set it to the maximum available.
				else if (item.getCount() < tradeItem.getCount())
					tradeItem.setCount(item.getCount());
			}
		}
	}
	
	/**
	 * Confirm this {@link TradeList}, cancelling the trade if checks aren't properly passed (distance, items manipulation, etc).<br>
	 * <br>
	 * In case partner already confirmed its {@link TradeList}, then proceed to the exchange. Otherwise confirm this {@link TradeList}.
	 */
	public void confirm()
	{
		if (isConfirmed() || isLocked())
			return;
		
		if (_partner == null)
		{
			_owner.cancelActiveTrade();
			return;
		}
		
		final TradeList partnerList = _partner.getActiveTradeList();
		if (partnerList == null)
		{
			_owner.cancelActiveTrade();
			return;
		}
		
		if (!_isConfirmed.compareAndSet(false, true))
			return;
		
		// If partner has already confirmed this trade, proceed to the exchange.
		if (partnerList.isConfirmed())
		{
			// Lock both TradeLists.
			if (_isLocked.compareAndSet(false, true) && partnerList.getLock().compareAndSet(false, true))
			{
				// Test the validity of TradeLists.
				if (!validate(_partner, true) || !partnerList.validate(_owner, true))
				{
					_owner.cancelActiveTrade();
					return;
				}
				
				// We passed all tests ; finally exchange.
				doExchange(partnerList);
			}
		}
		// Otherwise, we are the first to try to confirm the trade.
		else
		{
			// Test the validity of TradeLists.
			if (!validate(_partner, false) || !partnerList.validate(_owner, false))
			{
				_owner.cancelActiveTrade();
				return;
			}
			
			// Test is passed ; confirm our TradeList.
			_partner.onTradeConfirm(_owner);
		}
	}
	
	/**
	 * Test the validity of this {@link TradeList}.
	 * @param partner : The {@link Player} partner to test.
	 * @param isCheckingItems : If True, we also check item manipulation.
	 * @return True if all tests passed, false otherwise.
	 */
	private boolean validate(Player partner, boolean isCheckingItems)
	{
		// Check owner validity.
		if (_owner == null || !_owner.isOnline())
			return false;
		
		// Check partner validity.
		if (partner == null || !partner.isOnline() || !_owner.isIn3DRadius(partner, Npc.INTERACTION_DISTANCE))
			return false;
		
		// Check item validity.
		if (isCheckingItems)
			return stream().allMatch(tradeItem -> _owner.checkItemManipulation(tradeItem.getObjectId(), tradeItem.getCount()) != null);
		
		return true;
	}
	
	/**
	 * Transfer all {@link TradeItem}s of this {@link TradeList} from {@link Player} owner inventory to {@link Player} partner.
	 * @param partner : The {@link Player} used as partner.
	 * @param owner : The {@link Player} used as owner.
	 */
	private void transferItems(Player partner, Player owner)
	{
		forEach(tradeItem -> _owner.getInventory().transferItem(tradeItem.getObjectId(), tradeItem.getCount(), partner));
	}
	
	/**
	 * Proceed to the transfer of items, if all tests successfully passed.
	 * @param partnerTradeList : The {@link TradeList} of the {@link Player} partner.
	 */
	private void doExchange(TradeList partnerTradeList)
	{
		boolean isSuccessful = true;
		
		// Check weight integrity.
		if (!_owner.getInventory().validateTradeListWeight(partnerTradeList) || !partnerTradeList.getOwner().getInventory().validateTradeListWeight(this))
		{
			isSuccessful = false;
			
			_owner.sendPacket(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
			partnerTradeList.getOwner().sendPacket(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
		}
		// Check inventory slots integrity.
		else if (!_owner.getInventory().validateTradeListCapacity(partnerTradeList) || !partnerTradeList.getOwner().getInventory().validateTradeListCapacity(this))
		{
			isSuccessful = false;
			
			_owner.sendPacket(SystemMessageId.SLOTS_FULL);
			partnerTradeList.getOwner().sendPacket(SystemMessageId.SLOTS_FULL);
		}
		// Check count integrity.
		else if (!_owner.getInventory().validateTradeListCount(partnerTradeList) || !partnerTradeList.getOwner().getInventory().validateTradeListCount(this))
		{
			isSuccessful = false;
		}
		// Check if both TradeLists were empty.
		else if (isEmpty() && partnerTradeList.isEmpty())
		{
			isSuccessful = false;
		}
		// All tests passed, it's a success.
		else
		{
			// Transfer items.
			partnerTradeList.transferItems(_owner, partnerTradeList.getOwner());
			transferItems(partnerTradeList.getOwner(), _owner);
		}
		
		// Finish the trade.
		_owner.onTradeFinish(isSuccessful);
		partnerTradeList.getOwner().onTradeFinish(isSuccessful);
	}
	
	/**
	 * Buy items from this {@link TradeList}.
	 * @param player : The {@link Player} who tries to buy.
	 * @param items : The array of {@link ItemRequest}s to test.
	 * @return True if all checks passed and the buy was successful, or false otherwise.
	 */
	public boolean privateStoreBuy(Player player, ItemRequest[] items)
	{
		if (isConfirmed() || isLocked())
			return false;
		
		// Test the validity of this TradeList.
		if (!validate(player, false))
			return false;
		
		int totalSlots = 0;
		int totalWeight = 0;
		long totalPrice = 0;
		
		final PcInventory ownerInventory = _owner.getInventory();
		final PcInventory playerInventory = player.getInventory();
		
		// Check requested items.
		for (ItemRequest item : items)
		{
			// Sanity check.
			if (item.getCount() < 1)
				return false;
			
			// Check if request is actual part of that TradeList.
			final TradeItem tradeItem = stream().filter(ti -> ti.getObjectId() == item.getObjectId() && ti.getPrice() == item.getPrice()).findFirst().orElse(null);
			if (tradeItem == null)
				return false;
			
			// Don't go further if item template doesn't exist. Retrieve TradeItem itemId, since ItemRequest's is blank at this point.
			final Item template = ItemData.getInstance().getTemplate(tradeItem.getItemId());
			if (template == null)
				return false;
			
			// Check if requested item is available for manipulation.
			final ItemInstance oldItem = _owner.checkItemManipulation(item.getObjectId(), item.getCount());
			if (oldItem == null || !oldItem.isTradable())
				return false;
			
			// Integer overflow check for the single item.
			if ((Integer.MAX_VALUE / item.getCount()) < item.getPrice())
				return false;
			
			totalPrice += item.getCount() * item.getPrice();
			
			// Integer overflow check for the total price.
			if (Integer.MAX_VALUE < totalPrice || totalPrice < 0)
				return false;
			
			totalWeight += item.getCount() * template.getWeight();
			
			if (!template.isStackable())
				totalSlots += item.getCount();
			else if (playerInventory.getItemByItemId(item.getItemId()) == null)
				totalSlots++;
		}
		
		if (totalPrice > playerInventory.getAdena())
		{
			player.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
			return false;
		}
		
		if (!playerInventory.validateWeight(totalWeight))
		{
			player.sendPacket(SystemMessageId.WEIGHT_LIMIT_EXCEEDED);
			return false;
		}
		
		if (!playerInventory.validateCapacity(totalSlots))
		{
			player.sendPacket(SystemMessageId.SLOTS_FULL);
			return false;
		}
		
		// Transfer items.
		for (ItemRequest item : items)
		{
			// Proceed with item transfer. If transfer fails, reduce total price.
			final ItemInstance newItem = ownerInventory.transferItem(item.getObjectId(), item.getCount(), player);
			if (newItem == null)
			{
				totalPrice -= item.getCount() * item.getPrice();
				continue;
			}
			
			removeItem(item.getObjectId(), -1, item.getCount());
			
			// Send messages about the transaction to both players.
			if (newItem.isStackable())
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PURCHASED_S3_S2_S);
				sm.addString(player.getName());
				sm.addItemName(newItem.getItemId());
				sm.addNumber(item.getCount());
				_owner.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.PURCHASED_S3_S2_S_FROM_S1);
				sm.addString(_owner.getName());
				sm.addItemName(newItem.getItemId());
				sm.addNumber(item.getCount());
				player.sendPacket(sm);
			}
			else if (newItem.getEnchantLevel() > 0)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PURCHASED_S2_S3);
				sm.addString(player.getName());
				sm.addNumber(newItem.getEnchantLevel());
				sm.addItemName(newItem.getItemId());
				_owner.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.PURCHASED_S2_S3_FROM_S1);
				sm.addString(_owner.getName());
				sm.addNumber(newItem.getEnchantLevel());
				sm.addItemName(newItem.getItemId());
				player.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PURCHASED_S2);
				sm.addString(player.getName());
				sm.addItemName(newItem.getItemId());
				_owner.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.PURCHASED_S2_FROM_S1);
				sm.addString(_owner.getName());
				sm.addItemName(newItem.getItemId());
				player.sendPacket(sm);
			}
		}
		
		// Transfer adena.
		if (totalPrice > 0)
		{
			playerInventory.reduceAdena((int) totalPrice);
			ownerInventory.addAdena((int) totalPrice);
		}
		
		return true;
	}
	
	/**
	 * Sell items from this {@link TradeList}.
	 * @param player : The {@link Player} who tries to sell.
	 * @param items : The array of {@link ItemRequest} to test.
	 * @return True if all checks passed and the buy was successful, or false otherwise.
	 */
	public boolean privateStoreSell(Player player, ItemRequest[] items)
	{
		if (isConfirmed() || isLocked())
			return false;
		
		// Test the validity of this TradeList.
		if (!validate(player, false))
			return false;
		
		final PcInventory ownerInventory = _owner.getInventory();
		final PcInventory playerInventory = player.getInventory();
		
		long totalPrice = 0;
		
		// Check requested items.
		for (ItemRequest item : items)
		{
			// Sanity check.
			if (item.getCount() < 1)
				return false;
			
			// Don't go further if item template doesn't exist.
			final Item template = ItemData.getInstance().getTemplate(item.getItemId());
			if (template == null)
				return false;
			
			// Check if request is actual part of that TradeList.
			if (stream().noneMatch(ti -> ti.getItemId() == item.getItemId() && ti.getPrice() == item.getPrice()))
				return false;
			
			// Check if requested item is available for manipulation.
			final ItemInstance oldItem = player.checkItemManipulation(item.getObjectId(), item.getCount());
			if (oldItem == null || !oldItem.isTradable() || oldItem.getItemId() != item.getItemId() || oldItem.getEnchantLevel() != item.getEnchant())
				return false;
			
			// Integer overflow check for the single item.
			if ((Integer.MAX_VALUE / item.getCount()) < item.getPrice())
				return false;
			
			totalPrice += item.getCount() * item.getPrice();
			
			// Integer overflow check for the total price.
			if (Integer.MAX_VALUE < totalPrice || totalPrice < 0)
				return false;
		}
		
		if (totalPrice > ownerInventory.getAdena())
		{
			player.sendPacket(SystemMessageId.YOU_NOT_ENOUGH_ADENA);
			return false;
		}
		
		// Transfer items.
		for (ItemRequest item : items)
		{
			// Proceed with item transfer. If transfer fails, reduce total price.
			final ItemInstance newItem = playerInventory.transferItem(item.getObjectId(), item.getCount(), _owner);
			if (newItem == null)
			{
				totalPrice -= item.getCount() * item.getPrice();
				continue;
			}
			
			removeItem(-1, item.getItemId(), item.getCount());
			
			// Send messages about the transaction to both players.
			if (newItem.isStackable())
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PURCHASED_S3_S2_S_FROM_S1);
				sm.addString(player.getName());
				sm.addItemName(newItem.getItemId());
				sm.addNumber(item.getCount());
				_owner.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PURCHASED_S3_S2_S);
				sm.addString(_owner.getName());
				sm.addItemName(newItem.getItemId());
				sm.addNumber(item.getCount());
				player.sendPacket(sm);
			}
			else if (newItem.getEnchantLevel() > 0)
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PURCHASED_S2_S3_FROM_S1);
				sm.addString(player.getName());
				sm.addNumber(newItem.getEnchantLevel());
				sm.addItemName(newItem.getItemId());
				_owner.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PURCHASED_S2_S3);
				sm.addString(_owner.getName());
				sm.addNumber(newItem.getEnchantLevel());
				sm.addItemName(newItem.getItemId());
				player.sendPacket(sm);
			}
			else
			{
				SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.PURCHASED_S2_FROM_S1);
				sm.addString(player.getName());
				sm.addItemName(newItem.getItemId());
				_owner.sendPacket(sm);
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.S1_PURCHASED_S2);
				sm.addString(_owner.getName());
				sm.addItemName(newItem.getItemId());
				player.sendPacket(sm);
			}
		}
		
		// Transfer adena.
		if (totalPrice > 0)
		{
			ownerInventory.reduceAdena((int) totalPrice);
			playerInventory.addAdena((int) totalPrice);
		}
		
		return true;
	}
}