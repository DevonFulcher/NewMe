package com.example.newme;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    //https://developer.android.com/training/basics/firstapp/starting-activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button button = findViewById(R.id.make_acct_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                /*
                 * Intent is just like glue which helps to navigate one activity
                 * to another.
                 */Intent intent = new Intent(LoginActivity.this,
                        MakeAccount.class);
                   LoginActivity.this.startActivity(intent); // startActivity allow you to move
            }
        });


    }

    private void signIn(){
        System.out.println("get user sign in information");
        //get user information from user class?
        //Query bigchainDB.
    }

    private void makeAcct(){

        final Button button = findViewById(R.id.make_acct_page);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //have user enter user data and then query bigChain to check if this person is real...
                //enter username and pin...
                //Code here executes on main thread after user presses button
                //after button is clicked then run some code to see if a person has an account.
                startActivity(new Intent(LoginActivity.this,MakeAccount.class));


            }
        });
        System.out.println("Make an account!!!!");
        //launch the make account activity
//        Intent intent = new Intent(this,MakeAccoun t.class);
//        startActivity(intent);
        //will launch the make account activity.
    }

//    private boolean signOut(){
//        //once signed out, user will have to manually sign in again
//        //return true for signout
//        Login.signInCheck = true;
//        return true;
//
//    }


}
