package com.capstone.exff.controllers;

import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.ImageServices;
import com.capstone.exff.services.ItemServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ItemController {

    private final ItemServices itemServices;
    private final ImageServices imageServices;

    @Autowired
    public ItemController(ItemServices itemServices, ImageServices imageServices) {
        this.itemServices = itemServices;
        this.imageServices = imageServices;
    }


    @PostMapping("/item")
    @Transactional
    public ResponseEntity createItem(@RequestBody Map<String, Object> body, ServletRequest servletRequest/*, @RequestBody Map<String, String[]> urlArray*/) {
        ItemEntity itemEntity;
        try {
            String name = (String) body.get("name");
            int userId = getLoginUserId(servletRequest);
            String description = (String) body.get("description");

            String address = (String) body.get("address");
            String privacy = (String) body.get("privacy");
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            int categoryId = Integer.parseInt((String) body.get("category"));
            itemEntity = itemServices.createItem(name, userId, description, address, privacy, createTime, categoryId);
            try {
                ArrayList<String> url = (ArrayList<String>) body.get("urls");
                imageServices.saveImages(url, itemEntity.getId(), false);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(itemEntity, HttpStatus.OK);
    }

    @PutMapping("/item/{id:[\\d]+}")
    public ResponseEntity updateItem(@RequestBody Map<String, Object> body, @PathVariable("id") int id, ServletRequest servletRequest) {
        String name = (String) body.get("name");
        int userId = getLoginUserId(servletRequest);
        String description = (String) body.get("description");
        String address = (String) body.get("address");
        String privacy = (String) body.get("privacy");
        Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
        int categoryId = (int) body.get("category");

        return itemServices.updateItem(id, name, userId, description, address, privacy, modifyTime, categoryId);
    }

    @DeleteMapping("item/{id:[\\d]+}")
    public ResponseEntity removeItem(@PathVariable("id") int id, ServletRequest servletRequest) {
        int userId = getLoginUserId(servletRequest);

        return itemServices.removeItem(id, userId);
    }

    @GetMapping("/itemSearch")
    public ResponseEntity findItem(@RequestParam(value = "name") String itemName) {
        try {
            List<ItemEntity> results = itemServices.findItemsByItemName(itemName);
            if (results.isEmpty()) {
                return new ResponseEntity("no item found", HttpStatus.OK);
            } else {
                return new ResponseEntity(results, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }

    @GetMapping("/item")
    public ResponseEntity loadItems() {
        try {
            List<ItemEntity> result = itemServices.loadAllItems();
            if (result == null) {
                return new ResponseEntity("no item found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/item/{id:[\\d]+}")
    public ResponseEntity getItemById(@PathVariable("id") int id) {
        try {
            ItemEntity result = itemServices.getItemById(id);
            if (result == null) {
                return new ResponseEntity("no item found", HttpStatus.OK);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("user/{userId:[\\d]+}/item")
    public ResponseEntity getItemsByUserId(@PathVariable("userId") int userId) {
        try {
            List<ItemEntity> result = itemServices.getItemsByUserId(userId);
            if (result == null) {
                return new ResponseEntity("no item found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
