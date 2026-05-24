package com.vtlamdev.sourcebase.dao.cosmos;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import com.vtlamdev.sourcebase.dao.container.Item;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CosmosRepository<Item, String> {

    @Query("SELECT * FROM products p WHERE p.category = @category")
    List<Item> getItemsByCategory(@Param("category") String category);

}
