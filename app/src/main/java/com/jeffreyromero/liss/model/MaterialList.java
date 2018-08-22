package com.jeffreyromero.liss.model;

import android.text.format.DateFormat;

import com.jeffreyromero.liss.interfaces.List;
import java.util.ArrayList;

public class MaterialList implements List {
    private ArrayList<Material> materialList;
    private String name;
    private String dateCreated;

    public MaterialList() {
        this.dateCreated = DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
        this.materialList = new ArrayList<>();
    }

    public MaterialList(String name) {
        this.dateCreated = DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
        this.materialList = new ArrayList<>();
        this.name = name;
    }

    public MaterialList(ArrayList<Material> materialList, String name) {
        this.dateCreated = DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
        this.materialList = materialList;
        this.name = name;
    }

    @Override
    public ArrayList<Material> getList() {
        return materialList;
    }

    @Override
    public void setList(ArrayList<Material> materialList) {
        this.materialList = materialList;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<String> getNames() {
        ArrayList<String> list = new ArrayList<>();
        for (Material material : materialList ) {
            list.add(material.getName());
        }
        return list;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Material get(int position) {
        return materialList.get(position);
    }

    @Override
    public void set(int position, Material material) {
        materialList.set(position, material);
    }

    @Override
    public void add(Material material) {
        materialList.add(material);
    }

    @Override
    public int size() {
        return materialList.size();
    }

    @Override
    public String getDateCreated() {
        return dateCreated;
    }
}
