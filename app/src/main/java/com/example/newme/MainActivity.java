package com.example.newme;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        //Intent intent = new Intent(MainActivity.this, SignIn.class);
        //MainActivity.this.startActivity(intent);

    }

}