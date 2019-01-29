package com.capstone.exff.controller;

import com.capstone.exff.entities.Item;
import com.capstone.exff.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @RequestMapping(value = "/item/{itemName}", method = RequestMethod.GET)
    public ResponseEntity findItem(@PathVariable(value = "itemName") String itemName) {
       try {
          List<Item> results = itemRepository.findItemsByItemName(itemName);
          if (results.isEmpty()) {
              return new ResponseEntity("no item found", HttpStatus.OK);
          } else {
              return new ResponseEntity(results, HttpStatus.OK);
          }
       } catch (Exception e) {
           return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
       }
    }

}
