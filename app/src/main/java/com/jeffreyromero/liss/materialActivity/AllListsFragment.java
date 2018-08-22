package com.jeffreyromero.liss.materialActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.data.MaterialDeserializer;
import com.jeffreyromero.liss.interfaces.List;
import com.jeffreyromero.liss.model.MaterialList;
import com.jeffreyromero.liss.repositories.MaterialListRepository;
import com.jeffreyromero.liss.utilities.OnSwipeTouchListener;

import java.util.ArrayList;

/**
 * Receives a list of MaterialList and displays it.
 * Returns the clicked list item position to the Listener.
 * Informs the listener when the add new button is clicked.
 */
public class AllListsFragment extends Fragment {

    private ArrayList<MaterialList> allLists;
    private OnItemClickListener Listener;
    private AllListsAdapter adapter;
    private Context context;

    public AllListsFragment() {
        // Required empty public constructor
    }

    public interface OnItemClickListener {
        void onAllListsFragmentItemClick(int position);

        void onAllListsFragmentAddNewListButtonClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This ensures that the Listener has implemented
        // the callback interface and is registered to this class.
        try {
            Listener = (OnItemClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement AllListsFragment callback methods");
        }
    }

    public static AllListsFragment newInstance(ArrayList<MaterialList> AllLists) {
        AllListsFragment fragment = new AllListsFragment();
        //Serialize the material list.
        String json = new Gson().toJson(AllLists);
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("AllLists", json);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the context.
        context = getActivity();
        // Get AllLists from the bundle and deserialize it.
        String json = getArguments().getString("AllLists");
        allLists = MaterialDeserializer.toArrayListOfMaterialList(json);

        // Inflate the fragment layout
        final View view = inflater.inflate(R.layout.fragment_all_lists, container, false);
        // Set the title
        TextView titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText("ALL MATERIAL LISTS");
        // Get the recyclerView view
        RecyclerView rv = view.findViewById(R.id.recyclerView);
        // Instantiate the adapter and load the list
        adapter = new AllListsAdapter();
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(adapter);

        // Fab button to create new list.
        FloatingActionButton fab = view.findViewById(R.id.add_new_list_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Inform listener of button click.
                Listener.onAllListsFragmentAddNewListButtonClick();
            }
        });

        // Swipe and Dray functionality.
        SwipeAndDragHelper swipeAndDragHelper = new SwipeAndDragHelper();
        ItemTouchHelper touchHelper = new ItemTouchHelper(swipeAndDragHelper);
        touchHelper.attachToRecyclerView(rv);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Reload all the stored material lists.
        allLists = new MaterialListRepository(getActivity()).getAll();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Listener = null;
    }

    //---------------------------------------- adapter ------------------------------------------//

    public class AllListsAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return allLists.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(context);
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.item_view_2col, parent, false);
            return new itemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            List list = allLists.get(position);
            itemViewHolder viewHolder = (itemViewHolder) holder;
            viewHolder.columnLeftTV.setText(list.getName());
            viewHolder.columnRightTV.setText(list.getDateCreated());
        }


        private class itemViewHolder extends RecyclerView.ViewHolder {
            TextView columnLeftTV;
            TextView columnRightTV;

            itemViewHolder(final View itemView) {
                super(itemView);
                columnLeftTV = itemView.findViewById(R.id.columnLeftTV);
                columnRightTV = itemView.findViewById(R.id.columnRightTV);
                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // Pass clicked position
                        Listener.onAllListsFragmentItemClick(getAdapterPosition());
                    }

                });
            }
        }
    }

    //------------------------------------ SwipeAndDragHelper -----------------------------------//

    public class SwipeAndDragHelper extends ItemTouchHelper.Callback {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            int oldPosition = viewHolder.getAdapterPosition();
            int newPosition = target.getAdapterPosition();
            MaterialList material = allLists.get(oldPosition);
            allLists.remove(oldPosition);
            allLists.add(newPosition, material);
            adapter.notifyItemMoved(oldPosition, newPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //Get position of removed viewHolder
            int position = viewHolder.getAdapterPosition();
            //Get the material list
            MaterialList materialList = allLists.get(position);
            //Remove it from current list.
            allLists.remove(position);
            adapter.notifyItemRemoved(position);
            //Remove it from SP.
            new MaterialListRepository(context).remove(materialList.getName());
        }
    }

}
