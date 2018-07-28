package com.example.dell.weeding;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String CITY_PARAM = "CITY";

    private String city;
    private RecyclerView restaurants;
    private RecyclerView photography;
    private RecyclerView clothing;
    private RecyclerView makeUp;
    private RecyclerView music;
    private RecyclerView cake;
    private RecyclerView decoration;


    public HomeFragment() {}

    public static HomeFragment newInstance(String city) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(CITY_PARAM, city);
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
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        this.city = getArguments().getString(CITY_PARAM);
        this.restaurants = rootView.findViewById(R.id.restaurantsView);
        this.photography = rootView.findViewById(R.id.photgraphyView);
        this.clothing = rootView.findViewById(R.id.clothingView);
        this.makeUp = rootView.findViewById(R.id.makeUpView);
        this.music = rootView.findViewById(R.id.musicView);
        this.cake = rootView.findViewById(R.id.cakeView);
        this.decoration = rootView.findViewById(R.id.decorationView);

        setUpAdapter(restaurants,"restaurants");
        setUpAdapter(photography,"photography");
        setUpAdapter(clothing,"clothing");
        setUpAdapter(makeUp,"makeUp");
        setUpAdapter(music,"music");
        setUpAdapter(cake,"cake");
        setUpAdapter(decoration,"decoration");
        return rootView;
    }

    public void onCategoryClickHandler (String category, final HomeRecyclerViewAdapter  adapter){
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

    public void setUpAdapter(RecyclerView recyclerView, final String category){

        HomeRecyclerViewAdapter adapter;
        adapter = new HomeRecyclerViewAdapter(getActivity(), new OnItemClickListener() {
            @Override
            public void onItemClick(Data item) {
                Intent itemDetailIntent = new Intent(getActivity(),ItemDetials.class);
                itemDetailIntent.putExtra("TYPE",category);
                itemDetailIntent.putExtra("ITEM",item);
                startActivity(itemDetailIntent);
            }
        });
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        onCategoryClickHandler(category, adapter);

    }

}
