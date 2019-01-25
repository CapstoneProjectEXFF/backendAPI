package com.capstone.exff.controllers;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.UserServices;
import com.capstone.exff.utilities.ExffError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
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
        return userServices.login(phoneNumber, password);

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody Map<String, String> body) {
        String phoneNumber = body.get("phoneNumber");
        String password = body.get("password");
        String fullName = body.get("fullName");
        String status = body.get("status");
        return userServices.register(phoneNumber, password, fullName, status);
    }
}
