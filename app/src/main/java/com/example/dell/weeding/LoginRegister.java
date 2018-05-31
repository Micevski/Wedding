package com.example.dell.weeding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginRegister extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth mAuht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        mAuht = FirebaseAuth.getInstance();


    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView textViewReg = (TextView) findViewById(R.id.register);
        textViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread registerThred = new Thread() {
                    @Override
                    public void run() {
                        Intent regIntent = new Intent(getApplicationContext(), Register.class);
                        startActivity(regIntent);
                    }
                };
                registerThred.start();
            }
        });
        TextView textViewSkip = (TextView) findViewById(R.id.skip);
        textViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(regIntent);
            }
        });
    }

    public void btnLogIn_OnClick(View v) {
        (mAuht.signInWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginRegister.this, "Највата е успешна", Toast.LENGTH_LONG).show();
                            //checkFirstTimeFella(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            Intent intent = new Intent(LoginRegister.this, SplashAct.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginRegister.this, "Внесовте погрешн емаил или лозинка", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

}
