package com.capstone.exff.controllers;

import com.capstone.exff.entities.UserCategoryEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.UserCategoryServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class UserCategoryController {
    private final UserCategoryServices userCategoryServices;

    @Autowired
    public UserCategoryController(UserCategoryServices userCategoryServices) {
        this.userCategoryServices = userCategoryServices;
    }

    @PostMapping("/usercategory")
    public ResponseEntity createUserCategory(@RequestBody Map<String, int[]> body, ServletRequest servletRequest) {
        try {
            int userId = getUserId(servletRequest);
            int[] tmpCategoryIds = body.get("categoryIds");
            List<Integer> categoryIds = Arrays.stream(tmpCategoryIds).boxed().collect(Collectors.toList());
            List<UserCategoryEntity> userCategoryEntities = userCategoryServices.createUserCategory(userId, categoryIds);
            if (userCategoryEntities != null){
                return new ResponseEntity<>(userCategoryEntities, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ExffMessage("Fails"), HttpStatus.BAD_REQUEST);
    }
    @PutMapping("/usercategory")
    public ResponseEntity updateUserCategory(@RequestBody Map<String, int[]> body, ServletRequest servletRequest) {
        try {
            int userId = getUserId(servletRequest);
            int[] tmpCategoryIds = body.get("categoryIds");
            int[] tmpRemoveIds = body.get("removeIds");
            List<Integer> categoryIds = Arrays.stream(tmpCategoryIds).boxed().collect(Collectors.toList());
            List<Integer> removeIds = Arrays.stream(tmpRemoveIds).boxed().collect(Collectors.toList());
            List<UserCategoryEntity> userCategoryEntities = userCategoryServices.updateUserCategory(userId, categoryIds, removeIds);
            if (userCategoryEntities != null){
                return new ResponseEntity<>(userCategoryEntities, HttpStatus.OK);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ExffMessage("Cannot update"), HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/usercategory")
    public ResponseEntity updateUserCategory(ServletRequest servletRequest) {
        try {
            int userId = getUserId(servletRequest);
            List<UserCategoryEntity> userCategoryEntities = userCategoryServices.getUserCategory(userId);
            if (userCategoryEntities != null){
                return new ResponseEntity<>(userCategoryEntities, HttpStatus.OK);
            }
        } catch (Exception ex) {
        }
        return new ResponseEntity<>(new ExffMessage("Cannot get"), HttpStatus.BAD_REQUEST);
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
