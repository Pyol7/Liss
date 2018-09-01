package com.jeffreyromero.liss.repositories;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.Project;

import java.util.ArrayList;
import java.util.Map;

public class ProjectsDataSource implements ListsDataSource<Project> {
    private SharedPreferences spInstance;

    public ProjectsDataSource(int spName, Context context) {
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
     * Get the Project by key.
     */
    @Override
    public Project get(String key) {
        String json = spInstance.getString(key, null);
        return Deserializer.toProject(json);
    }

    /**
     * Get the Project by position.
     */
    @Override
    public Project get(int position) {
        ArrayList<Project> allProjects = getAll();
        return allProjects.get(position);
    }

    /**
     * Get all the stored Project.
     */
    @Override
    public ArrayList<Project> getAll() {
        ArrayList<Project> allProjects = new ArrayList<>();
        ArrayList<String> keys = getAllKeys();
        for (String key : keys) {
            Project project = get(key);
            allProjects.add(project);
        }
        return allProjects;
    }

    /**
     * Store a Project using it's name property as key.
     * If the list exists it would overwrite it.
     */
    @Override
    public void put(Project project) {
        SharedPreferences.Editor editor = spInstance.edit();
        String json = new Gson().toJson(project);
        editor.putString(project.getName(), json);
        editor.commit();
    }

    /**
     * Remove a Project by key.
     */
    @Override
    public void remove(String key) {
        SharedPreferences.Editor editor = spInstance.edit();
        editor.remove(key).commit();
    }

    /**
     * Get the keys of all the stored Projects.
     * This is also all the names of Projects.
     */
    @Override
    public ArrayList<String> getAllKeys() {
        Map<String, ?> map = spInstance.getAll();
        return new ArrayList<>(map.keySet());
    }

    /**
     * Rename a stored Project.
     */
    @Override
    public void renameKey(String oldName, String newName) {
        Project project = get(oldName);
        spInstance.edit().remove(oldName).commit();
        project.setName(newName);
        put(project);
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
