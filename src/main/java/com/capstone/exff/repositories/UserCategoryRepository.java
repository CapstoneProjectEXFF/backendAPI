package com.capstone.exff.repositories;

import com.capstone.exff.entities.UserCategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserCategoryRepository extends CrudRepository<UserCategoryEntity, Integer> {
    List<UserCategoryEntity> findAllByUserId(int userId);

    @Override
    <S extends UserCategoryEntity> S save(S s);

    @Override
    <S extends UserCategoryEntity> List<S> saveAll(Iterable<S> iterable);

    @Override
    void deleteById(Integer integer);
}
