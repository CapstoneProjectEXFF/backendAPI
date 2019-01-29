package com.capstone.exff.controllers;

import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.ItemServices;
import com.capstone.exff.utilities.ExffError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemServices itemServices;

    @Autowired
    public ItemController(ItemServices itemServices) {
        this.itemServices = itemServices;
    }


    @PostMapping
    public ResponseEntity createItem(@RequestBody Map<String, String> body, ServletRequest servletRequest){
        String name = body.get("name");
        int userId = getLoginUserId(servletRequest);
        String description = body.get("description");
        ItemEntity itemEntity;

        try{
            itemEntity = itemServices.createItem(name, userId, description);
        } catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(itemEntity, HttpStatus.OK);
    }

    @PutMapping("/{id:[\\d]+}")
    public ResponseEntity updateItem(@RequestBody Map<String, String> body, @PathVariable("id") int id, ServletRequest servletRequest){
        int userId = getLoginUserId(servletRequest);
        String name = body.get("name");
        String description = body.get("description");

//        try{
//            id = Integer.parseInt(body.get("id"));
//        }catch (Exception e){
//            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
//        }
        return itemServices.updateItem(id, name, description, userId);
    }

    @DeleteMapping
    public ResponseEntity removeItem(@RequestBody Map<String, String> body, ServletRequest servletRequest){
        int id;
        int userId = getLoginUserId(servletRequest);
        try{
            id = Integer.parseInt(body.get("id"));
        }catch (Exception e){
            return new ResponseEntity(new ExffError(e.getMessage()), HttpStatus.CONFLICT);
        }
        return itemServices.removeItem(id, userId);
    }

    private int getLoginUserId(ServletRequest servletRequest){
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }
}
