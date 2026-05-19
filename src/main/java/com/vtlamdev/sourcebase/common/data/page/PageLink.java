package com.vtlamdev.sourcebase.common.data.page;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageLink {

    private static final String DEFAULT_SORT_PROPERTY = "id";

    private final String textSearch;
    private final int pageSize;
    private final int page;
    private final SortOrder sortOrder;

    public PageLink(int pageSize, int page, String textSearch, SortOrder sortOrder) {
        this.textSearch = textSearch;
        this.pageSize = pageSize;
        this.page = page;
        this.sortOrder = sortOrder;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPage() {
        return page;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public PageLink nextPageLink() {
        return new PageLink(pageSize, page + 1, textSearch, sortOrder);
    }

    public Sort toSort(List<SortOrder> sortOrders, Map<String, String> columnMap, boolean addDefaultSorting) {
        List<SortOrder> resolvedSortOrders = new ArrayList<>(sortOrders);
        if (resolvedSortOrders.isEmpty() && sortOrder != null) {
            resolvedSortOrders.add(sortOrder);
        }
        if (addDefaultSorting && resolvedSortOrders.stream().noneMatch(order -> DEFAULT_SORT_PROPERTY.equals(order.getProperty()))) {
            resolvedSortOrders.add(new SortOrder(DEFAULT_SORT_PROPERTY, SortOrder.Direction.ASC));
        }
        return Sort.by(resolvedSortOrders.stream()
                .map(order -> new Sort.Order(Sort.Direction.fromString(order.getDirection().name()), columnMap.getOrDefault(order.getProperty(), order.getProperty())))
                .toList());
    }

}
