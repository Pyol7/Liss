package com.jeffreyromero.liss.project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Project;

/**
 * Displays a dialog to create a new project and passes
 * the name and description back to the hosting fragment.
 */
public class AddProjectDialog extends DialogFragment {

    private OnCreateListener mListener;

    public AddProjectDialog() {
        // Required empty public constructor
    }

    public static AddProjectDialog newInstance() {
        return new AddProjectDialog();
    }

    public interface OnCreateListener {
        void onAddProjectDialogSubmit(String name, String description);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Ensure that the host implements the callbacks.
        try {
            mListener = (OnCreateListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(
                    "The hosting fragment must implement AddProjectDialog.OnCreateListener"
            );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the view
        final View dialogView = getActivity().getLayoutInflater()
                .inflate(R.layout.add_project_dialog, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Create New Project");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Get user input.
                EditText nameET = dialogView.findViewById(R.id.nameET);
                EditText descriptionET = dialogView.findViewById(R.id.descriptionET);
                String name = nameET.getText().toString();
                String description = descriptionET.getText().toString();

                //Inform the listener of the new project created.
                mListener.onAddProjectDialogSubmit(name, description);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //
            }
        });

        return dialogBuilder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
