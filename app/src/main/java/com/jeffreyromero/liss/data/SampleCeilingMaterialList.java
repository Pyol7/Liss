package com.jeffreyromero.liss.data;

import android.content.Context;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Board;
import com.jeffreyromero.liss.models.CChannel;
import com.jeffreyromero.liss.models.DrywallScrew;
import com.jeffreyromero.liss.models.FurringChannel;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.WallAngle;

import java.util.ArrayList;

public class SampleCeilingMaterialList {

    public static MaterialList getList(Context context) {
        ArrayList<Material> aList = new ArrayList<>();
        aList.add(new Board("Ultra Light Boards", 7400, 8f, 4f, 0.5f));
        aList.add(new FurringChannel("Furring Channels", 2000, 10f));
        aList.add(new CChannel("C Channels", 2000, 16f));
        aList.add(new WallAngle("Metal Wall Angles", 7400, 8f));
        aList.add(new DrywallScrew("Drywall Screws", 20, 1.25f));
        aList.add(new DrywallScrew("Framing Screws", 12, 0.75f));
        return new MaterialList(aList, context.getString(R.string.sample_ceiling_material_list));
    }
}
