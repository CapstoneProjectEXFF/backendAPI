package com.capstone.exff.services;

import com.capstone.exff.entities.Item;
import com.capstone.exff.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemServicesImpl implements ItemServices {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemServicesImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<Item> findItemsByItemName(String itemName) {
        return itemRepository.findItemsByItemName(itemName);
    }
}
