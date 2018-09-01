package com.jeffreyromero.liss.models;

import static java.lang.Math.sqrt;

public class Stud extends Material {
    private double width;

    public Stud(String name, int price, double length, double width) {
        super("Stud", name, price, length);
        this.width = width;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    @Override
    public double calcQuantity(double roomLength, double roomHeight) {
        double centers = 24;
        double pieces = roomLength/centers;
        double lengths = roomHeight/this.length;
        double q = pieces*lengths;
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
        return  getName() + " " + width + "x" + getLength();
    }
}
