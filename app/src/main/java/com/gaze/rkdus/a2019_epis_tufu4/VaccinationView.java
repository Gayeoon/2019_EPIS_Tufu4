package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  VaccinationView class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class VaccinationView extends LinearLayout {
    TextView vaccine_name, date, time;

    public VaccinationView(Context context) {
        super(context);

        init(context);
    }

    public VaccinationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.vaccination_item, this, true);

        date = (TextView) findViewById(R.id.date);
        vaccine_name = (TextView) findViewById(R.id.vaccine_name);
        time = (TextView) findViewById(R.id.time);
    }

    public void setVaccine_name(String n) {
        vaccine_name.setText(n);
    }

    public void setDate(String t) {
        date.setText(t);
    }

    public void setTime(String i) {
        time.setText(i);
    }
}
