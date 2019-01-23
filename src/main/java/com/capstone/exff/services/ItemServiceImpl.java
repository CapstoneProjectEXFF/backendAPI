package com.capstone.exff.services;

import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.repositories.ItemRepository;
import com.capstone.exff.utilities.ExffError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemServices {
    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository){
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity createItem(String name, int user_id, String description) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(name);
        itemEntity.setUser_id(user_id);
        itemEntity.setDescription(description);
        return itemRepository.save(itemEntity);
    }

    @Override
    public ResponseEntity updateItem(int id, String name, String description) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null){
            return ResponseEntity.notFound().build();
        }

        itemEntity.setName(name);
        itemEntity.setDescription(description);
        try{
            newItemEntity = itemRepository.save(itemEntity);
        } catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(newItemEntity, HttpStatus.OK);
    }

    @Override
    public ResponseEntity removeItem(int id) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        if (itemEntity == null){
            return ResponseEntity.notFound().build();
        }

        try{
            itemRepository.delete(itemEntity);
        } catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok().build();
    }
}
