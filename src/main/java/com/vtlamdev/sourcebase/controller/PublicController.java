package com.vtlamdev.sourcebase.controller;

import com.vtlamdev.sourcebase.controller.base.BaseController;
import com.vtlamdev.sourcebase.dao.container.Item;
import com.vtlamdev.sourcebase.service.CosmosItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@Tag(name = "Public")
@RequiredArgsConstructor
public class PublicController extends BaseController {

    private final CosmosItemService cosmosItemService;

    @PostMapping()
    public ResponseEntity<Void> create() {
        cosmosItemService.createItem();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/id/{id}")
    public Item get(@PathVariable String id) {
        return cosmosItemService.getItem(id);
    }

    @GetMapping("/category/{category}")
    public List<Item> getByCategory(@PathVariable String category) {
        return cosmosItemService.getItemByCategory(category);
    }

}
