package com.jeffreyromero.liss.project;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.Material;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.models.ProjectItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectItemFragment extends Fragment {

    private static final String PROJECT_ITEM = "projectItem";
    private ProjectItem projectItem;


    public ProjectItemFragment() {
        // Required empty public constructor
    }

    public static ProjectItemFragment newInstance(ProjectItem projectItem) {
        ProjectItemFragment fragment = new ProjectItemFragment();
        Bundle args = new Bundle();
        String json = new Gson().toJson(projectItem);
        args.putString(PROJECT_ITEM, json);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set all fields.
        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT_ITEM);
            projectItem = Deserializer.toProjectItem(json);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project_item, container, false);

        //Get views and set values.
        TextView nameTV = view.findViewById(R.id.nameTV);
        TextView lengthTV = view.findViewById(R.id.lengthTV);
        TextView widthTV = view.findViewById(R.id.widthTV);
        nameTV.setText(projectItem.getName());
        lengthTV.setText(String.valueOf(projectItem.getLength()));
        widthTV.setText(String.valueOf(projectItem.getWidth()));

        //Setup recyclerView
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adaptor
        RecyclerViewAdapter adapter = new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return projectItem.getMaterialList().size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.item_view_2col, parent, false);
            return new ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Material material = projectItem.getMaterialList().get(position);
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
