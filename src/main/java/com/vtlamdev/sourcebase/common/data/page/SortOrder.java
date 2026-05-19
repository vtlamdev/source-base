package com.vtlamdev.sourcebase.common.data.page;

public class SortOrder {

    public enum Direction {
        ASC,
        DESC
    }

    private final String property;
    private final Direction direction;

    public SortOrder(String property, Direction direction) {
        this.property = property;
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public Direction getDirection() {
        return direction;
    }

}
