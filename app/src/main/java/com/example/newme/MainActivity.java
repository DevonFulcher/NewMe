package com.example.newme;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    public static TextView resultTV;
    Button scanButton;


    RecyclerView voucherRecycler;
    String s1[], s2[];
    MyOwnAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTV = (TextView)findViewById(R.id.tvResult);
        scanButton = (Button)findViewById(R.id.btnQRStart);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QRCode.class));
            }
        });
        setContentView(R.layout.profile_page);
        setContentView(R.layout.activity_main);
        voucherRecycler = (RecyclerView) findViewById(R.id.voucherRecycler);
        s1 = getResources().getStringArray(R.array.person);
        s2 = getResources().getStringArray(R.array.description);
        ad =  new MyOwnAdapter(this, s1, s2);

        voucherRecycler.setAdapter(ad);
        voucherRecycler.setLayoutManager(new LinearLayoutManager(this));
        Button loginButton = findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub

            Intent intent = new Intent(MainActivity.this,
                    LoginActivity.class);
            MainActivity.this.startActivity(intent); // startActivity allow you to move
        }
        });
    }
}