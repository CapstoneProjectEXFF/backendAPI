package com.capstone.exff.services;

import com.capstone.exff.entities.DonationPostEntity;
import com.capstone.exff.entities.ImageEntity;
import com.capstone.exff.entities.ItemEntity;
import com.capstone.exff.repositories.DonationPostRepository;
import com.capstone.exff.repositories.ImageRepository;
import com.capstone.exff.repositories.ItemRepository;
import com.capstone.exff.utilities.ExffMessage;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.capstone.exff.constants.ExffStatus.DONATION_POST_DISABLE;
import static com.capstone.exff.constants.ExffStatus.ITEM_ENABLE;

@Service
public class ImageServiceImpl implements ImageServices {

    ImageRepository imageRepository;
    ItemRepository itemRepository;
    DonationPostRepository donationPostRepository;

    public ImageServiceImpl(ImageRepository imageRepository, ItemRepository itemRepository, DonationPostRepository donationPostRepository) {
        this.imageRepository = imageRepository;
        this.itemRepository = itemRepository;
        this.donationPostRepository = donationPostRepository;
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
            if (!typeOfPost) {
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

    @Override
    public boolean removeImage(List<Integer> removedImageIds, int userId, boolean typeOfPost) {
        if (!typeOfPost) {
            ItemEntity itemEntity = new ItemEntity();
            for (int i = 0; i < removedImageIds.size(); i++) {
                try {
                    int itemId = imageRepository.getItemIdByImageId(removedImageIds.get(i));
                    itemEntity = itemRepository.findById(itemId).get();
                    if (itemEntity == null) {
                        System.out.println("item null");
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("check item" + e.getMessage());
                    return false;
                }
            }
            if (itemEntity.getUserId() == userId && itemEntity.getStatus().equals(ITEM_ENABLE)) {
                try {
                    for (int i = 0; i < removedImageIds.size(); i++) {
                        imageRepository.deleteById(removedImageIds.get(i));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            } else {
                System.out.println("failed");
                return false;
            }
        } else {
            DonationPostEntity donationPostEntity = new DonationPostEntity();
            for (int i = 0; i < removedImageIds.size(); i++) {
                try {
                    int donationId = imageRepository.getDonationPostIdByImageId(removedImageIds.get(i));
                    donationPostEntity = donationPostRepository.findById(donationId).get();
                    if (donationPostEntity == null) {
                        System.out.println("donation null");
                        return false;
                    }
                } catch (Exception e) {
                    System.out.println("check donation" + e.getMessage());
                    return false;
                }
            }
            if (donationPostEntity.getUserId() == userId && !donationPostEntity.getStatus().equals(DONATION_POST_DISABLE)) {
                try {
                    for (int i = 0; i < removedImageIds.size(); i++) {
                        imageRepository.deleteById(removedImageIds.get(i));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            } else {
                System.out.println("failed");
                return false;
            }
        }
        return true;
    }
}
