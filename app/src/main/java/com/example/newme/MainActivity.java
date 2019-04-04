package com.example.newme;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static TextView resultTV;
    public static TextView Name;
    public static TextView Email;
    Button scanButton;
    Button loginButton;
    Button profile;
    Button settings_button;
    Button voucher_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Name = (TextView)findViewById(R.id.Name);
        Email= (TextView)findViewById(R.id.Email);
        resultTV = (TextView)findViewById(R.id.tvResult);
        scanButton = (Button)findViewById(R.id.btnQRStart);
        loginButton = (Button)findViewById(R.id.login_button);
        profile = (Button)findViewById(R.id.profileButton);
        settings_button = (Button) findViewById(R.id.settingsButton);
        voucher_button = (Button) findViewById(R.id.vouchers);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent qrIntent = new Intent(MainActivity.this, QRCode.class);
                MainActivity.this.startActivity(qrIntent); // startActivity allow you to move
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
               MainActivity.this.startActivity(loginIntent);
           }
        });


        voucher_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent voucherIntent = new Intent(MainActivity.this, VoucherActivity.class);
                MainActivity.this.startActivity(voucherIntent);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this,ProfilePage.class);
                MainActivity.this.startActivity(profileIntent);
            }
        });

        if(checkSharedPreferences()){
            Intent signInIntent = new Intent(MainActivity.this,SignIn.class);
            startActivity(signInIntent);
            MainActivity.this.finish();
        }else{
            Log.d("check","Need Toast \n");
            Intent needToMakeAccountIntent = new Intent(MainActivity.this,MakeAccount.class);
            startActivity(needToMakeAccountIntent);
            MainActivity.this.finish();
        }


    }

    public boolean checkSharedPreferences(){
        //access shared preferences here=====>>>>
        //Preference Managaer found here:
        //  https://stackoverflow.com/questions/5946135/difference-between-getdefaultsharedpreferences-and-getsharedpreferences
        //google docs:
        //  https://developer.android.com/training/data-storage/shared-preferences
        //if a user has registered an account then ask for login pin...

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        if(sharedPref.contains("Pin") && sharedPref.contains("Email")){
            Log.d("check",sharedPref.getString("Pin",""));
            return true;
        }
        return false;
    }
}

