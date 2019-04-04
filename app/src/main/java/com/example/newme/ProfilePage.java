package com.example.newme;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.zxing.Result;

public class ProfilePage  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        MainActivity.Name.setText(MakeAccount.user.firstName + " " + MakeAccount.user.lastName);
        MainActivity.Email.setText(MakeAccount.user.email);

    }
}
