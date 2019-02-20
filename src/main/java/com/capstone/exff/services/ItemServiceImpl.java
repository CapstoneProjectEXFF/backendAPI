package com.capstone.exff.services;

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
    public ItemServiceImpl(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity createItem(String name, int userId, String description, String address, boolean tmpPrivacy, Timestamp createTime, int categoryId) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(name);
        itemEntity.setUserId(userId);
        itemEntity.setDescription(description);
        itemEntity.setAddress(address);
        itemEntity.setStatus(ITEM_ENABLE);
        itemEntity.setCreateTime(createTime);
        itemEntity.setCategoryId(categoryId);

        if (tmpPrivacy == false){
            itemEntity.setPrivacy(ITEM_PRIVACY_PUBLIC);
        } else {
            itemEntity.setPrivacy(ITEM_PRIVACY_FRIENDS);
        }

        return itemRepository.save(itemEntity);
    }

    @Override
    public ResponseEntity updateItem(int id, String name, int userId, String description, String address, boolean tmpPrivacy, Timestamp modifyTime, int categoryId) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null){
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.getStatus() == ITEM_ENABLE) {
            itemEntity.setName(name);
            itemEntity.setDescription(description);
            itemEntity.setAddress(address);
            itemEntity.setModifyTime(modifyTime);
            itemEntity.setCategoryId(categoryId);

            if (tmpPrivacy == false){
                itemEntity.setPrivacy(ITEM_PRIVACY_PUBLIC);
            } else {
                itemEntity.setPrivacy(ITEM_PRIVACY_FRIENDS);
            }

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
        if (itemEntity == null){
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.getStatus() == ITEM_ENABLE) {
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
    public List<ItemEntity> findItemsByItemName(String itemName) {
        return itemRepository.findItemsByItemName(itemName);
    }

    @Override
    public List<ItemEntity> loadAllItems() {
        return itemRepository.findAll();
    }

}
