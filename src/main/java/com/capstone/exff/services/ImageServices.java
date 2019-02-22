package com.capstone.exff.services;

import com.capstone.exff.entities.ImageEntity;

import java.util.List;

public interface ImageServices  {
//    List<ImageEntity> saveImages(String[] url, int itemId);
    ImageEntity saveImages(String url, int itemId);
}
