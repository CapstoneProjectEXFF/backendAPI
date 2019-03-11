package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.repositories.DonationPostRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

import static com.capstone.exff.constants.ExffStatus.DONATION_POST_DISABLE;
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

    @Override
    public ResponseEntity updateDonationPost(int id, String content, Timestamp modifyTime, int userId) {
        DonationPostEntity donationPostEntity = donationPostRepository.getOne(id);
        DonationPostEntity newDonationPostEntity;
        if (donationPostEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (donationPostEntity.getUserId() == userId && donationPostEntity.getStatus().equals(DONATION_POST_ENABLE)) {
            donationPostEntity.setContent(content);
            donationPostEntity.setModifyTime(modifyTime);
            try {
                newDonationPostEntity = donationPostRepository.save(donationPostEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(newDonationPostEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this donation post", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity removeDonationPost(int id, int userId) {
        DonationPostEntity donationPostEntity = donationPostRepository.getOne(id);
        DonationPostEntity newDonationPostEntity;
        if (donationPostEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (donationPostEntity.getUserId() == userId && donationPostEntity.getStatus().equals(DONATION_POST_ENABLE)) {
            try {
                donationPostEntity.setStatus(DONATION_POST_DISABLE);
                newDonationPostEntity = donationPostRepository.save(donationPostEntity);
            } catch (Exception e) {
                return new ResponseEntity(new ExffMessage(e.getMessage()), HttpStatus.CONFLICT);
            }
            return new ResponseEntity(newDonationPostEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity("Cannot access this donation post", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public DonationPostEntity getDonationPostById(int donationPostId) {
        return donationPostRepository.getDonationPostById(donationPostId);
    }
}
