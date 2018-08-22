package com.jeffreyromero.liss.model;

import static java.lang.Math.sqrt;

public class FurringChannel extends Material {

    public FurringChannel(String name, int price, double length) {
        super("FurringChannel", name, price, length);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double pieces = (roomLength * 12) / 16;
        double lengths = roomWidth / 12 + (1 / 12);
        double q = pieces * lengths;
        super.setQuantity(q);
        return q;
    }

    @Override
    public double calcQuantity(double area) {
        double length = sqrt(area);
        double q = this.calcQuantity(length, length);
        super.setQuantity(q);
        return q;
    }

    @Override
    public String toString() {
        return  getName() + " (" +  getLength() + ")";
    }
}
