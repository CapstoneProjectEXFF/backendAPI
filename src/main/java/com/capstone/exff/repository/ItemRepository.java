package com.capstone.exff.repository;

import com.capstone.exff.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    @Query("select u from Item u where u.itemName like concat('%', :itemName, '%')")
    List<Item> findItemsByItemName(String itemName);

}
