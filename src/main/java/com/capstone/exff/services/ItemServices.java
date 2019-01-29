package com.capstone.exff.services;

import com.capstone.exff.model.Item;

import java.util.List;

public interface ItemServices {

    List<Item> findItemsByItemName(String itemName);
}
