package com.jeffreyromero.liss.models;

import android.text.format.DateFormat;

import java.util.ArrayList;

public class MaterialList{
    private ArrayList<Material> materialList;
    private String dateCreated;
    private String name;

    public MaterialList(String name) {
        this.materialList = new ArrayList<>();
        this.dateCreated = setDateCreated();
        this.name = name;
    }

    public MaterialList(ArrayList<Material> materialList, String name) {
        this.dateCreated = setDateCreated();
        this.materialList = materialList;
        this.name = name;
    }

    public ArrayList<Material> getList() {
        return materialList;
    }

    public void setList(ArrayList<Material> materialList) {
        this.materialList = materialList;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> list = new ArrayList<>();
        for (Material material : materialList ) {
            list.add(material.getName());
        }
        return list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material get(int position) {
        return materialList.get(position);
    }

    public Material remove(int position) {
        return materialList.remove(position);
    }


    public void replace(int position, Material material) {
        materialList.set(position, material);
    }

    public void add(Material material) {
        materialList.add(material);
    }

    public void add(int position, Material material) {
        materialList.add(position, material);
    }

    public int size() {
        return materialList.size();
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String setDateCreated() {
        return DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
    }

    public void calculateQuantities(double roomLength, double roomWidth) {
        ArrayList<Material> newList = new ArrayList<>();
        for (Material material : materialList){
            // Each material calculates and stores its on quantity.
            material.calcQuantity(roomLength, roomWidth);
            // Rebuild a new list.
            newList.add(material);
        }
        materialList = newList;
    }

    public void calculateQuantities(double area) {
        ArrayList<Material> newList = new ArrayList<>();
        for (Material material : materialList){
            // Each material calculates and stores its on quantity.
            material.calcQuantity(area);
            // Rebuild a new list.
            newList.add(material);
        }
        materialList = newList;
    }

    @Override
    public String toString() {
        return name;
    }
}
