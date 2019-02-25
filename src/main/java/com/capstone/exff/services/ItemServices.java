package com.capstone.exff.services;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ItemServices {
    ItemEntity createItem(String name, int userId, String description, String address, boolean tmpPrivacy, Timestamp createTime, int categoryId);
    ResponseEntity updateItem(int id, String name, int userId, String description, String address, boolean tmpPrivacy, Timestamp modifyTime, int categoryId);
    ResponseEntity removeItem(int id, int userId);


    List<ItemEntity> findItemsByItemName(String itemName);
    List<ItemEntity> loadAllItems();
    ItemEntity getItemById(int itemId);
}
