package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.List;

public interface DonationPostServices {
    DonationPostEntity createDonationPost(String title, String content, String address, Timestamp createTime, int userId);
    ResponseEntity updateDonationPost(int id, String title, String content, String address, Timestamp modifyTime, int userId);
    DonationPostEntity getDonationPostById(int id);
    ResponseEntity removeDonationPost(int id, int userId);
    List<DonationPostEntity> getDonationPosts(int page, int size);
    List<DonationPostEntity> searchDonationPosts(String searchValue, int page, int size);
    List<DonationPostEntity> getDonationPostByUserID(int userID);
}
