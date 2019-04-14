package com.capstone.exff.services;

import com.capstone.exff.entities.RatingEntity;

import java.util.List;

public interface RatingServices {
    List<RatingEntity> getRatingByReceiverId(int receiverId);
    RatingEntity createRating(RatingEntity ratingEntity);
}
