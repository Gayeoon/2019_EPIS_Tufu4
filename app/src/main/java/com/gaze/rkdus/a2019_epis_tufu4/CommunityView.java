package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  CommunityView class
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class CommunityView extends LinearLayout {
    TextView title, written, index;

    public CommunityView(Context context) {
        super(context);

        init(context);
    }

    public CommunityView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.community_item, this, true);

        title = (TextView) findViewById(R.id.title);
        written = (TextView) findViewById(R.id.written);
        index = (TextView) findViewById(R.id.index);
    }

    public void setTitle(String n) {
        title.setText(n);
    }

    public void setWritten(String t) {
        written.setText(t);
    }

    public void setIndex(int i) {
        index.setText(i);
    }
}
