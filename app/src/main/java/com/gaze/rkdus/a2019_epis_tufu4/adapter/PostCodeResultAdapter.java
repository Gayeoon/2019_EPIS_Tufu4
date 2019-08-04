package com.gaze.rkdus.a2019_epis_tufu4.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaze.rkdus.a2019_epis_tufu4.R;
import com.gaze.rkdus.a2019_epis_tufu4.item.PostCodeItem;

import java.util.ArrayList;

/*
SearchActivity RecyclerView의 Adapter
 */
public class PostCodeResultAdapter extends RecyclerView.Adapter<PostCodeResultAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<PostCodeItem> listData;
    private Context context;

    //아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view,int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }


    public PostCodeResultAdapter(ArrayList<PostCodeItem> arrayList) {
        listData = arrayList;
    }

    public void resetAll(ArrayList<PostCodeItem> newArrayList) {

        this.listData = null;
        this.listData = newArrayList;
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        private TextView postcdText;
        private TextView addressText;
        private TextView jibunText;

        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            postcdText = itemView.findViewById(R.id.postcdText);
            addressText = itemView.findViewById(R.id.addressText);
            jibunText = itemView.findViewById(R.id.addressjibunText);

        }

        void onBind(PostCodeItem data) {
            postcdText.setText(data.getPostcd());
            addressText.setText(data.getAddress());
            jibunText.setText(data.getAddrjibun());
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate.
        // return 인자는 ViewHolder.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postcode_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClick != null) {
                    itemClick.onClick(v, position);
                }
            }
        });

        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(PostCodeItem postCodeItem) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(postCodeItem);
    }
}