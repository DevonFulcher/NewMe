package com.example.newme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView greeting;
    Button click;
    QRCode test = new QRCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        greeting = (TextView)findViewById(R.id.tvGreeting);
        click = (Button)findViewById(R.id.btnClick);

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greeting.setVisibility(View.VISIBLE);
                greeting.append(test.getVar());
            }
        });
    }
}
