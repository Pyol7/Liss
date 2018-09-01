package com.jeffreyromero.liss.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Board;
import com.jeffreyromero.liss.models.DrywallScrew;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.MaterialList;
import com.jeffreyromero.liss.models.Stud;
import com.jeffreyromero.liss.repositories.MaterialListsDataSource;


/**
 * Displays the input item for editing.
 */
public class AddMaterialDialog extends DialogFragment {

    private OnItemChangeListener onItemChangeListener;
    private MaterialList spinnerList;
    private Material selectedMaterial;
    private View dialogView;
    private Context context;

    public interface OnItemChangeListener {
        void onAddMaterialDialogSubmit(Material material);
    }

    /**
     * Used for adding a material.
     * @return the added material.
     */
    public static AddMaterialDialog newInstance() {
        return new AddMaterialDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Set the context.
        this.context = context;
        // Get the hosting fragment and ensure that it implements onItemChangeListener.
        try {
            onItemChangeListener = (OnItemChangeListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement AddMaterialDialog.onItemChangeListener"
            );
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Inflate the view
        dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.add_edit_material_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Add Material");
        // Set the OK button click listener that would update the selected material
        // with the changes from user input and send it to the onItemChangeListener.
        // In this case, the hosting fragment (MaterialListFragment).
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // UPDATE THE SELECTED MATERIAL WITH USER INPUT VALUES.
                EditText priceET = dialogView.findViewById(R.id.priceET);
                selectedMaterial.setPrice(Integer.valueOf(priceET.getText().toString()));
                // SEND THE NEW SELECTED MATERIAL TO THE LISTENER.
                onItemChangeListener.onAddMaterialDialogSubmit(selectedMaterial);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        // Initialize spinner.
        initSpinner();

        setDataToViews(dialogView, selectedMaterial);

        return dialogBuilder.create();
    }

    private void initSpinner() {
        // LOAD MAIN MATERIAL LIST INTO SPINNER.
        spinnerList = new MaterialListsDataSource(R.string.app_material_lists, context)
                .get(getString(R.string.main_material_list));
        // Get spinner view
        Spinner spinner = dialogView.findViewById(R.id.spinner);

        // MAKE SELECTED MATERIAL EQUAL TO THE FIRST ITEM IN THE LIST.
        selectedMaterial = (Material) spinnerList.getList().get(0);

        // Create an ArrayAdapter
        ArrayAdapter<Material> adapter = new ArrayAdapter<>(
                context,
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
                // GET THE SELECTED MATERIAL FROM THE SPINNER AND STORE IT
                // TO BE SENT WHEN EVER THE OK BUTTON IS INPUT.
                AddMaterialDialog.this.selectedMaterial = (Material) parent.getItemAtPosition(position);

                // update dialog views
                setDataToViews(dialogView, selectedMaterial);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setDataToViews(View view, Material selectedMaterial) {
        // Set data to views based on Material type.
        TextView nameTV = view.findViewById(R.id.nameET);
        nameTV.setText(selectedMaterial.getName());
        EditText priceET = view.findViewById(R.id.priceET);
        priceET.setText(String.valueOf(selectedMaterial.getPrice()));
        TextView lengthTV = view.findViewById(R.id.lengthET);
        lengthTV.setText(String.valueOf(selectedMaterial.getLength()));
        if (selectedMaterial instanceof DrywallScrew) {
            TextView widthTV = view.findViewById(R.id.widthET);
            widthTV.setText("n/a");
            TextView thicknessTV = view.findViewById(R.id.thicknessTV);
            thicknessTV.setText("n/a");
        } else if (selectedMaterial instanceof Stud) {
            Stud stud = (Stud) selectedMaterial;
            TextView widthTV = view.findViewById(R.id.widthET);
            widthTV.setText(String.valueOf(stud.getWidth()));
            TextView thicknessTV = view.findViewById(R.id.thicknessTV);
            thicknessTV.setText("n/a");
        } else if (selectedMaterial instanceof Board) {
            Board board = (Board) selectedMaterial;
            TextView widthTV = view.findViewById(R.id.widthET);
            widthTV.setText(String.valueOf(board.getWidth()));
            TextView thicknessTV = view.findViewById(R.id.thicknessTV);
            thicknessTV.setText(String.valueOf(board.getThickness()));
        }
    }

    public AddMaterialDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onItemChangeListener = null;
    }
}
