package com.jeffreyromero.liss.interfaces;

import com.jeffreyromero.liss.model.Material;
import com.jeffreyromero.liss.model.MaterialList;

import java.util.ArrayList;

public interface List {

    ArrayList<Material> getList();

    void setList(ArrayList<Material> materialList);

    String getDateCreated();

    String getName();

    ArrayList<String> getNames();

    void setName(String name);

    Material get(int position);

    void set(int position, Material material);

    void add(Material material);

    int size();

}
