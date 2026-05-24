package com.vtlamdev.sourcebase.dao.container;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.GeneratedValue;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.*;
import org.springframework.data.annotation.Id;

@Container(containerName = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    @Id
    @GeneratedValue
    private String id;
    private String name;
    private Integer quantity;
    private Boolean sale;

    @PartitionKey
    private String category;

    public Item(String name, Integer quantity, Boolean sale, String category) {
        this.name = name;
        this.quantity = quantity;
        this.sale = sale;
        this.category = category;
    }
}
