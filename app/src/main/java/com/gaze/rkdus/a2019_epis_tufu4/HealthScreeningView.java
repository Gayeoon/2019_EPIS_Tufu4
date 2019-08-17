package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HealthScreeningView extends LinearLayout {
    TextView date, time;

    public HealthScreeningView(Context context) {
        super(context);

        init(context);
    }

    public HealthScreeningView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.health_item, this, true);

        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
    }


    public void setDate(String t) {
        date.setText(t);
    }

    public void setTime(String i) {
        time.setText(i);
    }
}
