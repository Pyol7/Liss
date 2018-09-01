package com.jeffreyromero.liss.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.MaterialList;
import java.util.ArrayList;
import java.util.Map;

public class MaterialListsDataSource implements ListsDataSource<MaterialList> {
    private SharedPreferences spInstance;

    public MaterialListsDataSource(int spName, Context context) {
        this.spInstance = context.getSharedPreferences(context.getString(spName), 0);
    }

    @Override
    public boolean contains(String key){
        return spInstance.contains(key);
    }

    @Override
    public void clearAll(){
        SharedPreferences.Editor editor = spInstance.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Get the MaterialList by key.
     */
    @Override
    public MaterialList get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toMaterialList(json);
    }

    /**
     * Get the MaterialList by position.
     */
    @Override
    public MaterialList get(int position) {
        ArrayList<MaterialList> allMaterialLists = getAll();
        return allMaterialLists.get(position);
    }

    /**
     * Get all the stored MaterialList.
     */
    @Override
    public ArrayList<MaterialList> getAll() {
        ArrayList<MaterialList> allMaterialLists = new ArrayList<>();
        ArrayList<String> keys = getAllKeys();
        for (String key : keys) {
            MaterialList materialList = get(key);
            allMaterialLists.add(materialList);
        }
        return allMaterialLists;
    }

    /**
     * Store a MaterialList using it's name property as key.
     * If the list exists it would overwrite it.
     */
    @Override
    public void put(MaterialList materialList) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(materialList);
        editor.putString(materialList.getName(), json);
        editor.commit();
    }

    /**
     * Remove a MaterialList by key.
     */
    @Override
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
    }

    /**
     * Get the keys of all the stored MaterialList.
     * This is also all the names of MaterialLists.
     */
    @Override
    public ArrayList<String> getAllKeys() {
        Map<String, ?> map = spInstance.getAll();
        return new ArrayList<>(map.keySet());
    }

    /**
     * Rename a stored MaterialList.
     */
    @Override
    public void renameKey(String oldName, String newName) {
        MaterialList materialList = get(oldName);
        spInstance.edit().remove(oldName).commit();
        materialList.setName(newName);
        put(materialList);
    }

    /**
     * Check if Shared preferences is empty.
     */
    @Override
    public boolean isEmpty() {
        Map<String, ?> map = spInstance.getAll();
        return map.size() == 0;
    }
}
