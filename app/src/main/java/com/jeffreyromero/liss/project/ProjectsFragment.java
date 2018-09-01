package com.jeffreyromero.liss.project;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.Deserializer;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.repositories.ProjectsDataSource;

import java.util.ArrayList;

public class ProjectsFragment extends Fragment implements
        AddProjectDialog.OnCreateListener {

    private ProjectsDataSource projectsDataSource;
    private OnItemClickListener mListener;
    private ArrayList<Project> projects;
    private RecyclerViewAdapter adapter;

    public ProjectsFragment() {
        // Required empty public constructor
    }

    public static ProjectsFragment newInstance() {
        return new ProjectsFragment();
    }

    public interface OnItemClickListener {
        void loadProjectFragment(Project project);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemClickListener) {
            mListener = (OnItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ProjectsFragment.OnItemClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the list of projects
        projectsDataSource = new ProjectsDataSource(R.string.projects, getActivity());
        projects = projectsDataSource.getAll();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Show options menu.
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_projects, container, false);
        // Set the title
        TextView titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText("PROJECTS");
        // Get the recyclerView from the parent fragment view.
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adaptor
        adapter = new RecyclerViewAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Create the add menu item
        MenuItem item = menu.add(Menu.NONE, R.id.action_add, 10, R.string.action_add);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_add_white_24dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            //Load AddProjectDialog.
            AddProjectDialog f = AddProjectDialog.newInstance();
            f.setTargetFragment(ProjectsFragment.this, 0);
            f.show(getActivity().getSupportFragmentManager(), f.getClass().getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        projects = projectsDataSource.getAll();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAddProjectDialogSubmit(String name, String description) {
        Project project = new Project(name);
        project.setDescription(description);
        projects.add(project);
        adapter.notifyDataSetChanged();
        projectsDataSource.put(project);

        //Load ProjectFragment
        mListener.loadProjectFragment(project);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //------------------------------- Adapter -------------------------------//

    public class RecyclerViewAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return projects.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.item_view_2col, parent, false);
            return new ProjectsFragment.RecyclerViewAdapter.ItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Project project = projects.get(position);
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            viewHolder.columnLeftTV.setText(project.getName());
            viewHolder.columnRightTV.setText(String.valueOf(project.getDateCreated()));
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
                        mListener.loadProjectFragment(projects.get(getAdapterPosition()));

                    }
                });
            }
        }
    }

}
