package com.jeffreyromero.liss.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.materialActivity.MaterialActivity;
import com.jeffreyromero.liss.repositories.MaterialListRepository;

/**
 * Loads MainActivityFragment.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Check MaterialListRepository SP version and upgrade, downgrade or create new.
        new MaterialListRepository(this).checkVersion();

        // Add the view for this activity
        if (findViewById(R.id.fragment_container) != null) {
            // Avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, new MainActivityFragment(), "MainActivityFragment");
            ft.commit();
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
        } else if (id == R.id.materialLists_mi) {
            // Load MaterialActivity
            Intent Intent = new Intent(this, MaterialActivity.class);
            startActivity(Intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
//Toast.makeText(getActivity(), "Clicked @" + position, Toast.LENGTH_SHORT).show();
