package com.vtlamdev.sourcebase.common.data.page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ListData<T> implements Serializable {

    private final ListMode mode;
    private final List<T> data;
    private final Integer page;
    private final Integer pageSize;
    private final Integer totalPages;
    private final Long totalElements;
    private final Integer limit;
    private final String nextCursor;
    private final boolean hasNext;

    private ListData(ListMode mode,
                     List<T> data,
                     Integer page,
                     Integer pageSize,
                     Integer totalPages,
                     Long totalElements,
                     Integer limit,
                     String nextCursor,
                     boolean hasNext) {
        this.mode = mode;
        this.data = data == null ? Collections.emptyList() : data;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.limit = limit;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
    }

    public static <T> ListData<T> page(List<T> data, int page, int pageSize, int totalPages, long totalElements, boolean hasNext) {
        return new ListData<>(ListMode.PAGE, data, page, pageSize, totalPages, totalElements, null, null, hasNext);
    }

    public static <T> ListData<T> scroll(List<T> data, int limit, String nextCursor, boolean hasNext) {
        return new ListData<>(ListMode.SCROLL, data, null, null, null, null, limit, nextCursor, hasNext);
    }

    public ListMode getMode() {
        return mode;
    }

    public List<T> getData() {
        return data;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public Long getTotalElements() {
        return totalElements;
    }

    public Integer getLimit() {
        return limit;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public boolean isHasNext() {
        return hasNext;
    }

}
