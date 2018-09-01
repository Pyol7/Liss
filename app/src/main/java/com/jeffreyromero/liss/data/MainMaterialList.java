package com.jeffreyromero.liss.data;

import android.content.Context;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Board;
import com.jeffreyromero.liss.models.CChannel;
import com.jeffreyromero.liss.models.FurringChannel;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.Stud;

import java.util.ArrayList;

public class MainMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> aList = new ArrayList<>();
        aList.add(new Board("Moisture Resistant Boards", 7400, 8f, 4f, 0.5f));
        aList.add(new FurringChannel("Furring Channels", 2000, 10f));
        aList.add(new CChannel("C Channels", 2000, 16f));
        aList.add(new Stud("Metal Studs", 1540, 9f, 2.5f));
        aList.add(new FurringChannel("Furring Channels", 2000, 10f));
        aList.add(new CChannel("C Channels", 2000, 16f));
        aList.add(new Stud("Metal Studs", 1540, 9f, 2.5f));
        return new MaterialList(aList, context.getString(R.string.main_material_list));
    }
}
