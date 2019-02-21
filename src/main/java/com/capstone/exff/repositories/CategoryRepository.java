package com.capstone.exff.repositories;

import com.capstone.exff.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    @Override
    List<CategoryEntity> findAll();

}
