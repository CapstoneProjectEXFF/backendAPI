package com.capstone.exff.services;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ItemServices {
    ItemEntity createItem(String name, int userId, String description, String address, String privacy, Timestamp createTime, int categoryId);
    ResponseEntity updateItem(int id, String name, int userId, String description, String address, String privacy, Timestamp modifyTime, int categoryId);
    ResponseEntity removeItem(int id, int userId);
    ResponseEntity setItemUnavailable(int id);


    List<ItemEntity> findItemsByItemName(String itemName);
    List<ItemEntity> loadAllItems();
    List<ItemEntity> loadItemsByStatus(String status);
    List<ItemEntity> verifyItems(String status, List<Integer> ids);
    List<ItemEntity> getItemsByUserId(int userId);
    ItemEntity getItemById(int itemId);
}
