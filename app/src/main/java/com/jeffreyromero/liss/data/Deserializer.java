package com.jeffreyromero.liss.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jeffreyromero.liss.models.Board;
import com.jeffreyromero.liss.models.CChannel;
import com.jeffreyromero.liss.models.DrywallScrew;
import com.jeffreyromero.liss.models.FurringChannel;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.models.ProjectItem;
import com.jeffreyromero.liss.models.Stud;
import com.jeffreyromero.liss.models.WallAngle;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Gson would serialize anything.
 * Deserialization requires a little more effort:
 * Array - the square brackets is needed. e.g. fromJson(json, array[].class).
 * IList - a Type is needed using a TypeToken. e.g. new TypeToken<ArrayList<Object>(){}.getType().
 * Object must not be a subclass.
 * IList<subtype> - Gson must know every subtype. This is achieved by registering the
 * GsonRuntimeTypeAdapterFactory, adding a "type" field in the superclass and have all subtypes
 * pass their class name as the type to the superclass.
 * To avoid this extra boilerplate assign the IList<Object> to a field inside of a class.
 */
public class Deserializer {
    private static Gson gson;

    static {
        GsonRuntimeTypeAdapterFactory<Material> MaterialTypeAdapter = GsonRuntimeTypeAdapterFactory
                .of(Material.class, "type")
                .registerSubtype(Board.class, "Board")
                .registerSubtype(FurringChannel.class, "FurringChannel")
                .registerSubtype(CChannel.class, "CChannel")
                .registerSubtype(WallAngle.class, "WallAngle")
                .registerSubtype(Stud.class, "Stud")
                .registerSubtype(DrywallScrew.class, "DrywallScrew");
        gson = new GsonBuilder().registerTypeAdapterFactory(MaterialTypeAdapter).create();
    }

    public static ArrayList<MaterialList> toArrayListOfMaterialList(String json) {
        Type type = new TypeToken<ArrayList<MaterialList>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static MaterialList toMaterialList(String json) {
        return gson.fromJson(json, MaterialList.class);
    }

    public static Material toMaterial(String json) {
        return gson.fromJson(json, Material.class);
    }

    public static ArrayList<Project> toProjects(String json) {
        Type type = new TypeToken<ArrayList<Project>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static Project toProject(String json) {
        return gson.fromJson(json, Project.class);
    }

    public static ProjectItem toProjectItem(String json) {
        return gson.fromJson(json, ProjectItem.class);
    }

}
