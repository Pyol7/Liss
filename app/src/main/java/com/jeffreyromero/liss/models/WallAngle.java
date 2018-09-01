package com.jeffreyromero.liss.models;

import static java.lang.Math.sqrt;

public class WallAngle extends Material {

    public WallAngle(String name, int price, double length) {
        super("WallAngle", name, price, length);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double par = (roomLength * 2) + (roomWidth * 2);
        double q = par / length;
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
