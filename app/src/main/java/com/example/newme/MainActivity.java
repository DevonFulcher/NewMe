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
    Button scanButton;
    Button loginButton;
    Button profile;
    Button settings_button;
    Button voucher_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //access shared preferences here=====>>>>
        //Preference Managaer found here: https://stackoverflow.com/questions/5946135/difference-between-getdefaultsharedpreferences-and-getsharedpreferences
        //google docs: https://developer.android.com/training/data-storage/shared-preferences

        //if a user has registed an account then ask for login pin...
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        if(sharedPref.contains("UserData")){
//            Log.d("check","**********\n**************");
//            Intent checkUserIntent = new Intent(MainActivity.this,MakeAccount.class);
//            startActivity(checkUserIntent);
//        }else{
//            Intent signInIntent = new Intent(MainActivity.this,SignIn.class);
//            startActivity(signInIntent);
//        }

        if(User.userSet.isEmpty()){
            Intent checkUserIntent = new Intent(MainActivity.this,MakeAccount.class);
            startActivity(checkUserIntent);
        }else{
            Intent signInIntent = new Intent(MainActivity.this,SignIn.class);
            startActivity(signInIntent);
        }
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

        //Why are there two identical methods here?
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

    }
}

