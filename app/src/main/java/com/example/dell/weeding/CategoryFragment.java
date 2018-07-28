package com.example.dell.weeding;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryFragment extends Fragment {


    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private String category;
    private String city;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(String category, String city) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString("TYPE",category);
        args.putString("CITY",city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        recyclerView = rootView.findViewById(R.id.RecyclerView);
        this.category = this.getArguments().getString("TYPE");
        this.city = this.getArguments().getString("CITY");

        adapter = new RecycleViewAdapter(getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(Data item) {
                Intent itemDetailIntent = new Intent(getActivity(),ItemDetials.class);
                itemDetailIntent.putExtra("TYPE",category);
                itemDetailIntent.putExtra("ITEM",item);
                startActivity(itemDetailIntent);
            }
        });
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        onCategoryClickHandler();

        return rootView;
    }

    public void onCategoryClickHandler (){
        FirebaseDatabase.getInstance().getReference().child(city).child(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Data> temp = new ArrayList<Data>();
                List<String> images = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    images = (List<String>)snap.child("images").getValue();
                    Long id = (Long)snap.child("id").getValue();
                    List<Double> coordinates = (List<Double>)snap.child("coordinates").getValue();
                    Map<String, String> item = (Map<String, String>)snap.getValue();
                    Data data = new Data(id, item.get("name"), item.get("address"),
                            item.get("phone"),item.get("img"),
                            item.get("shortDescription"),images, coordinates);
                    temp.add(data);
                }
                adapter.setData(temp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
