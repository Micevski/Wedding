package com.example.dell.weeding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PersonalInfo extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private List<String> cityList;
    private Spinner cityDropdown;
    private DatabaseReference reference;
    private FirebaseUser  currentUser;
    /*private Button citySubmitButton;*/
    private String selectedCity;
    private EditText nameText;
    private EditText surnameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_select);
        cityList = getCities();
        cityDropdown = findViewById(R.id.spinnerCity);
        cityDropdown.setOnItemSelectedListener(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        citySubmitButton = findViewById(R.id.citySelected);
        nameText = findViewById(R.id.name);
        surnameText = findViewById(R.id.surrname);


        cityList.add(0,"Grad");
        cityList.remove("userInfo");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PersonalInfo.this, android.R.layout.simple_spinner_item, cityList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cityDropdown.setAdapter(dataAdapter);

    }

    public List<String> getCities(){

        final List<String> listOfCities = new ArrayList<String>();
        FirebaseDatabase.getInstance().getReference().addValueEventListener(new ValueEventListener()  {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap: dataSnapshot.getChildren())
                {
                    listOfCities.add(snap.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return listOfCities;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
     selectedCity = cityList.get(i);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void btnCitySelectedOnClick(View v){
        reference = FirebaseDatabase.getInstance().getReference().child("userInfo");
        reference.child(currentUser.getUid()).child("city").setValue(selectedCity).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {


            }
        });
        UserProfileChangeRequest profilUpdates = new UserProfileChangeRequest.Builder().setDisplayName(surnameText.getText() + " " + nameText.getText()).build();
        currentUser.updateProfile(profilUpdates);
        Intent citySelected = new Intent(PersonalInfo.this, MainActivity.class);
        citySelected.putExtra("CITY", selectedCity);
        startActivity(citySelected);
    }
}
