package com.capstone.exff.services;

import com.capstone.exff.entities.ImageEntity;
import com.capstone.exff.repositories.ImageRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageServiceImpl implements ImageServices {

    ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    //    @Override
//    public List<ImageEntity> saveImage(String[] url, int itemId) {
//        List<ImageEntity> listImage = new ArrayList<>();
//
//        for (int i = 0; i < url.length; i++) {
//            ImageEntity imageEntity = new ImageEntity();
//            imageEntity.setItemId(itemId);
//            //imageEntity.setItemId(i);
//            imageEntity.setUrl(url[i]);
//            listImage.add(imageEntity);
//        }
//        return (List<ImageEntity>) imageRepository.saveAll(listImage);
//    }
    @Override
    public ImageEntity saveImage(String url, int itemId) {
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setItemId(itemId);
        imageEntity.setUrl(url);

        return imageRepository.save(imageEntity);
    }

    @Override
    public Iterable<ImageEntity> saveImages(List<String> urls, int idOfType, boolean typeOfPost) {
        List<ImageEntity> imageEntities = new ArrayList<>();
        for (String url :
                urls) {
            ImageEntity tmpImage = new ImageEntity();
            if (typeOfPost) {
                tmpImage.setItemId(idOfType);
            } else {
                tmpImage.setDonationPostId(idOfType);
            }
            tmpImage.setUrl(url);
            imageEntities.add(tmpImage);
        }
        return imageRepository.saveAll(imageEntities);
    }

    @Override
    public List<ImageEntity> getImagesByItemId(int itemId) {
        return imageRepository.getImagesByIdItem(itemId);
    }
}
