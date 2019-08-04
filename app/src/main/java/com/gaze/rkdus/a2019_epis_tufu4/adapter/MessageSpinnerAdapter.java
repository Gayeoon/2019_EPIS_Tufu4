package com.gaze.rkdus.a2019_epis_tufu4.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gaze.rkdus.a2019_epis_tufu4.R;

import java.util.ArrayList;
import java.util.List;

/*
예약하기 화면에서 사용하는 스피너 미리보기(hint) 설정
 */
public class MessageSpinnerAdapter extends ArrayAdapter<String> {

    public MessageSpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if(position == getCount()) {
            ((TextView) v.findViewById(R.id.text1)).setText("");
            ((TextView) v.findViewById(R.id.text1)).setHint(getItem(getCount()));
        }
        return v;
    }

    @Override
    public int getCount() {
        return super.getCount() - 1;
    }
}
