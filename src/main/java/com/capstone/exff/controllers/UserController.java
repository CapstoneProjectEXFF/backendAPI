package com.capstone.exff.controllers;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.UserServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private final UserServices userServices;

    @Autowired
    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        String password = body.get("password");
        ResponseEntity responseEntity = userServices.login(phoneNumber, password);
        return responseEntity;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        String password = body.get("password");
        String fullName = body.get("fullName");
        return userServices.register(phoneNumber, password, fullName);
    }

    @GetMapping("/user")
    public ResponseEntity getAllUser(){
        return userServices.getAllUser();
    }

    @RequestMapping(value = "/name")
    public ResponseEntity findUsersByName(@Param("name") String name) {
        try {
            List<UserEntity> results = userServices.findUsersByName(name);
            if (results.isEmpty()) {
                return new ResponseEntity("no user found", HttpStatus.OK);
            } else {
                return new ResponseEntity(results, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/phone")
    public ResponseEntity findUserByPhone(@Param("phone") String phone) {
        try {
            UserEntity result = userServices.findUserByPhone(phone);
            if (result == null) {
                return new ResponseEntity("no user found", HttpStatus.OK);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
