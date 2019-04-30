package com.capstone.exff.services;

import com.capstone.exff.entities.UserCategoryEntity;
import com.capstone.exff.repositories.UserCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCategoryServiceImpl implements UserCategoryServices {

    private final UserCategoryRepository userCategoryRepository;

    @Autowired
    public UserCategoryServiceImpl(UserCategoryRepository userCategoryRepository) {
        this.userCategoryRepository = userCategoryRepository;
    }

    @Override
    public List<UserCategoryEntity> createUserCategory(int userId, List<Integer> categoryIds) {
        try {
            List<UserCategoryEntity> userCategoryEntities = userCategoryRepository.findAllByUserId(userId);
            if (userCategoryEntities.size() == 0) {
                userCategoryEntities = new ArrayList<>();
                for (int i = 0; i < categoryIds.size(); i++) {
                    UserCategoryEntity userCategoryEntity = new UserCategoryEntity();
                    userCategoryEntity.setUserId(userId);
                    userCategoryEntity.setCategoryId(categoryIds.get(i));
                    userCategoryEntities.add(userCategoryEntity);
                }
                userCategoryEntities = userCategoryRepository.saveAll(userCategoryEntities);
                return userCategoryEntities;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<UserCategoryEntity> updateUserCategory(int userId, List<Integer> categoryIds, List<Integer> removeIds) {
        try {
            List<UserCategoryEntity> userCategoryEntities = userCategoryRepository.findAllByUserId(userId);
            if (userCategoryEntities.size() == 0) {
                userCategoryEntities = new ArrayList<>();
                for (int i = 0; i < categoryIds.size(); i++) {
                    UserCategoryEntity userCategoryEntity = new UserCategoryEntity();
                    userCategoryEntity.setUserId(userId);
                    userCategoryEntity.setCategoryId(categoryIds.get(i));
                    userCategoryEntities.add(userCategoryEntity);
                }
                userCategoryEntities = userCategoryRepository.saveAll(userCategoryEntities);
                return userCategoryEntities;
            } else {
                List<UserCategoryEntity> addUserCategoryEntities = new ArrayList<>();
                for (int i = 0; i < removeIds.size(); i++) {
                    int id = removeIds.get(i);
                    boolean isPresent = userCategoryEntities.stream().filter(userCate -> userCate.getId() == id).findAny().isPresent();
                    if (isPresent) {
                        userCategoryRepository.deleteById(removeIds.get(i));
                    }
                }
                for (int i = 0; i < categoryIds.size(); i++) {
                    int id = categoryIds.get(i);
                    boolean isPresent = userCategoryEntities.stream().filter(userCate -> userCate.getCategoryId() == id).findAny().isPresent();
                    if (!isPresent) {
                        UserCategoryEntity userCategoryEntity = new UserCategoryEntity();
                        userCategoryEntity.setUserId(userId);
                        userCategoryEntity.setCategoryId(categoryIds.get(i));
                        addUserCategoryEntities.add(userCategoryEntity);
                    }
                }
                userCategoryRepository.saveAll(addUserCategoryEntities);
                return userCategoryRepository.findAllByUserId(userId);
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List<UserCategoryEntity> getUserCategory(int userId) {
        try {
            return userCategoryRepository.findAllByUserId(userId);
        } catch (Exception e) {
        }
        return null;
    }
}
