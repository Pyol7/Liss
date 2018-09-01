package com.jeffreyromero.liss.models;

public class ProjectItem {
    private String name;
    private double length;
    private double width;
    private MaterialList materialList;

    public ProjectItem(String name, double length, double width, MaterialList materialList) {
        this.name = name;
        this.length = length;
        this.width = width;
        this.materialList = materialList;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public MaterialList getMaterialList() {
        return materialList;
    }

    @Override
    public String toString() {
        return name;
    }
}
