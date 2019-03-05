package com.capstone.exff.services;

import com.capstone.exff.entities.ImageEntity;

import java.util.List;

public interface ImageServices  {
//    List<ImageEntity> saveImage(String[] url, int itemId);
    ImageEntity saveImage(String url, int itemId);
    Iterable<ImageEntity> saveImages(List<String> urls, int itemId);
    List<ImageEntity> getImagesByItemId(int itemId);
}
