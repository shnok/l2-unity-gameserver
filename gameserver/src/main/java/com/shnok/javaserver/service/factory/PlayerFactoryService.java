package com.shnok.javaserver.service.factory;

import com.shnok.javaserver.db.entity.DBCharacter;
import com.shnok.javaserver.db.entity.DBItem;
import com.shnok.javaserver.db.entity.DBPlayerItem;
import com.shnok.javaserver.db.repository.CharacterRepository;
import com.shnok.javaserver.db.repository.PlayerItemRepository;
import com.shnok.javaserver.enums.ItemLocation;
import com.shnok.javaserver.model.PlayerAppearance;
import com.shnok.javaserver.model.Point3D;
import com.shnok.javaserver.model.item.PlayerInventory;
import com.shnok.javaserver.model.object.ItemInstance;
import com.shnok.javaserver.model.object.entity.PlayerInstance;
import com.shnok.javaserver.model.template.PlayerTemplate;
import com.shnok.javaserver.service.WorldManagerService;
import com.shnok.javaserver.service.db.ItemTable;
import com.shnok.javaserver.thread.ai.PlayerAI;
import com.shnok.javaserver.util.VectorUtils;
import lombok.extern.log4j.Log4j2;

import java.util.List;

import static com.shnok.javaserver.config.Configuration.server;

@Log4j2
public class PlayerFactoryService {
    private static PlayerFactoryService instance;
    public static PlayerFactoryService getInstance() {
        if (instance == null) {
            instance = new PlayerFactoryService();
        }
        return instance;
    }

    public PlayerInstance getPlayerInstanceByAccount(String account) {
        List<DBCharacter> characters = CharacterRepository.getInstance().getCharactersForAccount(account);

        DBCharacter character;
        if(characters == null || characters.size() == 0) {
            if(server.playerSpecificCharacterEnabled()) {
                character = CharacterRepository.getInstance().getCharacterById(server.playerSpecificCharacterId());
            } else {
                character = CharacterRepository.getInstance().getRandomCharacter();
            }
        } else {
            character = characters.get(0);
        }

        return buildPlayerInstance(character);
    }

    public PlayerInstance getPlayerInstanceById(int charId) {
        DBCharacter character = CharacterRepository.getInstance().getCharacterById(charId);

        if(character == null) {
            return null;
        }

        return buildPlayerInstance(character);
    }

    private PlayerInstance buildPlayerInstance(DBCharacter character) {
        PlayerTemplate playerTemplate = new PlayerTemplate(character);
        PlayerInstance player = new PlayerInstance(WorldManagerService.getInstance().nextID(),
                character.getId(), character.getCharName(), playerTemplate);

        // Player items
        PlayerInventory inventory = getInventoryForPlayer(player);
        player.setInventory(inventory);

        // Player appearance
        PlayerAppearance appearance = new PlayerAppearance(character.getFace(), character.getHairColor(),
                character.getHairStyle(), character.getSex() == 1);
        player.setAppearance(appearance);

        //TODO: Use the character pos or add setting for defined spawn point

        // Using setWorldposition instead of setPosition so that knownlist wont get updated
        player.getPosition().setWorldPosition(VectorUtils.randomPos(
                new Point3D(server.spawnLocationX(), server.spawnLocationY(), server.spawnLocationZ())
                , 1.5f));
        player.setHeading(character.getHeading());

        // AI initialization
        PlayerAI ai = new PlayerAI(player);
        player.setAi(ai);

        return player;
    }

    private PlayerInventory getInventoryForPlayer(PlayerInstance player) {
        PlayerInventory playerInventory = new PlayerInventory(player);

        // Load equipped items
        List<DBPlayerItem> equipped = PlayerItemRepository.getInstance().getEquippedItemsForUser(player.getCharId());
        List<DBItem> equippedData = ItemTable.getInstance().getPlayerItemData(equipped);
        log.debug("Player {} has {} equipped item(s).", player.getId(), equippedData.size());

        for (DBItem item: equippedData) {
            ItemInstance itemInstance = new ItemInstance(player.getId(), item);
            itemInstance.setLocation(ItemLocation.EQUIPPED);
            playerInventory.addItem(itemInstance);
        }
        // Load inventory
        List<DBPlayerItem> inventory = PlayerItemRepository.getInstance().getInventoryItemsForUser(player.getCharId());
        List<DBItem> inventoryData = ItemTable.getInstance().getPlayerItemData(inventory);
        log.debug("Player {} has {} item(s) in his inventory.", player.getId(), inventoryData.size());

        for (DBItem item: inventoryData) {
            ItemInstance itemInstance = new ItemInstance(player.getId(), item);
            itemInstance.setLocation(ItemLocation.INVENTORY);
            playerInventory.addItem(itemInstance);
        }

        return playerInventory;
    }
}
