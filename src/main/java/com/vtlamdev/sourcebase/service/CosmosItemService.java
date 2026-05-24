package com.vtlamdev.sourcebase.service;

import com.vtlamdev.sourcebase.dao.container.Item;

import java.util.List;

public interface CosmosItemService {
    void createItem();

    Item getItem(String id);

    List<Item> getItemByCategory(String category);
}
