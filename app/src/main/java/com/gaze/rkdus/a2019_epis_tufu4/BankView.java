package com.gaze.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BankView extends LinearLayout{
    TextView bank_name;
    ImageView image;

    public BankView(Context context) {
        super(context);

        init(context);
    }

    public BankView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);

    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bank_item, this, true);

        bank_name = (TextView) findViewById(R.id.bank_name);
        image = (ImageView)findViewById(R.id.image);
    }

    public void setBankName(String n) {
        bank_name.setText(n);
    }

    public void setImage(int resId) {
        if(resId == 0){
            image.setVisibility(View.INVISIBLE);
            image.setMaxWidth(3);
        }else {
            image.setImageResource(resId);}
            }
}
