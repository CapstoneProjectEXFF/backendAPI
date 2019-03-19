package com.capstone.exff.services;

import com.capstone.exff.entities.ImageEntity;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ImageServices  {
//    List<ImageEntity> saveImage(String[] url, int itemId);
    ImageEntity saveImage(String url, int itemId);
    Iterable<ImageEntity> saveImages(List<String> urls, int idOfType, String typeOfPost);
    List<ImageEntity> getImagesByItemId(int itemId);
    boolean removeImage(List<Integer> removedImageIds, int userId, String typeOfPost);
}
