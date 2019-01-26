package com.capstone.exff.services;

import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface UserServices {
    UserEntity login(String phoneNumber, String password);
    UserEntity register(String phoneNumber, String password, String fullname, String status);
    UserEntity register(String phoneNumber, String password, String fullname, String status, RoleEntity roleId);

    List<UserEntity> findUsersByName(String name);
    UserEntity findUserByPhone(String phone);
}
