package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;

public interface DonationPostServices {
    DonationPostEntity getDonationPostById(int id);
    DonationPostEntity createDonationPost(String content, Timestamp createTime, int userId);
    ResponseEntity updateDonationPost(int id, String content, Timestamp modifyTime, int userId);
    ResponseEntity removeDonationPost(int id, int userId);
}
