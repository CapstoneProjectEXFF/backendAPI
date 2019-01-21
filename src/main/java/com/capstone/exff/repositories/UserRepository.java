package com.capstone.exff.repositories;

import com.capstone.exff.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findFirstByPhoneNumberAndPassword(String phoneNumber, String password);
}
