package com.capstone.exff.services;

import com.capstone.exff.entities.RoleEntity;
import org.springframework.http.ResponseEntity;

public interface UserServices {
    ResponseEntity login(String phoneNumber, String password);
    ResponseEntity register(String phoneNumber, String password, String fullname, String status);
    ResponseEntity register(String phoneNumber, String password, String fullname, String status, RoleEntity roleId);
}
