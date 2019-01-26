package com.capstone.exff.repositories;

import com.capstone.exff.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findFirstByPhoneNumberAndPassword(String phoneNumber, String password);

    @Query("select u from UserEntity u where u.fullName like concat('%', :name, '%')")
    List<UserEntity> findUserEntitiesByFullName(String name);

    @Query("select u from UserEntity u where u.phoneNumber = :phone")
    UserEntity findUserEntitiesByPhoneNumber(String phone);

    @Override
    <S extends UserEntity> S save(S s);
}

