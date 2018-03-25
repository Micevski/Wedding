package com.example.dell.weeding;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private EditText txtEmail;
    private EditText txtPassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtEmail = findViewById(R.id.emailAddres);
        txtPassword = findViewById(R.id.passwordReg);
        mAuth = FirebaseAuth.getInstance();
    }

    public void btnRegister_OnClick(View V){
        (mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString(), txtPassword.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Регистрација е успешна", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Register.this, LoginRegister.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Register.this, "Се случи грешка ве молиме обидете се подоцна", Toast.LENGTH_LONG).show();

                        }
                    }
                });

    }
}
