package com.capstone.services;

import com.capstone.model.Item;
import com.capstone.repository.ItemRepository;
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
