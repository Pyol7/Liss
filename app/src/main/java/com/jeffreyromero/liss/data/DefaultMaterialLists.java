package com.jeffreyromero.liss.data;

import android.content.Context;

import com.jeffreyromero.liss.model.Board;
import com.jeffreyromero.liss.model.CChannel;
import com.jeffreyromero.liss.model.DrywallScrew;
import com.jeffreyromero.liss.model.FurringChannel;
import com.jeffreyromero.liss.model.Material;
import com.jeffreyromero.liss.model.MaterialList;
import com.jeffreyromero.liss.model.Stud;
import com.jeffreyromero.liss.model.WallAngle;
import com.jeffreyromero.liss.repositories.MaterialListRepository;

import java.util.ArrayList;

/**
 * TODO find a better way.
 * These lists are used to populate shared preferences when MaterialActivity first starts
 * or the app is reset to default.
 */
public class DefaultMaterialLists {
    private Context context;

    public DefaultMaterialLists(Context context) {
        this.context = context;
    }

    public void loadDefaultLists(MaterialListRepository mr){
        mr.put(DefaultCeilingMaterialList());
        mr.put(DefaultPartitionMaterialList());
    }

    // TODO - This list should contain all the material available to the app.
    // TODO - The other sample lists should be a subset of this list.
    public MaterialList appMaterialList(){
        ArrayList<Material> aList = new ArrayList<>();
        aList.add(new Board("Moisture Resistant Boards", 7400, 8f, 4f, 0.5f));
        aList.add(new FurringChannel("Furring Channels", 2000, 10f));
        aList.add(new CChannel("C Channels", 2000, 16f));
        aList.add(new Stud("Metal Studs", 1540, 9f, 2.5f));
        aList.add(new FurringChannel("Furring Channels", 2000, 10f));
        aList.add(new CChannel("C Channels", 2000, 16f));
        aList.add(new Stud("Metal Studs", 1540, 9f, 2.5f));
        MaterialList aml = new MaterialList( aList, "appMaterialList");
        // Store list to SP.
        new MaterialListRepository(context).put(aml);
        return aml;
    }

    private MaterialList DefaultCeilingMaterialList() {
        ArrayList<Material> aList = new ArrayList<>();
        aList.add(new Board("Ultra Light Boards", 7400, 8f, 4f, 0.5f));
        aList.add(new FurringChannel("Furring Channels", 2000, 10f));
        aList.add(new CChannel("C Channels", 2000, 16f));
        aList.add(new WallAngle("Metal Wall Angles", 7400, 8f));
        aList.add(new DrywallScrew("Drywall Screws", 20, 1.25f));
        aList.add(new DrywallScrew("Framing Screws", 12, 0.75f));
        return new MaterialList( aList, "DefaultCeilingMaterialList");
    }

    private MaterialList DefaultPartitionMaterialList() {
        ArrayList<Material> aList = new ArrayList<>();
        aList.add(new Board("Regular Boards", 7400, 8f, 4f, 0.5f));
        aList.add(new Stud("Metal Studs", 1540, 9f, 2.5f));
        aList.add(new Stud("Metal Tracks", 1800, 10f, 2.5f));
        aList.add(new DrywallScrew("Drywall Screws", 20, 1.25f));
        aList.add(new DrywallScrew("Framing Screws", 12, 0.75f));
        return new MaterialList(aList,"DefaultPartitionMaterialList");
    }

}
