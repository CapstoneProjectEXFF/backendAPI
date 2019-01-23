package com.capstone.exff.repositories;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<ItemEntity, Integer> {

    @Override
    <S extends ItemEntity> S save(S s);
}
