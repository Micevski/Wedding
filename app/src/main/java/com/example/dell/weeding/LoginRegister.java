package com.example.dell.weeding;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginRegister extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        TextView textView = (TextView)findViewById(R.id.register);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread registerThred = new Thread(){
                    @Override
                    public void run() {
                        Intent regIntent = new Intent(getApplicationContext(),Register.class);
                        startActivity(regIntent);
                    }
                };
                registerThred.start();
            }
        });
        TextView textViewSkip = (TextView)findViewById(R.id.skip);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread skip = new Thread(){
                    @Override
                    public void run() {
                        Intent regIntent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(regIntent);
                    }
                };
                skip.start();
            }
        });
    }

}
