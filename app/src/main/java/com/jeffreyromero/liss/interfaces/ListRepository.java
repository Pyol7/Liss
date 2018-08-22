package com.jeffreyromero.liss.interfaces;

import com.jeffreyromero.liss.model.MaterialList;

import java.util.ArrayList;

public interface ListRepository {

    List get(String key);

    ArrayList<MaterialList> getAll();

    void put(List list);

    void remove(String key);

    ArrayList<String> getAllKeys();

    void renameKey(String oldKey, String newKey);

}
