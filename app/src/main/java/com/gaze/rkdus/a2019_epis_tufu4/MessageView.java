package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageView extends LinearLayout {
    TextView textView;

    public MessageView(Context context) {
        super(context);

        init(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.alarm_item, this, true);

        textView = (TextView) findViewById(R.id.name);
    }

    public void setName(String name) {
        textView.setText(name);
    }
}
