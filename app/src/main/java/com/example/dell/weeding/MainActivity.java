package com.example.dell.weeding;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity  {

    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Intent intent;
    private String city;
    private TextView profilName;
    private Intent itemDetailIntent;
    private Button favoriteButton;


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.menuFavorite:
                startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_bar, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        recyclerView = findViewById(R.id.RecyclerView);
        mAuth = FirebaseAuth.getInstance();
        favoriteButton = findViewById(R.id.menuFavorite);

        currentUser = mAuth.getCurrentUser();
        intent = getIntent();
        city = intent.getStringExtra("city");
        Log.d("CITY", city);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionbar.setDisplayShowTitleEnabled(false);
        itemDetailIntent = new Intent(MainActivity.this, ItemDetials.class);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        int id = menuItem.getItemId();

                        if (id==R.id.homeStart){

                        }
                        else if (id == R.id.restorurant) {
                            onCategoryClickHandler("restaurants");
                            itemDetailIntent.putExtra("TYPE","restaurants");
                        } else if (id == R.id.photo) {
                            onCategoryClickHandler("photography");
                            itemDetailIntent.putExtra("TYPE","photography");
                        } else if (id == R.id.clothing) {
                            onCategoryClickHandler("clothing");
                            itemDetailIntent.putExtra("TYPE","clothing");
                        } else if (id == R.id.makeUp) {
                            onCategoryClickHandler("makeUp");
                            itemDetailIntent.putExtra("TYPE","makeUp");
                        } else if (id == R.id.music) {
                            onCategoryClickHandler("music");
                            itemDetailIntent.putExtra("TYPE","music");
                        } else if (id == R.id.cake) {
                            onCategoryClickHandler("cake");
                            itemDetailIntent.putExtra("TYPE","cake");
                        } else if (id==R.id.decoration){
                            onCategoryClickHandler("decoration");
                            itemDetailIntent.putExtra("TYPE","decoration");
                        }
                        else if (id==R.id.logOut){
                            mAuth.signOut();
                            startActivity(new Intent(MainActivity.this, LoginRegister.class));
                            finish();
                        }


                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);

        profilName = header.findViewById(R.id.nameOfUser);
        profilName.setText(currentUser.getDisplayName());
    }

    public void onCategoryClickHandler (String category){
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
        adapter = new RecycleViewAdapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(Data item) {
                itemDetailIntent.putExtra("ITEM",item);
                startActivity(itemDetailIntent);
            }
        });
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);


    }
}