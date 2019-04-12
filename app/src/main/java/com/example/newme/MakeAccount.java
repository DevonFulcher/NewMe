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
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;
import java.math.BigInteger;
import java.security.MessageDigest;
import com.bigchaindb.model.Account;
import com.bigchaindb.api.AccountApi;
import com.bigchaindb.model.GenericCallback;

import okhttp3.Response;

import static com.bigchaindb.api.AccountApi.loadAccount;


public class MakeAccount extends AppCompatActivity {
    public static User user;
    Bigchain bigchainDBApi = new Bigchain(this.handleServerResponse());
    private static final String TAG = "MakeAccountActivity";
    int SUCCESS_CODE = 1;
    PublicKey publicKey = null;
    PrivateKey privateKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /**
         * shared preferences used to save data, call in main to check if there is data of a user.
         * https://developer.android.com/reference/android/content/Context.html#getSharedPreferences(java.lang.String,%20int)
         * name of the shared preferences file will be
         */
        SharedPreferences saveData = this.getSharedPreferences("com.example.newme.USER_DATA",0);
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
                Context context = getApplicationContext();
                CharSequence acct_exists = "This account already exists";
                int duration =Toast.LENGTH_LONG;
                Toast acctToast = Toast.makeText(context,acct_exists,duration);

                //TODO: change to query bigchainDB to check if user's public key already exists
                if(hashData("CHANGE").equals(null)){
                    acctToast.show();//this account already exists

                }else{
                    //Add Strings to the defaultSharedPreferences file
                    //use key to get info
                    //TODO: make user account https://github.com/bigchaindb/java-bigchaindb-driver/blob/master/src/main/java/com/bigchaindb/model/Account.java
                    //Make user accounts
                    userDataEditor.putString("FirstName",user.getFirstName());
                    userDataEditor.putString("LastName",user.getLastName());
                    userDataEditor.putString("Email",user.getEmail());
                    userDataEditor.putString("Pin",user.getPin());
                    userDataEditor.apply();
                    //commmit to file ^^^^


                    /**
                     * There are two docs for creating accounts in different parts of the repo
                     * userAccountAPI allows the creation of the  accout,
                     *
                     * userAccount = new Account allows setting public and private keys as well as getting them.
                     */

                    AccountApi userAccountApi = new AccountApi();//com.bigchaindb.model.account/unt();
                    userAccountApi.createAccount();
                    Account userAccount = new Account();
                    try {
                        bigchainDBApi.setConfig();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        userAccount.setPrivateKey(userAccount.privateKeyFromHex(user.getSecret()));
                        Log.d("connection test","Success");
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                        Log.d("Connection Failure", "Fail");
                    }

                    try {
                        userAccount.setPublicKey(userAccount.publicKeyFromHex(user.getEmail()));
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                    privateKey = userAccount.getPrivateKey();
                    publicKey = userAccount.getPublicKey();

                    try {
                        userAccountApi.loadAccount(publicKey.toString(),privateKey.toString());
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }


                    Intent intent = new Intent(MakeAccount.this, ProfilePage.class);
                    MakeAccount.this.startActivity(intent); // startActivity allow you to Profile Page
                    MakeAccount.this.finish();
                }

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






    private String hashData(String data){

        TextInputLayout first = (TextInputLayout) findViewById(R.id.first_name);
        TextInputLayout last = (TextInputLayout)findViewById(R.id.last_name);
        EditText e = (EditText)findViewById(R.id.email);  //email
        EditText p = (EditText) findViewById(R.id.user_pin); //pin
        EditText cPin = (EditText) findViewById(R.id.confirm_pin);
        EditText secret = (EditText) findViewById(R.id.secret_text);

        String UFirst = first.getEditText().getText().toString();
        String ULast = last.getEditText().getText().toString();
        String UEmail = e.getText().toString();
        String UPin = p.getText().toString();
        String confirmPin = cPin.getText().toString();
        String userSecret = secret.getText().toString();




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

        String to_hash = data;

        String cp_hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("Sha-256");
            byte[] user_hash = md.digest(to_hash.getBytes());
            BigInteger big_num = new BigInteger(1, user_hash);
            String hashed_val = big_num.toString(16);

            while (hashed_val.length() < 32) {
                hashed_val = "O" + hashed_val;
            }
//            if(MakeAccount.this.bigchainDB.contains(hashed_val)){
//                existToast.show();
//                return null;
//            }
//            else{
//                added_to_DB.show(); //toast
//            }

            cp_hash = hashed_val;
            user = new User(UFirst,ULast,UEmail,UPin,cp_hash,userSecret);
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