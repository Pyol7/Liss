package com.jeffreyromero.liss.dialogs;

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

import com.jeffreyromero.liss.data.MaterialDeserializer;
import com.jeffreyromero.liss.data.DefaultMaterialLists;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.model.Board;
import com.jeffreyromero.liss.model.Material;
import com.jeffreyromero.liss.model.MaterialList;
import com.jeffreyromero.liss.model.DrywallScrew;
import com.jeffreyromero.liss.model.Stud;

/**
 * Displays the clicked item for editing.
 */
public class ListFragmentDialog extends DialogFragment {
    public OnItemChangeListener onItemChangeListener;
    private Material selectedMaterial;
    private View dialogView;

    public interface OnItemChangeListener{
        void onListFragmentDialogSubmit(Material selectedMaterial);
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
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        inflateView();
        setSpinner();
        return dialogBuilder().create();
    }

    private void inflateView() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        dialogView = inflater.inflate(R.layout.fragment_list_dialog, null);
    }

    private AlertDialog.Builder dialogBuilder(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle("Edit Material");
        // Set the OK button click listener that would update the selected Models
        // with the changes from user input and send it to the onItemChangeListener.
        // In this case, the hosting fragment (ListFragment).
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Update the selected Models with user input.
                EditText priceET = dialogView.findViewById(R.id.priceET);
                selectedMaterial.setPrice(Integer.valueOf(priceET.getText().toString()));
                // Send the new selected Models to the onItemChangeListener.
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

    private void setSpinner() {
        // Get spinner view
        Spinner spinner = dialogView.findViewById(R.id.spinner);
        // Get the clicked Models from the bundle.
        Material clickedMaterial = MaterialDeserializer
                .toMaterial(getArguments().getString("material"));

        // Get Models list to populate the spinner
        // TODO - create a list that would contain all default and
        // TODO - user added materials without duplication.
        MaterialList mList = new DefaultMaterialLists(getActivity()).appMaterialList();

        // Add the clicked Models to the start of the DefaultMaterialList
        mList.set(0, clickedMaterial);
        // Create an ArrayAdapter
        ArrayAdapter<Material> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                mList.getList()
        );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set a selection listener that would update the dialog based on the selected item.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected Models from the spinner
                com.jeffreyromero.liss.model.Material material = (com.jeffreyromero.liss.model.Material) parent.getItemAtPosition(position);
                // Set the new Models to the dialog views
                setDataToViews(dialogView, material);
                // Make the current selected Models global
                ListFragmentDialog.this.selectedMaterial = material;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDataToViews(View view, com.jeffreyromero.liss.model.Material material) {
        // Set data to views based on Material type.
        TextView nameTV = view.findViewById(R.id.nameTV);
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
