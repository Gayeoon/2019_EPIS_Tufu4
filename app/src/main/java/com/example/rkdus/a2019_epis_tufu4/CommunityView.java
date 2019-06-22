package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommunityView extends LinearLayout {
    TextView name, time, content, comment;
    ImageView profile;

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

        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        content = (TextView) findViewById(R.id.content);
        comment = (TextView) findViewById(R.id.comment);

        profile = (ImageView) findViewById(R.id.profile);

    }

    public void setName(String n) {
        name.setText(n);
    }

    public void setTime(String t) {
        time.setText(t);
    }

    public void setComment(String c) {
        comment.setText(c);
    }

    public void setContent(String c) {
        content.setText(c);
    }
}
