package com.jeffreyromero.liss.utilities;

public class RightColumn {
    private String name;

    public static final RightColumn PRICE = new RightColumn("price");
    public static final RightColumn QUANTITY = new RightColumn("quantity");

    private RightColumn(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
