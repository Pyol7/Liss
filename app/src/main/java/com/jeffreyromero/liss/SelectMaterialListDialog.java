package com.jeffreyromero.liss;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Displays the list of user material lists.
 */
public class SelectMaterialListDialog extends DialogFragment {

    private OnDialogSubmitListener Listener;
    private int selectedPosition;

    public static SelectMaterialListDialog newInstance(ArrayList<String> MaterialListsNames) {
        SelectMaterialListDialog f = new SelectMaterialListDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("MaterialListsNames", MaterialListsNames);
        f.setArguments(args);
        return f;
    }

    public interface OnDialogSubmitListener {
        void onSelectMaterialListDialogSubmit(int selectedPosition);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Ensure that the host implements this interface.
        try {
            Listener = (OnDialogSubmitListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The host must implement " +
                            "SelectMaterialListDialog.OnDialogSubmitListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Get MaterialListsNames from the bundle.
        final ArrayList<String> MaterialListsNames = getArguments()
                .getStringArrayList("MaterialListsNames");

        // Inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.single_spinner_dialog, null);

        // Get spinner view and populate it.
        Spinner spinner = dialogView.findViewById(R.id.listsSpinner);
        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                MaterialListsNames
        );
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        // Set a selection listener that would update the dialog based on the selected item.
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Get the selected materialList
                selectedPosition = position;

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle("Select a Material List");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Send the selected list to the listener.
                Listener.onSelectMaterialListDialogSubmit(selectedPosition);

            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });
        return dialog.create();
    }

    public SelectMaterialListDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener = null;
    }
}
