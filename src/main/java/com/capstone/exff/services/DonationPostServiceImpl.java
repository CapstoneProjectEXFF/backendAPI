package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.repositories.DonationPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.capstone.exff.constants.ExffStatus.DONATION_POST_ENABLE;

@Service
public class DonationPostServiceImpl implements DonationPostServices {
    DonationPostRepository donationPostRepository;

    @Autowired
    public DonationPostServiceImpl(DonationPostRepository donationPostRepository) {
        this.donationPostRepository = donationPostRepository;
    }

    @Override
    public DonationPostEntity createDonationPost(String content, Timestamp createTime, int userId) {
        DonationPostEntity donationPostEntity = new DonationPostEntity();
        donationPostEntity.setContent(content);
        donationPostEntity.setStatus(DONATION_POST_ENABLE);
        donationPostEntity.setCreateTime(createTime);
        donationPostEntity.setUserId(userId);

        donationPostEntity = donationPostRepository.save(donationPostEntity);
        donationPostRepository.flush();
        return donationPostEntity;
    }
}
