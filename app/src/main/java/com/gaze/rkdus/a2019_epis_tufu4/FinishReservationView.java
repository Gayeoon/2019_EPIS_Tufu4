package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  FinishReservationView class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class FinishReservationView extends LinearLayout  {
    TextView owner, animal;
    ImageView call;

    public FinishReservationView(Context context) {
        super(context);

        init(context);
    }

    public FinishReservationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.finish_item, this, true);

        owner = (TextView) findViewById(R.id.owner);
        animal = (TextView) findViewById(R.id.animal);


    }

    public void setowner(String n) {
        owner.setText(n);
    }

    public void setanimal(String t) {
        animal.setText(t);
    }
}

