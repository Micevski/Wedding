package com.example.dell.weeding;

import android.app.Fragment;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;

public class ItemDetials extends AppCompatActivity implements OnMapReadyCallback {

    public Slider slider;
    public List<Slide> slideList;
    public Data clickedItem;
    public TextView name;
    public TextView description;
    public TextView phone;
    public TextView address;
    private GoogleMap mMap;

    private FloatingActionButton fab;
    private DatabaseReference reference;
    private FirebaseUser currentUser;
    private String type;

    private List<Long> likesList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detials);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        slider = findViewById(R.id.slider);
        slideList = new ArrayList<>();
        Intent clickedItemIntent = getIntent();
        clickedItem = (Data) clickedItemIntent.getSerializableExtra("ITEM");
        type = clickedItemIntent.getStringExtra("TYPE");

        Toolbar toolbar = findViewById(R.id.detailsToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(clickedItem.getName());
        setSupportActionBar(toolbar);


        name = findViewById(R.id.clickedItemName);
        description = findViewById(R.id.description);
        phone = findViewById(R.id.clickedItemPhone);
        address = findViewById(R.id.clickedItemAddress);
        fab = findViewById(R.id.fabLike);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        name.setText(clickedItem.getName());
        description.setText(clickedItem.getShortDescription());
        phone.setText(clickedItem.getPhone());
        address.setText(clickedItem.getAddress());
        description.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_subject, 0, 0, 0);
        phone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_phone, 0, 0, 0);
        address.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_place, 0, 0, 0);

        List<Slide> slideList = new ArrayList<>();

        for (int i = 0; i < clickedItem.getImages().size(); i++) {
            slideList.add(new Slide(i, clickedItem.getImages().get(i), getResources().getDimensionPixelSize(ir.apend.sliderlibrary.R.dimen.slider_image_corner)));
        }

        slider.addSlides(slideList);

        //get user Likes of restaurants
        FirebaseDatabase.getInstance().getReference().child("userInfo").child(currentUser.getUid()).child("likes").child(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likesList = new ArrayList<>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    likesList.add((Long) snap.getValue());
                    // boolean liked = likeMap.values().contains(clickedItem.getId());
                }
                if (likesList.contains(clickedItem.getId())) {
                    fab.setImageResource(R.drawable.ic_favorite_black);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.onBackPressed();
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(clickedItem.getCoordinates().get(0), clickedItem.getCoordinates().get(1));
        mMap.setMinZoomPreference(10);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker at" + clickedItem.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void dial(View view) {
        Uri uri = Uri.parse("tel:" + clickedItem.getPhone());
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(dialIntent);
    }

    public void onLikeClike(View view) {
        if (!likesList.contains(clickedItem.getId())) {
            like();
        } else {
            unlike();
        }


    }

    public void unlike() {
        reference = FirebaseDatabase.getInstance().getReference().child("userInfo");
        reference.child(currentUser.getUid()).child("likes").child(type).child(clickedItem.getName()).removeValue();
        Toast.makeText(ItemDetials.this, "You remove this"+type+"from your list",Toast.LENGTH_LONG).show();
        fab.setImageResource(R.drawable.ic_favorite_empty);
    }

    public void like() {
        reference = FirebaseDatabase.getInstance().getReference().child("userInfo");
        reference.child(currentUser.getUid()).child("likes").child(type)
                .child(clickedItem.getName())
                .setValue(clickedItem.getId())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(ItemDetials.this, "You liked this " + type, Toast.LENGTH_LONG).show();
                        fab.setImageResource(R.drawable.ic_favorite_black);
                    }
                });
    }
}

