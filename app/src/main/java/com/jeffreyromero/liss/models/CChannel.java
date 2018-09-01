package com.jeffreyromero.liss.models;

import static java.lang.Math.sqrt;

public class CChannel extends Material {

    public CChannel(String name, int price, double length) {
        super("CChannel", name, price, length);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double q = 0.00f;
        if (roomWidth > 4) {
            double lengths = roomLength / 15;
            double pieces = Math.floor(roomWidth / 4.1);
            q = pieces * lengths;
        }
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
