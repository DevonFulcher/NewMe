package com.example.newme;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    RecyclerView voucherRecycler;
    String s1[], s2[];
    MyOwnAdapter ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voucherRecycler = (RecyclerView) findViewById(R.id.voucherRecycler);
        s1 = getResources().getStringArray(R.array.person);
        s2 = getResources().getStringArray(R.array.description);
        ad =  new MyOwnAdapter(this, s1, s2);

        voucherRecycler.setAdapter(ad);
        voucherRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
