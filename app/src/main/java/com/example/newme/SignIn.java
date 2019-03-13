package com.example.newme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;

import java.util.Iterator;

public class SignIn extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        EditText pin = (EditText)findViewById(R.id.sign_in);
        Intent launch_profile_intent = new Intent(SignIn.this,ProfilePage.class);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String sharedPin = sharedPref.getString("Pin","");
        if(User.userSet.contains(sharedPin)){
            SignIn.this.startActivity(launch_profile_intent);

        }else{

        }



    }
}
