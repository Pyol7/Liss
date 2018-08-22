package com.jeffreyromero.liss.repositories;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.liss.data.DefaultMaterialLists;
import com.jeffreyromero.liss.data.MaterialDeserializer;
import com.jeffreyromero.liss.interfaces.List;
import com.jeffreyromero.liss.interfaces.ListRepository;
import com.jeffreyromero.liss.model.MaterialList;

import java.util.ArrayList;
import java.util.Map;

public class MaterialListRepository implements ListRepository {
    private static final int VERSION_NUMBER = 1;
    private static final String SP_NAME = "MATERIAL_LISTS";
    private SharedPreferences spInstance;
    private Context context;

    public MaterialListRepository(Context context) {
        this.spInstance = context.getSharedPreferences(SP_NAME, 0);
        this.context = context;
    }

    /**
     * Check the version number and upgrade, downgrade or create SP data.
     */
    public void checkVersion() {
        if (spInstance.contains("VERSION_NUMBER")){
            int storedVersionNumber = spInstance.getInt("VERSION_NUMBER", 0);
            if (VERSION_NUMBER > storedVersionNumber){
                // Update SP
                // Get and hold all data, clear SP, store old data under one key,
                // store new version number and load default lists.
                Map<String, ?> map = spInstance.getAll();
                String json = new Gson().toJson(map);
                this.clearAll();
                SharedPreferences.Editor editor = spInstance.edit();
                editor.putString("OLD_DATA", json);
                editor.putInt("VERSION_NUMBER", VERSION_NUMBER);
                editor.commit();

                // Load default lists
                new DefaultMaterialLists(context).loadDefaultLists(this);

                Toast.makeText(context,
                        "Shared Preferences Upgraded to v" + VERSION_NUMBER,
                        Toast.LENGTH_SHORT).show();
            } else if (VERSION_NUMBER < storedVersionNumber){
                // Downgrade SP
                //TODO restore old version of sp
                Toast.makeText(context,
                        "Shared Preferences Downgraded to v" + VERSION_NUMBER,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Create SP
            SharedPreferences.Editor editor = spInstance.edit();
            editor.putInt("VERSION_NUMBER", 1);
            editor.apply();

            // Load default lists
            new DefaultMaterialLists(context).loadDefaultLists(this);

            Toast.makeText(context,
                    "Shared Preferences created v" + VERSION_NUMBER,
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void clearAll(){
        SharedPreferences.Editor editor = spInstance.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Get the MaterialList by key.
     */
    public MaterialList get(String key) {
        if (!spInstance.contains(key)) {
            Log.e("<< ERROR >>", "Shared Preferences key: '" + key + "' was not found.");
            return null;
        }
        String json = spInstance.getString(key, null);
        return MaterialDeserializer.toMaterialList(json);
    }

    /**
     * Get all the stored List.
     */
    public ArrayList<MaterialList> getAll() {
        ArrayList<MaterialList> amList = new ArrayList<>();
        ArrayList<String> keys = getAllKeys();
        for (String key : keys) {
            MaterialList mList = get(key);
            amList.add(mList);
        }
        return amList;
    }

    /**
     * Store a List using it's name property as key.
     */
    public void put(List mList) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(mList);
        editor.putString(mList.getName(), json);
        editor.apply();
    }

    @Override
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
    }

    /**
     * Get the keys of all the stored Lists.
     */
    public ArrayList<String> getAllKeys() {
        Map<String, ?> map = spInstance.getAll();
        map.remove("VERSION_NUMBER");
        map.remove("OLD_DATA");
        return new ArrayList<>(map.keySet());
    }

    public void renameKey(String oldName, String newName) {
        MaterialList mList = get(oldName);
        spInstance.edit().remove(oldName).commit();
        mList.setName(newName);
        put(mList);
    }
}
