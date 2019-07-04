package com.example.rkdus.a2019_epis_tufu4;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.rkdus.a2019_epis_tufu4.MyPageActivity.CHECK_RESERVATION;

/*
MyPageActivity RecyclerView의 Adapter
 */
public class MyReservationListAdapter extends RecyclerView.Adapter<MyReservationListAdapter.ItemViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<MyReservationData> listData;
    private Context context;

    //아이템 클릭시 실행 함수
    private ItemClick itemClick;
    public interface ItemClick {
        public void onClick(View view, int position);
    }

    //아이템 클릭시 실행 함수 등록 함수
    public void setItemClick(ItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public MyReservationListAdapter(ArrayList<MyReservationData> arrayList, Context context) {
        this.context = context;
        listData = arrayList;
    }

    public void resetAll(ArrayList<MyReservationData> newArrayList) { ;

        this.listData = null;
        this.listData = newArrayList;
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {
        View view;
        private TextView hospitalNameText;
        private TextView typeText;
        private TextView dateText;
        private ImageView checkRegistrationImage;

        ItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            hospitalNameText = itemView.findViewById(R.id.myRegiDataHospitalName);
            typeText = itemView.findViewById(R.id.myRegiDataType);
            dateText = itemView.findViewById(R.id.myRegiDataDate);
            checkRegistrationImage = itemView.findViewById(R.id.myRegiCheckImage);
        }

        void onBind(MyReservationData data) {
            final MyReservationData resultData = data;
            hospitalNameText.setText(data.getHOSPITAL_NAME());
            typeText.setText(data.getTypeToStr(data.getTYPE()));

            String[] date = data.getASK_DATE().split("\\s");
            dateText.setText(date[0]);
            checkRegistrationImage.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:   // 클릭 시
                            Intent intent = new Intent(context, MessageActivity.class);
                            intent.putExtra("data", (Serializable) resultData);
                            ((Activity) context).startActivityForResult(intent, CHECK_RESERVATION);
                            break;
                        case MotionEvent.ACTION_CANCEL: // 클릭하지 않은 상태 시
                            break;
                    }
                    return true;
                }
            });
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate.
        // return 인자는 ViewHolder.
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reservation_list_item, parent, false);
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

    void addItem(MyReservationData myRegistrationData) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(myRegistrationData);
    }
}