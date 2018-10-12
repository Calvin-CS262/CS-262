package com.example.lukeadmin.loginpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void return_to_login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
