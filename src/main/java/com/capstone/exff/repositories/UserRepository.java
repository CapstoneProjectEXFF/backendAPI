package com.capstone.exff.repositories;

import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findFirstByPhoneNumber(String phoneNumber);

    @Query("select u from UserEntity u where u.fullName like concat('%', :name, '%')")
    List<UserEntity> findUserEntitiesByFullName(String name);

    @Query("select u from UserEntity u where u.phoneNumber = :phone")
    UserEntity findUserEntitiesByPhoneNumber(String phone);

    UserEntity findTop1ByPhoneNumber(String phoneNumber);

    @Override
    Optional<UserEntity> findById(Integer integer);

    @Override
    <S extends UserEntity> S save(S s);

    @Override
    List<UserEntity> findAll();

    List<UserEntity> findAllByRoleByRoleIdAndStatus(RoleEntity roleEntity, String status);

    List<UserEntity> findByPhoneNumberInAndStatus(ArrayList<String> phoneNumnerList, String status);
}

