package com.capstone.exff.services;

import com.capstone.exff.entities.RatingEntity;
import com.capstone.exff.repositories.RatingReppository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingServices{

    private RatingReppository ratingReppository;

    @Autowired
    public RatingServiceImpl(RatingReppository ratingReppository) {
        this.ratingReppository = ratingReppository;
    }

    @Override
    public List<RatingEntity> getRatingByReceiverId(int receiverId) {
        return ratingReppository.findByReceiverId(receiverId);
    }

    @Override
    public RatingEntity createRating(RatingEntity ratingEntity) {
        return ratingReppository.save(ratingEntity);
    }
}
