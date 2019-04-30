package com.capstone.exff.services;

import com.capstone.exff.entities.UserCategoryEntity;

import java.util.List;

public interface UserCategoryServices {
    List<UserCategoryEntity> createUserCategory(int userId, List<Integer> categoryIds);
    List<UserCategoryEntity> updateUserCategory(int userId, List<Integer> categoryIds, List<Integer> removeIds);
    List<UserCategoryEntity> getUserCategory(int userId);
}
