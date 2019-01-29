package com.capstone.exff.services;

import com.capstone.exff.entities.ItemEntity;
import org.springframework.http.ResponseEntity;

public interface ItemServices {
    ItemEntity createItem(String name, int userId, String description);
    ResponseEntity updateItem(int id, String name, String description, int userId);
    ResponseEntity removeItem(int id, int userId);
}
