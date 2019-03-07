package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;

import java.sql.Timestamp;

public interface DonationPostServices {
    DonationPostEntity createDonationPost(String content, Timestamp createTime, int userId);
}
