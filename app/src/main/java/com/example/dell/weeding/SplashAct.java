package com.example.dell.weeding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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

        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent intentReg = new Intent(SplashAct.this,LoginRegister.class);

        if(currentUser !=null){
            Intent intent = new Intent(SplashAct.this,MainActivity.class);
            startActivity(intent);
        }
        else
            startActivity(intentReg);
    }
}
