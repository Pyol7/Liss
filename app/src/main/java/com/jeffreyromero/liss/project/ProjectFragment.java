package com.jeffreyromero.liss.project;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.models.ProjectItem;
import com.jeffreyromero.liss.repositories.ProjectsDataSource;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProjectFragment extends Fragment {

    private static final String PROJECT = "project";
    private OnItemClickListener mListener;
    private RecyclerView.Adapter adapter;
    private Project project;

    public ProjectFragment() {
        // Required empty public constructor
    }

    public static ProjectFragment newInstance(Project project) {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        String json = new Gson().toJson(project);
        args.putString(PROJECT, json);
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnItemClickListener {
        void onProjectFragmentItemClick(ProjectItem projectItem);
        void onAddProjectItemBtnClick(Project project);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ProjectFragment.OnItemClickListener) {
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProjectFragment.OnItemClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set all fields.
        if (getArguments() != null) {
            String json = getArguments().getString(PROJECT);
            project = Deserializer.toProject(json);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_project, container, false);

        // Set text views
        TextView projectTV = view.findViewById(R.id.projectTV);
        projectTV.setText(project.getName());
        TextView dateTV = view.findViewById(R.id.dateCreatedTV);
        dateTV.setText(project.getDateCreated());

        // Get the recyclerView from the parent fragment view.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adaptor
        adapter = new ProjectFragment.RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public void replaceProject(Project newProject){
        project = newProject;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Programmatically create the add (+) menu item
        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {

            //Load CreateProjectItemFragment.
            mListener.onAddProjectItemBtnClick(project);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //------------------------------- Adapter -------------------------------//

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return project.getProjectItems().size();
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
            ProjectItem item = project.getProjectItems().get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.columnLeftTV.setText(item.getName());
            viewHolder.columnRightTV.setText("cost");
        }

        private class ItemViewHolder extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            ItemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Inform mListener to load ProjectFragment.
                        mListener.onProjectFragmentItemClick(
                                project.getProjectItems().get(getAdapterPosition()));

                    }
                });
            }
        }
    }

}

//Toast.makeText(getActivity(), "resumed", Toast.LENGTH_SHORT).show();
