package com.jeffreyromero.liss.materialActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.model.MaterialList;
import com.jeffreyromero.liss.repositories.MaterialListRepository;
import java.util.ArrayList;

/**
 * Handles the following:
 * Loads ListFragment when a list item is click.
 */
public class MaterialActivity extends AppCompatActivity implements
        AllListsFragment.OnItemClickListener {

    private ArrayList<MaterialList> allLists;
    private MaterialList clickedList;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get all the stored material lists
        allLists = new MaterialListRepository(this).getAll();

        // Display all lists.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            AllListsFragment f = AllListsFragment.newInstance(allLists);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.commit();
        }
    }

    //----------------- AllListFragment Callbacks --------------------//

    @Override
    public void onAllListsFragmentItemClick(int position) {
        // Reload all the stored material lists
        allLists = new MaterialListRepository(this).getAll();
        // Load ListFragment with the clicked list
        clickedList = allLists.get(position);
        replaceFragment(ListFragment.newInstance(clickedList));
    }

    @Override
    public void onAllListsFragmentAddNewListButtonClick() {
        // Load ListFragment with an empty list.
        MaterialList m = new MaterialList();
        replaceFragment(ListFragment.newInstance(m));
    }

    //----------------- ListsFragment Callbacks -----------------------//

    private void replaceFragment(Fragment fragment) {
        // Replace existing fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(null);
        transaction.commit();
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

}

//Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();


