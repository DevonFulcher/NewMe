package com.example.newme;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfilePage  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        Button QRButton = findViewById(R.id.btnQR);
        Button settingsButton = findViewById(R.id.btnSettings);//add settings listener when thats done
        Button voucherButton = findViewById(R.id.btnVoucher);

        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this,QRCode.class);
                ProfilePage.this.startActivity(intent);
            }
        });

        voucherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//work with devon to figure out what this needs to do
                Intent intent = new Intent(ProfilePage.this,MyOwnAdapter.class);
                ProfilePage.this.startActivity(intent);
            }
        });
    }
}
