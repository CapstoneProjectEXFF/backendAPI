package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.repositories.DonationPostRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

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
    public DonationPostEntity getDonationPostById(int id) {
        return donationPostRepository.findById(id).get();
    }

    @Override
    public List<DonationPostEntity> getDonationPostByUserID(int userID) {
        return donationPostRepository.findByUserIdOrderByCreateTimeDesc(userID);
    }

    @Override
    public DonationPostEntity createDonationPost(String content, String address, Timestamp createTime, int userId) {
        DonationPostEntity donationPostEntity = new DonationPostEntity();
        donationPostEntity.setContent(content);
        donationPostEntity.setAddress(address);
        donationPostEntity.setStatus(DONATION_POST_ENABLE);
        donationPostEntity.setCreateTime(createTime);
        donationPostEntity.setUserId(userId);

        donationPostEntity = donationPostRepository.save(donationPostEntity);
        donationPostRepository.flush();
        return donationPostEntity;
    }

    @Override
    public ResponseEntity updateDonationPost(int id, String content, String address, Timestamp modifyTime, int userId) {
        DonationPostEntity donationPostEntity = donationPostRepository.findById(id).get();
        DonationPostEntity newDonationPostEntity;
        if (donationPostEntity == null) {
            return ResponseEntity.notFound().build();
        }
        if (donationPostEntity.getUserId() == userId && !donationPostEntity.getStatus().equals(DONATION_POST_DISABLE)) {
            donationPostEntity.setContent(content);
            donationPostEntity.setAddress(address);
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
    public List<DonationPostEntity> getDonationPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return donationPostRepository.findAll(pageable).getContent();
    }
}
