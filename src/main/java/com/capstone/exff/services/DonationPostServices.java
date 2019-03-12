package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface DonationPostServices {
    DonationPostEntity createDonationPost(String content, String address, Timestamp createTime, int userId);
    ResponseEntity updateDonationPost(int id, String content, String address, Timestamp modifyTime, int userId);
    ResponseEntity removeDonationPost(int id, int userId);
    DonationPostEntity getDonationPostById(int donationPostId);
    List<DonationPostEntity> getDonationPostByUserID(int userID);
}
