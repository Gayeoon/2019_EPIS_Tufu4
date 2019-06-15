package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<SearchItemData> listData = null;
    private int listCount = 0;

    public SearchListAdapter(ArrayList<SearchItemData> data) {
        listData = data;
        listCount = data.size();
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount");
        return listCount;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {  // 아직 존재하지 않은 경우 생성
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.search_list_item, parent, false);
        }

        // 각각의 데이터 뷰에 넣기
        TextView hospitalNameText = (TextView) convertView.findViewById(R.id.hospitalNameTextView);
        TextView ceoNameText = (TextView) convertView.findViewById(R.id.ceoNameTextView);
        TextView phoneNumText = (TextView) convertView.findViewById(R.id.phoneNumTextView);
        TextView signUpAppText = (TextView) convertView.findViewById(R.id.signUpAppTextView);

        hospitalNameText.setText(listData.get(position).getHospitalName());
        ceoNameText.setText(listData.get(position).getCeoName());
        phoneNumText.setText(listData.get(position).getPhoneNum());


        return convertView;
    }
}
