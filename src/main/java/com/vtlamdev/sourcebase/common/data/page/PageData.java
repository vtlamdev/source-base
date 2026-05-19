package com.vtlamdev.sourcebase.common.data.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PageData<T> implements Serializable {

    public static final PageData<?> EMPTY_PAGE_DATA = new PageData<>();

    private final List<T> data;
    private final int totalPages;
    private final long totalElements;
    private final boolean hasNext;

    public PageData() {
        this(Collections.emptyList(), 0, 0, false);
    }

    public PageData(List<T> data,
                    int totalPages,
                    long totalElements,
                    boolean hasNext) {
        this.data = data;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.hasNext = hasNext;
    }

    public List<T> getData() {
        return data;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public <D> PageData<D> mapData(Function<T, D> mapper) {
        return new PageData<>(data.stream().map(mapper).toList(), totalPages, totalElements, hasNext);
    }

}
