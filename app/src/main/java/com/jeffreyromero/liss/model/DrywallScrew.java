package com.jeffreyromero.liss.model;

import static java.lang.Math.sqrt;

public class DrywallScrew extends Material {

    public DrywallScrew(String name, int price, double length) {
        super("DrywallScrew", name, price, length);
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double screwsPer4x8Board = 32;
        double q = (roomLength*roomWidth)/screwsPer4x8Board;
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
