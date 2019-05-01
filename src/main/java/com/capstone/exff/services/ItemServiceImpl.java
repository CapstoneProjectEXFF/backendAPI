package com.capstone.exff.services;

import com.capstone.exff.constants.ExffStatus;
import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.repositories.ItemRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.capstone.exff.constants.ExffStatus.*;

@Service
public class ItemServiceImpl implements ItemServices {
    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity createItem(String name, int userId, String description, String preferItems, String address, String privacy, Timestamp createTime, int categoryId) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setName(name);
        itemEntity.setUserId(userId);
        itemEntity.setDescription(description);
        itemEntity.setPreferItems(preferItems);
        itemEntity.setAddress(address);
        itemEntity.setPrivacy(privacy);
        itemEntity.setStatus(ITEM_ENABLE);
        itemEntity.setCreateTime(createTime);
        itemEntity.setModifyTime(createTime);
        itemEntity.setCategoryId(categoryId);

        itemEntity = itemRepository.save(itemEntity);
        itemRepository.flush();
        return itemEntity;
    }

    @Override
    public ResponseEntity updateItem(int id, String name, int userId, String description, String preferItems, String address, String privacy, Timestamp modifyTime, int categoryId) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.getStatus().equals(ITEM_ENABLE)) {
            itemEntity.setName(name);
            itemEntity.setDescription(description);
            itemEntity.setPreferItems(preferItems);
            itemEntity.setAddress(address);
            itemEntity.setModifyTime(modifyTime);
            itemEntity.setPrivacy(privacy);
            itemEntity.setCategoryId(categoryId);

            try {
                newItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
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
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getUserId() == userId && itemEntity.getStatus().equals(ITEM_ENABLE)) {
            try {
                itemEntity.setStatus(ITEM_DISABLE);
                removedItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(removedItemEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this item", HttpStatus.FORBIDDEN);
        }
    }

    @Override
    public ResponseEntity setItemUnavailable(int id) {
        ItemEntity itemEntity = itemRepository.getOne(id);
        ItemEntity newItemEntity;
        if (itemEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (itemEntity.getStatus().equals(ITEM_ENABLE)) {
            itemEntity.setStatus(ITEM_TRADED);
            try {
                newItemEntity = itemRepository.save(itemEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(newItemEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this item", HttpStatus.OK);
        }
    }


    @Override
    public List<ItemEntity> findItemsByItemName(String itemName, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return itemRepository.findItemsByItemName(itemName, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, pageable);
    }


    @Override
    public List<ItemEntity> findItemsByItemNameAndCategoryWithPrivacy(String itemName, int categoryId, int userId) {
        return itemRepository.findItemsByItemNameAndCategoryWithPrivacy(itemName, categoryId, userId, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, ITEM_PRIVACY_FRIENDS, RELATIONSHIP_ACCEPTED);
    }

    @Override
    public List<ItemEntity> findItemsByItemNamePublic(String itemName, int categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        return itemRepository.findItemsByItemNameandCategoryWithPrivacy(itemName, categoryId, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, pageable);
    }


    @Override
    public List<ItemEntity> findItemsByItemNameWithPrivacy(String itemName, int userId) {
        return itemRepository.findItemsByItemNameWithPrivacy(itemName, userId, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, ITEM_PRIVACY_FRIENDS, RELATIONSHIP_ACCEPTED);
    }

    @Override
    public List<ItemEntity> loadAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Page<ItemEntity> loadAllItemsWithPublicPrivacy(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.findAllByStatusAndPrivacyOrderByModifyTimeDesc(ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, pageable);
    }

    @Override
    public List<ItemEntity> verifyItems(String status, List<Integer> ids) {
        return itemRepository.filterItems(status, ids);
    }

    @Override
    public List<ItemEntity> getItemsByUserId(int userId) {
        return itemRepository.findItemEntitiesByUserIdAndStatus(userId, ITEM_ENABLE);
    }

    @Override
    public ItemEntity getItemById(int itemId) {
        return itemRepository.getItemById(itemId);
    }

    @Override
    public List<ItemEntity> loadItemsByStatus(String status) {
        return itemRepository.loadItemsByStatus(status);
    }

    @Override
    public Page<ItemEntity> getAllItemWithPrivacy(int userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return itemRepository.getAllItemWithPrivacy(userId, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, ITEM_PRIVACY_FRIENDS, RELATIONSHIP_ACCEPTED, pageable);
    }

    @Override
    public List<ItemEntity> getItemsByUserIdwithPrivacy(int userId, int loginUserId) {
        return itemRepository.getAllItemByUserIdWithPrivacyOrderByModifyTimeDesc(userId, loginUserId, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC, ITEM_PRIVACY_FRIENDS, RELATIONSHIP_ACCEPTED);
    }

    @Override
    public List<ItemEntity> getPublicItemsByUserId(int userId) {
        return itemRepository.findByUserIdAndStatusAndPrivacyOrderByCreateTimeDesc(userId, ITEM_ENABLE, ITEM_PRIVACY_PUBLIC);
    }


    @Override
    public List<ItemEntity> loadItemsByUserIdAndStatus(int userId, String status) {
        return itemRepository.findAllByUserIdAndStatusOrderByCreateTimeDesc(userId, status);
    }


    public List<ItemEntity> checkUserOwnedItems(int userId, List<Integer> itemIds) {
        return itemRepository.userOwnedItems(userId, itemIds);
    }


    @Override
    public void changeItemsStatus(String newStatus, List<Integer> itemIds) {
        itemRepository.updateStatusItems(newStatus, itemIds);
    }
}
