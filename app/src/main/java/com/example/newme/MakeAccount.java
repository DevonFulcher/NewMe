package com.example.newme;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import android.content.Intent;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.bigchaindb.model.Account;
import com.bigchaindb.api.AccountApi;
import com.bigchaindb.model.GenericCallback;
import okhttp3.Response;
import com.bigchaindb.model.Asset;
import com.bigchaindb.model.FulFill;
import com.bigchaindb.model.MetaData;
import com.bigchaindb.model.Transaction;


public class MakeAccount extends AppCompatActivity {

    Bigchain bigchainDBApi = new Bigchain(this.handleServerResponse());
    private static final String TAG = "MakeAccountActivity";
    int SUCCESS_CODE = 1;
    PublicKey publicKey = null;
    PrivateKey privateKey = null;
    public static User user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //new Thread(Runnable){

        /**
         * shared preferences used to save data, call in main to check if there is data of a user.
         * https://developer.android.com/reference/android/content/Context.html#getSharedPreferences(java.lang.String,%20int)
         * name of the shared preferences file will be
         */
        SharedPreferences saveData = this.getSharedPreferences("com.example.newme.USER_DATA", 0);
        //SharedPreferences savedData = this.getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor userDataEditor = saveData.edit();
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
//                new Thread(new Runnable() {
//                    public void run() {

                TextInputLayout first = (TextInputLayout) findViewById(R.id.first_name);
                TextInputLayout last = (TextInputLayout) findViewById(R.id.last_name);
                EditText e = (EditText) findViewById(R.id.email);  //email
                EditText p = (EditText) findViewById(R.id.user_pin); //pin
                EditText cPin = (EditText) findViewById(R.id.confirm_pin);
                //EditText secret = (EditText) findViewById(R.id.secret_text);

                String UFirst = first.getEditText().getText().toString();
                String ULast = last.getEditText().getText().toString();
                String UEmail = e.getText().toString();
                String UPin = p.getText().toString();
                String confirmPin = cPin.getText().toString();
                //String userSecret = secret.getText().toString();

                user = new User(UFirst, ULast, UEmail, UPin, confirmPin);


                Context context = getApplicationContext();
                CharSequence acct_exists = "This account already exists";
                int duration = Toast.LENGTH_LONG;
                Toast acctToast = Toast.makeText(context, acct_exists, duration);

                //TODO: query bigchainDB to check if user's public key already exists
                //TODO: make user account https://github.com/bigchaindb/java-bigchaindb-driver/blob/master/src/main/java/com/bigchaindb/model/Account.java
                //Make user accounts
                userDataEditor.putString("FirstName", user.getFirstName());
                userDataEditor.putString("LastName", user.getLastName());
                userDataEditor.putString("Email", user.getEmail());
                userDataEditor.putString("Pin", user.getPin());

                /**
                 * There are two docs for creating accounts in different parts of the bigchaindb repo
                 * userAccountAPI allows the creation of the  accout,
                 *
                 * userAccount = new Account allows setting public and private keys as well as getting them.
                 *
                 * Creating a new account sets the public and private keys. Call api.account to retrieve the public and private keys. Do not have to manually make them.
                 */


                bigchainDBApi.setConfig();  //connect to our bigchaindb node?

                /**
                 * it seems you need to create an account, create a variable to the new account then call load account.
                 * Missing a method to send the account to the DB.
                 */

                AccountApi userAccountApi = new AccountApi();//com.bigchaindb.model.account
                Account accessUserAccount = userAccountApi.createAccount();


                userDataEditor.putString("PublicKey", accessUserAccount.getPublicKey().toString());
                userDataEditor.putString("PrivateKey", accessUserAccount.getPrivateKey().toString());
                userDataEditor.apply();
                //commmit to file^^^^^


                Asset userAsset = new Asset(user, User.class);
                try {
                    bigchainDBApi.sendTransaction(userAsset.getData() + userAsset.getId());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }



                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Log.d(TAG, "MEMES - " + SUCCESS_CODE);
                                while(SUCCESS_CODE == 1){
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d(TAG, "Still waiting with code - " + SUCCESS_CODE);
                                }

                            }
                        }, 3000);


                Intent intent = new Intent(MakeAccount.this, ProfilePage.class);
                MakeAccount.this.startActivity(intent); // startActivity allow you to Profile Page
                MakeAccount.this.finish();

            }
        });



    }



    private GenericCallback handleServerResponse() {
        //define callback methods to verify response from BigchainDBServer
        GenericCallback callback = new GenericCallback() {

            @Override
            public void transactionMalformed(Response response) {
                Log.d(TAG, "malformed " + response.message());
                onFailure();
            }

            @Override
            public void pushedSuccessfully(Response response) {
                Log.d(TAG, "pushedSuccessfully");
                onSuccess(response);
            }

            @Override
            public void otherError(Response response) {
                Log.d(TAG, "otherError" + response.message());
                onFailure();
            }
        };

        return callback;
    }

    private void onSuccess(Response response) {
        SUCCESS_CODE = 0;
        Log.d(TAG, "(*) Transaction successfully committed..");
        Log.d(TAG, response.toString());
    }

    private void onFailure() {
        SUCCESS_CODE = -1;
        Log.d(TAG, "Transaction failed");
        Log.d(TAG, "Transaction failed");
    }











}
