package com.capstone.exff.controllers;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.entities.DonationPostTargetEntity;
import com.capstone.exff.entities.UserEntity;
import com.capstone.exff.services.DonationPostServices;
import com.capstone.exff.services.DonationPostTargetServices;
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

import static com.capstone.exff.constants.ExffStatus.DONATION_TYPE;

@RestController
public class DonationPostController {

    private final DonationPostServices donationPostServices;
    private final ImageServices imageServices;
    private final DonationPostTargetServices donationPostTargetServices;

    @Autowired
    public DonationPostController(DonationPostServices donationPostServices, ImageServices imageServices, DonationPostTargetServices donationPostTargetServices) {
        this.donationPostServices = donationPostServices;
        this.imageServices = imageServices;
        this.donationPostTargetServices = donationPostTargetServices;
    }

    @PostMapping("/donationPost")
    @Transactional
    public ResponseEntity createDonationPost(@RequestBody DonationPostWrapper body, ServletRequest servletRequest) {
        DonationPostEntity donationPostEntity;
        try {
            donationPostEntity = body.getDonationPost();
            int userId = getLoginUserId(servletRequest);
            donationPostEntity.setUserId(userId);
            donationPostEntity = donationPostServices.createDonationPost(donationPostEntity);

            ArrayList<String> url = body.getUrls();
            imageServices.saveImages(url, donationPostEntity.getId(), DONATION_TYPE);


            ArrayList<DonationPostTargetEntity> targetEntities = body.getTargets();
            for (DonationPostTargetEntity target :
                    targetEntities) {
                target.setDonationPostId(donationPostEntity.getId());
            }
            donationPostTargetServices.createDonationTargets(targetEntities);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
        return new ResponseEntity(donationPostEntity, HttpStatus.OK);
    }

    @PutMapping("/donationPost/{id:[\\d]+}")
    @Transactional
    public ResponseEntity updateDonationPost(@RequestBody DonationPostWrapper body, @PathVariable("id") int id, ServletRequest servletRequest) {
        DonationPostEntity donationPostEntity = body.getDonationPost();
        int userId = getLoginUserId(servletRequest);
        donationPostEntity.setUserId(userId);
        Timestamp modifyTime = new Timestamp(System.currentTimeMillis());

        try {
            ArrayList<String> newUrls = body.getNewUrls();
            ArrayList<Integer> removedUrlIds = body.getRemovedUrlIds();
            ArrayList<DonationPostTargetEntity> updateTarget = body.getTargets();
            ArrayList<Integer> removeTarget = body.getRemoveTargets();
            if (removedUrlIds.size() != 0) {
                if (imageServices.removeImage(removedUrlIds, userId, DONATION_TYPE)) {
                    imageServices.saveImages(newUrls, id, DONATION_TYPE);
                } else {
                    return new ResponseEntity("cannot update donation", HttpStatus.CONFLICT);
                }
            } else {
                imageServices.saveImages(newUrls, id, DONATION_TYPE);
            }
            donationPostTargetServices.updateDonationTargets(updateTarget,removeTarget, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return donationPostServices.updateDonationPost(id, donationPostEntity, modifyTime, userId);
    }

    @DeleteMapping("/donationPost/{id:[\\d]+}")
    public ResponseEntity removeDonationPost(@PathVariable("id") int id, ServletRequest servletRequest) {
        int userId = getLoginUserId(servletRequest);
        return donationPostServices.removeDonationPost(id, userId);
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
            List<DonationPostEntity> result = donationPostServices.searchDonationPosts(searchValue, page, size);
            if (result == null) {
                return new ResponseEntity("no donation post found", HttpStatus.BAD_REQUEST);
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
                return new ResponseEntity("no donation post found", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(result, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
        }
    }

    private int getLoginUserId(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserEntity userEntity = (UserEntity) request.getAttribute("USER_INFO");
        int userId = userEntity.getId();
        return userId;
    }

}
