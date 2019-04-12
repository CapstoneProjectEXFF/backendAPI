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

import static com.capstone.exff.constants.ExffStatus.DONATION_TYPE;

@RestController
public class DonationPostController {

    private final DonationPostServices donationPostServices;
    private final ImageServices imageServices;

    @Autowired
    public DonationPostController(DonationPostServices donationPostServices, ImageServices imageServices) {
        this.donationPostServices = donationPostServices;
        this.imageServices = imageServices;
    }

    @PostMapping("/donationPost")
    @Transactional
    public ResponseEntity createDonationPost(@RequestBody Map<String, Object> body, ServletRequest servletRequest) {
        DonationPostEntity donationPostEntity;
        try {
            String title = (String) body.get("title");
            String content = (String) body.get("content");
            String address = (String) body.get("address");
            int userId = getLoginUserId(servletRequest);
            Timestamp createTime = new Timestamp(System.currentTimeMillis());
            donationPostEntity = donationPostServices.createDonationPost(title, content, address, createTime, userId);

            ArrayList<String> url = (ArrayList<String>) body.get("urls");
            imageServices.saveImages(url, donationPostEntity.getId(), DONATION_TYPE);

        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(donationPostEntity, HttpStatus.OK);
    }

    @PutMapping("/donationPost/{id:[\\d]+}")
    @Transactional
    public ResponseEntity updateDonationPost(@RequestBody Map<String, Object> body, @PathVariable("id") int id, ServletRequest servletRequest) {
        int userId = getLoginUserId(servletRequest);
        String title = (String) body.get("title");
        String content = (String) body.get("content");
        String address = (String) body.get("address");
        Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
        ArrayList<String> newUrls = (ArrayList<String>) body.get("newUrls");
        ArrayList<Integer> removedUrlIds = (ArrayList<Integer>) body.get("removedUrlIds");

        try {
            if (removedUrlIds.size() != 0) {
                if (imageServices.removeImage(removedUrlIds, userId, DONATION_TYPE)) {
                    imageServices.saveImages(newUrls, id, DONATION_TYPE);
                } else {
                    return new ResponseEntity("cannot update donation", HttpStatus.CONFLICT);
                }
            } else {
                imageServices.saveImages(newUrls, id, DONATION_TYPE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donationPostServices.updateDonationPost(id, title, content, address, modifyTime, userId);
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
            e.printStackTrace();
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/donationPost")
    public ResponseEntity getDonationPosts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            List<DonationPostEntity> result = donationPostServices.getDonationPosts(page, size);
            if (result == null) {
                return new ResponseEntity("no donation post found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
    @GetMapping("/donationPost/search")
    public ResponseEntity searchDonationPosts(
            @RequestParam(value = "searchValue", defaultValue = "") String searchValue,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        try {
            List<DonationPostEntity> result = donationPostServices.searchDonationPosts(searchValue,page, size);
            if (result == null) {
                return new ResponseEntity("no donation post found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getdonationPost/{userId:[\\d]+}")
    public ResponseEntity getDonationPostByUserID(@PathVariable("userId") int userId) {
        try {
            List<DonationPostEntity> result = donationPostServices.getDonationPostByUserID(userId);
            if (result == null) {
                return new ResponseEntity("no donation post found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }
}
