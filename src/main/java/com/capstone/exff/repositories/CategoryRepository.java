package com.capstone.exff.repositories;

import com.capstone.exff.entities.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer> {
    @Override
    List<CategoryEntity> findAll();

    @Override
    <S extends CategoryEntity> S save(S s);

    @Override
    <S extends CategoryEntity> Iterable<S> saveAll(Iterable<S> iterable);
}
