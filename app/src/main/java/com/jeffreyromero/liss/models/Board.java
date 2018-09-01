package com.jeffreyromero.liss.models;

import static java.lang.Math.sqrt;

public class Board extends Material {
    private double thickness;
    private double width;

    public Board(String name, int price, double length, double width, double thickness) {
        super("Board", name, price, length);
        this.width = width;
        this.thickness = thickness;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    @Override
    public double calcQuantity(double roomLength, double roomWidth) {
        double area = roomLength*roomWidth;
        double q = area/(length*width);
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
        return  getName();
    }

}