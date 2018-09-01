package com.jeffreyromero.liss.quickMaterialCalculator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.models.ProjectItem;
import com.jeffreyromero.liss.repositories.MaterialListsDataSource;
import com.jeffreyromero.liss.repositories.ProjectsDataSource;

import java.util.ArrayList;


public class QuickMaterialCalculatorActivity extends AppCompatActivity {

    private MaterialListsDataSource userMaterialLists;
    private ArrayList<MaterialList> allLists;
    private RecyclerViewAdapter adapter;
    private MaterialList selectedList;
    private ProjectsDataSource pds;
    private MenuItem saveMenuItem;
    private Project project;
    private String length;
    private String width;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.material_calculator);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup fields.
        //TODO - create projects activity
        project = new Project("La Riveria Apartment");
        //Get the projects data source.
        pds = new ProjectsDataSource(R.string.projects, this);
        //Get the array of material lists.
        userMaterialLists = new MaterialListsDataSource(R.string.user_material_lists,this);
        allLists = userMaterialLists.getAll();
        //TODO - this would eventually be the user default list.
        selectedList = allLists.get(0);

        //Display the selected list.
        RecyclerView rv = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        initSpinner((Spinner)findViewById(R.id.listsSpinner));

        //Get the user input and calculate quantities.
        Button btn = findViewById(R.id.calc_material_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user inputs
                EditText lengthET = findViewById(R.id.lengthET);
                EditText widthET = findViewById(R.id.widthET);
                length = lengthET.getText().toString();
                width = widthET.getText().toString();
                //Calculate quantities.
                selectedList.calculateQuantities(Double.valueOf(length), Double.valueOf(width));
                //Update list.
                adapter.notifyDataSetChanged();
                //add save button.
                saveMenuItem.setVisible(true);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the main menu.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //Create the save menu item.
        MenuItem saveMenuItem = menu.add(Menu.NONE, R.id.action_save, 10, R.string.action_save);
        saveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        saveMenuItem.setIcon(R.drawable.ic_save_white_24dp);
        saveMenuItem.setVisible(false);
        //Hold on to saveMenuItem
        this.saveMenuItem = saveMenuItem;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:

                saveProjectItem();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveProjectItem() {
        //Ensure that a project item name is provided.
        EditText projectItemET = findViewById(R.id.projectItemET);
        String projectItemName = projectItemET.getText().toString();
        if (!projectItemName.isEmpty()) {
            // If the name exists, prompt user
            if (pds.contains(selectedList.getName())) {
                // Prompt user to cancel and change name or overwrite.
                alertDialog();
            } else {
                //Build the project item.
                ProjectItem projectItem = new ProjectItem(
                        projectItemName,
                        Double.valueOf(length),
                        Double.valueOf(width),
                        selectedList
                );
                //Add the project item to the project.
                project.add(projectItem);
                //Save the project.
                pds.put(project);
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Add a project item name", Toast.LENGTH_LONG).show();
        }
    }

    private void initSpinner(Spinner spinner){
        // Create an ArrayAdapter
        final ArrayAdapter<MaterialList> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                allLists
        );
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(spinnerAdapter);
        // Set a selection listener that would update the dialog based on the selected item.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Get the selected materialList
                selectedList = allLists.get(position);
                //Update list.
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void alertDialog() {
        new AlertDialog.Builder(this)
                .setTitle( "Alert" )
                .setMessage("A list with that name was found. Cancel and rename or Overwrite?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Toast.makeText(QuickMaterialCalculatorActivity.this, "cancel", Toast.LENGTH_SHORT).show();
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("OverWrite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Toast.makeText(QuickMaterialCalculatorActivity.this, "overwrite", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    //------------------------------- Adapter -------------------------------//

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return selectedList.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(QuickMaterialCalculatorActivity.this);
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.item_view_2col, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Material material = selectedList.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.columnLeftTV.setText(material.getName());
            viewHolder.columnRightTV.setText(String.valueOf(material.getQuantity()));
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            ItemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
            }
        }
    }


}