package com.shnok.javaserver.gameserver.network.clientpackets.item;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RequestInventoryUpdateOrder extends L2GameClientPacket
{
    @Getter
    @AllArgsConstructor
    public static class InventoryOrder {
        private final int objectID;
        private final int order;
    }

    /** client limit */
    private static final int LIMIT = 125;
    private List<InventoryOrder> orderList;

    @Override
    protected void readImpl()
    {
        int itemCount = readD();
        itemCount = Math.min(itemCount, LIMIT);

        orderList = new ArrayList<>(itemCount);
        for (int i = 0; i < itemCount; i++) {
            int objectId = readD();
            int order = readD();
            orderList.add(new InventoryOrder(objectId, order));
        }
    }

    @Override
    protected void runImpl()
    {
        final Player player = getClient().getPlayer();
        if (player == null)
            return;

        if (player.isInventoryDisabled())
            return;

        getOrderList().forEach((inventoryOrder -> {
            player.getInventory().moveItemAndRecord(inventoryOrder.getObjectID(), inventoryOrder.getOrder());
        }));

//        List<ItemInstance> items = player.getInventory().getUpdatedItems();
//
//        InventoryUpdatePacket iu = new InventoryUpdatePacket(items);
//        iu.writeMe();
//        player.sendPacket(iu);

      //  player.getInventory().resetAndApplyUpdatedItems();
    }
}