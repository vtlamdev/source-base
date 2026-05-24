package com.vtlamdev.sourcebase.service.impl;

import com.azure.cosmos.models.PartitionKey;
import com.vtlamdev.sourcebase.dao.container.Item;
import com.vtlamdev.sourcebase.dao.cosmos.ItemRepository;
import com.vtlamdev.sourcebase.service.CosmosItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CosmosItemServiceImpl implements CosmosItemService {

    private final ItemRepository itemRepository;

    @Override
    public void createItem() {
        Item item = new Item(
                "Yamba Surfboard",
                12,
                false,
                "gear-surf-surfboards"
        );
        Item createdItem = itemRepository.save(item);
        log.info("Save item id: {}", createdItem.getId());
    }

    @Override
    public Item getItem(String id) {
        return itemRepository.findById(id, new PartitionKey("gear-surf-surfboards")).orElseThrow(RuntimeException::new);
    }

    @Override
    public List<Item> getItemByCategory(String category) {
        List<Item> items = itemRepository.getItemsByCategory(category);
        for(Item item : items) {
            log.info("Item info: {}", item);
        }
        return items;
    }
}
