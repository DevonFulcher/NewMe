package com.example.newme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MakeAccount extends AppCompatActivity {
    private static User user;
    private static Set bigchainDB = new HashSet<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * shared preferences used to save data, call in main to check if there is data of a user.
         * Fucntionality to add a user?
         *
         */

        SharedPreferences savedData = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor userDataEditor = savedData.edit();
        //a sharedPreferences file that will allow us to save user data


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_account);
        Intent intent = getIntent();
        intent.getAction();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Button button = findViewById(R.id.create_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence acct_exists = "This account already exists";
                int duration =Toast.LENGTH_LONG;
                Toast acctToast = Toast.makeText(context,acct_exists,duration);

                if(hashData().equals(null)){
                    acctToast.show();//this account already exists

                }else{
                    Set tempSet = new HashSet<String>();
                    tempSet.add(user.getFirstName());
                    tempSet.add(user.getLastName());
                    tempSet.add(user.getEmail());
                    tempSet.add(user.getPin());
                    tempSet.add(user.getHash());
                    //put a stringSet into the sharedPreference
                    userDataEditor.putString("FirstName",user.getFirstName());
                    userDataEditor.putString("LastName",user.getLastName());
                    userDataEditor.putString("Email",user.getEmail());
                    userDataEditor.putString("Pin",user.getPin());
                    userDataEditor.apply();
                    //commmit to file ^^^^
                    Intent intent = new Intent(MakeAccount.this, ProfilePage.class);
                    MakeAccount.this.startActivity(intent); // startActivity allow you to Profile Page
                    MakeAccount.this.finish();
                }

            }
        });


    }

    private boolean makeAcct(){
        return true;
    }


    private String hashData(){

        TextInputLayout first = (TextInputLayout) findViewById(R.id.first_name);
        TextInputLayout last = (TextInputLayout)findViewById(R.id.last_name);
        EditText e = (EditText)findViewById(R.id.email);  //email
        EditText p = (EditText) findViewById(R.id.user_pin); //pin
        EditText cPin = (EditText) findViewById(R.id.confirm_pin);

        String UFirst = first.getEditText().getText().toString();
        String ULast = last.getEditText().getText().toString();
        String UEmail = e.getText().toString();
        String UPin = p.getText().toString();
        String confirmPin = cPin.getText().toString();




        Log.d("FName",UFirst);
        int duration =Toast.LENGTH_LONG;
        Context context = getApplicationContext();
        CharSequence text = "Pin Doesn't Match";
        CharSequence acceptedText = "Pin Accepted";
        CharSequence userExists = "This user already has an account";
        CharSequence added = "Added user!";
        Toast added_to_DB = Toast.makeText(context,added,duration);
        Toast toast = Toast.makeText(context,text,duration);
        Toast accToast = Toast.makeText(context,acceptedText,duration);
        Toast existToast = Toast.makeText(context,userExists,duration);

        if(!(UPin.equals(confirmPin))){
            toast.show();

        }else{
            accToast.show();
        }

        String to_hash = UFirst+ ULast + UEmail + UPin;
        String cp_hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("Sha-256");
            byte[] user_hash = md.digest(to_hash.getBytes());
            BigInteger big_num = new BigInteger(1, user_hash);
            String hashed_val = big_num.toString(16);

            while (hashed_val.length() < 32) {
                hashed_val = "O" + hashed_val;
            }
            if(MakeAccount.this.bigchainDB.contains(hashed_val)){
                existToast.show();
                return null;
            }
            else{
                added_to_DB.show(); //toast
            }
            cp_hash = hashed_val;
            user = new User(UFirst,ULast,UEmail,UPin,cp_hash);
            //add user to set... now add user to sharedPreferences
            User.userSet.add(user);



        }catch (NoSuchAlgorithmException al){
            System.out.println("rip" + al);
            return null;
        }
        Log.d("hash",cp_hash);
        return cp_hash;




    }




}
