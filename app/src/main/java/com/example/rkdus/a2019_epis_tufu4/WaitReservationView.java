package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  WaitReservationView
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class WaitReservationView extends LinearLayout {
    TextView owner, animal;
    ImageView call;

    public WaitReservationView(Context context) {
        super(context);

        init(context);
    }

    public WaitReservationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.wait_item, this, true);

        owner = (TextView) findViewById(R.id.owner);
        animal = (TextView) findViewById(R.id.animal);
        call = (ImageView)findViewById(R.id.call);

    }

    public void setowner(String n) {
        owner.setText(n);
    }

    public void setanimal(String t) {
        animal.setText(t);
    }

    public void setCall(boolean c){
        if(c){
            call.setBackgroundResource(R.drawable.wait_finish);
        }else{
            call.setBackgroundResource(R.drawable.wait_call);
        }
    }

}
