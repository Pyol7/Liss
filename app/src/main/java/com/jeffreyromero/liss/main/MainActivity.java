package com.jeffreyromero.liss.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.quickMaterialCalculator.QuickMaterialCalculatorActivity;
import com.jeffreyromero.liss.data.MainMaterialList;
import com.jeffreyromero.liss.data.SampleCeilingMaterialList;
import com.jeffreyromero.liss.data.SamplePartitionMaterialList;
import com.jeffreyromero.liss.material.MaterialActivity;
import com.jeffreyromero.liss.project.ProjectActivity;
import com.jeffreyromero.liss.repositories.MaterialListsDataSource;

/**
 *
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Load local version of sample lists on first run else
        //Load from shared preferences.
        MaterialListsDataSource aml =
                new MaterialListsDataSource(R.string.app_material_lists,this);
        if(aml.isEmpty()){
            aml.put(MainMaterialList.getList(this));
            aml.put(SampleCeilingMaterialList.getList(this));
            aml.put(SamplePartitionMaterialList.getList(this));
        }

        // If the user material list is empty load the two samples from App material list.
        MaterialListsDataSource uml =
                new MaterialListsDataSource(R.string.user_material_lists,this);
        // Check for empty
        if (uml.isEmpty()){
            uml.put(aml.get(getString(R.string.sample_ceiling_material_list)));
            uml.put(aml.get(getString(R.string.sample_partition_material_list)));
        }

        //Project activity button
        Button projectsBtn = findViewById(R.id.projectsBtn);
        projectsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, ProjectActivity.class);
                startActivity(Intent);
            }
        });

        //Project activity button
        Button calculatorBtn = findViewById(R.id.calculatorBtn);
        calculatorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, QuickMaterialCalculatorActivity.class);
                startActivity(Intent);
            }
        });

        //Material activity button
        Button materialListsBtn = findViewById(R.id.materialListsBtn);
        materialListsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(MainActivity.this, MaterialActivity.class);
                startActivity(Intent);
            }
        });
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
