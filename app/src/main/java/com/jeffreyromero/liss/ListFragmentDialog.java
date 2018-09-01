package com.jeffreyromero.liss;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.Board;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.DrywallScrew;
import com.jeffreyromero.liss.models.Stud;


/**
 * Displays the clicked item for editing.
 */
public class ListFragmentDialog extends DialogFragment {
    private OnItemChangeListener onItemChangeListener;
    private Material clickedMaterial;
    private MaterialList spinnerList;
    private Material selectedMaterial;
    private View dialogView;

    public interface OnItemChangeListener{
        void onListFragmentDialogSubmit(Material selectedMaterial);
    }

    public static ListFragmentDialog newInstance(Material clickedMaterial, MaterialList spinnerList) {
        ListFragmentDialog fragment = new ListFragmentDialog();
        //Serialize data.
        String clickedMaterialJson = new Gson().toJson(clickedMaterial);
        String spinnerListJson = new Gson().toJson(spinnerList);
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("clickedMaterial", clickedMaterialJson);
        args.putString("spinnerList", spinnerListJson);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the hosting fragment and ensure that it implements onItemChangeListener.
        try {
            onItemChangeListener = (OnItemChangeListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement ListFragmentDialog.onItemChangeListener");
        }
        // Get bundle data and add to instance variables.
        this.clickedMaterial = Deserializer
                .toMaterial(getArguments().getString("clickedMaterial"));
        this.spinnerList = Deserializer
                .toMaterialList(getArguments().getString("spinnerList"));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.add_edit_material_dialog, null);
        initSpinner();
        return dialogBuilder().create();
    }

    private AlertDialog.Builder dialogBuilder(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle("Edit Material");
        // Set the OK button click listener that would update the selected material
        // with the changes from user input and send it to the onItemChangeListener.
        // In this case, the hosting fragment (MaterialListFragment).
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the selected material with new price.
                EditText priceET = dialogView.findViewById(R.id.priceET);
                selectedMaterial.setPrice(Integer.valueOf(priceET.getText().toString()));
                // Send the new selected material to the onItemChangeListener.
                onItemChangeListener.onListFragmentDialogSubmit(selectedMaterial);
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return dialog;
    }

    private void initSpinner() {
        // Get spinner view
        Spinner spinner = dialogView.findViewById(R.id.spinner);

        // Add the clicked material to the start of the list
        spinnerList.add(0, clickedMaterial);
        // Create an ArrayAdapter
        ArrayAdapter<Material> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                spinnerList.getList()
        );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set a selection listener that would update the dialog based on the selected item.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected material from the spinner
                Material material = (Material) parent.getItemAtPosition(position);
                // Set the new material to the dialog views
                setDataToViews(dialogView, material);
                // Store the selected material to be sent when ever the OK button is clicked.
                ListFragmentDialog.this.selectedMaterial = material;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDataToViews(View view, Material material) {
        // Set data to views based on Material type.
        TextView nameTV = view.findViewById(R.id.nameET);
        nameTV.setText(material.getName());
        EditText priceET = view.findViewById(R.id.priceET);
        priceET.setText(String.valueOf(material.getPrice()));
        TextView lengthTV = view.findViewById(R.id.lengthET);
        lengthTV.setText(String.valueOf(material.getLength()));
        if (material instanceof DrywallScrew) {
            TextView widthTV = view.findViewById(R.id.widthET);
            widthTV.setText("n/a");
            TextView thicknessTV = view.findViewById(R.id.thicknessTV);
            thicknessTV.setText("n/a");
        } else if (material instanceof Stud) {
            Stud stud = (Stud) material;
            TextView widthTV = view.findViewById(R.id.widthET);
            widthTV.setText(String.valueOf(stud.getWidth()));
            TextView thicknessTV = view.findViewById(R.id.thicknessTV);
            thicknessTV.setText("n/a");
        } else if (material instanceof Board) {
            Board board = (Board) material;
            TextView widthTV = view.findViewById(R.id.widthET);
            widthTV.setText(String.valueOf(board.getWidth()));
            TextView thicknessTV = view.findViewById(R.id.thicknessTV);
            thicknessTV.setText(String.valueOf(board.getThickness()));
        }
    }

    public ListFragmentDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onItemChangeListener = null;
    }
}
