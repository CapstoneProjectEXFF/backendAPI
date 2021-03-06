package com.capstone.exff.repositories;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    Page<ItemEntity> findAllByStatusAndPrivacy(String status, String privacy, Pageable pageable);

    @Override
    <S extends ItemEntity> S save(S s);

    @Query("select i " +
            "from ItemEntity i " +
            "where i.privacy = :itemPublic " +
            "   and i.status = :itemStatus " +
            "   and i.name like concat('%', :itemName, '%') " +
            "order by i.modifyTime desc")
    List<ItemEntity> findItemsByItemName(String itemName, String itemStatus, String itemPublic);


    @Query("select i " +
            "from ItemEntity i " +
            "where i.name like concat('%', :itemName, '%') " +
            "   and i.status = :itemStatus " +
            "   and (i.categoryId = :categoryId " +
            "       or i.categoryId in (select r.id " +
            "                           from CategoryEntity r " +
            "                           where r.supercategoryId = :categoryId)) " +
            "   and (i.privacy = :itemPublic " +
            "       or (i.privacy = :itemPrivate " +
            "           and (i.userId in(select r.senderId " +
            "                           from RelationshipEntity r " +
            "                           where r.receiverId = :userId and r.status = :friendStatus) " +
            "               or i.userId in (select r.receiverId " +
            "                               from RelationshipEntity r " +
            "                               where r.senderId = :userId and r.status = :friendStatus)))) " +
            "order by i.modifyTime desc")
    List<ItemEntity> findItemsByItemNameAndCategoryWithPrivacy(String itemName, int categoryId, int userId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus);

    @Query("select i " +
            "from ItemEntity i " +
            "where i.name like concat('%', :itemName, '%') " +
            "   and i.status = :itemStatus " +
            "   and (i.privacy = :itemPublic " +
            "       or (i.privacy = :itemPrivate  " +
            "           and (i.userId in (select r.senderId " +
            "                           from RelationshipEntity r " +
            "                           where r.receiverId = :userId and r.status = :friendStatus) " +
            "               or i.userId in (select r.receiverId " +
            "                               from RelationshipEntity r " +
            "                               where r.senderId = :userId and r.status = :friendStatus)))) " +
            "order by i.modifyTime desc")
    List<ItemEntity> findItemsByItemNameWithPrivacy(String itemName, int userId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus);

    @Query("select i " +
            "from ItemEntity i " +
            "where (i.categoryId = :categoryId " +
            "   or i.categoryId in (select r.id " +
            "                       from CategoryEntity r " +
            "                       where r.supercategoryId = :categoryId)) " +
            "   and i.name like concat('%', :itemName, '%') " +
            "   and i.status = :itemStatus " +
            "   and i.privacy = :itemPublic " +
            "order by i.modifyTime desc")
    List<ItemEntity> findItemsByItemNameandCategoryWithPrivacy(String itemName, int categoryId, String itemStatus, String itemPublic);

    @Query("select i from ItemEntity i where i.id = :itemId")
    ItemEntity getItemById(int itemId);

    @Query("select i from ItemEntity i where i.status = :status and i.id In :ids")
    List<ItemEntity> filterItems(String status, List<Integer> ids);

    List<ItemEntity> findItemEntitiesByUserIdAndStatus(int userId, String status);

    @Query("select i from ItemEntity i where i.status = :status")
    List<ItemEntity> loadItemsByStatus(String status);

    List<ItemEntity> findAllByUserIdAndStatusOrderByCreateTimeDesc(int userId, String status);

    @Query("select i from ItemEntity i where i.userId = :userId and i.id In :ids")
    List<ItemEntity> userOwnedItems(int userId, List<Integer> ids);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.status = :newStatus where i.id in :ids")
    void updateStatusItems(String newStatus, List<Integer> ids);

    @Query("select i " +
            "from ItemEntity i " +
            "where i.status = :itemStatus " +
            "   and i.privacy = :itemPublic " +
            "   or (i.privacy = :itemPrivate and (i.userId in(select r.senderId " +
            "                                               from RelationshipEntity r " +
            "                                               where r.receiverId = :userId " +
            "                                               and r.status = :friendStatus) " +
            "   or i.userId in (select r.receiverId " +
            "               from RelationshipEntity r " +
            "               where r.senderId = :userId " +
            "               and r.status = :friendStatus))) " +
            "order by i.modifyTime desc")
    Page<ItemEntity> getAllItemWithPrivacy(int userId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus, Pageable pageable);

    @Query("select i " +
            "from ItemEntity i " +
            "where i.userId = :userId " +
            "   and i.status = :itemStatus " +
            "   and (i.privacy = :itemPublic " +
            "   or (i.privacy = :itemPrivate  " +
            "       and (i.userId in(select r.senderId " +
            "                       from RelationshipEntity r " +
            "                       where r.receiverId = :targetUserId " +
            "                       and r.status = :friendStatus) " +
            "           or i.userId in (select r.receiverId " +
            "                           from RelationshipEntity r " +
            "                           where r.senderId = :targetUserId " +
            "                           and r.status = :friendStatus)))) " +
            "order by i.modifyTime desc")
    List<ItemEntity> getAllItemByUserIdWithPrivacyOrderByModifyTimeDesc(int userId, int targetUserId, String itemStatus, String itemPublic, String itemPrivate, String friendStatus);

    List<ItemEntity> findByUserIdAndStatusAndPrivacyOrderByCreateTimeDesc(int userId, String status, String privacy);


}
