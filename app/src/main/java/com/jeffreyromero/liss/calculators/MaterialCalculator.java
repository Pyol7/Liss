package com.jeffreyromero.liss.calculators;

import com.jeffreyromero.liss.model.Material;
import com.jeffreyromero.liss.model.MaterialList;
import java.util.ArrayList;

/**
 * Calling the calculate() method on an instance of this class populates the quantity field
 * of every Material in the list. This is done by calling each material's calcQuantity()
 * method and returning the same MaterialList.
 */
public class MaterialCalculator {

    public static MaterialList calculateByWidthLength(MaterialList materialList, double roomLength, double roomWidth) {
        ArrayList<Material> newList = new ArrayList<>();
        for (Material material : materialList.getList()){
            // Each material calculates and stores its on quantity.
            material.calcQuantity(roomLength, roomWidth);
            // Rebuild a new list.
            newList.add(material);
        }
        materialList.setList(newList);
        return materialList;
    }

    public static MaterialList calculateByArea(MaterialList materialList, double area) {
        ArrayList<Material> newList = new ArrayList<>();
        for (Material material : materialList.getList()){
            // Each material calculates and stores its on quantity.
            material.calcQuantity(area);
            // Rebuild a new list.
            newList.add(material);
        }
        materialList.setList(newList);
        return materialList;
    }
}
