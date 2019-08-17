package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  StatusView
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class StatusView extends LinearLayout {
    TextView owner, animal;
    ImageView state;

    public StatusView(Context context) {
        super(context);

        init(context);
    }

    public StatusView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.state_item, this, true);

        owner = (TextView) findViewById(R.id.owner);
        animal = (TextView) findViewById(R.id.animal);
        state = (ImageView)findViewById(R.id.stateNow);

    }

    public void setowner(String n) {
        owner.setText(n);
    }

    public void setanimal(String t) {
        animal.setText(t);
    }

    public void setState(int s){
        if (s == 0){
            state.setVisibility(View.GONE);
        }
        else if (s == 1){
            state.setBackgroundResource(R.drawable.state_new);
        }else if (s == 2){
            state.setBackgroundResource(R.drawable.state_wait);
        }else if (s == 3){
            state.setBackgroundResource(R.drawable.state_wait);
        }else if (s == 4){
            state.setBackgroundResource(R.drawable.state_finish);
        }
    }
}

