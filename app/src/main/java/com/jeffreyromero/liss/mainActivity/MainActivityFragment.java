package com.jeffreyromero.liss.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.calculators.CalculatorActivity;
import com.jeffreyromero.liss.materialActivity.MaterialActivity;

/**
 * Landing page for the app and provides a clear over view of it's capabilities.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);

        // Calculator activity button
        view.findViewById(R.id.load_calculator_activity_btn)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getActivity(), CalculatorActivity.class);
                startActivity(Intent);
            }
        });

        // Material activity button
        view.findViewById(R.id.load_material_activity_btn)
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(getActivity(), MaterialActivity.class);
                startActivity(Intent);
            }
        });

        return view;
    }

}
