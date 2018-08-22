package com.jeffreyromero.liss.materialActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jeffreyromero.liss.calculators.MaterialCalculator;
import com.jeffreyromero.liss.dialogs.SingleInputDialog;
import com.jeffreyromero.liss.R;
import com.jeffreyromero.liss.dialogs.SingleSpinnerDialog;
import com.jeffreyromero.liss.data.DefaultMaterialLists;
import com.jeffreyromero.liss.data.MaterialDeserializer;
import com.jeffreyromero.liss.dialogs.ListFragmentDialog;
import com.jeffreyromero.liss.model.Material;
import com.jeffreyromero.liss.model.MaterialList;
import com.jeffreyromero.liss.repositories.MaterialListRepository;

/**
 * Receives a MaterialList and displays it.
 * Loads ListFragmentDialog for editing material list items.
 */
public class ListFragment extends Fragment implements
        ListFragmentDialog.OnItemChangeListener,
        SingleInputDialog.OnDialogSubmitListener,
        SingleSpinnerDialog.OnDialogSubmitListener {

//    private OnClickListener Listener;
    private RecyclerView.Adapter adapter;
    private int selectedPosition;
    private MaterialList mList;
    private String listName;
    private TextView titleTV;

    public ListFragment() {
        // Required empty public constructor
    }

    public static ListFragment newInstance(MaterialList mList) {
        ListFragment fragment = new ListFragment();
        //Serialize the material list.
        String json = new Gson().toJson(mList);
        //Add it to the bundle.
        Bundle args = new Bundle();
        args.putString("mList", json);
        //Set the bundle to the fragment and return it.
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        // Get the list from bundle and deserialize it.
        String json = getArguments().getString("mList");
        mList = MaterialDeserializer.toMaterialList(json);
        listName = mList.getName();
        // A new list is created without a name and with an empty arrayList.
        if (listName == null){

            listName = "New List";
            showSingleInputDialog("Add List Name", listName);

        }
        // Set the title.
        titleTV = view.findViewById(R.id.titleTV);
        titleTV.setText(listName);
        // Set a click listener that would open a dialog to edit the Title.
        titleTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                showSingleInputDialog("Edit List Name", listName);

                return false;
            }
        });
        if (mList.getList() != null) {
            // Get the recyclerView from the parent fragment view.
            RecyclerView rv = view.findViewById(R.id.recyclerView);
            // Instantiate the adaptor
            adapter = new ListAdapter();
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            // Set an item click listener that would load a dialog for editing the clicked Models
        }

        // Fab button to add new list item.
        FloatingActionButton fab = view.findViewById(R.id.add_new_material_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSingleSpinnerDialog();
            }
        });

        return view;
    }

    public void calculateQuantitiesByWidthLength(double roomLength, double roomWidth){
        mList = MaterialCalculator.calculateByWidthLength(mList, roomLength, roomWidth);
        adapter.notifyDataSetChanged();
    }

    public void calculateQuantitiesByArea(double area){
        mList = MaterialCalculator.calculateByArea(mList, area);
        adapter.notifyDataSetChanged();
    }

    //------------------------ SingleInputDialog -------------------------//

    private void showSingleInputDialog(String title, String input) {
        // Load SingleInputDialog giving it a title.
        SingleInputDialog f = SingleInputDialog.newInstance(title, input);
        // Set target fragment
        f.setTargetFragment(ListFragment.this, 0);
        // Show fragment
        f.show(getActivity().getSupportFragmentManager(), "SingleInputDialog");
    }

    public void OnSingleInputDialogSubmit(String userInput) {
        // Set the title of the list to the user input.
        titleTV.setText(userInput);
        // If there is a list, update it's name here and in SP.
        // Else do not store an empty list.
        if (mList.size() > 0 && userInput != null){
            // Rename the current mList.
            mList.setName(userInput);
            // Rename the current mList key in shared preferences
            new MaterialListRepository(getActivity()).renameKey(listName, userInput);
        }
    }

    //------------------------ SingleSpinnerDialog -------------------------//

    private void showSingleSpinnerDialog() {
        // Load SingleSpinnerDialog giving it a mList.
        // TODO Create appMaterialList. This list should contain all materials available to the app.
        MaterialList spinnerList = new DefaultMaterialLists(getActivity()).appMaterialList();
        SingleSpinnerDialog f = SingleSpinnerDialog.newInstance(spinnerList);
        // Set target fragment
        f.setTargetFragment(ListFragment.this, 0);
        // Show fragment
        f.show(getActivity().getSupportFragmentManager(), "SingleInputDialog");
    }

    @Override
    public void OnSingleSpinnerDialogSubmit(Material material) {
        // Add the name of the list.
        mList.setName((String)titleTV.getText());
        // Add the selected material to the list
        mList.add(material);
        adapter.notifyDataSetChanged();
        // Update list in shared preferences
        new MaterialListRepository(getActivity()).put(mList);
    }

    //--------------------------ListAdapter-------------------------------//

    public class ListAdapter extends RecyclerView.Adapter {

        @Override
        public int getItemCount() {
            return mList.size();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            // Get the inflater
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            // Inflate the item view layout
            View itemView = inflater.inflate(R.layout.item_view_2col, parent, false);
            return new itemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Material material = mList.get(position);
            itemViewHolder viewHolder = (itemViewHolder) holder;
            viewHolder.columnLeftTV.setText(material.toString());
            viewHolder.columnRightTV.setText(String.valueOf(material.getQuantity()));
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
                        // Load ListFragmentDialog.
                        // Get the clicked material, serialize it.
                        String material = new Gson().toJson(mList.get(getAdapterPosition()));
                        Bundle args = new Bundle();
                        args.putString("material", material);
                        ListFragmentDialog f = new ListFragmentDialog();
                        f.setArguments(args);
                        // Set the target fragment for this dialog.
                        f.setTargetFragment(ListFragment.this, 0);
                        f.show(getActivity().getSupportFragmentManager(), "ListFragmentDialog");

                        // Make the clicked position globally accessible
                        selectedPosition = getAdapterPosition();
                    }
                });
            }
        }
    }

    @Override
    public void onListFragmentDialogSubmit(Material selectedMaterial) {
        mList.set(selectedPosition, selectedMaterial);
        adapter.notifyDataSetChanged();
        // Update list in shared preferences
        new MaterialListRepository(getActivity()).put(mList);
    }

    //-------------------------- Listener for this class -------------------------------//

    public interface OnClickListener {
        void onListFragmentInteraction();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        try {
//            Listener = (OnClickListener) context;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(context.toString()
//                    + " must implement ListFragment callback methods");
//        }
    }


//    @Override
//    public void onDetach() {
//        super.onDetach();
//        Listener = null;
//    }


}
// Toast.makeText(getActivity(), "" + material, Toast.LENGTH_SHORT).show();
