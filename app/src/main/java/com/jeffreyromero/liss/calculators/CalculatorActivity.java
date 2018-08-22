package com.jeffreyromero.liss.calculators;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.DefaultMaterialLists;
import com.jeffreyromero.liss.materialActivity.ListFragment;
import com.jeffreyromero.liss.model.MaterialList;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Button btn = findViewById(R.id.calc_material_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user inputs
                EditText lengthET = findViewById(R.id.lengthET);
                EditText widthET = findViewById(R.id.widthET);
                String length = lengthET.getText().toString();
                String width = widthET.getText().toString();
                // Get ListFragment
                ListFragment f = (ListFragment) getSupportFragmentManager()
                        .findFragmentByTag("ListFragment");
                // Calculate
                f.calculateQuantitiesByWidthLength(Double.valueOf(length), Double.valueOf(width));
            }
        });

        // Display all lists.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            //TODO - add flag to set default list.
            // Get list to load into ListFragment
            MaterialList mList = new DefaultMaterialLists(this).appMaterialList();

            ListFragment f = ListFragment.newInstance(mList);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragment_container, f, f.getClass().getSimpleName());
            ft.commit();
            Toast.makeText(this, "" + f.getClass().getSimpleName(), Toast.LENGTH_SHORT).show();
        }
    }
}