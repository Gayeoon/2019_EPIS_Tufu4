package com.example.rkdus.a2019_epis_tufu4;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import android.widget.Toast;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<SearchResultData> listData;
    private Context context;

    public SearchListAdapter(ArrayList<SearchResultData> arrayList) {
        listData = arrayList;
    }

    public void resetAll(ArrayList<SearchResultData> newArrayList) { ;

        this.listData = null;
        this.listData = newArrayList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate.
        // return 인자는 ViewHolder.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "병원 이름 : " + listData.get(position).getHOSPITAL_NAME(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(SearchResultData searchResultData) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(searchResultData);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView hospitalNameText;
        private TextView ceoNameText;
        private ImageView signUpAppImage;

        ItemViewHolder(View itemView) {
            super(itemView);

            hospitalNameText = itemView.findViewById(R.id.hospitalNameTextView);
            ceoNameText = itemView.findViewById(R.id.ceoNameTextView);
            signUpAppImage = itemView.findViewById(R.id.imageView);
        }

        void onBind(SearchResultData data) {
            hospitalNameText.setText(data.getHOSPITAL_NAME());
            ceoNameText.setText(data.getCEO_NAME());
            // signUpAppImage.setImageResource(data.getResId());
        }
    }
}