package com.jeffreyromero.liss.project;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.models.Project;
import com.jeffreyromero.liss.models.ProjectItem;
import com.jeffreyromero.liss.repositories.ProjectsDataSource;

import java.util.ArrayList;

public class ProjectActivity extends AppCompatActivity implements
        ProjectsFragment.OnItemClickListener,
        ProjectFragment.OnItemClickListener,
        CreateProjectItemFragment.OnCreationListener {

    private ArrayList<Project> projects;
    private ProjectsDataSource pds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Display all lists.
        if (findViewById(R.id.fragment_container) != null) {
            // To avoid overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            ProjectsFragment f = ProjectsFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadProjectFragment(Project project) {
        //Load ProjectFragment.
        if (findViewById(R.id.fragment_container) != null) {
            ProjectFragment f = ProjectFragment.newInstance(project);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onProjectFragmentItemClick(ProjectItem projectItem) {
        //Show Project Item
        if (findViewById(R.id.fragment_container) != null) {
            ProjectItemFragment f = ProjectItemFragment.newInstance(projectItem);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onAddProjectItemBtnClick(Project project) {
        //Load CreateProjectItemFragment
        if (findViewById(R.id.fragment_container) != null) {
            CreateProjectItemFragment f = CreateProjectItemFragment.newInstance(project);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, f, f.getClass().getSimpleName());
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onProjectItemAdded(Project updatedProject) {
        FragmentManager fm = getSupportFragmentManager();
        //To avoid creating a new instance, get the existing fragment form the stack.
        ProjectFragment pf = (ProjectFragment) fm.findFragmentByTag("ProjectFragment");
        //Call a custom method on it to update it's data.
        pf.replaceProject(updatedProject);
        //Remove CreateProjectItemFragment from the top and give pf the focus.
        fm.popBackStackImmediate();
    }
}
