package com.jeffreyromero.liss.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jeffreyromero.liss.R;

/**
 * Displays a single input dialog.
 * @newInstance(param) title of the dialog.
 * @return (String) one user input.
 */
public class SingleInputDialog extends DialogFragment {
    public OnDialogSubmitListener Listener;

    public static SingleInputDialog newInstance(String title, String input) {
        SingleInputDialog fragment = new SingleInputDialog();
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("input", input);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnDialogSubmitListener {
        void OnSingleInputDialogSubmit(String userInput);
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
        final View dialogView = inflater.inflate(R.layout.single_input_dialog, null);
        // Get the title and input from the bundle.
        String title = getArguments().getString("title");
        String input = getArguments().getString("input");
        // Get the EditText input.
        final EditText inputET = dialogView.findViewById(R.id.inputET);
        // Set the input value.
        inputET.setText(input);
        //Build dialog
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setView(dialogView);
        dialog.setTitle(title);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Get user input
                String userInput = inputET.getText().toString();
                // Pass userInput to parent fragment.
                Listener.OnSingleInputDialogSubmit(userInput);

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

    public SingleInputDialog() {
        // Required empty public constructor
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener = null;
    }
}
