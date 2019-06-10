package com.example.rkdus.a2019_epis_tufu4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

public class ReservationActivity extends AppCompatActivity {

    ImageButton call, check;
    String tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        tel = "tel:01044910778";

        startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));

    }
}

