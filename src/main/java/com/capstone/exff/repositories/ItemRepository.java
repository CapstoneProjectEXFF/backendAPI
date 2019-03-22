package com.capstone.exff.repositories;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    @Override
    <S extends ItemEntity> S save(S s);

    @Query("select i from ItemEntity i where i.name like concat('%', :itemName, '%')")
    List<ItemEntity> findItemsByItemName(String itemName);

    @Query("select i from ItemEntity i where i.id = :itemId")
    ItemEntity getItemById(int itemId);

    @Query("select i from ItemEntity i where i.status = :status and i.id In :ids")
    List<ItemEntity> filterItems(String status, List<Integer> ids);

    List<ItemEntity> findItemEntitiesByUserId(int userId);

    @Query("select i from ItemEntity i where i.status = :status")
    List<ItemEntity> loadItemsByStatus(String status);

    @Query("select i from ItemEntity i where i.userId = :userId and i.id In :ids")
    List<ItemEntity> userOwnedItems(int userId, List<Integer> ids);

    @Transactional
    @Modifying
    @Query("update ItemEntity i set i.status = :newStatus where i.id in :ids")
    void updateStatusItems(String newStatus, List<Integer> ids);

}
