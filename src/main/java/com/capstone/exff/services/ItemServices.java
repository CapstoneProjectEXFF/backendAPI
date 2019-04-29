package com.capstone.exff.services;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface ItemServices {
    ItemEntity createItem(String name, int userId, String description, String preferItems, String address, String privacy, Timestamp createTime, int categoryId);

    ResponseEntity updateItem(int id, String name, int userId, String description, String preferItems, String address, String privacy, Timestamp modifyTime, int categoryId);

    ResponseEntity removeItem(int id, int userId);

    ResponseEntity setItemUnavailable(int id);

    List<ItemEntity> getPublicItemsByUserId(int userId);

    List<ItemEntity> findItemsByItemName(String itemName);

    List<ItemEntity> findItemsByItemNameAndCategoryWithPrivacy(String itemName, int categoryId, int userId);

    List<ItemEntity> findItemsByItemNamePublic(String itemName,  int categoryId);

    List<ItemEntity> findItemsByItemNameWithPrivacy(String itemName,  int userId);

    List<ItemEntity> loadAllItems();

    Page<ItemEntity> loadAllItemsWithPublicPrivacy(int page, int size);

    List<ItemEntity> loadItemsByStatus(String status);

    Page<ItemEntity> getAllItemWithPrivacy(int userId,int page,int size);

    List<ItemEntity> getItemsByUserIdwithPrivacy(int userId, int targetUserId);

    List<ItemEntity> loadItemsByUserIdAndStatus(int userId, String status);

    List<ItemEntity> verifyItems(String status, List<Integer> ids);

    List<ItemEntity> getItemsByUserId(int userId);

    ItemEntity getItemById(int itemId);

    List<ItemEntity> checkUserOwnedItems(int userId, List<Integer> itemIds);

    void changeItemsStatus(String newStatus, List<Integer> itemIds);
}
