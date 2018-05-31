package com.example.dell.weeding;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class SplashAct extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Check for internet connection
        if (!checkForConnection()) {
            new AlertDialog.Builder(this)
                    .setTitle("Немате интернет конекција")
                    .setMessage("Има проблеми со интернет конекцијата обидете се повтроно")
                    .setPositiveButton("Обиди се повторно", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(SplashAct.this, SplashAct.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Откажи", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            System.exit(0);
                        }
                    }).show();
        }

//
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intentReg = new Intent(SplashAct.this, LoginRegister.class);


        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference().child("userInfo").child(currentUser.getUid()).child("city").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Object city = dataSnapshot.getValue();
                    if (city != null) {
                        Intent intent = new Intent(SplashAct.this, MainActivity.class);
                        intent.putExtra("city", city.toString());
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(SplashAct.this, PersonalInfo.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else
            startActivity(intentReg);
    }

    public boolean checkForConnection() {
        try {
            Process ipProcess = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
            return (ipProcess.waitFor() == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
