package com.capstone.exff.controllers;

import com.capstone.exff.entities.RatingEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.RatingServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

@RestController
public class RatingController {
    private RatingServices ratingServices;

    @Autowired
    public RatingController(RatingServices ratingServices) {
        this.ratingServices = ratingServices;
    }

    @GetMapping("/rating/{userId:[\\d]+}")
    public ResponseEntity getRatingByReceiverId(@PathVariable("userId") int receiverId){
        try {
            List<RatingEntity> res = ratingServices.getRatingByReceiverId(receiverId);
            if (res.isEmpty()){
                return new ResponseEntity(new ExffMessage("Have no rating"), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(res, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage("Can not get"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rating")
    public ResponseEntity createRating(@RequestBody RatingEntity body, ServletRequest servletRequest){
        try {
            int senderId = getLoginUserId(servletRequest);
            if (senderId == 0){
                return new ResponseEntity(new ExffMessage("Can not add"), HttpStatus.BAD_REQUEST);
            }
            body.setSenderId(senderId);
            body.setCreateTime(new Timestamp(System.currentTimeMillis()));
            RatingEntity res = ratingServices.createRating(body);
            if (res != null){
                return new ResponseEntity(res, HttpStatus.OK);
            }
        } catch (Exception e){
            return new ResponseEntity(new ExffMessage("Can not add"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(new ExffMessage("Can not add"), HttpStatus.BAD_REQUEST);
    };

    private int getLoginUserId(ServletRequest servletRequest) {
        int userId = 0;
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
            userId = userEntity.getId();
        } catch (Exception e) {
        }
        return userId;
    }
}
