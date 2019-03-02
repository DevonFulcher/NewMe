package com.example.newme;

import android.content.Context;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;

import java.util.HashSet;
import java.util.Set;

public class MakeAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);
        Intent intent = getIntent();
        intent.getAction();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }


    private void hashData(){
        int first = R.id.first_name;
        int last = R.id.last_name;
        int email = R.id.email;
        int pin = R.id.pin;
        int hash = first + last + email + pin;


        int duration =Toast.LENGTH_LONG;

        Set bigChainDB = new HashSet<Integer>();


        Context context = getApplicationContext();
        CharSequence text = "Pin Doesn't Match";
        CharSequence acceptedText = "Pin Accepted";
        CharSequence userExists = "This user already has an account";
        Toast toast = Toast.makeText(context,text,duration);
        Toast accToast = Toast.makeText(context,acceptedText,duration);
        Toast existToast = Toast.makeText(context,userExists,duration);

        if(R.id.confirm_pin != pin){
            toast.show();
            hashData();
        }else{
            accToast.show();
        }

        if(bigChainDB.contains(hash)){
            existToast.show();
        }else{
            bigChainDB.add(hash);
        }


    }




}
