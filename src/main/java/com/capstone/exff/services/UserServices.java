package com.capstone.exff.services;

import com.capstone.exff.entities.RoleEntity;
import com.capstone.exff.entities.UserEntity;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface UserServices {
    ResponseEntity login(String phoneNumber, String password);

    ResponseEntity register(String phoneNumber, String password, String fullname);

    ResponseEntity register(String phoneNumber, String password, String fullname, RoleEntity roleId);

    ResponseEntity updateUserInfo(String phoneNumber, String fullName, String avatar);

    ResponseEntity changePassword(String phoneNumber, String oldPassword, String newPassword);

    ResponseEntity getAllUser();

    UserEntity getUserById(int id);

    List<UserEntity> findUsersByName(String name);

    UserEntity findUserByPhone(String phone);

    List<UserEntity> findUsersbyPhoneNumberList(ArrayList<String> phoneNumberList);

}
