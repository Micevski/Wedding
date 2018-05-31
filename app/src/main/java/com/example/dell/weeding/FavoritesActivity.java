package com.example.dell.weeding;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Debug;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class FavoritesActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private RecycleViewAdapter adapter;
    private RecyclerView recyclerView;
    private Intent itemDetailIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        itemDetailIntent = new Intent(FavoritesActivity.this, ItemDetials.class);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorites, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        RecyclerView recyclerView;
        RecycleViewAdapter recycleViewAdapter;
        RecyclerView.LayoutManager layoutManager;
        List<Data> likesData;
        Intent itemDetailIntent;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater,
                                 final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
            recyclerView = rootView.findViewById(R.id.RecyclerViewFragment);
            layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);

            if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
                getUserLikes("restaurants");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
                getUserLikes("photography");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 3) {
                getUserLikes("clothing");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 4) {
                getUserLikes("makeUp");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 5) {
                getUserLikes("music");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 6) {
                getUserLikes("cake");
            } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 7) {
                getUserLikes("decoration");
            }
            return rootView;
        }

        public void getUserLikes(final String category) {
            FirebaseDatabase.getInstance().getReference().child("Kochani").child(category).addValueEventListener(new ValueEventListener() {
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
                    recycleViewAdapter.setData(temp);


                    //get user likes
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    FirebaseDatabase.getInstance().getReference().child("userInfo").child(currentUser.getUid()).child("likes").child(category).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<Long> userLikesIndex = new ArrayList<>();
                            for (DataSnapshot snap : dataSnapshot.getChildren()){
                                userLikesIndex.add((Long) snap.getValue());
                            }
                            List<Data> finalList = recycleViewAdapter.getData();
                            final List<Data> filtered = new ArrayList<>();
                            for (Data d : finalList){
                                if(userLikesIndex.contains(d.getId()))
                                    filtered.add(d);
                            }
                            recycleViewAdapter.setData(filtered);
                            recycleViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //finish geting user likes
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            recycleViewAdapter = new RecycleViewAdapter(this.getContext(), new OnItemClickListener() {
                @Override
                public void onItemClick(Data item) {
                    Log.d("Kur","ebi se");
                    Intent intent = new Intent(getContext(), ItemDetials.class);
                    intent.putExtra("ITEM",item);
                    intent.putExtra("TYPE",category);
                    startActivity(intent);
                }
            });

            RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(recycleViewAdapter);

        }

    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 7 total pages.
            return 7;
        }
    }
}
