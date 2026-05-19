package com.vtlamdev.sourcebase.common.data.page;

import java.util.Collections;
import java.util.List;

public class ScrollData<T> {

    private final List<T> data;
    private final int limit;
    private final String nextCursor;
    private final boolean hasNext;

    public ScrollData(List<T> data, int limit, String nextCursor, boolean hasNext) {
        this.data = data == null ? Collections.emptyList() : data;
        this.limit = limit;
        this.nextCursor = nextCursor;
        this.hasNext = hasNext;
    }

    public List<T> getData() {
        return data;
    }

    public int getLimit() {
        return limit;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public boolean isHasNext() {
        return hasNext;
    }

}
