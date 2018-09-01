package com.jeffreyromero.liss.material;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.repositories.MaterialListsDataSource;

import java.util.ArrayList;

/**
 * Handles the following:
 * Loads MaterialListFragment when a list item is click.
 */
public class MaterialActivity extends AppCompatActivity implements
        MaterialListsFragment.OnItemClickListener,
        MaterialListFragment.OnItemClickListener {

    private ArrayList<MaterialList> allLists;
    private MaterialListsDataSource mlr;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get all the stored material lists
        mlr =  new MaterialListsDataSource(R.string.user_material_lists,this);
        allLists = mlr.getAll();

        // Display all lists.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            MaterialListsFragment f = MaterialListsFragment.newInstance(allLists);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------- MaterialListsFragment Callbacks --------------------//

    @Override
    public void onItemClick(int position) {
        // Reload all the stored material lists
        allLists = mlr.getAll();
        // Get the clicked list
        MaterialList clickedList = allLists.get(position);
        // Load MaterialListFragment
        MaterialListFragment f = MaterialListFragment.newInstance(clickedList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAddNewListButtonClick() {
        // Create an empty list.
        MaterialList materialList = new MaterialList("New List");
        // Load MaterialListFragment
        MaterialListFragment f = MaterialListFragment.newInstance(materialList);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onListItemRemoved(MaterialList materialList) {
        mlr.remove(materialList.getName());
    }

    //----------------- MaterialListFragment Callbacks -----------------------//

    @Override
    public void onListNameChange(String newName) {
        // The material list has been renamed
    }

    @Override
    public void onListItemRemove(MaterialList newMaterialList) {
        // Overwrite list with same name in source.
        mlr.put(newMaterialList);

    }


}

//Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();


