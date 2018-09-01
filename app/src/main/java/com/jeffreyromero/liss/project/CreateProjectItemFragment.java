package com.jeffreyromero.liss.project;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

import com.google.gson.Gson;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.models.ProjectItem;
import com.jeffreyromero.liss.repositories.MaterialListsDataSource;
import com.jeffreyromero.liss.repositories.ProjectsDataSource;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateProjectItemFragment extends Fragment {

    private static final String PROJECT = "project";
    private MaterialListsDataSource userMaterialLists;
    private ArrayList<MaterialList> allLists;
    private RecyclerViewAdapter adapter;
    private OnCreationListener mListener;
    private MaterialList selectedList;
    private ProjectsDataSource pds;
    private MenuItem saveMenuItem;
    private Project project;
    private Context context;
    private String length;
    private String width;
    private View view;

    public CreateProjectItemFragment() {
        // Required empty public constructor
    }

    public static CreateProjectItemFragment newInstance(Project project) {
        CreateProjectItemFragment f = new CreateProjectItemFragment();
        Bundle args = new Bundle();
        String json = new Gson().toJson(project);
        args.putString(PROJECT, json);
        f.setArguments(args);
        return f;
    }

    public interface OnCreationListener {
        void onProjectItemAdded(Project updatedProject);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnCreationListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.toString()
                    + " must implement CreateProjectItemFragment.OnCreationListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        //Set all fields.
        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT);
            project = Deserializer.toProject(json);
        }
        //Get the projects data source.
        pds = new ProjectsDataSource(R.string.projects, context);
        //Get the array of material lists.
        userMaterialLists = new MaterialListsDataSource(R.string.user_material_lists, context);
        allLists = userMaterialLists.getAll();
        //TODO - this would eventually be the user default list.
        selectedList = allLists.get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.material_calculator, container, false);

        //Display the selected list.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(context));

        Spinner spinnerView = view.findViewById(R.id.listsSpinner);
        initSpinner(spinnerView);

        //Get the user input and calculate quantities.
        Button btn = view.findViewById(R.id.calc_material_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get user inputs
                EditText lengthET = view.findViewById(R.id.lengthET);
                EditText widthET = view.findViewById(R.id.widthET);
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

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Programmatically create the save menu item
        MenuItem saveMenuItem = menu.add(Menu.NONE, R.id.action_save, 10, R.string.action_save);
        saveMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        saveMenuItem.setIcon(R.drawable.ic_save_white_24dp);
        saveMenuItem.setVisible(false);
        //Hold on to saveMenuItem
        this.saveMenuItem = saveMenuItem;
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
        EditText projectItemET = view.findViewById(R.id.projectItemET);
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
                //Add it to the project.
                project.add(projectItem);
                //Save updated project
                pds.put(project);
                //Pass the updated project to the listener.
                mListener.onProjectItemAdded(project);
            }
        } else {
            Toast.makeText(context, "Add a project item name", Toast.LENGTH_LONG).show();
        }
    }

    private void alertDialog() {
        new AlertDialog.Builder(context)
                .setTitle( "Alert" )
                .setMessage("A list with that name was found. Cancel and rename or Overwrite?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                        dialoginterface.cancel();
                    }})
                .setPositiveButton("OverWrite", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        Toast.makeText(context, "overwrite", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void initSpinner(Spinner spinner){
        // Create an ArrayAdapter
        ArrayAdapter<MaterialList> spinnerAdapter = new ArrayAdapter<>(
                context,
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
            LayoutInflater inflater = LayoutInflater.from(context);
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
