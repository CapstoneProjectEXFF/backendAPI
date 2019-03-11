package com.capstone.exff.services;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.repositories.ItemRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

import static com.capstone.exff.constants.ExffStatus.*;

@Service
public class ItemServiceImpl implements ItemServices {
    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity createItem(String name, int userId, String description, String address, String privacy, Timestamp createTime, int categoryId) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(name);
        itemEntity.setUserId(userId);
        itemEntity.setDescription(description);
        itemEntity.setAddress(address);
        itemEntity.setPrivacy(privacy);
        itemEntity.setStatus(ITEM_ENABLE);
        itemEntity.setCreateTime(createTime);
        itemEntity.setCategoryId(categoryId);

        itemEntity = itemRepository.save(itemEntity);
        itemRepository.flush();
        return itemEntity;
    }

    @Override
    public ResponseEntity updateItem(int id, String name, int userId, String description, String address, String privacy, Timestamp modifyTime, int categoryId) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.getStatus().equals(ITEM_ENABLE)) {
            itemEntity.setName(name);
            itemEntity.setDescription(description);
            itemEntity.setAddress(address);
            itemEntity.setModifyTime(modifyTime);
            itemEntity.setPrivacy(privacy);
            itemEntity.setCategoryId(categoryId);

            try {
                newItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(newItemEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this item", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity removeItem(int id, int userId) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity removedItemEntity;
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.getStatus().equals(ITEM_ENABLE)) {
            try {
                itemEntity.setStatus(ITEM_DISABLE);
                removedItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(removedItemEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this item", HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity setItemUnavailable(int id) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getStatus().equals(ITEM_ENABLE)) {
            itemEntity.setStatus(ITEM_DISABLE);
            try {
                newItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(newItemEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this item", HttpStatus.OK);
        }
    }

    @Override
    public List<ItemEntity> findItemsByItemName(String itemName) {
        return itemRepository.findItemsByItemName(itemName);
    }

    @Override
    public List<ItemEntity> loadAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public List<ItemEntity> verifyItems(String status, List<Integer> ids) {
        return itemRepository.verifyItems(status, ids);
    }

    @Override
    public List<ItemEntity> getItemsByUserId(int userId) {
        return itemRepository.findItemEntitiesByUserId(userId);
    }

    @Override
    public ItemEntity getItemById(int itemId) {
        return itemRepository.getItemById(itemId);
    }
}
