package com.example.newme;


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfilePage  extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPref = getSharedPreferences("com.example.newme.USER_DATA",0 );
        setContentView(R.layout.profile_page);
        MainActivity.Name.setText(sharedPref.getString("FirstName",null) + " " + sharedPref.getString("LastName",null));
        MainActivity.Email.setText(sharedPref.getString("Email","a"));
    }
}
