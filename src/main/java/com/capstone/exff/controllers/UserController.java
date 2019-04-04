package com.capstone.exff.controllers;

import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.UserServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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

    @PostMapping("/user/updateInfo")
    public ResponseEntity updateUserInfo(@RequestBody Map<String, String> body, ServletRequest servletRequest) {
        String phoneNumber = getPhoneNumber(servletRequest);
        String fullName = body.get("fullName");
        String avatar = body.get("avatar");
        return userServices.updateUserInfo(phoneNumber, fullName, avatar);

    }

    @PostMapping("/user/changePassword")
    public ResponseEntity changePassword(@RequestBody Map<String, String> body, ServletRequest servletRequest) {
        String phoneNumber = getPhoneNumber(servletRequest);
        String oldPassword = (String) body.get("oldPassword");
        String newPassword = (String) body.get("newPassword");
        return userServices.changePassword(phoneNumber, oldPassword, newPassword);

    }

    @GetMapping("/user")
    public ResponseEntity getUsers() {
        return userServices.getAllUser();
    }

    @GetMapping("/user/{id:[\\d]+}")
    public ResponseEntity getUserById(@PathVariable("id") int id, ServletRequest servletRequest) {
        UserEntity userEntity = null;
        try {
            int userId = getUserId(servletRequest);
            if (userId != -1) {
                id = userId;
            }
            userEntity = userServices.getUserById(id);
        } catch (Exception e) {
        }
        if (userEntity == null) {
            return new ResponseEntity(new ExffMessage("Get fails"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(userEntity, HttpStatus.OK);
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

    @GetMapping(value = "/phone")
    public ResponseEntity findUserByPhone(@RequestParam("phone") String phone) {
        try {
            UserEntity result = userServices.findUserByPhone(phone);
            if (result == null) {
                return new ResponseEntity("no user found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    private String getPhoneNumber(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        String phoneNumber = userEntity.getPhoneNumber();
        return phoneNumber;
    }

    private int getUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getAttribute("USER_INFO") != null) {
            UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
            int id = userEntity.getId();
            return id;
        }
        return -1;
    }
}
