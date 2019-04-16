package com.example.newme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Iterator;

public class SignIn extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        final Button create_button = findViewById(R.id.confirm_button);
        final Button makeAcct_button = findViewById(R.id.create_account);

        create_button.setOnClickListener(new View.OnClickListener() {
            SharedPreferences sharedPref = getSharedPreferences("com.example.newme.USER_DATA",0 );
            @Override
            public void onClick(View v) {
                EditText pin = (EditText)findViewById(R.id.sign_in);
                EditText email = findViewById(R.id.email_text);
                Log.d("logPin",pin.getText().toString());
                //if email and pin are on record
                //if(MakeAccount.user.getEmail().equals(email.toString()) && MakeAccount.user.getPin().equals(pin.toString())){
                if(sharedPref.getString("Pin","NULL").equals(pin.getText().toString()) && sharedPref.getString("Email", "NULL").equals(email.getText().toString())){
                    Intent intent = new Intent(SignIn.this, ProfilePage.class);
                    SignIn.this.startActivity(intent);
                }else Log.d("progress", sharedPref.getString("Name","try"));
            }
        });

        makeAcct_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, MakeAccount.class);
                SignIn.this.startActivity(intent);
            }
        });

        //SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);





    }


}
