package com.capstone.exff.controllers;

import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.services.ItemServices;
import com.capstone.exff.utilities.ExffError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemServices itemServices;

    @Autowired
    public ItemController(ItemServices itemServices) {
        this.itemServices = itemServices;
    }


    @PostMapping("create")
    public ResponseEntity createItem(@RequestBody Map<String, String> body){
        String name = body.get("name");
        int userId;
        String description = body.get("description");
        ItemEntity itemEntity;

        try{
            userId = Integer.parseInt(body.get("userId"));
            itemEntity = itemServices.createItem(name, userId, description);
        } catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(itemEntity, HttpStatus.OK);
    }

    @PutMapping("update")
    public ResponseEntity updateItem(@RequestBody Map<String, String> body){
        int id;
        String name = body.get("name");
        String description = body.get("description");

        try{
            id = Integer.parseInt(body.get("id"));
        }catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return itemServices.updateItem(id, name, description);
    }

    @DeleteMapping("remove")
    public ResponseEntity removeItem(@RequestBody Map<String, String> body){
        int id;

        try{
            id = Integer.parseInt(body.get("id"));
        }catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return itemServices.removeItem(id);
    }
}
