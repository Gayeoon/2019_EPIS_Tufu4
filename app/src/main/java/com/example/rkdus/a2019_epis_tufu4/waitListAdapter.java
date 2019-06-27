package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class waitListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<waitItemData> m_oData = null;
    private int nListCnt = 0;

    public waitListAdapter(ArrayList<waitItemData> _oData)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.wait_item, parent, false);
        }

        TextView oTextOwner = (TextView) convertView.findViewById(R.id.owner);
        TextView oTextAnimal = (TextView) convertView.findViewById(R.id.animal);
        ImageView oBtn = (ImageView) convertView.findViewById(R.id.call);

        oTextOwner.setText(m_oData.get(position).strOwner);
        oTextAnimal.setText(m_oData.get(position).strAnimal);
        oBtn.setOnClickListener(m_oData.get(position).onClickListener);

        convertView.setTag(""+position);
        return convertView;
    }
}

