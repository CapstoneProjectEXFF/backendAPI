package com.capstone.exff.controllers;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.DonationPostServices;
import com.capstone.exff.services.ImageServices;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class DonationPostController {

    private final DonationPostServices donationPostServices;
    private final ImageServices imageServices;

    @Autowired
    public DonationPostController(DonationPostServices donationPostServices, ImageServices imageServices) {
        this.donationPostServices = donationPostServices;
        this.imageServices = imageServices;
    }

    @GetMapping("/donationPost/{id:[\\d]+}")
    public ResponseEntity getDonationPostById(@PathVariable("id") int id) {
        DonationPostEntity donationPostEntity;
        try {
            donationPostEntity = donationPostServices.getDonationPostById(id);
        } catch (Exception e){
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(donationPostEntity, HttpStatus.OK);
    }

    @PostMapping("/donationPost")
    @Transactional
    public ResponseEntity createDonationPost(@RequestBody Map<String, Object> body, ServletRequest servletRequest) {
        DonationPostEntity donationPostEntity;
        try {
            String content = (String) body.get("content");
            String address = (String) body.get("address");
            int userId = getLoginUserId(servletRequest);
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            donationPostEntity = donationPostServices.createDonationPost(content, address, createTime, userId);

            ArrayList<String> url = (ArrayList<String>) body.get("urls");
            imageServices.saveImages(url, donationPostEntity.getId(), false);

        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(donationPostEntity, HttpStatus.OK);
    }

    @PutMapping("/donationPost/{id:[\\d]+}")
    public ResponseEntity updateDonationPost(@RequestBody Map<String, Object> body, @PathVariable("id") int id, ServletRequest servletRequest) {
        int userId = getLoginUserId(servletRequest);
        String content = (String) body.get("content");
        String address = (String) body.get("address");
        Timestamp modifyTime = new Timestamp(System.currentTimeMillis());

        return donationPostServices.updateDonationPost(id, content, address, modifyTime, userId);
    }

    @DeleteMapping("/donationPost/{id:[\\d]+}")
    public ResponseEntity removeDonationPost(@PathVariable("id") int id, ServletRequest servletRequest) {
        int userId = getLoginUserId(servletRequest);
        return donationPostServices.removeDonationPost(id, userId);
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }

    @GetMapping("/donationPost/{id:[\\d]+}")
    public ResponseEntity getDonationPostById(@PathVariable("id") int id) {
        try {
            DonationPostEntity result = donationPostServices.getDonationPostById(id);
            if (result == null) {
                return new ResponseEntity("no donation post found", HttpStatus.OK);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/user/{userId:[\\d]+}/donationPost")
    public ResponseEntity getDonationPostByUserID(@PathVariable("userId") int userId) {
        try {
            List<DonationPostEntity> result = donationPostServices.getDonationPostByUserID(userId);
            if (result == null) {
                return new ResponseEntity("no donation post found", HttpStatus.OK);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
