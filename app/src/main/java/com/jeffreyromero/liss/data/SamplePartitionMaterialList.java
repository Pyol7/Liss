package com.jeffreyromero.liss.data;

import android.content.Context;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Board;
import com.jeffreyromero.liss.models.DrywallScrew;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.Stud;

import java.util.ArrayList;

public class SamplePartitionMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> aList = new ArrayList<>();
        aList.add(new Board("Regular Boards", 7400, 8f, 4f, 0.5f));
        aList.add(new Stud("Metal Studs", 1540, 9f, 2.5f));
        aList.add(new Stud("Metal Tracks", 1800, 10f, 2.5f));
        aList.add(new DrywallScrew("Drywall Screws", 20, 1.25f));
        aList.add(new DrywallScrew("Framing Screws", 12, 0.75f));
        return new MaterialList(aList, context.getString(R.string.sample_partition_material_list));
    }
}
