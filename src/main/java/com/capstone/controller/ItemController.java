package com.capstone.controller;

import com.capstone.model.Item;
import com.capstone.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ItemController {

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/item/", method = RequestMethod.POST)
    public Item createItem(@Valid @RequestBody Item item){
        return itemRepository.save(item);
    }

    @RequestMapping(value = "/item/", method = RequestMethod.PUT)
    public ResponseEntity<Item> updateItem(@Valid @RequestBody Item updatedItem){
        Item item = itemRepository.getOne(updatedItem.getItemId());
        if (item == null){
            return ResponseEntity.notFound().build();
        }

        item.setItemName(updatedItem.getItemName());
        Item newItem = itemRepository.save(item);
        return ResponseEntity.ok(newItem);
    }

    @RequestMapping(value = "/item/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Item> removeItem(@PathVariable(value = "itemId") String itemId){
        Item item = itemRepository.getOne(itemId);
        if (item == null){
            return ResponseEntity.notFound().build();
        }

        itemRepository.delete(item);
        return ResponseEntity.ok().build();
    }

}
