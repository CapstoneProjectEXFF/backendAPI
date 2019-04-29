package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface DonationPostServices {
    DonationPostEntity createDonationPost(DonationPostEntity donationPostEntity);
    ResponseEntity updateDonationPost(int id, DonationPostEntity donationPostEntity, Timestamp modifyTime, int userId);
    DonationPostEntity getDonationPostById(int id);
    ResponseEntity removeDonationPost(int id, int userId);
    List<DonationPostEntity> getDonationPosts(int page, int size);
    List<DonationPostEntity> searchDonationPosts(String searchValue, int page, int size);
    List<DonationPostEntity> getDonationPostByUserID(int userID);
    int countOwnerDonationPost(int userId);
}
