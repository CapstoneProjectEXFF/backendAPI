package com.capstone.services;

import com.capstone.model.Item;

import java.util.List;

public interface ItemServices {

    List<Item> findItemsByItemName(String itemName);
}
