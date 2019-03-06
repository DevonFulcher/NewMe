package com.example.newme;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    public static TextView resultTV;
    Button scanButton;
    RecyclerView voucherRecycler;
    String s1[], s2[];
    MyOwnAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_main);//if user has not made an account, don't display and call LogIn/MakeAccount
        Button loginButton = findViewById(R.id.signIn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                switch(v.getId()){
                    case R.id.btnVoucher:
                        voucherRecycler = (RecyclerView) findViewById(R.id.voucherRecycler);
                        s1 = getResources().getStringArray(R.array.person);
                        s2 = getResources().getStringArray(R.array.description);
                        ad =  new MyOwnAdapter(MainActivity.this, s1, s2);

                        voucherRecycler.setAdapter(ad);
                        voucherRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        break;
                    case R.id.btnQR:
                        startActivity(new Intent(getApplicationContext(), QRCode.class));
                        break;

                }
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                MainActivity.this.startActivity(intent); // startActivity allow you to move
            }
        });

        super.onCreate(savedInstanceState);

    }
}