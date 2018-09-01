package com.jeffreyromero.liss.models;

import android.text.format.DateFormat;

import java.util.ArrayList;

public class Project {
    private ArrayList<ProjectItem> projectItems;
    private String description;
    private String dateCreated;
    private String name;

    public Project(String name) {
        this.projectItems = new ArrayList<>();
        this.dateCreated = setDateCreated();
        this.name = name;
    }

    public Project(String name, ArrayList<ProjectItem> projectItems) {
        this.dateCreated = setDateCreated();
        this.projectItems = projectItems;
        this.name = name;
    }

    public ArrayList<ProjectItem> getProjectItems() {
        return projectItems;
    }

    public void setProjectItems(ArrayList<ProjectItem> projectItems) {
        this.projectItems = projectItems;
    }

    public void add(ProjectItem projectItem){
        projectItems.add(projectItem);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String setDateCreated() {
        return DateFormat.format("dd-MM-yyyy", new java.util.Date()).toString();
    }

    public void setDateCreated(String date) {
        this.dateCreated = date;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

