package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  NewReservationView
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class NewReservationView extends LinearLayout {
    TextView name, time;

    public NewReservationView(Context context) {
        super(context);

        init(context);
    }

    public NewReservationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.new_item, this, true);

        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.date);
    }

    public void setName(String n) {
        name.setText(n);
    }

    public void setTime(String t) {
        time.setText(t);
    }

}
