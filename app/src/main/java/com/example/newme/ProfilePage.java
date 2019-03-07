package com.example.newme;


import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ProfilePage  extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        Button QRButton = (Button)findViewById(R.id.btnQR);
        Button voucherButton = (Button)findViewById(R.id.btnVoucher);
        QRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, QRCode.class);
                ProfilePage.this.startActivity(intent);
            }
        });

        voucherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, VoucherActivity.class);
                ProfilePage.this.startActivity(intent);
            }
        });
    }
}
