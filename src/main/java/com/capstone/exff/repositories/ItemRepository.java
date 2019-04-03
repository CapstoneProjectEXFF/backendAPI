package com.capstone.exff.repositories;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    @Override
    <S extends ItemEntity> S save(S s);

    @Query("select i from ItemEntity i where i.name like concat('%', :itemName, '%')")
    List<ItemEntity> findItemsByItemName(String itemName);

    @Query("select i from ItemEntity i where i.name like concat('%', :itemName, '%') and i.status = :itemStatus and (i.categoryId = :categoryId or i.categoryId in (select r.id from CategoryEntity r where r.suppercategoryId = :supercategoryId)) and  (i.privacy = :itemPublic or (i.privacy = :itemPrivate  and (i.userId in(select r.senderId from RelationshipEntity r where r.receiverId = :userId and r.status = :friendStatus)or i.userId in (select r.receiverId from RelationshipEntity r where r.senderId = :userId and r.status = :friendStatus))))")
    List<ItemEntity> findItemsByItemNameAndCategoryWithPrivacy(String itemName, int categoryId, int supercategoryId, int userId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus);

    @Query("select i from ItemEntity i where i.id = :itemId")
    ItemEntity getItemById(int itemId);

    @Query("select i from ItemEntity i where i.status = :status and i.id In :ids")
    List<ItemEntity> filterItems(String status, List<Integer> ids);

    List<ItemEntity> findItemEntitiesByUserId(int userId);

    @Query("select i from ItemEntity i where i.status = :status")
    List<ItemEntity> loadItemsByStatus(String status);

    List<ItemEntity> findAllByUserIdAndStatus(int userId, String status);

    @Query("select i from ItemEntity i where i.userId = :userId and i.id In :ids")
    List<ItemEntity> userOwnedItems(int userId, List<Integer> ids);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.status = :newStatus where i.id in :ids")
    void updateStatusItems(String newStatus, List<Integer> ids);

    @Query("select i from ItemEntity i where i.status = :itemStatus and i.privacy = :itemPublic or " +
            "(i.privacy = :itemPrivate and (" +
            "i.userId in(select r.senderId from RelationshipEntity r where r.receiverId = :userId and r.status = :friendStatus) " +
            "or i.userId in (select r.receiverId from RelationshipEntity r where r.senderId = :userId and r.status = :friendStatus)))")
    List<ItemEntity> getAllItemWithPrivacy(int userId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus);

    @Query("select i from ItemEntity i where i.userId = :userId and i.status = :itemStatus and (i.privacy = :itemPublic or " +
            "(i.privacy = :itemPrivate  and (" +
            "i.userId in(select r.senderId from RelationshipEntity r where r.receiverId = :targetUserId and r.status = :friendStatus) " +
            "or i.userId in (select r.receiverId from RelationshipEntity r where r.senderId = :targetUserId and r.status = :friendStatus))))")
    List<ItemEntity> getAllItemByUserIdWithPrivacy(int userId, int targetUserId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus);


}
