package com.vtlamdev.sourcebase.common.data.page;

public class ListQuery {

    private final ListMode mode;
    private final int page;
    private final int pageSize;
    private final int limit;
    private final String textSearch;
    private final String cursor;

    public ListQuery(ListMode mode, int page, int pageSize, int limit, String textSearch, String cursor) {
        this.mode = mode == null ? ListMode.PAGE : mode;
        this.page = Math.max(page, 0);
        this.pageSize = pageSize > 0 ? pageSize : 20;
        this.limit = limit > 0 ? limit : 20;
        this.textSearch = textSearch;
        this.cursor = cursor;
    }

    public ListMode getMode() {
        return mode;
    }

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getLimit() {
        return limit;
    }

    public String getTextSearch() {
        return textSearch;
    }

    public String getCursor() {
        return cursor;
    }

    public boolean isScrollMode() {
        return mode == ListMode.SCROLL;
    }

    public PageLink toPageLink() {
        return new PageLink(pageSize, page, textSearch, new SortOrder("createdTime", SortOrder.Direction.DESC));
    }

}
