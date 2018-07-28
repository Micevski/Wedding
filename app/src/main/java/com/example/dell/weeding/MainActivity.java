package com.example.dell.weeding;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
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


public class MainActivity extends AppCompatActivity {


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

                        Intent type = new Intent(MainActivity.this, CategoryFragment.class);

                        if (id == R.id.homeStart) {
                            setHomeFragment();
                        } else if (id == R.id.restorurant) {
                            setFragment("restaurants");
                        } else if (id == R.id.photo) {
                            setFragment("photography");
                        } else if (id == R.id.clothing) {
                            setFragment("clothing");
                        } else if (id == R.id.makeUp) {
                            setFragment("makeUp");
                        } else if (id == R.id.music) {
                            setFragment("music");
                        } else if (id == R.id.cake) {
                            setFragment("cake");
                        } else if (id == R.id.decoration) {
                            setFragment("decoration");
                        } else if (id == R.id.logOut) {
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

        navigationView.getMenu().getItem(0).setChecked(true);
        setHomeFragment();
    }


    public void setFragment(String category) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CategoryFragment categoryFragment = CategoryFragment.newInstance(category, city);
        fragmentTransaction.replace(R.id.categoryFragment, categoryFragment);
        fragmentTransaction.commit();
    }

    public void setHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = HomeFragment.newInstance(city);
        fragmentTransaction.replace(R.id.categoryFragment, homeFragment);
        fragmentTransaction.commit();
    }
}