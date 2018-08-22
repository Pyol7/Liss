package com.jeffreyromero.liss.dialogs;

import android.app.Dialog;
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

import com.google.gson.Gson;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.MaterialDeserializer;
import com.jeffreyromero.liss.model.Material;
import com.jeffreyromero.liss.model.MaterialList;

import java.util.ArrayList;

/**
 * Displays a single spinner dialog.
 * @newInstance(param) spinner entities list.
 * @return (entity) the selected entity.
 */
public class SingleSpinnerDialog extends DialogFragment {
    private OnDialogSubmitListener Listener;
    private Material material;

    public static SingleSpinnerDialog newInstance(MaterialList mList) {
        SingleSpinnerDialog fragment = new SingleSpinnerDialog();
        //Add it to the bundle.
        Bundle args = new Bundle();
        String json = new Gson().toJson(mList);
        args.putString("mList", json);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDialogSubmitListener {
        void OnSingleSpinnerDialogSubmit(Material material);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the hosting fragment and ensure that it implements Listener.
        try {
            Listener = (OnDialogSubmitListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting Fragment must implement " +
                            "SingleInputDialog.OnDialogSubmitListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.single_spinner_dialog, null);
        // Get the list from the bundle.
        String json = getArguments().getString("mList");
        // Deserialize it
        final MaterialList mList = MaterialDeserializer.toMaterialList(json);

        // Get spinner view and populate it with the passed in list
        Spinner spinner = dialogView.findViewById(R.id.newMaterialSpinner);
        // Get material list names to populate the spinner
        ArrayList<String> mListNames = mList.getNames();
        // Create an ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                mListNames
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
                material = mList.get(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle("Add new Material");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Pass the material to the calling fragment
                Listener.OnSingleSpinnerDialogSubmit(material);

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

    public SingleSpinnerDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener = null;
    }
}
