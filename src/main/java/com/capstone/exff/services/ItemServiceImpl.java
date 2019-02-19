package com.capstone.exff.services;

import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.repositories.ItemRepository;
import com.capstone.exff.utilities.ExffError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemServices {
    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity createItem(String name, int userId, String description) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(name);
        itemEntity.setUserId(userId);
        itemEntity.setDescription(description);
        itemEntity.setStatus(true);
        return itemRepository.save(itemEntity);
    }

    @Override
    public ResponseEntity updateItem(int id, String name, String description, int userId) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null){
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.isStatus() == true) {
            itemEntity.setName(name);
            itemEntity.setDescription(description);
            try {
                newItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
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
        if (itemEntity.getUserId() == userId && itemEntity.isStatus() == true) {
            try {
                itemEntity.setStatus(false);
                removedItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
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
