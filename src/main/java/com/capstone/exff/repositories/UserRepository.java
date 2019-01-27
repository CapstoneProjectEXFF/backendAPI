package com.capstone.exff.repositories;

import com.capstone.exff.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findFirstByPhoneNumberAndPassword(String phoneNumber, String password);

    @Override
    <S extends UserEntity> S save(S s);

    @Override
    List<UserEntity> findAll();
}
