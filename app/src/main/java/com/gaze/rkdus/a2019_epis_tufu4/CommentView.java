package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 *  CommentView
 *  Copyright 2019, 김가연. All rights reserved.
 */

public class CommentView extends LinearLayout {
    TextView id, date, comment;

    public CommentView(Context context) {
        super(context);

        init(context);
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.comment_item, this, true);

        date = (TextView) findViewById(R.id.date);
        id = (TextView) findViewById(R.id.id);
        comment = (TextView) findViewById(R.id.comment);
    }

    public void setId(String n) {
        id.setText(n);
    }

    public void setDate(String t) {
        date.setText(t);
    }

    public void setComment(String i) {
        comment.setText(i);
    }
}
